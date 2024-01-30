package com.testautomation.apitesting.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.FileNameConstents;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class PostApiRequestUsingFile {
	
	@Test
	public void postAPIRequest() {
		
		try {
			String PostAPIRequestBody=FileUtils.readFileToString(new File(FileNameConstents.POST_API_API_REQUEST_BODY),"UTF-8");
		//System.out.println(PostAPIRequestBody);
		
			Response response=
			RestAssured
				.given()
					.contentType(ContentType.JSON)
					.body(PostAPIRequestBody)
					.baseUri("https://restful-booker.herokuapp.com/booking")
				.when()
					.post()
				.then()
					.assertThat()
					.statusCode(200)
					.extract()
					.response();
		JSONArray jsonArray=	JsonPath.read(response.body().asString(),"$.booking..firstname");
			
			String firstname=(String) jsonArray.get(0);
			Assert.assertEquals(firstname, "nisha");
			
JSONArray jsonArrayLastname=	JsonPath.read(response.body().asString(),"$.booking..lastname");
			
			String lastname=(String) jsonArrayLastname.get(0);
			Assert.assertEquals(lastname, "khatiwada");
			
			
JSONArray jsonArrayBookingDates=	JsonPath.read(response.body().asString(),"$.booking..bookingdates.checkin");
			
			String bookingDates=(String) jsonArrayBookingDates.get(0);
			Assert.assertEquals(bookingDates, "2018-01-01");
			
int BookingID=	JsonPath.read(response.body().asString(),"$.bookingid");
			
			
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.get("/{BookingID}",BookingID)
			.then()
				.assertThat()
				.statusCode(200);
			
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
