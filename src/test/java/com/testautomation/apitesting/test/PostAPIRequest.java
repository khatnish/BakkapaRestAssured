package com.testautomation.apitesting.test;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.testautomation.apitesting.utils.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

public class PostAPIRequest extends BaseTest {
	@Test
	public void createBooking() {
		JSONObject booking = new JSONObject();
		JSONObject bookingDates= new JSONObject();
		
		booking.put("firstname", "nisha");
		booking.put("lastname", "khatiwoda");
		booking.put("totalprice", 1000);
		booking.put("depositpaid",true);
		booking.put("bookingdates", bookingDates);
		booking.put("additionalneeds", "music");
		
		bookingDates.put("checkin", "2024-01-23");
		bookingDates.put("checkout", "2024-01-25");
		Response response=
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(booking.toString())
				.baseUri("https://restful-booker.herokuapp.com/booking")
				//.log().body()
			.when()
				.post()
			.then()
				.assertThat()
				.statusCode(200)
				.body("booking.firstname", Matchers.equalTo("nisha"))
				.body("booking.totalprice", Matchers.equalTo(1000))
				.body("booking.bookingdates.checkin", Matchers.equalTo("2024-01-23"))
				//.log().ifValidationFails()
			.extract()
				.response();
		
		int bookingId=response.path("bookingid");
		
		
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.pathParam("bookingID", bookingId)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			
			.when()
				.get("{bookingID}")
			
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", Matchers.equalTo("nisha"))
				.body("lastname", Matchers.equalTo("khatiwoda"))
				.body("bookingdates.checkin", Matchers.equalTo("2024-01-23"));
				
		
	}

}
