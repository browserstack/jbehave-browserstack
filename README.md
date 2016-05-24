# JBehave-Browserstack

Execute [JBehave](https://github.com/jbehave/jbehave-core) scripts on BrowserStack.

## Usage

### Prerequisites

Java and Maven

### Clone the repo

`git clone https://github.com/browserstack/jbehave-browserstack.git`

### BrowserStack Authentication

Export the environment variables for the username and access key of your BrowserStack account.
These can be found on the automate accounts page on [BrowserStack](https://www.browserstack.com/accounts/automate)

`export BROWSERSTACK_USERNAME=<browserstack-username>`

`export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key>`

### Run the tests

 - To start a single test, run: `mvn clean install`
 - To start parallel tests, run: `TEST_TYPE=parallel mvn clean install`
 - To start local tests, run: `TEST_TYPE=local mvn clean install`
 - To start local tests in parallel, run: `TEST_TYPE=local,parallel mvn clean install`

------

#### How to specify the capabilities

The [Code Generator](https://www.browserstack.com/automate/node#setting-os-and-browser) can come in very handy when specifying the capabilities especially for mobile devices.
