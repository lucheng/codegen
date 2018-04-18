<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign type=system+".common.entity."+package+"." +class>
<#assign namespace=system+".common.repository."+package+"." +class+"Dao">
<#assign tableName=model.tableName>
<#assign system=vars.system>
<#assign foreignKey=model.foreignKey>
<#assign sub=model.sub>
<#assign colList=model.columnList>
<#assign commonList=model.commonList>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.getPkVar(model) >
$(function(){
	var tablePrefix = "#datatable_";
	$("#datatable").dataTable({
		serverSide: true,//分页，取数据等等的都放到服务端去
		processing: true,//载入数据的时候是否显示“载入中”
		pageLength: 10,//首次加载的数据条数
		//paging: true,//分页
		ordering: false,//是否启用排序
		//searching: true,//搜索
		ajax: {//类似jquery的ajax参数，基本都可以用。
            type: "post",//后台指定了方式，默认get，外加datatable默认构造的参数很长，有可能超过get的最大长度。
            url: __ctx + "/${package}/${classVar}/list",
            dataSrc: "data",//默认data，也可以写其他的，格式化table的时候取里面的数据
            data: function (d) {//d 是原始的发送给服务器的数据，默认很长。
                var param = {};//因为服务端排序，可以新建一个参数对象
                param.start = d.start;//开始的序号
                param.limit = d.length;//要取的数据的
                var formData = $("#filter_form").serializeArray();//把form里面的数据序列化成数组
                formData.forEach(function (e) {
                    param[e.name] = e.value;
                });
                return param;//自定义需要传递的参数。
            },
        },
        columns: [//对应上面thead里面的序列
                  <#list model.columnList as col>
                  <#assign colName=func.convertUnderLine(col.columnName)>
                  { data: "${colName}"},
                  </#list>
                  {
                      data: function (e) {//这里给最后一列返回一个操作列表
                          //e是得到的json数组中的一个item ，可以用于控制标签的属性。
                          return '<a class="btn btn-default btn-xs show-detail-json"><i class="icon-edit"></i>修改</a>'
                          		 +'&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn btn-default btn-xs del-detail-json"><i class="icon-del"></i>删除</a>';
                      }
                  }
        ],
        initComplete: function (setting, json) {
            //初始化完成之后替换原先的搜索框。
            //本来想把form标签放到hidden_filter 里面，因为事件绑定的缘故，还是拿出来。
            $(tablePrefix + "filter").html("<form id='filter_form'>" + $("#hidden_filter").html() + "</form>");
        },
		language: {
            lengthMenu: '<select class="form-control input-xsmall">' + '<option value="1">1</option>' + '<option value="10">10</option>' + '<option value="20">20</option>' + '<option value="30">30</option>' + '<option value="40">40</option>' + '<option value="50">50</option>' + '</select>条记录',//左上角的分页大小显示。
            processing: "载入中",//处理页面数据的时候的显示
            //search: '<span class="label label-success">搜索：</span>',//右上角的搜索文本，可以写html标签
            paginate: {//分页的样式内容。
                previous: "上一页",
                next: "下一页",
                first: "第一页",
                last: "最后"
            },
            zeroRecords: "没有内容",//table tbody内容为空时，tbody的内容。
            //下面三者构成了总体的左下角的内容。
            info: "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，筛选之后得到 _TOTAL_ 条，初始_MAX_ 条 ",//左下角的信息显示，大写的词为关键字。
            infoEmpty: "0条记录",//筛选为空时左下角的显示。
            infoFiltered: ""//筛选之后的左下角筛选提示，
        }
        //,paging: true,
        //pagingType: "full_numbers",//分页样式的类型
	});
	//$("#datatable input[type=search]").css({ width: "auto" });//右上角的默认搜索文本框，不写这个就超出去了。
	//保存按钮触发事件
	$("#saveOrUpdate").click(function(){
    	var dataPara = getFormJson($("#myForm"));
	    $.ajax({
	        url: __ctx + "/${package}/${classVar}/save",
	        type: 'POST',
	        data: dataPara,
	        dataType:"json",
	        success:function(req){
				var obj = eval(req);
				if(obj.code == 200){
					$("#myModal").modal("hide");
					$("#datatable").DataTable().draw();//点搜索重新绘制table。
				}else{
					Ewin.alert(obj.message);
				}
			}
	    });
	});
});
$(document).on("submit", "#filter_form", function () {
    return false;
});

//新增按钮触发事件
$(document).on('click',"#addButton",function(){
	clearModelData();
	$("#myModal").modal("show");
}); 

//修改按钮出发事件
$(document).on("click", ".show-detail-json", function () {
    //alert(JSON.stringify($("#datatable").DataTable().row($(this).parents("tr")).data()));
	var obj = $.parseJSON(JSON.stringify($("#datatable").DataTable().row($(this).parents("tr")).data()));
	<#list model.columnList as col>
    <#assign colName=func.convertUnderLine(col.columnName)>
    $("#${colName}").val(obj.${colName});
    </#list>
	$("#myModal").modal("show");
});
//删除按钮出发事件
$(document).on("click", ".del-detail-json", function () {
    //alert(JSON.stringify($("#datatable").DataTable().row($(this).parents("tr")).data()));
	var obj = $.parseJSON(JSON.stringify($("#datatable").DataTable().row($(this).parents("tr")).data()));
	Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		$.ajax({
			url:__ctx + "/${package}/${classVar}/del",
			type:"post",
			data:{
				id:obj.${func.convertUnderLine(pk)}
			},
			dataType:"json",
			success:function(req){
				var obj = eval(req);
				if(obj.code == 200){
					$("#datatable").DataTable().draw();//点搜索重新绘制table。
				}else{
					Ewin.warning(obj.message);
				}
			}
		});
	})
	//$("#myModal").modal("show");
});
$(document).on("click", "#go_search", function () {
     $("#datatable").DataTable().draw();//点搜索重新绘制table。
});
function clearModelData(){
	<#list model.columnList as col>
    <#assign colName=func.convertUnderLine(col.columnName)>
    $("#${colName}").val("");
    </#list>
}

function getFormJson(frm) {
    var o = {};
    var a = frm.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });

    return o;
}
