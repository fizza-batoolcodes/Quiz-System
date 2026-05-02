package application;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentResultView {

    String email;

    public StudentResultView(String email) {
        this.email = email;
    }

    public void start(Stage stage) {

        TextArea area = new TextArea();
        area.setEditable(false);

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM student_answers WHERE student_email=?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;

                area.appendText(
                        "Question: " + rs.getString("question") + "\n" +
                                "Your Answer: " + rs.getString("selected_answer") + "\n" +
                                "Correct Answer: " + rs.getString("correct_answer") + "\n" +
                                "Result: " + (rs.getBoolean("is_correct") ? "✔ CORRECT" : "❌ WRONG") +
                                "\n--------------------------------\n"
                );
            }

            if (!found) {
                area.setText("No results found for this user.");
            }

            con.close();

        } catch (Exception e) {
            area.setText("Error loading results: " + e.getMessage());
        }

        VBox root = new VBox(area);

        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("My Results");
        stage.show();
    }
}