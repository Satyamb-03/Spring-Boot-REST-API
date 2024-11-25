package quiz.backend.Quiz.controller;

import quiz.backend.Quiz.model.Question;
import quiz.backend.Quiz.model.QuizTournament;
import quiz.backend.Quiz.model.User;
import quiz.backend.Quiz.model.Score;
import quiz.backend.Quiz.services.QuizTournamentService;
import quiz.backend.Quiz.services.UserService;
import quiz.backend.Quiz.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/participation")
public class QuizParticipationController {

    @Autowired
    private QuizTournamentService quizTournamentService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private UserService userService;

    // Player: Participate in an ongoing tournament
    @GetMapping("/tournament/{id}/start")
    public ResponseEntity<?> startQuiz(@PathVariable Long id) {
        QuizTournament tournament = quizTournamentService.getTournamentById(id);

        if (tournament == null) {
            return ResponseEntity.status(404).body("Tournament not found");
        }

        // Fetch all questions for the given tournament ID
        List<Question> questions = quizTournamentService.getQuestionsByTournamentId(id);

        if (questions.isEmpty()) {
            return ResponseEntity.status(404).body("No questions found for the tournament");
        }

        // Debug logs
        System.out.println("Fetched Tournament: " + tournament.getName());
        questions.forEach(q -> System.out.println("Question: " + q.getQuestionText()));

        return ResponseEntity.ok(questions);
    }


    @PostMapping("/tournament/{id}/submit")
    public ResponseEntity<?> submitQuiz(@PathVariable Long id, @RequestBody Map<String, Object> payload, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }

        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        QuizTournament tournament = quizTournamentService.getTournamentById(id);
        if (tournament == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found.");
        }

        List<Question> questions = new ArrayList<>(tournament.getQuestions());
        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No questions found for this tournament.");
        }

        List<String> answers = (List<String>) payload.get("answers");
        if (answers == null || answers.size() != questions.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid number of answers provided.");
        }

        int scoreValue = calculateScore(questions, answers);

        Score score = new Score();
        score.setUser(user);
        score.setQuizTournament(tournament);
        score.setScore(scoreValue);
        score.setCompletedDate(LocalDateTime.now());

        scoreService.saveScore(score);

        return ResponseEntity.ok("Your score is: " + scoreValue + "/" + questions.size());
    }



    // Helper method to calculate score
    private int calculateScore(List<Question> questions, List<String> answers) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            System.out.println("Comparing Question: " + questions.get(i).getQuestionText()
                    + " | Correct Answer: " + questions.get(i).getCorrectAnswer()
                    + " | Provided Answer: " + answers.get(i));
            if (questions.get(i).getCorrectAnswer().equalsIgnoreCase(answers.get(i))) {
                score++;
            }
        }
        System.out.println("Total Score: " + score);
        return score;
    }




}
