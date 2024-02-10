package com.Ronenii.Kaplat_server_exercise.services;

import com.Ronenii.Kaplat_server_exercise.model.ESortBy;
import com.Ronenii.Kaplat_server_exercise.model.EState;
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

    public boolean existsTODOByTitle(TodoMongodb todo){
        return mongodbTodoRepository.existsTODOByTitle(todo.getTitle());
    }

    public TodoMongodb getById(Integer id) {
        return mongodbTodoRepository.findTodoByRawid(id);
    }

    public List<TodoMongodb> getTodosByState(EState state) {
        return mongodbTodoRepository.findTODOByState(state);
    }

    public List<TodoMongodb> getTodosByStateAndSortBy(EState state, ESortBy sortBy){
        List<TodoMongodb> todoList = getTodosByState(state);
        switch (sortBy) {
            case ID:
                todoList.sort(Comparator.comparing(TodoMongodb::getRawid));
                break;
            case DUE_DATE:
                todoList.sort(Comparator.comparing(TodoMongodb::getDueDate));
                break;
            case TITLE:
                todoList.sort(Comparator.comparing(TodoMongodb::getTitle));
                break;
        }

        return todoList;
    }

    public void deleteTodoById(Integer id){mongodbTodoRepository.deleteByRawid(id);}

    public TodoMongodb updateTodo(Integer id, EState state){
        TodoMongodb todoToUpdate = getById(id);

        if(todoToUpdate == null){
            return null;
        }

        todoToUpdate.setState(state);
        return mongodbTodoRepository.save(todoToUpdate);
    }
}
