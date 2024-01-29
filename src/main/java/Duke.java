import java.util.ArrayList;
import java.util.Scanner;

public class Duke {
    static void greeting(String botName) {
        System.out.println("Hello! I'm " + botName + "\n"
                + "What can I do for you?");
    }

    static void exit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    static void echo(String userInput, ArrayList<String> TodoList) {
        System.out.println("added: " + userInput);
        addToList(TodoList, userInput);
    }

    static void addToList(ArrayList<String> TodoList, String userInput) {
        TodoList.add(userInput);
    }

    static void printList(ArrayList<String> TodoList) {
        int length = TodoList.size();
        for (int i = 0; i < length; i++) {
            String pos = String.valueOf(i + 1);
            System.out.println(pos + ". " + TodoList.get(i));
        }
    }

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);

        String botName = "Zizhen";
        greeting(botName);

        ArrayList<String> TodoList = new ArrayList<>();

        boolean isExit = false;
        while (!isExit) {
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                isExit = true;
            } else if (userInput.equals("list")) {
                printList(TodoList);
            } else {
                echo(userInput, TodoList);
            }
        }

        exit();
    }
}
