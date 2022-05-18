package es.atam.webdriver.portalempleado;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AuxDemoWD {

	/** Atributo WebDriver. */
	private WebDriver driver;

	/** Constantes con credenciales en portal del empleado. */
	private static final String TU_USUARIO = "jorge.merino";
	private static final String TU_PASSWORD = "50884634Dd";

	public AuxDemoWD(WebDriver driver) {
		this.driver = driver;
	}

	public void execute() {
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

		loadPage("http://empleado.panel.es");

		login(TU_USUARIO, TU_PASSWORD);

		accesoDirectorio();

		seleccionarGerencia("oi1");

		listarResultadosBusqueda();
	}

	private void loadPage(String URL) {
		try {
			driver.get(URL);
		} catch (TimeoutException e) {
			System.out.println("La web no ha cargado en el tiempo esperado.");
		}
	}

	private void login(String user, String password) {
		driver.findElement(By.id("usuario")).sendKeys(user);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[@value='aceptar']")).click();
	}

	private void accesoDirectorio() {
		driver.findElement(By.xpath("//a[contains(@href,'BuscarEmpleados')]")).click();
		Wait<WebDriver> wait = new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("DirectorioTitulo"))));
	}

	private void seleccionarGerencia(String nombreGerencia) {
		Select select = new Select(driver.findElement(By.name("udOrganizativa")));
		List<WebElement> listaOpciones = select.getOptions();
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && i < listaOpciones.size()) {
			String opcionActual = listaOpciones.get(i).getText();
			encontrado = opcionActual.toLowerCase().contains(nombreGerencia.toLowerCase());
			i++;
		}

		if (encontrado) {
			select.selectByValue(listaOpciones.get(i - 1).getAttribute("value"));
			driver.findElement(By.xpath("//a[@class='botonbuscar']")).click();
		} else {
			System.out.println("Error, no se ha encontrado la opcion solicitada: " + nombreGerencia);
			driver.quit();
			throw new RuntimeException("Error, no se ha encontrado la opcion solicitada: " + nombreGerencia);
		}
	}

	private void listarResultadosBusqueda() {
		List<WebElement> contenedoresEmpleado = driver.findElements(By
				.xpath("//div[@class='fichapersonal_espacioarriba']"));
		for (WebElement elementoActual : contenedoresEmpleado) {
			System.out.println(elementoActual.findElement(By.xpath(".//div[@class='fichaPQNombre']")).getText() + " "
					+ elementoActual.findElement(By.xpath(".//div[@class='fichaPQApellido'][1]")).getText() + " "
					+ elementoActual.findElement(By.xpath(".//div[@class='fichaPQApellido'][2]")).getText());
		}
	}
}
