package com.codecademy.diningreviewapi.exceptions;

@SuppressWarnings("serial")
public class RestaurantUserNotFoundException extends RuntimeException {

	public RestaurantUserNotFoundException(Integer id) {
		super("User with the id " + id + "couldn't be found.");
	}
}
