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

> ___/todo/health___
