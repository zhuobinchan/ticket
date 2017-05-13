package com.tjing.bussiness.object;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaSeatData {
	Map<String,Integer> price_map = new HashMap<String,Integer>();
	Map<String,List<String>> seat_map = new HashMap<String,List<String>>();
	private String theater_sn;
	public Map<String, Integer> getPrice_map() {
		return price_map;
	}
	public void setPrice_map(Map<String, Integer> price_map) {
		this.price_map = price_map;
	}
	public Map<String, List<String>> getSeat_map() {
		return seat_map;
	}
	public void setSeat_map(Map<String, List<String>> seat_map) {
		this.seat_map = seat_map;
	}
	public String getTheater_sn() {
		return theater_sn;
	}
	public void setTheater_sn(String theater_sn) {
		this.theater_sn = theater_sn;
	}
}
