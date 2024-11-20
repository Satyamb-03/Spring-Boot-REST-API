package com.example.quiztournament.controller;

import com.example.quiztournament.model.TriviaQuestion;
import com.example.quiztournament.service.QuizTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trivia-questions")
public class TriviaQuestionController {

    @Autowired
    private QuizTournamentService triviaQuestionService;

    @PostMapping("/add")
    public ResponseEntity<TriviaQuestion> addTriviaQuestion(@RequestParam String quizTournamentId,
                                                            @RequestBody TriviaQuestion triviaQuestion) {
        // Call the service method to save the trivia question
        TriviaQuestion savedTriviaQuestion = triviaQuestionService.saveTriviaQuestion(quizTournamentId, triviaQuestion);

        // Return the saved trivia question as a response
        return ResponseEntity.ok(savedTriviaQuestion);
    }
}