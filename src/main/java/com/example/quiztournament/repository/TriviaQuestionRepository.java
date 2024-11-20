package com.example.quiztournament.repository;

import com.example.quiztournament.model.TriviaQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TriviaQuestionRepository extends JpaRepository<TriviaQuestion, Long> {
    List<TriviaQuestion> findByQuizTournamentId(Long tournamentId);
}
