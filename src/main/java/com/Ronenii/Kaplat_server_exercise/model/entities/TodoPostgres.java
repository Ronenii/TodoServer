package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "todos")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class TodoPostgres implements Serializable, Todo {
    @Id
    @Column(name = "rawid", nullable = false)
    private Integer id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "duedate", nullable = false)
    private Long dueDate;
    @Column(name = "state", nullable = false)
    private String state;

    public TodoPostgres(String Title, String Content, long dueDate) {
        this.title = Title;
        this.content = Content;
        this.dueDate = dueDate;
        this.state = EState.PENDING.name();
    }

    public TodoPostgres() {
    }

    public String toString() {
        return "TODO(id=" + id +
                ", Title=" + this.title +
                ", Content=" + this.content +
                ", DueDate=" + this.dueDate +
                ", Status=" + this.state + ")";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public long getDueDate() {
        return dueDate;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setState(EState state) {
        this.state = state.name();
    }

    @Override
    public EState getState() {
        return EState.valueOf(state);
    }

    @Override
    public String getContent() {
        return content;
    }
}
