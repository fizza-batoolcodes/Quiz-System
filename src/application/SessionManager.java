package application;

import java.util.UUID;

public class SessionManager {

    private static String sessionToken;
    private static int studentId;

    // Create session when student logs in
    public static void createSession(int id) {

        studentId = id;
        sessionToken = UUID.randomUUID().toString();

        System.out.println("Session Created: " + sessionToken);
    }

    public static String getToken() {
        return sessionToken;
    }

    public static int getStudentId() {
        return studentId;
    }

    public static void clearSession() {
        sessionToken = null;
        studentId = 0;
    }
}
