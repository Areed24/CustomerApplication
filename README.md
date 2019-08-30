# CustomerApplicationService

### To build the project and run all tests:
```
./gradlew clean build
```

### Description:
The Customer Application Service collects information from the customer to apply for a policy. It does so in a 'wizard' like fashion, collecting information incrementally via RESTful endpoints and persisting the state. Once the application is completed, it will raise an event to the Policy Service to create a policy.