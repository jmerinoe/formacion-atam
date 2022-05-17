package es.atam.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DemoTestDataProvider {

	private static final String BASE_API_URL = "https://www.metaweather.com/api";

	@DataProvider (name = "data-provider")
	public Object[] dpMethod(){
		JsonPath testData;
		try {
			testData = getTestData();
			return testData.getList("name").toArray(new String[0]);
		} catch (IOException e) {
			return new String[0];
		}
	}
	
	@Test (dataProvider = "data-provider")
    public void maxTemp(String cityName) {
		int woeID = getwoeID(cityName);
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
	 
	 private JsonPath getTestData() throws IOException {
		 byte[] bytesFromJsonFile = Files.readAllBytes(Paths.get("src/test/resources/cities.json"));
		 return new JsonPath(new String(bytesFromJsonFile));
	 }
	 
}
