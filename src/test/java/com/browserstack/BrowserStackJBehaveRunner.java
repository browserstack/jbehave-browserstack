package com.browserstack;
import com.browserstack.local.Local;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.lang.reflect.Constructor;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import org.jbehave.core.embedder.Embedder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


@RunWith(Parameterized.class)
public class BrowserStackJBehaveRunner {

    public WebDriver driver;
    private Local l;
    private static Object lock = new Object();
    private static Integer parallels = 0;

    private static JSONObject config;

    @Parameter(value = 0)
    public int taskID;

    @Parameters
    public static Collection<Object[]> data() throws Exception {
        List<Object[]> taskIDs = new ArrayList<Object[]>();
        if(System.getProperty("config") != null) {
            JSONParser parser = new JSONParser();
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + System.getProperty("config")));
            int envs = ((JSONArray)config.get("environments")).size();

            for(int i=0; i<envs; i++) {
              taskIDs.add(new Object[] {i});
            }
        }

        return taskIDs;
    }

    @Before
    public void setUp() throws Exception {
        JSONArray envs = (JSONArray) config.get("environments");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserstack.source","jbehave:sample-master:v1.0");

        Map<String, String> envCapabilities = (Map<String, String>) envs.get(taskID);
        Iterator it = envCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
        }
        
        Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
        it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(capabilities.getCapability(pair.getKey().toString()) == null){
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
        }

        String username = System.getenv("BROWSERSTACK_USERNAME");
        if(username == null) {
            username = (String) config.get("user");
        }

        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if(accessKey == null) {
            accessKey = (String) config.get("key");
        }

        synchronized (lock) {
          parallels++;
          if ((l == null || !l.isRunning()) && capabilities.getCapability("browserstack.local") != null && capabilities.getCapability("browserstack.local") == "true") {
              l = new Local();
              Map<String, String> options = new HashMap<>();
              options.put("key", accessKey);
              try {
                  l.start(options);
              } catch (Exception e){
                  e.printStackTrace();
              }
          }
      }

        driver = new RemoteWebDriver(new URL("http://"+username+":"+accessKey+"@"+config.get("server")+"/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        synchronized (lock){
            parallels--;
            driver.quit();
            if (l != null && parallels == 0) l.stop();
        }
    }

    @Test
    public void runStories() throws Exception {
        Class<?> c = Class.forName(System.getProperty("embedder"));
        Constructor<?> cons = c.getConstructor(WebDriver.class);
        Embedder storyEmbedder = (Embedder) cons.newInstance(driver);

        List<String> storyPaths = Arrays.asList(System.getProperty("stories"));
        storyEmbedder.runStoriesAsPaths(storyPaths);
    }
}
