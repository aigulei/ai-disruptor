package com.ai.disruptor.height;

import java.util.concurrent.atomic.AtomicInteger;

public class Trade {
	private String id;
	private String name;
	private double price;
	private AtomicInteger countAtomicInteger = new AtomicInteger(0);
	
	public Trade() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public AtomicInteger getCountAtomicInteger() {
		return countAtomicInteger;
	}
	public void setCountAtomicInteger(AtomicInteger countAtomicInteger) {
		this.countAtomicInteger = countAtomicInteger;
	}
	
	
}
