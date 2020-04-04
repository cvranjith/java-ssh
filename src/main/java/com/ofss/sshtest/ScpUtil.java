package com.ofss.sshtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.FileWriter; 

@RestController
public class ScpUtil {

	private UUID uuid;
	private void log(String txt) {
		Date date = new Date();
		System.out.println(new Timestamp(date.getTime())+ " [ " + this.uuid + " ] " + txt);
	}
	
	@PostMapping("/scp")
	public SshOutput doScp(@RequestBody ScpInput scpInput) {

		this.uuid = UUID.randomUUID();
		
		log("SCP Request "+ scpInput.getHostName());
		log("SCP Request "+ scpInput.getLocalfileName());
		log("SCP Request "+ scpInput.getRemotefileName());

		
		SshOutput output = new SshOutput();
		output.setHostName(scpInput.getHostName());
		

		if (scpInput.getDoNotOverWrite() == true) {
			//System.out.println("Check file exists or not");
			SshInput sshinp = new SshInput
					(scpInput.getPort(), scpInput.getHostName(), scpInput.getUsr(), scpInput.getPwd(), "ls "+scpInput.getRemotefileName(), "");
			SshUtil sshutil = new SshUtil();
			SshOutput sshout = sshutil.ssh(sshinp);
			if (sshout.getExitStatus() == 0) {
				log("file Already Exists !!");
				output.setOutput("File Already Exists. Not transferring " + scpInput.getRemotefileName());
				output.setExitStatus(0);
				return output;
			}
		}
		
		if (scpInput.getFileContent() == "") {
		}
		else if (scpInput.getFileContent() == null) {
		}
		else if	(scpInput.getFileContent().isEmpty()) {
		}
		else {
			try {
			FileWriter fw=new FileWriter(scpInput.getLocalfileName());    
	           fw.write(scpInput.getFileContent());    
	           fw.close();    
	          }catch(Exception e)
			{System.out.println(e);
			log(e.getMessage());
	    	  output.setErr("Write File Failed " +scpInput.getLocalfileName() + " " + e.getMessage());
	    	  output.setExitStatus(1);
	    	  return output;
			}    
	          //System.out.println("Success...");   
			
		}
		
	    FileInputStream fis=null;
	    try{

	      String lfile=scpInput.getLocalfileName();
	      String rfile=scpInput.getRemotefileName();
	      java.util.Properties config = new java.util.Properties(); 
	      config.put("StrictHostKeyChecking", "no");
	      JSch jsch=new JSch();
	      Session session=jsch.getSession(scpInput.getUsr(), scpInput.getHostName(), scpInput.getPort());
			session.setPassword(scpInput.getPwd());
			session.setConfig(config);
			session.connect();
	      boolean ptimestamp = true;
	      rfile=rfile.replace("'", "'\"'\"'");
	      rfile="'"+rfile+"'";
	      String command="scp " + (ptimestamp ? "-p" :"") +" -t "+rfile;
	      Channel channel=session.openChannel("exec");
	      ((ChannelExec)channel).setCommand(command);

	      output.setCommand(command);
	      
	      OutputStream out=channel.getOutputStream();
	      InputStream in=channel.getInputStream();

	      channel.connect();

	      if(checkAck(in)!=0){
	    	  //return "1";
	    	  output.setErr("Connect Failed");
	    	  output.setExitStatus(1);
	    	  return output;
	      }

	      File _lfile = new File(lfile);

	      if(ptimestamp){
	        command="T"+(_lfile.lastModified()/1000)+" 0";
	        command+=(" "+(_lfile.lastModified()/1000)+" 0\n"); 
	        out.write(command.getBytes()); out.flush();
	        if(checkAck(in)!=0){
	        	//return "2";
	        	
		    	  output.setErr("Check Ack 1 Failed");
		    	  output.setExitStatus(2);
		    	  return output;
	        }
	      }
	      long filesize=_lfile.length();
	      command="C0644 "+filesize+" ";
	      if(lfile.lastIndexOf('/')>0){
	        command+=lfile.substring(lfile.lastIndexOf('/')+1);
	      }
	      else{
	        command+=lfile;
	      }
	      command+="\n";
	      out.write(command.getBytes()); out.flush();
	      if(checkAck(in)!=0){
	    	  //return "3";
	    	  
	    	  output.setErr("Check Ack 2 Failed");
	    	  output.setExitStatus(3);
	    	  return output;
	    	  
	      }
	      fis=new FileInputStream(lfile);
	      byte[] buf=new byte[1024];
	      while(true){
	        int len=fis.read(buf, 0, buf.length);
		if(len<=0) break;
	        out.write(buf, 0, len); //out.flush();
	      }
	      fis.close();
	      fis=null;
	      // send '\0'
	      buf[0]=0; out.write(buf, 0, 1); out.flush();
	      if(checkAck(in)!=0){
		//System.exit(0);
	    	  //return "4";
	    	  
	    	  output.setErr("Check Ack 3 Failed");
	    	  output.setExitStatus(4);
	    	  return output;
	    	  
	      }
	      out.close();

	      channel.disconnect();
	      session.disconnect();
    	  
	      
		  log("SCP Done " + scpInput.getRemotefileName());
			
    	  output.setExitStatus(0);
    	  return output;
    	  
	    }
	    catch(Exception e){
	      System.out.println(e);
	      try{if(fis!=null)fis.close();}catch(Exception ee){}
	      //return "9";
    	  output.setErr("Exception " + e.getMessage());
    	  output.setExitStatus(9);
    	  return output;
	    }
	  }

	  static int checkAck(InputStream in) throws IOException{
	    int b=in.read();
	    if(b==0) return b;
	    if(b==-1) return b;

	    if(b==1 || b==2){
	      StringBuffer sb=new StringBuffer();
	      int c;
	      do {
		c=in.read();
		sb.append((char)c);
	      }
	      while(c!='\n');
	      if(b==1){ // error
		System.out.print(sb.toString());
	      }
	      if(b==2){ // fatal error
		System.out.print(sb.toString());
	      }
	    }
	    return b;
	  }
	  
	  @RequestMapping("/scp")
		public SshOutput scp(
				@RequestParam String localfileName,
				@RequestParam String remoteHost,
				@RequestParam int remotePort,
				@RequestParam String remoteUser,
				@RequestParam String remoteUserPass,
				@RequestParam String remotefileName
				){
				return doScp(new ScpInput(remotePort, remoteHost, remoteUser, remoteUserPass, localfileName, remotefileName,"",false));
		}
		
}
