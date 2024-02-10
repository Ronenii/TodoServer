package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoPostgres;
import com.Ronenii.Kaplat_server_exercise.repositories.PostgresTodoRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PostgresTodoService {
    private final PostgresTodoRepository postgresTodoRepository;

    public PostgresTodoService(PostgresTodoRepository postgresTodoRepository) {
        this.postgresTodoRepository = postgresTodoRepository;
    }

    public List<TodoPostgres> list() {
        return postgresTodoRepository.findAll();
    }

    public Long count() {
        return postgresTodoRepository.count();
    }

    public TodoPostgres addTodo(TodoPostgres todo) {
        todo.setRawid(null);
        return postgresTodoRepository.save(todo);
    }

    public boolean existsTODOByTitle(TodoPostgres todo){
        return postgresTodoRepository.existsTODOByTitle(todo.getTitle());
    }

    public TodoPostgres getById(Integer id) {
        return postgresTodoRepository.findTodoByRawid(id);
    }

    public List<TodoPostgres> getTodosByState(EState state) {
        return postgresTodoRepository.findTODOByState(state);
    }

    public List<TodoPostgres> getTodosByStateAndSortBy(EState state, ESortBy sortBy){
        List<TodoPostgres> todoList = getTodosByState(state);
        switch (sortBy) {
            case ID:
                todoList.sort(Comparator.comparing(TodoPostgres::getRawid));
                break;
            case DUE_DATE:
                todoList.sort(Comparator.comparing(TodoPostgres::getDueDate));
                break;
            case TITLE:
                todoList.sort(Comparator.comparing(TodoPostgres::getTitle));
                break;
        }

        return todoList;
    }

    public void deleteTodoById(Integer id){postgresTodoRepository.deleteByRawid(id);}

    public TodoPostgres updateTodo(Integer id, EState state){
        TodoPostgres todoToUpdate = getById(id);

        if(todoToUpdate == null){
            return null;
        }

        todoToUpdate.setState(state);
        return postgresTodoRepository.save(todoToUpdate);
    }
}
