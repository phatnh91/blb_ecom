import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.BasePage;
import pageObjects.BaseTest;
import pageObjects.DetailPage;
import pageObjects.HomePage;
import report.ExtentTestManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;


public class UIAutomationTest extends BaseTest {
        WebDriver driver;
        DetailPage detailPage;
        HomePage homePage;
        String mainWindow;

    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browserName) {
        driver = getBrowserDriver(browserName);
        mainWindow = driver.getWindowHandle();
        homePage = new HomePage(driver);
    }

        @Test
        public void crawlData(Method method) throws IOException {
            ExtentTestManager.startTest(method.getName(), "Get Item Data");
            int view = Integer.parseInt(System.getProperty("view"));
            int creationDays = Integer.parseInt(System.getProperty("day"));
            String isSold = System.getProperty("sold");
            System.out.println("View is set = "+view);
            System.out.println("Creation Day less than "+creationDays);
            System.out.println("Check Item Sold "+isSold);
            List<Integer> indexes = homePage.getItemIndexesWithTotalViewsAndCreationDays(view, creationDays);
            for (Integer index : indexes) {
                boolean checkSold;
                if(isSold.equalsIgnoreCase("yes")){
                    checkSold = homePage.isItemSoldIn24h(index) || homePage.IsItemSold(index);
                }else {
                    checkSold = true;
                }
                if(checkSold){
                    homePage = new HomePage(driver);
                    homePage.selectItemByIndex(index);
                    homePage.sleepInSecond(3);
                    String itemTitle = homePage.getItemTitleByIndex(index);
                    String first30Chars = itemTitle.length() > 30 ? itemTitle.substring(0, 30) : itemTitle;
                    // Get all open window handles
                    BasePage.switchToTabByTitle(driver, first30Chars);
                    // Switch to the new tab
                    detailPage = new DetailPage(driver);
                    String folderName = String.valueOf(index);
                    detailPage.addDataToPropertiesFile(folderName,detailPage.getItemTitle(), detailPage.getTags(), detailPage.getRelatedSearches(), detailPage.getMoreRelatedSearches());
                    detailPage.downLoadImage(folderName);
                    driver.close();
                    driver.switchTo().window(mainWindow);
                    homePage.sleepInSecond(3);
                }
            }
            ExtentTestManager.endTest();

        }


        @AfterClass(alwaysRun = true)
        public void tearDown() {
            driver.quit();
        }
}

