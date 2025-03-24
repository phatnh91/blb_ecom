package pageUIs.components.items;

import org.openqa.selenium.WebElement;
import pageUIs.components.base.IBaseElement;
import pageUIs.components.base.ICanCreateInstance;

public interface IItem extends IBaseElement, ICanCreateInstance {
    static IItem createInstance(WebElement element) {
        return new Item(element);
    }
}
