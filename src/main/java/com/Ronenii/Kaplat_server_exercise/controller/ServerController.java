package com.Ronenii.Kaplat_server_exercise.controller;

import com.Ronenii.Kaplat_server_exercise.model.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.Result;
import com.Ronenii.Kaplat_server_exercise.model.EPersistenceMethod;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOPostgres;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
import com.Ronenii.Kaplat_server_exercise.services.MongodbTodoService;
import com.Ronenii.Kaplat_server_exercise.services.PostgresTodoService;
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
public class ServerController {

    Gson gson;
    private static int requestCount = 0;

    //private static DB db = new DB();

    private final MongodbTodoService mongodbTodoService;
    private final PostgresTodoService postgresTodoService;

    public static final String REQUEST_LOGGER = "request-logger";
    public static final String TODO_LOGGER = "todo-logger";
    private static final Logger requestLogger = LoggerFactory.getLogger(REQUEST_LOGGER);
    private static final Logger todoLogger = LoggerFactory.getLogger(TODO_LOGGER);
    private final static List VALID_LEVELS = Arrays.asList("DEBUG", "INFO", "ERROR");
    private final static List VALID_LOGGERS = Arrays.asList(REQUEST_LOGGER, TODO_LOGGER);


    public ServerController(MongodbTodoService mongodbTodoService, PostgresTodoService postgresTodoService) {
        this.mongodbTodoService = mongodbTodoService;
        this.postgresTodoService = postgresTodoService;
        gson = new Gson();
    }

    // Handles the GET query sent to the given endpoint.
    // Returns "OK" if user reached the endpoint.
    @GetMapping({"/todo/health"})
    @ResponseStatus(HttpStatus.OK)
    public String healthQuery(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);

        logRequestDebug(startTime);
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
    public ResponseEntity<String> createTodoQuery(String title, String content, long dueDate, HttpServletRequest request) {
        TODOMongodb newMongoTODO = new TODOMongodb(title, content, dueDate);
        TODOPostgres newPostgresTODO = new TODOPostgres(title, content, dueDate);
        long startTime = System.currentTimeMillis();
        long todoCount = mongodbTodoService.count();
        logRequestInfo(request);
        Result<Integer> result = new Result<>();
        String responseJson;
        HttpStatus responseStatus;

        if (todoExists(newMongoTODO, newPostgresTODO)) {
            result.setErrorMessage("Error: TODO with the title " + newMongoTODO.getTitle() + " already exists in the system");
            logTodoError(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
        } else if (!todoTimeValid(dueDate)) {
            result.setErrorMessage("Error: Can’t create new TODO that its due date is in the past");
            logTodoError(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
        } else {
            todoLogger.info("Creating new TODO with Title [{}] {}", newMongoTODO.getTitle(), logEndMSG());
            todoLogger.debug("Currently there are {} TODOs in the system. New TODO will be assigned with id {} {}", todoCount, todoCount + 1, logEndMSG());
            responseStatus = HttpStatus.OK;
            result.setResult(newMongoTODO.getRawid());
            mongodbTodoService.addTodo(newMongoTODO);
            // TODO: postgresTodoService.addTodo(newPostgresTodo);
        }

        // send the required response
        responseJson = gson.toJson(result);
        logRequestDebug(startTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Handles the GET query sent to the given endpoint.
    // Tries to count and return in the response the number of objects in the todos list with the given status.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @GetMapping({"/todo/size"})
    public ResponseEntity<String> getTodosCountQuery(String status, String persistenceMethod, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        Result<Integer> result = new Result<>();
        String responseJson;
        HttpStatus responseStatus;
        int instances = 0;
        try {
            EPersistenceMethod ePersistenceMethod = EPersistenceMethod.valueOf(persistenceMethod);
            EState eState = EState.valueOf(status);
            switch (ePersistenceMethod) {
                case POSTGRES -> {
                    instances = postgresTodoService.getTodosByState(eState).size();
                }
                case Mongo -> {
                    instances = mongodbTodoService.getTodosByState(eState).size();
                }
            }
            responseStatus = HttpStatus.OK;
            result.setResult(instances);
            todoLogger.info("Total TODOs count for state {} is {} {}", status, instances, logEndMSG());
        } catch (IllegalArgumentException e) {
            responseStatus = HttpStatus.BAD_REQUEST;
        } finally {
            responseJson = gson.toJson(result);
        }

        logRequestDebug(startTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Handles the GET query sent to the given endpoint.
    // Returns a response with its body being a JSON array of the objects with the given status sorted in ascending order.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @GetMapping({"/todo/content"})
    public ResponseEntity<String> getTodosDataQuery(String status, String sortBy, String persistenceMethod, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        List<TODO> resultArray = null;
        Result<String> result = new Result<>();
        String responseJson;
        HttpStatus responseStatus;

        if (sortBy == null) {
            sortBy = "ID";
        }

        todoLogger.info("Extracting todos content. Filter: {} | Sorting by: {} {}", status, sortBy, logEndMSG());
        try {
            EPersistenceMethod ePersistenceMethod = EPersistenceMethod.valueOf(persistenceMethod);
            EState eState = EState.valueOf(status);
            ESortBy eSortBy = ESortBy.valueOf(sortBy);

            switch (ePersistenceMethod) {
                case POSTGRES -> {
                    resultArray = postgresTodoService.getTodosByStateAndSortBy(eState, eSortBy);
                }
                case Mongo -> {
                    resultArray = mongodbTodoService.getTodosByStateAndSortBy(eState, eSortBy);
                }
            }
            result.setResult(gson.toJson(resultArray));

            todoLogger.debug("There are a total of {} todos in the system. The result holds {} todos {}", mongodbTodoService.count(), resultArray.size(), logEndMSG());
            responseStatus = HttpStatus.OK;

        } catch (IllegalArgumentException e) {
            responseStatus = HttpStatus.BAD_REQUEST;
        } finally {
            responseJson = gson.toJson(result);
        }

        logRequestDebug(startTime);
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
        Result<String> result = new Result<>();
        String responseJson, oldStatus;
        HttpStatus responseStatus;
        todoLogger.info("Update TODO id [{}] state to {} {}", id, status, logEndMSG());
        try {
            oldStatus = mongodbTodoService.getById(id).getState().toString();
            EState newStatus = EState.valueOf(status);
            mongodbTodoService.updateTodo(id, newStatus);
            postgresTodoService.updateTodo(id, newStatus);
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

        logRequestDebug(startTime);
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
        Result<Integer> result = new Result<>();
        String responseJson;
        HttpStatus responseStatus;

        try {
            mongodbTodoService.deleteTodoById(id);
            //TODO: postgresTodoService.deleteTodoById(id);
            todoLogger.info("Removing todo id {} {}", id, logEndMSG());
            responseStatus = HttpStatus.OK;
            long instancesCount = mongodbTodoService.count();
            result.setResult((int) instancesCount);
            todoLogger.debug("After removing todo id [{}] there are {} TODOs in the system {}", id, instancesCount, logEndMSG());

        } catch (NoSuchElementException e) {
            responseStatus = HttpStatus.NOT_FOUND;
            result.setErrorMessage("Error: no such TODO with id " + id);
            logTodoError(result.getErrorMessage());
        } finally {
            responseJson = gson.toJson(result);
        }

        logRequestDebug(startTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    private void logRequestDebug(long startTime) {
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        requestLogger.debug("request #{} duration: {}ms {}", requestCount, responseTime, logEndMSG());
    }

    private void logRequestInfo(HttpServletRequest request) {
        requestLogger.info("Incoming request | #{} | resource: {} | HTTP Verb {} {}", ++requestCount, request.getRequestURI(), request.getMethod(), logEndMSG());
    }

    @GetMapping({"/logs/level"})
    public String getCurrentLogLevel(@RequestParam("logger-name") String loggerName, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        String logLevel;
        try {
            logLevel = getLogLevel(loggerName);
        } catch (IllegalArgumentException e) {
            return "Failure: " + e.getMessage();
        }

        logRequestDebug(startTime);
        return "Success: " + logLevel;
    }

    @PutMapping({"/logs/level"})
    public String setCurrentLogLevel(@RequestParam("logger-name") String loggerName,
                                     @RequestParam("logger-level") String loggerLevel, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestInfo(request);
        try {
            setLogLevel(loggerLevel, loggerName);
        } catch (IllegalArgumentException e) {
            return "Failure: " + e.getMessage();
        }

        logRequestDebug(startTime);
        return "Success " + loggerLevel;
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
        todoLogger.error("{} {}", errorMSG, logEndMSG());
    }

    private boolean todoTimeValid(long dueDate){
        return java.lang.System.currentTimeMillis() <= dueDate;
    }

    private boolean todoExists(TODOMongodb todoMongodb, TODOPostgres todoPostgres){
        // TODO: Add postgres todo
        return mongodbTodoService.existsTODOByTitle(todoMongodb);
    }
}

