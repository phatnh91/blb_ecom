package pageObjects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Reporter;
import report.VerificationFailures;

import java.time.Duration;

public class BaseTest {


    protected WebDriver driver;
    protected Log log;

    protected BaseTest(){
        log = LogFactory.getLog(getClass());
    }

    public WebDriver getDriver(){
        return this.driver;
    }

        protected WebDriver getBrowserDriver (String browserName){


            if (browserName.equals("firefox")) {
                driver = new FirefoxDriver();
            } else if (browserName.equals("h_firefox")) {
                //Browser options: Selenium > 3. version
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("-headless");
                options.addArguments("window-size=1920x1080");
                driver = new FirefoxDriver(options);
            } else if (browserName.equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
//                options.addArguments("--disable-blink-features=AutomationControlled"); // Hides automation flag
                options.addArguments("--disable-infobars"); // Hides "Chrome is being controlled..."
                options.addArguments("--start-maximized");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--no-sandbox");
                options.setExperimentalOption("debuggerAddress", "localhost:9222");
                options.addArguments("--disable-blink-features=AutomationControlled");
                driver = new ChromeDriver(options);
                ((JavascriptExecutor) driver).executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");


            } else if (browserName.equals("h_chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("-headless");
                options.addArguments("window-size=1920x1080");
                driver = new ChromeDriver(options);
            } else {
                throw new RuntimeException("Browser name is invalid");
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.manage().window().maximize();
//            driver.get(urlPage);
            return driver;
        }

    protected void verifyTrue(boolean condition, String errorMessage) {
        boolean pass = true;
        try {
            Assert.assertTrue(errorMessage, condition);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            pass = false;
            log.info(" -------------------------- FAILED -------------------------- ");

            // Add lỗi vào ReportNG
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
    }

    protected void verifyFalse(boolean condition, String errorMessage) {
        boolean pass = true;
        try {

            Assert.assertFalse(errorMessage, condition);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            pass = false;
            log.info(" -------------------------- FAILED -------------------------- ");
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
    }

    protected boolean verifyEquals(Object actual, Object expected) {
        boolean pass = true;
        try {
            Assert.assertEquals(expected, actual);
            log.info(" -------------------------- PASSED -------------------------- ");
        } catch (Throwable e) {
            pass = false;
            log.info(" -------------------------- FAILED -------------------------- ");
            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return pass;
    }



    }
