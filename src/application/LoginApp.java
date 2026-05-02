package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginApp {

    public void start(Stage stage) {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        grid.setStyle("-fx-background-color: #ffe4ec;");

        Label title = new Label("Login System");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #5a3e4d;");

        Label emailLabel = new Label("Email:");
        Label passwordLabel = new Label("Password:");
        Label roleLabel = new Label("Role:");

        emailLabel.setStyle("-fx-text-fill:#5a3e4d;");
        passwordLabel.setStyle("-fx-text-fill:#5a3e4d;");
        roleLabel.setStyle("-fx-text-fill:#5a3e4d;");

        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Student", "Teacher");

        String btnStyle =
                "-fx-background-color: #ff85a2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;";

        Button loginBtn = new Button("Login");
        Button clearBtn = new Button("Clear");

        loginBtn.setStyle(btnStyle);
        clearBtn.setStyle(btnStyle);

        HBox buttons = new HBox(10, loginBtn, clearBtn);
        buttons.setAlignment(Pos.CENTER);

        Label status = new Label();
        status.setStyle("-fx-text-fill:#5a3e4d;");

        grid.add(title, 0, 0, 2, 1);
        grid.add(emailLabel, 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(roleLabel, 0, 3);
        grid.add(roleBox, 1, 3);
        grid.add(buttons, 0, 4, 2, 1);
        grid.add(status, 0, 5, 2, 1);

        // CLEAR BUTTON
        clearBtn.setOnAction(e -> {
            emailField.clear();
            passwordField.clear();
            roleBox.setValue(null);
            status.setText("Cleared");
        });

        // LOGIN BUTTON (DEBUG VERSION)
        loginBtn.setOnAction(e -> {

            System.out.println("LOGIN CLICKED");

            String email = emailField.getText();
            String password = passwordField.getText();
            String role = roleBox.getValue();

            if (email.isEmpty() || password.isEmpty() || role == null) {
                status.setText("Fill all fields");
                return;
            }

            try {
                System.out.println("Connecting DB...");

                Connection con = DBConnection.getConnection();

                if (con == null) {
                    System.out.println("DB CONNECTION IS NULL");
                    status.setText("DB Connection Failed");
                    return;
                }

                String table = role.equals("Student") ? "students" : "teachers";
                String sql = "SELECT * FROM " + table + " WHERE email=? AND password=?";

                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, email);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    System.out.println("LOGIN SUCCESS");

                    String name = rs.getString("name");
                    status.setText("Welcome " + name);

                    int studentId = rs.getInt("id");

                    if (role.equals("Student")) {

                        System.out.println("OPENING STUDENT DASHBOARD");

                        SessionManager.createSession(studentId);

                        new StudentDashboard(email).start(stage);

                    } else {

                        System.out.println("OPENING TEACHER DASHBOARD");

                        new TeacherDashboard().start(stage);
                    }

                } else {
                    System.out.println("INVALID LOGIN");
                    status.setText("Invalid login");
                }

                con.close();

            } catch (Exception ex) {
                System.out.println("ERROR OCCURRED:");
                ex.printStackTrace();
                status.setText("DB Error");
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
}