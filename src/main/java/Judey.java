import java.util.Scanner;

public class Judey {
    public static void main(String[] args) {
        System.out.println("----------------------------------------");
        String banner = "JJJJJJ  uu   uu  dddddd   eeeeeee  yy   yy\n" +
                "   JJ   uu   uu  dd   dd  ee       yy   yy\n" +
                "   JJ   uu   uu  dd   dd  eeeee     yyyyy\n" +
                "JJ JJ   uu   uu  dd   dd  ee         yyy\n" +
                " JJJ     uuuu u  dddddd   eeeeeee    yyy";
        System.out.println(banner + "\n");
        System.out.println("Hello! I'm Judey.\n" + "What can I do for you?");
        System.out.println("----------------------------------------");
        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int index = 0;
        String echo;

        while (true){
            echo = scanner.nextLine();
            System.out.println("----------------------------------------");
            if (echo.equals("bye")){
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("----------------------------------------");
                break;
            }
            if (echo.equals("list")) {
                String display = "Here are the tasks in your list: \n";
                for (int i = 0; i < index; i++) {
                    display += Integer.toString(i + 1) + "." + tasks[i].toString();
                }
                System.out.println(display);
                System.out.println("----------------------------------------");
                continue;
            }
            String[] words = echo.trim().split(" ", 2);
            if (words[0].equals("mark")) {
                if (words.length != 2) {
                    System.out.println("Please provide a task number, for example: mark 2");
                    System.out.println("----------------------------------------");
                    continue;
                }

                try {
                    int i = Integer.parseInt(words[1]);
                    if (i < 1 || i > index) {
                        System.out.println("That task number does not exist.");
                        System.out.println("----------------------------------------");
                        continue;
                    }
                    tasks[i-1].markAsDone();
                    String display = "Nice! I've marked this task as done: \n";
                    display += "  " + tasks[i-1].toString();
                    display += "----------------------------------------";
                    System.out.println(display);
                    continue;
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                    System.out.println("----------------------------------------");
                    continue;
                }
            }
            else if (words[0].equals("unmark")) {
                if (words.length != 2) {
                    System.out.println("Please provide a task number, for example: unmark 2");
                    System.out.println("----------------------------------------");
                    continue;
                }

                try {
                    int i = Integer.parseInt(words[1]);
                    if (i < 1 || i > index) {
                        System.out.println("That task number does not exist.");
                        System.out.println("----------------------------------------");
                        continue;
                    }
                    tasks[i-1].markAsNotDone();
                    String display = "OK, I've marked this task as not done yet: \n";
                    display += "  " + tasks[i-1].toString();
                    display += "----------------------------------------";
                    System.out.println(display);
                    continue;
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                    System.out.println("----------------------------------------");
                    continue;
                }
            }
            String[] parts = echo.split("/", 3);
            if (words[0].equals("todo")) {
                String[] desc = echo.split(" ", 2);
                tasks[index] = new Todo(desc[1]);
                System.out.println("Got it. I've added this task \n" + "  " + tasks[index].toString());
                System.out.println("Now you have " + Integer.toString(index + 1) + " tasks in this list.\n");
                System.out.println("----------------------------------------");
                index++;
            }
            else if (words[0].equals("event")) {
                String[] desc = parts[0].trim().split(" ", 2);
                String[] from = parts[1].trim().split(" ", 2);
                String[] to = parts[2].trim().split(" ", 2);
                tasks[index] = new Event(desc[1], from[1], to[1]);
                System.out.println("Got it. I've added this task \n" + "  " + tasks[index].toString());
                System.out.println("Now you have " + Integer.toString(index + 1) + " tasks in this list.\n");
                System.out.println("----------------------------------------");
                index++;
            }
            else if (words[0].equals("deadline")){
                String[] desc = parts[0].trim().split(" ", 2);
                String[] by = parts[1].trim().split(" ", 2);
                tasks[index] = new Deadline(desc[1], by[1]);
                System.out.println("Got it. I've added this task \n" + "  " + tasks[index].toString());
                System.out.println("Now you have " + Integer.toString(index + 1) + " tasks in this list.\n");
                System.out.println("----------------------------------------");
                index++;
            }
            else {
                continue;
            }
        }
    }
}
