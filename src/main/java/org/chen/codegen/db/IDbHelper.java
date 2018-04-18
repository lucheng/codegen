package org.chen.codegen.db;

import java.util.List;

import org.chen.codegen.exception.CodegenException;
import org.chen.codegen.model.TableModel;

public interface IDbHelper {
	
	public void setUrl(String paramString1, String paramString2, String paramString3);

	public TableModel getByTable(String paramString) throws CodegenException;

	public List<String> getAllTable() throws CodegenException;
	
}
