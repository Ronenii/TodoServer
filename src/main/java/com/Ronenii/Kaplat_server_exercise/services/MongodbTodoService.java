package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TodoMongodb;
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

    public List<TodoMongodb> list() {
        return mongodbTodoRepository.findAll();
    }

    public Long count() {
        return mongodbTodoRepository.count();
    }

    public TodoMongodb addTodo(TodoMongodb todo) {
        todo.setRawid(null);
        return mongodbTodoRepository.save(todo);
    }

    public boolean existsTodoByTitle(TodoMongodb todo){
        return mongodbTodoRepository.existsTodoMongodbByTitle(todo.getTitle());
    }

    public TodoMongodb getById(Integer id) {
        return mongodbTodoRepository.findTodoMongodbByRawid(id);
    }

    public List<TodoMongodb> getTodosByState(EState state) {
        List<TodoMongodb> ret = null;
        if(state == EState.ALL){
            ret = list();
        }
        else{
            ret = mongodbTodoRepository.findTodoMongodbByState(state);
        }
        return ret;
    }

    public List<TodoMongodb> getTodosByStateAndSortBy(EState state, ESortBy sortBy){
        List<TodoMongodb> todoList = getTodosByState(state);
        switch (sortBy) {
            case ID -> todoList.sort(Comparator.comparing(TodoMongodb::getRawid));
            case DUE_DATE -> todoList.sort(Comparator.comparing(TodoMongodb::getDueDate));
            case TITLE -> todoList.sort(Comparator.comparing(TodoMongodb::getTitle));
        }

        return todoList;
    }

    public void deleteTodoById(Integer id){mongodbTodoRepository.deleteTodoMongodbByRawid(id);}

    public TodoMongodb updateTodo(Integer id, EState state){
        TodoMongodb todoToUpdate = getById(id);

        if(todoToUpdate == null){
            return null;
        }

        todoToUpdate.setState(state);
        return mongodbTodoRepository.save(todoToUpdate);
    }
}
