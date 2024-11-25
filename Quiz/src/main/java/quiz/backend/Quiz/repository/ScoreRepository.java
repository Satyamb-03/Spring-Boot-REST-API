package quiz.backend.Quiz.repository;

import quiz.backend.Quiz.model.QuizTournament;
import quiz.backend.Quiz.model.Score;
import quiz.backend.Quiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByQuizTournament(QuizTournament quizTournament);

    List<Score> findByQuizTournament_Id(Long quizTournamentId);

    List<Score> findByUser(User user);

    List<Score> findByUser_Id(Long userId);
}

