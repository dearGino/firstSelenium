package test.java;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestClass {

	WebDriver driver;
	WebDriverWait wait;
	@Before
	public void before() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 20, 1000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
	}
	
	
	@Test
	public void TestClass() throws InterruptedException {
		
		//1 - Перейти по ссылке http://www.rgs.ru
		driver.get("https://www.rgs.ru/");
				
		//2.1 - Выбрать: Компаниям
		String companiesButtonXpath = "//a[contains(@href,'for-companies') and contains(text(),'Компаниям')]";
		WebElement companiesButton = driver.findElement(By.xpath(companiesButtonXpath));
		waitUtilElementToBeClickable(companiesButton);
		companiesButton.click();	
		
		//Убрать ужасный баннер
		By elem = By.id("fl-616371");
		wait.until(ExpectedConditions.presenceOfElementLocated(elem));
		WebElement iframe = driver.findElement(By.id("fl-616371"));
		driver.switchTo().frame(iframe);
		String annoyinMfXpath = "//div[contains(@data-fl-track,'click-close-login') and contains(@class,'widget__close js-collapse-login')]";
		WebElement annoyinMf = driver.findElement(By.xpath(annoyinMfXpath));
		annoyinMf.click();
		driver.switchTo().defaultContent();
		
	    
		//2.2- выбрать: Компаниям > Здоровье
		String healthButtonXpath = "//span[contains(@class,'padding') and text()='Здоровье']";
		WebElement healthButton = driver.findElement(By.xpath(healthButtonXpath));
		waitUtilElementToBeClickable(healthButton);
		healthButton.click();
		
		//2.3- Выбрать: Компаниям > Здоровье > Добровольное медицинское страхование
		String insuranceButtonXpath = "//a[contains(@href,'dobrovolnoe-meditsinskoe-strakhovanie') and text()='Добровольное медицинское страхование']";
		WebElement insuranceButton = driver.findElement(By.xpath(insuranceButtonXpath));
		waitUtilElementToBeClickable(insuranceButton);
		insuranceButton.click();
		
		//2 - Проверить наличие заголовка – Добровольное медицинское страхование
		String insuranceTitleXpath = "//h1[contains(@class,'title word-breaking title--h2')]";
		WebElement insuranceTitle = driver.findElement(By.xpath(insuranceTitleXpath));
		Assert.assertTrue("Страница не загрузилась",
				  insuranceTitle.isDisplayed() && insuranceTitle.getText().contains("Добровольное медицинское страхование"));
       

		//закрыть попап с куки
		String cookiesClose = "//button[@class='btn--text']";
		WebElement cookiesBtnClose = driver.findElement(By.xpath(cookiesClose));
		cookiesBtnClose.click();
		
        
		//4 - Нажать на кнопку "Отправить заявку"
		String applicationButtonXpath = "//span[contains(text(),'Отправить заявку')]/..";
		WebElement applicationButton = driver.findElement(By.xpath(applicationButtonXpath));
		waitUtilElementToBeClickable(applicationButton);
		scrollToElementJs(applicationButton);
		applicationButton.click();
		
		
		//5 - Страница просрколилась до текста "Оперативно перезвоним для оформления полиса"
		String titleWillCallXpath = "//h2[contains(text(),'Оперативно перезвоним')]";
		WebElement titleWillCall = driver.findElement(By.xpath(titleWillCallXpath));
		Assert.assertTrue("Страница не прокрутилась",
				titleWillCall.isDisplayed() && titleWillCall.getText().contains("Оперативно перезвоним"));
       
		
		//6 - Заполнить поля: Имя, Фамилия, Отчество, Регион, Телефон, Эл. почта – qwertyqwerty, галочка Я согласен на обработку
		String _address ="Московская обл, г Лобня, ул Братьев Улюшкиных, д 1";
		String _fio = "Петров Петр Петрович";
		String _telephone = " (779) 012-3456";
		String _email = "qwertyqwerty";
		String _checkbox = "true";
		
		//Thread.sleep(2000);
		WebElement fio = driver.findElement(By.xpath("//input[@name='userName']"));
		fillInputField(fio, _fio);
		WebElement telephone = driver.findElement(By.xpath("//input[@name='userTel']"));
		fillInputField(telephone,_telephone.trim());
		WebElement email = driver.findElement(By.xpath("//input[@name='userEmail']"));
		fillInputField(email, _email);
		WebElement address = driver.findElement(By.xpath("//input[@class='vue-dadata__input']"));
		fillInputField(address, _address);
		email.click();
		WebElement checkboxSpan = driver.findElement(By.xpath("//span[contains(text(),'Я соглашаюсь с условиями')]"));
		clickOnCheckbox(checkboxSpan);
		
		
		//7 - Проверить, что все поля заполнены введенными значениями
		checkInputValue(fio, _fio);
		checkInputValue(telephone,"+7"+_telephone);
		checkInputValue(email, _email);
		checkInputValue(address, _address);
		WebElement checkbox = driver.findElement(By.xpath("//input[@type='checkbox']"));
		checkInputValue(checkbox, _checkbox);
        
		
		//8 - Нажать кнопку "Отправить"
		String sendButtonXpath ="//button[@type='submit']";
		WebElement sendButton = driver.findElement(By.xpath(sendButtonXpath));
		scrollToElementJs(sendButton);
		waitUtilElementToBeClickable(sendButton);
		sendButton.click();
        
		
		//9 -Проверка, что есть ошибка под полем с неправильным email
		String errorAlertXPath = "//span[@class='input__error text--small']";
		WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
		scrollToElementJs(errorAlert);
		checkErrorMessageAtField(errorAlert,"Введите корректный адрес электронной почты");
       
	}

	
	
	
	@After
	public void after() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.quit();
	}
	
	private void waitUtilElementToBeClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
    
	private void scrollToElementJs(WebElement element) {
		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	private void fillInputField(WebElement element, String value) {
		scrollToElementJs(element);
		waitUtilElementToBeClickable(element);
		element.click();
		element.clear();
		element.sendKeys(value);
	}
	
	private void clickOnCheckbox(WebElement element) {
		scrollToElementJs(element);
		waitUtilElementToBeClickable(element);
		element.click();
	}
    
	private void checkInputValue(WebElement element, String value) {
		Assert.assertEquals("Поле было заполнено некорректно", value, element.getAttribute("value"));
	}
	
	private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        element = element.findElement(By.xpath("./..//span"));
        Assert.assertEquals("Проверка ошибки у поля не была пройдена",
			    errorMessage, element.getText());
	}
}
