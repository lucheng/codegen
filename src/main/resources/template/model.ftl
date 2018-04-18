<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign org=vars.org>
<#assign system=vars.system>
<#assign subtables=model.subTableList>
package ${org}.common.entity.${package};

/**
 *<pre>
 * 对象功能:${model.tabComment} Model对象
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 *</pre>
 */
public class ${class}
{
<#list model.columnList as col>
	// ${col.comment}
	<#if (col.colType=="Integer")>
	protected Long  ${func.convertUnderLine(col.columnName)};
	<#else>
	protected ${col.colType}  ${func.convertUnderLine(col.columnName)};
	</#if>
</#list>
<#if subtables?exists && subtables?size!=0>
	<#list subtables as table>
	<#assign vars=table.variables>
	//${table.tabComment}列表
	protected List<${vars.class}> ${vars.classVar}List=new ArrayList<${vars.class}>();
	</#list>
</#if>
<#if model.variables.flowKey?exists>
	protected Long runId=0L;
</#if>
<#list model.columnList as col>
	<#assign colName=func.convertUnderLine(col.columnName)>
	public void set${colName?cap_first}(<#if (col.colType="Integer")>Long<#else>${col.colType}</#if> ${colName}) 
	{
		this.${colName} = ${colName};
	}
	/**
	 * 返回 ${col.comment}
	 * @return
	 */
	public <#if (col.colType="Integer")>Long<#else>${col.colType}</#if> get${colName?cap_first}() 
	{
		return this.${colName};
	}
</#list>
<#if subtables?exists && subtables?size!=0>
<#list subtables as table>
<#assign vars=table.variables>
	public void set${vars.classVar?cap_first}List(List<${vars.class}> ${vars.classVar}List) 
	{
		this.${vars.classVar}List = ${vars.classVar}List;
	}
	/**
	 * 返回 ${table.tabComment}列表
	 * @return
	 */
	public List<${vars.class}> get${vars.classVar?cap_first}List() 
	{
		return ${vars.classVar}List;
	}
</#list>
</#if>
	<#if model.variables.flowKey?exists>
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	</#if>
}