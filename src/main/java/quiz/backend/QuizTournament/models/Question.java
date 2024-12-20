package quiz.backend.QuizTournament.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText; // The question text from OpenTDB

    @ElementCollection
    private List<String> options; // Stores options (correct and incorrect answers)

    private String correctAnswer; // Stores the correct answer

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz; // Link this question to a specific quiz

    public Question(Long id, String questionText, List<String> options, String correctAnswer, Quiz quiz) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.quiz = quiz;
    }

    public Question() {

    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    //public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
}
