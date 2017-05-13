    var ticketArr = null;
    /**
     * @todo 刷新座位状态
     */
    var refreshSeatStatus = function(playDate){
    	top.getTickets(playDate,1);
    	top.setVars();
    	drawSeatGraphs();
    	$('#topPlayDate').html(playDate);
    }
    //seatObj 座位对象，含ticketId
    var gaiqian = function(seatObj){
    	var id = seatObj.attr("ticketId");
    	layer.confirm(seatObj.html()+"：确定改签吗？",{btn:["确定","取消"]},function(index){
			var arr = [id];
			BaseDwr.changeDate(arr,function(ret){
				var areaIds = [seatObj.attr("areaId")];
				synccolor($('#playDate').val(),areaIds);
				layer.close(index);
			});  
		});
    }
    /*
     * 作废
     */
    var unReserve = function(pickedArr){
    	data = {},ticketArr = [];
    	var reserved = true;
    	pickedArr.each(function(i,p){
    		var $p = $(p);
    		if($p.attr('status')!=="0203002"){
    			reserved = false;
    			return;
    		}
    		var area = data[$p.attr('areaId')];
    		if(!area){
    			area = [];
    		}
    		area.push($p.attr('oid'));
    		data[$p.attr('areaId')] = area;
    		ticketArr[i] = $p.attr("ticketId");
    	});
    	if(!reserved){
    		layer.msg("未预订的座位不可取消预订");
    		return;
    	}
    	layer.open({
    		title:'取消预订座位',
		    type: 2,
		    skin: 'layui-layer-rim', //加上边框
		    area: ['660px', '330px'], //宽高
		    content: g_path+'/public/common/r2.htmls?page=sell/job/unreserve',
		    end:function(ret){
		    	data = null;
		    	ticketArr=null;
		    	setSeatStatus();
		    }
		});
    }
    //查看预订门票
    var printReserved = function(){
    	layer.open({
    		title:false,
		    type: 2,
		    id:'print',
		    skin: 'layui-layer-rim', //加上边框
		    area: ['1060px', '600px'], //宽高
		    content: g_path+'/public/common/r2.htmls?page=sell/job/sale_reserved',
		    end:function(ret){
		    	pickedArr = null;
		    	
		    	if(ticketArr!=null){
		    		printTicket(ticketArr,function(){
		    			var areaIds = _.keys(data);
						synccolor($('#playDate').val(),areaIds);
		    		});
		    		data = null;
		    		ticketArr = null;
		    	}	
		    }
		});
    }
    //查看某条预订门票记录
    var printReservedSingle = function(obj){
    	if(obj.hasClass("enclose")){
    		var kaname = obj.attr("kaname");
    		var ticketId = obj.attr("ticketId");
    		layer.open({
        		title:false,
        		type: 2,
        		id:'print',
        		hade: [0.8, 'red'],
        		skin: 'layui-layer-lan', //加上边框
        		area: ['800px', '600px'], //宽高
        		content: [g_path+'/public/common/r2.htmls?page=sell/job/sale_reserved_box&kaName='+kaname+"&ticketId="+ticketId,"auto"],
        		end:function(ret){
        			pickedArr = null;
        			data = null;
        			if(ticketArr!=null){
        				printTicket(ticketArr,function(){
    						synccolor($('#playDate').val(),[obj.attr('areaId')]);
    		    		});
        				ticketArr = null;
        			}	
        		}
        	});
    	}else{
    		var ticketId = obj.attr("ticketId");
    		layer.open({
        		title:false,
        		type: 2,
        		id:'print',
        		hade: [0.8, 'red'],
        		skin: 'layui-layer-lan', //加上边框
        		area: ['1000px', '600px'], //宽高
        		content: [g_path+'/public/common/r2.htmls?page=sell/job/sale_single_reserve&ticketId='+ticketId,"auto"],
        		end:function(ret){
        			if(ticketArr!=null){
        				ticketArr = null;
        				pickedArr = null;
            			data = null;
        			}	
        		}
        	});
    	}
    	
    }
    var reserve = function(pickedArr,type){
    	data = {};
    	pickedArr.each(function(i,p){
    		var $p = $(p);
    		var area = data[$p.attr('areaId')];
    		if(!area){
    			area = [];
    		}
    		area.push($p.attr('oid'));
    		data[$p.attr('areaId')] = area;
    	});
    	layer.open({
    		title:false,
		    type: 2,
		    skin: 'layui-layer-rim', //加上边框
		    area: ['800px', '380px'], //宽高
		    content: [g_path+'/public/common/r2.htmls?page=sell/job/simple_reserve',"no"],
		    end:function(ret){
		    	
		    	if(ticketArr!=null){
		    		var areaIds = _.keys(data);
					synccolor($('#playDate').val(),areaIds);
		    		ticketArr = null;
		    	}
		    	data = null;
		    }
		});
    }
    function quickReserve(pickedArr){
    	var data = {};
    	pickedArr.each(function(i,p){
    		var $p = $(p);
    		var area = data[$p.attr('areaId')];
    		if(!area){
    			area = [];
    		}
    		area.push($p.attr('oid'));
    		data[$p.attr('areaId')] = area;
    	});
    	
    	
    	var param = {};
		var totalSize = pickedArr.length;
		param.foregift = 0;
		param.totalSize = totalSize;
		param.playDate = $("#playDate").val();
		param.showNumberId = 1;
		param.amount = 0;
		param.reserveType = "0214002";
		BaseDwr.reserveTickets(JSON.stringify(data),JSON.stringify(param),function(ret){
			var areaIds = _.keys(data);
			synccolor(param.playDate,areaIds);
			
		});
    }
    var data = null,ticketArr = null;
    var sale = function(pickedArr){
    	data = {};
		pickedArr.each(function(i,p){
    		var $p = $(p);
    		var area = data[$p.attr('areaId')];
    		if(!area){
    			area = [];
    		}
    		area.push($p.attr('oid'));
    		data[$p.attr('areaId')] = area;
    	});
    	
		layer.open({
    		title:false,
    		id:'salewin',
		    type: 2,
		    skin: 'layui-layer-rim', //加上边框
		    area: ['900px', '520px'], //宽高
		    content: g_path+'/public/common/r2.htmls?page=sell/job/sale_print',
		    end:function(ret){
		    	
		    	if(ticketArr!=null){
		    		printTicket(ticketArr,function(){
		    			var areaIds = _.keys(data);
						synccolor($('#playDate').val(),areaIds);
		    			
		    		});
		    		ticketArr = null;
		    	}

		    	data = null;
		    	pickedArr = null;
		    }
		});
    	reservedArr = null;
    	
    }
var changeSeat = function(seatId){
	var obj = $('td[oid='+seatId+"]");
	layer.open({
		title:false,
	    type: 2,
	    skin: 'layui-layer-rim', //加上边框
	    area: ['1000px', '550px'], //宽高
	    content: [g_path+'/public/common/r2.htmls?page=sell/job/change_seat&seatId='+seatId,"no"],
	    end:function(ret){
	    	data = null;
	    	if(ticketArr!=null){
	    		printTicket(ticketArr,function(){
	    			synccolor($('#playDate').val(),[obj.attr('areaId')]);
	    		});
	    		ticketArr = null;
	    	}
	    }
	});
}
/**
 * 查看已售记录，进行改签退票等操作
 */
var stopTicket = function(obj){
	var ticketId = obj.attr("ticketId");
	layer.open({
		title:false,
		type: 2,
		skin: 'layui-layer-rim', //加上边框
		area: ['1000px', '550px'], //宽高
		content: [g_path+'/public/common/r2.htmls?page=sell/job/stop_ticket&ticketId='+ticketId,"no"],
		end:function(ret){
			data = null;
			if(ticketArr!=null){
				printTicket(ticketArr,function(){
					synccolor($('#playDate').val(),[obj.attr('areaId')]);
				});
				ticketArr = null;
			}
		}
	});
}
var lock = function(){
	   mouseoverEvent = 0;
	   var arr = [];
	   var playDate = $('#playDate').val();
	   $("td.toggle").each(function(i,v){
			if(!$(v).hasClass("picked")){
				arr.push($(v).attr("oid"));
			}
		})
		if(arr.length===0){
			return false;
		}
      	$.ajax({
  			url:g_path + '/public/tool/toggleReservedSeat.htmls',
  			data:{'playDate':playDate,showNumberId:1,seatIds:arr},
  			dataType:'json',
  			type:'POST',
  			async: false,
  			success:function(ret){
  				for(var n=0;n<arr.length;n++){
  					var obj = $("td[oid="+arr[n]+"]");
      				switch(ret[arr[n]]){
      				case 0 :
      					pickSeat(obj);
      					break;
      				case 1 :  //别人订了
      					
      					break;
      				default :
      					pickSeat(obj);
      				}
      				
  				}
  				setTimeout(function(){
  					$("#pickedNum").html($("td.picked").length);
  				},1000)
  				
  			}
  		})
  	return arr;
}
var clearLocked = function(){
	var playDate = $('#playDate').val();
	$.ajax({
		url:g_path + '/public/tool/unlockAll.htmls',
		data:{'playDate':playDate,showNumberId:1},
		dataType:'json',
		type:'POST',
		async: true,
		success:function(ret){
			var objs = $('td.picked');
			var areaIds = [];
			objs.each(function(i,obj){
				areaIds[i] = $(obj).attr("areaId");
			})
			synccolor(playDate,areaIds);
		}
	}) 
}
function openEnclose(obj){
	mouseoverEvent = 0;
	layer.open({
		title:false,
		type: 2,
		skin: 'layui-layer-rim', //加上边框
		area: ['800px', '450px'], //宽高
		content: [g_path+"/public/common/r2.htmls?page=sell/job/enclose&name="+obj.attr("kaName"),"no"],
		end:function(ret){
			data = null;
			if(ticketArr!=null){
				printTicket(ticketArr,function(){
			    	var areaId = obj.attr("areaId");
					synccolor($('#playDate').val(),[areaId]);
				});
				ticketArr = null;
			}
		}
	});
}
//选择楼层
var selectFloor = function(index){
	$(".section").hide();
	$(".section:eq("+(index-1)+")").show();
}
var contextmenu = {
		onShow:function(e){
			mouseoverEvent = 0;
			$('#menu li:not(#quit):not(#refrushPage):not(.separator):not(#selectAll)').hide();
			if($('td.selectAll').length>0){
				$('#selectAll').show();
				return;
			}
			var obj = $(e.target);
			$("ul#menu").attr("oid",obj.attr('oid'));
			if($("td.toggle").length===1||$("td.picked").length===1){
       			if($("td.saled.toggle,td.reserved.toggle").length==0){
       				$("#changeSeat").show();
       			}
       		}
			if(obj.hasClass('locked')){
				$.ajax({
					url:g_path+'/public/tool/getLockedUser.htmls',
					data:{playDate:$('#playDate').val(),showNumberId:1,seatId:obj.attr('oid')},
					dataType:'json',
					type:"POST",
					success:function(ret){
						$('#info label').html(ret.name);
					}
				})
       			$("#info").show();
       		}
			if($("td.toggle,td.picked").length>0){//显示预订和快速预订
       			if($("td.saled.toggle,td.reserved.toggle").length==0){
       				$("#reserve").show();
       				$("#quickreserve").show();
       			}
       		}
			if($("td.toggle,td.picked").length>0){//显示售票
       			if($("td.saled.toggle,td.reserved.toggle").length==0){
       				$("#sale").show();
       			}
       		}
			if($("td.picked").length>0){
				$("#clearLocked").show();
			}
			if($('td.toggle').length>0){
				$("#clearSelected").show();
			}
			if(obj.hasClass('reserved')){
				$("#salereserved").show().find("span[name=text]").html($(e.target).attr("info"));
			}
			if(obj.hasClass('saled')){
				$("#saled").show().find("span[name=text]").html($(e.target).attr("info"));
				$("#gaiqian").show().find("span[name=text]").html("【"+obj.html()+"】改签");
				var info = $(e.target).attr("info");
				$("#gaiqianall").show().find("span[name=text]").html(info.substring(0,info.indexOf("】")+1) + "全部改签");
			}
		},
        widthOverflowOffset: 0,
        heightOverflowOffset: 3,
        submenuLeftOffset: -4,
        submenuTopOffset: -5
    }
	function contextEvents(e){
    		var id = $(this).attr("id");
    		switch(id){
    			case "clearLocked":
    				mouseoverEvent = 0;
    				clearLocked();
    				
    				break;
    			case "clearSelected":
    				$('td[oid].toggle').not(".picked").removeClass("toggle");
    				break;
    			case "selectAll":
    				mouseoverEvent = 0;
    				if($('td.selectAll').length===0){
    					var oid = $(this).closest('ul').attr("oid");
        				var td = $('td[oid='+oid+']');
        				var table = td.closest("table");
        				table.find("td[areaId="+td.attr("areaId")+"]:not(.picked,.saled,.reserved,locked)").addClass("selectAll").removeClass("toggle");
        				$(this).find("label").html("关闭指示灯");
    				}else{
    					$(this).find("label").html("打开指示灯");
    					$('td.selectAll').removeClass("selectAll");
    				}
    				
    				break;
    			case "refrushPage":
    		       top.getTickets($('#playDate').val(),1,1);
    		       top.document.getElementById("dataFrame").contentWindow.valueVar();
    		       drawSeatGraphs(1);
             	   window.location.reload();
             	   break;
                case "sale" :
             	   lock();
             	   var oid = $(this).closest('ul').attr("oid");
  				   var td = $('td[oid='+oid+']');
             	   if(td.hasClass("enclose")){
             		    var pickedArr = $('td.picked.enclose');
   	   					if(pickedArr.length>=1){
   	   						openEnclose(td);
   	   					}else{
 	  	   					var pickedArr = $('td.picked:not(.enclose)');
 		   	   				if(pickedArr.length!==0){
 		   	   					sale(pickedArr);
 		   	   				}else{
 		   	   					if(td.hasClass("enclose")){
 		   	   						openEnclose(td);
 		   	   					}else
 		   	   					layer.msg("请选择座位");
 		   	   				}
   	   					}
             	   }else{
             		    var pickedArr = $('td.picked:not(.enclose)');
 	   	   				if(pickedArr.length!==0){
 	   	   					sale(pickedArr);
 	   	   				}else{
 	   	   					pickedArr = $('td.picked.enclose');
 	   	   					if(pickedArr.length===1){
 	   	   						openEnclose(pickedArr[0]);
 	   	   					}else{
 	   	   						layer.msg("请选择座位");
 	   	   					}
 	   	   					
 	   	   				}
             	   }
             	   
             	   break;
                case "salereserved" :
                   var oid = $(this).closest('ul').attr("oid");
                   printReservedSingle($('td[oid='+oid+']'));
             	   break;
                case "saled" :
                	var oid = $(this).closest('ul').attr("oid");
                	stopTicket($('td[oid='+oid+']'));
             	   break;
                case "gaiqian" :
                	var oid = $(this).closest('ul').attr("oid");
                	gaiqian($('td[oid='+oid+']'));
             	   break;
                case "gaiqianall" :
                	var oid = $(this).closest('ul').attr("oid");
                	var obj =  $('td[oid='+oid+']');
                	var ticketId =obj.attr("ticketId");
            		layer.confirm(obj.attr('info')+"确定全部改签吗？",{btn:["确定","取消"]},function(index){
						   BaseDwr.changeDateAllBrother(ticketId,function(ret){
								synccolor2($('#playDate').val(),ret);
							   layer.close(index);
						   });  
					   });
             	   break;
                case "reserve" :
             	   lock();
             	   var pickedArr = $('td.picked');
 	   				if(pickedArr.length!==0){
 	   					reserve(pickedArr);
 	   				}else{
 	   					layer.msg("请选择座位");
 	   				}
             	   break;
                case "quickreserve" :
             	   lock();
             	   var pickedArr = $('td.picked');
 	   				if(pickedArr.length!==0){
 	   					quickReserve(pickedArr);
 	   				}else{
 	   					layer.msg("请选择座位");
 	   				}
             	   break;
                case "quit" :
             	   $('#menu').hide();
             	   break;
                case "changeSeat" :
             	   lock();
             	   var pickedArr = $('td.picked:not(.saled)');
 	   				if(pickedArr.length!==1){
 	   					layer.msg("请选择一个座位");
 	   				}else{
 	   					changeSeat(pickedArr.attr("oid"));
 	   				}
             	   break;
    		}
    		
    	}
function synccolor(playDate,areaIds){
	$('#topPlayDate').html(playDate);
	top.getTickets(playDate,1);
	for(var i=0;i<areaIds.length;i++){
		var id = top.areas[areaIds[i]].code;
		top.setVar(id);
		if(id=="C2"||id=="B2"||id=="B4"){
			$("#C2B2B4").html("").append(top.C2).find("table tbody").append($(top.B2).find("tbody").html()).append($(top.B4).find("tbody").html());
			readyEvents('C2B2B4');
		}else if("C3B3".indexOf(id)!=-1){
			$("#C3B3").html("").append(top.C3).find("table tbody").append($(top.B3).find("tbody").html());
	    	readyEvents('C3B3');
		}else if("C1B1".indexOf(id)!=-1){
			$("#C1B1").html("").append(top.C1).find("table tbody").append($(top.B1).find("tbody").html());
			
	    	readyEvents('C1B1');
		}else if("Y02J01Y04".indexOf(id)!=-1){
			$("#Y02J01Y04").html("").append(top.Y02).find("table tbody").append($(top.J01).find("tbody").html()).append($(top.Y04).find("tbody").html());
	    	readyEvents('Y02J01Y04');
		}else if("G01Q01".indexOf(id)!=-1){
	    	$("#G01Q01").html("").append(top.G01).find("table tbody").append($(top.Q01).find("tbody").html());
	    	readyEvents('G01Q01');
		}else{
			drawSeatGraph(top[id],id);
			readyEvents(id);
		}
		
	}
}
function synccolor2(playDate,areaCodes){
	$('#topPlayDate').html(playDate);
	top.getTickets(playDate,1);
	for(var i=0;i<areaCodes.length;i++){
		var id = areaCodes[i];
		top.setVar(id);
		if(id=="C2"||id=="B2"||id=="B4"){
			$("#C2B2B4").html("").append(top.C2).find("table tbody").append($(top.B2).find("tbody").html()).append($(top.B4).find("tbody").html());
			readyEvents('C2B2B4');
		}else if("C3B3".indexOf(id)!=-1){
			$("#C3B3").html("").append(top.C3).find("table tbody").append($(top.B3).find("tbody").html());
			readyEvents('C3B3');
		}else if("C1B1".indexOf(id)!=-1){
			$("#C1B1").html("").append(top.C1).find("table tbody").append($(top.B1).find("tbody").html());
			
			readyEvents('C1B1');
		}else{
			drawSeatGraph(top[id],id);
			readyEvents(id);
		}
		
	}
}

function showMessage(seatIds,playDate,showNumberId,type){
	if($('#topPlayDate').html()===playDate&&showNumberId==$("#showNumber").val()){
		var seatId = 0;
		for(var i=0;i<seatIds.length;i++){
			seatId = seatIds[i];
			var td = $('td[oid='+seatId+']');
			if(type===0){//被其他人锁定了
				if(seatId==3657){
					$('td[oid=3666],td[oid=3675]').addClass("locked").removeClass("toggle").removeClass("picked");
				}
				if(seatId==3666||seatId==3675){
					$('td[oid=3657]').addClass("locked").removeClass("toggle").removeClass("picked");
				}
				if(seatId==3622){
					$('td[oid=3640],td[oid=3631]').addClass("locked").removeClass("toggle").removeClass("picked");
				}
				if(seatId==3640||seatId==3631){
					$('td[oid=3622]').addClass("locked").removeClass("toggle").removeClass("picked");
				}
				if(seatId==4095||seatId==3581){//长
					td.closest('table').find("td[oid]").addClass("locked").removeClass("toggle").removeClass("picked");
				}else{
					td.addClass("locked").removeClass("toggle").removeClass("picked");
				}
				
    		}else if(type===4){//改签了，取消预订
    			if(seatId==3657){
					$('td[oid=3666],td[oid=3675]').removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass("reserved").removeAttr("ticketId");
				}
    			if(seatId==3666||seatId==3675){
					$('td[oid=3657]').removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass("reserved").removeAttr("ticketId");
				}
    			if(seatId==3622){
					$('td[oid=3640],td[oid=3631]').removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass("reserved").removeAttr("ticketId");
				}
    			
    			if(seatId==3640||seatId==3631){
					$('td[oid=3622]').removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass("reserved").removeAttr("ticketId");
				}
    			if(seatId==4095||seatId==3581){
    				td.closest('table').find("td[oid]").removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass("reserved").removeAttr("ticketId");
    			}else{
    				td.removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass("reserved").removeAttr("ticketId");
    			}
    			
    		}
		}
	}
}
function showMessage2(seatIds,param){
	if($('#topPlayDate').html()===param.playDate&&param.showNumberId==$("#showNumber").val()){
			var seatId = 0;
			for(var i=0;i<seatIds.length;i++){
    			seatId = seatIds[i];
    			if(param.type===2){//售出去了
        			var td = $('td[oid='+seatId.seatId+']');
        			if(param.sallerCode!==""&&param.sallerCode!==null&&param.sallerCode!=='null'){
						info = param.sallerCode;
					}
					if(param.descs!==""&&param.descs!==null){
						info +=" | "+param.descs;
					}
					if($.trim(info)===""){
						info = "";
					}else{
						info = "【"+info+"】";
					}
					info += param.createUserName+""+new Date(param.createTime).format("HH:mm:ss")
					td.attr("info",info);
        			td.removeClass("locked").removeClass("reserved").removeClass("toggle").removeClass("picked").addClass("saled").attr("ticketId",param.ticketId);
        			
        		}else if(param.type===3){
        			var td = $('td[oid='+seatId.seatId+']');
        			td.removeClass("locked").removeClass("toggle").removeClass("picked").addClass("reserved").attr("reserveType",param.reserveType).attr("ticketId",seatId.ticketId);
        			var info = "";
        			if(param.reserveType==="0214002"){
        				
        				info = "快速预订 | ";
        			}else{
        				if(param.sallerCode&&param.sallerCode!=''){
        					info = param.sallerCode + " | ";
        				}
        				if(param.descs&&param.descs!=''){
        					info += param.descs + " | ";
        				}
        			}
        			info += param.createUserName+" | "+new Date(param.createTime).format("HH:mm:ss");
        			td.attr("info",info);
        		}else if(param.type===4){//批量退订，批量改签
        			var td = $('td[oid='+seatId.seatId+']');
        			td.removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass("reserved").removeAttr("ticketId");
        		}
    		}
		
		
	}
}
function readyEvents(id){
	var tds = $('#'+id+' td[oid]');
	if(tds.length==0)return;
	//单击座位，触发事件
 	tds.bind("click",clickTd);

	//右键菜单
	tds.jeegoocontext('menu', contextmenu);
	//鼠标经过时
    tds.mouseover(function(e){
    	var lastOverTd = $(this);;
    	e.preventDefault();
    	if(!lastOverTd.hasClass("locked")&&!lastOverTd.hasClass("reserved")){
    		if(mouseoverEvent===1){
    			
    			if(lastOverTd.hasClass("toggle")){
    				
					if(lastOutTd.has("toggle")&&!lastOutTd.hasClass("picked")){
						
						lastOutTd.removeClass("toggle");
					}else{
						if(!lastOverTd.hasClass("picked")){
							lastOverTd.removeClass("toggle");
	    				}
					}
    			}else{
    				lastOverTd.addClass("toggle").addClass("textCursor");
    			}
    		}
    	}
		
	}).mouseout(function(e){
		e.preventDefault();
		if(mouseoverEvent===1){
			var obj = $(this);
			lastOutTd = obj;
		}
	}); 
	
    
};
function clickTd(e){
	var obj = $(this);
	//显示座位信息
	$("#seatDescs").html(obj.attr("areaName")+"："+obj.attr("price")+"元");
	var oid = obj.attr("oid");
	if(obj.hasClass("reserved")){//只有快速预订的座位才可以单击取消
		if(obj.attr("reserveType")==="0214002"){
			BaseDwr.unreserve([obj.attr("ticketId")],function(ret){
				if(ret==0||ret==1){
					synccolor($('#playDate').val(),[obj.attr("areaId")]); 
				}
			});
		}
		return;
	}
	if(!obj.hasClass("picked")){//无toggle,picked=no
		if(!obj.hasClass("locked")&&!obj.hasClass("disabledTd")&&!obj.hasClass("saled")&&((obj.attr("areaId")!=="17"&&obj.attr("areaId")!=="18")||obj.hasClass("enclose"))){
			if(!obj.hasClass("toggle")){//选择的起点
				if(obj.hasClass("enclose")){
					mouseoverEvent = 0;
				}else{
					mouseoverEvent = 1;
				}
				obj.addClass("toggle").addClass("textCursor");
				return;
			}else{
				if(mouseoverEvent!==1){
					obj.removeClass("toggle");
				}else{
					$('td.textCursor').removeClass('textCursor');
				}
				mouseoverEvent = 0;
			}
		}
	}else{//锁定
		mouseoverEvent = 0;//停止悬停选择
		//通知其他用户
		$.ajax({
			url:g_path + '/public/tool/toggleReservedSeat.htmls',
			data:{'playDate':$('#playDate').val(),showNumberId:$('#showNumber').val(),seatIds:[oid]},
			dataType:'json',
			type:'POST',
			async: true,
			success:function(ret){
				cancelOne(obj);//自己锁定的，这次单击为取消
			}
		}) 
	}
	}
var switchSize = function(t,e){
	var h = $(document.body.scrollHeight)[0];
	var mainl = $(top.document).find('.mainl');
	var mainr = $(top.document).find('.mainr');
	var mainFrame = $(top.document).find('#mainFrame');
	var header = $(top.document).find('#header');
	var footer = $(top.document).find('#footer');
	
	if(mainl.is(":hidden")){
		//退出
		mainl.show();
		mainr.width(top.rightWidth);
		header.show();
		footer.show();
		setTimeout(function(){
			mainFrame.height(h);
		},1)
		t.innerHTML = "全屏";
	}else{
		//全屏，第一次进入时也会执行
		mainl.hide();
		mainr.width(top.availWidth);
		t.innerHTML = "退出全屏";
		header.hide();
		footer.hide();
		//mainr.height(h);
		setTimeout(function(){
			mainFrame.height(h+60);
		},1)
		
	}
	mainl = null;
	mainr = null;
	header = null;
	t = null;
}
var cancelOne = function(obj){
	synccolor($('#playDate').val(),[obj.attr("areaId")]);
}
var pickSeat = function(obj){
	if(obj.hasClass("saled")){
		layer.msg("已售");
	}
	obj.addClass("picked").removeClass("toggle");
	obj.attr("time",new Date().format("yyyy-MM-dd HH:mm:ss"));
	//obj.html(obj.attr('columnNum'));
	obj = null;
}
$(function(){
	//绑定鼠标右键事件
	$("#menu li").bind("click",contextEvents);
	dwr.engine.setActiveReverseAjax(true);
    dwr.engine.setNotifyServerOnPageUnload(true);
	MessagePush.onPageLoad(userId);
    document.oncontextmenu=function(){
    //	return false;
    };
    if($(top.document).find('.mainl').is(":visible")){
		switchSize($('#sizeSwitch')[0]);
		$(top.document).find("iframe[name=mainFrame]").height($('body').height());
	}else{
		$('#sizeSwitch').html("退出全屏");
	}
	//创建完毕
	$('#sizeSwitch').bind("click",function(e){
		switchSize(this,e);
	});
})