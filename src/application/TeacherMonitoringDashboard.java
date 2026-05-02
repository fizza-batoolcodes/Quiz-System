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

public class TeacherMonitoringDashboard {

    public void start(Stage stage) {

        Label title = new Label("Teacher Results Dashboard");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<ResultRow> table = new TableView<>();

        // ================= COLUMNS (IMPORTANT FIX) =================

        TableColumn<ResultRow, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell ->
                cell.getValue().emailProperty()
        );

        TableColumn<ResultRow, Number> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(cell ->
                cell.getValue().scoreProperty()
        );

        TableColumn<ResultRow, Number> bonusCol = new TableColumn<>("Bonus");
        bonusCol.setCellValueFactory(cell ->
                cell.getValue().bonusProperty()
        );

        TableColumn<ResultRow, Number> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(cell ->
                cell.getValue().totalProperty()
        );

        table.getColumns().addAll(emailCol, scoreCol, bonusCol, totalCol);

        // ================= LOAD DATA =================

        ObservableList<ResultRow> data = FXCollections.observableArrayList();

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM leaderboard");

            while (rs.next()) {

                data.add(new ResultRow(
                        rs.getString("student_email"),
                        rs.getInt("score"),
                        rs.getInt("bonus"),
                        rs.getInt("total")
                ));
            }

            table.setItems(data);

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ================= BACK BUTTON =================

        Button back = new Button("Back");
        back.setOnAction(e -> {
            new TeacherDashboard().start(stage);
        });

        VBox root = new VBox(10, title, table, back);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 600, 400);

        stage.setScene(scene);
        stage.setTitle("Results");
        stage.show();
    }
}