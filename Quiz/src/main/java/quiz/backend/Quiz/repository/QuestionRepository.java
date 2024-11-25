package quiz.backend.Quiz.repository;

import quiz.backend.Quiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q WHERE q.quizTournament.id = :quizId")
    List<Question> findByQuizTournamentId(@Param("quizId") Long quizId);

}
