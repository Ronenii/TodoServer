package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import com.Ronenii.Kaplat_server_exercise.repositories.MongodbTodoRepository;
import com.Ronenii.Kaplat_server_exercise.services.api.AbstractTodoService;
import com.Ronenii.Kaplat_server_exercise.services.api.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MongodbTodoService extends AbstractTodoService implements TodoService {

    @Autowired
    public MongodbTodoService(MongodbTodoRepository mongodbTodoRepository) {
        super(mongodbTodoRepository);
    }

    @Override
    public List<Todo> list() {
        List<TodoMongodb> todosMongodb = ((MongodbTodoRepository)todoRepository).findAll();

        return new ArrayList<>(todosMongodb);
    }

    @Override
    public Todo addTodo(Todo todo) {
        todo.setRawid(null);
        return ((MongodbTodoRepository) todoRepository).save((TodoMongodb) todo);
    }

    @Override
    public Todo updateTodo(Integer id, EState state) {
        Todo todoToUpdate = getById(id);

        if (todoToUpdate == null) {
            return null;
        }

        todoToUpdate.setState(state);
        return ((MongodbTodoRepository) todoRepository).save((TodoMongodb) todoToUpdate);
    }
}
