package es.atam.webdriver.portalempleado;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DemoWD {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "D:\\ENVIRONMENT\\selenium\\chromedriver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver(DesiredCapabilities.chrome());
		AuxDemoWD demo = new AuxDemoWD(driver);
		demo.execute();
		driver.quit();
	}

}
