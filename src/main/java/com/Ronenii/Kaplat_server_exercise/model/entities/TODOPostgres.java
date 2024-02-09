package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="todos")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class TODOPostgres implements Serializable, TODO {
    @Id
    @Column(name="rawid", nullable = false)
    private Integer rawid;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @Column(name="duedate", nullable = false)
    private Long dueDate;

    @Column(name="state", nullable = false)
    private EState state;

    public TODOPostgres(String Title, String Content, long dueDate) {
        this.title = Title;
        this.content = Content;
        this.dueDate = dueDate;
        this.state = EState.PENDING;
    }

    public TODOPostgres() {
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

    @Override
    public String getTitle() {
        return title;
    }
}
