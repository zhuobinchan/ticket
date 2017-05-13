package com.tjing.bussiness.object;

import java.util.List;
import com.tjing.bussiness.model.Area;
import com.tjing.bussiness.model.Seat;

public class AreaSeat {
	private Area area;
	private List<Seat> seats;
	public AreaSeat(Area area, List<Seat> seats) {
		super();
		this.area = area;
		this.seats = seats;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public List<Seat> getSeats() {
		return seats;
	}
	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
	
}
