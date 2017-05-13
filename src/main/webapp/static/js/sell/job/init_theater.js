var SWICompany = function(name) {
	this.name = name;
	this.theaters = {};
}

/**
 * 整个剧院初始化，加载剧场、楼层、分区、分块等信息
 */
SWICompany.prototype.init = function() {
	this.theaters = genMaps("from Theater");
	var theaters = this.theaters;
	var floors = SWI.util.genArray("from Floor order by theaterId, orders");
	var partitions = SWI.util.genArray("from Partition order by floorId, orders");
	var blocks = SWI.util.genArray("from Block order by partitionId, orders");
	var areas = SWI.util.genArray("from Area order by blockId, orders");
	$.each(floors, function(indFloor, floor) {
		if (theaters[floor.theaterId]) {
			if (!theaters[floor.theaterId].floors) {
				theaters[floor.theaterId].floors = [];
			}
			theaters[floor.theaterId].floors.push(floor);
			$.each(partitions, function(indPart, partition) {
				if (floor.id == partition.floorId) {
					if (!floor.partitions) {
						floor.partitions = [];
					}
					floor.partitions.push(partition);
					$.each(blocks, function(indBlock, block) {
						if (partition.id == block.partitionId) {
							if (!partition.blocks) {
								partition.blocks = [];
							}
							partition.blocks.push(block);
							$.each(areas, function(indArea, area) {
								if (block.id == area.blockId) {
									if (!block.areas) {
										block.areas = [];
									}
									block.areas.push(area);
								}
							});
						}
					});
				}
			});
		}
	});
}

/**
 * 显示指定剧场
 * 
 * @param theaterId
 *            剧场ID
 */
SWICompany.prototype.showFloors = function(theaterId) {
	var theater = this.theaters[theaterId];
	if (theater.floors) {
		$.each(theater.floors, function(floorIndex, floor) {
			
			var divFloor = $('<div class="section" floor="' + floor.id + '" '
					+ (floor.defaults ? '' : 'style="display: none;"') + '>');
			divFloor.appendTo('div#floors');
			var tableFloor = $('<table width="99%" align="center" style="margin-left: 10px" border="0" '
					+ ' cellpadding="0" cellspacing="0"></table>');
			divFloor.append(tableFloor);
			if (floor.partitions) {
				$.each(floor.partitions, function(partitionIndex, partition) {
					var trPart = $('<tr/>');
					var tablePart = $('<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">'
							+ '</table>');
					trPart.appendTo(tablePart);
					var trFloor = $('<tr/>').appendTo(tableFloor);
					var tdFloor = $('<td/>').appendTo(trFloor);
					tablePart.appendTo(tdFloor);
					if (partition.blocks) {
						$.each(partition.blocks, function(blockIndex, block) {
							var blockStyle = '';
							if (block.style) {
								blockStyle = ' style="' + block.style + '"';
							}
							var tdBlock = $('<td id="block_' + block.id + '" ' + blockStyle + '/>').appendTo(trPart);
							if (block.content) {
								tdBlock.append(block.content);
							}
							if (top.AreaLayout === undefined || top.AreaLayout === null) {
								top.AreaLayout = {};
							}
							top.AreaLayout['block_' + block.id] = [];
							if (block.areas) {
								$.each(block.areas, function(areaId, area) {
									top.AreaLayout['block_' + block.id].push(area.code);
								});
							}
						});
					}
				});
			}
		});
	}
}

SWI.company = new SWICompany('演艺中心');
SWI.company.init();