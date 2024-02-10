package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.Todo;
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
    private Long dueDate;
    private EState state;

    public TodoMongodb(String Title, String Content, long dueDate) {
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

    //    public static void revokeId() {idCount--;}
//
//    public void giveId() {
//        this.rawid = idCount++;
//    }
//
    public String getTitle() {
        return title;
    }

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
    public long getDueDate() {
        return dueDate;
    }

    public EState getState() {
        return state;
    }

    public void setState(EState state) {
        this.state = state;
    }


}