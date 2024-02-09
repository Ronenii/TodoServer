package com.Ronenii.Kaplat_server_exercise.model.entities.api;

public interface TODO {
    String toString();

    Integer getRawid();

    void setRawid(Integer rawid);

    long getDueDate();

    EState getState();

    void setState(EState state);

    String getTitle();
}
