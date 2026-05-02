package application;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MonitoringManager {

    private int tabSwitchCount = 0;
    private int copyCount = 0;

    // START MONITORING
    public void startMonitoring(Stage stage, Scene scene) {

        // ================= TAB SWITCH DETECTION =================
        stage.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                tabSwitchCount++;
                System.out.println("Tab switched: " + tabSwitchCount);
            }
        });

        // ================= COPY / CHEAT DETECTION =================
        scene.setOnKeyPressed(event -> {

            if (event.isControlDown()) {

                switch (event.getCode()) {

                    case C:   // Copy
                    case V:   // Paste
                    case X:   // Cut
                    case A:   // Select All (VERY IMPORTANT ADD)

                        copyCount++;
                        System.out.println("Copy-related action: " + copyCount);
                        break;
                }
            }
        });
    }

    // ================= SAVE TO DATABASE =================
    public void saveLogs(int studentId, String sessionToken) {

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO monitoring_logs(student_id, session_token, tab_switches, copy_attempts) VALUES (?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, studentId);
            pst.setString(2, sessionToken);
            pst.setInt(3, tabSwitchCount);
            pst.setInt(4, copyCount);

            pst.executeUpdate();

            System.out.println("Monitoring logs saved!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= RESET =================
    public void reset() {
        tabSwitchCount = 0;
        copyCount = 0;
    }

    // ================= GETTERS =================
    public int getTabSwitchCount() {
        return tabSwitchCount;
    }

    public int getCopyCount() {
        return copyCount;
    }
}