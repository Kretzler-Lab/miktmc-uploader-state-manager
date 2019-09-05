package org.kpmp.stateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

@Component
public class CustomStateRepository {

	private static final String PACKAGE_ID_FIELD = "packageId";
	private static final String STATE_COLLECTION = "state";
	private StateRepository stateRepository;
	private MongoTemplate mongoTemplate;

	public CustomStateRepository(StateRepository stateRepository, MongoTemplate mongoTemplate) {
		this.stateRepository = stateRepository;
		this.mongoTemplate = mongoTemplate;
	}

	public State save(State state) {
		state.setStateChangeDate(new Date());
		return stateRepository.save(state);
	}

	public State findFirstByPackageIdOrderByStateChangeDateDesc(String packageId) {
		return stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc(packageId);
	}

	public List<String> findAllPackageIds() {
		List<String> packageIds = new ArrayList<String>();
		MongoCollection<Document> collection = mongoTemplate.getCollection(STATE_COLLECTION);
		DistinctIterable<String> distinctPackageIds = collection.distinct(PACKAGE_ID_FIELD, String.class);
		MongoCursor<String> iterator = distinctPackageIds.iterator();
		while (iterator.hasNext()) {
			String packageId = (String) iterator.next();
			packageIds.add(packageId);
		}

		return packageIds;
	}
}
