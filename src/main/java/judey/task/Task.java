package judey.task;

public class Task {
    /**
     * Represents a task with a description and completion status
     */
    protected boolean isDone; //whether the task is done
    protected String description; //the task description

    /**
     * Creates a task with the description name given and is initially marked as incomplete
     * @param desc the text describing this task
     */
    public Task(String desc) {
        this.isDone = false;
        this.description = desc;
    }

    /**
     * Marks the task as complete
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the status icon displayed for the task
     * @return {@code "X"} when done; otherwise a space
     */
    public String getStatusIcon(){
        return (isDone ? "X" : " ");
    }

    /**
     * Returns this task in the pipe-separated format used by judey.Judey's save file.
     *
     * @return task type, completion status, and description
     */
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the description of this task.
     *
     * @return task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a formatted text representation of this task
     * @return status icon and description
     */
    @Override
    public String toString() {
        String done = this.getStatusIcon();
        String item = "[" + done + "] " + this.description;
        return item;
    }
}
