package ru.tinkoff.autotests.steps;


import io.qameta.allure.Step;
import ru.tinkoff.autotests.pages.SendAppPage;


import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * Created by Maria on 06.05.2017.
 */
public class SendAppSteps {

    @Step("поле {field} заполняется значением {value}")
    public void fillField(String field, String value){
        new SendAppPage().fillField(field, value);
    }

    @Step("поле {field} заполнено значением {value}")
    public void checkFillField(String field, String value){
        String actual = new SendAppPage().getFillField(field);
        assertTrue(String.format("Значение поля [%s] равно [%s]. Ожидалось - [%s]", field, actual, value),
                actual.equals(value));
    }

    @Step("в поле {field} присутствует сообщение об ошибке {value}")
    public void checkErrorMessageField(String field, String value){
        new SendAppPage().checkFieldErrorMessage(field, value);
    }

    @Step("заголовок страницы - Отправить заявку равен {expectedTitle}")
    public void checkPageTitle(String expectedTitle){
        String actualTitle = new SendAppPage().title.getText();
        assertTrue(String.format("Заголовок равен [%s]. Ожидалось - [%s]",
                actualTitle, expectedTitle), actualTitle.contains(expectedTitle));
    }

    @Step("заполняются поля")
    public void fillFields(HashMap<String, String> fields){
        fields.forEach(this::fillField);
    }

    @Step("поля заполнены верно")
    public void checkFillFields(HashMap<String, String> fields){
        fields.forEach(this::checkFillField);
    }

    @Step
    public String getErrorMessageField(String field) {
        return new SendAppPage().getFieldErrorMessage(field);
    }
}
