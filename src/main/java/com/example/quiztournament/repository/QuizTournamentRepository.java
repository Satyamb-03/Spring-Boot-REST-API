package com.example.quiztournament.repository;

import com.example.quiztournament.model.QuizTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizTournamentRepository extends JpaRepository<QuizTournament, Long> {

    // Custom query method to find a tournament by name
    QuizTournament findByName(String name);
}