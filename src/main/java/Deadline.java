public class Deadline extends Task {
    protected String by;

    public Deadline(String desc, String by) {
        super(desc);
        this.by = by;
    }

    /**
     * Returns this deadline in the pipe-separated format used by Judey's save file.
     *
     * @return deadline type, completion status, description, and due date
     */
    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    @Override
    public String toString(){
        return "[D]" + super.toString() + " (by: " + this.by + ")\n";
    }
}
