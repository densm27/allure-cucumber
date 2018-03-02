package ru.tinkoff.autotests.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.tinkoff.autotests.steps.BaseSteps;

import java.util.List;


/**
 * Created by Maria on 29.04.2017.
 */
public class MainPage extends BasePageObject{


    @FindBy(xpath = "//ol[contains(@class,'rgs-menu pull-left')]//li[contains(@class,'current')]")
    List<WebElement> menuItems;

    @FindBy(xpath = "//div[contains(@class,'grid rgs-main-menu')]//li[contains(@class,'line3-link')]")
    List<WebElement> menuInsurance;

    public MainPage(){
        PageFactory.initElements(BaseSteps.getDriver(), this);
    }

    public void selectMenuItem(String itemName){
        selectCollectionItem(itemName, menuItems);
    }

    public void selectInsuranceItem(String itemName){
        selectCollectionItem(itemName, menuInsurance);
    }
}


