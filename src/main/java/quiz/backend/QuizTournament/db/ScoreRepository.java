package quiz.backend.QuizTournament.db;

import quiz.backend.QuizTournament.models.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
