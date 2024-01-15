package com.example.unit.beverage;

import com.example.unit.beverage.Beverage;

public class Latte implements Beverage {
	@Override
	public String getName() {
		return "라떼";
	}

	@Override
	public int getPrice() {
		return 4500;
	}
}
