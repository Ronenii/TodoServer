package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.repositories.MongodbTodoRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MongodbTodoService {
    private final MongodbTodoRepository mongodbTodoRepository;

    public MongodbTodoService(MongodbTodoRepository mongodbTodoRepository) {
        this.mongodbTodoRepository = mongodbTodoRepository;
    }

    public List<TODOMongodb> list() {
        return mongodbTodoRepository.findAll();
    }

    public Long count() {
        return mongodbTodoRepository.count();
    }

    public TODOMongodb addTodo(TODOMongodb todo) {
        todo.setRawid(null);
        return mongodbTodoRepository.save(todo);
    }

    public boolean existsTODOByTitle(TODOMongodb todo){
        return mongodbTodoRepository.existsTODOByTitle(todo.getTitle());
    }

    public TODOMongodb getById(Integer id) {
        return mongodbTodoRepository.findTodoByRawid(id);
    }

    public List<TODOMongodb> getTodosByState(EState state) {
        return mongodbTodoRepository.findTODOByState(state);
    }

    public List<TODOMongodb> getTodosByStateAndSortBy(EState state, ESortBy sortBy){
        List<TODOMongodb> todoList = getTodosByState(state);
        switch (sortBy) {
            case ID:
                todoList.sort(Comparator.comparing(TODOMongodb::getRawid));
                break;
            case DUE_DATE:
                todoList.sort(Comparator.comparing(TODOMongodb::getDueDate));
                break;
            case TITLE:
                todoList.sort(Comparator.comparing(TODOMongodb::getTitle));
                break;
        }

        return todoList;
    }

    public void deleteTodoById(Integer id){mongodbTodoRepository.deleteByRawid(id);}

    public TODOMongodb updateTodo(Integer id, EState state){
        TODOMongodb todoToUpdate = getById(id);

        if(todoToUpdate == null){
            return null;
        }

        todoToUpdate.setState(state);
        return mongodbTodoRepository.save(todoToUpdate);
    }
}
