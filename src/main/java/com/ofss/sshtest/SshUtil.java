package com.ofss.sshtest;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


@RestController


public class SshUtil {
	
	
	@Value("${ssh.user}")
	private String sshuser;
	@Value("${ssh.password}")
	private String sshpassword;
	@Value("${ssh.port}")
	private int sshport;
	
	
	private UUID uuid;
	private void log(String txt) {
		Date date = new Date();
		System.out.println(new Timestamp(date.getTime())+ " [ " + this.uuid + " ] " + txt);
	}
	
	SshOutput getSshOutput (SshInput sshInput) {

		this.uuid = UUID.randomUUID();
	    log("SSH to host " + sshInput.getHostName());
	    log("Command " + sshInput.getCommand());
	    
		String output ="";
		int status=0;
		String err="";

	    try{
	    	
	    	java.util.Properties config = new java.util.Properties(); 
	    	config.put("StrictHostKeyChecking", "no");
	    	JSch jsch = new JSch();
	    	Session session=jsch.getSession(sshInput.getUsr(), sshInput.getHostName(), sshInput.getPort());
	    	
	    	//Session session=jsch.getSession();
	    	
	    	session.setPassword(sshInput.getPwd());
	    	session.setConfig(config);
	    	session.connect();
	    	
  
            
	    	Channel channel=session.openChannel("exec");
	    	
	    	//if (sudo) {
		    //     ((ChannelExec) channel).setCommand("sudo -S -p '' " + cmd);
		            
	    	//} else {
		        ((ChannelExec)channel).setCommand(sshInput.getCommand());
	    	//}
	    	
	    	OutputStream out = channel.getOutputStream();
	    	channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        channel.connect();
	        

	            out.write((sshInput.getPwd() + "\n").getBytes());
	            out.flush();

	        
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            output = output + new String(tmp, 0, i);
	            
	            
	            
	            
	          }
	          if(channel.isClosed()){
	        	status = channel.getExitStatus();
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
	    }catch(Exception e){
	    	e.printStackTrace();
	    	//output = output + e;
	    	err = e.toString();
	    }
	    log("status = "+status);
	    log("err = "+err);
	    log("output = "+output);
	    return new SshOutput(sshInput.getHostName(),sshInput.getCommand(),status,err,output);
	}
	
	
	@RequestMapping("/sshJson/{hostName}/{cmd}")
	public SshOutput sshJson(@PathVariable String hostName, @PathVariable String cmd) {
		return getSshOutput (new SshInput(sshport,hostName,sshuser,sshpassword,cmd,""));
	}
	@RequestMapping("/ssh/{hostName}/{cmd}")
	public String ssh(@PathVariable String hostName, @PathVariable String cmd) {
		return getSshOutput (new SshInput(sshport,hostName,sshuser,sshpassword,cmd,"")).getOutput();
	    //return getSshOutput(hostName,cmd,false).getOutput();
	}
	@RequestMapping("/sudoJson/{hostName}/{cmd}")
	public SshOutput sudoJson(@PathVariable String hostName, @PathVariable String cmd) {
		return getSshOutput (new SshInput(sshport,hostName,sshuser,sshpassword,"sudo -S -p '' " +cmd,""));
		//return getSshOutput(hostName,"sudo -S -p '' " +cmd,true);
    }
	@RequestMapping("/sudo/{hostName}/{cmd}")
	public String sudo(@PathVariable String hostName, @PathVariable String cmd) {
		
		return getSshOutput (new SshInput(sshport,hostName,sshuser,sshpassword,"sudo -S -p '' " +cmd,"")).getOutput();
		//return getSshOutput(hostName,"sudo -S -p '' " +cmd,true).getOutput();
    }
	@RequestMapping("/dockerps/{hostName}/{all}")
	public String dockerps(@PathVariable String hostName, @PathVariable String all) {
		
		return getSshOutput (new SshInput(sshport,hostName,sshuser,sshpassword, "sudo -S -p '' curl --unix-socket /run/docker.sock -X GET \"http://localhost/containers/json?all="+all+"\"","")).getOutput();
		
		//return getSshOutput(hostName,"sudo -S -p '' curl --unix-socket /run/docker.sock -X GET \"http://localhost/containers/json?all="+all+"\"",true).getOutput();
    }
	@PostMapping("/ssh")
	public SshOutput ssh(@RequestBody SshInput sshInput) {
		
		System.out.println(sshInput.toString());
		return getSshOutput(sshInput);
	}
	
	@RequestMapping("/ssh")
	public SshOutput sshParam(@RequestParam String hostName, @RequestParam String cmd) {
		return getSshOutput (new SshInput(sshport,hostName,sshuser,sshpassword,cmd,""));
	}
	
	@RequestMapping("/ping")
	public String ping() {
		return "ok";
    }
	
}
