package ru.appline.autotests.steps;

import io.qameta.allure.Step;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.appline.autotests.pages.HealthInsurancePage;

import static org.junit.Assert.assertTrue;

/**
 * Created by Maria on 06.05.2017.
 */
public class HealthInsuranceSteps {

    @Step("выполнено нажатие на ДМС")
    public void goToDMSPage(){
        new HealthInsurancePage().dmsLink.click();
    }


    @Step("заголовок страницы равен {0}")
    public void checkPageTitle(String expectedTitle){
        WebDriverWait wait = new WebDriverWait(BaseSteps.getDriver(),5);
        String actualTitle =wait.until(ExpectedConditions.visibilityOf(new HealthInsurancePage().title)).getText();
        assertTrue(String.format("Заголовок равен [%s]. Ожидалось - [%s]",
                actualTitle, expectedTitle), actualTitle.contains(expectedTitle));
    }

}
