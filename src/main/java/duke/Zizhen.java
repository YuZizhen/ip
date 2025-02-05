package duke;

import java.util.ArrayList;
import java.util.Scanner;

import dialogbox.DialogBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import task.Task;
import task.TaskList;
import ui.Ui;
import storage.Storage;
import parser.Parser;

/**
 * The main class for Zizhen chat bot.
 */

public class Zizhen extends Application {

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    private Image user = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image duke = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    public Zizhen() {

    }

    public static void main(String[] args) {
        Ui Zizhen = new Ui("Zizhen");
        Storage storage = new Storage("./data/duke.txt");
        Storage archived = new Storage("./data/archived.txt");
        Zizhen.greeting();

        ArrayList<Task> temp = new ArrayList<>();
        temp = storage.getHistory();
        TaskList todoList = new TaskList(temp);

        Parser parser = new Parser(todoList, storage, archived);

        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);
        while ((!isExit) && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                isExit = true;
            } else {
                parser.parse(userInput);
            }
        }
        scanner.close();

        Zizhen.exit();
    }

    /**
     * Iteration 1:
     * Returns a label with the specified text and adds it to the dialog container.
     * 
     * @param text String containing text to add
     * @return a label with the specified text that has word wrap enabled.
     */
    private Label getDialogLabel(String text) {
        Label textToAdd = new Label(text);
        textToAdd.setWrapText(true);

        return textToAdd;
    }

    /**
     * Iteration 2:
     * Returns two dialog boxes, one echoing user input and the other containing
     * Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    private void handleUserInput(Parser parser) {
        Label userText = getDialogLabel(userInput.getText());
        Label dukeText = getDialogLabel(getResponse(userInput.getText(), parser));
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, new ImageView(user)),
                DialogBox.getDukeDialog(dukeText, new ImageView(duke)));
        userInput.clear();
    }

    private void greet(Ui Zizhen) {
        Label dukeText = getDialogLabel(Zizhen.greeting());
        dialogContainer.getChildren().addAll(
                DialogBox.getDukeDialog(dukeText, new ImageView(duke)));
    }

    /**
     * Passes in the user input and returns what the parser will return.
     */
    public String getResponse(String userInput, Parser parser) {
        return parser.parse(userInput);
    }

    @Override
    public void start(Stage stage) {
        Ui Zizhen = new Ui("Zizhen");
        Storage storage = new Storage("./data/duke.txt");
        Storage archived = new Storage("./data/archived.txt");
        greet(Zizhen);

        ArrayList<Task> temp = new ArrayList<>();
        temp = storage.getHistory();
        TaskList todoList = new TaskList(temp);

        Parser parser = new Parser(todoList, storage, archived);

        // Step 1. Setting up required components

        // The container for the content of the chat to scroll.
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        // Step 2. Formatting the window to look as expected
        stage.setTitle("Duke");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        // Part 3. Add functionality to handle user input.
        sendButton.setOnMouseClicked((event) -> {
            handleUserInput(parser);
        });

        userInput.setOnAction((event) -> {
            handleUserInput(parser);
        });

        // Scroll down to the end every time dialogContainer's height changes.
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        boolean isExit = false;
        Scanner scanner = new Scanner(System.in);
        while ((!isExit) && scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                isExit = true;
            }
            parser.parse(userInput);
        }
        scanner.close();

        Zizhen.exit();
    }
}
