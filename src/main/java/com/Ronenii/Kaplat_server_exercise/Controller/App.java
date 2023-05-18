package com.Ronenii.Kaplat_server_exercise.Controller;

import com.Ronenii.Kaplat_server_exercise.Model.Result;
import com.Ronenii.Kaplat_server_exercise.Model.TODO;
import com.Ronenii.Kaplat_server_exercise.Model.eStatus;
import com.Ronenii.Kaplat_server_exercise.Model.eSortBy;
import com.google.gson.Gson;
import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.*;
import java.util.*;

@RestController
public class App {

    private List<TODO> todos = new ArrayList<TODO>();
    Gson gson;
    private final int INVALID = -1;
    public static int requestCount = 0;
    private static final Logger requestLogger = LoggerFactory.getLogger("com.Ronenii.Kaplat_server_exercise.requests-logger");


    public App() {
        gson = new Gson();
    }

    // Handles the GET query sent to the given endpoint.
    // Returns "OK" if user reached the endpoint.
    @GetMapping({"/todo/health"})
    @ResponseStatus(HttpStatus.OK)
    public String healthQuery(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        long endTime= System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequest(request, responseTime);
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

        Result<Integer> result = new Result<Integer>();
        String responseJson;
        HttpStatus responseStatus = null;
        if (TodoExists(todo)) {
            result.setErrorMessage("Error: TODO with the title " + todo.getTitle() + " already exists in the system");
            System.out.println(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
            TODO.revokeId();
        } else if (!TodoHasCorrectTime(todo)) {
            result.setErrorMessage("Error: Can’t create new TODO that its due date is in the past");
            System.out.println(result.getErrorMessage());
            responseStatus = HttpStatus.CONFLICT;
            TODO.revokeId();
        } else {
            // add the to-do to the to-do array.
            responseStatus = HttpStatus.OK;
            result.setResult(todo.getId());
            System.out.println("Success in task 2: " + todo);
            todos.add(todo);
        }

        // send the required response
        responseJson = gson.toJson(result);
        long endTime= System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequest(request, responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // check if the do-do already exists in the array
    public boolean TodoExists(TODO todo) {
        for (TODO t : todos) {
            if (t.getTitle().equals(todo.getTitle()))
                return true;
        }
        return false;
    }

    // check if the to-do has a valid dueDate
    public boolean TodoHasCorrectTime(TODO todo) {
        return java.lang.System.currentTimeMillis() <= todo.getDueDate();
    }

    // Handles the GET query sent to the given endpoint.
    // Tries to count and return in the response the number of objects in the todos list with the given status.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @GetMapping({"/todo/size"})
    public ResponseEntity<String> getTodosCountQuery(String status, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        Result<Integer> result = new Result<Integer>();
        String responseJson;
        HttpStatus responseStatus = null;
        int instances;
        try {
            instances = countTodoInstances(status);
            responseStatus = HttpStatus.OK;
            result.setResult(instances);
            System.out.println("Success in task 3 (Filter=" + status + ", instances=" + instances + ")");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            responseStatus = HttpStatus.BAD_REQUEST;
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequest(request, responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Counts the number of instances of to-dos with the given filter in the to-do list.
    // If the filter given is wrong will return
    public int countTodoInstances(String filter) {
        if (!isValidStatus(filter)) {
            throw new IllegalArgumentException(invalidParameterMessage(filter));
        } else if (filter.equals("ALL")) {
            return todos.size();
        } else {
            eStatus status = eStatus.valueOf(filter);
            int instances = 0;
            for (TODO t : todos) {
                if (t.getStatus() == status) {
                    instances++;
                }
            }
            return instances;
        }

    }


    public boolean isValidStatus(String status) {
        Set<String> filters = new HashSet<String>();
        filters.add("ALL");
        filters.add("PENDING");
        filters.add("DONE");
        filters.add("LATE");

        return filters.contains(status);
    }

    // Handles the GET query sent to the given endpoint.
    // Returns a response with its body being a JSON array of the objects with the given status sorted in ascending order.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @GetMapping({"/todo/content"})
    public ResponseEntity<String> getTodosDataQuery(String status, String sortBy, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        ArrayList<TODO> resultArray = new ArrayList<TODO>();
        Result<String> result = new Result<String>();
        String responseJson;
        HttpStatus responseStatus = null;
        try {
            sortTodos(resultArray, status, sortBy);
            responseStatus = HttpStatus.OK;
            result.setResult(gson.toJson(resultArray));
            System.out.println("Success in task 4 (status=" + status + ", sortBy=" + sortBy + ")");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            responseStatus = HttpStatus.BAD_REQUEST;
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime- startTime;
        logRequest(request, responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Check if the given sortBy filter is valid
    public boolean isValidSortBy(String sortBy) {
        Set<String> filters = new HashSet<String>();
        filters.add("");
        filters.add("ID");
        filters.add("DUE_DATE");
        filters.add("TITLE");

        return filters.contains(sortBy);
    }

    public void sortTodos(ArrayList<TODO> resultArray, String status, String sortBy) {
        StringBuilder errorMessage = new StringBuilder();

        /// Check validity of the given filters and build an appropriate error message
        if (!isValidSortBy(sortBy)) {
            errorMessage.append(invalidParameterMessage(sortBy));
        }
        if (!isValidStatus(status)) {
            errorMessage.append(invalidParameterMessage(status));
        }
        if (!errorMessage.isEmpty()) {
            throw new IllegalArgumentException(errorMessage.toString());
        }

        // Add the todos to the result array based on what status they are
        if (status.equals("ALL")) {
            resultArray.addAll(todos);

        } else {
            addToArrayByStatus(resultArray, status);
        }

        // Sort the result array based on the given sorting filter
        SortTodosWithSortby(resultArray, sortBy);
    }

    // Adds the to-dos to the resultArray based on if they have the same status as the given "status" parameter
    public void addToArrayByStatus(ArrayList<TODO> resultArray, String status) {
        for (TODO t : todos) {
            if (eStatus.valueOf(status) == t.getStatus()) {
                resultArray.add(t);
            }
        }
    }

    // Sort resultArray based on the possible sortBy filter given
    public void SortTodosWithSortby(ArrayList<TODO> resultArray, String sortBy) {
        if (sortBy.equals("")) {
            sortBy = "ID";
        }
        eSortBy newSortBy = eSortBy.valueOf(sortBy);
        switch (newSortBy) {
            case ID:
                resultArray.sort(Comparator.comparing(TODO::getId));
                break;
            case DUE_DATE:
                resultArray.sort(Comparator.comparing(TODO::getDueDate));
                break;
            case TITLE:
                resultArray.sort(Comparator.comparing(TODO::getTitle));
                break;
        }
    }

    public String invalidParameterMessage(String param) {
        return "Invalid parameter: " + param + "\n";
    }

    // Handles the PUT query sent to the given endpoint.
    // Tries to update the status of the object with the given id.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @PutMapping({"/todo"})
    public ResponseEntity<String> updateTodoStatusQuery(int id, String status, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        Result<String> result = new Result<String>();
        String responseJson, oldStatus;
        HttpStatus responseStatus = null;

        try {
            oldStatus = updateTodoStatus(id, status);
            result.setResult(oldStatus);
            responseStatus = HttpStatus.OK;
            System.out.println("Success in task 5 (id=" + id + ", status=" + status + ")");

        } catch (IllegalArgumentException e) {
            responseStatus = HttpStatus.BAD_REQUEST;
            System.out.println(e.getMessage());
        } catch (NoSuchElementException e) {
            responseStatus = HttpStatus.NOT_FOUND;
            result.setErrorMessage("Error: no such TODO with id " + id);
            System.out.println(e.getMessage());
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequest(request, responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Updates the given to-do's status. The to-do is found by id
    public String updateTodoStatus(int id, String status) {
        StringBuilder errorMessage = new StringBuilder();

        if (!isValidStatus(status)) {
            throw new IllegalArgumentException(invalidParameterMessage(status));
        }
        eStatus newStatus = eStatus.valueOf(status);
        String oldStatus;
        for (TODO t : todos) {
            if (t.getId() == id) {
                oldStatus = t.getStatus().toString();
                t.setStatus(newStatus);
                return oldStatus;
            }
        }
        throw new NoSuchElementException(noIdErrorMessage(id));
    }

    public String noIdErrorMessage(int id) {
        return "ID " + id + " not found";
    }

    // Handles the DELETE query sent to the given endpoint.
    // Tries to delete the object with the given id.
    // Prints out to console based on if the task succeeded or not.
    // Returns a response with the appropriate response status and boy.
    @DeleteMapping({"/todo"})
    public ResponseEntity<String> deleteTodoQuery(int id, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        Result<Integer> result = new Result<Integer>();
        String responseJson, oldStatus;
        HttpStatus responseStatus = null;

        try {
            deleteTodo(id);
            responseStatus = HttpStatus.OK;
            result.setResult(countTodoInstances("ALL"));
            System.out.println("Success in task 5 (id=" + id + ")");

        } catch (NoSuchElementException e) {
            responseStatus = HttpStatus.NOT_FOUND;
            result.setErrorMessage("Error: no such TODO with id " + id);
            System.out.println(e.getMessage());
        } finally {
            responseJson = gson.toJson(result);
        }
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        logRequest(request, responseTime);
        return ResponseEntity.status(responseStatus).body(responseJson);
    }

    // Finds the to-do in the todos list based on its id and removes it.
    // Throws an exception if id doesn't exist.
    public void deleteTodo(int id) {
        for (TODO t : todos) {
            if (t.getId() == id) {
                todos.remove(t);
                return;
            }
        }
        throw new NoSuchElementException(noIdErrorMessage(id));
    }

    // TODO: need to make suer info gets printed out to requests.log and debug to console.
    public void logRequest(HttpServletRequest request, long responseTime) {
        String requestCountStr = String.valueOf(++requestCount);
        String logEnd = String.format("| request #" + requestCountStr);
        String infoMsg = String.format("Incoming request | #%s | resource: %s | HTTP Verb %s %s", requestCountStr,  request.getRequestURI(), request.getMethod(),logEnd);
        String debugMsg = String.format("request #%s duration: %sms %s", requestCountStr, responseTime,logEnd);
        //ThreadContext.put("number", requestCountStr);
        requestLogger.info(infoMsg);
        requestLogger.debug(debugMsg);
    }
}

