var SWI;
if (SWI === undefined || SWI === null) {
	SWI = {};
}
if (SWI.setting === undefined || SWI.setting === null) {
	SWI.setting = {};
}
SWI.setting.parameter = function(id, setting) {
	this.id = id;
	this.setting = setting;
}

/**
 * 初始化页面
 */
SWI.setting.parameter.prototype.initPage = function() {
	var layout = new dhtmlXLayoutObject(document.body, "1C");
	layout.cells('a').setText('参数配置');
	var grid = layout.cells('a').attachGrid();
	grid.setHeader("类型,键值,配置1,配置2,配置3");
	grid.attachHeader("#connector_select_filter,#connector_text_filter,#connector_text_filter")
	grid.setInitWidths("200,150,400,*,*");
	grid.setColTypes("ed,ed,txt,txt,txt");
	grid.setColSorting("connector,connector,connector,connector,connector");
	grid.enableSmartRendering(true);
	grid.enableMultiselect(true);
	grid.load(SWI.util.getContextPath() + '/public/util/parameterGrid.htmls');
	grid.init();
	var dp = new dataProcessor(SWI.util.getContextPath() + '/public/util/parameterGrid.htmls');
	dp.init(grid);

	var tbar = layout.cells('a').attachToolbar();
	tbar.setAlign('right');
	tbar.setIconsPath(SWI.util.getContextPath() + '/static/dhtmlx/codebase/imgs/dhxtree_material/');
	tbar.addButton('delete', 0, '删除', 'iconCheckGray.gif');
	tbar.addButton('add', 0, '新增', 'iconCheckAll.gif');
	tbar.attachEvent('onClick', function(id) {
		if (id == 'add') {
			var rid = grid.uid();
			grid.addRow(rid, ',,,,', 0);
			grid.showRow(rid);
		} else if (id == 'delete') {
			grid.deleteSelectedRows();
		}
	});

}

$(document).ready(function() {
	SWI.util.adjustPageHeight();
	$(document.body).height(window.screen.height - 150);
	var parameter = new SWI.setting.parameter();
	parameter.initPage();
	SWI.util.adjustPageHeight();
});
