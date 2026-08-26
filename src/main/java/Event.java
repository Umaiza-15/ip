public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String desc, String from, String to){
        super(desc);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the pipe-separated format used by Judey's save file.
     *
     * @return event type, completion status, description, start time, and end time
     */
    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String toString(){
        String display = "[E]" + super.toString();
        display += " (from: " + this.from + " to: " + this.to + ")\n";
        return display;
    }
}
