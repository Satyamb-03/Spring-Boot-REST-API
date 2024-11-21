package com.example.quiztournament.service;

import com.example.quiztournament.model.TriviaQuestion;
import com.example.quiztournament.model.QuizTournament;
import com.example.quiztournament.repository.TriviaQuestionRepository;
import com.example.quiztournament.repository.QuizTournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TriviaQuestionService {

    @Autowired
    private TriviaQuestionRepository triviaQuestionRepository;

    @Autowired
    private QuizTournamentRepository quizTournamentRepository;

    public TriviaQuestion saveTriviaQuestion(TriviaQuestion triviaQuestion, Long quizTournamentId) {
        if (quizTournamentId == null) {
            throw new IllegalArgumentException("Quiz tournament ID cannot be null");
        }

        // Fetch the QuizTournament by its ID
        QuizTournament quizTournament = quizTournamentRepository.findById(quizTournamentId)
                .orElseThrow(() -> new RuntimeException("Quiz tournament not found with ID: " + quizTournamentId));

        // Set the relationship
        triviaQuestion.setQuizTournament(quizTournament);

        // Save the trivia question
        return triviaQuestionRepository.save(triviaQuestion);
    }


    // Fetch all trivia questions
    public List<TriviaQuestion> getAllTriviaQuestions() {
        return triviaQuestionRepository.findAll();
    }

    // Fetch trivia questions by QuizTournament ID
    public List<TriviaQuestion> getTriviaQuestionsByTournamentId(Long tournamentId) {
        return triviaQuestionRepository.findByQuizTournamentId(tournamentId);
    }

    // Fetch a single trivia question by its ID
    public TriviaQuestion getTriviaQuestionById(Long id) {
        return triviaQuestionRepository.findById(id).orElseThrow(() -> new RuntimeException("Trivia question not found"));
    }

    // Delete a trivia question
    public void deleteTriviaQuestion(Long id) {
        triviaQuestionRepository.deleteById(id);
    }

    // Update a trivia question
    public TriviaQuestion updateTriviaQuestion(Long id, TriviaQuestion triviaQuestionDetails) {
        TriviaQuestion triviaQuestion = triviaQuestionRepository.findById(id).orElseThrow(() -> new RuntimeException("Trivia question not found"));
        triviaQuestion.setId(triviaQuestion.getId());
        triviaQuestion.setCategory(triviaQuestionDetails.getCategory());
        triviaQuestion.setDifficulty(triviaQuestionDetails.getDifficulty());
        triviaQuestion.setQuestion(triviaQuestionDetails.getQuestion());
        triviaQuestion.setCorrectAnswer(triviaQuestionDetails.getCorrectAnswer());
        triviaQuestion.setIncorrectAnswers(triviaQuestionDetails.getIncorrectAnswers());
        triviaQuestion.setQuizTournament(triviaQuestion.getQuizTournament());

        return triviaQuestionRepository.save(triviaQuestion);
    }
}