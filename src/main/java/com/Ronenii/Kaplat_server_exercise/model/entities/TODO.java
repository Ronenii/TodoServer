package com.Ronenii.Kaplat_server_exercise.model.entities;

import com.Ronenii.Kaplat_server_exercise.model.eState;

public class TODO {
    static private int idCount = 1;
    private Integer rawid;
    private String title, content;
    private Long duedate;
    private eState state;

    public TODO(String Title, String Content, long dueDate) {
        this.rawid = idCount++;
        this.title = Title;
        this.content = Content;
        this.duedate = dueDate;
        this.state = eState.PENDING;
    }

    public String toString() {
        return "TODO(id=" + rawid +
                ", Title=" + this.title +
                ", Content=" + this.content +
                ", DueDate=" + this.duedate +
                ", Status=" + this.state + ")";
    }

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int idCount) {
        TODO.idCount = idCount;
    }

    public Integer getRawid() {
        return rawid;
    }

    public void setRawid(Integer rawid) {
        this.rawid = rawid;
    }

    public static void revokeId() {idCount--;}

    public void giveId() {
        this.rawid = idCount++;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDuedate() {
        return duedate;
    }

    public void setDuedate(Long duedate) {
        this.duedate = duedate;
    }

    public eState getState() {
        return state;
    }

    public void setState(eState state) {
        this.state = state;
    }


}
