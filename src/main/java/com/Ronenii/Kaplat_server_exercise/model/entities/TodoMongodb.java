package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "todos")
public class TodoMongodb implements Serializable, Todo {
    @Id
    private String id;
    private Integer rawid;
    private final String title;
    private final String content;
    private Long duedate;
    private String state;

    public TodoMongodb(String title, String content, long duedate) {
        this.title = title;
        this.content = content;
        this.duedate = duedate;
        this.state = EState.PENDING.name();
    }

    public String toString() {
        return "TODO(id=" + rawid +
                ", Title=" + this.title +
                ", Content=" + this.content +
                ", DueDate=" + this.duedate +
                ", Status=" + this.state + ")";
    }

    public Integer getRawid() {
        return rawid;
    }

    public void setRawid(Integer rawid) {
        this.rawid = rawid;
    }

    public String getTitle() {
        return title;
    }

    public long getDueDate() {
        return duedate;
    }

    public EState getState() {
        return EState.valueOf(state);
    }

    public void setState(EState state) {
        this.state = state.name();
    }


}
