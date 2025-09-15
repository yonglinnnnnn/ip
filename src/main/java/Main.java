import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import megabot.MegaBot;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private final MegaBot megabot = new MegaBot("./data/megabot.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setMegaBot(megabot); // inject the instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
