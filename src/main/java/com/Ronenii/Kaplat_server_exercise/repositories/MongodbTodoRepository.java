package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoMongodb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongodbTodoRepository extends MongoRepository<TodoMongodb, String> {
    TodoMongodb findTodoByRawid(int rawid);
    boolean existsTODOByTitle(String title);
    TodoMongodb findTODOByTitle(String title);
    List<TodoMongodb> findTODOByState(EState state);
    void deleteByRawid(int rawid);
    long count();
}
