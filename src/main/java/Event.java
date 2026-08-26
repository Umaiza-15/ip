public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String desc, String from, String to){
        super(desc);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString(){
        String display = "[E]" + super.toString();
        display += " (from: " + this.from + " to: " + this.to + ")\n";
        return display;
    }
}
