## Project Todo-list
This repository includes a small project for using Spring boot.
## Tech/framework used
- Java 8
- Spring framework
- MySQL
- JUnit test
## IDE
- Apache Netbean IDE 11.1
## Build
```bash
mvn clean install
```
## Run
```bash
mvn spring-boot:run
```
## API method
| Method | url |Description |
| ------------- | ------------- | ------------- | 
| GET | url/tasks|Find all tasks|
| GET | url/tasks/{id}|Find task by id|
| POST| url/tasks|Create a task|
| PUT| url/tasks|Update a task|
| PATCH| url/tasks|Update field isDone only|
| DELETE| url/task/{id}|Delete a task with id|

Thank you for your mentorship and support Thang Le