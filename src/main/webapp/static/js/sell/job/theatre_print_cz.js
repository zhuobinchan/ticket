var mouseoverEvent = 0, lastOutTd = null/* 刚刚经过的那个座位，用于鼠标离开时取消座位 */;
var setSeatStatus = function(playDate, id) {// 设置座位状态

}
var cancelOne = function(obj) {

	synccolor($('#playDate').val(), [ obj.attr("areaId") ]);
}

var pickSeat = function(obj) {
	if (obj.hasClass("saled")) {
		layer.msg("已售");
	}
	obj.addClass("picked").removeClass("toggle");
	obj.attr("time", new Date().format("yyyy-MM-dd HH:mm:ss"));
	// obj.html(obj.attr('columnNum'));
	obj = null;
}
// 绘座位图

var switchSize = function(t, e) {
	var height = $(document.body.scrollHeight)[0];
	var mainl = $(top.document).find('.mainl');
	var mainr = $(top.document).find('.mainr');
	var mainFrame = $(top.document).find('#mainFrame');
	var header = $(top.document).find('#header');
	var footer = $(top.document).find('#footer');

	if (mainl.is(":hidden")) {
		// 退出
		mainl.show();
		mainr.width(top.rightWidth);
		header.show();
		footer.show();
		// mainr.height(h);
		setTimeout(function() {
			mainFrame.height(height);
		}, 1)
		t.innerHTML = "全屏";
	} else {
		// 全屏，第一次进入时也会执行
		mainl.hide();
		mainr.width(top.availWidth);
		t.innerHTML = "退出全屏";
		header.hide();
		footer.hide();
		// mainr.height(h);
		setTimeout(function() {
			mainFrame.height(height + 60);
		}, 1)

	}

	mainl = null;
	mainr = null;
	header = null;
	t = null;
}

var drawSeatGraph = function(html, id) {
	var div = $('#' + id).html('');
	div.html(html);
}
var drawSeatGraphs = function(floor) {
	drawLayout(top.AreaLayout);
	$('td button[kaName]').click(function(e) {
		mouseoverEvent = 0;
		openEnclose($(this).attr('kaName'));
		e.stopPropagation();
	})
	for (var i = 0; i < top.areaNames.length; i++) {
		var areaName = top.areaNames[i];
		readyEvents(areaName);
	}
	// readyEvents('leftArea');
	// readyEvents('centerArea');
	// readyEvents('rightArea');
	// readyEvents('leftestMiddle');
	// readyEvents('leftMiddle');
	// readyEvents('centerMiddle');
	// readyEvents('rightMiddle');
	// readyEvents('rightestMiddle');
}
// var h = $(document).height();// 计算当前容器的高度
$(document).ready(function() {
	$('#title').delay("slow").fadeIn();
	SWI.company.showFloors(currentTheaterId);
	drawSeatGraphs(1);
	$('#progress').hide();
});

function readyEvents(id) {
	var tds = $('#' + id + ' td[oid]');
	if (tds.length == 0)
		return;
	// 单击座位，触发事件
	tds.bind("click", clickTd);

	// 右键菜单
	tds.jeegoocontext('menu', contextmenu);
	// 鼠标经过时
	tds.mouseover(function(e) {
		var lastOverTd = $(this);
		;
		e.preventDefault();
		if (!lastOverTd.hasClass("locked") && !lastOverTd.hasClass("reserved")) {
			if (mouseoverEvent === 1) {

				if (lastOverTd.hasClass("toggle")) {

					if (lastOutTd.has("toggle") && !lastOutTd.hasClass("picked")) {

						lastOutTd.removeClass("toggle");
					} else {
						if (!lastOverTd.hasClass("picked")) {
							lastOverTd.removeClass("toggle");
						}
					}
				} else {
					lastOverTd.addClass("toggle").addClass("textCursor");
				}
			}
		}

	}).mouseout(function(e) {
		e.preventDefault();
		if (mouseoverEvent === 1) {
			var obj = $(this);
			lastOutTd = obj;
		}
	});

};
function clickTd(e) {
	var obj = $(this);
	// 显示座位信息
	$("#seatDescs").html(obj.attr("areaName") + "：" + obj.attr("price") + "元");
	var oid = obj.attr("oid");
	if (obj.hasClass("reserved")) {// 只有快速预订的座位才可以单击取消
		if (obj.attr("reserveType") === "0214002") {
			BaseDwr.unreserve([ obj.attr("ticketId") ], function(ret) {
				if (ret == 0 || ret == 1) {
					obj.removeClass("reserved").removeAttr("ticketId");
					;
				}
			});
		}
		return;
	}
	if (!obj.hasClass("picked")) {// 无toggle,picked=no
		if (!obj.hasClass("locked") && !obj.hasClass("saled")
				&& (obj.attr("areaId") !== "17" && obj.attr("areaId") !== "18" || obj.hasClass("enclose"))) {
			if (!obj.hasClass("toggle")) {// 选择的起点
				mouseoverEvent = 1;
				obj.addClass("toggle").addClass("textCursor");
				return;
			} else {
				if (mouseoverEvent !== 1) {
					obj.removeClass("toggle");
				} else {
					$('td.textCursor').removeClass('textCursor');
				}
				mouseoverEvent = 0;
			}
		}
	} else {// 锁定
		mouseoverEvent = 0;// 停止悬停选择
		// 通知其他用户
		$.ajax({
			url : g_path + '/public/tool/toggleReservedSeat.htmls',
			data : {
				'playDate' : $('#playDate').val(),
				showNumberId : $('#showNumber').val(),
				seatIds : [ oid ]
			},
			dataType : 'json',
			type : 'POST',
			async : true,
			success : function(ret) {
				cancelOne(obj);// 自己锁定的，这次单击为取消
			}
		})
	}
}
// 后台推送调用此方法
function showMessage(seatIds, playDate, showNumberId, type) {
	if ($('#topPlayDate').html() === playDate && showNumberId == $("#showNumber").val()) {
		var seatId = 0;
		for (var i = 0; i < seatIds.length; i++) {
			seatId = seatIds[i];
			var td = $('td[oid=' + seatId + ']');
			if (type === 0) {// 被其他人锁定了
				td.addClass("locked").removeClass("toggle").removeClass("picked");
			} else if (type === 4) {// 改签了，取消预订
				td.removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass(
						"reserved").removeAttr("ticketId");
			}
		}
	}
}
function showMessage2(seatIds, param) {
	if ($('#topPlayDate').html() === param.playDate && param.showNumberId == $("#showNumber").val()) {
		var seatId = 0;
		for (var i = 0; i < seatIds.length; i++) {
			seatId = seatIds[i];
			if (param.type === 2) {// 售出去了
				var td = $('td[oid=' + seatId.seatId + ']');
				if (param.sallerCode !== "" && param.sallerCode !== null && param.sallerCode !== 'null') {
					info = param.sallerCode;
				}
				if (param.descs !== "" && param.descs !== null) {
					info += " | " + param.descs;
				}
				if ($.trim(info) === "") {
					info = "";
				} else {
					info = "【" + info + "】";
				}
				info += param.createUserName + "" + new Date(param.createTime).format("HH:mm:ss")
				td.attr("info", info);
				td.removeClass("locked").removeClass("reserved").removeClass("toggle").removeClass("picked").addClass(
						"saled").attr("ticketId", param.ticketId);

			} else if (param.type === 3) {
				var td = $('td[oid=' + seatId.seatId + ']');
				td.removeClass("locked").removeClass("toggle").removeClass("picked").addClass("reserved").attr(
						"reserveType", param.reserveType).attr("ticketId", seatId.ticketId);
				var info = "";
				if (param.reserveType === "0214002") {

					info = "快速预订 | ";
				} else {
					if (param.sallerCode && param.sallerCode != '') {
						info = param.sallerCode + " | ";
					}
					if (param.descs && param.descs != '') {
						info += param.descs + " | ";
					}
				}
				info += param.createUserName + " | " + new Date(param.createTime).format("HH:mm:ss");
				td.attr("info", info);
			} else if (param.type === 4) {// 批量退订，批量改签
				var td = $('td[oid=' + seatId.seatId + ']');
				td.removeClass("locked").removeClass("saled").removeClass("toggle").removeClass("picked").removeClass(
						"reserved").removeAttr("ticketId");
			}
		}

	}
}
