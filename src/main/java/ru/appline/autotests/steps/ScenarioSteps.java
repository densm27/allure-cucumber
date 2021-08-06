package ru.appline.autotests.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


/**
 * Created by m.baykova on 13.09.2017.
 */
public class ScenarioSteps {

    MainPageSteps mainPageSteps = new MainPageSteps();

    DMSSteps dmsSteps = new DMSSteps();
    HealthInsuranceSteps healthInsuranceSteps = new HealthInsuranceSteps();

    SendAppSteps sendAppSteps = new SendAppSteps();

    @When("^выбран пункт меню \"(.+)\"$")
    public void selectMenuItem(String menuName){
        mainPageSteps.selectMenuItem(menuName);
    }

    @When("^открыто меню$")
    public void openMenu(){
        mainPageSteps.openMenu();
    }

    @Then("^заголовок страницы - Страхование здоровья равен \"(.+)\"$")
    public void checkTitleHealthPage(String title){
        healthInsuranceSteps.checkPageTitle(title);
    }

    @When("^выполнено нажати на кнопку ДМС$")
    public void clickBtnDms(){
        healthInsuranceSteps.goToDMSPage();
    }

    @Then("^заголовок страницы - ДМС равен \"(.+)\"$")
    public void checkTitleDMSPage(String title){
        dmsSteps.checkPageTitle(title);
    }

    @When("^выполнено нажати на кнопку Отправить заявку$")
    public void clickBtnSendApp(){
        dmsSteps.goToSendAppPage();
    }

    @Then("^заголовок страницы - Заявка на ДМС равен \"(.+)\"$")
    public void checkTitleSendAppPage(String title){
        sendAppSteps.checkPageTitle(title);
    }


    @When("^заполняются поля:$")
    public void fillForm(DataTable fields){
        fields.<String,String>asMap(String.class, String.class)
                .forEach((field, value) -> sendAppSteps.fillField(field, value));

    }


    @Then("^значения полей равны:$")
    public void checkFillForm(DataTable fields){
        fields.<String,String>asMap(String.class, String.class)
                .forEach((field, value) -> sendAppSteps.checkFillField(field, value));
    }

    @Then("^в поле \"(.+)\" присутствует сообщение об ошибке \"(.+)\"$")
    public void checkErrorMessage(String field, String errorMessage){
        sendAppSteps.checkErrorMessageField(field, errorMessage);

    }

}
