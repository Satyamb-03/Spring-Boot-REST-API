package com.example.quiztournament.controller;

import com.example.quiztournament.model.TriviaQuestion;
import com.example.quiztournament.service.TriviaQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trivia-questions")
public class TriviaQuestionController {

    @Autowired
    private TriviaQuestionService triviaQuestionService; // Use TriviaQuestionService

    // Endpoint to add a new trivia question to a specific tournament
    @PostMapping("/add")
    public ResponseEntity<TriviaQuestion> addTriviaQuestion(@RequestParam Long quizTournamentId, // Expect Long type
                                                            @RequestBody TriviaQuestion triviaQuestion) {
        // Call the service method to save the trivia question
        TriviaQuestion savedTriviaQuestion = triviaQuestionService.saveTriviaQuestion(triviaQuestion, quizTournamentId);

        // Return the saved trivia question as a response
        return ResponseEntity.ok(savedTriviaQuestion);
    }
}
