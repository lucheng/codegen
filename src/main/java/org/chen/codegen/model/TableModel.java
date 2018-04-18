package org.chen.codegen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TableModel {

	private String tableName;
	private String tabComment;
	private String foreignKey = "";
	private Map<String,String> variables = new HashMap<>();
	private List<ColumnModel> columnList = new ArrayList<>();
	private List<TableModel> subTableList = new ArrayList<>();
	private boolean sub;

	public TableModel() {
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String s) {
		tableName = s;
	}

	public String getTabComment() {
		return tabComment.replaceAll("\r\n", "");
	}

	public void setTabComment(String s) {
		tabComment = s;
	}

	public boolean isSub() {
		return sub;
	}

	public void setSub(boolean flag) {
		sub = flag;
	}

	public boolean getSub() {
		return sub;
	}

	public List<ColumnModel> getColumnList() {
		return columnList;
	}

	public List<ColumnModel> getPkList() {
		ArrayList<ColumnModel> arraylist = new ArrayList<>();
		Iterator<ColumnModel> iterator = columnList.iterator();
		do {
			if (!iterator.hasNext())
				break;
			ColumnModel columnmodel = (ColumnModel) iterator.next();
			if (columnmodel.getIsPK())
				arraylist.add(columnmodel);
		} while (true);
		return arraylist;
	}

	public ColumnModel getPkModel() {
		for (ColumnModel columnmodel: columnList) {
			if (columnmodel.getIsPK())
				return columnmodel;
		}
		return null;
	}

	public List<ColumnModel> getCommonList() {
		ArrayList<ColumnModel> arraylist = new ArrayList<>();
		Iterator<ColumnModel> iterator = columnList.iterator();
		do {
			if (!iterator.hasNext())
				break;
			ColumnModel columnmodel = (ColumnModel) iterator.next();
			if (!columnmodel.getIsPK())
				arraylist.add(columnmodel);
		} while (true);
		return arraylist;
	}

	public void setColumnList(List<ColumnModel> columnList) {
		this.columnList = columnList;
	}

	public List<TableModel> getSubTableList() {
		return subTableList;
	}

	public void setSubTableList(List<TableModel> subTableList) {
		this.subTableList = subTableList;
	}

	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String s) {
		foreignKey = s;
	}

	public Map<String,String> getVariables() {
		return variables;
	}

	public void setVariables(Map<String,String> variables) {
		this.variables = variables;
	}

	public String toString() {
		return (new StringBuilder()).append("TableModel [tableName=").append(tableName).append(", tabComment=")
				.append(tabComment).append(", foreignKey=").append(foreignKey).append(", variables=").append(variables)
				.append(", columnList=").append(columnList).append(", subTableList=").append(subTableList)
				.append(", sub=").append(sub).append("]").toString();
	}
}
