package org.chen.codegen.model;

public class File {
	private String template;
	private String filename;
	private String dir;
	private boolean sub = false;
	private boolean override = false;
	private boolean append = false;
	private String insertTag = "";
	private String startTag = "start{tabname}";
	private String endTag = "end{tabname}";

	public File(String template, String filename, String dir, boolean sub, boolean override, boolean append,
			String insertTag, String startTag, String endTag) {
		super();
		this.template = template;
		this.filename = filename;
		this.dir = dir;
		this.sub = sub;
		this.override = override;
		this.append = append;
		this.insertTag = insertTag;
		this.startTag = startTag;
		this.endTag = endTag;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String paramString) {
		this.template = paramString;
	}

	public boolean isSub() {
		return this.sub;
	}

	public void setSub(boolean paramBoolean) {
		this.sub = paramBoolean;
	}

	public boolean isOverride() {
		return this.override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String paramString) {
		this.filename = paramString;
	}

	public String getDir() {
		return this.dir;
	}

	public void setDir(String paramString) {
		this.dir = paramString;
	}

	public boolean getAppend() {
		return this.append;
	}

	public void setAppend(boolean paramBoolean) {
		this.append = paramBoolean;
	}

	public String getInsertTag() {
		return this.insertTag;
	}

	public void setInsertTag(String paramString) {
		this.insertTag = paramString;
	}

	public String getStartTag() {
		return this.startTag;
	}

	public void setStartTag(String paramString) {
		this.startTag = paramString;
	}

	public String getEndTag() {
		return this.endTag;
	}

	public void setEndTag(String paramString) {
		this.endTag = paramString;
	}
}
