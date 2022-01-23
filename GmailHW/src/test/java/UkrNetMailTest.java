import org.junit.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

public class UkrNetMailTest {
    WebDriver driver;

    @BeforeTest
    public void InitialiseWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/Users/tetianavynogradska/IdeaProjects/WebDrivers/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("incognito");
        this.driver = new ChromeDriver(chromeOptions);
    }

    @Test
    public void LogInHP() {
        //Go to Ukr.net URL and select frame
        driver.navigate().to("https://www.ukr.net/");
        driver.switchTo().frame("mail widget");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Enter email and password
        driver.findElement(By.xpath("//input[@name = 'login']")).sendKeys("---");
        driver.findElement(By.xpath("//input[@name = 'password']")).sendKeys("---");
        driver.findElement(By.xpath("//button[@class = 'form__submit']")).click();

        //Verify email
        new WebDriverWait(driver, Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) webDriver -> webDriver.findElement(By.xpath("//p[@id = 'id-user-email']")).getText().contentEquals("malykh_marina@ukr.net"));
        String email = driver.findElement(By.xpath("//p[@id = 'id-user-email']")).getText();
        Assert.assertEquals(email, "malykh_marina@ukr.net");
    }

    @Test
    public void LogInNegative() {
        //Go to Ukr.net URL and select frame
        driver.navigate().to("https://www.ukr.net/");
        driver.switchTo().frame("mail widget");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Enter email and password
        driver.findElement(By.xpath("//input[@name = 'login']")).sendKeys("---");
        driver.findElement(By.xpath("//input[@name = 'password']")).sendKeys("Wrong");
        driver.findElement(By.xpath("//button[@class = 'form__submit']")).click();

        //Verify error text
        new WebDriverWait(driver, Duration.ofSeconds(5)).until((ExpectedCondition<Boolean>) webDriver -> webDriver.findElement(By.xpath("//p[@class = 'form__error form__error_wrong form__error_visible']")).isDisplayed());
        String email = driver.findElement(By.xpath("//p[@class = 'form__error form__error_wrong form__error_visible']")).getText();
        Assert.assertEquals(email, "Неправильні дані");
    }

    @Test
    public void SendEmailNegative() {
        //Go to Ukr.net URL
        driver.navigate().to("https://accounts.ukr.net/login");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Enter email and password
        driver.findElement(By.xpath("//input[@name = 'login']")).sendKeys("---");
        driver.findElement(By.xpath("//input[@name = 'password']")).sendKeys("---");
        driver.findElement(By.xpath("//button[@type = 'submit']")).click();

        //Go to mail box and send mail without recipients
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[@class = 'button primary compose']"))));
        driver.findElement(By.xpath("//button[@class = 'button primary compose']")).click();
        driver.findElement(By.xpath("//button[@class = 'button primary send']")).click();

        //Verify that error is shown
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class = 'modal show']//div[@class = 'alert__content']"))));
        String errorText = driver.findElement(By.xpath("//div[@class = 'modal show']//div[@class = 'alert__content']")).getText();
        Assert.assertTrue(errorText.contains("Потрібно вказати хоча б одного отримувача в полі 'Кому"));
    }

    @Test
    public void SendEmail() {
        //Go to Ukr.net URL
        driver.navigate().to("https://accounts.ukr.net/login");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Enter email and password
        driver.findElement(By.xpath("//input[@name = 'login']")).sendKeys("---");
        driver.findElement(By.xpath("//input[@name = 'password']")).sendKeys("---");
        driver.findElement(By.xpath("//button[@type = 'submit']")).click();

        //Go to mail box and send mail with recipient and subject
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[@class = 'button primary compose']"))));
        driver.findElement(By.xpath("//button[@class = 'button primary compose']")).click();
        driver.findElement(By.xpath("//input[@name = 'toFieldInput']")).sendKeys("svinogr.tat@gmail.com");
        driver.findElement(By.xpath("//input[@name = 'subject']")).sendKeys("test");
        driver.findElement(By.xpath("//button[@class = 'button primary send']")).click();

        //Verify that error is shown
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@class = 'link3']"))));
        String text = driver.findElement(By.xpath("//a[@class = 'link3']")).getText();
        Assert.assertEquals(text, "лист");
    }

    @AfterClass
    public void CloseWebDriver() {
        driver.close();
    }
}
