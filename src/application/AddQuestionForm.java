package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddQuestionForm {

    public void start(Stage stage) {

        TextField question = new TextField();
        question.setPromptText("Enter Question");

        TextField op1 = new TextField();
        op1.setPromptText("Option A");

        TextField op2 = new TextField();
        op2.setPromptText("Option B");

        TextField op3 = new TextField();
        op3.setPromptText("Option C");

        TextField op4 = new TextField();
        op4.setPromptText("Option D");

        TextField correct = new TextField();
        correct.setPromptText("Correct Answer (A/B/C/D)");

        Button save = new Button("Save Question");
        Button clear = new Button("Clear");
        Button back = new Button("Back to Dashboard");

        Label status = new Label();

        VBox root = new VBox(10,
                question, op1, op2, op3, op4, correct,
                save, clear, back,
                status
        );

        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        // ================= SAVE =================
        save.setOnAction(e -> {

            try {
                Connection con = DBConnection.getConnection();

                String sql =
                        "INSERT INTO Question(question, option1, option2, option3, option4, correct_answer) VALUES (?,?,?,?,?,?)";

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, question.getText());
                pst.setString(2, op1.getText());
                pst.setString(3, op2.getText());
                pst.setString(4, op3.getText());
                pst.setString(5, op4.getText());
                pst.setString(6, correct.getText());

                pst.executeUpdate();

                status.setText("Question Saved Successfully!");

                con.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                status.setText("Error saving question");
            }
        });

        // ================= CLEAR =================
        clear.setOnAction(e -> {
            question.clear();
            op1.clear();
            op2.clear();
            op3.clear();
            op4.clear();
            correct.clear();
            status.setText("Form Cleared");
        });

        // ================= BACK =================
        back.setOnAction(e -> {
            TeacherDashboard t = new TeacherDashboard();
            t.start(stage);
        });

        // ================= SCENE =================
        stage.setScene(new Scene(root, 400, 450));
        stage.setTitle("Add Question");
        stage.show();
    }
}