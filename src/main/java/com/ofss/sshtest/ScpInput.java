package com.ofss.sshtest;

public class ScpInput {

	Integer port;
	String 	hostName;
	String 	usr;
	String 	pwd;
	String 	localfileName;
	String 	remotefileName;
	String 	fileContent;
	Boolean	doNotOverWrite;

	
	
	public ScpInput() {
		super();
		// TODO Auto-generated constructor stub
	}



	public ScpInput(Integer port, String hostName, String usr, String pwd, String localfileName, String remotefileName,
			String fileContent, Boolean doNotOverWrite) {
		super();
		this.port = port;
		this.hostName = hostName;
		this.usr = usr;
		this.pwd = pwd;
		this.localfileName = localfileName;
		this.remotefileName = remotefileName;
		this.fileContent = fileContent;
		this.doNotOverWrite = doNotOverWrite;
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



	public String getLocalfileName() {
		return localfileName;
	}



	public void setLocalfileName(String localfileName) {
		this.localfileName = localfileName;
	}



	public String getRemotefileName() {
		return remotefileName;
	}



	public void setRemotefileName(String remotefileName) {
		this.remotefileName = remotefileName;
	}



	public String getFileContent() {
		return fileContent;
	}



	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}



	public Boolean getDoNotOverWrite() {
		return doNotOverWrite;
	}



	public void setDoNotOverWrite(Boolean doNotOverWrite) {
		this.doNotOverWrite = doNotOverWrite;
	}



	
	
	

}
