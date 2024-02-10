package com.Ronenii.Kaplat_server_exercise.model.entities.api;

public interface Todo {
    String toString();
    Integer getRawid();
    void setRawid(Integer rawid);
    long getDueDate();
    String getTitle();
    void setState(EState state);
    EState getState();
}
