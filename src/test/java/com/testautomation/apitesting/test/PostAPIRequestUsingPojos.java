package com.testautomation.apitesting.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testautomation.apitesting.pojos.Booking;
import com.testautomation.apitesting.pojos.BookingDates;
import com.testautomation.apitesting.utils.FileNameConstents;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class PostAPIRequestUsingPojos {
	
	@Test
	public void postApiRequest() {
		try {
			String jsonSchema=FileUtils.readFileToString(new File(FileNameConstents.JSON_SCHEMA),"UTF-8");
		
		
		
			BookingDates bookingdates=new BookingDates("2024-01-25","2024-01-30");
			Booking booking = new Booking("nisha","khatiwada","music",1000,true,bookingdates);
			
			ObjectMapper objectmapper=new ObjectMapper();
String requestBody=	objectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
	System.out.println(requestBody);
	
	
	//deserialization
	
	Booking bookingDetails=objectmapper.readValue(requestBody, Booking.class);
	System.out.println(bookingDetails.getFirstname());
	System.out.println(bookingDetails.getLastname());
	System.out.println(bookingDetails.getAdditionalneeds());
	
	Response response=
	RestAssured
		.given()
			.contentType(ContentType.JSON)
			.body(requestBody)
			.baseUri("https://restful-booker.herokuapp.com/booking")
		.when()
			.post()
		.then()
			.assertThat()
			.statusCode(200)
		.extract()
			.response();
	
	int bookingId=response.path("bookingid");
	System.out.println(jsonSchema);
	
	
	RestAssured
		.given()
			.contentType(ContentType.JSON)
			.baseUri("https://restful-booker.herokuapp.com/booking")
		.when()
			.get("/{bookingId}",bookingId)
		.then()
			.assertThat()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
	
	//System.out.println(jsonSchema);
	
	
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		}

}
