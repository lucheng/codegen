<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign org=vars.org>
<#assign system=vars.system>
<#assign subtables=model.subTableList>
<#assign classVar=model.variables.classVar>
<#assign table=model.subTableList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk)>
package com.${org}.${system}.service.${package};

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ${org}.common.repository.BaseDao;
import ${org}.common.service.BaseService;
import ${org}.common.entity.${package}.${class};
import ${org}.common.repository.${package}.${class}Dao;

/**
 *<pre>
 * 对象功能:${model.tabComment} Service类
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 *</pre>
 */
@Service
public class ${class}Service extends BaseService<${class}>
{
	@Resource
	private ${class}Dao dao;
	
	@Override
	public BaseDao<${class}> getEntityDao() {
		return dao;
	}
}
