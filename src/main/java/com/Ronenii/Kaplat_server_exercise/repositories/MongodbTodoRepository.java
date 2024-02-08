package com.Ronenii.Kaplat_server_exercise.repositories;

import com.Ronenii.Kaplat_server_exercise.model.eState;
import com.Ronenii.Kaplat_server_exercise.model.entities.TODO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongodbTodoRepository extends MongoRepository<TODO, String> {
    TODO findTodoByRawid(int rawid);
    boolean existsTODOByTitle(String title);
    TODO findTODOByTitle(String title);
    List<TODO> findTODOByState(eState state);
    void deleteByRawid(int rawid);
    long count();
}
