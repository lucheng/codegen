<#import "function.ftl" as func>
<#assign class=model.variables.class>
<#assign tabcomment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign package=model.variables.package>
<#assign commonList=model.commonList>
<#assign system=vars.system>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign subtables=model.subTableList>
<%--
	time:${date?string("yyyy-MM-dd HH:mm:ss")}
	desc:edit the ${tabcomment}
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 ${tabcomment}</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/js/hotent/CustomValid.js"></script>
	<#if subtables?exists && subtables?size!=0>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/js/hotent/formdata.js"></script>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/js/hotent/subform.js"></script>
	</#if>
	<#if model.variables.flowKey?exists>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/js/hotent/platform/bpm/FlowDetailWindow.js"></script>
	</#if>
	<script type="text/javascript">
		$(function() {
			$("a.save").click(function() {
				$("#${classVar}Form").attr("action","save.ht");
				submitForm();
			});
			<#if model.variables.flowKey?exists>
			$("a.run").click(function() {
				$("#${classVar}Form").attr("action","run.ht");
				submitForm();
			});
			</#if>
		});
		//提交表单
		function submitForm(){
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#${classVar}Form').form();
			frm.ajaxForm(options);
			if(frm.valid()){
				<#if subtables?exists && subtables?size!=0>
				frm.sortList();
				</#if>
				frm.submit();
			}
		}
		
		function showResponse(responseText) {
			var obj = new com.hotent.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.success(obj.getMessage(),"提示信息", function(rtn) {
					if(rtn){
						if(window.opener){
							window.opener.location.reload();
							window.close();
						}else{
							this.close();
							window.location.href="list.ht";
						}
					}
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
	</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
		    <c:choose>
			    <c:when test="<#noparse>${</#noparse>${classVar}.${pkVar} !=null}">
			        <span class="tbar-label">编辑${tabcomment}</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label">添加${tabcomment}</span>
			    </c:otherwise>			   
		    </c:choose>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="#"><span></span>保存</a></div>
				<div class="l-bar-separator"></div>
				<#if model.variables.flowKey?exists>
				<c:if test="<#noparse>${</#noparse>runId==0}">
				<div class="group"><a class="link run"  href="#"><span></span>提交</a></div>
				<div class="l-bar-separator"></div>
				</c:if>
				<c:if test="<#noparse>${</#noparse>runId!=0}">
				<div class="group"><a class="link detail" onclick="FlowDetailWindow({runId:<#noparse>${</#noparse>runId}})"" href="#"><span></span>流程明细</a></div>
				<div class="l-bar-separator"></div>
				</c:if>
				</#if>
				<div class="group"><a class="link back" href="list.ht"><span></span>返回</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="${classVar}Form" method="post" action="save.ht">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
				<#list commonList as col>
				<#assign colName=func.convertUnderLine(col.columnName)>
				<#if (col.colType=="java.util.Date") >
				<tr>
					<th width="20%">${col.comment}: <#if (col.isNotNull) > <span class="required">*</span></#if></th>
					<td><input type="text" id="${colName}" name="${colName}" value="<fmt:formatDate value='<#noparse>${</#noparse>${classVar}.${colName}}' pattern='yyyy-MM-dd'/>" validate="{<#if col.isNotNull>required:true<#else>required:false</#if>,date:true}" class="inputText date"/></td>
				</tr>
				<#else>
				<tr>
					<th width="20%">${col.comment}: <#if (col.isNotNull) > <span class="required">*</span></#if></th>
					<td><input type="text" id="${colName}" name="${colName}" value="<#noparse>${</#noparse>${classVar}.${colName}}" validate="{<#if col.isNotNull>required:true<#else>required:false</#if><#if col.colType=='String' && col.length<1000>,maxlength:${col.length}</#if><#if col.colType=='Integer'|| col.colType=='Long'||col.colType=='Float'>,number:true<#if col.scale!=0>,maxDecimalLen:${col.scale}</#if><#if col.precision!=0>,maxIntLen:${col.precision}</#if> </#if>}" class="inputText"/></td>
				</tr>
				</#if>
				</#list>
			</table>
			<#if subtables?exists && subtables?size!=0>
			<#list subtables as table>
			<table class="table-grid table-list" cellpadding="1" cellspacing="1" type="subtable" formtype="page" id="${table.variables.classVar}">
				<tr>
					<td colspan="${table.columnList?size-1}">
						<div class="group" align="left">
				   			<a id="btnAdd" class="link add">添加</a>
			    		</div>
			    		<div align="center">
						<#if table.tabComment?exists>${table.tabComment}<#else>${table.tableName}</#if>
			    		</div>
		    		</td>
				</tr>
				<tr>
					<#list table.columnList as col>
					<#assign colName=func.convertUnderLine(col.columnName?lower_case)>
					<#assign foreignKey=func.convertUnderLine(table.foreignKey)>
					<#if !(col.isPK)&& colName?lower_case!=foreignKey?lower_case>							                              
					<th>${col.comment}</th>
					</#if>									
					</#list>
				</tr>
				<c:forEach items="<#noparse>${</#noparse>${table.variables.classVar}List}" var="${table.variables.classVar}Item" varStatus="status">
				    <tr type="subdata">
				        <#list table.columnList as col>												
					    <#assign colName=func.convertUnderLine(col.columnName)>
					    <#assign foreignKey=func.convertUnderLine(table.foreignKey)>
					    <#if  !(col.isPK)&&colName?lower_case!=foreignKey?lower_case>
					    <#if (col.colType=="java.util.Date")>
						<td style="text-align: center"><input type="text" name="${colName}" value="<fmt:formatDate value='<#noparse>${</#noparse>${table.variables.classVar}Item.${colName?lower_case}}' pattern='yyyy-MM-dd'/>" validate="{<#if col.isNotNull>required:true<#else>required:false</#if>,date:true}" class="inputText date"/></td>								
					    <#else>
					    <td style="text-align: center"><input type="text" name="${colName}" value="<#noparse>${</#noparse>${table.variables.classVar}Item.${colName}}" validate="{<#if col.isNotNull>required:true<#else>required:false</#if><#if col.colType=='String' && col.length<1000>,maxlength:${col.length}</#if><#if col.colType=='Integer'|| col.colType=='Long'||col.colType=='Float'>,number:true </#if>}" class="inputText"/></td>
					    </#if>
					    </#if>
					    </#list>
				    </tr>
				</c:forEach>
				<tr type="append" style="display:none;">
			        <#list table.columnList as col>												
				    <#assign colName=func.convertUnderLine(col.columnName)>
				    <#assign foreignKey=func.convertUnderLine(table.foreignKey)>
				    <#if  !(col.isPK)&&colName?lower_case!=foreignKey?lower_case>
				    <#if (col.colType=="java.util.Date")>
					<td style="text-align: center"><input type="text" name="${colName}" value="<fmt:formatDate value='<#noparse>${</#noparse>${table.variables.classVar}Item.${colName?lower_case}}' pattern='yyyy-MM-dd'/>" validate="{<#if col.isNotNull>required:true<#else>required:false</#if>,date:true}" class="inputText date"/></td>								
				    <#else>
				    <td style="text-align: center"><input type="text" name="${colName}" value="" validate="{<#if col.isNotNull>required:true<#else>required:false</#if><#if col.colType=='String' && col.length<1000>,maxlength:${col.length}</#if><#if col.colType=='Integer'|| col.colType=='Long'||col.colType=='Float'>,number:true<#if col.scale!=0>,maxDecimalLen:${col.scale}</#if><#if col.precision!=0>,maxIntLen:${col.precision}</#if> </#if>}" class="inputText"/></td>
				    </#if>
				    </#if>
				    </#list>
			    </tr>
		    </table>
			</#list>
			</#if>
			<input type="hidden" name="${pkVar}" value="<#noparse>${</#noparse>${classVar}.${pkVar}<#noparse>}</#noparse>" />					
		</form>
	</div>
</div>
</body>
</html>
