import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTestRunner {
    public static void runTest(String url) {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver"); // Set your WebDriver path

        WebDriver driver = new ChromeDriver();
        try {
            driver.get(url);
            System.out.println("Page title: " + driver.getTitle());
        } finally {
            driver.quit();
        }
    }
}
