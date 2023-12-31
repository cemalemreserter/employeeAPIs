#How to install

Before you run the application creating the db and the tables is required. 

To achieve this 
1. Deploy and Start the MySQL Container,  Connect with the Docker MySQL Container 
2. Update "application.properties" and application-docker.properties files accordingly
   with its URL, username and password (switch on/off spring.profiles.active=docker)
3. Create a database in mysql server named employeedb with the command `create database employeedb`
4. Switch to this database with the command `use employeedb`
5. And then run the scripts in "schema.sql" file.
6. Kafka configuration steps: 
7. Start ZooKeeper by running the following command:
   cd C:\kafka_2.13-3.5.0\bin\windows
   'zookeeper-server-start.bat ../../config/zookeeper.properties'
8. Start the Kafka broker by running the following command:
  'kafka-server-start.bat ../../config/server.properties'
   By default, Kafka will start using the configuration file config/server.properties
9. With kafka topic named :   "topic-employeeEvent" we are able to produce and consume the employee events based on API calls properly
   Once an event published/produced into our kafka topic, Event Consuming with @KafkaListener is also handled including slf4j-logging as well.

#How to use

* Postman: You can import "EmployeeAPI.postman_collection.json" file in Postman to see sample endpoint calls.  
* JWT Auth : The first thing you should do is calling the register endpoint with username and password to get JWT authentication token.  
Once registered the token can also be obtained by calling the login endpoint  
This token should be used in all subsequent  requests from there on.  

* Swagger: To see request and response details go to `http://localhost:9000/swagger-ui/`  
ATTENTION: You should disable access to this endpoint in PROD environments. 

* All unit and integration tests pass by covering all scenarios (~13 tests).

* Why A relational database selected is It may be required to make additional relational transactions for instance; employee contacts, actions, addresses and so on for the long run,
and there may not be needed to handle a big data considering employees data size can not become so big in terms of using a nosql db to be scaled not that much,
In order to be able to make use of auto generated UUID and uniqueness constraints..