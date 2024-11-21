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

    // Fetch all trivia questions for a specific tournament
    public List<TriviaQuestion> getAllQuestionsByTournamentId(Long tournamentId) {
        return triviaQuestionRepository.findByQuizTournamentId(tournamentId);
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

    // Delete a quiz tournament
    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    // Fetch trivia questions from the external API and save them to the database
    public List<TriviaQuestion> fetchAndSaveTriviaQuestions(int numberOfQuestions, String category, String difficulty, Long id) {
        QuizTournament quizTournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuizTournament not found with ID: " + id));

        String url = triviaApiUrl + "?amount=" + numberOfQuestions + "&category=" + category + "&difficulty=" + difficulty;
        RestTemplate restTemplate = new RestTemplate();
        TriviaApiResponse response = restTemplate.getForObject(url, TriviaApiResponse.class);

        if (response != null && response.getResults() != null) {
            List<TriviaQuestion> questions = response.getResults();
            for (TriviaQuestion question : questions) {
                question.setQuizTournament(quizTournament);
            }
            triviaQuestionRepository.saveAll(questions);
            return questions;
        }

        return null;
    }
}
