package com.browserstack.runner;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collection;

import org.jbehave.core.embedder.Embedder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.browserstack.local.Local;

@RunWith(Parameterized.class)
public class StoryRunner {

  public WebDriver driver;
  public String os;
  public String osVersion;
  public String browser;
  public String browserVersion;

  private static Local bsLocal;
  private static String TEST_TYPE = System.getenv("TEST_TYPE");
  private static String BS_USER = System.getenv("BROWSERSTACK_USERNAME");
  private static String BS_AUTH_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");

  public StoryRunner(String os, String osVersion, String browser,
      String browserVersion) {
    this.os = os;
    this.osVersion = osVersion;
    this.browser = browser;
    this.browserVersion = browserVersion;
  }

  @BeforeClass
  public static void setupLocalBeforeClass() throws Exception {
    if(TEST_TYPE != null && TEST_TYPE.toLowerCase().indexOf("local") > -1) {
      bsLocal = new Local();
      HashMap<String, String> bsLocalArgs = new HashMap<String, String>();
      bsLocalArgs.put("key", BS_AUTH_KEY);
      bsLocalArgs.put("forcelocal", "");
      bsLocal.start(bsLocalArgs);
    }
  }
  @AfterClass
  public static void stopLocalAfterClass() throws Exception {
    if(bsLocal != null) {
      bsLocal.stop();
    }
  }

  @Before
  public void setUp() throws Exception {
    String url = "http://"+BS_USER+":"+BS_AUTH_KEY+"@hub.browserstack.com/wd/hub/";
    DesiredCapabilities dc = new DesiredCapabilities();
    dc.setCapability("os", os);
    dc.setCapability("os_version", osVersion);
    dc.setCapability("browser", browser);
    dc.setCapability("browser_version", browserVersion);
    dc.setCapability("browserstack.debug", "true");
    dc.setCapability("build", "Sample JBehave Tests");
    dc.setCapability("name", "Sample " + ( TEST_TYPE != null ? TEST_TYPE : "" ) + " JBehave Tests");
    if(TEST_TYPE != null && TEST_TYPE.toLowerCase().indexOf("local") > -1) {
      dc.setCapability("browserstack.local", "true");
    }
    try {
      System.out.println("Creating driver using key: "+BS_AUTH_KEY);
      driver = new RemoteWebDriver(new URL(url), dc);
    } catch (MalformedURLException e) {
      System.err.println("Exception in url: " + e.getMessage());
    }
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
  }

  @Parameters
  public static Collection<Object[]> data() {
    Object[][] data;
    if(System.getenv("TEST_TYPE") != null &&
        System.getenv("TEST_TYPE").toLowerCase().indexOf("parallel") > -1) {
      data = new Object[][] {
        { "OS X", "El Capitan", "firefox", "46" },
          { "Windows", "10", "chrome", "49" } };
    } else {
      data = new Object[][] {
        { "OS X", "El Capitan", "firefox", "46" } };
    }
    return Arrays.asList(data);
  }

  @Test
  public void runStories() throws Exception {
    Embedder storyEmbedder = new StoryEmbedder(driver);
    List<String> storyPaths = Arrays.asList("features/search/google.feature");
    storyEmbedder.runStoriesAsPaths(storyPaths);
  }

}
