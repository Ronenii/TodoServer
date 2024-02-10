package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoPostgres;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import com.Ronenii.Kaplat_server_exercise.repositories.api.TodoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostgresTodoRepository extends JpaRepository<TodoPostgres, Integer>, TodoRepository {
    @Override
    Todo findTodoByRawid(int rawid);

    @Override
    boolean existsTODOByTitle(String title);

    @Override
    List<Todo> findTODOByState(EState state);
}
