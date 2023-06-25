# TodoServer

## Technologies used

* IDE: IntelliJ
* Programming language: Java 17
* Framework: Spring Boot
* Logging framework: Logback (built in logger in spring boot)
* Containerization: Docker

## Overview

This is an HTTP server developed using Spring Boot and is a TODO app. TODO apps allow users to maintain a list of things they need to do.
It features multiple endpoints which allow clients to manage the server's todos as well as setting logger levels for the server.

This was a 3 part assignment in a course called "Practical tools for the industry pt.1".
The course teaches concepts "on a high level" and for the implementation of the assignemnts we had to research and choose our own platforms and
frameworks to develop on.

This assignment focused on these concepts:
1. Client-server model
2. Logging 
3. Containerization 

## Usage
The server's default listening port is 9285.

**Server endpoints:**

**1) Health**
> /todo/health

This is a sanity endpoint used to check that the server is up and running.

Method: GET

The response will be 200, and the result is the string OK (not a json, simply the string itself, case sensitive)

**2) Create new TODO**
> /todo

Creates a new TODO item in the system. 

Method: POST

Body: json object:
{
    title: <TODO title> //string
    content: <TODO content> //string
    dueDate: <timestamp in millis> // long number
}

The TODO is created with the status PENDING.
When a new TODO is created, it is assigned by the server to the next id in turn.

Upon processing, 2 verifications occur:
1) Is there already a TODO with this title (TODOs are distinguished by their title)
2) dueDate is in the future. 

If the operation can be invoked (all verification went OK): the response code will be 200.
The result will hold the (newly) assigned TODO number

If there is an error, the response will end with 409 (conflict);  the errorMessage will be set according to the error:

**TODO already exists:** 
>“Error: TODO with the title [<TODO title>] already exists in the system”

**due date is in the past:** 
>“Error: Can’t create new TODO that its due date is in the past”
