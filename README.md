
# Code On Cloud

Code On Cloud is a powerful backend platform designed to provide features like Coding Problem setting, Creating coding contests and custom input based code execution feature which can be used in contest or normal question submission while in practice.

## Features

- Code Execution Server: Deployed on Amazon EC2 for reliable code execution.
- Authentication: Implemented JWT for secure and stateless user authentication.
- Java Technologies: Built with Spring Boot, Spring Security, Spring Data MongoDB, Redis, and Spring AMQP for a robust backend.
- Database : Utilized MongoDB for efficient data storage and retrieval.
- Messaging Queue: Integrated RabbitMQ to handle asynchronous code execution requests in a queue, for system efficiency.
- Real-time Leaderboards: Applied scheduling to dynamically update contest leaderboards during running contests.
- Performance Optimization: Used in-memory caching (Redis) to reduce response latency by nearly 65% for frequent requests.
- Pagination and Sorting: Implemented pagination and sorting to efficiently handle and display large sets of data.
- Global Error Handling: Comprehensive error handling to ensure smooth user experience and easy debugging.


## Tech Stack


- **Authentication:** JWT (JSON Web Token)

- **Database:** MongoDB, Redis

- **Messaging Queue:** RabbitMQ

- **Server:** Amazon EC2

- **API Documentation:** Swagger with Springfox

- **API Testing:** Postman

- **JSON Processing:** Jackson

- **Scheduling:** Spring Scheduler

- **Caching:** In-Memory Cache (Redis)

- **Pagination and Sorting:** Spring Data JPA


## Installation

**Prerequisites:**  
- Java 8 or higher
- Maven or Gradle
- MongoDB instance
- Redis instance
- RabbitMQ instance
- Amazon EC2 account

**Setup**
- Clone the repository:
```bash
  git clone https://github.com/manas-0407/Code-On-Cloud.git
  cd code-on-cloud
```
- Configure the application:
    
```bash
  Update the "application.properties" file with your MongoDB, Redis, RabbitMQ configurations.

  # Mongo Connection -

    spring.application.name= <name>
    spring.data.mongodb.uri= <your_connection_uri>
    spring.data.mongodb.database= <your_db_name>


  # Tomcat Port -

    server.port=8081

  # Redis Connection -

    spring.data.redis.host= <redis_host>
    spring.data.redis.port= <redis_port>
    spring.data.redis.password= <your_connection_password>

  # Rabbit MQ Connection(On code execution remote server) -

    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest

    rabbitmq.queue.name=myQueue
    rabbitmq.reply.queue.name=myQueue2
    rabbitmq.exchange.name=myExchange
    rabbitmq.routing.key=myRoutingKey
    rabbitmq.reply.routing.key=myRoutingKey

```

- Build the project::
    
```bash
  mvn clean install
```

- Run the application:
    
```bash
  mvn spring-boot:run
```
## API Reference

#### **Endpoints under /contest :**
- Endpoint to create a contest by an authenticated user
        
```http
      HTTP method : POST 
      Endpoint : /create
      HTTP request parameters : Request Body
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `Title` | `string` | Contest title |
| `start` | `date` | Start date time of contest |
| `end` | `date` | End date time of contest |
| `question_ids` | `array` | List of questions to be added in contest |


   - Endpoint to fetch contest page if under time 
				else returns relevant response regarding contest status(Started, Finished)
        
```http
      HTTP method : GET 
      Endpoint : /{contest_id}
      HTTP request parameters : Path Variable
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `contest_id` | `string` | Id of contest to be fetched |

-  Endpoint to delete contest if not started yet and requested only by it's author. 
        
```http
      HTTP method : DELETE 
      Endpoint : /{contest_id}/delete
      HTTP request parameters : Path Variable
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `contest_id` | `string` | Id of contest to be fetched |


- Return all question of contest if under time. 
        
```http
      HTTP method : GET 
      Endpoint : /{contest_id}/question
      HTTP request parameters : Path Variable
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `contest_id` | `string` | Id of contest to be fetched |

- Return question with given question id of contest if contest is running.
        
```http
      HTTP method : GET 
      Endpoint : /{contest_id}/question/{question_id}
      HTTP request parameters : Path Variable
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `contest_id` | `string` | Id of contest to be fetched |
| `question_id` | `string` | Id of question to be fetched |

- Endpoint to submit and execute solution to a particular question from the contest, done on a remote server.
        
```http
      HTTP method : POST 
      Endpoint : /{contest_id}/question/{question_id}/submit
      HTTP request parameters : Path Variable, Request Body
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `contest_id` | `string` | Id of contest to be fetched |
| `question_id` | `string` | Id of question to be fetched |
| `code` | `string` | Code submitted by participant |

- Returns current leaderboard of the contest(Paginated). 
        
```http
      HTTP method : GET 
      Endpoint : /{contest_id}/leaderboard/pageNumber=X&pageSize=Y
      HTTP request parameters : Path Variable, Query Param
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `contest_id` | `string` | Id of contest to be fetched |
| `pageNumber` | `integer` | Index of page to be fetched |
| `pageSize` | `integer` | Number of rows per page |

- Return a list of upcoming contest 
        
```http
      HTTP method : GET 
      Endpoint : /upcoming 
```

- Returns a list of running contest 
        
```http
      HTTP method : GET 
      Endpoint : /running  
```

#### **Endpoints under /question :**

- Endpoint to create a question by an authenticated user.
        
```http
      HTTP method : POST 
      Endpoint : /create
      HTTP request parameters : Request Body
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `title` | `string` | Question Title |
| `statement` | `string` | Problem Statement |
| `test_input` | `string` | Input to test code |
| `test_output` | `string` | Corresponding Output to test code |

- Endpoint to fetch questions which appeared in previous contests and are now public 
        
```http
      HTTP method : GET 
      Endpoint : /all
```

- Returns current leaderboard of the contest. Accepts query parameter like pageSize to define number of rows per page and pageNumber, index of page to be fetched
        
```http
      HTTP method : GET 
      Endpoint : /id_search/{question_id}
      HTTP request parameters : Path Variable
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `question_id` | `string` | Id of question to be fetched |

#### **Endpoints under /api/auth :**

- Endpoint to register a user.
        
```http
      HTTP method : POST 
      Endpoint : /register
      HTTP request parameters : Request Body
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | Username |
| `password` | `string` | Password |

- Endpoint to login a user. Upon successful login user receives JWT token.
        
```http
      HTTP method : POST 
      Endpoint : /login
      HTTP request parameters : Request Body
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | Username |
| `password` | `string` | Password |

#### **Endpoints under /user :**

- Endpoint to fetch List of questions created by a user
        
```http
      HTTP method : GET 
      Endpoint : /questions
```

- Endpoint to fetch List of contests created by a user
        
```http
      HTTP method : GET 
      Endpoint : /contest
```

#### **Endpoint for remote code execution server :**

- Endpoint to send participant's code for execution. Receives output of code execution as response.
        
```http
      HTTP method : POST
      Endpoint : /run
      HTTP request parameters : Request Body
```     
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `code` | `string` | Code submitted by participant |
| `input` | `string` | Input for code execution |
| `lang_code` | `int` | Language code for multiple Language support |
| `dateTime` | `date` | Date Time of request |

## Checkout

 - Visit https://github.com/manas-0407/CoC_Server
 - Repo for code execution server, currently deployed in AWS.
## Screenshots

![Demo Screenshot](https://github.com/manas-0407/Code-On-Cloud/blob/main/imgs/create.png)
![Demo Screenshot](https://github.com/manas-0407/Code-On-Cloud/blob/main/imgs/leaderb.png)
![Demo Screenshot](https://github.com/manas-0407/Code-On-Cloud/blob/main/imgs/ques.png)

