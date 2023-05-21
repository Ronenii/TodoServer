package com.Ronenii.Kaplat_server_exercise.Controller;

import com.Ronenii.Kaplat_server_exercise.Model.TODO;
import com.Ronenii.Kaplat_server_exercise.Model.eSortBy;
import com.Ronenii.Kaplat_server_exercise.Model.eStatus;

import java.util.*;

public class DB {

    private List<TODO> todos = new ArrayList<TODO>();

    public int getTodoCount()
    {
        return todos.size();
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

    public void addTodo(TODO todo)
    {
        todos.add(todo);
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

    public String noIdErrorMessage(int id) {
        return "ID " + id + " not found";
    }

    public boolean isValidStatus(String status) {
        Set<String> filters = new HashSet<String>();
        filters.add("ALL");
        filters.add("PENDING");
        filters.add("DONE");
        filters.add("LATE");

        return filters.contains(status);
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
}
