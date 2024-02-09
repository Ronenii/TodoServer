package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.entities.api.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import com.Ronenii.Kaplat_server_exercise.model.entities.api.TODO;
import com.Ronenii.Kaplat_server_exercise.repositories.api.TodoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongodbTodoRepository extends MongoRepository<TODOMongodb, String>, TodoRepository {
    @Override
    TODO findTodoByRawid(int rawid);

    @Override
    boolean existsTODOByTitle(String title);

    @Override
    List<TODO> findTODOByState(EState state);

    @Override
    void deleteByRawid(int rawid);
}
