package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOPostgres;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
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
    public List<TODO> list() {
        List<TODOPostgres> todosPostgres = ((PostgresTodoRepository)todoRepository).findAll();

        return new ArrayList<>(todosPostgres);
    }

    @Override
    public TODO addTodo(TODO todo) {
        todo.setRawid(null);
        return ((PostgresTodoRepository) todoRepository).save((TODOPostgres) todo);
    }

    @Override
    public TODO updateTodo(Integer id, EState state) {
        TODO todoToUpdate = getById(id);

        if (todoToUpdate == null) {
            return null;
        }

        todoToUpdate.setState(state);
        return ((PostgresTodoRepository)todoRepository).save((TODOPostgres) todoToUpdate);
    }
}
