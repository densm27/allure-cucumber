package ru.appline.autotests.pages;



import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.appline.autotests.steps.BaseSteps;

/**
 * Created by Maria on 06.05.2017.
 */
public class HealthInsurancePage extends BasePageObject{


    @FindBy(xpath = "//*[@class='content-document-header']")
    public WebElement title;

    @FindBy(xpath = "//a[contains(.,'Добровольное медицинское страхование (ДМС)')]")
    public WebElement dmsLink;

    public HealthInsurancePage(){
        PageFactory.initElements(BaseSteps.getDriver(), this);
    }
}
