package org.chen.codegen.model;

import java.util.HashMap;
import java.util.Map;

public class Templates {
	private String basepath;
	private Map<String, String> template = new HashMap<>();

	public Templates(String paramString) {
		this.basepath = paramString;
	}

	public String getBasepath() {
		return this.basepath;
	}

	public void setBasepath(String paramString) {
		this.basepath = paramString;
	}

	public Map<String, String> getTemplate() {
		return this.template;
	}

	public void setTemplate(Map<String, String> paramMap) {
		this.template = paramMap;
	}
}
