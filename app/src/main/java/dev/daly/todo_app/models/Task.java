package dev.daly.todo_app.models;

public class Task {
    private String title;
    private Status status;

    public Task(String title, String status) {
        this.title = title;
        this.status = Status.fromString(status);
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
