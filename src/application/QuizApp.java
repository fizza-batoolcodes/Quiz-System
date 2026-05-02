package application;

import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class QuizApp {

    ArrayList<Question> questions = new ArrayList<>();

    int index = 0;
    int score = 0;
    int bonus = 0;

    int time = 15;
    int maxTime = 15;

    Timeline timeline;

    String studentEmail;
    MonitoringManager monitor = new MonitoringManager();

    public QuizApp(String email) {
        this.studentEmail = email;
    }

    public void start(Stage stage) {

        Label title = new Label("Quiz Dashboard");

        ComboBox<String> difficulty = new ComboBox<>();
        difficulty.getItems().addAll("Easy", "Medium", "Hard");
        difficulty.setValue("Medium");

        Button startBtn = new Button("Start Quiz");
        Button backBtn = new Button("Back To Dashboard");

        VBox startRoot = new VBox(10, title, difficulty, startBtn, backBtn);
        startRoot.setAlignment(Pos.CENTER);

        Scene startScene = new Scene(startRoot, 500, 400);

        Label numberLabel = new Label();
        Label timerLabel = new Label();
        ProgressBar bar = new ProgressBar(1);

        Label questionLabel = new Label();

        RadioButton op1 = new RadioButton();
        RadioButton op2 = new RadioButton();
        RadioButton op3 = new RadioButton();
        RadioButton op4 = new RadioButton();

        ToggleGroup group = new ToggleGroup();
        op1.setToggleGroup(group);
        op2.setToggleGroup(group);
        op3.setToggleGroup(group);
        op4.setToggleGroup(group);

        Button nextBtn = new Button("Next");

        VBox quizRoot = new VBox(10,
                numberLabel,
                timerLabel,
                bar,
                questionLabel,
                op1, op2, op3, op4,
                nextBtn
        );

        quizRoot.setAlignment(Pos.CENTER);

        Scene quizScene = new Scene(quizRoot, 550, 450);

        Label resultLabel = new Label();
        Button dashboardBtn = new Button("Go To Dashboard");
        Button restartBtn = new Button("Restart Quiz");

        VBox resultRoot = new VBox(10, resultLabel, dashboardBtn, restartBtn);
        resultRoot.setAlignment(Pos.CENTER);

        Scene resultScene = new Scene(resultRoot, 500, 400);

        loadQuestions();

        Runnable loadQuestion = () -> {

            if (questions.isEmpty()) {
                resultLabel.setText("No Questions Found!");
                stage.setScene(resultScene);
                return;
            }

            if (index >= questions.size()) {

                if (timeline != null) timeline.stop();

                int total = score + bonus;

                saveResult(total);

                monitor.saveLogs(
                        SessionManager.getStudentId(),
                        SessionManager.getToken()
                );

                resultLabel.setText(
                        "Quiz Finished\n\n" +
                                "Score: " + score +
                                "\nBonus: " + bonus +
                                "\nTotal: " + total
                );

                stage.setScene(resultScene);
                return;
            }

            Question q = questions.get(index);

            numberLabel.setText("Question " + (index + 1) + " / " + questions.size());
            questionLabel.setText(q.getQuestion());

            op1.setText(q.getOp1());
            op2.setText(q.getOp2());
            op3.setText(q.getOp3());
            op4.setText(q.getOp4());

            group.selectToggle(null);

            time = maxTime;
            timerLabel.setText("Time: " + time);
            bar.setProgress(1);

            if (timeline != null) timeline.stop();

            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

                time--;
                timerLabel.setText("Time: " + time);
                bar.setProgress((double) time / maxTime);

                if (time <= 5) Toolkit.getDefaultToolkit().beep();

                if (time <= 0) nextBtn.fire();

            }));

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        };

        startBtn.setOnAction(e -> {

            String level = difficulty.getValue();

            maxTime = level.equals("Easy") ? 20 :
                    level.equals("Medium") ? 15 : 10;

            stage.setScene(quizScene);

            monitor.startMonitoring(stage, quizScene);

            loadQuestion.run();
        });

        nextBtn.setOnAction(e -> {

            if (timeline != null) timeline.stop();

            RadioButton selected = (RadioButton) group.getSelectedToggle();

            if (selected != null) {

                String answer = "";
                String correct = questions.get(index).getCorrect().trim();

                if (selected == op1) answer = "A";
                else if (selected == op2) answer = "B";
                else if (selected == op3) answer = "C";
                else if (selected == op4) answer = "D";

                if (answer.equalsIgnoreCase(correct)) {

                    if (time <= 5) {
                        score += 2;
                        bonus++;
                    } else {
                        score++;
                    }
                }
            }

            index++;
            loadQuestion.run();
        });

        restartBtn.setOnAction(e -> {

            index = 0;
            score = 0;
            bonus = 0;

            questions.clear();
            loadQuestions();

            stage.setScene(startScene);
        });

        dashboardBtn.setOnAction(e -> {
            new StudentDashboard(studentEmail).start(stage);
        });

        backBtn.setOnAction(e -> {
            new StudentDashboard(studentEmail).start(stage);
        });

        stage.setScene(startScene);
        stage.setTitle("Quiz System");
        stage.show();
    }

    void loadQuestions() {

        try {
            Connection con = DBConnection.getConnection();

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT * FROM Question ORDER BY RAND() LIMIT 10"
            );

            while (rs.next()) {

                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getString("correct_answer")
                ));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveResult(int total) {

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO leaderboard(student_email, score, bonus, total) VALUES (?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, studentEmail);
            pst.setInt(2, score);
            pst.setInt(3, bonus);
            pst.setInt(4, total);

            pst.executeUpdate();

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}