package pageUIs;

public class HomePageUI {

    public static final String MAIN_SIDE_MENU = "xpath=//button[contains(@class,'MuiButtonBase-root') and contains(string(),'%s')]";
    public static final String MAIN_SEARCH_TEXT_BOX = "xpath=//input[@data-id='search-query']";
    public static final String SEARCH_BUTTON = "xpath=//button[@data-id='gnav-search-submit-button']";
    public static final String IMAGE_LINK = "xpath=//a[contains(@class,listing-link)]//img";
    public static final String IMAGE_TITLE = "xpath=//a[contains(@class,listing-link)]//h3";
    public static final String DOWNLOAD_FOLDER = "xpath=//a[contains(@class,listing-link)]//h3";
    public static final String VIEWS_24H = "xpath=//span[contains(string(),'Views 24H')]/following-sibling::span";
    public static final String AVERAGE_VIEW = "xpath=//dt[text()='Views']/following-sibling::dd//div[contains(string(),'This is the estimated average daily view.')]/following-sibling::div";
    public static final String TOTAL_VIEW = "xpath=//dt[text()='Views']/following-sibling::dd//div[contains(string(),'Total views of the listing.')]/following-sibling::div";
    public static final String ITEM_IMAGE = "xpath=//div[contains(@class,'listing-card')]//img[@data-clg-id='WtImage']";
    public static final String ITEM_TITLE = "css=div.v2-listing-card__info h3";
    public static final String HEY_ETSY_CARD = "xpath=//div[@id='heyetsy-card-container']";
    public static final String HEY_ETSY_CARD_SOLD_IN_24H = "xpath=//div[@id='heyetsy-card-container']/div/div/div/div/div[contains(string(),'Sold in the Last 24 Hours')]/following-sibling::div//p";
    public static final String HEY_ETSY_CARD_ESTIMATED_SOLD = "xpath=//div[@id='heyetsy-card-container']/div/div/div/div/div[contains(string(),'Estimated Total Sales')]/following-sibling::div//p";
    public static final String HEY_ETSY_CARD_CREATED_DATE = "xpath=//div[@id='heyetsy-card-container']//div[contains(text(),'The listing was created.')]/following-sibling::div";

}
