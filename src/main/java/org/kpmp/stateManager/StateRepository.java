package org.kpmp.stateManager;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StateRepository extends MongoRepository<State, String> {

	@SuppressWarnings("unchecked")
	public State save(State state);

	public State findFirstByPackageIdOrderByStateChangeDateDesc(String packageId);


}
