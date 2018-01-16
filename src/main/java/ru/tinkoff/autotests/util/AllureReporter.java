package ru.tinkoff.autotests.util;

import cucumber.runtime.StepDefinitionMatch;
import gherkin.I18n;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;
import gherkin.lexer.Pa;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.*;
import io.qameta.allure.util.ResultsUtils;



import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static ru.tinkoff.autotests.steps.BaseSteps.getDriver;


/**
 * Created by 777 on 08.05.2017.
 */
public class AllureReporter implements Reporter, Formatter {
    private static final List<String> SCENARIO_OUTLINE_KEYWORDS = Collections.synchronizedList(new ArrayList<String>());

    private static final String FAILED = "failed";
    private static final String PASSED = "passed";
    private static final String SKIPPED = "skipped";


    private final Deque<Step> gherkinSteps = new LinkedList<>();
    private final AllureLifecycle lifecycle;
    private Feature currentFeature;
    private boolean isNullMatch = true;
    private Scenario currentScenario;


    public AllureReporter() {
        this.lifecycle = Allure.getLifecycle();
        final List<I18n> i18nList = I18n.getAll();

        i18nList.forEach(i18n -> SCENARIO_OUTLINE_KEYWORDS.addAll(i18n.keywords("scenario_outline")));
    }

    @Override
    public void feature(final Feature feature) {
        this.currentFeature = feature;
    }

    @Override
    public void before(final Match match, final Result result) {
        new StepUtils(currentFeature, currentScenario).fireFixtureStep(match, result, true);
    }

    @Override
    public void after(final Match match, final Result result) {
        new StepUtils(currentFeature, currentScenario).fireFixtureStep(match, result, false);
    }

    @Override
    public void startOfScenarioLifeCycle(final Scenario scenario) {
        this.currentScenario = scenario;

        final Deque<Tag> tags = new LinkedList<>();
        tags.addAll(scenario.getTags());

        if (SCENARIO_OUTLINE_KEYWORDS.contains(scenario.getKeyword())) {
            synchronized (gherkinSteps) {
                gherkinSteps.clear();
            }
        } else {
            tags.addAll(currentFeature.getTags());
        }

        final LabelBuilder labelBuilder = new LabelBuilder(currentFeature, scenario, tags);


        final TestResult result = new TestResult()
            .withUuid(scenario.getId())
            .withHistoryId(StepUtils.getHistoryId(scenario.getId()))
            .withName(scenario.getName())
            .withLabels(labelBuilder.getScenarioLabels())
            .withLinks(labelBuilder.getScenarioLinks());

        if (!currentFeature.getDescription().isEmpty()) {
            result.withDescription(currentFeature.getDescription());
        }

        lifecycle.scheduleTestCase(result);
        lifecycle.startTestCase(scenario.getId());

    }

    @Override
    public void step(final Step step) {
        synchronized (gherkinSteps) {
            gherkinSteps.add(step);
        }
    }

    @Override
    public void match(final Match match) {
        final StepUtils stepUtils = new StepUtils(currentFeature, currentScenario);
        if (match instanceof StepDefinitionMatch) {
            isNullMatch = false;
            final Step step = stepUtils.extractStep((StepDefinitionMatch) match);
            synchronized (gherkinSteps) {
                while (gherkinSteps.peek() != null && !stepUtils.isEqualSteps(step, gherkinSteps.peek())) {
                    stepUtils.fireCanceledStep(gherkinSteps.remove());
                }
                if (stepUtils.isEqualSteps(step, gherkinSteps.peek())) {
                    gherkinSteps.remove();
                }
            }
            List <Parameter> params = new ArrayList<>();
            if (step.getRows()!=null) {
                step.getRows().forEach(dataTableRow -> {
                        Parameter param = new Parameter();
                        param.setName(dataTableRow.getCells().get(0));
                        param.setValue(dataTableRow.getCells().get(1));
                        params.add(param);
                    }
                );
            }
            final StepResult stepResult = new StepResult();
            stepResult.withName(String.format("%s %s", step.getKeyword(), getStepName(step)))
                .withStart(System.currentTimeMillis())
                .withParameters(params);

            lifecycle.startStep(currentScenario.getId(), stepUtils.getStepUuid(step), stepResult);
            createDataTableAttachment(step.getRows());

        }
    }


    @Override
    public void result(final Result result) {
        if (!isNullMatch) {
            final StatusDetails statusDetails =
                ResultsUtils.getStatusDetails(result.getError()).orElse(new StatusDetails());
            final TagParser tagParser = new TagParser(currentFeature, currentScenario);
            statusDetails
                .withFlaky(tagParser.isFlaky())
                .withMuted(tagParser.isMuted())
                .withKnown(tagParser.isKnown());

            switch (result.getStatus()) {
                case FAILED:
                    lifecycle.updateStep(stepResult -> stepResult.withStatus(Status.FAILED));
                    lifecycle.updateTestCase(currentScenario.getId(), scenarioResult ->
                        scenarioResult.withStatus(Status.FAILED)
                            .withStatusDetails(statusDetails));
                    lifecycle.stopStep();
                    break;
                case SKIPPED:
                    lifecycle.updateStep(stepResult -> stepResult.withStatus(Status.SKIPPED));
                    lifecycle.stopStep();
                    break;
                case PASSED:
                    lifecycle.updateStep(stepResult -> stepResult.withStatus(Status.PASSED));
                    lifecycle.stopStep();
                    lifecycle.updateTestCase(currentScenario.getId(), scenarioResult ->
                        scenarioResult.withStatus(Status.PASSED)
                            .withStatusDetails(statusDetails));
                    break;
                default:
                    break;
            }
            isNullMatch = true;
        }
    }

    @Override
    public void endOfScenarioLifeCycle(final Scenario scenario) {
        final StepUtils stepUtils = new StepUtils(currentFeature, currentScenario);
        synchronized (gherkinSteps) {
            while (gherkinSteps.peek() != null) {
                stepUtils.fireCanceledStep(gherkinSteps.remove());
            }
        }
        lifecycle.stopTestCase(scenario.getId());
        lifecycle.writeTestCase(scenario.getId());
    }

    public String getStepName(final Step step) {
        return step.getName();
    }

    private void createDataTableAttachment(final List<DataTableRow> dataTableRows) {
        final StringBuilder dataTableCsv = new StringBuilder();

        if (dataTableRows != null && !dataTableRows.isEmpty()) {
            dataTableRows.forEach(dataTableRow -> {
                dataTableCsv.append(dataTableRow.getCells().stream().collect(Collectors.joining("\t")));
                dataTableCsv.append('\n');
            });

            final String attachmentSource = lifecycle
                .prepareAttachment("Data table", "text/tab-separated-values", "csv");
            lifecycle.writeAttachment(attachmentSource,
                new ByteArrayInputStream(dataTableCsv.toString().getBytes(Charset.forName("UTF-8"))));
        }
    }

    @Override
    public void embedding(final String string, final byte[] bytes) {
        //Nothing to do with Allure
    }

    @Override
    public void write(final String string) {
        //Nothing to do with Allure
    }

    @Override
    public void syntaxError(final String state, final String event,
                            final List<String> legalEvents, final String uri, final Integer line) {
        //Nothing to do with Allure
    }

    @Override
    public void uri(final String uri) {
        //Nothing to do with Allure
    }

    @Override
    public void scenarioOutline(final ScenarioOutline so) {
        //Nothing to do with Allure
    }

    @Override
    public void examples(final Examples exmpls) {
        //Nothing to do with Allure
    }


    @Override
    public void background(final Background b) {
        //Nothing to do with Allure
    }

    @Override
    public void scenario(final Scenario scnr) {
        //Nothing to do with Allure
    }

    @Override
    public void done() {
        //Nothing to do with Allure
    }

    @Override
    public void close() {
        //Nothing to do with Allure
    }

    @Override
    public void eof() {
        //Nothing to do with Allure

    }
}



