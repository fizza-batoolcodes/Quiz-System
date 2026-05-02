package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentDashboard {

    String studentEmail;

    public StudentDashboard(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public void start(Stage stage) {

        // Title
        Label title = new Label("Student Dashboard");
        title.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #5a3e4d;"
        );

        // Welcome
        Label welcome = new Label("Welcome, " + studentEmail);
        welcome.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: #5a3e4d;"
        );

        // Button style (reuse for all)
        String btnStyle =
                "-fx-background-color: #ff85a2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;";

        // Start Quiz
        Button startQuizBtn = new Button("Start Quiz");
        startQuizBtn.setPrefWidth(220);
        startQuizBtn.setStyle(btnStyle);

        // View Results
        Button resultBtn = new Button("View Results");
        resultBtn.setPrefWidth(220);
        resultBtn.setStyle(btnStyle);

        // Logout
        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(220);
        logoutBtn.setStyle(btnStyle);

        // ================= BUTTON ACTIONS =================

        startQuizBtn.setOnAction(e -> {
            new QuizApp(studentEmail).start(stage);
        });

        resultBtn.setOnAction(e -> {
            new ViewResults(studentEmail).start(stage);
        });

        logoutBtn.setOnAction(e -> {
            new LoginApp().start(stage);
        });

        // ================= LAYOUT =================
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                title,
                welcome,
                startQuizBtn,
                resultBtn,
                logoutBtn
        );

        // 🌸 Background (baby pink)
        root.setStyle(
                "-fx-background-color: #ffe4ec;" +
                        "-fx-padding: 40;"
        );

        Scene scene = new Scene(root, 600, 450);

        stage.setTitle("Student Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}