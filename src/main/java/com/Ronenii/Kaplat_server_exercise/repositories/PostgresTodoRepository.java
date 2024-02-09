package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOPostgres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostgresTodoRepository extends JpaRepository<TODOPostgres, Integer> {
    TODOPostgres findTodoByRawid(int rawid);
    boolean existsTODOByTitle(String title);
    List<TODOPostgres> findTODOByState(EState state);
    void deleteByRawid(int rawid);
    long count();
}
