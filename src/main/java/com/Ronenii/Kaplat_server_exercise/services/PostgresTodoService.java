package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoPostgres;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import com.Ronenii.Kaplat_server_exercise.repositories.PostgresTodoRepository;
import com.Ronenii.Kaplat_server_exercise.services.api.AbstractTodoService;
import com.Ronenii.Kaplat_server_exercise.services.api.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostgresTodoService extends AbstractTodoService implements TodoService {
    @Autowired
    public PostgresTodoService(PostgresTodoRepository postgresTodoRepository) {
        super(postgresTodoRepository);
    }

    @Override
    public List<Todo> list() {
        List<TodoPostgres> todosPostgres = ((PostgresTodoRepository)todoRepository).findAll();

        return new ArrayList<>(todosPostgres);
    }

    @Override
    public Todo addTodo(Todo todo) {
        todo.setRawid(null);
        return ((PostgresTodoRepository) todoRepository).save((TodoPostgres) todo);
    }

    @Override
    public Todo updateTodo(Integer id, EState state) {
        Todo todoToUpdate = getById(id);

        if (todoToUpdate == null) {
            return null;
        }

        todoToUpdate.setState(state);
        return ((PostgresTodoRepository)todoRepository).save((TodoPostgres) todoToUpdate);
    }
}
