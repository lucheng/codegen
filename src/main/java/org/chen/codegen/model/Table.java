package org.chen.codegen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
	private String tableName;
	private Map<String, String> variable = new HashMap<>();
	private List<SubTable> subtable = new ArrayList<>();

	public Table(String paramString) {
		this.tableName = paramString;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String paramString) {
		this.tableName = paramString;
	}

	public void addSubTable(String paramString1, String paramString2, Map<String, String> paramMap) {
		SubTable localSubTable = new SubTable(paramString1, paramString2, paramMap);
		this.subtable.add(localSubTable);
	}

	public Map<String, String> getVariable() {
		return this.variable;
	}

	public void setVariable(Map<String, String> paramMap) {
		this.variable = paramMap;
	}

	public List<SubTable> getSubtable() {
		return this.subtable;
	}

	public void setSubtable(List<SubTable> paramList) {
		this.subtable = paramList;
	}
}