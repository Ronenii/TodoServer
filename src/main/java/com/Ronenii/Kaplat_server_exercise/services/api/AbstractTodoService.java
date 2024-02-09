package com.Ronenii.Kaplat_server_exercise.services.api;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
import com.Ronenii.Kaplat_server_exercise.repositories.api.TodoRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractTodoService implements TodoService {
    protected TodoRepository todoRepository;

    protected AbstractTodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Long count() {
        return todoRepository.count();
    }

    @Override
    public boolean existsTODOByTitle(TODO todo) {
        return todoRepository.existsTODOByTitle(todo.getTitle());
    }

    @Override
    public TODO getById(Integer id) {
        return todoRepository.findTodoByRawid(id);
    }

    @Override
    public List<TODO> getTodosByState(EState state) {
        return todoRepository.findTODOByState(state);
    }

    @Override
    public List<TODO> getTodosByStateAndSortBy(EState state, ESortBy sortBy) {
        List<TODO> todoList = new ArrayList<>(getTodosByState(state));
        switch (sortBy) {
            case ID -> todoList.sort(Comparator.comparing(TODO::getRawid));
            case DUE_DATE -> todoList.sort(Comparator.comparing(TODO::getDueDate));
            case TITLE -> todoList.sort(Comparator.comparing(TODO::getTitle));
        }

        return todoList;
    }

    @Override
    public void deleteTodoById(Integer id) {
        todoRepository.deleteByRawid(id);
    }
}
