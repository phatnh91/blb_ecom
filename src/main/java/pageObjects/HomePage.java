package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageUIs.HomePageUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class HomePage extends BasePage {
    WebDriver driver;
    static String fileSeparator = File.separator;
    public static String BASE_FILE_LOCATION = System.getProperty("user.dir") + fileSeparator+"downloads"+fileSeparator;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }
    public void sendTextToSearchTextbox(String value){
        waitForElementVisible(driver, HomePageUI.MAIN_SEARCH_TEXT_BOX);
        sendKeysToElement(driver, HomePageUI.MAIN_SEARCH_TEXT_BOX, value);
    }

    public void clickToSearchButton(){
        waitForElementClickable(driver, HomePageUI.SEARCH_BUTTON);
        clickToElement(driver, HomePageUI.SEARCH_BUTTON);
        waitForPageToLoad(driver);
    }

    public List<String> getListItemTitles(){
        List<String> titles = new ArrayList<>();
        List<WebElement> elements = getListWebElement(driver, HomePageUI.IMAGE_TITLE);
        for(WebElement element : elements){
            titles.add(getElementText(element));
        }
        return titles;
    }

    public int getIndexItemByTitle(String title){
        List<String> titles = getListItemTitles();
        int index = 0;
        for(int i=0;i<titles.size();i++){
            if(titles.get(0).equalsIgnoreCase(title)){
                index = i;
                break;
            }
        }
        return index;
    }

    public void downLoadImage() throws IOException {
        List<WebElement> items = getListWebElement(driver, HomePageUI.IMAGE_LINK);
        List<String> titles = getListItemTitles();
        for(int i=0;i<items.size();i++){
            String imageLink = items.get(i).getAttribute("src");
            String name = titles.get(i).replaceAll(", ","_");
            downloadImage(imageLink, BASE_FILE_LOCATION +name+".png");
        }
    }

    public void selectItemByIndex(int index){
        List<WebElement> items = getListWebElement(driver, HomePageUI.ITEM_IMAGE);
        WebElement item = items.get(index);
        sleepInSecond(1);
        clickToElementByJS(driver, item);
        waitForPageToLoad(driver);
        waitPageContentLoaded(driver);
    }

    public String getItemTitleByIndex(int index){
        List<WebElement> items = getListWebElement(driver, HomePageUI.ITEM_TITLE);
        return items.get(index).getText();
    }

    public List<Integer> getItemIndexesWithTotalViewsAndCreationDays(int views, long creationDays){
        List<WebElement> averageViews = getListWebElement(driver, HomePageUI.TOTAL_VIEW);
        List<Integer> indexes = new ArrayList<>();
        for(int i=0;i<averageViews.size();i++){
//            String viewCount = averageViews.get(i).getText().replaceAll("\\s*\\(Avg\\)","").trim();
            String viewCount = averageViews.get(i).getText().replaceAll(",","").trim();
            if(viewCount.equalsIgnoreCase("-")){
                continue;
            }
            int view = Integer.parseInt(viewCount.replaceAll(",",""));
            if(view>=views && calculateDaysSinceCreation(i)<=creationDays){
                indexes.add(i);
            }
            if(indexes.size()==10){
                break;
            }
        }
        return indexes;
    }

    public boolean isItemSoldIn24h(int index){
        List<WebElement> elements = getListWebElement(driver, HomePageUI.HEY_ETSY_CARD_SOLD_IN_24H);
        return !elements.get(index).getText().contains("0");
    }

    public boolean IsItemSold(int index){
        List<WebElement> elements = getListWebElement(driver, HomePageUI.HEY_ETSY_CARD_ESTIMATED_SOLD);
        String totalSold = elements.get(index).getText().split("\\+")[0];
        return Integer.parseInt(totalSold)>0;
    }

    public long calculateDaysSinceCreation(int index) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Parse the creation date
        LocalDate creationDateParsed = LocalDate.parse(getCreationDate(index), formatter);

        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the number of days between the two dates
        return ChronoUnit.DAYS.between(creationDateParsed, currentDate);
    }

    public String getCreationDate(int index){
        List<WebElement> elements = getListWebElement(driver, HomePageUI.HEY_ETSY_CARD_CREATED_DATE);
        String input = getElementText(elements.get(index));
        // Define the regular expression for matching the date format (dd/MM/yyyy)
        Pattern pattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(input);

        // If a match is found, return the date
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Date not found";
        }
    }

}
