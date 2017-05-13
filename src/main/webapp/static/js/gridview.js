/*
 * target:容器ID或者容器对象，el：表格宽度的参考对象，不一定和容器是同一对象
 */
function GridView(target,settings,el){
	this.actionType = "";
	settings.url = g_path + "/public/frame/gridview.htmls";
	if(typeof(target)==="string"){
		target = $('#'+target);
	}
	this.target = target;
//	this.width = target.width();
	if(settings.className==undefined){
		settings.className = "mtable";
	}
	this.settings = settings;
	if(typeof(el)=="string"){
		this.frameWidth = $("#"+el).width();
	}else if(typeof(el)=="number"){
		this.frameWidth = el;
	}else{
		this.frameWidth = $(el).width();
	}
	this.settings.pageInfo.showPageNum=1;
	this.table = null;
	this.listeners = {};
	if(settings.listeners){
		this.listeners = settings.listeners;
	}
	this.simlePageMode = false;
	if(typeof(settings.pageInfo.simple)!="undefined"&&settings.pageInfo.simple){
		this.simlePageMode = true;
	}
	this.invalid = false;//参数是否合法
};
GridView.prototype.setActionType = function(type){
	this.actionType = type;
}
GridView.prototype.init = function(){
	this.target.html('');
	var obj = this;
	var gridTable = $('<table border="0" cellspacing="0" cellpadding="0" class="' + this.settings.className + '"></table>').appendTo(this.target);
	
	this.table = gridTable;
	if(this.target.hasClass("rbox1")||this.target.hasClass("rbox2")){
		gridTable.wrap($("<div class='rcon2'></div>"));
	}
	var thead = $("<thead>").appendTo(gridTable);
	var headTr = $('<tr></tr>').appendTo(thead);
	var th = $("<th>").html("&nbsp;").appendTo(headTr);//索引
	if(!this.settings.hideCheckbox){//显示复选框
		this.settings.hideCheckbox = false;
		th = $("<th>").html("<input type='checkbox' name='_all'/>").appendTo(headTr).find(":checkbox").change(function(){
			gridTable.find(":checkbox").prop("checked",$(this).is(':checked'));
			if(obj.listeners.rowClick)
				obj.listeners.rowClick.call(obj,"###");
		});
	}
	var sumwidth = 0;
	this.widthArr = [];
	$.each(this.settings.fields,function(i,v){
		if(!v.hide){
			if(v.width!=undefined&&v.width!=''){
				if(typeof(v.width)!="number"){
					
					obj.invalid = true;
					return false;
				}else if(!IsNum(v.width)){
					obj.invalid = true;
					return false;
				}
			}
			if(!v.width){
				v.width = obj.frameWidth - sumwidth;
			}
			obj.widthArr.push(v.width);
			sumwidth += v.width;
			th = $("<th>").attr("name",v.name).html(v.descs).appendTo(headTr);//其他的表头列
			if(v.type==='select'||v.type=='popText'){
				//在表头保存数据字典分组名称，增加时在此查询
				$.ajax({
					async:false,
					url:g_path + "/public/frame/searchFieldDic.htmls",
					data:{'modelName':obj.settings.po,propName:v.name},
					success:function(dic){
						th.attr("dic",dic);
					}
				});
			}
		}
	});
	
	if(obj.invalid){
		art.dialog.tips("<label style='font-size:14px;color:red'>非法参数，视图渲染失败</label>");
		return false;
	}
	var colgroup = $('<colgroup></colgroup>').prependTo(obj.table);
	colgroup.append("<col width='50'></col>");
	if(!this.settings.hideCheckbox){
		colgroup.append("<col width='30'></col>");
	}
	var tableWidth = 0;
	$.each(this.widthArr,function(i,f){
		if(obj.settings.fixWidth){
			$('<col width="' + obj.widthArr[i] + '"></col>').appendTo(colgroup);
		}else{
			$('<col width="' + (obj.frameWidth * (obj.widthArr[i]/sumwidth)) + '"></col>').appendTo(colgroup);
		}
		tableWidth+= obj.widthArr[i];
	});
	$('<tbody></tbody>').appendTo(obj.table);
	if(!obj.settings.fixWidth){
		gridTable.width(obj.frameWidth - gridTable.position().left + this.target.position().left);
	}
	this.initToolbar();
	this.initPageInfo();
};
GridView.prototype.initPageInfo = function(){
	//添加分页栏
	var obj = this;
	if(this.settings.pageInfo.records!=-1){
		var recordNumDiv = $("<div class='tablef'>共<b>0</b>条记录</div>");
		this.table.after(recordNumDiv);
		var pagerDiv = $("<div class='bigpage'></div>");
		pagerDiv.append("<a class='pageprev valid'><b></b></a><span class='aspan'></span>");
		pagerDiv.append("<a href='#' class='pagenext valid'><b></b></a><span class='pl30'>跳转到:&nbsp;</span><input class='istxtbig' type='text' value=''><a class='gospecial' href='javascript:void(0);'>GO</a>");
		recordNumDiv.after(pagerDiv);
		this.target.find("a.pageprev").click(function(){
			if(this.className.indexOf('valid')==-1){
				obj.settings.pageInfo.showPageNum--;
				obj.loadRest();
			}
		});
		this.target.find("a.pagenext").click(function(){
			if(this.className.indexOf('valid')==-1){
				obj.settings.pageInfo.showPageNum++;
				obj.loadRest();
			}
		});
		this.target.find(".gospecial").click(function(){
			_goSpecialPage(obj,obj.target.find(".bigpage input")[0]);
		});
		this.target.find(".bigpage input").keydown(function(e){
			e=e||event;
			if(e.keyCode===13){
				_goSpecialPage(obj,this);
			}
		});
	}
}
GridView.prototype.initToolbar = function(){
	
	if(typeof(this.settings.toolbar)!='undefined'||typeof(this.settings.title)!='undefined'){
		var toolbarContainer = $("<div class='rtit2'></div>");
		this.target.prepend(toolbarContainer);
		if(typeof(this.settings.toolbar)!='undefined'){
			var toolbardiv = $('<div class="rtit2r"></div>').appendTo(toolbarContainer);
			var buttons = this.settings.toolbar.buttons;
			var button = null;
			for(var i=0;i<buttons.length;i++){
				var buttonInfo = buttons[i];
				if(typeof(btnString)=='undefined'||btnString.indexOf(buttonInfo.id)!=-1){
					if(buttonInfo.type){
						if(buttonInfo.type=='file'){
							button = $('<input  multiple="true" type="file" name="' + buttonInfo.id + '" id="' + buttonInfo.id + '" ></input>');
							button.appendTo(toolbardiv);
						}
					}else{
						if(buttonInfo.href==undefined){
							button = $('<button id="'+ buttonInfo.id+'">'+buttonInfo.title+'</button>');
							button.appendTo(toolbardiv);
						}else{
							button = $('<a id="'+ buttonInfo.id+'" href="'+buttonInfo.href+'">'+buttonInfo.title+'</a>');
							button.appendTo(toolbardiv);
						}
						for(fn in buttonInfo.handlers){
							button.bind(fn,buttonInfo.handlers[fn]);
						}
					}
				}
				if(buttonInfo.width){
					button.width(buttonInfo.width);
				}
				
			}
			toolbardiv.find("button").addClass('btn7').last().addClass('btn7');
			
		}
		if(typeof(this.settings.title)!='undefined'){
			var titlediv = $('<div class="rtit2l">'+this.settings.title+'</div>');
			toolbarContainer.prepend(titlediv);
		}
	}
}
GridView.prototype.load=function(queryData,beforeFunc){
	if(beforeFunc==undefined){
		this.settings.pageInfo.showPageNum=1;
	}else{
		beforeFunc(this);
	}
	if(typeof(queryData)==="undefined"||queryData==null){
		this.settings.data = {};
		this.loadRest();
	}else{
		if(JSON.stringify(queryData.data)=="{}"){
			this.settings.data = {};
		}else{
			this.settings.data = queryData.data;
		}
		if(queryData.recordId)
			this.loadRest(queryData.recordId);
		else{
			this.loadRest();
		}
	}
};
GridView.prototype.reload = function(recordid,callback){
	//重新加载，recordid可选，传入该参数，则在加载完数据后，选中该记录
	if(typeof(recordid)==="function"){
		this.loadRest();
		recordid();
	}else{
		this.loadRest(recordid);
	}
	if(callback){
		callback();
	}
};
GridView.prototype.loadRest = function(recordId){
	if(this.invalid){
		return false;
	}
	var obj = this;
	this.settings.pageInfo.orderby = this.settings.pageInfo.orderby.replace("select ","xuanze ").replace(' from '," cong ");
	var fieldString = JSON.stringify(this.settings.fields);
	fieldString = fieldString.replace(/\"select\"/gi,"\"$xuanze$\"");
	var params = {fieldsStr:fieldString,id:this.settings.id,
			modelName: this.settings.po,
			'pageInfo.showPageNum':this.settings.pageInfo.showPageNum,'pageInfo.records':this.settings.pageInfo.records,
			'pageInfo.orderby':this.settings.pageInfo.orderby,'service':this.settings.service,'hideCheckbox':this.settings.hideCheckbox};
	if(this.settings.data){
		params.queryParams = JSON.stringify(this.settings.data);
		params.queryParams = params.queryParams.replace(/\"or\":/gi,"$huozhe$");
	}else{
		params.queryParams = "{1:2}";
	}

	
	$.ajax({
		url: this.settings.url,
		async: false,
		dataType: "json",
		data: params,
		type:'post',
		success: function(res){
			
			obj.settings.pageInfo = res.pageInfo;
			if(obj.table.find('tbody').length==0){
				$('<tbody></tbody>').appendTo(obj.table);
			}
			
			obj.table.find('tbody').html(res.tableStr);
			if(obj.settings.listeners){
				if(obj.settings.listeners.loaded){
					obj.settings.listeners.loaded();
				}
			}
			if(typeof(recordId)!=='undefined'){//默認選中
				$("#"+obj.settings.id+"_"+recordId).addClass("highlight").find(":checkbox[name=_rowbox]").prop("checked",true);
				changeThBoxStatus(obj);
			}
			//单击tr
			obj.table.find("tbody tr").click(function(ev){
				ev.stopPropagation();
				obj.table.find("tbody tr").find(":checkbox[name=_rowbox]").prop("checked",false);
				$(this).find(":checkbox[name=_rowbox]").prop("checked",true);
				changeThBoxStatus(obj);
				if(obj.listeners.rowClick){
					obj.listeners.rowClick.call(this,$(this).attr("recordId"),$(this));
				}
				var jEdittingCells = $(this).siblings().find("td[editting=true]");
				obj.cancelEditting(jEdittingCells);//取消其他行处于编辑状态的单元格的编辑状态
			}).mouseover(function(e){
				$("tr.highlight").removeClass("highlight");
				$(this).addClass("highlight");
			});//.end().find("tr:odd").addClass("oddRow");
			//复选框被选中
			obj.table.find(":checkbox[name=_rowbox]").click(function(ev){
				var cr = $(this).closest("tr");
				if(obj.listeners.rowClick){
					obj.listeners.rowClick.call(cr[0],cr.attr("recordId"),cr);
				}
				changeThBoxStatus(obj);
				ev.stopPropagation();
			}).parent().click(function(ev){
				var checkbox = $(this).find(":checkbox");
				checkbox.click();
				ev.stopPropagation();
			}).siblings('td[name=_ordernum]').click(function(ev){
				var checkbox = $(this.parentNode).find(":checkbox[name=_rowbox]");
				checkbox.click();
				ev.stopPropagation();
			});
			obj.openEditMode();
		}
	});
/*	if(this.tableWidth<this.table.width()){
		var tdNum = this.table.find("tr:first th:gt(1)").length;
		var w = this.table.width()+(tdNum*10);
		//this.gridWrap.width(w);
		this.table.width(w);
	}*/
	this.changePageInfo();
	if(this.afterPage){
		this.afterPage();
	}
	
};
GridView.prototype.changePageInfo = function(){
	//修改分页信息
	var showPageNum = this.settings.pageInfo.showPageNum;
	var pageInfo = this.settings.pageInfo;
	this.target.find(".tablef b").html(this.settings.pageInfo.totalRecords);
	var totalPageNum = pageInfo.totalPageNum;
	var aspan = this.target.find(".aspan").html("");
	if(!this.simlePageMode){
		var beginNum = 0;
		if(showPageNum>=5){
			beginNum = showPageNum - 4;
			if(totalPageNum-beginNum<6){
				beginNum = totalPageNum - 6;
			}
		}
		beginNum = beginNum<0?0:beginNum;
		for(var i=0;i<totalPageNum;i++){
			var linkNum = i+1+beginNum;
			if(i<5){
				aspan.append("<a name='" + linkNum + "' href='#'>"+(linkNum)+"</a>");
			}else if(totalPageNum==6){
				aspan.append("<a name='" + linkNum + "' href='#'>"+linkNum+"</a>");
			}else if(i==(totalPageNum-beginNum-1)){
				if(totalPageNum-beginNum>6)
				aspan.append("<span>...</span>") ;
				aspan.append("<a name='" + totalPageNum + "' href='#'>"+totalPageNum+"</a>");
			}
		}
	}
	
	if(this.settings.pageInfo.showPageNum<2){
		this.target.find(".pageprev").addClass('valid');
	}else{
		this.target.find(".pageprev").removeClass("valid");
	}
	var totalPageNum = this.settings.pageInfo.totalPageNum;
	if(this.settings.pageInfo.showPageNum<totalPageNum){
		this.target.find(".pagenext").removeClass('valid');
	}else{
		this.target.find(".pagenext").addClass("valid");
	}
	this.target.find(".aspan a").removeClass("selected");
	this.target.find("a[name=" + showPageNum + "]").addClass("selected");
	var obj = this;
	aspan.find("a").click(function(){
		obj.settings.pageInfo.showPageNum = $(this).html();
		obj.loadRest();
		
	});
//	_newHeight(this);
};
function _goSpecialPage(obj,t){
	var pageInfo = obj.settings.pageInfo;
	t.value = t.value>pageInfo.totalPageNum?pageInfo.totalPageNum:t.value;
	t.value = t.value<1?1:t.value;
	if(t.value!=pageInfo.showPageNum&&t.value>0&&t.value<=pageInfo.totalPageNum){
		obj.settings.pageInfo.showPageNum = t.value;
		obj.loadRest();
	}
}
/**
 * to do 取消编辑行为
**/
GridView.prototype.cancelEditting = function(cells){
	if(typeof(cells)=="undefined"){
		cells = this.table.find("td[editting]");
	}
	cells.each(function(i,cell){
		var jInput = $(cell).find("input");
		if(jInput.length==0){
			jInput = $(cell).find("select");
			$(cell).attr("value",jInput.find("option:selected").val());
			if(jInput.val()==""){
				cell.innerHTML = "";
			}else{
				cell.innerHTML = jInput.find("option:selected").text();
			}
			
		}else{
			cell.innerHTML = jInput.val();
			if($(cell).attr('type')!='popText')
				$(cell).attr("value",jInput.val());
		}
		
		if($(cell).attr("value")!==$(cell).attr("dbvalue")){
			$(cell).attr("edited",true);
		}else{
			$(cell).removeAttr("edited");
		}
		$(cell).removeAttr("editting");
	});
}
GridView.prototype.openEditMode  = function(element){
	element = element||this.table;
	var obj = this;
	var fields = obj.settings.fields;
	element.find("td[dbvalue]").click(function(e){
		var name = $(this).attr("name");
		var field = null;
		try{
			field = _.findWhere(fields,{name:name});
		}catch(e){
			$.ajax({
				url: g_path+'/static/js/underscore.js',
				dataType: "script",
				async:false,
				success: function(ret){
					field = _.findWhere(fields,{name:name});
				}
			});
		}
		if(!$(this).attr('editting')){//如果处于非编辑状态
			var jRow = $(this).closest("tr");
			var jOldCell = jRow.find("td[editting=true]");//目前处于编辑状态的单元格
			obj.cancelEditting(jOldCell);//取消当前处于编辑状态的单元格的编辑状态
			$(this).attr('editting',true);
			var type = $(this).attr("type");
			
			if(!type){
				$(this).attr("type","text");
				type = "text";
			}
			var height = $(this).closest('tr').height() - 5;
			switch(type){
				case "text":
				case "int":
				case "date":
				case "datetime":
				case "pop":
				case "popText":
					var cellWidth = $(this).width()-5;
					var jInput = $("<input style='width:98%;text-align:center;margin:0;vertical-align: middle;line-height:26px' type='text'/>").height(height).val(this.innerHTML).appendTo($(this).empty());
					jInput.attr("name",name);
					
					if(type==="int"){
						jInput.addClass("spinner").spinner();
					}else if(type=="popText"||type=='pop'){
						jInput.bind("focus",function(e){
							popSelect2(obj.target.attr("id") ,jInput);
						});
						jInput.width(cellWidth);
					}else{
						if(type==="date"){
							jInput.addClass("date1");
							jInput.datepicker({ dateFormat: "yy-mm-dd" ,changeYear: true});
						}
						if(type==='datetime'){
							jInput.addClass("date1");
							jInput.datetimepicker({
								controlType: 'select',
								changeYear: true,
								timeFormat: 'HH:mm'
							});
							jInput.datepicker({ dateFormat: "yy-mm-dd" , timeFormat: 'hh:mm:ss',changeYear: true});
						}
						jInput.focus();
					}
					if($(this).attr("length")!='undefined'){
						jInput.attr("maxlength",$(this).attr("length"));
					}
					
					jInput.focus().select().keydown(function(e){
						if(e.shiftKey&&e.keyCode===9){
							$(this).closest("td").prev("td").click();
						}else if(e.keyCode===9){
							var mytd = $(this).closest("td").next("td");
							mytd.click();
							setTimeout(function(){
							//	mytd.find(":input").focus();
							},1)
						}
					});
					var blur = $(this).attr("blur");
					if(blur){
						jInput.bind('blur',function(){
							window[blur](jRow,$(this),jInput);
						});	
					}
					if(field.init){
						field.init(jInput);
					}
					break;
				case "select":
					var jSe = $("<select>").appendTo($(this).empty());
					var dic = obj.table.find("th[name=" + $(this).attr('name') + "]").attr("dic");
					var dicJson = eval("dic"+dic);
					$("<option>").val("").text("--请选择--").appendTo(jSe);
					for(var x in dicJson){
						$("<option>").val(x).text(dicJson[x].text).appendTo(jSe);
					}
					jSe.focus().select().val($(this).attr("value")).click(function(e){
						return false;
					});
					break;
			} 
		}
		
	});
};
//判断所有的行是否选中，全选则将标题行的复选框选中
function changeThBoxStatus(obj){
	obj.table.find('tbody :checkbox:not(:checked)').closest('tr').removeClass("picked");
	obj.table.find('tbody :checkbox:checked').closest('tr').addClass("picked");
	if(obj.table.find("tbody :checkbox").not(":checked").length===0){
		obj.target.find(":checkbox[name=_all]").prop("checked",true);
	}else{
		obj.target.find(":checkbox[name=_all]").prop("checked",false);
	}
}
function validateGrid(gridObj){
	var el = null;
	var se = gridObj.getChangedRows();
	se.each(function(i,row){
		var jCells = $(row).find("td[required]");
		jCells.each(function(j,cell){
			if($(cell).attr('value')===""){
				el = cell;
				return false;
			}
		});
	});
	if(el==null){
		return true;
	}else{
		layer.alert($(el).attr('title') + "不能为空",function(){
			el.click();
		});
		return false;
	}
}
GridView.prototype.getCellValueById = function(id,name){
	var cells = null;
	if(typeof(id)==="object"){
		cells = id.find("td[name="+name+"]");
	}else{
		cells = this.table.find("tr[recordId="+id+"] td[name="+name+"]");
	}
	if(cells.length===1){
		return cells.attr("value");
	}else{
		var hidevalues = eval("(" + this.table.find("tr[recordId="+id+"]").attr('hidevalues') + ")");
		return hidevalues[name];
	}
};
GridView.prototype.rowClick = function(fn){
	this.listeners.rowClick = fn;
};
GridView.prototype.newRow = function(){
	var obj = this;
	var fields = this.settings.fields;
	this.cancelEditting();
	this.table.find(":checkbox[name=_rowbox]").prop("checked",false);
	this.target.height(this.target.height()+38);
	var newRow = $("<tr name='newrow'>").append($("<td height='28'><img width='10' src='" + g_path + "/static/images/icon/pencil.png'/></td><td align='center'><input type='checkbox' name='_rowbox' checked/></td>")).prependTo(this.table.find("tbody"));
	newRow.click(function(){
		var jEdittingCells = $(this).siblings().find("td[editting=true]");
		obj.cancelEditting(jEdittingCells);//取消其他行处于编辑状态的单元格的编辑状态
		obj.table.find("tbody tr").removeClass("heightlight").find(":checkbox[name=_rowbox]").prop("checked",false);
		$(this).addClass("heightlight").find(":checkbox[name=_rowbox]").prop("checked",true);
		if(obj.listeners.rowClick){
			obj.listeners.rowClick.call(this,"");
		}
	});
	$.each(fields,function(i,v){
		if(!v.hide){
			var jCell = $("<td></td>");
			var type = v.type;
			if(type==='select'||v.dicMap){
				type = "select";
				var dic = obj.target.find("."+obj.settings.className+" th[name=" + v.name + "]").attr("dic");
				jCell.attr("dic",dic);
			}
			if(v.required){
				jCell.attr("required","true");
			}
			if(v.defaultValue){
				if(type==="select"){
					jCell.html(getDicValue(dic,v.defaultValue));
				}else{
					jCell.html(v.defaultValue);
				}
				jCell.attr("value",v.defaultValue).attr("edited",true);
			}else{
				jCell.attr("value","");
			}
			if(v.editable){
				jCell.attr("dbvalue","");
			}
			jCell.attr("style", "word-break:break-all;");
			jCell.attr("name", v.name);
			jCell.attr("type", type);
			jCell.attr("title", v.descs);
			if(v.align){
				jCell.attr("align","center");
			}
			newRow.append(jCell);
		}
	});
	this.openEditMode(newRow);
	return newRow;
};
GridView.prototype.getChangedRows = function(){
	this.cancelEditting();
	var jRows = this.table.find("tr").filter(function(){
		return $('td[edited]',this).length>0;
	});
	return jRows;
}
/**
 *获取选择的行
 **/
GridView.prototype.getSelectedRows = function(){
	var jRows = this.table.find("tr:has(:checked[name=_rowbox])");
	return jRows;
}
GridView.prototype.getAllRows = function(){
	var jRows = this.table.find("tr[recordId]");
	return jRows;
}
GridView.prototype.save = function(id) {
	var callback=null;
	var handler = "";
	var recordid = id;
	if(typeof(id)==="function"){
		recordid = "";
	}
	
	for(var i=0;i<arguments.length;i++){
		if(typeof(arguments[i])=="function"){
			callback = arguments[i];
		}
	}
	if(typeof(arguments[1])=="string"){
		handler = arguments[1];
	}
	var obj = this;
	var jRows = this.getChangedRows();
	if(!jRows.length){
		layer.msg("没有改动的记录，不需要保存！");
		return false;
	}
	var records = [];
	jRows.each(function(i, row) {
		var jCells = $(row).find('td[edited]');
		var record = {};
		jCells.each(function(i, cell) {
			var name = $(cell).attr("name");
			record[name] = $(cell).attr("value");
		});
		if ($(row).attr("recordId")){//有recordId属性则为更新
			record.id = $(row).attr("recordId");
		}
		var extData = $(row).data("exData");
		if(extData){
			for(x in extData){
				record[x] = extData[x];
			}
		}
		records.push(record);
	});
	$.ajax({
		url:g_path + "/public/tool/saveOrUpdateRecords.htmls",
		type:"POST",
		dataType:"json",
		data:{id:recordid,poName:obj.settings.po,data:records,method:handler},
		success:function(ret){
			if(ret.message){
				if(callback){
					callback(ret);
				}else{
					obj.reload(recordid);
				}
			}else{
				layer.alert("数据保存失败");
			}
		}
	})
	
};
GridView.prototype.deletes = function(){
	var callback = null,handler="";
	var jRows = this.getSelectedRows();
	if(!jRows.length){
		layer.msg("请选择要删除的记录！");
		return false;
	}
	for(var i=0;i<arguments.length;i++){
		if(typeof(arguments[i])=="function"){
			callback = arguments[i];
		}
	}
	if(typeof(arguments[0])=="string"){
		handler = arguments[0];
	}
	var obj = this;
	layer.confirm('确定删除吗？',{icon: 3, title:'提示信息'}, function (index) {
		var records = [];
		jRows.each(function(i,v){
			if($(v).attr('name')==='newrow'){
				$(v).remove();
			}else{
				records.push($(v).attr("recordId"));
			}
		});
		if(records.length>0){
			$.ajax({
				url:g_path + "/public/tool/deleteRecords.htmls",
				type:"POST",
				dataType:"json",
				data:{arr:records.join(","),poName:obj.settings.po,method:handler},
				success:function(ret){
					if(callback){
						callback(ret);
					}else{
						obj.reload();
					}
				}
			})
		}
		layer.close(index);
	}, function () {
	   
	});
};
GridView.prototype.removeAll = function(fn){
	this.settings.pageInfo.showPageNum = 1;
	this.settings.pageInfo.totalPageNum = 1;
	this.settings.pageInfo.totalRecords = 0;
	this.changePageInfo();
	this.table.find("tbody").empty();
};
GridView.prototype.addExData = function(row,data){
	var oldData = row.data("exData");
	if(oldData!=undefined){
		for(x in oldData){
			data[x] = oldData[x];
		}
	}
	row.data("exData",data);
};
GridView.prototype.setDefaultValue = function(cellName,value){
	var fields = this.settings.fields;
	for(var i=0;i<fields.length;i++){
		if(fields[i].name===cellName){
			fields[i].defaultValue = value;
			break;
		}
	}
};
GridView.prototype.setCellValue = function(row,cellName,value){
	var cell = row.find("td[name=" + cellName + "]");
	if(cell.attr("type")=='select'){
		var dic = this.table.find("th[name=" + cellName + "]").attr("dic");
		var dicJson = eval("dic"+dic);
		cell.attr("value",value).html(dicJson[value].text).attr("edited",true);
		
	}else if(cell.attr("type")=='pop'){
		var dic = this.table.find("th[name=" + cellName + "]").attr("dic");
		var txt = getDicValue(dic,value);
		cell.attr("value",value).html(txt).attr("edited",true);
	}else{
		cell.attr("value",value).html(value).attr("edited",true);
	}
};
GridView.prototype.setCellHtml = function(row,cellName,value){
	var cell = row.find("td[name=" + cellName + "]");
	if(cell.attr("type")=='select'){
		var dic = this.table.find("th[name=" + cellName + "]").attr("dic");
		var dicJson = eval("dic"+dic);
		cell.attr("value",value).html(dicJson[value].text).attr("edited",true);
		
	}else if(cell.attr("type")=='pop'){
		var dic = this.table.find("th[name=" + cellName + "]").attr("dic");
		var txt = getDicValue(dic,value);
		cell.attr("value",value).html(txt).attr("edited",true);
	}else{
		cell.attr("value",value).html(value).attr("edited",true);
	}
};
GridView.prototype.getCellValue = function(row,cellName){
	var cell = row.find("td[name=" + cellName + "]");
	return cell.attr("value");
};
function IsNum(num){
	var reNum=/^\d*$/;
	return(reNum.test(num));
}