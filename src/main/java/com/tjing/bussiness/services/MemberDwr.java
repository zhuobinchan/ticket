package com.tjing.bussiness.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjing.bussiness.model.Member;
import com.tjing.bussiness.model.MemberCard;
import com.tjing.bussiness.model.MemberCardConsumer;
import com.tjing.bussiness.model.MemberCardCredit;
import com.tjing.bussiness.model.MemberCardHistory;
import com.tjing.bussiness.model.MemberCardPoint;
import com.tjing.bussiness.model.MemberCardPointExchange;
import com.tjing.bussiness.model.MemberCardRecharge;
import com.tjing.bussiness.model.MemberCardType;
import com.tjing.bussiness.support.BusinessException;
import com.tjing.frame.model.User;
import com.tjing.frame.services.DbServices;
import com.tjing.frame.util.CodeHelper;
import com.tjing.frame.util.SimpleDao;

@Service
public class MemberDwr {
	@Autowired
	private SimpleDao simpleDao;
	@Autowired
	private DbServices dbServices;

	/**
	 * 会员卡发卡
	 * 
	 * @param code
	 *            会员卡号
	 * @param count
	 *            发卡数量
	 * @param salesmanId
	 *            营销员ID
	 * @param name
	 *            会员卡名称
	 * @param type
	 *            会员卡类型
	 * @param lastRechargeWay
	 *            最后充值类型
	 * @param password
	 *            密码
	 * @param remark
	 *            备注
	 * @param memberId
	 *            会员ID
	 * @param money
	 *            充值金额
	 * @param payType
	 *            充值方式
	 * @return 成功发卡的数量
	 */
	public int newMemberCard(String code, Integer count, Integer salesmanId, String name, String type,
			String lastRechargeWay, String password, String remark, Integer memberId, Float money, String payType) {
		User user = CodeHelper.getCurrentUser();
		int sum = 0;

		// 会员卡号数字化
		String prefix = "";
		String num = "";
		int len = 0;
		String zero = "";
		for (int i = code.length() - 1; i >= 0; i--) {
			char c = code.charAt(i);
			if (c >= '0' && c <= '9') {
				num = c + num;
				len++;
				zero += "0";
			} else {
				prefix = code.substring(0, i + 1);
				break;
			}
		}
		long number = Long.parseLong(num);
		for (int i = 0; i < count; i++) {
			// 添加会员卡
			String newCardId = (zero + (number + i));
			newCardId = prefix + newCardId.substring(newCardId.length() - len);
			int cc = simpleDao.getIntNumByHql(
					"from MemberCard where status in ('0218001', '0218003') and code = '" + newCardId + "'");
			if (cc <= 0) {
				MemberCard card = new MemberCard();
				Date now = new Date();
				card.setCode(newCardId);
				card.setCreateUserId(user.getId());
				card.setUpdateUserId(user.getId());
				card.setCreateTime(now);
				card.setUpdateTime(now);
				card.setSalesmanId(salesmanId);
				card.setName(name);
				card.setType(type);
				card.setBalance(0.0f);
				card.setPoint(0);
				card.setLastRechargeWay(lastRechargeWay);
				card.setPassword(password);
				card.setRemark(remark);
				card.setStatus("0218001");
				simpleDao.persist(card);

				// 记录操作日志
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"), "会员卡ID " + card.getId() + " 发卡");

				// 成功发卡数量+1
				sum++;

				// 绑定会员
				if (memberId != null) {
					Member member = simpleDao.getEntity(Member.class, memberId);
					if (member != null) {
						card.setMemberId(memberId);
						dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
								"会员卡号 " + card.getId() + " 绑定会员ID " + memberId);

						// 消费卡充值
						if (money != null && money > 0) {
							MemberCardRecharge recharge = new MemberCardRecharge();
							recharge.setCardId(card.getId());
							recharge.setCreateTime(now);
							recharge.setRealAmount(money);
							recharge.setRechargeAmount(money);
							recharge.setStatus("0202001");
							recharge.setPayType(payType);
							recharge.setBranch("0220001");
							recharge.setUserId(user.getId());
							simpleDao.persist(recharge);

							card.setBalance(card.getBalance() + money);

							dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
									"消费卡号 " + card.getId() + " 充值 " + money + " 元");
						}
					}
				}
			}
		}
		return sum;
	}

	/**
	 * 会员卡更换营销员
	 * 
	 * @param ids
	 *            会员卡号（可多个）
	 * @param salesmanId
	 *            营销员ID
	 * @return 成功修改的数量
	 */
	public int changeSalesman(Integer[] ids, Integer salesmanId) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<MemberCard> list = simpleDao.getListByHqlWithStrParams("from MemberCard where id in(:ids)", paramMap);
		for (MemberCard card : list) {
			if ("0218001".equals(card.getStatus())) {
				card.setSalesmanId(salesmanId);
				card.setUpdateUserId(user.getId());
				card.setUpdateTime(new Date());

				// 记录操作日志
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
						"会员卡号 " + card.getId() + " 更换营销员ID为 " + salesmanId);

				// 成功修改数量+1
				count++;
			}
		}
		return count;
	}

	/**
	 * 修改卡状态
	 * 
	 * @param ids
	 *            待修改状态的卡
	 * @param newStatus
	 *            新状态
	 * @param oldStatus
	 *            旧状态
	 * @return 成功修改的数量
	 */
	public int changeCardStatus(Integer[] ids, String newStatus, String oldStatus) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<MemberCard> list = simpleDao.getListByHqlWithStrParams("from MemberCard where id in(:ids)", paramMap);
		for (MemberCard card : list) {
			if (oldStatus != null && oldStatus.equals(card.getStatus())) {
				card.setStatus(newStatus);
				card.setUpdateUserId(user.getId());
				card.setUpdateTime(new Date());

				// 记录操作日志
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
						"会员卡号 " + card.getId() + " 由状态 " + oldStatus + " 更改状态为 " + newStatus);

				// 成功修改数量+1
				count++;
			}
		}
		return count;
	}

	/**
	 * 恢复初始化卡密码
	 * 
	 * @param ids
	 *            待修改密码的卡号
	 * @param newPwd
	 *            新密码
	 * @return 成功修改的数量
	 */
	public int restoreCardPassword(Integer[] ids, String newPwd) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<MemberCard> list = simpleDao.getListByHqlWithStrParams("from MemberCard where id in(:ids)", paramMap);
		for (MemberCard card : list) {
			if ("0218001".equals(card.getStatus())) {
				card.setPassword(newPwd);
				card.setUpdateUserId(user.getId());
				card.setUpdateTime(new Date());

				// 记录操作日志
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
						"会员卡号 " + card.getId() + " 恢复初始化密码");

				// 成功修改数量+1
				count++;
			}
		}
		return count;
	}

	/**
	 * 修改卡密码
	 * 
	 * @param id
	 *            待修改密码的卡号
	 * @param newPwd
	 *            新密码
	 * @param oldPwd
	 *            旧密码
	 * @return
	 */
	public String changeCardPassword(Integer id, String newPwd, String oldPwd) {
		User user = CodeHelper.getCurrentUser();
		if (id == null) {
			return "请提供卡号";
		}
		MemberCard card = simpleDao.getEntity(MemberCard.class, id);
		if (card == null) {
			return "找不到卡号";
		}
		if (!"0218001".equals(card.getStatus())) {
			return "无效或挂失的卡不能修改密码";
		}
		if (oldPwd == null || !oldPwd.equals(card.getPassword())) {
			return "原密码错误";
		} else {
			card.setPassword(newPwd);
			card.setUpdateUserId(user.getId());
			card.setUpdateTime(new Date());

			// 记录操作日志
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"), "会员卡号 " + card.getId() + " 修改密码成功");
		}
		return "成功修改密码";
	}

	/**
	 * 消费卡余额合并
	 * 
	 * @param receiveCode
	 *            主卡号
	 * @param sendCode
	 *            次卡号
	 * @param money
	 *            从次卡转到主卡的金额
	 * @return 返回1表示成功
	 */
	public String mergeConsumerCard(String receiveCode, String sendCode, Float money) {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		if (receiveCode == null || sendCode == null) {
			return "请提供主卡号和次卡号";
		}
		MemberCard receiver = simpleDao.getEntityByHql(MemberCard.class,
				"from MemberCard where status in ('0218001', '0218003') and code = '" + receiveCode + "'");
		MemberCard sender = simpleDao.getEntityByHql(MemberCard.class,
				"from MemberCard where status in ('0218001', '0218003') and code = '" + sendCode + "'");
		if (receiver != null && sender != null) {
			if (!"0218001".equals(receiver.getStatus()) || !"0218001".equals(sender.getStatus())) {
				return "无效或挂失卡不能合并余额";
			}
			if (receiver.getMemberId() == null || sender.getMemberId() == null
					|| !receiver.getMemberId().equals(sender.getMemberId())) {
				return "主卡和次卡不属于同一个会员";
			}
			if (sender.getBalance() < money) {
				return "次卡余额少于 " + money + " 元，无法合并";
			} else {
				receiver.setBalance(receiver.getBalance() + money);
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
						"次卡 " + sendCode + " 的余额 " + sender.getBalance() + " 元合并到主卡 " + receiveCode + " 上");
				sender.setBalance(sender.getBalance() - money);
				receiver.setUpdateTime(now);
				receiver.setUpdateUserId(user.getId());
				sender.setUpdateTime(now);
				sender.setUpdateUserId(user.getId());

				// 记录转出
				MemberCardRecharge out = new MemberCardRecharge();
				out.setCardId(sender.getId());
				out.setCreateTime(now);
				out.setRealAmount(0 - money);
				out.setRechargeAmount(0 - money);
				out.setStatus("0202001");
				out.setPayType("0207009"); // 转出
				out.setBranch("0220001");
				out.setUserId(user.getId());
				simpleDao.persist(out);

				// 记录转入
				MemberCardRecharge in = new MemberCardRecharge();
				in.setCardId(receiver.getId());
				in.setCreateTime(now);
				in.setRealAmount(money);
				in.setRechargeAmount(money);
				in.setStatus("0202001");
				in.setPayType("0207008"); // 转入
				in.setBranch("0220001");
				in.setUserId(user.getId());
				simpleDao.persist(in);
			}
		}
		return "1";
	}

	/**
	 * 客户换卡
	 * 
	 * @param oldCode
	 *            旧卡号
	 * @param newCode
	 *            新卡号
	 * @param reason
	 *            换卡原因
	 * @return
	 */
	public String changeCard(String oldCode, String newCode, String reason) {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		if (oldCode == null || newCode == null) {
			return "请提供新旧卡号";
		}
		MemberCard oldCard = simpleDao.getEntityByHql(MemberCard.class,
				"from MemberCard where status in ('0218001', '0218003') and code = '" + oldCode + "'");
		MemberCard newCard = simpleDao.getEntityByHql(MemberCard.class,
				"from MemberCard where status in ('0218001', '0218003') and code = '" + newCode + "'");
		if (oldCard == null) {
			return "找不到有效的旧卡号";
		}
		if (newCard == null) {
			return "找不到有效的新卡号";
		}
		if (oldCard.getMemberId() == null) {
			return "旧卡没有关联客户，无须换卡，直接删除即可";
		}
		if (!"0218001".equals(newCard.getStatus())) {
			return "新卡状态必须为有效";
		}
		if (newCard.getMemberId() != null) {
			return "新卡号已经关联客户，不能更换到此卡号";
		}
		if (!newCard.getType().equals(oldCard.getType())) {
			return "新旧两张卡类型不一致";
		}
		newCard.setStatus("0218002");
		newCard.setCode(oldCode);
		newCard.setRemark("此卡原卡号为 " + newCode + " ，由于 " + reason + " 的原因，经过会员 " + oldCard.getMemberId() + " 的同意，与 "
				+ oldCode + " 置换，此卡现已失效，仅作记录使用");
		newCard.setUpdateTime(now);
		newCard.setUpdateUserId(user.getId());

		oldCard.setCode(newCode);
		oldCard.setUpdateTime(now);
		oldCard.setUpdateUserId(user.getId());

		dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
				"旧卡 " + oldCode + " 由于 " + reason + " 的原因更换到新卡 " + newCode);

		return "换卡成功";
	}

	/**
	 * 收回挂账
	 * 
	 * @param id
	 *            挂账记录ID
	 * @param money
	 *            收回金额
	 * @param payType
	 *            还款方式
	 * @param branch
	 *            还款网点
	 * @param salesmanId
	 *            还款营销员
	 * @return
	 */
	public String recoverCredit(Integer id, Float money, String payType, String branch, Integer salesmanId) {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		if (id == null) {
			return "请提供挂账记录";
		}
		MemberCardRecharge recharge = simpleDao.getEntity(MemberCardRecharge.class, id);
		MemberCardCredit credit = simpleDao.getEntity(MemberCardCredit.class, id);
		if (recharge == null || credit == null) {
			return "找不到挂账记录";
		}
		if (!"0221001".equals(credit.getStatus())) {
			return "这笔挂账不需要处理";
		}
		if (credit.getRechargeAmount() > money) {
			return "收回债款少于充值实收金额";
		}
		recharge.setPayType(payType);
		recharge.setCreateTime(now);

		credit.setRepayAmount(money);
		credit.setRepayType(payType);
		credit.setRepayBranch(branch);
		credit.setRepaySalesmanId(salesmanId);
		credit.setUserId(user.getId());
		credit.setRepayTime(now);
		credit.setStatus("0221002");

		if (recharge.getCardId() != null) {
			MemberCard card = simpleDao.getEntity(MemberCard.class, recharge.getCardId());
			if (card != null) {
				card.setLastRechargeWay(payType);
			}
		}

		dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"), "挂账ID " + id + " 已经收回金额 " + money + " 元");
		return "成功收回赊账";
	}

	/**
	 * 新增会员，如果有卡号的话，就关联该卡号
	 * 
	 * @param cardId
	 *            待关联卡号
	 * @param name
	 *            会员姓名
	 * @param gender
	 *            会员性别
	 * @param mobile
	 *            会员手机
	 * @param telephone
	 *            会员固话
	 * @param email
	 *            会员邮箱
	 * @param address
	 *            会员地址
	 * @param birthday
	 *            会员生日
	 * @param company
	 *            会员工作单位
	 * @param idCardType
	 *            会员证件类型
	 * @param idCardNo
	 *            会员证件号码
	 * @param hobby
	 *            会员爱好
	 * @param remark
	 *            会员备注
	 * @param memberId
	 *            会员ID
	 * @return
	 */
	public String createMember(Integer cardId, String name, String gender, String mobile, String telephone,
			String email, String address, String birthday, String company, String idCardType, String idCardNo,
			String hobby, String remark, Integer memberId) {
		try {
			saveMember(cardId, name, gender, mobile, telephone, email, address, birthday, company, idCardType, idCardNo,
					hobby, remark, memberId);
			return "会员资料录入成功";
		} catch (BusinessException e) {
			return e.getMessage();
		}
	}

	/**
	 * 新增会员，如果有卡号的话，就关联该卡号
	 * 
	 * @param cardId
	 *            待关联卡号
	 * @param name
	 *            会员姓名
	 * @param gender
	 *            会员性别
	 * @param mobile
	 *            会员手机
	 * @param telephone
	 *            会员固话
	 * @param email
	 *            会员邮箱
	 * @param address
	 *            会员地址
	 * @param birthday
	 *            会员生日
	 * @param company
	 *            会员工作单位
	 * @param idCardType
	 *            会员证件类型
	 * @param idCardNo
	 *            会员证件号码
	 * @param hobby
	 *            会员爱好
	 * @param remark
	 *            会员备注
	 * @param memberId
	 *            会员ID
	 * @return 会员ID
	 * @throws BusinessException
	 */
	public int saveMember(Integer cardId, String name, String gender, String mobile, String telephone, String email,
			String address, String birthday, String company, String idCardType, String idCardNo, String hobby,
			String remark, Integer memberId) throws BusinessException {
		try {
			User user = CodeHelper.getCurrentUser();
			Date now = new Date();
			Date bd = null;
			if (birthday != null && !"".equals(birthday)) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				bd = df.parse(birthday);
			}
			Member member = new Member();
			if (memberId != null) {
				member = simpleDao.getEntity(Member.class, memberId);
				if (member == null) {
					throw new BusinessException("找不到ID为 " + memberId + " 的会员信息");
				}
			}
			member.setName(name);
			member.setGender(gender);
			member.setMobileno(mobile);
			member.setTelephone(telephone);
			member.setEmail(email);
			member.setAddress(address);
			member.setBirthday(bd);
			member.setCompany(company);
			member.setIdcardType(idCardType);
			member.setIdcardNo(idCardNo);
			member.setHobby(hobby);
			member.setRemark(remark);
			member.setStatus("0202001");
			member.setCreateTime(now);
			member.setCreateUserId(user.getId());
			member.setUpdateTime(now);
			member.setUpdateUserId(user.getId());
			simpleDao.persist(member);

			if (memberId == null) {
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("Member"), "新增会员ID为 " + member.getId());
				if (cardId != null) {
					MemberCard card = simpleDao.getEntity(MemberCard.class, cardId);
					if (card != null) {
						card.setMemberId(member.getId());
						card.setUpdateTime(now);
						card.setUpdateUserId(user.getId());

						dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
								"卡ID " + cardId + " 关联会员ID " + member.getId());
					}
				}
			} else {
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("Member"), "会员ID " + memberId + " 修改资料");
			}
			return member.getId();
		} catch (ParseException e) {
			throw new BusinessException("生日 " + birthday + "日期格式错误");
		}
	}

	/**
	 * 获取会员资料
	 * 
	 * @param memberId
	 *            会员ID
	 * @return 会员资料
	 */
	public Map<String, Object> getMemberInfo(Integer memberId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (memberId != null) {
			Member member = simpleDao.getEntity(Member.class, memberId);
			if (member != null) {
				map.put("name", member.getName());
				map.put("gender", member.getGender());
				map.put("mobile", member.getMobileno());
				map.put("telephone", member.getTelephone());
				map.put("email", member.getEmail());
				map.put("address", member.getAddress());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if (member.getBirthday() != null) {
					map.put("birthday", df.format(member.getBirthday()));
				}
				map.put("company", member.getCompany());
				map.put("idType", member.getIdcardType());
				map.put("idCard", member.getIdcardNo());
				map.put("hobby", member.getHobby());
				map.put("remark", member.getRemark());
				map.put("id", memberId);
			}
		}
		return map;
	}

	/**
	 * 根据手机号码获取会员信息
	 * 
	 * @param mobile
	 *            手机号码
	 * @return 会员信息
	 * @throws BusinessException
	 */
	public Map<String, Object> getMemberInfoByMobile(String mobile) throws BusinessException {
		List<Member> list = simpleDao.getListByHql(
				"from Member where status = '0202001' and mobileno = '" + mobile + "'", new HashMap<Integer, Object>());
		if (list.size() == 0) {
			throw new BusinessException("找不到手机号码为 " + mobile + " 的会员信息");
		}
		return getMemberInfo(list.get(0).getId());
	}

	/**
	 * 充值消费卡或充值卡
	 * 
	 * @param ids
	 *            待充值卡号
	 * @param money
	 *            充值金额
	 * @param payType
	 *            充值方式
	 * @param salesmanId
	 *            营销员ID
	 * @return 返回成功充值的数量
	 */
	public int rechargeMemberCard(Integer[] ids, Float money, String payType, Integer salesmanId) {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<MemberCard> list = simpleDao.getListByHqlWithStrParams("from MemberCard where id in(:ids)", paramMap);
		for (MemberCard card : list) {
			if (card.getMemberId() != null && "0218001".equals(card.getStatus())) {
				// 消费卡充值
				if (money != null && money > 0) {
					MemberCardRecharge recharge = new MemberCardRecharge();
					recharge.setCardId(card.getId());
					recharge.setCreateTime(now);
					recharge.setRealAmount(money);
					recharge.setRechargeAmount(money);
					recharge.setStatus("0202001");
					recharge.setPayType(payType);
					recharge.setBranch("0220001");
					recharge.setUserId(user.getId());
					recharge.setSalesmanId(salesmanId);
					simpleDao.persist(recharge);

					card.setBalance(card.getBalance() + money);
					card.setLastRechargeWay(recharge.getPayType());
					card.setUpdateUserId(user.getId());
					card.setUpdateTime(now);

					dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
							"卡ID " + card.getId() + " 充值 " + money + " 元");

					// 充值卡充值总额达到一定金额，自动升级
					int sum = simpleDao
							.getIntNumByHql("select floor(sum(realAmount)) from MemberCardRecharge where cardId = "
									+ card.getId() + " and status = '0202001'");
					MemberCardType cardType = simpleDao.getEntityByHql(MemberCardType.class,
							"from MemberCardType where cumulation = (select max(cumulation) from MemberCardType where cumulation <= "
									+ sum + " and type = '0216003')");
					if (cardType != null && !cardType.getName().equals(card.getName())) {
						card.setName(cardType.getName());
						dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
								"卡ID " + card.getId() + " 升级为 " + cardType.getName());
					}

					// 挂账处理
					if ("0207004".equals(payType)) {
						MemberCardCredit credit = new MemberCardCredit();
						credit.setId(recharge.getId());
						credit.setCardId(card.getId());
						credit.setCreateTime(now);
						credit.setRechargeAmount(recharge.getRechargeAmount());
						credit.setCreditAmount(recharge.getRealAmount());
						credit.setRepayAmount(0f);
						credit.setSalesmanId(salesmanId);
						credit.setStatus("0221001");
						credit.setUserId(user.getId());
						simpleDao.persist(credit);

						dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"),
								"卡ID " + card.getId() + " 挂账 " + money + " 元");
					}

					// 成功充值数量+1
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 删除会员卡，仅限未关联会员资料的卡可以删除
	 * 
	 * @param ids
	 *            待删除的会员卡号
	 * @return 成功删除会员卡的数量
	 */
	public int deleteMemberCard(Integer[] ids) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<MemberCard> list = simpleDao.getListByHqlWithStrParams("from MemberCard where id in(:ids)", paramMap);
		for (MemberCard card : list) {
			if (card.getMemberId() == null) {
				// 添加会员卡历史
				MemberCardHistory history = new MemberCardHistory();
				history.setCardId(card.getId());
				history.setCode(card.getCode());
				history.setPassword(card.getPassword());
				history.setName(card.getName());
				history.setType(card.getType());
				history.setStatus(card.getStatus());
				history.setEffectiveTime(card.getEffectiveTime());
				history.setExpiryTime(card.getExpiryTime());
				history.setSalesmanId(card.getSalesmanId());
				history.setBalance(card.getBalance());
				history.setPoint(card.getPoint());
				history.setLastRechargeWay(card.getLastRechargeWay());
				history.setCreateTime(card.getCreateTime());
				history.setUpdateTime(card.getUpdateTime());
				history.setCreateUserId(card.getCreateUserId());
				history.setUpdateUserId(card.getUpdateUserId());
				history.setRemark(card.getRemark());
				history.setDeleteUserId(user.getId());
				history.setDeleteTime(new Date());
				simpleDao.persist(history);

				// 记录操作日志
				dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCard"), "删除会员卡ID " + card.getId());

				// 删除会员卡
				simpleDao.deleteEntity(card);

				// 成功删除数量+1
				count++;
			}
		}
		return count;
	}

	/**
	 * 删除会员卡类别设置
	 * 
	 * @param ids
	 *            记录ID
	 * @return 成功删除的数量
	 */
	public int deleteMemberCardType(Integer[] ids) {
		User user = CodeHelper.getCurrentUser();
		int count = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ids", ids);
		List<MemberCardType> list = simpleDao.getListByHqlWithStrParams("from MemberCardType where id in(:ids)",
				paramMap);
		for (MemberCardType type : list) {
			// 记录操作日志
			dbServices.addOperateLog(user, CodeHelper.getClassInfo("MemberCardType"), "删除会员卡类别记录ID " + type.getId());

			// 删除会员卡类别
			simpleDao.deleteEntity(type);

			// 成功删除数量+1
			count++;
		}
		return count;
	}

	/**
	 * 检查卡号和密码是否正确
	 * 
	 * @param code
	 *            卡号
	 * @param password
	 *            密码
	 * @throws BusinessException
	 */
	public void checkPassword(String code, String password) throws BusinessException {
		MemberCard card = getCardByCode(code);
		if (password == null || !password.equals(card.getPassword())) {
			throw new BusinessException("卡号或密码错误");
		}
	}

	/**
	 * 根据卡号获取MemberCard类
	 * 
	 * @param code
	 *            卡号
	 * @return
	 * @throws BusinessException
	 */
	public MemberCard getCardByCode(String code) throws BusinessException {
		if (code == null) {
			throw new BusinessException("请提供卡号");
		}
		MemberCard card = simpleDao.getEntityByHql(MemberCard.class,
				"from MemberCard where status in ('0218001', '0218003') and code = '" + code + "'");
		if (card == null) {
			throw new BusinessException("找不到卡号 " + code);
		}
		return card;
	}

	/**
	 * 根据卡ID获取MemberCard对象
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public MemberCard getCardById(Integer id) throws BusinessException {
		if (id == null) {
			throw new BusinessException("请提供卡ID");
		}
		MemberCard card = simpleDao.getEntity(MemberCard.class, id);
		if (card == null) {
			throw new BusinessException("找不到卡ID " + id);
		}
		return card;
	}

	/**
	 * 卡消费，扣除余额并积分
	 * 
	 * @param cardId
	 *            卡ID
	 * @param money
	 *            消费金额
	 * @param serial
	 *            销售编号
	 * @param branch
	 *            消费场所
	 * @param remark
	 *            备注
	 * @throws BusinessException
	 */
	public void cardConsume(Integer cardId, Float money, String serial, String branch, String remark)
			throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		MemberCard card = getCardById(cardId);
		if (money == null) {
			throw new BusinessException("请提供消费金额");
		}
		if (!"0216001".equals(card.getType()) && money > card.getBalance()) {
			throw new BusinessException("消费金额少于卡余额");
		}
		// 消费记录
		MemberCardConsumer con = new MemberCardConsumer();
		con.setSaleNo(serial);
		con.setCardId(cardId);
		con.setMoney(money);
		con.setBalance("0216001".equals(card.getType()) ? 0 : card.getBalance() - money);
		con.setBranch(branch);
		con.setStatus("0202001");
		con.setRemark(remark);
		con.setCreateTime(now);
		con.setCreateUserId(user.getId());
		con.setUpdateTime(now);
		con.setUpdateUserId(user.getId());
		simpleDao.persist(con);

		// 积分记录
		MemberCardPoint point = new MemberCardPoint();
		int pointValue = getPointByCardConsume(money, serial, branch, card.getName());
		point.setPoint(pointValue);
		point.setBalance(card.getPoint() + pointValue);
		point.setBranch(branch);
		point.setCardId(cardId);
		point.setCreateTime(now);
		point.setCreateUserId(user.getId());
		point.setRemark(remark);
		point.setSaleNo(serial);
		point.setStatus("0202001");
		point.setUpdateTime(now);
		point.setUpdateUserId(user.getId());
		simpleDao.persist(point);

		card.setBalance(con.getBalance());
		card.setPoint(point.getBalance());
		card.setUpdateTime(now);
		card.setUpdateUserId(user.getId());
	}

	/**
	 * 根据消费情况计算积分
	 * 
	 * @param money
	 *            消费金额
	 * @param serial
	 *            销售编号
	 * @param branch
	 *            消费场所
	 * @param cardName
	 *            卡名称
	 * @return 积分值
	 * @throws BusinessException
	 */
	public int getPointByCardConsume(Float money, String serial, String branch, String cardName)
			throws BusinessException {
		int point = 0;
		float ratio = 1;
		MemberCardType type = simpleDao.getEntityByHql(MemberCardType.class,
				"from MemberCardType where name = '" + cardName + "'");
		if ("0226001".equals(branch)) {
			ratio = type.getTicketPointRatio();
			if ("0233001".equals(type.getTicketPointMethod())) {
				point = Math.round(money * ratio);
			} else if ("0233002".equals(type.getTicketPointMethod())) {
				int count = simpleDao
						.getIntNumByHql("select ticketNum from TicketSale where saleNo = '" + serial + "'");
				point = Math.round(count * ratio);
			}
		} else if ("0226002".equals(branch)) {
			ratio = type.getVenuePointRatio();
			if ("0233001".equals(type.getVenuePointMethod())) {
				point = Math.round(money * ratio);
			}
		}
		return point;
	}

	/**
	 * 取消卡消费
	 * 
	 * @param serial
	 * @throws BusinessException
	 */
	public void cancelCardConsume(String serial, Float money) throws BusinessException {
		User user = CodeHelper.getCurrentUser();
		Date now = new Date();
		// 取消消费记录
		MemberCardConsumer con = simpleDao.getEntityByHql(MemberCardConsumer.class,
				"from MemberCardConsumer where status = '0202001' and saleNo = '" + serial + "'");
		if (con == null) {
			throw new BusinessException("找不到销售编号为 " + serial + " 的有效卡消费记录");
		}
		con.setStatus("0202002");
		con.setUpdateTime(now);
		con.setUpdateUserId(user.getId());
		// 取消积分记录
		MemberCardPoint point = simpleDao.getEntityByHql(MemberCardPoint.class,
				"from MemberCardPoint where status = '0202001' and saleNo = '" + serial + "'");
		if (point == null) {
			throw new BusinessException("找不到销售编号为 " + serial + " 的有效积分记录");
		}
		point.setStatus("0202002");
		point.setUpdateTime(now);
		point.setUpdateUserId(user.getId());

		MemberCard card = getCardById(con.getCardId());
		card.setBalance(card.getBalance() + con.getMoney());
		card.setPoint(card.getPoint() - point.getPoint());
		card.setUpdateTime(now);
		card.setUpdateUserId(user.getId());

		// 剩余消费金额重新记录
		if (con.getMoney() > money) {
			cardConsume(con.getCardId(), con.getMoney() - money, serial, con.getBranch(), "原消费记录产生部分退款，此记录为剩余的消费金额");
		}
	}

	/**
	 * 获取卡级别相关信息，包括升级累积金额，优惠打折等
	 * 
	 * @param code
	 *            卡号
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getCardLevel(String code) throws BusinessException {
		Map<String, Object> map = new HashMap<String, Object>();
		MemberCard card = getCardByCode(code);
		MemberCardType type = simpleDao.getEntityByHql(MemberCardType.class,
				"from MemberCardType where name = '" + card.getName() + "'");
		map.put("cumulation", type.getCumulation());
		map.put("ticketDiscount", type.getTicketDiscount());
		map.put("ticketReduce", type.getTicketReduce());
		map.put("ticketAlgorithm", type.getTicketAlgorithm());
		map.put("venueDiscount", type.getVenueDiscount());
		map.put("venueReduce", type.getVenueReduce());
		map.put("venueAlgorithm", type.getVenueAlgorithm());
		map.put("balance", card.getBalance());
		map.put("point", card.getPoint());
		return map;
	}

	/**
	 * 获取该卡号可以兑换的积分值
	 * 
	 * @param code
	 *            卡号
	 * @return 可兑换的积分值
	 * @throws BusinessException
	 */
	public int getExchangeablePoint(String code) throws BusinessException {
		int point = 0;
		MemberCard card = getCardByCode(code);
		point = card.getPoint();

		// 计算未到期兑换积分
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (month % 2 == 1) {
			cal.add(Calendar.MONTH, -1);
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		Map<Integer, Object> para = new HashMap<Integer, Object>();
		List<Object> list = simpleDao.getListBySql("select ifnull(sum(point), 0) from bi_member_card_point "
				+ " where date_format(create_time, '%Y-%m-%d') >= '" + date
				+ "' and status = '0202001' and member_card_id = " + card.getId(), para);
		int invalidPoint = Integer.parseInt(list.get(0).toString());
		point = point - invalidPoint;
		return point;
	}

	/**
	 * 兑换积分
	 * 
	 * @param code
	 *            卡号
	 * @param point
	 *            待兑换的积分值
	 * @param content
	 *            兑换的内容
	 * @param branch
	 *            兑换场所
	 * @param remark
	 *            备注
	 * @throws BusinessException
	 */
	public void exchangePoint(String code, Integer point, String content, String branch, String remark)
			throws BusinessException {
		Date now = new Date();
		User user = CodeHelper.getCurrentUser();
		int exchangeable = getExchangeablePoint(code);
		if (point > exchangeable) {
			throw new BusinessException(
					"卡号为 " + code + " 的卡号的可兑换积分值为 " + exchangeable + " ，少于待兑换积分值 " + point + " ，不能兑换");
		}
		MemberCard card = getCardByCode(code);
		// 兑换记录
		MemberCardPointExchange exchange = new MemberCardPointExchange();
		exchange.setCardId(card.getId());
		exchange.setPoint(point);
		exchange.setBalance(card.getPoint() - point);
		exchange.setContent(content);
		exchange.setBranch(branch);
		exchange.setStatus("0202001");
		exchange.setRemark(remark);
		exchange.setCreateTime(now);
		exchange.setCreateUserId(user.getId());
		exchange.setUpdateTime(now);
		exchange.setUpdateUserId(user.getId());
		simpleDao.persist(exchange);

		// 扣减积分
		card.setPoint(exchange.getBalance());
		card.setUpdateTime(now);
		card.setUpdateUserId(user.getId());
	}

	/**
	 * 取消积分兑换
	 * 
	 * @param id
	 *            兑换ID
	 * @throws BusinessException
	 */
	public void cancelExchangePoint(Integer id) throws BusinessException {
		Date now = new Date();
		User user = CodeHelper.getCurrentUser();
		MemberCardPointExchange exchange = simpleDao.getEntity(MemberCardPointExchange.class, id);
		if (exchange == null) {
			throw new BusinessException("找不到ID为 " + id + " 的积分兑换记录");
		}
		if ("0202002".equals(exchange.getStatus())) {
			throw new BusinessException("该积分兑换记录已经失效，不能重复取消");
		}
		// 积分记录失效
		exchange.setStatus("0202002");
		exchange.setUpdateTime(now);
		exchange.setUpdateUserId(user.getId());
		// 积分回退
		MemberCard card = getCardById(exchange.getCardId());
		card.setPoint(card.getPoint() + exchange.getPoint());
		card.setUpdateTime(now);
		card.setUpdateUserId(user.getId());
	}
}
