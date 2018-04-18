package org.chen.codegen.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.chen.codegen.exception.CodegenException;

public class DaoHelper<T>
{
  private String url = "";
  private String userName = "";
  private String pwd = "";
  
  public void setUrl(String url)
  {
    this.url = url;
  }
  
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  public void setPwd(String pwd)
  {
    this.pwd = pwd;
  }
  
  public DaoHelper(String paramString1, String paramString2, String paramString3)
  {
    this.url = paramString1;
    this.userName = paramString2;
    this.pwd = paramString3;
  }
  
  public T queryForObject(String paramString, MapCmd<T> paramMapCmd)
    throws CodegenException
  {
    Connection localConnection = null;
    Statement localStatement = null;
    ResultSet localResultSet = null;
    try
    {
      localConnection = DriverManager.getConnection(this.url, this.userName, this.pwd);
      localStatement = localConnection.createStatement();
      localResultSet = localStatement.executeQuery(paramString);
      if (localResultSet.next())
      {
        return paramMapCmd.getObjecFromRs(localResultSet);
      }
      System.out.println("û�е�����:" + paramString);
      return null;
    }
    catch (SQLException localSQLException1)
    {
      throw new CodegenException(localSQLException1);
    }
    finally
    {
      try
      {
        if (localResultSet != null) {
          localResultSet.close();
        }
        if (localStatement != null) {
          localStatement.close();
        }
        if (localConnection != null) {
          localConnection.close();
        }
      }
      catch (SQLException localSQLException4)
      {
        throw new CodegenException(localSQLException4);
      }
    }
  }
  
  public List<T> queryForList(String paramString, MapCmd<T> paramMapCmd)
    throws CodegenException
  {
    Connection localConnection = null;
    Statement localStatement = null;
    ResultSet localResultSet = null;
    List<T> localArrayList = new ArrayList<>();
    try
    {
      localConnection = DriverManager.getConnection(this.url, this.userName, this.pwd);
      localStatement = localConnection.createStatement();
      localResultSet = localStatement.executeQuery(paramString);
      while (localResultSet.next()) {
        localArrayList.add(paramMapCmd.getObjecFromRs(localResultSet));
      }
      return localArrayList;
    }
    catch (SQLException localSQLException2)
    {
      throw new CodegenException(localSQLException2);
    }
    finally
    {
      try
      {
        if (localResultSet != null) {
          localResultSet.close();
        }
        if (localStatement != null) {
          localStatement.close();
        }
        if (localConnection != null) {
          localConnection.close();
        }
      }
      catch (SQLException localSQLException3)
      {
        throw new CodegenException(localSQLException3);
      }
    }
  }
}
