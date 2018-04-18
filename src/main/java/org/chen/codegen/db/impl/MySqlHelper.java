package org.chen.codegen.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.chen.codegen.db.DaoHelper;
import org.chen.codegen.db.IDbHelper;
import org.chen.codegen.db.MapCmd;
import org.chen.codegen.exception.CodegenException;
import org.chen.codegen.model.ColumnModel;
import org.chen.codegen.model.TableModel;
import org.chen.codegen.util.StringUtil;

public class MySqlHelper implements IDbHelper {
	private String sqlColumns = "select * from information_schema.columns where table_schema=DATABASE() and table_name='%s' ";
	private String sqlComment = "select table_name,table_comment  from information_schema.tables t where t.table_schema=DATABASE() and table_name='%s' ";
	private String sqlAllTable = "select table_name,table_comment from information_schema.tables t where t.table_schema=DATABASE()";
	private String url;
	private String username;
	private String password;

	public MySqlHelper() throws CodegenException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException classnotfoundexception) {
			throw new CodegenException("找不到mysql驱动!", classnotfoundexception);
		}
	}

	public void setUrl(String url, String s1, String s2) {
		this.url = url;
		this.username = s1;
		this.password = s2;
	}

	public TableModel getByTable(String s) throws CodegenException {
		TableModel tablemodel = getTableModel(s);
		List<ColumnModel> list = getColumnsByTable(s);
		tablemodel.setColumnList(list);
		return tablemodel;
	}

	private List<ColumnModel> getColumnsByTable(String s) throws CodegenException {
		DaoHelper<ColumnModel> daohelper = new DaoHelper<ColumnModel>(url, username, password);
		String s1 = String.format(sqlColumns, s);
		return daohelper.queryForList(s1, new MySqlMapCmd<ColumnModel>());
	}

	private TableModel getTableModel(String s) throws CodegenException {
		TableModel tablemodel = new TableModel();
		DaoHelper<String> daohelper = new DaoHelper<String>(url, username, password);
		String s1 = String.format(sqlComment, s);
		String s2 = daohelper.queryForObject(s1, new MapCmd<String>() {
			public String getObjecFromRs(ResultSet resultset) throws SQLException {
				return resultset.getString("table_comment");
			}
		});
		tablemodel.setTableName(s);
		if (StringUtil.isEmpty(s2))
			s2 = s;
		if (s2.startsWith("InnoDB free")) {
			s2 = s;
			tablemodel.setTabComment(s2);
		} else {
			if (s2.indexOf(";") != -1) {
				int i = s2.indexOf(";");
				s2 = s2.substring(0, i);
			}
			String as[] = s2.split("\n");
			tablemodel.setTabComment(as[0]);
		}
		return tablemodel;
	}

	public List<String> getAllTable() throws CodegenException {
		DaoHelper<String> daohelper = new DaoHelper<>(url, username, password);
		List<String> list = daohelper.queryForList(sqlAllTable, new MapCmd<String>() {
			public String getObjecFromRs(ResultSet resultset) throws SQLException {
				return resultset.getString("table_name");
			}
		});
		return list;
	}
}