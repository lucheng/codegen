package org.chen.codegen.model;

public class ColumnModel
{
	private String columnName;
	private String comment;
	private String colType;
	private String colDbType;
	private boolean isPK;
	private long length;
	private int precision;
	private int scale;
	private int autoGen;
	private boolean isNotNull;
	private String displayDbType;

	public ColumnModel()
	{
		columnName = "";
		comment = "";
		colType = "";
		colDbType = "";
		isPK = false;
		length = 0L;
		precision = 0;
		scale = 0;
		autoGen = 0;
		isNotNull = false;
		displayDbType = "";
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String s)
	{
		columnName = s;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String s)
	{
		comment = s;
	}

	public String getColType()
	{
		return colType;
	}

	public void setColType(String s)
	{
		colType = s;
	}

	public String getColDbType()
	{
		return colDbType;
	}

	public void setColDbType(String s)
	{
		colDbType = s;
	}

	public boolean getIsPK()
	{
		return isPK;
	}

	public void setIsPK(boolean flag)
	{
		isPK = flag;
	}

	public long getLength()
	{
		return length;
	}

	public void setLength(long l)
	{
		length = l;
	}

	public int getPrecision()
	{
		return precision;
	}

	public void setPrecision(int i)
	{
		precision = i;
	}

	public int getScale()
	{
		return scale;
	}

	public void setScale(int i)
	{
		scale = i;
	}

	public int getAutoGen()
	{
		return autoGen;
	}

	public void setAutoGen(int i)
	{
		autoGen = i;
	}

	public boolean getIsNotNull()
	{
		return isNotNull;
	}

	public void setIsNotNull(boolean flag)
	{
		isNotNull = flag;
	}

	public String getDisplayDbType()
	{
		return displayDbType;
	}

	public void setDisplayDbType(String s)
	{
		displayDbType = s;
	}

	public String getDisplay()
	{
		return "";
	}

	public String toString()
	{
		return (new StringBuilder()).append("ColumnModel [columnName=").append(columnName).append(", comment=").append(comment).append(", colType=").append(colType).append(", colDbType=").append(colDbType).append(", isPK=").append(isPK).append(", length=").append(length).append(", precision=").append(precision).append(", scale=").append(scale).append(", autoGen=").append(autoGen).append(", isNotNull=").append(isNotNull).append(", displayDbType=").append(displayDbType).append("]").toString();
	}
}