package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "todos")
public class TodoPostgres implements Serializable, Todo {
    @Id
    @Column(name = "rawid", nullable = false)
    private Integer rawid;
    @Column(name = "title", nullable = false)
    private final String title;
    @Column(name = "content", nullable = false)
    private final String content;
    @Column(name = "duedate", nullable = false)
    private final Long dueDate;
    @Column(name = "state", nullable = false)
    private EState state;

    public TodoPostgres(String Title, String Content, long dueDate) {
        this.title = Title;
        this.content = Content;
        this.dueDate = dueDate;
        this.state = EState.PENDING;
    }

    public String toString() {
        return "TODO(id=" + rawid +
                ", Title=" + this.title +
                ", Content=" + this.content +
                ", DueDate=" + this.dueDate +
                ", Status=" + this.state + ")";
    }

    public Integer getRawid() {
        return rawid;
    }

    public void setRawid(Integer rawid) {
        this.rawid = rawid;
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
        this.state = state;
    }

    @Override
    public EState getState() {
        return state;
    }

    //    public static void revokeId() {idCount--;}
//
//    public void giveId() {
//        this.rawid = idCount++;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public Long getDuedate() {
//        return duedate;
//    }
//
//    public void setDuedate(Long duedate) {
//        this.duedate = duedate;
//    }
//
//    public eState getState() {
//        return state;
//    }
//
//    public void setState(eState state) {
//        this.state = state;
//    }


}
