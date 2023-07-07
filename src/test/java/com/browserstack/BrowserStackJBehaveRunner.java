package com.browserstack;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.lang.reflect.Constructor;
import org.jbehave.core.embedder.Embedder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.yaml.snakeyaml.Yaml;

public class BrowserStackJBehaveRunner {

    public WebDriver driver;
    private static Map<String, Object> browserstackYamlMap;
    private String userName;
    private String accessKey;
    public static final String USER_DIR = "user.dir";

    @Before
    public void setUp() throws Exception {
        if (browserstackYamlMap == null) {
            File file = new File(getUserDir() + "/browserstack.yml");
            browserstackYamlMap = convertYamlFileToMap(file, new HashMap<>());
        }
        userName = System.getenv("BROWSERSTACK_USERNAME") != null ? System.getenv("BROWSERSTACK_USERNAME") : (String) browserstackYamlMap.get("userName");
        accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY") != null ? System.getenv("BROWSERSTACK_ACCESS_KEY") : (String) browserstackYamlMap.get("accessKey");
        MutableCapabilities capabilities = new MutableCapabilities();
        HashMap<String, Object> bStackOptions = new HashMap<>();
        bStackOptions.put("source", "jbehave:sample-sdk:v1.0");
        bStackOptions.put("sessionName", "BStack Sample JBehave");
        capabilities.setCapability("bstack:options", bStackOptions);
        try {
            this.driver = new RemoteWebDriver(
                    new URL("https://" + userName + ":" + accessKey + "@hub.browserstack.com/wd/hub"), capabilities);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    private static String getUserDir() {
        return System.getProperty(USER_DIR);
    }

    private static Map<String, Object> convertYamlFileToMap(File yamlFile, Map<String, Object> map) {
        try {
            InputStream inputStream = Files.newInputStream(yamlFile.toPath());
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(inputStream);
            map.putAll(config);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Malformed browserstack.yml file - %s.", e));
        }
        return map;
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
