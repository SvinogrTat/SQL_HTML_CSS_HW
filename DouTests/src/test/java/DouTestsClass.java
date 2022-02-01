import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class DouTestsClass {
    WebDriver driver;

    @DataProvider(name = "positive_search")
    public Object[][] dpMethod() {
        return new Object[][]{{"топ-50"}, {"nix"}, {"зарплат"}, {"тшч"}, {"   nix   "}};
    }

    @DataProvider(name = "negative_search")
    public Object[][] dpMethod2() {
        return new Object[][]{{"NIX25"}};
    }

    @BeforeTest(alwaysRun = true)
    public void InitialiseWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/Users/tetianavynogradska/IdeaProjects/WebDrivers/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("incognito");
        this.driver = new ChromeDriver(chromeOptions);
    }

    @Test(dataProvider = "positive_search", groups = "positive_test")
    public void SearchHP(String textToSearch) {

        //Go to Dou.ua URL
        driver.navigate().to("https://dou.ua/");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Enter key phrase to search
        WebElement search = driver.findElement(By.xpath("//input[@class = 'inp']"));
        search.sendKeys(textToSearch);
        search.sendKeys(Keys.ENTER);

        //Wait the result and verify that first link contains key phrase
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("(//a[@class = 'gs-title'])[1]"))));
        String searchTop50 = driver.findElement(By.xpath("(//a[@class = 'gs-title'])[1]")).getText().toLowerCase();
        Assert.assertTrue(searchTop50.contains(textToSearch));
    }

    @Test(dataProvider = "negative_search", groups = "negative_test")
    public void CompanySearchNegative(String textToSearch) {
        //Go to Dou.ua URL
        driver.navigate().to("https://dou.ua/");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Enter key phrase to search
        WebElement search = driver.findElement(By.xpath("//input[@class = 'inp']"));
        search.sendKeys(textToSearch);
        search.sendKeys(Keys.ENTER);

        //Wait the result and verify that first link contains key phrase
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class = 'gs-snippet']"))));
        String searchTop50 = driver.findElement(By.xpath("//div[@class = 'gs-snippet']")).getText().toLowerCase();
        Assert.assertEquals(searchTop50, "ничего не найдено");
    }

    @Test(groups = "positive_test")
    public void SalaryCheck() {
        //Go to Dou.ua URL
        driver.navigate().to("https://dou.ua/");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Go to Salaries page
        driver.findElement(By.xpath("//a[text() = 'Зарплати']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1"))));


        //Configure parameters
        Select salaryDropdown = new Select(driver.findElement(By.xpath("//select[@name = 'city']")));
        salaryDropdown.selectByVisibleText("Харків");
        driver.findElement(By.xpath("//select[@class = 'salarydec-field-title']/optgroup[@label = 'QA']/option[text() = 'QA engineer']")).click();
        Select specDropdown = new Select(driver.findElement(By.xpath("//select[@name = 'spec']")));
        specDropdown.selectByVisibleText("General QA");

        //Get value and compare it
        WebElement minSalary = driver.findElement(By.xpath("//dd[@class = 'salarydec-results-min']/span[@class = 'num']"));
        int minSalaryValue = Integer.parseInt(minSalary.getText());
        Assert.assertTrue(minSalaryValue > 1000);
    }

    @Test(groups = "positive_test")
    public void Top50Test() {
        //Go to Dou.ua URL
        driver.navigate().to("https://dou.ua/");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Go to the Top-50 link and wait
        driver.findElement(By.xpath("//a[text() = 'Топ-50']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1"))));


        //Get the number of companies and assert
        List listOfCompanies = driver.findElements(By.xpath("//a[@class = 'company-name']"));
        int numberOfElements = listOfCompanies.size();
        Assert.assertEquals(50, numberOfElements);
    }

    @Test(groups = "positive_test")
    public void TopicalACompare() {
        //Go to Dou.ua URL
        driver.navigate().to("https://dou.ua/");

        //Wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //Get right img and click the same page
        String src1 = driver.findElement(By.xpath("//li/a[@rel = 'nofollow']")).getAttribute("href");
        driver.findElement(By.xpath("//li[@class = 'logo']/a[@href = 'https://dou.ua/']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[text() = 'Свіже']"))));


        //Get the img making sure that it is the same page
        String src2 = driver.findElement(By.xpath("//li/a[@rel = 'nofollow']")).getAttribute("href");
        Assert.assertEquals(src1, src2);
    }

    @AfterClass(alwaysRun = true)
    public void CloseWebDriver() {
        driver.close();
    }
}
