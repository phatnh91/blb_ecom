package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageUIs.DetailPageUI;
import pageUIs.HomePageUI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DetailPage extends BasePage {
    WebDriver driver;
    static String fileSeparator = File.separator;
    public static String BASE_FILE_LOCATION = System.getProperty("user.dir") + fileSeparator+"downloads"+fileSeparator;

    public DetailPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getTags(){
        List<String> tags = new ArrayList<>();
        for (WebElement tag : getListWebElement(driver, DetailPageUI.TAGS_NAME)){
            tags.add(tag.getText().trim());
        }
        return tags;
    }

    public String getItemTitle(){
        return getElementText(getWebElement(driver, DetailPageUI.ITEM_TITLE));
    }

    public void downLoadImage(String folderName) throws IOException {
        WebElement item = getWebElement(driver, DetailPageUI.MAIN_IMAGE_LINK);
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String imageLink = item.getAttribute("src");
            downloadImage(imageLink, BASE_FILE_LOCATION + folderName + fileSeparator +dateStr+".png");
    }

    public List<String> getRelatedSearches(){
        List<String> relatedTags = new ArrayList<>();
        for (WebElement tag : getListWebElement(driver, DetailPageUI.EXPLORE_RELATED_SEARCHES)){
            relatedTags.add(tag.getText().trim());
        }
        return relatedTags;
    }

    public List<String> getMoreRelatedSearches(){
        List<String> relatedTags = new ArrayList<>();
        for (WebElement tag : getListWebElement(driver, DetailPageUI.EXPLORE_MORE_RELATED_SEARCHES)){
            relatedTags.add(tag.getText().trim());
        }
        return relatedTags;
    }

}
