package org.chen.codegen.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.chen.codegen.db.MapCmd;
import org.chen.codegen.model.ColumnModel;
import org.chen.codegen.util.StringUtil;

public class MySqlMapCmd<T> implements MapCmd<ColumnModel> {
	public MySqlMapCmd() {
	}

	public ColumnModel getObjecFromRs(ResultSet resultset) throws SQLException {
		ColumnModel columnmodel = new ColumnModel();
		String s = resultset.getString("column_name");
		String s1 = resultset.getString("column_key");
		long l = resultset.getLong("character_octet_length");
		String s2 = resultset.getString("is_nullable");
		boolean flag = s2.equals("YES");
		int i = resultset.getInt("numeric_precision");
		int j = resultset.getInt("NUMERIC_scale");
		String s3 = resultset.getString("column_comment");
		s3 = StringUtil.isEmpty(s3) ? s : s3;
		boolean flag1 = s1.equals("PRI");
		String s4 = resultset.getString("data_type");
		String columnType = resultset.getString("column_type");
		String s5 = getJavaType(s4, i, j, columnType);
		String s6 = getDisplayDbType(s4, l, i, j);
		String as[] = s3.split("\n");
		columnmodel.setColumnName(s);
		columnmodel.setIsNotNull(!flag);
		columnmodel.setPrecision(i);
		columnmodel.setScale(j);
		columnmodel.setLength(l);
		columnmodel.setComment(as[0]);
		columnmodel.setIsPK(flag1);
		columnmodel.setColDbType(s4);
		columnmodel.setColType(s5);
		columnmodel.setDisplayDbType(s6);
		return columnmodel;
	}

	private String getDisplayDbType(String s, long l, int i, int j) {
		if (s.equals("varchar"))
			return (new StringBuilder()).append("varchar(").append(l).append(")").toString();
		if (s.equals("decimal"))
			return (new StringBuilder()).append("decimal(").append(i).append(",").append(j).append(")").toString();
		else
			return s;
	}

	private String getJavaType(String s, int i, int j, String columnType) {
		if (s.equals("bigint"))
			return "Long";
		if (s.equals("int") || s.equals("bit"))
			return "Integer";
		if (s.equals("tinyint") && "tinyint(1)".equals(columnType))
			return "Boolean";
		if (s.equals("tinyint") || s.equals("smallint"))
			return "Integer";
		if (s.equals("varchar") || s.endsWith("text"))
			return "String";
		if (s.equals("varchar") || s.endsWith("text"))
			return "String";
		if (s.equals("double"))
			return "Double";
		if (s.equals("float"))
			return "Float";
		if (s.endsWith("blob"))
			return "byte[]";
		if (s.equals("decimal"))
			if (j == 0) {
				if (i <= 10)
					return "Integer";
				else
					return "Long";
			} else {
				return "Double";
			}
		if (s.startsWith("date"))
			return "LocalDateTime";
		else
			return s;
	}
}
