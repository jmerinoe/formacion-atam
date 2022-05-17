package es.atam.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DemoTestCities {

	private static final String BASE_API_URL = "https://www.metaweather.com/api";
	
	private static final String KEY_DATE = "DATE";
	private static final String KEY_TEMP = "TEMP";
	private static final String KEY_CITY = "CITY";

	@Test
    public void maxTemp() {
		try {
			JsonPath testData = getTestData();
			List<String> cities = testData.getList("name");
			List<Map<String,Object>> citiesData = new ArrayList<>();
			for (String currentCity: cities) {
				Map<String,Object> cityData = executeIteration(currentCity);
				cityData.put(KEY_CITY, currentCity);
				citiesData.add(cityData);
			}
			
			Double maxTemp = citiesData.stream()
				.mapToDouble(map -> (Double)map.get(KEY_TEMP))
				.max()
				.orElse(0);
			
			Map<String, Object> result = citiesData.stream()
                    .filter(map -> maxTemp.equals(map.get(KEY_TEMP)))
                    .collect(Collectors.toList())
                    .get(0);
			
			System.out.println("### El dia mas caluroso se dara en " 
					+ result.get(KEY_CITY) + " el dia "
					+ result.get(KEY_DATE) + " con una temperatura de "
					+ result.get(KEY_TEMP) + "º");
			
		} catch (IOException e) {
			Assert.assertTrue(false, "No se ha podido recuperar datos del test");
		}
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
	 
	 private Map<String,Object> executeIteration(String cityName) {
		 	Map<String,Object> result = new HashMap<>();
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
			result.put(KEY_DATE, maxTempDate);
			result.put(KEY_TEMP, maxTemp);
			return result;
	 }
}
