package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import jakarta.persistence.Id;

import java.io.Serializable;

public class TodoMongodb implements Serializable, Todo {
    @Id
    private String id;
    private Integer rawid;
    private final String title;
    private final String content;
    private final Long dueDate;
    private EState state;

    public TodoMongodb(String Title, String Content, long dueDate) {
        this.title = Title;
        this.content = Content;
        this.dueDate = dueDate;
        this.state = EState.PENDING;
    }

    @Override
    public String toString() {
        return "TODO(id=" + rawid +
                ", Title=" + this.title +
                ", Content=" + this.content +
                ", DueDate=" + this.dueDate +
                ", Status=" + this.state + ")";
    }

    @Override
    public Integer getRawid() {
        return rawid;
    }

    @Override
    public void setRawid(Integer rawid) {
        this.rawid = rawid;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public long getDueDate() {
        return dueDate;
    }

    @Override
    public EState getState() {
        return state;
    }

    @Override
    public void setState(EState state) {
        this.state = state;
    }
}
