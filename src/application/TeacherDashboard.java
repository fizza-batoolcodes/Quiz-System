package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TeacherDashboard {

    public void start(Stage stage) {

        // TITLE
        Label title = new Label("Teacher Dashboard");
        title.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #5a3e4d;"
        );

        // Common button style
        String btnStyle =
                "-fx-background-color: #ff85a2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;";

        // BUTTONS
        Button addQuestionBtn = new Button("Add Question");
        Button viewResultsBtn = new Button("View Results");
        Button behaviourBtn = new Button("Behaviour Logs");
        Button logoutBtn = new Button("Logout");

        addQuestionBtn.setPrefWidth(180);
        viewResultsBtn.setPrefWidth(180);
        behaviourBtn.setPrefWidth(180);
        logoutBtn.setPrefWidth(180);

        addQuestionBtn.setStyle(btnStyle);
        viewResultsBtn.setStyle(btnStyle);
        behaviourBtn.setStyle(btnStyle);
        logoutBtn.setStyle(btnStyle);

        // LAYOUT
        VBox root = new VBox(12);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                title,
                addQuestionBtn,
                viewResultsBtn,
                behaviourBtn,
                logoutBtn
        );

        // 🌸 Background
        root.setStyle(
                "-fx-background-color: #ffe4ec;" +
                        "-fx-padding: 30;"
        );

        // ================= ACTIONS =================

        addQuestionBtn.setOnAction(e -> {
            new AddQuestionForm().start(stage);
        });

        viewResultsBtn.setOnAction(e -> {
            new TeacherMonitoringDashboard().start(stage);
        });

        behaviourBtn.setOnAction(e -> {
            new BehaviourLogsView().start(stage);
        });

        logoutBtn.setOnAction(e -> {
            new LoginApp().start(stage);
        });

        // ================= SCENE =================
        Scene scene = new Scene(root, 350, 300);

        stage.setTitle("Teacher Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}