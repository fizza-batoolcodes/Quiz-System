import application.DBConnection;
import application.LoginApp;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        LoginApp login = new LoginApp();
        login.start(stage);
    }

    public static void main(String[] args) {

        // Check database connection first
        DBConnection.getConnection();

        // Launch JavaFX app
        launch(args);
    }
}
