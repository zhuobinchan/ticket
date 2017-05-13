var pathName=window.document.location.pathname;  
var path=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
var FormView = function(id,setting){
	this.id = id;
	this.setting = setting;
}
FormView.prototype.init = function(){
	var that = this;
	var wrap = $('#'+that.id);
	$.ajax({
		dataType: "json",
		async:false,
		url:path+'/public/frame/formview.htmls',
		data:{"data":this.setting.data},
		type:'POST',
		success:function(ret){
			$('#'+that.id).html("");
			var table = $("<table width='100%' border='0' cellspacing='0' cellpadding='0' class='ctable'></table>");
			for(var rowIndex=0;rowIndex<that.setting.fields.length;rowIndex++){
				var tr = $("<tr></tr>").attr("rowIndex",rowIndex+1).appendTo(table);
				var cellDatas = that.setting.fields[rowIndex];
				for(var cellIndex=0;cellIndex<cellDatas.length;cellIndex++){
					var cellData = cellDatas[cellIndex];
					var td = $("<td align='right'>"+(cellData['title'].length==0?"&nbsp;":cellData['title'])+"</td>").appendTo(tr);
					var td2 = $('<td></td>').appendTo(tr);
					if(cellData.colspan){
						td2.attr("colspan",cellData.colspan);
					}
					switch(cellData.type){
					case "blank":td2.html("&nbsp;");
						break;
					
					case "password":
					case "text":
					case "int":
				
						var type = cellData.type;
						type = type=="int"?"text":type;
						var input = $("<input uiType='"+cellData.type+"' type='"+type+"'>").appendTo(td2);
						if(cellData.name){
							input.attr("name",cellData.name);
						}
						if(cellData.id){
							input.attr("id",cellData.id);
						}else{
							input.attr("id",that.id+"_"+cellData.name);
						}
						if(cellData.width){
							input.css("width",cellData.width);
						}
						if(cellData.disabled){
							input.attr("disabled",cellData.disabled);
						}
						break;
					case "date":
					case "datetime":
						var input = $("<input type='text' uiType='"+cellData.type+"'>").appendTo(td2);
						if(cellData.name){
							input.attr("name",cellData.name);
						}
						if(cellData.id){
							input.attr("id",cellData.id);
						}else{
							input.attr("id",that.id+"_"+cellData.name);
						}
						input.attr("onclick","WdatePicker()");
						break;
					case "datescale":
			
						var input = $("<input type='text' size='10' uiType='"+cellData.type+"'>").appendTo(td2);
						if(cellData.name){
							input.attr("name",cellData.name);
						}
						if(cellData.id){
							input.attr("id",cellData.id);
						}else{
							input.attr("id",that.id+"_"+cellData.name);
						}
						input.attr("onclick","WdatePicker()");//end
						td2.append("-");
						input = $("<input size='10' type='text' uiType='"+cellData.type+"'>").appendTo(td2);
						if(cellData.name){
							input.attr("name",cellData.name);
						}
						if(cellData.id){
							input.attr("id",cellData.id+"_2");
						}else{
							input.attr("id",that.id+"_"+cellData.name+"_2");
						}
						input.attr("onclick","WdatePicker()");
						break;
					case "select":
						var select = null;
						if(cellData.dic){
							var dic = eval("dic"+cellData.dic);
							select = dicSelect(dic);
						}else{
							select = joinSelect({data:cellData["map"],valueKey:"id",textKey:"name"});
						}
						if(cellData.id){
							select.attr("id",cellData.id);
						}
						select.appendTo(td2).attr("uiType","select").attr("name",cellData.name);
						if(cellData.disabled){
							select.attr("disabled",cellData.disabled);
						}
						break;
					case "pop":
					case "popText":
					
						var input = $("<input uiType='pop' type='text'>").appendTo(td2);
						if(cellData.name){
							input.attr("name",cellData.name+"_txt");
						}
						if(cellData.id){
							input.attr("id",cellData.id);
						}else{
							input.attr("id",that.id+"_"+cellData.name+"_txt");
						}
						if(cellData.type==="pop"){
							input.bind('click',{fn:cellData.fn,title:cellData.title},function(e){
								window[e.data.fn](e,this,e.data.title);
							});
						}
						var input2 = $("<input uiType='pop' type='hidden'>").appendTo(td2);
						if(cellData.name){
							input2.attr("name",cellData.name);
						}
						if(cellData.id){
							input2.attr("id",cellData.id);
						}else{
							input2.attr("id",that.id+"_"+cellData.name);
						}
						break;
					case "autocomplete":
						var input = $("<input uiType='autocomplete' type='text'>").appendTo(td2);
						if(cellData.name){
							input.attr("name",cellData.name+"_txt");
						}
						if(cellData.id){
							input.attr("id",cellData.id);
						}else{
							input.attr("id",that.id+"_"+cellData.name+"_txt");
						}
						var input2 = $("<input uiType='autocomplete' type='hidden'>").appendTo(td2);
						if(cellData.name){
							input2.attr("name",cellData.name);
						}
						if(cellData.id){
							input2.attr("id",cellData.id);
						}else{
							input2.attr("id",that.id+"_"+cellData.name);
						}
						break;
					}
					
				}
				
			}
			if(that.setting.toolbar){
				var wrapDiv = $("<div class='rtit2'></div>").prependTo(wrap);
				var titleDiv = $("<div class='rtit2l'>"+that.setting.title+"</div>").prependTo(wrapDiv);
				var toolbarDiv = $("<div class='rtit2r'></div>").appendTo(wrapDiv);
				wrapDiv.append($("<div class='clear'></div>"));
				var toolbar =that.setting.toolbar;
				var buttons = toolbar.buttons;
				for(var i=0;i<buttons.length;i++){
					var button = buttons[i];
					var btnElement = $('<a href="javascript:void(0)" class="btn7"></a>').attr("id",button.id).html(button.title).appendTo(toolbarDiv);
					if(button.handlers){
						var eventNames = _.keys(button.handlers);
						for(var j=0;j<eventNames.length;j++){
							btnElement.bind(eventNames[j],button.handlers[eventNames[j]]);
						}
					}
				}
				wrapDiv.prependTo(wrap);
			}
			if(that.setting.type&&that.setting.type==="query"){
				if(that.setting.title){
					$("<div class='rtit'>"+that.setting.title+"</div>").appendTo(wrap);
				}
			}
			if(that.setting.queryRow){
				var tdNum = 0;
				table.find("tr:first").find("td").each(function(i,td){
					var colspan = $(td).attr("colspan");
					colspan = colspan===undefined?1:colspan;
					tdNum += parseInt(colspan);
				})
				var td = $("<td>").attr("colspan",tdNum).attr("align",that.setting.queryRow.align);
				var tr = $("<tr>").append(td);
				$(that.setting.queryRow.buttons).each(function(j,buttonConf){
					var button = $("<input type='button' class='btn2' value='" +buttonConf.title+"'/>").attr("id",buttonConf.id);
					if(buttonConf.handlers){
						$(buttonConf.handlers).each(function(k,handlerConf){
							if(handlerConf.click){
								button.bind("click",function(btnEv){
									handlerConf.click(btnEv,$('#'+that.id+" form"));
								})
							}
						})
					}
					if(buttonConf.hide){
						button.hide();
					}
					if(buttonConf.disabled){
						button.attr("disabled",true);
					}
					td.append(button);
				})
				table.append(tr);
			}
			wrap.append(table);
			table.wrap($("<div class='rcon2'><form></form></div>"));
			var hiddens = that.setting.hiddens;
			var form = wrap.find("form");
			if(hiddens){
				for(var i=0;i<hiddens.length;i++){
					form.append("<input name='"+hiddens[i] + "' type='hidden' uiType='hidden'/>");
				}
			}
		}
	})
	
}
FormView.prototype.setValue = function(name,value){
	var input = $('#'+this.id).find(":input[name="+name+"],select[name="+name+"]");
	if(input.attr("uiType")==="pop"||input.attr("uiType")==="autocomplete"){
		var rowIndex = input.closest("tr[rowIndex]").attr("rowIndex");
		input.siblings("input").val(_.findWhere(this.setting.fields[rowIndex-1],{name:name}).map[value].name);
		input.val(value);
	}else{
		input.val(value);
	}
}
FormView.prototype.getValue = function(name){
	var input = $('#'+this.id).find(":input[name="+name+"],select[name="+name+"]");
	return input.val();
}
FormView.prototype.reset = function(){
	var form = $("#"+this.id + " form");
	form[0].reset();
	$('input[name=id]').val("");
	// autocomplete的hidden项不会reset()，只能在此强制清空
	form.find('input[uitype="autocomplete"][type="hidden"]').val('');
}
FormView.prototype.save = function(callback){
	var obj = this;
	var inputs = $('#'+this.id).find(":input[uiType],select[uiType]");
	var data = {};
	inputs.each(function(i,el){
		data[el.name] = el.value;
	})
	$.ajax({
		url:g_path + "/public/tool/saveOrUpdateRecords.htmls",
		type:"POST",
		dataType:"json",
		data:{poName:obj.setting.po,data:[data]},
		success:function(ret){
			if(ret.message){
				if(callback){
					callback(ret);
				}
			}else{
				layer.alert("数据保存失败");
			}
		}
	})
}
FormView.prototype.loadData = function(id,poName,callback){
	var obj = this;
	var fields = obj.setting.fields;
	var inputs = $('#'+this.id).find(":input[uiType],select[uiType]");
	$.ajax({
		url:g_path + "/public/tool/loadFormData.htmls",
		type:"POST",
		dataType:"json",
		data:{poName:obj.setting.po,id:id},
		success:function(ret){
			inputs.each(function(i,el){
				var name = el.name;
				if(($(el).attr("uiType")==="hidden")){
					el.value = ret[name]===undefined ? "" : ret[name];
				}else{
					var cellData = _.findWhere(fields[$(el).closest('tr').attr("rowIndex")-1],{name:name});
					if(!_.has(cellData,'map')){
						el.value = ret[name]===undefined?"":ret[name];
					}else{
						if($(el).is(":hidden")){//弹出框的uiType="pop"
							if(_.has(ret,name)){
								el.value = ret[name];
								if(cellData.map[ret[name]]){
									$(el).siblings(":input[uiType]").val(cellData.map[ret[name]].name);
								}
								
							}
						}else{
							el.value = ret[name];
						}
					}
				}
				
			})
		}
	})
}