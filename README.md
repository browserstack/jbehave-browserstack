# jbehave-browserstack

[JBehave](https://github.com/jbehave/jbehave-core) Integration with BrowserStack.

## Setting up a sample test

- Clone the repo
- Install dependencies `mvn install`
- Update `*.conf.json` files inside the `src/test/resources/conf` directory with your BrowserStack Username and Access Key. (These can be found in the [settings](https://www.browserstack.com/accounts/settings) section on BrowserStack accounts page)

## Running the sample test

- To run single test, run `mvn test -P single`
- To run parallel tests, run `mvn test -P parallel`
- To run local tests, run `mvn test -P local`

## Integrating with your existing test suite

- Update ```pom.xml``` in your project.
- Copy src/test/java/com/browserstack/BrowserStackJBehaveRunner.java and src/test/resources/conf/* to your project.
- Adjust your configuration file as per ```*.conf.json```. Here is the full list of [BrowserStack configuration capabilities](https://www.browserstack.com/automate/capabilities)

## Notes

- In order to test on different set of browsers, check out our [code generator](https://www.browserstack.com/automate/python#setting-os-and-browser)
- You can export the environment variables for the Username and Access Key of your BrowserStack account using `export BROWSERSTACK_USERNAME=<browserstack-username> && export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>`
