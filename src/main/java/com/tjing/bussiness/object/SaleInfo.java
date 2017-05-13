package com.tjing.bussiness.object;

import java.util.List;

import com.tjing.bussiness.model.Ticket;
import com.tjing.bussiness.model.TicketReserve;
import com.tjing.bussiness.model.TicketSale;

public class SaleInfo {
	private List<Ticket> tickets;
	private List<Ticket> reservedTickets;
	private List<Ticket> saledTickets;
	private List<TicketReserve> reserves;
	private List<TicketSale> sales;
	private List<Integer> lockedSeatIds;
	private List<Integer> pickedSeatIds;//我锁定的座位
	
	public SaleInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SaleInfo(List<Ticket> tickets, List<Ticket> reservedTickets,
			List<Integer> lockedSeatIds,List<Integer> pickedSeatIds, List<Ticket> saledTickets,List<TicketReserve> reserves,List<TicketSale> sales) {
		super();
		this.tickets = tickets;
		this.reservedTickets = reservedTickets;
		this.saledTickets = saledTickets;
		this.lockedSeatIds = lockedSeatIds;
		this.pickedSeatIds = pickedSeatIds;
		this.reserves = reserves;
		this.sales = sales;
	}
	public List<Ticket> getTickets() {
		return tickets;
	}
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	public List<Ticket> getReservedTickets() {
		return reservedTickets;
	}
	public void setReservedTickets(List<Ticket> reservedTickets) {
		this.reservedTickets = reservedTickets;
	}
	public List<Ticket> getSaledTickets() {
		return saledTickets;
	}
	public void setSaledTickets(List<Ticket> saledTickets) {
		this.saledTickets = saledTickets;
	}
	public List<Integer> getLockedSeatIds() {
		return lockedSeatIds;
	}
	public void setLockedSeatIds(List<Integer> lockedSeatIds) {
		this.lockedSeatIds = lockedSeatIds;
	}
	public List<Integer> getPickedSeatIds() {
		return pickedSeatIds;
	}
	public void setPickedSeatIds(List<Integer> pickedSeatIds) {
		this.pickedSeatIds = pickedSeatIds;
	}
	public List<TicketReserve> getReserves() {
		return reserves;
	}
	public void setReserves(List<TicketReserve> reserves) {
		this.reserves = reserves;
	}
	public List<TicketSale> getSales() {
		return sales;
	}
	public void setSales(List<TicketSale> sales) {
		this.sales = sales;
	}
	
}
