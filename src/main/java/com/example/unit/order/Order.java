package com.example.unit.order;

import java.time.LocalDateTime;
import java.util.List;

import com.example.unit.beverage.Beverage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Order {
	private final LocalDateTime orderDateTime;
	private final List<Beverage> beverages;
}
