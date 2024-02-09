package com.Ronenii.Kaplat_server_exercise.services.api;

import com.Ronenii.Kaplat_server_exercise.model.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;

import java.util.List;

public interface TodoService {
    List<TODO> list();

    TODO addTodo(TODO todo);

    TODO updateTodo(Integer id, EState state);

    Long count();

    public boolean existsTODOByTitle(TODO todo);

    public TODO getById(Integer id);

    public List<TODO> getTodosByState(EState state);
    public List<TODO> getTodosByStateAndSortBy(EState state, ESortBy sortBy);
    public void deleteTodoById(Integer id);
}
