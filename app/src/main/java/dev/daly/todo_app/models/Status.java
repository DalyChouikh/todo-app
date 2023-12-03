package dev.daly.todo_app.models;

public enum Status {
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private String status;
    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static Status fromString(String status) {
        status = status.replace("_","");
        for (Status s : Status.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }

}
