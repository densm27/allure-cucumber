package ru.appline.autotests.util;


import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestStepFinished;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import ru.appline.autotests.steps.BaseSteps;

import java.io.ByteArrayInputStream;

/**
 * Created by 777 on 08.05.2017.
 */
public class AllureReporter extends io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        super.setEventPublisher(publisher);
        publisher.registerHandlerFor(TestStepFinished.class,this::handleTestStepFinished);
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (!event.result.isOk(false)){
            //Не работает из-за бага в Allure (предположительно https://github.com/allure-framework/allure-java/issues/467)
            Allure.getLifecycle().addAttachment("Скриншот в момент ошибки","image/png","png",new ByteArrayInputStream(((TakesScreenshot) BaseSteps.getDriver()).getScreenshotAs(OutputType.BYTES)));
        }
    }

}
