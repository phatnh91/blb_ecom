package pageUIs.components.simpleelement;

import org.openqa.selenium.WebElement;
import pageUIs.components.base.IBaseElement;
import pageUIs.components.base.ICanCreateInstance;

public interface ISimpleElement extends IBaseElement, ICanCreateInstance {
    static SimpleElement createInstance(WebElement element) {
        return new SimpleElement(element);
    }

}
