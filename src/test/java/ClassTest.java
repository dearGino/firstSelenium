package src.test.java;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ClassTest {

	WebDriver driver;
	WebDriverWait wait;
	Scanner input = new Scanner(System.in);
	String fioInput;
	String telephoneInput;
	String emailInput;
	String addressInput;
	
	
	public ClassTest(String fioInput, String telephoneInput, String emailInput, String addressInput) {
		this.fioInput = fioInput;
		this.telephoneInput = telephoneInput;
		this.emailInput = telephoneInput;
		this.addressInput = telephoneInput;
	}
	
	 @Parameters
	    public static Collection<Object[]> data() {
	        return Arrays.asList(new Object[][] {
	        	{"Петров Петр Петрович", " (779) 012-3456", "qwertyqwerty", "Московская обл, г Лобня, ул Братьев Улюшкиных, д 1" },
	            { "Иванов Иван Иванович"," (779) 012-3456", "qwertyqwerty", "Московская обл, г Лобня, ул Братьев Улюшкиных, д 1" },
	            { "Сергеев Сергей Сергеевич"," (779) 012-3456", "qwertyqwerty", "Московская обл, г Лобня, ул Братьев Улюшкиных, д 1" } });
	    }
	
	
	@Before
	public void before() {
		

		System.out.println("Выберите один из браузеров:\nIE,\nFirefox,\nChrome");
		driver = null;
        try {
			String chosenBrowser = input.nextLine();
			    switch (chosenBrowser) {
				case "IE":
					System.setProperty("webdriver.ie.driver", "src/test/resources/iedriver/IEDriverServer.exe");
					driver = new InternetExplorerDriver();
					break;
				case "Firefox":
					System.setProperty("webdriver.gecko.driver", "src/test/resources/firefoxdriver/geckodriver.exe");
					driver = new FirefoxDriver();
					break;
				case "Chrome":
					System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver/chromedriver.exe");
					driver = new ChromeDriver();
					break;
				default:
					System.out.println("Неверное введены данные для выбора браузера");
					break;
			}
		} catch (Exception e) {
            System.out.println("Во время ввода произошла непредвиденная ошибка" + e);
		}
        input.close();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofMillis(4000));
		
	}
	
	
	@Test
	public void TestClass() throws Exception {
		
		//1 - Перейти по ссылке http://www.rgs.ru
		String URL = "https://www.rgs.ru/";
		driver.get(URL);
		String currentURL = driver.getCurrentUrl();
		Assert.assertEquals(currentURL, URL );


		//2.1 - Выбрать: Компаниям
		String companiesButtonXpath = "//a[contains(@href,'for-companies') and contains(text(),'Компаниям')]";
		WebElement companiesButton = driver.findElement(By.xpath(companiesButtonXpath));
		waitUtilElementToBeClickable(companiesButton);
		companiesButton.click();
		Assert.assertTrue("Клика по кнопке 'Компаниям' не произошло", companiesButton.getAttribute("class").contains("active"));
		
		//Убрать баннер
		try {
			By elem = By.id("fl-616371");
			wait.until(ExpectedConditions.presenceOfElementLocated(elem));
			WebElement iframe = driver.findElement(By.id("fl-616371"));
			driver.switchTo().frame(iframe);
			String annoyinMfXpath = "//div[contains(@class,'widget__close js-collapse-login')]";
			WebElement annoyinMf = driver.findElement(By.xpath(annoyinMfXpath));
			annoyinMf.click();
		} catch (Exception e) {
		} finally {
			driver.switchTo().defaultContent();
		}
		
	    
		//2.2- выбрать: Компаниям > Здоровье
		String healthButtonXpath = "//span[contains(@class,'padding') and text()='Здоровье']";
		WebElement healthButton = driver.findElement(By.xpath(healthButtonXpath));
		waitUtilElementToBeClickable(healthButton);
		healthButton.click();
		WebElement healthButtonExternalElement = driver.findElement(By.xpath("//span[contains(@class,'padding') and text()='Здоровье']/.."));
		Assert.assertTrue("Кнопка Компаниям не была нажата", healthButtonExternalElement.getAttribute("class").contains("active"));
		
		//2.3- Выбрать: Компаниям > Здоровье > Добровольное медицинское страхование
		String insuranceButtonXpath = "//a[contains(@href,'dobrovolnoe-meditsinskoe-strakhovanie') and text()='Добровольное медицинское страхование']";
		WebElement insuranceButton = driver.findElement(By.xpath(insuranceButtonXpath));
		waitUtilElementToBeClickable(insuranceButton);
		insuranceButton.click();
		//проверка перехода - наличие заголовка, пункт 2
		
		//2 - Проверить наличие заголовка – Добровольное медицинское страхование
		
		String insuranceTitleXpath = "//h1[contains(@class,'title word-breaking title--h2')]";
		By elem1 = By.xpath(insuranceTitleXpath);
		wait.until(ExpectedConditions.presenceOfElementLocated(elem1));
		WebElement insuranceTitle = driver.findElement(By.xpath(insuranceTitleXpath));
		Assert.assertTrue("Страница не загрузилась",
				  insuranceTitle.isDisplayed() && insuranceTitle.getText().contains("Добровольное медицинское страхование"));
       

		//закрыть попап с куки
		String cookiesClose = "//button[@class='btn--text']";
		WebElement cookiesBtnClose = driver.findElement(By.xpath(cookiesClose));
		cookiesBtnClose.click();
		Assert.assertTrue("Попап с куки не был закрыт", driver.findElements(By.xpath("cookiesClose")).isEmpty());
		
        
		//4 - Нажать на кнопку "Отправить заявку"
		String applicationButtonXpath = "//span[contains(text(),'Отправить заявку')]/..";
		WebElement applicationButton = driver.findElement(By.xpath(applicationButtonXpath));
		waitUtilElementToBeClickable(applicationButton);
		scrollToElementJs(applicationButton);
		applicationButton.click();
		JavascriptExecutor j = (JavascriptExecutor) driver;
		Long position = (Long)j.executeScript("return window.pageYOffset;");
		Assert.assertTrue("Скрол после нажатия на кнопку не произошёл", position != 0);
		
		
		//5 - Страница просрколилась до текста "Оперативно перезвоним для оформления полиса"
		String titleWillCallXpath = "//h2[contains(text(),'Оперативно перезвоним')]";
		WebElement titleWillCall = driver.findElement(By.xpath(titleWillCallXpath));
		Assert.assertTrue("Страница не прокрутилась",
				titleWillCall.isDisplayed() && titleWillCall.getText().contains("Оперативно перезвоним"));
       
		
		//6 - Заполнить поля: Имя, Фамилия, Отчество, Регион, Телефон, Эл. почта – qwertyqwerty, галочка Я согласен на обработку
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));
		WebElement fio = driver.findElement(By.xpath("//input[@name='userName']"));
		fillInputField(fio, fioInput);
		Assert.assertEquals(fioInput, fio.getAttribute("value"));
		WebElement telephone = driver.findElement(By.xpath("//input[@name='userTel']"));
		fillInputField(telephone,telephoneInput.trim());
		Assert.assertEquals("+7"+telephoneInput, telephone.getAttribute("value"));
		WebElement email = driver.findElement(By.xpath("//input[@name='userEmail']"));
		fillInputField(email, emailInput);
		Assert.assertEquals(emailInput, email.getAttribute("value"));
		WebElement address = driver.findElement(By.xpath("//input[@class='vue-dadata__input']"));
		fillInputField(address, addressInput);
		Assert.assertEquals(addressInput, address.getAttribute("value"));
		email.click();
		String checkboxInput = "true";
		WebElement checkboxSpan = driver.findElement(By.xpath("//span[contains(text(),'Я соглашаюсь с условиями')]"));
		clickOnCheckbox(checkboxSpan);
		WebElement checkbox = driver.findElement(By.xpath("//input[@type='checkbox']"));
		Assert.assertEquals(checkboxInput, checkbox.getAttribute("value"));
		
		
		
		//7 - Проверить, что все поля заполнены введенными значениями
		checkInputValue(fio, fioInput);
		checkInputValue(telephone,"+7"+telephoneInput);
		checkInputValue(email, emailInput);
		checkInputValue(address, addressInput);
		checkInputValue(checkbox, checkboxInput);
        
		
		//8 - Нажать кнопку "Отправить"
		String sendButtonXpath ="//button[@type='submit']";
		WebElement sendButton = driver.findElement(By.xpath(sendButtonXpath));
		scrollToElementJs(sendButton);
		waitUtilElementToBeClickable(sendButton);
		sendButton.click();
		Assert.assertTrue("Клик на кнопку 'Свяжитесь мо мной' не произошел",
				sendButton.getAttribute("class").contains("disabled"));
		
		//9 -Проверка, что есть ошибка под полем с неправильным email
		String errorAlertXPath = "//span[@class='input__error text--small']";
		WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
		scrollToElementJs(errorAlert);
		checkErrorMessageAtField(errorAlert,"Введите корректный адрес электронной почты");
       
	}

	
	
	
	@After
	public void after() {
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {		
//			e.printStackTrace();
//		}
		driver.quit();
	}
	
	
	private void waitUtilElementToBeClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
    
	//пролистывает страницу до переданного элемента
	private void scrollToElementJs(WebElement element) {
		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	//Заполняет поля переданного элемента переданным значением
	private void fillInputField(WebElement element, String value) {
		scrollToElementJs(element);
		waitUtilElementToBeClickable(element);
		element.click();
		element.clear();
		element.sendKeys(value);
	}
	
	//кликает на переданный элемент (создан для выбора чекбоксов)
	private void clickOnCheckbox(WebElement element) {
		scrollToElementJs(element);
		waitUtilElementToBeClickable(element);
		element.click();
	}
    
	//проверяет соответствие значения в элементе с переданным значением
	private void checkInputValue(WebElement element, String value) {
		Assert.assertEquals("Поле было заполнено некорректно", value, element.getAttribute("value"));
	}
	
	//проверяет, появилось ли неверно заполненного поля сообщение об ошбке
	private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        element = element.findElement(By.xpath("./..//span"));
        Assert.assertEquals("Проверка ошибки у поля не была пройдена",
			    errorMessage, element.getText());
	}
}