package org.chen.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class Files {
	private String baseDir = "";
	private List<File> files = new ArrayList<>();

	public Files() {
	}

	public String getBaseDir() {
		return this.baseDir;
	}

	public void setBaseDir(String paramString) {
		this.baseDir = paramString;
	}

	public void addFile(String paramString1, String paramString2, String paramString3, boolean paramBoolean1,
			boolean paramBoolean2, boolean paramBoolean3, String paramString4, String paramString5,
			String paramString6) {
		File localFile = new File(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2,
				paramBoolean3, paramString4, paramString5, paramString6);
		this.files.add(localFile);
	}

	public List<File> getFiles() {
		return this.files;
	}

	public void setFiles(List<File> paramList) {
		this.files = paramList;
	}
}