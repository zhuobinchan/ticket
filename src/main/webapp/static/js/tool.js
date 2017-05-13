var pathName=window.document.location.pathname;  
var g_path=pathName.substring(0,pathName.substr(1).indexOf('/')+1); 
function printTicket(arr,callback){
	if (typeof(SWI)!='undefined'&&eval(SWI.util.getParameter('system', 'newSeatMap'))) {
		SWI.util.printTicket(arr, callback);
		return;
	}
	if(typeof(callback)!='undefined'){
		callback();
	}
	//return;
	var LODOP=getLodop();
	var iPrinterCount=LODOP.GET_PRINTER_COUNT();
	var printerName = "EPSON LQ-630K ESC/P 2 Ver 2.0";
	for(var i=0;i<iPrinterCount;i++){
		var name = LODOP.GET_PRINTER_NAME(i);
		if(name.indexOf("Epson LQ-1600K")!==-1||name.indexOf("Print2Flash 3 Printer")!==-1){
			printerName = name;
			break;
		}
	};	
	var left = 380;
	var height = 17;
	var h,h3;
	jQuery(arr).each(function(i,infos){
		 LODOP.SET_PRINTER_INDEX(printerName);
		 h = 16;
		 h3 = 16;
		 LODOP.SET_PRINT_STYLE("FontSize",7);
		 
		 LODOP.ADD_PRINT_TEXT(39,200,180,height,infos['saleNo']);
		 LODOP.ADD_PRINT_TEXT(h3 = h3 + 15,10,180,height,infos['saleNo']);
		 LODOP.ADD_PRINT_TEXT(h3 = h3 + 15,10,180,height,infos['price']+"元");
		 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,infos["belongto"]==1?"田汉大剧院":"红太阳演义中心");
		// LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"流水号:"+infos['sn']);
//		 LODOP.SET_PRINT_STYLE("FontSize",9);
//		 LODOP.SET_PRINT_STYLE("FontSize",7);
//		 if(infos.priceShowType!=3)
		 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"门票价格："+infos['price']+"元");
		 if(infos['seatName']){
			 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"座位："+infos['seatName']); 
		 }
		 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"人数："+infos['userNum']+"人"); 
		 if(infos["gift"]){
			 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"赠送："+infos['gift']); 
			 LODOP.ADD_PRINT_TEXT(h3 = h3 + 15,10,180,height,infos['gift']);
		 }
		 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"演出日期："+infos['playDate']);
		 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"开场时间："+infos['showNumber']);
		 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"购票时间："+infos['createTime']);
		 LODOP.ADD_PRINT_TEXT(h = h+15,left,180,height,"票号："+infos['number']);
		 LODOP.ADD_PRINT_BARCODE(h = h+12,left+20,90,90,"QRCode",infos['number']);
		 
		 LODOP.PRINT(); 
	});
	if(typeof(callback)!='undefined'){
		callback();
	}
	
}
function ajustHeight(){
	var h = $(top).height()-80;
	var elements = $("body").children(":not(script):not(link)");
	var eh = 0;
	var element = null;
	elements.each(function(i,div){
		eh += $(div).height();
		element = div;
	})
	if(eh>h){
		$(top.document).find("iframe[name=mainFrame]").height(eh);
		$(top.document).find("div.mainl").height(eh);
	}else{
		$(top.document).find("iframe[name=mainFrame]").height(h);
		$(top.document).find("div.mainl").height(h);
		if($(element).find("div.rcon2").length>0){
			$(element).find("div.rcon2").height((h-eh)+$(element).height());
		}else if($(element).find("div.rcon").legnth>0){
			$(element).find("div.rcon").height((h-eh)+$(element).height());
		}else{
			$(element).height((h-eh)+$(element).height());
		}
		
	}
}
function ajustHeight2(){
	var h = $(top).height()-80;
	var elements = $("body").children(":not(script)");
	var eh = 0;
	var element = null;
	elements.each(function(i,div){
		eh += $(div).height();
		element = div;
	})
	if(eh>h){
		$(top.document).find("iframe[name=mainFrame]").height(eh);
		$(top.document).find("div.mainl").height(eh);
	}else{
		$(top.document).find("iframe[name=mainFrame]").height(h);
		$(top.document).find("div.mainl").height(h);
		$(element).height((h-eh)+$(element).height());
	}
}
function getDicValue(dic,value){
	var v = "";
	if(dic.indexOf(" from ")==-1){
		var dicObj = window['dic'+dic];
		if(dicObj){
			v = dicObj[value]["text"];
		}
	}else{
		dwr.engine.setAsync(false);
		DbServices.queryEntityDicValue(dic,value,function(ret){
			if(ret!=null)
				v = ret[1];
		});
	}
	return v;
}
function genDicJs(arr){
	$.ajax({
		url:g_path + '/public/tool/gendicjs.htmls',
		data:{'data':arr},
		dataType:'json',
		type:'POST',
		async:false,
		success:function(ret){
			document.write("<script>");
			for(var i=0;i<ret.length;i++){
				var obj = ret[i];
				document.write("var dic"+obj.code +"=" +JSON.stringify(obj.dicMap)+";");
			}
			
			document.write("</script>");
		}
	})
}
function genMaps(hql){
	var obj = null;
	$.ajax({
		url:g_path + '/public/tool/genMaps.htmls',
		data:{hql:hql},
		dataType:'json',
		type:'POST',
		async:false,
		success:function(ret){
			obj = ret;
		}
	})
	return obj;
}

/**
 * 生成下拉菜单
 * 
 * @param strategys 数据
 * @param id 可指定下拉菜单的id，如果找不到则把id赋值给下拉菜单
 * @returns 下拉菜单html代码
 */
function joinSelect(strategys,id){
	if(arguments.length>1){
		var str = _.map(strategys,function(obj,key){
			return "<option value='"+obj.id+"'>" + obj.name + "</option>";
		})
		var se = null;
		se = $('select[id="' + id + '"]');
		if (!se || se.length == 0) {
			se = $('<select id="' + id + '">');
		}
		return se.append("<option value=''>==请选择==</option>"+str);
	}else{
		var conf = arguments[0];
		var str = _.map(conf.data,function(obj,key){
			return "<option value='"+obj[conf.valueKey]+"'>" + obj[conf.textKey] + "</option>";
		})
		return $('<select>').append("<option value=''>==请选择==</option>"+str);
	}
	
}

function joinSelect4Dic(strategys,id){
	var str = _.map(strategys,function(obj,key){
		return "<option value='"+obj.code+"'>" + obj.text + "</option>";
	})
	var se = null;
	se = $('select[id="' + id + '"]');
	if (!se || se.length == 0) {
		se = $('<select id="' + id + '">');
	}
	return se.append("<option value=''>==请选择==</option>"+str);
}

/**
 * 生成数据字典的下拉菜单
 * 
 * @param dic 数据字典的类别，4位长度
 * @param id 可指定下拉菜单的id，如果找不到则把id赋值给下拉菜单
 * @returns 下拉菜单html代码
 */
function dicSelect(dic, id){
	var arr = _.map(dic,function(obj,key){
		return "<option value='"+obj.code+"'>" + obj.text + "</option>";
	})
	var se = null;
	if (id != null) {
		se = $('select[id="' + id + '"]');
	}
	if (!se || se.length == 0) {
		se = $('<select' + (id ? ' id="' + id + '" ' : '') + '>');
	}
	// IE不能用prepend方法
	se.append("<option value=''>==请选择==</option>" + arr.join(""));
	return se;
}
function popSelectWin(obj,title,url,callback,width,height){
	//var url = g_path+"/public/common/r2.htmls?page=abc/user/userWin";
	if(!width){
		width = 660;
	}
	if(!height){
		height = 450;
	}
	layer.open({
		title:title,
		skin: 'layui-layer-lan',
	    type: 2,
	    area: [width+'px', height+'px'], //宽高
	    content:url,
	    btn:["确定","取消"],
	    yes:function(index,layero){
	    	callback(index,layero);
	    }
	});
}
function onlyDigit(e){  
    var charCode = e.charCode ? e.charCode : e.keyCode;  
    if(charCode<48 || charCode >57){  
        return false;  
    }else{  
        return true;      
    }  
} 
function isNumber(num)
{
     var re = /^[\d.]+$/;      
     if (!re.test(num))
    {
        return false;
     }
     return true;
}
function getCurrentTime(pattern){
	pattern = (pattern===undefined?"yyyy-MM-dd":pattern);
	var value = "";
	$.ajax({
		url:g_path + '/public/tool/genCurrentTime.htmls',
		data:{'pattern':pattern},
		dataType:'html',
		type:'POST',
		async:false,
		success:function(ret){
			value = ret;
		}
	})
	return value;
}
