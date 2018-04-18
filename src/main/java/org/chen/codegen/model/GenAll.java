package org.chen.codegen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenAll {
	private String tableNames;
	private List<File> file = new ArrayList<>();

	public GenAll(String paramString) {
		this.tableNames = paramString;
	}

	public String getTableNames() {
		return this.tableNames;
	}

	public void setTableNames(String paramString) {
		this.tableNames = paramString;
	}

	public List<File> getFile() {
		return this.file;
	}

	public void setFile(List<File> paramList) {
		this.file = paramList;
	}

	public class File {
		private String template;
		private String filename;
		private String extName;
		private String dir;
		private String genMode;
		private String sub;
		private Map<String, String> variable = new HashMap<>();

		public File(String template, String filename, String extName, String dir, String genMode) {
			this.template = template;
			this.filename = filename;
			this.extName = extName;
			this.dir = dir;
			this.genMode = genMode;
		}

		public String getSub() {
			return this.sub;
		}

		public void setSub(String paramString) {
			this.sub = paramString;
		}

		public String getTemplate() {
			return this.template;
		}

		public void setTemplate(String paramString) {
			this.template = paramString;
		}

		public String getFilename() {
			return this.filename;
		}

		public void setFilename(String paramString) {
			this.filename = paramString;
		}

		public String getExtName() {
			return this.extName;
		}

		public void setExtName(String paramString) {
			this.extName = paramString;
		}

		public String getDir() {
			return this.dir;
		}

		public void setDir(String paramString) {
			this.dir = paramString;
		}

		public String getGenMode() {
			return this.genMode;
		}

		public void setGenMode(String paramString) {
			this.genMode = paramString;
		}

		public Map<String, String> getVariable() {
			return this.variable;
		}

		public void setVariable(Map<String, String> paramMap) {
			this.variable = paramMap;
		}
	}
}