package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOPostgres;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
import com.Ronenii.Kaplat_server_exercise.repositories.api.TodoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostgresTodoRepository extends JpaRepository<TODOPostgres, Integer>, TodoRepository {
    @Override
    TODO findTodoByRawid(int rawid);

    @Override
    boolean existsTODOByTitle(String title);

    @Override
    List<TODO> findTODOByState(EState state);
}
