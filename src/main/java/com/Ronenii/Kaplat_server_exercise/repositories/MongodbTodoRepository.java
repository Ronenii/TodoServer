package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import com.Ronenii.Kaplat_server_exercise.repositories.api.TodoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongodbTodoRepository extends MongoRepository<TodoMongodb, String>, TodoRepository {
    @Override
    Todo findTodoByRawid(int rawid);

    @Override
    boolean existsTODOByTitle(String title);

    @Override
    List<Todo> findTodoByState(EState state);

    @Override
    void deleteByRawid(int rawid);
}
