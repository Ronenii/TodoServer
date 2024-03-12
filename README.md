# TodoServer

## 1) Technologies used

* IDE: IntelliJ
* Programming language: Java 17
* Framework: Spring Boot
* Containerization: Docker
* Databse: Postgresql, MongoDB
* Logging framework: Logback (built in logger in spring boot)

## 2) Overview

This is an HTTP server developed using Spring Boot and is a TODO app. TODO apps allow users to maintain a list of things they need to do.
It features multiple endpoints which allow clients to manage the server's todos as well as setting logger levels for the server.

This was a 5 part assignment in a series of courses called "Practical tools for the industry".
The course teaches concepts "on a high level" and for the implementation of the assignemnts we had to research and choose our own platforms and
frameworks to develop on.

This assignment focused on these concepts:
1. Client-server model
2. Database
3. Containerization
4. Deploying to a cloud platform
5. Logging 


## 3) Usage

### 3.1) Properties
The server container's default listening port is 3769.

### 3.2) Server endpoints:

**3.2.1) Health**

`/todo/health`

Method: **GET**

This is a sanity endpoint used to check that the server is up and running.


**3.2.2) Create new TODO**

`/todo`

Method: **POST**

Creates a new TODO item in the system. 

```diff
Body: json object:
{
    title: <TODO title> //string
    content: <TODO content> //string
    dueDate: <timestamp in millis> // long number
}
```

The TODO is created with the status PENDING.
When a new TODO is created, it is assigned by the server to the next id in turn.

Upon processing, 2 verifications occur:
1) Is there already a TODO with this title (TODOs are distinguished by their title)
2) dueDate is in the future. 

If the operation can be invoked (all verification went OK): the response code will be 200.
The result will hold the (newly) assigned TODO number.


**3.2.3) Get TODOs count**

`/todo/size`

Method: **GET**

Query Parameters: 
- **status**. Value: ALL, PENDING, LATE, DONE (in capital case only).
- **persistenceMethod**. Value: POSTGRES, MONGO.

Returns the total number of TODOs in the system, according to the given filter. The persistence method dictates which of the DBs will the info come from.


**3.2.4) Get TODOs data**

`/todo/content`

Method: **GET**

Query Parameters: 
- **status**. Value: ALL, PENDING, LATE, DONE.
- **sortBy**. Value: ID, DUE_DATE, TITLE (Note: This is an optional query parameter. It does not have to appear.)
- **persistenceMethod**. Value: POSTGRES, MONGO.

The response will be a json array. The array will hold json objects that describe a single todo. 
Each TODO object holds:

```diff
{
	id: integer,
	title: string,
	content: string,
	status: string,
	dueDate: timestamp (ms): long,
}
```

The array will be sorted according to the sortBy parameter.

The sorting will always be ascending.

In case sortBy is not supplied, the sorting is done by ID

If no TODOs are available the result is an empty array.


**3.2.5) Update TODO status**

`/todo`

Method: **PUT**

Query Parameters: 
- **id**. Number. The TODO id
- **status**. The status to update. It can be PENDING, LATE, or DONE

If the TODO exists (according to the id), its status gets updated.


**3.2.6) Delete TODO**

`/todo`

Method: **DELETE**

Query Parameter: **id**. Number. The TODO id

Deletes a todo with the given id.


**3.2.7) Get current logger level**

`/logs/level`

Method: **GET**

Query Parameter: **logger-name**. The name of the logger (request-logger or todo-logger).

Returns the current level of the given logger.


**3.2.8) Get current logger level**

`/logs/level`

Method: **PUT**

Query Parameters: 
- **logger-name**. The name of the logger (request-logger or todo-logger).
- **logger-level** Value: ERROR, INFO, DEBUG

Sets the given logger's level to the given level.

### 3.3) Response
The response format is in json and it contains the following:
```diff
{
	result: <result of operation> : depends on the context
	errorMessage: <message in case of error> : string
}
```

## 4) Logging

The server keeps logs of all requests sent to it and all todos added. The log files are respectively called `requests.log` & `todos.log`.

Each log will hold the following structure ({} are placeholders):
```diff
{date-time} {log-level}: {log-message} | request #{request-number}
```

## 5) Deployment
Docker is the containerization framework used for the exercise. 

### 5.1) Setting up the environment
In the release section of this repo you will find a docker-compose.yml file. Download this file and place it in your sdesired path.
Make sure you have the docker engine installed and running.

### 5.2) Run the server:
In order to run the server you need to use the following command:
```diff
 docker-compose up
```
This will automatically run the server's conmtainer as well a container of MongoDB and a container of Postgresql for this server's todo Databases.

**5.2.1) Usage**

You can use the `curl` command if you can't reach the server with normal queries.
Other than that the usage of the server is the same as described [here](#3-Usage).

### 5.3) Shutting down the server
In order to shut down the server you need to use the following command:
```diff
 docker-compose down
```
