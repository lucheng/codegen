<#import "function.ftl" as func>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign package=model.variables.package>
<#assign comment=model.tabComment>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign commonList=model.commonList>
<#assign pkModel=model.pkModel>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html lang="zh-CN">
  <head>
  	<title>${comment }管理</title>
    <%@include file="/commons/include/get.jsp"%>
  </head>
  <body>
    <h1>${comment }管理</h1>
    <div class="panel panel-default">
	  	<div align="right">
	  		<a href="<%=basePath%>">返回</a>
	  	</div>
	  <!-- Default panel contents -->
	  <div class="panel-heading">${comment }列表</div>
	  <!-- Table -->
		<!-- 新增页面弹出框  start-->
		<form id="myForm" method="post" class="form-horizontal">
		<div id="myModal" class="modal fade" role="dialog">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title">新增/修改${comment }</h4>
		      </div>
		      <div class="modal-body">
						  <#list model.columnList as col>
						  <#assign colName=func.convertUnderLine(col.columnName)>
						  <#if (col.isPK) >
						  <input type="hidden" id="${colName }" name="${colName }" />
						  <#elseif (col.colType=='String' && col.length>1000)>
						  <div class="form-group">
						    <label for="${colName }" class="col-sm-4 control-label">${col.comment}</label>
						    <div class="col-sm-5">
						    <textarea id="${colName }" name="${colName }" rows="10" cols="30" maxlength="${col.length}"></textarea>
						    </div>
						   </div>
						   <#else>
						  <div class="form-group">
						    <label for="${colName }" class="col-sm-4 control-label">${col.comment}</label>
						    <div class="col-sm-5">
						    	<input id="${colName }" name="${colName }" <#if col.colType=='String' && col.length<1000>,maxlength="${col.length}"</#if> class="form-control" type="text"  />
						    </div>
						   </div>
						   </#if>
					      </#list>
		      </div>
		      <div class="modal-footer">
		        <button type="button" id="saveOrUpdate" class="btn btn-default">保存</button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->
		</form>
		<!-- 新增页面弹出框  start-->
	<div class="hidden" id="hidden_filter">
	    <!-- @* 把需要搜索的条件放到hidden里面，在table格式化完成的时候直接调用$.html()赋值，免去了在js拼接标签的麻烦 *@ -->
	    <div class="row">
	    	
	    </div>
	    <div class="row" style="margin-right:0;">
	   		 <button id="addButton"  type="button" class="btn btn-primary">新  增</button>
	    	<!-- 报告名称：<select name="templateid" id="select1">
				    		<option value="">请选择</option>
				    	</select> -->
			<#list model.columnList as col>
				<#assign colName=func.convertUnderLine(col.columnName)>
				${col.comment}：<input name="${colName }" />
			</#list>
	        <button id="go_search" class="btn btn-default">搜索</button>
	    </div> 
	
	</div>
	  <table id='datatable' class="table table-bordered table-striped table-hover">
	    	<thead>
	    		<tr>
		    		<#list model.columnList as col>
				    <th>${col.comment}</th>
				    </#list>
		    		<th>操作</th>
		    	</tr>
	    	</thead>
	  </table>
	</div>
    <script type="text/javascript" src="<%=path%>/static/resources/back/js/${classVar}.js"></script>
  </body>
</html>
