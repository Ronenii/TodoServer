package com.Ronenii.Kaplat_server_exercise.services.api;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;

import java.util.List;

public interface TodoService {
    List<TODO> list();

    TODO addTodo(TODO todo);

    TODO updateTodo(Integer id, EState state);
}
