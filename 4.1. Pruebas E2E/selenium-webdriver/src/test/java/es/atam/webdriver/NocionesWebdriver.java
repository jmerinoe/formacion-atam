package es.atam.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NocionesWebdriver {
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "D:\\ENVIRONMENT\\selenium\\chromedriver\\chromedriver.exe");

		// Instanciación navegador
		WebDriver driver = new ChromeDriver(DesiredCapabilities.chrome());
		
		try {
			// Opciones navegador a traves clase WebDriver
			driver.manage().window().maximize();

			driver.get("https://www.panel.es");

			// ImplicitWait
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

			// ExplicitWait
			Wait<WebDriver> wait = new WebDriverWait(driver, 5);
			WebElement cookiesButton = driver.findElement(By.xpath("//button[contains(@class,'cmplz-accept')]"));
			wait.until(ExpectedConditions.elementToBeClickable(cookiesButton));

			// Elemento web  - accion.
			cookiesButton.click();

			// Acciones.
			String xpathElementoMenu = "//*[@id='top-menu-nav']//a[.//*[contains(text(), 'QUÉ HACEMOS')]]";
			String xpathElementoSubMenu = "//a[contains(@href, 'zahori.io')]";
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath(xpathElementoMenu)))
				.click()
				.moveToElement(driver.findElement(By.xpath(xpathElementoSubMenu)))
				.click()
				.build().perform();

			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();	
		}
	}
}
