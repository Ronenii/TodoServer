package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOPostgres;
import com.Ronenii.Kaplat_server_exercise.repositories.MongodbTodoRepository;
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

    public List<TODOPostgres> list() {
        return postgresTodoRepository.findAll();
    }

    public Long count() {
        return postgresTodoRepository.count();
    }

    public TODOPostgres addTodo(TODOPostgres todo) {
        todo.setRawid(null);
        return postgresTodoRepository.save(todo);
    }

    public boolean existsTODOByTitle(TODOPostgres todo){
        return postgresTodoRepository.existsTODOByTitle(todo.getTitle());
    }

    public TODOPostgres getById(Integer id) {
        return postgresTodoRepository.findTodoByRawid(id);
    }

    public List<TODOPostgres> getTodosByState(EState state) {
        return postgresTodoRepository.findTODOByState(state);
    }

    public List<TODOPostgres> getTodosByStateAndSortBy(EState state, ESortBy sortBy){
        List<TODOPostgres> todoList = getTodosByState(state);
        switch (sortBy) {
            case ID:
                todoList.sort(Comparator.comparing(TODOPostgres::getRawid));
                break;
            case DUE_DATE:
                todoList.sort(Comparator.comparing(TODOPostgres::getDueDate));
                break;
            case TITLE:
                todoList.sort(Comparator.comparing(TODOPostgres::getTitle));
                break;
        }

        return todoList;
    }

    public void deleteTodoById(Integer id){postgresTodoRepository.deleteByRawid(id);}

    public TODOPostgres updateTodo(Integer id, EState state){
        TODOPostgres todoToUpdate = getById(id);

        if(todoToUpdate == null){
            return null;
        }

        todoToUpdate.setState(state);
        return postgresTodoRepository.save(todoToUpdate);
    }
}
