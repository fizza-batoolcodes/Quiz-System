package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TeacherResults {

    public void start(Stage stage) {

        TableView<ResultRow> table = new TableView<>();

        TableColumn<ResultRow, String> emailCol = new TableColumn<>("Student Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<ResultRow, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> data.getValue().scoreProperty().asObject());

        TableColumn<ResultRow, Integer> bonusCol = new TableColumn<>("Bonus");
        bonusCol.setCellValueFactory(data -> data.getValue().bonusProperty().asObject());

        TableColumn<ResultRow, Integer> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(data -> data.getValue().totalProperty().asObject());

        table.getColumns().addAll(emailCol, scoreCol, bonusCol, totalCol);

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

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(data);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new TeacherDashboard().start(stage));

        VBox root = new VBox(10, table, backBtn);

        stage.setScene(new Scene(root, 700, 400));
        stage.setTitle("Teacher Results");
        stage.show();
    }
}