package ru.tinkoff.autotests.steps;



import io.qameta.allure.Step;
import ru.tinkoff.autotests.pages.DMSPage;


import static org.junit.Assert.assertTrue;

/**
 * Created by Maria on 06.05.2017.
 */
public class DMSSteps {

    @Step("выполнено нажатие на Отправить заявку")
    public void goToSendAppPage(){
        new DMSPage().sendAppBtn.click();
    }


    @Step("заголовок страницы - ДМС равен {expectedTitle}")
    public void checkPageTitle(String expectedTitle){
        String actualTitle = new DMSPage().title.getText();
        assertTrue(String.format("Заголовок равен [%s]. Ожидалось - [%s]",
                actualTitle, expectedTitle), actualTitle.contains(expectedTitle));
    }

}
