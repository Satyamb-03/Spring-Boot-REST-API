package quiz.backend.Quiz.services;

import quiz.backend.Quiz.OpenTDBQuestion;
import quiz.backend.Quiz.OpenTDBResponse;
import quiz.backend.Quiz.model.Question;
import quiz.backend.Quiz.model.QuizTournament;
import quiz.backend.Quiz.model.Score;
import quiz.backend.Quiz.model.User;
import quiz.backend.Quiz.repository.QuestionRepository;
import quiz.backend.Quiz.repository.QuizTournamentRepository;
import quiz.backend.Quiz.repository.ScoreRepository;
import quiz.backend.Quiz.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class QuizTournamentService {

    @Autowired
    private QuizTournamentRepository quizTournamentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public QuizTournament createTournament(QuizTournament tournament) {
        // Step 1: Save the tournament to generate an ID
        tournament = quizTournamentRepository.save(tournament);
        System.out.println("Tournament saved with ID: " + tournament.getId());

        // Step 2: Fetch questions from OpenTDB API
        List<Question> questions = fetchQuestionsFromOpenTDB(tournament.getCategory(), tournament.getDifficulty());
        if (questions.isEmpty()) {
            System.out.println("No questions fetched from OpenTDB.");
        } else {
            System.out.println("Fetched " + questions.size() + " questions from OpenTDB.");
        }

        // Step 3: Assign fetched questions to the tournament and save each question
        for (Question question : questions) {
            question.setQuizTournament(tournament);
            questionRepository.save(question);
            System.out.println("Saved question: " + question.getQuestionText() + " with ID: " + question.getId());
        }

        // Step 4: Update the tournament with the questions
        tournament.setQuestions(new HashSet<>(questions));
        tournament = quizTournamentRepository.save(tournament);
        System.out.println("Tournament updated with questions.");

        // Step 5: Notify users about the new tournament
        List<User> users = userRepository.findAll();
        emailService.sendTournamentNotification(users, tournament.getName());

        return tournament;
    }

    // Helper method to fetch questions from OpenTDB API
    private List<Question> fetchQuestionsFromOpenTDB(String category, String difficulty) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://opentdb.com/api.php?amount=10";

        if (category != null && !category.isEmpty()) {
            url += "&category=" + category;
        }

        if (difficulty != null && !difficulty.isEmpty()) {
            url += "&difficulty=" + difficulty;
        }

        OpenTDBResponse response = restTemplate.getForObject(url, OpenTDBResponse.class);

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            System.out.println("OpenTDB API returned no questions.");
            return new ArrayList<>();
        }

        List<Question> questions = new ArrayList<>();
        for (OpenTDBQuestion apiQuestion : response.getResults()) {
            Question question = new Question();
            question.setQuestionText(apiQuestion.getQuestionText());
            question.setCorrectAnswer(apiQuestion.getCorrectAnswer());

            List<String> choices = new ArrayList<>(apiQuestion.getIncorrectAnswers());
            choices.add(apiQuestion.getCorrectAnswer());
            Collections.shuffle(choices);
            question.setChoices(choices);

            questions.add(question);
        }
        return questions;
    }

    public List<Question> getQuestionsByTournamentId(Long quizId) {
        return questionRepository.findByQuizTournamentId(quizId);
    }

    // Method to get a tournament by ID
    public QuizTournament getTournamentById(Long id) {
        return quizTournamentRepository.findById(id).orElse(null);
    }

    // Method to get all tournaments
    public List<QuizTournament> getAllTournaments() {
        return quizTournamentRepository.findAll();
    }

    // Method to update an existing tournament
    public QuizTournament updateTournament(QuizTournament tournament) {
        return quizTournamentRepository.save(tournament);
    }

    // Method to delete a tournament
    @Transactional
    public void deleteTournament(Long id) {
        QuizTournament tournament = quizTournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        // Delete associated questions
        List<Question> questions = questionRepository.findByQuizTournamentId(id);
        questionRepository.deleteAll(questions);

        // Delete associated scores
        List<Score> scores = scoreRepository.findByQuizTournament_Id(id);
        scoreRepository.deleteAll(scores);

        // Clear associations to avoid persistence issues
        tournament.getQuestions().clear();
        tournament.getScores().clear();

        // Finally, delete the tournament
        quizTournamentRepository.deleteById(id);
    }




    // Method to get ongoing tournaments
    public List<QuizTournament> getOngoingTournaments() {
        LocalDateTime now = LocalDateTime.now();
        return quizTournamentRepository.findByStartDateBeforeAndEndDateAfter(now, now);
    }

    // Method to get upcoming tournaments
    public List<QuizTournament> getUpcomingTournaments() {
        LocalDateTime now = LocalDateTime.now();
        return quizTournamentRepository.findByStartDateAfter(now);
    }

    // Method to get past tournaments
    public List<QuizTournament> getPastTournaments() {
        LocalDateTime now = LocalDateTime.now();
        return quizTournamentRepository.findByEndDateBefore(now);
    }

    // Method to like a tournament
    public void likeTournament(Long id) {
        QuizTournament tournament = quizTournamentRepository.findById(id).orElse(null);
        if (tournament != null) {
            tournament.setLikes(tournament.getLikes() + 1);
            quizTournamentRepository.save(tournament);
        }
    }

    // Method to unlike a tournament
    public void unlikeTournament(Long id) {
        QuizTournament tournament = quizTournamentRepository.findById(id).orElse(null);
        if (tournament != null && tournament.getLikes() > 0) {
            tournament.setLikes(tournament.getLikes() - 1);
            quizTournamentRepository.save(tournament);
        }
    }
}
