package com.ofss.sshtest;

public class SshOutput {
	

	String hostName;
	String command;
	Integer exitStatus;
	String err;
	String output;

	
	public SshOutput() {
		super();
	}
	public SshOutput(String hostName, String command, Integer exitStatus, String err, String output) {
		super();
		this.hostName = hostName;
		this.command = command;
		this.exitStatus = exitStatus;
		this.err = err;
		this.output = output;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Integer getExitStatus() {
		return exitStatus;
	}
	public void setExitStatus(Integer exitStatus) {
		this.exitStatus = exitStatus;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

}
