package ru.tinkoff.autotests.pages;


import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import ru.tinkoff.autotests.steps.BaseSteps;

import java.util.List;


/**
 * Created by Maria on 06.05.2017.
 */
public class BasePageObject  {

    public BasePageObject() {
        PageFactory.initElements(BaseSteps.getDriver(), this);
    }

    public void fillField(WebElement field, String value){
        field.clear();
        field.sendKeys(value);
    }

    public void selectCollectionItem(String itemName, List<WebElement> collection){
        for (WebElement item : collection ){
            if (item.getText().equalsIgnoreCase(itemName)){
                item.click();
                return;
            }
        }
        Assert.fail("Не найден элмент коллеции - " + itemName);
    }

}
