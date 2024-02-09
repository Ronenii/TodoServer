package com.Ronenii.Kaplat_server_exercise.repositories.api;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;

import java.util.List;

public interface TodoRepository {
    TODO findTodoByRawid(int rawid);

    boolean existsTODOByTitle(String title);

    List<TODO> findTODOByState(EState state);

    void deleteByRawid(int rawid);

    long count();
}
