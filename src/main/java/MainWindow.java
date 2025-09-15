import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import megabot.MegaBot;
import megabot.gui.Gui;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private MegaBot megabot;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/nezha2.png"));
    private Image megabotImage = new Image(this.getClass().getResourceAsStream("/images/megadino.png"));

    /**
     * Initialises the chatbot with welcome message
     */
    @FXML
    public void initialize() {
        userInput.clear();
        String welcomeMessage = Gui.showWelcome();
        dialogContainer.getChildren().add(DialogBox.getMegaBotDialog(welcomeMessage, megabotImage));
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the MegaBot instance */
    public void setMegaBot(MegaBot megabot) {
        this.megabot = megabot;
    }

    /**
     * Creates two dialog boxes, one echoing user input and
     * the other containing MegaBot's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input.equalsIgnoreCase("bye")) {
            String exitMessage = Gui.showGoodbye();
            dialogContainer.getChildren().add(DialogBox.getMegaBotDialog(exitMessage, megabotImage));
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else {
            String response = megabot.getResponse(input);
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getMegaBotDialog(response, megabotImage)
            );
        }

        userInput.clear();
    }
}
