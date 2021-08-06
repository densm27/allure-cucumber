
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/resources/"}, glue = {"ru.appline.autotests"},
        plugin = {
            "ru.appline.autotests.util.AllureReporter",
        }
)
public class CucumberRunner {

}
