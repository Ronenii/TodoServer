package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOPostgres;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
import com.Ronenii.Kaplat_server_exercise.repositories.MongodbTodoRepository;
import com.Ronenii.Kaplat_server_exercise.repositories.PostgresTodoRepository;
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
    public List<TODO> list() {
        List<TODOMongodb> todosMongodb = ((MongodbTodoRepository) todoRepository).findAll();

        return new ArrayList<>(todosMongodb);
    }

    @Override
    public TODO addTodo(TODO todo) {
        todo.setRawid(null);
        return ((MongodbTodoRepository) todoRepository).save((TODOMongodb) todo);
    }

    @Override
    public TODO updateTodo(Integer id, EState state) {
        TODO todoToUpdate = getById(id);

        if (todoToUpdate == null) {
            return null;
        }

        todoToUpdate.setState(state);
        return ((MongodbTodoRepository) todoRepository).save((TODOMongodb) todoToUpdate);
    }
}