public class Task {
    String task;
    Boolean isCompleted;

    public Task(String task) {
        this.task = task;
        this.isCompleted = false;
    }

    public void setIsCompleted() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        String str = isCompleted ? "[X]" : "[ ]";
        str += " " + this.task;

        return str;
    }
}
