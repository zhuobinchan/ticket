package com.tjing.bussiness.object;

public class PrintInfo {
	private String seatName;
	private String createTime;
	private String strategyName;
	private String price;
	private String number;
	private String showNumber;
	private String playDate;
	private String gift;
	private int userNum;
	private String saleNo;
	private String theatersn;
	public PrintInfo() {
		super();
	}
	
	public PrintInfo(String theatersn,String saleNo,String seatName, String strategyName,
			String price, int userNum,String number ,String playDate,String showNumber,String createTime,String gift) {
		super();
		this.theatersn = theatersn;
		this.saleNo = saleNo;
		this.seatName = seatName;
		this.createTime = createTime;
		this.strategyName = strategyName;
		this.price = price;
		this.number = number;
		this.playDate = playDate;
		this.showNumber = showNumber;
		this.gift = gift;
		this.userNum = userNum;
	}

	public String getSeatName() {
		return seatName;
	}
	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public String getShowNumber() {
		return showNumber;
	}

	public void setShowNumber(String showNumber) {
		this.showNumber = showNumber;
	}

	public String getPlayDate() {
		return playDate;
	}

	public void setPlayDate(String playDate) {
		this.playDate = playDate;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	public String getSaleNo() {
		return saleNo;
	}

	public void setSaleNo(String saleNo) {
		this.saleNo = saleNo;
	}

	public String getTheatersn() {
		return theatersn;
	}

	public void setTheatersn(String theatersn) {
		this.theatersn = theatersn;
	}

	
	
}
