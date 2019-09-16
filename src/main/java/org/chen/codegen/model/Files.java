package org.chen.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class Files {
	private String baseDir = "";
	private List<File> fileList = new ArrayList<>();

	public Files() {
	}

	public String getBaseDir() {
		return this.baseDir;
	}

	public void setBaseDir(String paramString) {
		this.baseDir = paramString;
	}

	public void addFile(String template, String filename, String dir, boolean sub,
			boolean override, boolean append, String insertTag, String startTag,
			String endTag) {
		File file = new File(template, filename, dir, sub, override,
				append, insertTag, startTag, endTag);
		this.fileList.add(file);
	}

	public List<File> getFileList() {
		return this.fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}
}