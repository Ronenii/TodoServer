package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoMongodb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongodbTodoRepository extends MongoRepository<TodoMongodb, String> {
    TodoMongodb findTodoMongodbByRawid(int id);
    boolean existsTodoMongodbByTitle(String title);
    List<TodoMongodb> findTodoMongodbByState(String state);
    void deleteTodoMongodbByRawid(int id);
    long count();
}
