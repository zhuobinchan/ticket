package com.tjing.bussiness.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjing.bussiness.model.Goods;
import com.tjing.bussiness.model.GoodsType;
import com.tjing.bussiness.model.MemberCard;
import com.tjing.bussiness.model.PackageGoods;
import com.tjing.bussiness.model.Packages;
import com.tjing.bussiness.model.Salesman;
import com.tjing.bussiness.model.Seat;
import com.tjing.bussiness.model.VenueCommision;
import com.tjing.bussiness.model.VenueSales;
import com.tjing.bussiness.model.VenueSalesDetail;
import com.tjing.bussiness.support.BusinessException;
import com.tjing.frame.model.Dic;
import com.tjing.frame.model.User;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.SimpleDao;

@Service
public class VenueDwr {
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private DbServices dbServices;
	@Autowired
	private MemberDwr memberDwr;

	/**
	 * 新增或修改商品类型
	 * 
	 * @param parentId
	 *            父类型ID
	 * @param name
	 *            名称
	 * @param text
	 *            说明
	 * @param order
	 *            排序
	 * @param remark
	 *            备注
	 * @param typeId
	 *            商品类型ID，为空则新增，不为空则修改
	 * @return
	 */
	public String saveGoodsType(Integer parentId, String name, String text, Integer order, String remark,
			Integer typeId) {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		GoodsType type = new GoodsType();
		if (typeId != null) {
			type = simpleDao.getEntity(GoodsType.class, typeId);
			if (type == null) {
				return "找不到ID为 " + typeId + "的商品类型";
			}
		}
		type.setParentId(parentId);
		type.setName(name);
		type.setText(text);
		if (order == null) {
			order = simpleDao
					.getIntNumByHql("select IFNULL(max(orderno), 0) + 1 from GoodsType where parentId = " + parentId);
		}
		type.setOrderno(order);
		type.setRemark(remark);
		type.setUpdateTime(now);
		type.setUpdateUserId(user.getId());
		if (typeId == null) {
			type.setStatus("0202001");
			type.setCreateTime(now);
			type.setCreateUserId(user.getId());
			simpleDao.persist(type);
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("GoodsType"), "新增商品类型ID为 " + type.getId());
		} else {
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("GoodsType"), "修改ID为 " + typeId + " 的商品类型");
		}
		return "保存商品类型成功";
	}

	/**
	 * 获取商品类型信息
	 * 
	 * @param typeId
	 *            商品类型ID
	 * @return
	 */
	public Map<String, Object> getGoodsType(Integer typeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (typeId != null) {
			GoodsType type = simpleDao.getEntity(GoodsType.class, typeId);
			if (type != null) {
				map.put("parentId", type.getParentId());
				map.put("name", type.getName());
				map.put("text", type.getText());
				map.put("order", type.getOrderno());
				map.put("remark", type.getRemark());
				map.put("status", type.getStatus());
			}
		}
		return map;
	}

	/**
	 * 删除商品类型
	 * 
	 * @param ids
	 *            商品类型ID
	 * @return 成功删除的数量
	 */
	public int deleteGoodsType(Integer[] ids) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<GoodsType> list = simpleDao.getListByHqlWithStrParams("from GoodsType where id in(:ids)", paramMap);
		for (GoodsType type : list) {
			int subCount = simpleDao.getIntNumByHql("select count(*) from GoodsType where parentId = " + type.getId());
			if (subCount <= 0) {
				int goodsCount = simpleDao.getIntNumByHql("select count(*) from Goods where typeId = " + type.getId());
				if (goodsCount > 0) {
					type.setStatus("0202002");
					dbServices.addOperateLog(user, CodeHelper.getClassInfo("GoodsType"),
							"ID为 " + type.getId() + " 的商品类型设置为无效");
				} else {
					dbServices.addOperateLog(user, CodeHelper.getClassInfo("GoodsType"),
							"删除ID为 " + type.getId() + " 的商品类型");
					simpleDao.deleteEntity(type);
				}
				count++;
			}
		}
		return count;
	}

	/**
	 * 保存商品资料
	 * 
	 * @param typeId
	 *            商品类型ID
	 * @param name
	 *            名称
	 * @param unit
	 *            单位
	 * @param price
	 *            单价
	 * @param discount
	 *            是否打折
	 * @param dispatch
	 *            是否配送
	 * @param code
	 *            编码
	 * @param process
	 *            是否加工
	 * @param cost
	 *            成本
	 * @param area
	 *            出品区域
	 * @param barCode
	 *            条形码
	 * @param order
	 *            排序
	 * @param remark
	 *            备注
	 * @param commision
	 *            提成
	 * @param goodsId
	 *            商品ID，为空则新增，不为空则修改
	 * @return
	 */
	public String saveGoods(Integer typeId, String name, String unit, Float price, String discount, String dispatch,
			String code, String process, Float cost, String area, String barCode, Integer order, String remark,
			String commision, Integer goodsId) {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		Goods goods = new Goods();
		if (goodsId != null) {
			goods = simpleDao.getEntity(Goods.class, goodsId);
			if (goods == null) {
				return "找不到ID为 " + goodsId + " 的商品资料";
			}
		}
		goods.setTypeId(typeId);
		goods.setName(name);
		goods.setUnit(unit);
		goods.setPrice(price);
		goods.setDiscount(discount);
		goods.setDispatch(dispatch);
		goods.setCode(code);
		goods.setProcess(process);
		goods.setCost(cost);
		goods.setArea(area);
		goods.setBarCode(barCode);
		if (order == null) {
			order = simpleDao.getIntNumByHql("select IFNULL(max(orderno), 0) + 1 from Goods where typeId = " + typeId);
		}
		goods.setOrderno(order);
		goods.setRemark(remark);
		goods.setCommision(commision);
		goods.setStatus("0202001");
		goods.setUpdateTime(now);
		goods.setUpdateUserId(user.getId());
		if (goodsId == null) {
			goods.setCreateTime(now);
			goods.setCreateUserId(user.getId());
			simpleDao.persist(goods);
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("Goods"), "新增商品ID为 " + goods.getId());
		} else {
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("Goods"), "修改ID为 " + goodsId + " 的商品资料");
		}
		return "保存商品资料成功";
	}

	/**
	 * 获取商品资料
	 * 
	 * @param goodsId
	 *            商品ID
	 * @return
	 */
	public Map<String, Object> getGoodsInfo(Integer goodsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (goodsId != null) {
			Goods goods = simpleDao.getEntity(Goods.class, goodsId);
			if (goods != null) {
				map.put("typeId", goods.getTypeId());
				map.put("name", goods.getName());
				map.put("unit", goods.getUnit());
				map.put("price", goods.getPrice());
				map.put("discount", goods.getDiscount());
				map.put("dispatch", goods.getDispatch());
				map.put("code", goods.getCode());
				map.put("process", goods.getProcess());
				map.put("cost", goods.getCost());
				map.put("area", goods.getArea());
				map.put("barCode", goods.getBarCode());
				map.put("order", goods.getOrderno());
				map.put("remark", goods.getRemark());
				map.put("commision", goods.getCommision());
				GoodsType type = simpleDao.getEntity(GoodsType.class, goods.getTypeId());
				map.put("bigTypeId", type.getParentId());
			}
		}
		return map;
	}

	/**
	 * 删除商品
	 * 
	 * @param ids
	 *            待删除的商品ID
	 * @return 被删除的数量
	 */
	public int deleteGoods(Integer[] ids) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<Goods> list = simpleDao.getListByHqlWithStrParams("from Goods where id in(:ids)", paramMap);
		for (Goods goods : list) {
			goods.setStatus("0202002");
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("Goods"), "ID为 " + goods.getId() + " 的商品设置为无效");
			count++;
		}
		return count;
	}

	/**
	 * 保存套餐资料
	 * 
	 * @param name
	 *            套餐名称
	 * @param unit
	 *            套餐单位
	 * @param price
	 *            套餐价格
	 * @param remark
	 *            套餐备注
	 * @param packageId
	 *            套餐ID，为空则新增，不为空则修改
	 * @return 套餐ID
	 */
	public int savePackage(String name, String unit, Float price, String remark, Integer packageId)
			throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		Packages pack = new Packages();
		if (packageId != null) {
			pack = simpleDao.getEntity(Packages.class, packageId);
			if (pack == null) {
				throw new BusinessException("找不到ID为 " + packageId + " 的商品套餐");
			}
		}
		pack.setName(name);
		pack.setUnit(unit);
		pack.setPrice(price);
		pack.setRemark(remark);
		pack.setUpdateTime(now);
		pack.setUpdateUserId(user.getId());
		if (packageId == null) {
			pack.setCreateTime(now);
			pack.setCreateUserId(user.getId());
			pack.setStatus("0202001");
			simpleDao.persist(pack);
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("Packages"), "新增ID为 " + pack.getId() + " 的商品套餐");
		} else {
			int salesCount = simpleDao
					.getIntNumByHql("select count(*) from VenueSales where packageId = " + pack.getId());
			if (salesCount > 0) {
				throw new BusinessException("该套餐曾经消费，不能修改");
			}
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("Packages"), "修改ID为 " + packageId + " 的商品套餐");
		}

		return pack.getId();
	}

	/**
	 * 获取商品套餐资料
	 * 
	 * @param packageId
	 *            套餐ID
	 * @return
	 */
	public Map<String, Object> getPackageInfo(Integer packageId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (packageId != null) {
			Packages pack = simpleDao.getEntity(Packages.class, packageId);
			if (pack != null) {
				map.put("name", pack.getName());
				map.put("unit", pack.getUnit());
				map.put("price", pack.getPrice());
				map.put("remark", pack.getRemark());
			}
		}
		return map;
	}

	/**
	 * 删除商品套餐
	 * 
	 * @param ids
	 *            待删除的商品套餐
	 * @return 成功删除的数量
	 */
	public int deletePackage(Integer[] ids) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<Packages> list = simpleDao.getListByHqlWithStrParams("from Packages where id in(:ids)", paramMap);
		for (Packages pack : list) {
			int salesCount = simpleDao
					.getIntNumByHql("select count(*) from VenueSales where packageId = " + pack.getId());
			if (salesCount > 0) {
				pack.setStatus("0202002");
				pack.setUpdateTime(new Date());
				pack.setUpdateUserId(user.getId());
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("GoodsType"),
						"ID为 " + pack.getId() + " 的商品套餐设置为无效");
			} else {
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("GoodsType"),
						"删除ID为 " + pack.getId() + " 的商品套餐");
				simpleDao.deleteEntity(pack);
			}
			count++;
		}
		return count;
	}

	/**
	 * 保存套餐商品关联关系
	 * 
	 * @param packageId
	 *            套餐ID
	 * @param goodsId
	 *            商品ID
	 * @param count
	 *            商品数量
	 * @param remark
	 *            备注
	 * @param id
	 *            关系ID，为空则新增，不为空则修改
	 * @return
	 */
	public String savePackageGoods(Integer packageId, Integer goodsId, Integer count, String remark, Integer id) {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		Packages pack = simpleDao.getEntity(Packages.class, packageId);
		if (pack == null) {
			return "找不到ID为 " + id + " 的套餐";
		} else if (!"0202001".equals(pack.getStatus())) {
			return "ID为 " + id + " 的套餐不是有效状态，不能修改关联关系";
		}
		int salesCount = simpleDao.getIntNumByHql("select count(*) from VenueSales where packageId = " + packageId);
		if (salesCount > 0) {
			return "该套餐曾经消费，不能修改关联关系";
		}
		PackageGoods pg = new PackageGoods();
		if (id != null) {
			pg = simpleDao.getEntity(PackageGoods.class, id);
			if (pg == null) {
				return "找不到ID为 " + id + " 的套餐商品关联关系";
			}
		}
		pg.setPackageId(packageId);
		pg.setGoodsId(goodsId);
		pg.setGoodsCount(count);
		pg.setRemark(remark);
		pg.setUpdateTime(now);
		pg.setUpdateUserId(user.getId());
		if (id == null) {
			pg.setCreateTime(now);
			pg.setCreateUserId(user.getId());
			simpleDao.persist(pg);
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("PackageGoods"),
					"新增ID为 " + pg.getId() + " 的套餐商品关联关系");
		} else {
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("PackageGoods"),
					"修改ID为 " + pg.getId() + " 的套餐商品关联关系");
		}
		return "保存套餐商品关联关系成功";
	}

	/**
	 * 获取套餐商品关联资料
	 * 
	 * @param pgId
	 *            关联ID
	 * @return
	 */
	public Map<String, Object> getPackageGoods(Integer pgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (pgId != null) {
			PackageGoods pg = simpleDao.getEntity(PackageGoods.class, pgId);
			if (pg != null) {
				map.put("packageId", pg.getPackageId());
				map.put("goodsId", pg.getGoodsId());
				map.put("count", pg.getGoodsCount());
				map.put("remark", pg.getRemark());
				Goods goods = simpleDao.getEntity(Goods.class, pg.getGoodsId());
				if (goods != null) {
					map.put("smallId", goods.getTypeId());
					GoodsType small = simpleDao.getEntity(GoodsType.class, goods.getTypeId());
					if (small != null) {
						map.put("bigId", small.getParentId());
					}
				}
			}
		}
		return map;
	}

	/**
	 * 删除套餐商品关联关系
	 * 
	 * @param ids
	 *            待删除关联关系
	 * @return 成功删除的数量
	 */
	public int deletePackageGoods(Integer[] ids) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<PackageGoods> list = simpleDao.getListByHqlWithStrParams("from PackageGoods where id in(:ids)", paramMap);
		for (PackageGoods pg : list) {
			int salesCount = simpleDao
					.getIntNumByHql("select count(*) from VenueSales where packageId = " + pg.getPackageId());
			if (salesCount <= 0) {
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("PackageGoods"),
						"删除ID为 " + pg.getId() + " 的套餐商品关联关系");
				simpleDao.deleteEntity(pg);
				count++;
			}
		}
		return count;
	}

	/**
	 * 保存场内消费单
	 * 
	 * @param seatId
	 *            座位ID
	 * @param salesmanId
	 *            营销员ID
	 * @param receivable
	 *            应收金额
	 * @param realPay
	 *            实收金额
	 * @param payType
	 *            支付方式
	 * @param code
	 *            充值卡号
	 * @param strategy
	 *            优惠策略
	 * @param packageId
	 *            套餐ID
	 * @param remark
	 *            备注
	 * @param status
	 *            状态
	 * @param fill
	 *            补差价
	 * @param fillPayType
	 *            补差价支付方式
	 * @param salesId
	 *            消费单ID，为空则新增，不为空则修改
	 * @return 消费单ID
	 * @throws BusinessException
	 */
	public Integer saveVenueSales(Integer seatId, Integer salesmanId, Float receivable, Float realPay, String payType,
			String code, String strategy, Integer packageId, String remark, String status, Float fill,
			String fillPayType, String[][] details, Integer salesId) throws BusinessException {
		if (fill == null) {
			fill = 0f;
		}
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		VenueSales sales = new VenueSales();
		if (salesId != null) {
			sales = simpleDao.getEntity(VenueSales.class, salesId);
			if (sales == null) {
				throw new BusinessException("找不到ID为 " + salesId + " 的场内消费单");
			}
		}
		MemberCard card = null;
		if (code != null && !"".equals(code.trim())) {
			card = memberDwr.getCardByCode(code);
			sales.setCardId(card.getId());
		}
		sales.setSeatId(seatId);
		sales.setSalesmanId(salesmanId);
		sales.setReceivable(receivable);
		sales.setRealPay(realPay);
		sales.setPayType(payType);
		sales.setStatus(status);
		sales.setStrategy(strategy);
		sales.setPackageId(packageId);
		sales.setRemark(remark);
		sales.setFillDifference(fill);
		sales.setFillPayType(fillPayType);
		sales.setUpdateTime(now);
		sales.setUpdateUserId(user.getId());
		if (salesId == null) {
			sales.setCreateTime(now);
			sales.setCreateUserId(user.getId());
			simpleDao.persist(sales);
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"), "新增场内消费单ID为 " + sales.getId());
			// 生成消费流水号
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			String serial = df.format(now);
			String suffix = "000000";
			int len = suffix.length();
			suffix = suffix + sales.getId();
			serial = serial + suffix.substring(suffix.length() - len);
			sales.setSaleNo(serial);

			// 消费明细
			if (details != null && details.length > 0) {
				for (int i = 0; i < details.length; i++) {
					String[] record = details[i];
					if (record.length >= 4) {
						int goodsId = Integer.parseInt(record[0]);
						int count = Integer.parseInt(record[1]);
						float price = Float.parseFloat(record[2]);
						float total = Float.parseFloat(record[3]);
						saveVenueSalesDetail(sales.getId(), goodsId, count, price, total, null, null);
					}
				}
			}
		} else {
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"), "修改场内消费单ID为 " + salesId);
		}

		// 下单扣费充值卡
		if ("0227002".equals(status) && "0207010".equals(payType) && card != null) {
			memberDwr.cardConsume(card.getId(), realPay - fill, sales.getSaleNo(), "0226002", remark);
		}
		return sales.getId();
	}

	/**
	 * 取消场内消费单状态，如果是充值卡消费的，要返还余额及扣除积分
	 * 
	 * @param ids
	 *            消费单ID
	 * @return 成功变更状态的数量
	 * @throws BusinessException
	 */
	public int cancelVenueSales(Integer[] ids) throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<VenueSales> list = simpleDao.getListByHqlWithStrParams("from VenueSales where id in(:ids)", paramMap);
		for (VenueSales sales : list) {
			if ("0227001".equals(sales.getStatus()) || "0227002".equals(sales.getStatus())) {
				sales.setStatus("0227004");
				sales.setUpdateTime(new Date());
				sales.setUpdateUserId(user.getId());
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"),
						"取消ID为 " + sales.getId() + " 的场内消费单");
				count++;

				// 如果是充值卡消费的，要返还余额及扣除积分
				if ("0207010".equals(sales.getPayType())) {
					memberDwr.cancelCardConsume(sales.getSaleNo(), sales.getRealPay());
				}
			}
		}
		return count;
	}

	/**
	 * 结算场内消费单
	 * 
	 * @param ids
	 *            待结算的消费单ID
	 * @return 成功结算的数量
	 * @throws BusinessException
	 */
	public int settleVenueSales(Integer[] ids) throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<VenueSales> list = simpleDao.getListByHqlWithStrParams("from VenueSales where id in(:ids)", paramMap);
		for (VenueSales sales : list) {
			if ("0227002".equals(sales.getStatus())) {
				sales.setStatus("0227003");
				sales.setUpdateTime(new Date());
				sales.setUpdateUserId(user.getId());
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"),
						"结算ID为 " + sales.getId() + " 的场内消费单");
				count++;
			}
		}
		return count;
	}

	/**
	 * 获取场内消费单
	 * 
	 * @param id
	 *            消费单ID
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getVenueSales(Integer id) throws BusinessException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (id == null) {
			throw new BusinessException("请提供场内消费单ID");
		}
		VenueSales sales = simpleDao.getEntity(VenueSales.class, id);
		if (sales == null) {
			throw new BusinessException("找不到ID为 " + id + " 的场内消费单");
		}
		map.put("saleNo", sales.getSaleNo());
		map.put("seatId", sales.getSeatId());
		map.put("salesmanId", sales.getSalesmanId());
		map.put("receivable", sales.getReceivable());
		map.put("realPay", sales.getRealPay());
		map.put("payType", sales.getPayType());
		map.put("status", sales.getStatus());
		map.put("cardId", sales.getCardId());
		map.put("strategy", sales.getStrategy());
		map.put("packageId", sales.getPackageId());
		map.put("remark", sales.getRemark());
		map.put("createTime", sales.getCreateTime());

		String salesmanName = "";
		Salesman salesman = simpleDao.getEntity(Salesman.class, sales.getSalesmanId());
		if (salesman != null) {
			salesmanName = salesman.getName();
		}
		map.put("salesmanName", salesmanName);
		String createUserName = "";
		User user = simpleDao.getEntity(User.class, sales.getCreateUserId());
		if (user != null) {
			createUserName = user.getName();
		}
		map.put("createUserName", createUserName);
		String seatName = "";
		Seat seat = simpleDao.getEntity(Seat.class, sales.getSeatId());
		if (seat != null) {
			seatName = seat.getName();
		}
		map.put("seatName", seatName);
		return map;
	}

	/**
	 * 删除场内消费单
	 * 
	 * @param ids
	 *            待删除的场内消费单ID
	 * @return 成功删除的数量
	 * @throws BusinessException
	 */
	public int deleteVenueSales(Integer[] ids) throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<VenueSales> list = simpleDao.getListByHqlWithStrParams("from VenueSales where id in(:ids)", paramMap);
		for (VenueSales sales : list) {
			if (!"0227003".equals(sales.getStatus())) {
				sales.setStatus("0227005");
				sales.setUpdateTime(new Date());
				sales.setUpdateUserId(user.getId());
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"),
						"删除ID为 " + sales.getId() + " 的场内消费单");
			}
			count++;
		}
		return count;
	}

	/**
	 * 保存场内消费明细
	 * 
	 * @param salesId
	 *            消费单ID
	 * @param goodsId
	 *            商品ID
	 * @param count
	 *            商品数量
	 * @param price
	 *            商品单价
	 * @param total
	 *            商品合计
	 * @param remark
	 *            备注
	 * @param detailId
	 *            明细ID，为空则新增，不为空则修改
	 * @throws BusinessException
	 */
	public void saveVenueSalesDetail(Integer salesId, Integer goodsId, Integer count, Float price, Float total,
			String remark, Integer detailId) throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		VenueSalesDetail detail = new VenueSalesDetail();
		if (detailId != null) {
			detail = simpleDao.getEntity(VenueSalesDetail.class, detailId);
			if (detail == null) {
				throw new BusinessException("找不到ID为 " + detailId + " 的消费明细");
			}
		}
		detail.setSalesId(salesId);
		detail.setGoodsId(goodsId);;
		detail.setGoodsCount(count);
		detail.setPrice(price);
		detail.setTotal(total);
		detail.setRemark(remark);
		detail.setUpdateTime(now);
		detail.setUpdateUserId(user.getId());
		if (detailId == null) {
			detail.setCreateTime(now);
			detail.setCreateUserId(user.getId());
			simpleDao.persist(detail);
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"),
					"新增ID为 " + detail.getId() + " 的场内消费明细");
		} else {
			VenueSales sales = simpleDao.getEntity(VenueSales.class, salesId);
			if (sales == null) {
				throw new BusinessException("找不到ID为 " + salesId + " 的场内消费单");
			}
			if ("0227003".equals(sales.getStatus()) || "0227004".equals(sales.getStatus())
					|| "0227005".equals(sales.getStatus())) {
				throw new BusinessException(
						"该场内消费单的状态为 " + simpleDao.getEntity(Dic.class, sales.getStatus()).getText() + " ，不能更改消费明细");
			}
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"),
					"修改ID为 " + detail.getId() + " 的场内消费明细");
		}
	}

	/**
	 * 保存场内消费套餐
	 * 
	 * @param salesId
	 *            场内消费单ID
	 * @param packageId
	 *            套餐ID
	 * @throws BusinessException
	 */
	public void saveVenueSalesPackage(Integer salesId, Integer packageId) throws BusinessException {
		simpleDao.deleteByHql("delete from VenueSalesDetail where salesId in (:ids) ", new Integer[]{salesId});
		List<PackageGoods> list = simpleDao
				.getListByHqlWithStrParams("from PackageGoods where packageId = " + packageId, new HashMap<String, Object>());
		for (PackageGoods pg : list) {
			Goods goods = simpleDao.getEntity(Goods.class, pg.getGoodsId());
			if (goods == null) {
				throw new BusinessException("找不到ID为 " + pg.getGoodsId() + " 的商品");
			}
			saveVenueSalesDetail(salesId, pg.getGoodsId(), pg.getGoodsCount(), goods.getPrice(),
					goods.getPrice() * pg.getGoodsCount(), pg.getRemark(), null);
		}
	}

	/**
	 * 获取场内消费明细
	 * 
	 * @param detailId
	 *            消费明细ID
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getVenueSalesDetail(Integer detailId) throws BusinessException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (detailId == null) {
			throw new BusinessException("请提供场内消费明细ID");
		}
		VenueSalesDetail detail = simpleDao.getEntity(VenueSalesDetail.class, detailId);
		if (detail == null) {
			throw new BusinessException("找不到ID为 " + detailId + " 的场内消费明细");
		}
		map.put("salesId", detail.getSalesId());
		map.put("goodsId", detail.getGoodsId());
		map.put("count", detail.getGoodsCount());
		map.put("price", detail.getPrice());
		map.put("total", detail.getTotal());
		map.put("remark", detail.getRemark());
		Goods goods = simpleDao.getEntity(Goods.class, detail.getGoodsId());
		if (goods != null) {
			map.put("smallId", goods.getTypeId());
			GoodsType small = simpleDao.getEntity(GoodsType.class, goods.getTypeId());
			if (small != null) {
				map.put("bigId", small.getParentId());
			}
		}
		return map;
	}

	/**
	 * 删除场内消费明细
	 * 
	 * @param ids
	 *            待删除消费明细ID
	 * @return 成功删除的数量
	 * @throws BusinessException
	 */
	public int deleteVenueSalesDetail(Integer[] ids) throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<VenueSalesDetail> list = simpleDao.getListByHqlWithStrParams("from VenueSalesDetail where id in(:ids)",
				paramMap);
		for (VenueSalesDetail detail : list) {
			VenueSales sales = simpleDao.getEntity(VenueSales.class, detail.getSalesId());
			if (sales == null) {
				throw new BusinessException("找不到ID为 " + detail.getSalesId() + " 的场内消费单");
			}
			if ("0227003".equals(sales.getStatus()) || "0227004".equals(sales.getStatus())
					|| "0227005".equals(sales.getStatus())) {
				throw new BusinessException(
						"该场内消费单的状态为 " + simpleDao.getEntity(Dic.class, sales.getStatus()).getText() + " ，不能删除消费明细");
			}
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueSales"),
					"删除ID为 " + detail.getId() + " 的场内消费明细");
			simpleDao.deleteEntity(detail);
			count++;
		}
		return count;
	}

	/**
	 * 删除场内消费提成设置
	 * 
	 * @param ids
	 *            记录ID
	 * @return 成功删除的数量
	 */
	public int deleteVenueCommision(Integer[] ids) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<VenueCommision> list = simpleDao.getListByHqlWithStrParams("from VenueCommision where id in(:ids)",
				paramMap);
		for (VenueCommision comm : list) {
			// 记录操作日志
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("VenueCommision"), "删除场内消费提成设置记录ID " + comm.getId());

			// 删除场内消费提成设置
			simpleDao.deleteEntity(comm);

			// 成功删除数量+1
			count++;
		}
		return count;
	}
}
