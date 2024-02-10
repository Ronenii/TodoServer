package com.Ronenii.Kaplat_server_exercise.services.api;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> list();

    Todo addTodo(Todo todo);

    Todo updateTodo(Integer id, EState state);

    Long count();

    boolean existsTODOByTitle(Todo todo);

    Todo getById(Integer id);

    List<Todo> getTodosByState(EState state);

    List<Todo> getTodosByStateAndSortBy(EState state, ESortBy sortBy);

    void deleteTodoById(Integer id);
}
