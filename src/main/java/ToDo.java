public class ToDo extends Task {
    public ToDo(String todo) {
        super(todo);
    }

    @Override
    public String toString() {
        return "[T]" + this.getStatusIcon() + " " + this.task;
    }
}
