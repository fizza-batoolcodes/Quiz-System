package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BehaviourLogsView {

    public void start(Stage stage) {

        Label title = new Label("Behaviour Logs");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<LogRow> table = new TableView<>();

        // ===== Columns =====
        TableColumn<LogRow, Number> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(data -> data.getValue().studentIdProperty());

        TableColumn<LogRow, String> tokenCol = new TableColumn<>("Session");
        tokenCol.setCellValueFactory(data -> data.getValue().sessionTokenProperty());

        TableColumn<LogRow, Number> tabCol = new TableColumn<>("Tab Switches");
        tabCol.setCellValueFactory(data -> data.getValue().tabSwitchProperty());

        TableColumn<LogRow, Number> copyCol = new TableColumn<>("Copy Attempts");
        copyCol.setCellValueFactory(data -> data.getValue().copyProperty());

        table.getColumns().addAll(idCol, tokenCol, tabCol, copyCol);

        // ===== Load Data =====
        ObservableList<LogRow> list = FXCollections.observableArrayList();

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM monitoring_logs");

            while (rs.next()) {

                list.add(new LogRow(
                        rs.getInt("student_id"),
                        rs.getString("session_token"),
                        rs.getInt("tab_switches"),
                        rs.getInt("copy_attempts")
                ));
            }

            table.setItems(list);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ===== Back Button =====
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            new TeacherDashboard().start(stage);
        });

        VBox root = new VBox(10, title, table, backBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 600, 400);

        stage.setScene(scene);
        stage.setTitle("Behaviour Logs");
        stage.show();
    }
}