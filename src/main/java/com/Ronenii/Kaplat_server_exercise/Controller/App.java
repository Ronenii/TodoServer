package com.Ronenii.Kaplat_server_exercise.Controller;

import com.Ronenii.Kaplat_server_exercise.Model.Result;
import com.Ronenii.Kaplat_server_exercise.Model.TODO;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.*;

import java.util.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@RestController
public class App {

    Gson gson;
    private final int INVALID = -1;
    private static int requestCount = 0;

    private static DB db = new DB();
    public static final String REQUEST_LOGGER = "request-logger";
    public static final String TODO_LOGGER = "todo-logger";
    private static final Logger requestLogger = LoggerFactory.getLogger(REQUEST_LOGGER);
    private static final Logger todoLogger = LoggerFactory.getLogger(TODO_LOGGER);
    private final static List VALID_LEVELS = Arrays.asList("DEBUG", "INFO", "ERROR");
    private final static List VALID_LOGGERS = Arrays.asList(REQUEST_LOGGER, TODO_LOGGER);


    public App() {
        gson = new Gson();
    }

    // Handles the GET query sent to the given endpoint.
    // Returns "OK" if user reached the endpoint.
    @GetMapping({"/todo/health"})
    @ResponseStatus(HttpStatus.OK)
    public String healthQuery(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequestDebug(responseTime);
        return "OK";
    }

    // Handles the POST query sent to the given endpoint.
    // Tries to create an object in the todos list with the given parameters.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @PostMapping(
            value = {"/todo"},
            consumes = {"application/json"}
    )
    public ResponseEntity<String> createTodoQuery(@RequestBody TODO todo, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        int todoCount = db.getTodoCount();
        logRequestInfo(request);
        Result<Integer> result = new Result<Integer>();
        String responseJson;
        HttpStatus responseStatus = null;
        if (db.TodoExists(todo)) {
            result.setErrorMessage("Error: TODO with the title " + todo.getTitle() + " already exists in the system");
            logTodoError(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
            TODO.revokeId();
        } else if (!db.TodoHasCorrectTime(todo)) {
            result.setErrorMessage("Error: Can’t create new TODO that its due date is in the past");
            logTodoError(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
            TODO.revokeId();
        } else {
            todoLogger.info("Creating new TODO with Title [{}] {}", todo.getTitle(), logEndMSG());
            todoLogger.debug("Currently there are {} TODOs in the system. New TODO will be assigned with id {} {}", todoCount, todoCount + 1, logEndMSG());
            responseStatus = HttpStatus.OK;
            result.setResult(todo.getId());
            db.addTodo(todo);
        }

        // send the required response
        responseJson = gson.toJson(result);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequestDebug(responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Handles the GET query sent to the given endpoint.
    // Tries to count and return in the response the number of objects in the todos list with the given status.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @GetMapping({"/todo/size"})
    public ResponseEntity<String> getTodosCountQuery(String status, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        Result<Integer> result = new Result<Integer>();
        String responseJson;
        HttpStatus responseStatus = null;
        int instances;
        try {
            instances = db.countTodoInstances(status);
            responseStatus = HttpStatus.OK;
            result.setResult(instances);
            todoLogger.info("Total TODOs count for state {} is {} {}", status, instances, logEndMSG());
        } catch (IllegalArgumentException e) {
            responseStatus = HttpStatus.BAD_REQUEST;
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequestDebug(responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Handles the GET query sent to the given endpoint.
    // Returns a response with its body being a JSON array of the objects with the given status sorted in ascending order.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @GetMapping({"/todo/content"})
    public ResponseEntity<String> getTodosDataQuery(String status, String sortBy, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        ArrayList<TODO> resultArray = new ArrayList<TODO>();
        Result<String> result = new Result<String>();
        String responseJson;
        HttpStatus responseStatus = null;

        todoLogger.info("Extracting todos content. Filter: {} | Sorting by: {} {}", status, sortBy, logEndMSG());
        try {
            db.sortTodos(resultArray, status, sortBy);
            responseStatus = HttpStatus.OK;
            result.setResult(gson.toJson(resultArray));
            todoLogger.debug("There are a total of {} todos in the system. The result holds {} todos {}", db.getTodoCount(), db.countTodoInstances(status), logEndMSG());
        } catch (IllegalArgumentException e) {
            responseStatus = HttpStatus.BAD_REQUEST;
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequestDebug(responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Handles the PUT query sent to the given endpoint.
    // Tries to update the status of the object with the given id.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @PutMapping({"/todo"})
    public ResponseEntity<String> updateTodoStatusQuery(int id, String status, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        Result<String> result = new Result<String>();
        String responseJson, oldStatus;
        HttpStatus responseStatus = null;
        todoLogger.info("Update TODO id [{}] state to {} {}", id, status, logEndMSG());
        try {
            oldStatus = db.updateTodoStatus(id, status);
            result.setResult(oldStatus);
            responseStatus = HttpStatus.OK;
            todoLogger.debug("Todo id [{}] state change: {} --> {} {}", id, oldStatus, status, logEndMSG());

        } catch (IllegalArgumentException e) {
            responseStatus = HttpStatus.BAD_REQUEST;
        } catch (NoSuchElementException e) {
            responseStatus = HttpStatus.NOT_FOUND;
            result.setErrorMessage("Error: no such TODO with id " + id);
            logTodoError(result.getErrorMessage());
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequestDebug(responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Handles the DELETE query sent to the given endpoint.
    // Tries to delete the object with the given id.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @DeleteMapping({"/todo"})
    public ResponseEntity<String> deleteTodoQuery(int id, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        Result<Integer> result = new Result<Integer>();
        String responseJson, oldStatus;
        HttpStatus responseStatus = null;

        try {
            db.deleteTodo(id);
            todoLogger.info("Removing todo id {} {}", id, logEndMSG());
            responseStatus = HttpStatus.OK;
            result.setResult(db.countTodoInstances("ALL"));
            todoLogger.debug("After removing todo id [{}] there are {} TODOs in the system {}", id, db.getTodoCount(), logEndMSG());

        } catch (NoSuchElementException e) {
            responseStatus = HttpStatus.NOT_FOUND;
            result.setErrorMessage("Error: no such TODO with id " + id);
            logTodoError(result.getErrorMessage());
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequestDebug(responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // TODO: need to make suer info gets printed out to requests.log and debug to console.
    private void logRequestDebug(long responseTime) {
        requestLogger.debug("request #{} duration: {}ms {}", requestCount, responseTime, logEndMSG());
    }

    private void logRequestInfo(HttpServletRequest request) {
        requestLogger.info("Incoming request | #{} | resource: {} | HTTP Verb {} {}", ++requestCount, request.getRequestURI(), request.getMethod(), logEndMSG());
    }

    @GetMapping({"/logs/level"})
    public String getCurrentLogLevel(String loggerName, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        String logLevel;
        try {
            logLevel = getLogLevel(loggerName);
        } catch (IllegalArgumentException e) {
            return "Failure: " + e.getMessage();
        }
        long endTime = System.currentTimeMillis();
        long responseTime = startTime - endTime;
        logRequestDebug(responseTime);
        return "Success: " + logLevel;
    }

    @PutMapping({"/logs/level"})
    public String setCurrentLogLevel(String loggerName, String logLevel, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        try {
            setLogLevel(logLevel, loggerName);
        } catch (IllegalArgumentException e) {
            return "Failure: " + e.getMessage();
        }

        long endTime = System.currentTimeMillis();
        long responseTime = startTime - endTime;
        logRequestDebug(responseTime);
        return "Success " + logLevel;
    }

    private void setLogLevel(String logLevel, String loggerName) {
        if (!VALID_LEVELS.contains(logLevel)) {
            throw new IllegalArgumentException("No such log level");
        }
        if (!VALID_LOGGERS.contains(loggerName)) {
            throw new IllegalArgumentException("No such logger name");
        }
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(loggerName);
        logger.setLevel(Level.toLevel(logLevel));
    }

    private String getLogLevel(String loggerName) {
        if (!VALID_LOGGERS.contains(loggerName)) {
            throw new IllegalArgumentException("No such logger name");
        }
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = loggerContext.getLogger(loggerName);
        return logger.getLevel().toString();
    }

    private String logEndMSG() {
        return String.format("| request #%s", requestCount);
    }

    private void logTodoError(String errorMSG) {
        todoLogger.error("ERROR: {} {}", errorMSG, logEndMSG());
    }
}

