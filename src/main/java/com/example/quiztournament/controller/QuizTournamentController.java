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
        if (tournament != null) {
            return ResponseEntity.ok(tournament);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Fetch and save trivia questions for a specific tournament
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<TriviaQuestion>> fetchQuestionsForTournament(@PathVariable Long id) {
        QuizTournament tournament = tournamentService.getTournamentById(id);
        if (tournament != null) {
            List<TriviaQuestion> questions = tournamentService.fetchAndSaveTriviaQuestions(
                    tournament.getNumberOfQuestions(),
                    tournament.getCategory(),
                    tournament.getDifficulty(),
                    tournament.getId()
            );
            return ResponseEntity.ok(questions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Fetch all trivia questions for a specific tournament
    @GetMapping("/{id}/questions/all")
    public ResponseEntity<List<TriviaQuestion>> getAllQuestionsForTournament(@PathVariable Long id) {
        List<TriviaQuestion> questions = tournamentService.getAllQuestionsByTournamentId(id);
        if (!questions.isEmpty()) {
            return ResponseEntity.ok(questions);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update a quiz tournament
    @PutMapping("/{id}")
    public ResponseEntity<QuizTournament> updateTournament(@PathVariable Long id, @RequestBody QuizTournament tournament) {
        QuizTournament updatedTournament = tournamentService.updateTournament(id, tournament);
        if (updatedTournament != null) {
            return ResponseEntity.ok(updatedTournament);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete a quiz tournament
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}
