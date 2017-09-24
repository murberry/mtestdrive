package org.jeecgframework.web.demo.entity.test;

import java.io.InputStream;

public class FileMeta {

	private String fileName;
	private String fileSize;
	private String fileType;
	private String originalFileName;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	public InputStream getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
