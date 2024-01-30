package com.testautomation.apitesting.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstents;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

public class EndToEndAPITest extends BaseTest {
	
	@Test
	public void e2eAPIRequest() {
		
		try {
			String PostAPIRequestBody=FileUtils.readFileToString(new File(FileNameConstents.POST_API_API_REQUEST_BODY),"UTF-8");
			String TokenAPIRequestBody=FileUtils.readFileToString(new File(FileNameConstents.TOKEN_API_BODY),"UTF-8");
			String PutAPIRequestBody=FileUtils.readFileToString(new File(FileNameConstents.PUT_API_BODY),"UTF-8");
			String PatchAPIRequestBody=FileUtils.readFileToString(new File(FileNameConstents.PATCH_API_BODY),"UTF-8");

			//post call
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
			

			

			
int BookingID=	JsonPath.read(response.body().asString(),"$.bookingid");
System.out.println(BookingID);
		
//get call
			
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.get("/{BookingID}",BookingID)
			.then()
				.assertThat()
				.statusCode(200);
		
		
		//post call for token
		
		Response tokenApiresponse=
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.body(TokenAPIRequestBody)
				.baseUri("https://restful-booker.herokuapp.com/auth")
			.when()
				.post()
			.then()
				.assertThat()
				.statusCode(200)
			.extract()
				.response();
		
		String token_id= JsonPath.read(tokenApiresponse.body().asString(),"$.token");
		
		//put call
		
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.header("Cookie", "token="+token_id)
				
				.body(PutAPIRequestBody)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.put("/{BookingID}", BookingID)
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname", Matchers.equalTo("Specflow"));
		
		
		//patch call
		
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.header("Cookie", "token="+token_id)
				.body(PatchAPIRequestBody)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.patch("/{BookingID}",BookingID)
			.then()
				.assertThat()
				.statusCode(200)
				.body("firstname",Matchers.equalTo("Testers Talk"));
		
		
		
		//delete api call
		
		RestAssured
			.given()
				.contentType(ContentType.JSON)
				.header("Cookie", "token="+token_id)
				.baseUri("https://restful-booker.herokuapp.com/booking")
			.when()
				.delete("/{BookingID}",BookingID)
			.then()
				.assertThat()
				.statusCode(201);
		
		
		
		
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
