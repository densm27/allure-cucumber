package ru.appline.autotests.steps;



import org.junit.Assert;
import ru.appline.autotests.pages.MainPage;
import io.qameta.allure.Step;

/**
 * Created by Maria on 29.04.2017.
 */
public class MainPageSteps {



    @Step("выбран пункт меню {0}")
    public void selectMenuItem(String menuItem){
        new MainPage().selectMenuItem(menuItem);
    }

    @Step("открыто меню сайта")
    public void openMenu(){
        new MainPage().menuButton.click();
    }

}
