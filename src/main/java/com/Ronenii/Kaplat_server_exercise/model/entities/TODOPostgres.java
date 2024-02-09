package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;

import java.io.Serializable;

public class TODOPostgres implements Serializable, TODO {
    private Integer rawid;
    private final String title;
    private final String content;
    private final Long dueDate;
    private final EState state;

    public TODOPostgres(String Title, String Content, long dueDate) {
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
