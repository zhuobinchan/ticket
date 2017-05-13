/**
 * 画座位图布局
 */
function drawLayout(layout) {
	$.each(layout, function(name, group) {
		if (group && group.length > 0) {
			drawAreaInLayout(name, group);
		}
	});
}

/**
 * 刷新指定区域的座位图布局分组
 * 
 * @param layout
 *            布局
 * @param area
 *            指定区域
 */
function refreshLayout(layout, area) {
	$.each(layout, function(name, group) {
		if ($.inArray(area, group) >= 0) {
			drawAreaInLayout(name, group);
		}
	});
}

/**
 * 画座位图布局里面的某个分组
 * 
 * @param name
 *            分组名称
 * @param group
 *            分组里面的区域数组
 */
function drawAreaInLayout(name, group) {
	$('#' + name +' table').remove();
	$.each(group, function(i, area) {
		if (i == 0) {
			$('#' + name).append(top[area]);
		} else {
			$('#' + name).find("table tbody").append($(top[area]).find("tbody").html());
		}
	});
	readyEvents(name);
}

function synccolor(playDate, areaIds) {
	$('#topPlayDate').html(playDate);
	var floor = $('div.section:visible').attr('floor');
	top.getTickets(playDate, 1, floor);
	for (var i = 0; i < areaIds.length; i++) {
		var id = top.areas[areaIds[i]].code;
		top.setVar(id);
		refreshLayout(top.AreaLayout, id);
	}
}
function synccolor2(playDate, areaCodes) {
	$('#topPlayDate').html(playDate);
	var floor = $('div.section:visible').attr('floor');
	top.getTickets(playDate, 1, floor);
	for (var i = 0; i < areaCodes.length; i++) {
		var id = areaCodes[i];
		top.setVar(id);
		refreshLayout(top.AreaLayout, id);
	}
}

/**
 * 根据楼层ID选择楼层
 */
var selectFloor = function(floorId) {
	$(".section").hide();
	var section = $(".section[floor='" + floorId + "']").show();
}