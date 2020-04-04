package com.ofss.sshtest;

public class SshInput {
	Integer port;
	String hostName;
	String usr;
	String pwd;
	String command;
	String refno;
	
	
	
	public SshInput() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SshInput(Integer port, String hostName, String usr, String pwd, String command, String refno) {
		super();
		this.port = port;
		this.hostName = hostName;
		this.usr = usr;
		this.pwd = pwd;
		this.command = command;
		this.refno = refno;
	}
	
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getUsr() {
		return usr;
	}
	public void setUsr(String usr) {
		this.usr = usr;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getRefno() {
		return refno;
	}
	public void setRefno(String refno) {
		this.refno = refno;
	}

	
}
