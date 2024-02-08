package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.eSortBy;
import com.Ronenii.Kaplat_server_exercise.model.eState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODO;
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

    public List<TODO> list() {
        return mongodbTodoRepository.findAll();
    }

    public Long count() {
        return mongodbTodoRepository.count();
    }

    public TODO addTodo(TODO todo) {
        todo.setRawid(null);
        return mongodbTodoRepository.save(todo);
    }

    public TODO getById(Integer id) {
        return mongodbTodoRepository.findTodoByRawid(id);
    }

    public List<TODO> getTodosByState(eState state) {
        return mongodbTodoRepository.findTODOByState(state);
    }

    public List<TODO> getTodosByStateAndSortBy(eState state, eSortBy sortBy){
        List<TODO> todoList = getTodosByState(state);
        switch (sortBy) {
            case ID:
                todoList.sort(Comparator.comparing(TODO::getRawid));
                break;
            case DUE_DATE:
                todoList.sort(Comparator.comparing(TODO::getDuedate));
                break;
            case TITLE:
                todoList.sort(Comparator.comparing(TODO::getTitle));
                break;
        }

        return todoList;
    }

    public void deleteTodoById(Integer id){mongodbTodoRepository.deleteByRawid(id);}

    public TODO updateTodo(Integer id, eState state){
        TODO todoToUpdate = getById(id);

        if(todoToUpdate == null){
            return null;
        }

        todoToUpdate.setState(state);
        return mongodbTodoRepository.save(todoToUpdate);
    }
}
