package com.example.quiztournament.service;

import com.example.quiztournament.model.QuizTournament;
import com.example.quiztournament.model.TriviaApiResponse;
import com.example.quiztournament.model.TriviaQuestion;
import com.example.quiztournament.repository.QuizTournamentRepository;
import com.example.quiztournament.repository.TriviaQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class QuizTournamentService {

    @Autowired
    private QuizTournamentRepository tournamentRepository;

    @Autowired
    private TriviaQuestionRepository triviaQuestionRepository;

    private final String triviaApiUrl = "https://opentdb.com/api.php";

    // Create a new quiz tournament
    public QuizTournament createTournament(QuizTournament tournament) {
        return tournamentRepository.save(tournament);
    }

    // Get all quiz tournaments
    public List<QuizTournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    // Get tournament by ID
    public QuizTournament getTournamentById(Long id) {
        return tournamentRepository.findById(id).orElse(null);
    }

    // Update a quiz tournament
    public QuizTournament updateTournament(Long id, QuizTournament tournamentDetails) {
        QuizTournament tournament = tournamentRepository.findById(id).orElse(null);
        if (tournament != null) {
            tournament.setName(tournamentDetails.getName());
            tournament.setCategory(tournamentDetails.getCategory());
            tournament.setDifficulty(tournamentDetails.getDifficulty());
            tournament.setStartDate(tournamentDetails.getStartDate());
            tournament.setEndDate(tournamentDetails.getEndDate());
            tournament.setNumberOfQuestions(tournamentDetails.getNumberOfQuestions());
            return tournamentRepository.save(tournament);
        }
        return null;
    }

    public TriviaQuestion saveTriviaQuestion(String quizTournamentId, TriviaQuestion triviaQuestion) {
        // Fetch the QuizTournament by its ID
        QuizTournament quizTournament = tournamentRepository.findById(Long.valueOf(quizTournamentId))
                .orElseThrow(() -> new RuntimeException("Quiz tournament not found"));

        // Set the trivia question's quiz tournament
        triviaQuestion.setQuizTournament(quizTournament);

        // Save the trivia question
        return triviaQuestionRepository.save(triviaQuestion);
    }


    // Delete a quiz tournament
    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    // Fetch trivia questions from the external API and save them to the database
    public List<TriviaQuestion> fetchAndSaveTriviaQuestions(int numberOfQuestions, String category, String difficulty, Long id) {
        // Fetch the associated QuizTournament
        QuizTournament quizTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizTournament not found with ID: " + id));

        // Build the API URL
        String url = triviaApiUrl + "?amount=" + numberOfQuestions + "&category=" + category + "&difficulty=" + difficulty;
        RestTemplate restTemplate = new RestTemplate();
        TriviaApiResponse response = restTemplate.getForObject(url, TriviaApiResponse.class);

        if (response != null && response.getResults() != null) {
            List<TriviaQuestion> questions = response.getResults();

            // Set the QuizTournament for each TriviaQuestion
            for (TriviaQuestion question : questions) {
                question.setQuizTournament(quizTournament); // Associate with the QuizTournament
            }

            // Save the questions to the database
            triviaQuestionRepository.saveAll(questions);
            return questions;
        }

        return null; // Return null if no questions are fetched
    }

}