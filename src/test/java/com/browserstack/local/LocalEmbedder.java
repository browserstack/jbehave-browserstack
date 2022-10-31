package com.browserstack.local;

import java.text.SimpleDateFormat;

import com.browserstack.local.steps.LocalSteps;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.openqa.selenium.WebDriver;

public class LocalEmbedder extends Embedder {
  private WebDriver driver;

  public LocalEmbedder(WebDriver driver) {
    this.driver = driver;
  }

  @Override
  public EmbedderControls embedderControls() {
    return new EmbedderControls().doIgnoreFailureInStories(true).doIgnoreFailureInView(true);
  }

  @Override
  public Configuration configuration() {
    Class<? extends LocalEmbedder> embedderClass = this.getClass();
    return new MostUsefulConfiguration()
            .useStoryLoader(new LoadFromClasspath(embedderClass.getClassLoader()))
            .useStoryControls(new StoryControls().doResetStateBeforeScenario(false))
            .useStoryControls(new StoryControls().doSkipScenariosAfterFailure(false))
            .useParameterConverters(new ParameterConverters()
                    .addConverters(new DateConverter(new SimpleDateFormat("yyyy-MM-dd"))))
            .useParameterControls(new ParameterControls().useNameDelimiterLeft("<").useNameDelimiterRight(">"))
            .useStepPatternParser(new RegexPrefixCapturingPatternParser("$"))
            .useStepMonitor(new SilentStepMonitor())
            .useStoryReporterBuilder(new StoryReporterBuilder()
                    .withCodeLocation(CodeLocations.codeLocationFromClass(embedderClass))
                    .withDefaultFormats());
  }

  @Override
  public InjectableStepsFactory stepsFactory() {
    return new InstanceStepsFactory(configuration(), new LocalSteps(driver));
  }

}
