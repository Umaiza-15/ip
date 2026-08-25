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
        String echo;
        while (true){
            echo = scanner.nextLine();
            System.out.println("----------------------------------------");
            if (echo.equals("bye")){
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("----------------------------------------");
                break;
            }
            System.out.println(echo);
            System.out.println("----------------------------------------");
        }
    }
}
