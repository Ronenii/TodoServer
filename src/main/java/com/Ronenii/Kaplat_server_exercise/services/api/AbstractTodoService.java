package com.Ronenii.Kaplat_server_exercise.services.api;

import com.Ronenii.Kaplat_server_exercise.model.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
import com.Ronenii.Kaplat_server_exercise.repositories.api.TodoRepository;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractTodoService implements TodoService{
    protected final TodoRepository todoRepository;

    protected AbstractTodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Long count() {
        return todoRepository.count();
    }

    public boolean existsTODOByTitle(TODOMongodb todo) {
        return todoRepository.existsTODOByTitle(todo.getTitle());
    }

    public TODO getById(Integer id) {
        return todoRepository.findTodoByRawid(id);
    }

    public List<TODO> getTodosByState(EState state) {
        return todoRepository.findTODOByState(state);
    }

    public List<TODO> getTodosByStateAndSortBy(EState state, ESortBy sortBy) {
        List<TODO> todoList = getTodosByState(state);
        switch (sortBy) {
            case ID -> todoList.sort(Comparator.comparing(TODO::getRawid));
            case DUE_DATE -> todoList.sort(Comparator.comparing(TODO::getDueDate));
            case TITLE -> todoList.sort(Comparator.comparing(TODO::getTitle));
        }

        return todoList;
    }

    public void deleteTodoById(Integer id) {
        todoRepository.deleteByRawid(id);
    }
}
