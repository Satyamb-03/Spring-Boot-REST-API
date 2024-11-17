package com.example.quiztournament.controller;

import com.example.quiztournament.model.QuizTournament;
import com.example.quiztournament.model.TriviaQuestion;
import com.example.quiztournament.service.QuizTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class QuizTournamentController {

    @Autowired
    private QuizTournamentService tournamentService;

    // Create a new quiz tournament
    @PostMapping
    public ResponseEntity<QuizTournament> createTournament(@RequestBody QuizTournament tournament) {
        QuizTournament createdTournament = tournamentService.createTournament(tournament);
        return new ResponseEntity<>(createdTournament, HttpStatus.CREATED);
    }

    // Get all quiz tournaments
    @GetMapping
    public ResponseEntity<List<QuizTournament>> getAllTournaments() {
        List<QuizTournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // Get a specific tournament by ID
    @GetMapping("/{id}")
    public ResponseEntity<QuizTournament> getTournamentById(@PathVariable Long id) {
        QuizTournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }

    // Fetch trivia questions for a specific tournament
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<TriviaQuestion>> getTriviaQuestions(
            @PathVariable Long id,
            @RequestParam int numberOfQuestions,
            @RequestParam String category,
            @RequestParam String difficulty) {
        QuizTournament tournament = tournamentService.getTournamentById(id);
        if (tournament != null) {
                List<TriviaQuestion> questions = tournamentService.fetchTriviaQuestions(numberOfQuestions, category, difficulty);
            return ResponseEntity.ok(questions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update a quiz tournament
    @PutMapping("/{id}")
    public ResponseEntity<QuizTournament> updateTournament(@PathVariable Long id, @RequestBody QuizTournament tournament) {
        QuizTournament updatedTournament = tournamentService.updateTournament(id, tournament);
        return ResponseEntity.ok(updatedTournament);
    }

    // Delete a quiz tournament
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}
