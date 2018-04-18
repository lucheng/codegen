package org.chen.codegen.model;

public class Database
{
  private String dbHelperClass;
  private String url;
  private String username;
  private String password;
  
  public Database(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.dbHelperClass = paramString1;
    this.url = paramString2;
    this.username = paramString3;
    this.password = paramString4;
  }
  
  public String getDbHelperClass()
  {
    return this.dbHelperClass;
  }
  
  public void setDbHelperClass(String paramString)
  {
    this.dbHelperClass = paramString;
  }
  
  public String getUrl()
  {
    return this.url;
  }
  
  public void setUrl(String paramString)
  {
    this.url = paramString;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String paramString)
  {
    this.username = paramString;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
}