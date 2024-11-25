package quiz.backend.Quiz.controller;

import quiz.backend.Quiz.model.QuizTournament;
import quiz.backend.Quiz.services.QuizTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class QuizTournamentController {

    @Autowired
    private QuizTournamentService quizTournamentService;
    private static final Logger logger = LoggerFactory.getLogger(QuizTournamentController.class);

    @PostMapping("/admin/create")
    public ResponseEntity<?> createTournament(@RequestBody QuizTournament tournament) {
        try {
            // Validate input data
            if (tournament.getName() == null || tournament.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("Tournament name is required.");
            }
            if (tournament.getStartDate() == null || tournament.getEndDate() == null) {
                return ResponseEntity.badRequest().body("Start and end dates are required.");
            }
            if (tournament.getStartDate().isAfter(tournament.getEndDate())) {
                return ResponseEntity.badRequest().body("Start date must be before end date.");
            }

            // Save the tournament
            quizTournamentService.createTournament(tournament);
            return ResponseEntity.ok("Quiz tournament created successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the tournament");
        }
    }



    // Admin: Update quiz tournament
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable Long id, @RequestBody QuizTournament tournament) {
        QuizTournament existingTournament = quizTournamentService.getTournamentById(id);
        if (existingTournament == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found.");
        }
        // Update fields
        existingTournament.setName(tournament.getName());
        existingTournament.setCategory(tournament.getCategory());
        existingTournament.setDifficulty(tournament.getDifficulty());
        existingTournament.setStartDate(tournament.getStartDate());
        existingTournament.setEndDate(tournament.getEndDate());
        quizTournamentService.updateTournament(existingTournament);

        return ResponseEntity.ok("Quiz tournament updated successfully");
    }


    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteTournament(@PathVariable Long id) {
        try {
            QuizTournament tournament = quizTournamentService.getTournamentById(id);
            if (tournament == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tournament not found.");
            }

            quizTournamentService.deleteTournament(id);
            return ResponseEntity.ok("Quiz tournament deleted successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the tournament.");
        }
    }


    // View all quiz tournaments
    @GetMapping("/all")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')") // Ensure user has appropriate roles
    public ResponseEntity<List<QuizTournament>> getAllTournaments() {
        List<QuizTournament> tournaments = quizTournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // View likes for each tournament
    @GetMapping("/{id}/likes")
    public ResponseEntity<Integer> getLikes(@PathVariable Long id) {
        QuizTournament tournament = quizTournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament.getLikes());
    }

    // Player: Like a tournament
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeTournament(@PathVariable Long id) {
        quizTournamentService.likeTournament(id);
        return ResponseEntity.ok("Tournament liked");
    }

    // Player: Unlike a tournament
    @PostMapping("/{id}/unlike")
    public ResponseEntity<?> unlikeTournament(@PathVariable Long id) {
        quizTournamentService.unlikeTournament(id);
        return ResponseEntity.ok("Tournament unliked");
    }

    @GetMapping("/ongoing")
    public ResponseEntity<List<QuizTournament>> getOngoingTournaments() {
        try {
            List<QuizTournament> tournaments = quizTournamentService.getOngoingTournaments();
            return ResponseEntity.ok(tournaments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Player: View upcoming tournaments
    @GetMapping("/upcoming")
    public ResponseEntity<List<QuizTournament>> getUpcomingTournaments() {
        List<QuizTournament> tournaments = quizTournamentService.getUpcomingTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // Player: View past tournaments
    @GetMapping("/past")
    public ResponseEntity<List<QuizTournament>> getPastTournaments() {
        List<QuizTournament> tournaments = quizTournamentService.getPastTournaments();
        return ResponseEntity.ok(tournaments);
    }

}
