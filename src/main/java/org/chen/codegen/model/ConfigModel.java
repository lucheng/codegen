package org.chen.codegen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigModel {
	private String charset;
	private Database database;
	private Map<String, String> variables = new HashMap<>();
	private Templates templates;
	private List<Table> tables = new ArrayList<>();
	private Files files = new Files();
	private GenAll genAll;

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String paramString) {
		this.charset = paramString;
	}

	public Database getDatabase() {
		return this.database;
	}

	public void setDatabase(Database paramDatabase) {
		this.database = paramDatabase;
	}

	public Map<String, String> getVariables() {
		return this.variables;
	}

	public void setVariables(Map<String, String> paramMap) {
		this.variables = paramMap;
	}

	public Templates getTemplates() {
		return this.templates;
	}

	public void setTemplates(Templates paramTemplates) {
		this.templates = paramTemplates;
	}

	public List<Table> getTables() {
		return this.tables;
	}

	public void setTables(List<Table> paramList) {
		this.tables = paramList;
	}

	public Files getFiles() {
		return this.files;
	}

	public void setFiles(Files paramFiles) {
		this.files = paramFiles;
	}

	public GenAll getGenAll() {
		return this.genAll;
	}

	public void setGenAll(GenAll paramGenAll) {
		this.genAll = paramGenAll;
	}
}
