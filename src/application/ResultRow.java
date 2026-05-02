package application;

import javafx.beans.property.*;

public class ResultRow {

    private StringProperty email;
    private IntegerProperty score;
    private IntegerProperty bonus;
    private IntegerProperty total;

    public ResultRow(String email, int score, int bonus, int total) {
        this.email = new SimpleStringProperty(email);
        this.score = new SimpleIntegerProperty(score);
        this.bonus = new SimpleIntegerProperty(bonus);
        this.total = new SimpleIntegerProperty(total);
    }

    public StringProperty emailProperty() { return email; }
    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty bonusProperty() { return bonus; }
    public IntegerProperty totalProperty() { return total; }
}