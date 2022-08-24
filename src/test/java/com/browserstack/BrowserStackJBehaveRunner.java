package com.browserstack;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jbehave.core.embedder.Embedder;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.yaml.snakeyaml.Yaml;


@RunWith(Parameterized.class)
public class BrowserStackJBehaveRunner {

    public WebDriver driver;
    private static Map<String, Object> browserstackYamlMap;
    @Parameter(value = 0)
    public int taskID;
    private String userName;
    private String accessKey;
    public static final String USER_DIR = "user.dir";

    @Parameters
    public static Iterable<? extends Object> data() {
        List<Integer> taskIDs = new ArrayList<>();
        if (browserstackYamlMap == null) {
            File file = new File(getUserDir() + "/browserstack.yml");
            browserstackYamlMap = convertYamlFileToMap(file, new HashMap<>());
        }
        if (browserstackYamlMap != null) {
            ArrayList<LinkedHashMap<String, Object>> browserStackPlatforms = (ArrayList<LinkedHashMap<String, Object>>) browserstackYamlMap.get("platforms");
            int envs = browserStackPlatforms.size();
            for (int i = 0; i < envs; i++) {
                taskIDs.add(i);
            }
        }
        return taskIDs;
    }

    @Before
    public void setUp() throws Exception {
        if (browserstackYamlMap == null) {
            File file = new File(getUserDir() + "/browserstack.yml");
            browserstackYamlMap = convertYamlFileToMap(file, new HashMap<>());
        }
        Thread.currentThread().setName(this.getClass().getName() + "@" + taskID);
        MutableCapabilities capabilities = new MutableCapabilities();
        HashMap<String, Object> bStackOptions = new HashMap<>();
        browserstackYamlMap.forEach((key, value) -> {
            if (key.equalsIgnoreCase("userName")) {
                userName = System.getenv("BROWSERSTACK_USERNAME") != null ? System.getenv("BROWSERSTACK_USERNAME") : (String) value;
            } else if (key.equalsIgnoreCase("accessKey")) {
                accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY") != null ? System.getenv("BROWSERSTACK_ACCESS_KEY") : (String) value;
            } else if (key.equalsIgnoreCase("platforms")) {
                ArrayList<LinkedHashMap<String, Object>> platformsArrayList = (ArrayList<LinkedHashMap<String, Object>>) value;
                platformsArrayList.get(taskID).forEach((k, v) -> {
                    if (k.equalsIgnoreCase("browserName") || k.equalsIgnoreCase("browserVersion")) {
                        capabilities.setCapability(k, v.toString());
                    } else {
                        bStackOptions.put(k, v.toString());
                    }
                });
            } else if (key.equalsIgnoreCase("browserstackLocal") ||
                    key.equalsIgnoreCase("local")) {
                bStackOptions.put("local", value);
            } else if (key.equalsIgnoreCase("browserStackLocalOptions") ||
                    key.equalsIgnoreCase("localOptions")) {
                if (value instanceof LinkedHashMap) {
                    ArrayList<LinkedHashMap<String, Object>> localOptionsArrayList = (ArrayList<LinkedHashMap<String, Object>>) value;
                    localOptionsArrayList.forEach(localOptionsMap -> {
                        if (((Boolean) browserstackYamlMap.get("browserstackLocal") || (Boolean) browserstackYamlMap.get("local"))
                                && localOptionsMap.containsKey("localIdentifier")) {
                            bStackOptions.put("localIdentifier", localOptionsMap.get("localIdentifier").toString());
                        }
                    });
                } else if (value instanceof HashMap) {
                    HashMap<String, ?> localOptionsHashMap = (HashMap<String, ?>) new ObjectMapper().convertValue(value, HashMap.class);
                    if (((Boolean) browserstackYamlMap.get("browserstackLocal") || (Boolean) browserstackYamlMap.get("local"))
                            && localOptionsHashMap.containsKey("localIdentifier")) {
                        bStackOptions.put("localIdentifier", localOptionsHashMap.get("localIdentifier").toString());
                    }
                }
            } else {
                bStackOptions.put(key, value);
            }
        });
        capabilities.setCapability("bstack:options", bStackOptions);
        this.driver = new RemoteWebDriver(
                new URL("https://" + userName + ":" + accessKey + "@hub.browserstack.com/wd/hub"), capabilities);
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
