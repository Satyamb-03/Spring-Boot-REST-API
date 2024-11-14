package com.example.quiztournament.service;

import com.example.quiztournament.model.QuizTournament;
import com.example.quiztournament.repository.QuizTournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizTournamentService {

    @Autowired
    private QuizTournamentRepository tournamentRepository;

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
            // Update additional fields as needed
            return tournamentRepository.save(tournament);
        }
        return null; // Or throw an exception
    }

    // Delete a quiz tournament
    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }
}