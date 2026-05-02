package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewResults {

    String email;

    public ViewResults(String email) {
        this.email = email;
    }

    public void start(Stage stage) {

        Label title = new Label("My Results");

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setPrefHeight(300);

        Button backBtn = new Button("Back");

        VBox root = new VBox(10, title, area, backBtn);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 500, 400);

        // Load Results
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT * FROM leaderboard WHERE student_email='"
                            + email + "'"
            );

            while (rs.next()) {

                area.appendText(
                        "Score: " + rs.getInt("score") +
                                "   Bonus: " + rs.getInt("bonus") +
                                "   Total: " + rs.getInt("total") +
                                "\n"
                );
            }

            con.close();

        } catch (Exception e) {
            area.setText("Error Loading Results");
            e.printStackTrace();
        }

        // Back button
        backBtn.setOnAction(e -> {
            StudentDashboard s =
                    new StudentDashboard(email);
            s.start(stage);
        });

        stage.setTitle("Results");
        stage.setScene(scene);
        stage.show();
    }
}