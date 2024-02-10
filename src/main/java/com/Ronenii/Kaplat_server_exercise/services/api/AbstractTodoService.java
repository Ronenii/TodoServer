package com.Ronenii.Kaplat_server_exercise.services.api;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
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
    public boolean existsTODOByTitle(Todo todo) {
        return todoRepository.existsTODOByTitle(todo.getTitle());
    }

    @Override
    public Todo getById(Integer id) {
        return todoRepository.findTodoByRawid(id);
    }

    @Override
    public List<Todo> getTodosByState(EState state) {
        List<Todo> ret = null;

        if(state == EState.ALL){
            ret =  list();
        }
        else{
            ret = todoRepository.findTODOByState(state);
        }

        return ret;
    }

    @Override
    public List<Todo> getTodosByStateAndSortBy(EState state, ESortBy sortBy) {
        List<Todo> todoList = null;
        if(state == EState.ALL){
            todoList = new ArrayList<>(list());
        }
        else{
            todoList = new ArrayList<>(getTodosByState(state));
        }
        switch (sortBy) {
            case ID -> todoList.sort(Comparator.comparing(Todo::getRawid));
            case DUE_DATE -> todoList.sort(Comparator.comparing(Todo::getDueDate));
            case TITLE -> todoList.sort(Comparator.comparing(Todo::getTitle));
        }

        return todoList;
    }

    @Override
    public void deleteTodoById(Integer id) {
        todoRepository.deleteByRawid(id);
    }
}
