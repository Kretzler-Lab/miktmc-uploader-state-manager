package org.kpmp.stateManager;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StateDisplayRepository extends MongoRepository<StateDisplay, String> {

	public List<StateDisplay> findAll();

}
