package es.atam.tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DemoTest {

	private static final String BASE_API_URL = "https://www.metaweather.com/api";

	@Test
     public void maxTemp() {
		int woeID = getwoeID("Madrid");
		Assert.assertTrue(woeID > 0, "Validacion woe ID valido");
		
		Double maxTemp = -273.0;
		String maxTempDate = "";
		List<Map<String,Object>> weatherNextDays = getWeather(woeID); 
		for (Map<String,Object> currentDay: weatherNextDays) {
			Double candidateMaxTemp = Double.parseDouble(currentDay.get("max_temp").toString());
			if (candidateMaxTemp > maxTemp) {
				maxTemp = candidateMaxTemp;
				maxTempDate = currentDay.get("applicable_date").toString();
			}
		}
		System.out.println("El dia " + maxTempDate + " sera el mas caluroso con " + maxTemp + "º");
		Assert.assertTrue(maxTemp > 0, "Validacion temperatura maxima valida");
		Assert.assertTrue(!maxTempDate.isEmpty(), "Validacion dia mas caluroso valido");
	}
	
	private int getwoeID(String cityName) {
		 Response response = RestAssured
                 .given()
                    .baseUri(BASE_API_URL)
                 .when()
                    .get("/location/search/?query=" + cityName)
                 .then()
                    .log().all()
                    .and().extract().response();
		 
		 JsonPath jsonPathEvaluator = response.jsonPath();
		 List<Integer> allData = jsonPathEvaluator.getList("woeid");
		 return allData.get(0);
     }
	
	
	 private List<Map<String,Object>> getWeather(int woeID) {
		 Response response = RestAssured
                 .given()
                    .baseUri(BASE_API_URL)
                 .when()
                    .get("/location/" + woeID)
                 .then()
                    .log().all()
                    .and().extract().response();
		 
		 return response.path("consolidated_weather");
	 }
}
