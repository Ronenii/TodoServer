package com.Ronenii.Kaplat_server_exercise.repositories.api;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;

import java.util.List;

public interface TodoRepository {
    Todo findTodoByRawid(int rawid);

    boolean existsTODOByTitle(String title);

    List<Todo> findTODOByState(EState state);

    void deleteByRawid(int rawid);

    long count();
}
