package com.codecademy.diningreviewapi.enums;

import com.codecademy.diningreviewapi.serializer.ReviewDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ReviewDeserializer.class)
public enum ReviewStatus {
	
	PENDING,
	APPROVED, 
	REJECTED
	

}
