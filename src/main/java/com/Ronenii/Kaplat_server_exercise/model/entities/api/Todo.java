package com.Ronenii.Kaplat_server_exercise.model.entities.api;

public interface Todo {
    String toString();
    Integer getId();
    void setId(Integer id);
    long getDueDate();
    String getTitle();
    void setState(EState state);
    EState getState();
    String getContent();
}
