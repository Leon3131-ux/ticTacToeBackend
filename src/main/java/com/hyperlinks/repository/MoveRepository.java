package com.hyperlinks.repository;

import com.hyperlinks.domain.Game;
import com.hyperlinks.domain.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    List<Move> findAllByGame(Game game);

    Optional<Move> findByGameAndXAndY(Game game, int x, int y);

}
