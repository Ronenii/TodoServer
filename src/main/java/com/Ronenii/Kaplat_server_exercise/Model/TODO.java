package com.Ronenii.Kaplat_server_exercise.Model;

public class TODO {
    static private int idCount = 1;
    private int id;
    private String Title, Content;
    private long dueDate;
    private eStatus Status;

    public TODO(String Title, String Content, long dueDate) {
        this.id = idCount++;
        this.Title = Title;
        this.Content = Content;
        this.dueDate = dueDate;
        this.Status = eStatus.PENDING;
        System.out.print(this);
    }

    public String toString() {
        return "TODO(id=" + id +
                ", Title=" + this.Title +
                ", Content=" + this.Content +
                ", DueDate=" + this.dueDate +
                ", Status=" + this.Status + ")";
    }

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int idCount) {
        TODO.idCount = idCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void revokeId() {idCount--;}

    public void giveId() {
        this.id = idCount++;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public eStatus getStatus() {
        return Status;
    }

    public void setStatus(eStatus status) {
        Status = status;
    }
}
