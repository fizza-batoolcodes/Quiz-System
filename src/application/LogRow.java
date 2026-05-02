package application;

import javafx.beans.property.*;

public class LogRow {

    private IntegerProperty studentId;
    private StringProperty sessionToken;
    private IntegerProperty tabSwitch;
    private IntegerProperty copy;

    public LogRow(int studentId, String sessionToken, int tabSwitch, int copy) {
        this.studentId = new SimpleIntegerProperty(studentId);
        this.sessionToken = new SimpleStringProperty(sessionToken);
        this.tabSwitch = new SimpleIntegerProperty(tabSwitch);
        this.copy = new SimpleIntegerProperty(copy);
    }

    public IntegerProperty studentIdProperty() { return studentId; }
    public StringProperty sessionTokenProperty() { return sessionToken; }
    public IntegerProperty tabSwitchProperty() { return tabSwitch; }
    public IntegerProperty copyProperty() { return copy; }
}