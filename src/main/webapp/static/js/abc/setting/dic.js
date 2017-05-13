var SWI;
if (SWI === undefined || SWI === null) {
	SWI = {};
}
if (SWI.setting === undefined || SWI.setting === null) {
	SWI.setting = {};
}
SWI.setting.dic = function(id, setting) {
	this.id = id;
	this.setting = setting;
}

/**
 * 初始化页面
 */
SWI.setting.dic.prototype.initPage = function() {
	var layout = new dhtmlXLayoutObject(document.body, "2U");
	var rightPanel = layout.cells('b');
	rightPanel.setText('数据字典列表');
	var grid = rightPanel.attachGrid();
	grid.setHeader("编码,说明,父ID,排序,层级");
	grid.attachHeader("#connector_text_filter,#connector_text_filter,#connector_text_filter,"
			+ "#connector_text_filter,#connector_select_filter")
	grid.setInitWidths("150,200,100,100,*");
	grid.setColTypes("ed,ed,edn,edn,edn");
	grid.setColSorting("connector,connector,connector,connector,connector");
	grid.enableSmartRendering(true);
	grid.enableMultiselect(true);
	grid.load(SWI.util.getContextPath() + '/public/util/dicGrid.htmls');
	grid.init();
	var dp = new dataProcessor(SWI.util.getContextPath() + '/public/util/dicGrid.htmls');
	dp.init(grid);

	var leftPanel = layout.cells('a');
	leftPanel.setWidth(250);
	leftPanel.setText('数据字典树');
	var tree = leftPanel.attachTreeView();
	tree.attachEvent('onSelect', function(id, mode) {
		grid.filterBy(2, function(data) {
			return data == id;
		});
	});
	tree.loadStruct(SWI.util.getContextPath() + '/public/util/dicTree.htmls', function() {
		tree.selectItem(1);
	});
	var dpTree = new dataProcessor(SWI.util.getContextPath() + '/public/util/dicTree.htmls');
	dpTree.init(tree);

	var tbar = layout.cells('b').attachToolbar();
	tbar.setAlign('right');
	tbar.setIconsPath(SWI.util.getContextPath() + '/static/dhtmlx/codebase/imgs/dhxtree_material/');
	tbar.addButton('delete', 0, '删除', 'iconCheckGray.gif');
	tbar.addButton('add', 0, '新增', 'iconCheckAll.gif');
	tbar.attachEvent('onClick', function(id) {
		if (id == 'add') {
			var rid = grid.uid();
			var parentId = '';
			var order = '';
			var level = '';
			if (grid.getRowsNum() > 0) {
				parentId = grid.cellByIndex(0, 2).getValue();
				order = parseInt(grid.cellByIndex(grid.getRowsNum() - 1, 3).getValue()) + 1;
				level = grid.cellByIndex(0, 4).getValue();
			}
			grid.addRow(rid, ',,' + parentId + ',' + order + ',' + level, 0);
			grid.showRow(rid);
		} else if (id == 'delete') {
			grid.deleteSelectedRows();
		}
	});

}

$(document).ready(function() {
	SWI.util.adjustPageHeight();
	$(document.body).height(window.screen.height - 150);
	var dic = new SWI.setting.dic();
	dic.initPage();
	SWI.util.adjustPageHeight();
});
