package ru.appline.autotests.util;


import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestStepFinished;
import io.qameta.allure.Allure;
import io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.appline.autotests.steps.BaseSteps;

import java.io.ByteArrayInputStream;

/**
 * Created by 777 on 08.05.2017.
 */
public class AllureReporter extends AllureCucumber4Jvm {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        //Важен порядок - добавлять свой обработчик нужно до super, иначе не сработает (шаг будет уже завершен)
        publisher.registerHandlerFor(TestStepFinished.class,this::handleTestStepFinished);
        super.setEventPublisher(publisher);
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (!event.result.isOk(false)){
            Allure.getLifecycle().addAttachment("Скриншот в момент ошибки","image/png","png",new ByteArrayInputStream(((TakesScreenshot) BaseSteps.getDriver()).getScreenshotAs(OutputType.BYTES)));
        }
    }

}
