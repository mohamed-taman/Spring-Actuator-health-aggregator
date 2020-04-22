# Spring Actuator μServices health aggregator
A spring boot actuator application, to demonstrate how to aggregate the reactive health of my microservices statuses under the central Edge or API Gateway Server.

I spent a fair amount of my time to understand how to migrate from old deprecated health aggregators APIs like `HealthAggregator`, `DefaultReactiveHealthIndicatorRegistry`, and `CompositeReactiveHealthIndicator` to new ones in the **latest spring framework version 5.2.5**, and the following application shows how to achieve that.



> **Note:** in the code example, both old way _(method with name `oldCoreServices()`)_ of aggregating health, and the new way _(method with name `coreServicesHealth()`)_; both are working when you run the application. You should use new method :) 

## How it works
Make sure that you use **Java SE 14**.

This application uses the latest version of **Spring boot version 2.3.0.M4** so there is no deprecated APIs. Beside I uses Reactive web using **WebFlux**.

1. Run application from your favorite IDE, or maven as the following:

   ```bash
   λ mvn spring-boot:run -Dspring-boot.run.jvmArguments="--enable-preview"
   ```

2. It will run and application listens to port `9090`.

3. Open your browser and paste this link or click it [http://localhost:9090/actuator/health](http://localhost:9090/actuator/health) and you should see something like this:

   ```json
   {
      "status":"DOWN", [[1]]
      "components":{
         "Core System Microservices":{ [[2]]
            "status":"DOWN",
            "components":{
               "Product Service":{
                  "status":"UP"
               },
               "Recommendation Service":{
                  "status":"DOWN",
                  "details":{
                     "error":"java.lang.IllegalStateException: Not working"
                  }
               },
               "Review Service":{
                  "status":"UP"
               }
            }
         },
         "Old API- Core Services":{ [[3]]
            "status":"DOWN",
            "details":{
               "Review Service":{
                  "status":"UP"
               },
               "Product Service":{
                  "status":"UP"
               },
               "Recommendation Service":{
                  "status":"DOWN",
                  "details":{
                     "error":"java.lang.IllegalStateException: Not working"
                  }
               }
            }
         },
         "diskSpace":{
            "status":"UP",
            "details":{
               "total":255382777856,
               "free":86618931200,
               "threshold":10485760,
               "exists":true
            }
         },
         "ping":{
            "status":"UP"
         }
      }
   }
   ```

### Explanation:
   1. [[1]]: General status of the service is **DOWN** because one of sub-services are down status.
   2. [[2]]: **New** implementation shows up in the health tree with name `"Core System Microservices"`.
   3. [[3]]: **Old** implementation also shows up in the tree with the name `"Old API- Core Services"`.

## The End
Happy coding :)

## License
Copyright (C) 2020 **Mohamed Taman**
Licensed under the MIT License.
