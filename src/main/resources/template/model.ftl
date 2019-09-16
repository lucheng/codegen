<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign org=vars.org>
<#assign system=vars.system>
<#assign subtables=model.subTableList>
package com.${org}.entity.${package};

import java.io.Serializable;
import lombok.Data;

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
@Data
public class ${class} implements Serializable {
	
	private static final long serialVersionUID = 1L;

<#list model.columnList as col>
	/**
     * ${col.comment}
     */
	private ${col.colType}  ${func.convertUnderLine(col.columnName)};
</#list>
}