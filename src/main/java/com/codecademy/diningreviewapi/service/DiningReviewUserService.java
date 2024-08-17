package com.codecademy.diningreviewapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codecademy.diningreviewapi.model.RestaurantUser;
import com.codecademy.diningreviewapi.repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DiningReviewUserService implements UserDetailsService {
	
	@Autowired
	private UserRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<RestaurantUser> user = repo.findByDisplayName(username);
		
		if(user.isPresent())
		{
			var userObj = user.get();
			return User.builder()
						.username(userObj.getDisplayName())
						.password(userObj.getPassword())
						.build();
		}
		else
			throw new UsernameNotFoundException(username + " not found!");
		
	}


}
