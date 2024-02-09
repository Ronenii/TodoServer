package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.EState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODOMongodb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongodbTodoRepository extends MongoRepository<TODOMongodb, String> {
    TODOMongodb findTodoByRawid(int rawid);
    boolean existsTODOByTitle(String title);
    TODOMongodb findTODOByTitle(String title);
    List<TODOMongodb> findTODOByState(EState state);
    void deleteByRawid(int rawid);
    long count();
}
