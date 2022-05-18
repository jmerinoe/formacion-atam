package es.atam.webdriver.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AtamTests {

	private WebDriver driver;
	
	@Test
	public void cargarMasNoticias() {
		initAtamHome();
		
		int initialNewsDisplayed = driver.findElements(By.xpath("//*[@id='noticias']//article")).size();
		Assert.assertTrue(initialNewsDisplayed == 7, "Validacion numero noticias mostradas por defecto");
		
		boolean continueLoadingNews = true;
		int loadMoreNewsClicks = 0;
		
		while (continueLoadingNews) {
			loadMoreNewsClicks++;
			loadMoreNews();
			int newsDisplayed = driver.findElements(By.xpath("//*[@id='noticias']//article")).size();
			int expectedNews = loadMoreNewsClicks*6 + initialNewsDisplayed;
			continueLoadingNews = newsDisplayed == expectedNews;
		}
		
		int totalNewsDisplayed = driver.findElements(By.xpath("//*[@id='noticias']//article")).size();
		WebElement moreNewsButton = driver.findElement(By.xpath("//*[@id='noticias']//div[@class='load-more']//button"));
		moreNewsButton.click();
		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.invisibilityOf(moreNewsButton));
		Assert.assertEquals(totalNewsDisplayed, driver.findElements(By.xpath("//*[@id='noticias']//article")).size());
	}
	
	@BeforeTest
	private void setup() {
		System.setProperty("webdriver.chrome.driver", "D:\\ENVIRONMENT\\selenium\\chromedriver\\chromedriver.exe");
		this.driver = new ChromeDriver(DesiredCapabilities.chrome());
		driver.manage().window().maximize();
	}
	
	@AfterTest
	private void tearDown() {
		if (this.driver != null) {
			this.driver.quit();
		}
	}
	
	
	private void initAtamHome() {
		driver.get("https://www.atam.es");
		
		// Espera carga pagina
		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		WebElement cookiesButton = driver.findElement(By.id("cn-accept-cookie"));
		wait.until(ExpectedConditions.elementToBeClickable(cookiesButton));
		
		cookiesButton.click();
	}
	
	
	private void loadMoreNews() {
		WebElement moreNewsButton = driver.findElement(By.xpath("//*[@id='noticias']//div[@class='load-more']//button"));
		moreNewsButton.click();
		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.attributeToBe(By.xpath("//*[@id='noticias']//div[@class='load-more']//button"), "data-enabled", "true"));
	}
	
}
