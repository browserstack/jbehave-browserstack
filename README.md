# jbehave-browserstack
[JBehave](https://github.com/jbehave/jbehave-core) Integration with BrowserStack.

![BrowserStack Logo](https://d98b8t1nnulk5.cloudfront.net/production/images/layout/logo-header.png?1469004780)

![JBehave Logo](http://jbehave.org/images/jbehave-logo.png)


## Using Maven
### Setting up a sample test

* Clone the repo
* Install dependencies `mvn install`
* Update `browserstack.yml` file inside the root directory with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings)

### Running your tests
* To run a sample parallel test, run `mvn test -P sample-test`
* To run local tests, update your `browserstack.yml` file with `browserstackLocal: true` and run `mvn test -P sample-local-test`

 Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

## Using Gradle

### Prerequisites
- If using Gradle, Java v9+ is required.

### Setting up a sample test

* Clone the repo
* Update `browserstack.yml` file inside the root directory with your [BrowserStack Username and Access Key](https://www.browserstack.com/accounts/settings)

### Running your tests
* To run a sample parallel test, run `gradle sampleTest`
* To run local tests, update your `browserstack.yml` file with `browserstackLocal: true` and run `gradle sampleLocalTest`

Understand how many parallel sessions you need by using our [Parallel Test Calculator](https://www.browserstack.com/automate/parallel-calculator?ref=github)

## Notes
* You can view your test results on the [BrowserStack Automate dashboard](https://www.browserstack.com/automate)
* To test on a different set of browsers, check out our [platform configurator](https://www.browserstack.com/automate/java#setting-os-and-browser)
* You can export the environment variables for the Username and Access Key of your BrowserStack account
  
  ```
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>
  ```
