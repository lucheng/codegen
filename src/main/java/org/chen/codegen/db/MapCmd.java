package org.chen.codegen.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MapCmd<T>
{
  public T getObjecFromRs(ResultSet paramResultSet)
    throws SQLException;
}
