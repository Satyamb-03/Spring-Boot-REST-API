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

    // Save a new trivia question with the related quiz tournament
    public TriviaQuestion saveTriviaQuestion(TriviaQuestion triviaQuestion, Long quizTournamentId) {
        // Fetch the QuizTournament by its ID
        Optional<QuizTournament> quizTournamentOptional = quizTournamentRepository.findById(quizTournamentId);

        if (quizTournamentOptional.isPresent()) {
            // Set the quiz tournament for the trivia question
            TriviaQuestion savedQuestion = triviaQuestion;
            savedQuestion.setQuizTournament(quizTournamentOptional.get());
            return triviaQuestionRepository.save(savedQuestion); // Save and return the question
        } else {
            throw new RuntimeException("Quiz tournament not found");
        }
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

        triviaQuestion.setCategory(triviaQuestionDetails.getCategory());
        triviaQuestion.setDifficulty(triviaQuestionDetails.getDifficulty());
        triviaQuestion.setQuestion(triviaQuestionDetails.getQuestion());
        triviaQuestion.setCorrectAnswer(triviaQuestionDetails.getCorrectAnswer());
        triviaQuestion.setIncorrectAnswers(triviaQuestionDetails.getIncorrectAnswers());
        triviaQuestion.setQuizTournament(triviaQuestion.getQuizTournament());

        return triviaQuestionRepository.save(triviaQuestion);
    }
}