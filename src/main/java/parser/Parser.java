package parser;

import java.util.Scanner;

import storage.Storage;
import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;
import task.TaskList;

public class Parser {
    protected TaskList todoList;
    protected Storage storage;

    public Parser(TaskList todoList, Storage storage) {
        this.todoList = todoList;
        this.storage = storage;
    }

    public void parse() {
        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);
        while ((!isExit) && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                isExit = true;
            } else if (userInput.equals("list")) {
                this.todoList.printList();
            } else if (isMarkTask(userInput)) {
                this.todoList.changeMarkingOfTask(userInput, storage);
            } else if (isDeleteTask(userInput)) {
                this.todoList.deleteTask(userInput, storage);
            } else if (isFindTask(userInput)) {
                this.todoList.findTask(userInput, storage);
            } else {
                this.echo(userInput);
            }
        }
        scanner.close();
    }

    public void echo(String userInput) {
        String[] words = userInput.split("\\s+");
        if (words.length > 0) {
            String firstWord = words[0];
            int firstSpaceIndex = userInput.indexOf(' ');
            String description = userInput.substring(firstSpaceIndex + 1);
            switch (firstWord) {
                case "todo": {
                    if (words.length == 1) {
                        System.out.println("The description of a todo cannot be empty!");
                    } else {
                        Task t = new Todo(description);
                        this.todoList.add(t);
                        this.todoList.listOverviewAfterAdding(t, this.storage);
                    }
                    break;
                }
                case "deadline": {
                    if (words.length == 1) {
                        System.out.println("The description of a deadline cannot be empty!");
                    } else {
                        String[] parts = description.split("\\\\by");
                        String ddl_description = parts.length > 0 ? parts[0].trim() : "";
                        String ddl_time = parts.length > 1 ? parts[1].trim() : "";
                        Task t = new Deadline(ddl_description, this.storage.readAsDate(ddl_time));
                        todoList.add(t);
                        this.todoList.listOverviewAfterAdding(t, this.storage);
                    }
                    break;
                }
                case "event": {
                    if (words.length == 1) {
                        System.out.println("The description of a todo cannot be empty!");
                    } else {
                        String[] parts = userInput.split("\\\\from|\\\\to");
                        String event_description = parts.length > 0 ? parts[0].trim() : "";
                        String event_from = parts.length > 1 ? parts[1].trim() : "";
                        String event_to = parts.length > 2 ? parts[2].trim() : "";
                        Task t = new Event(event_description, this.storage.readAsDate(event_from),
                                this.storage.readAsDate(event_to));
                        todoList.add(t);
                        this.todoList.listOverviewAfterAdding(t, this.storage);
                    }
                    break;
                }
                default:
                    System.out.println("Sorry, I don't understand your command.");
                    break;
            }
        } else {
            System.out.println("Sorry, I don't understand your command.");
        }
    }

    public boolean isMarkTask(String userInput) {
        String[] words = userInput.split("\\s+");
        if (words.length == 2) {
            if (words[0].equals("mark") || words[0].equals("unmark")) {
                try {
                    Integer.parseInt(words[1]);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isDeleteTask(String userInput) {
        String[] words = userInput.split("\\s+");
        if (words.length == 2) {
            if (words[0].equals("delete")) {
                try {
                    Integer.parseInt(words[1]);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isFindTask(String userInput) {
        String[] words = userInput.split("\\s+");
        if (words.length == 2) {
            if (words[0].equals("find")) {
                return true;
            }
        }
        return false;
    }
}
