## Running the Application
Since this applications uses gradle as its build tool, running locally is very easy. Just go to the root folder of the application and executes the following command:
```
./gradlew bootRun
```
You can (and should) run the unit tests of the application locally by executing the following:
```
./gradlew check
```

## Architecture
This web server has been written in java 8, using web development framework Spring Boot 2.0.0.X. It starts an embedded container (Reactor-Netty) when started as a jar file (simply running java -jar appname.jar).
Currently it rely heavily on 3rd apis through http calls. Given that its operations are IO bound, we have opted to choose an asychronous model to accomplish its goals. The façade and every internal operations are done in a non-blocking asynchronous fashion. We expect, as such, to achieve great efficience on computational resources and, as a side gain, to promote well written and reactive code.

### Internal Components (Packages)
In terms of its structure, the system is divided in the following packages:
* **api**:
  It holds the code that specifies how requests should be routed and responded to.
  * **exception**:
  Contains classes that translates exceptions raised by the underlying code to an appropriate HTTP response
  * **handlers**:
  Contains classes that transforms the given message to the domain of the application and passes it to other classes that has more knowledge about the business of the given action.
  * **routers**:
  Contains classes that know which handler may be called for given url-path and http-method (and headers)
  * **validations**:
  Contains classes that perform data validation on the given message (which may be gotten from the json body or from the query parameters)

* **configs**:
  Contains classes that configure miscellaneous, mostly needed by Spring (or that we want to have it managed by Spring).

* **domains**:
  Classes that model the domain of our application

* **gateways**:
  Classes that perform some 3rd party integration (database, api-calling, message-queue etc)
  * **http**:
  Implementation of the gateways that performs its actions through api-calling

* **models**:
  Classes that model all input-output of 3rd party api calls

* **monitors**:
  Contains helpers classes that wraps tools that monitors the application (datadog, newrelic etc)

* **services**:
  Contains the classes that know the most about the business logic and do all the needed wiring to accomplish some given action.


  

