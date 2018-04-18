<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign org=vars.org>
<#assign system=vars.system>

<#assign comment=model.tabComment>
<#assign subtables=model.subTableList>
<#assign classVar=model.variables.classVar>
<#assign role=model.variables.role>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
package com.${org}.${system}.web.${role};

import java.util.Map;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ${org}.common.ServiceException;
import ${org}.common.web.BaseController;
import ${org}.common.entity.${package}.${class};
import com.${org}.${system}.service.${package}.${class}Service;

/**
 *<pre>
 * 对象功能:${comment} 控制器类
 <#if vars.company?exists>
 * 开发公司:${vars.company}
 </#if>
 <#if vars.developer?exists>
 * 开发人员:${vars.developer}
 </#if>
 * 创建时间:${date?string("yyyy-MM-dd HH:mm:ss")}
 *</pre>
 */
@Controller("${role}${class}Controller")
@RequestMapping("/${role}/${classVar}/")
public class ${class}Controller extends BaseController
{
	private static final Logger LOG = LoggerFactory.getLogger(${class}Controller.class);
	
	@Resource
	private ${class}Service ${classVar}Service;
	
	/**
	 * 跳转到列表页
	 * @return
	 */
	@RequestMapping("")
	public String index() {
		return "${role}/${classVar}";
	}
	
	/**
	 * 添加或更新${comment}。
	 * @param ${classVar} 添加或更新的实体
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public String save(${class} ${classVar})
	{
		try{
			if(${classVar}.getId()==null|| ${classVar}.getId()==0){
				${classVar}Service.add(${classVar});
				return toJson("添加成功", ${classVar});
			}else{
			    ${classVar}Service.update(${classVar});
			    return toJson("更新成功", ${classVar});
			}
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return toJsonError("系统出现异常:" + e.getMessage(), null);
		}
	}
	
	
	/**
	 * 取得${comment}分页列表
	 * @param paramMap
	 * @return
	 */
	@RequestMapping("list")
	@ResponseBody
	public String list(@RequestParam Map<String,Object> paramMap)
	{	
		try {
			return toJson(${classVar}Service.queryPage(paramMap));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 删除${comment}
	 * @param id
	 * @return
	 */
	@RequestMapping("del")
	@ResponseBody
	public String del(String id)
	{
		try{
			Long[] ids = getLongAryByStr(id);
			${classVar}Service.delByIds(ids);
			return toJson("删除报告操作记录成功", null);
		}catch(ServiceException e){
			return toJson400(e.getMessage(), null);
		}catch(Exception e){
			LOG.info(e.getMessage(),e);
			return toJsonError(e.getMessage(), null);
		}
	}
	
	/**
	 * 取得${comment}明细
	 * @param id   
	 * @return
	 */
	@RequestMapping("get")
	@ResponseBody
	public String get(Long id)
	{
		${class} entity = ${classVar}Service.getById(id);
		if(entity==null){
			return toJson400("不存在该记录",null);
		}
		return toJson(null, entity);
	}
}
