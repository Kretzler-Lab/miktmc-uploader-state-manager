package org.kpmp.stateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import org.springframework.stereotype.Component;

@Component
public class CustomStateRepository {

	private static final Log log = LogFactory.getLog(CustomStateRepository.class);

	private static final String PACKAGE_ID_FIELD = "packageId";
	private static final String STATE_COLLECTION = "state";
	private static final String STATE_CHANGE_DATE = "stateChangeDate";
	private static final String STATE_FIELD = "state";

	@Value("${package.state.checker.timeout}")
	private long timeout;
	@Value("${package.state.upload.started}")
	private String uploadStartedState;
	@Value("${package.state.metadata.received}")
	private String metadataReceivedState;

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

	public List<State> findPackagesChangedAfterStateChangeDate(Date stateChangeDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where(STATE_CHANGE_DATE).gt(stateChangeDate));
		query.with(Sort.by(Direction.DESC, STATE_CHANGE_DATE));
		List<State> states = mongoTemplate.find(query, State.class);
		List<State> uniquePackageStates = new ArrayList<>();
		List<String> packageIds = new ArrayList<>();
		Iterator<State> iterator = states.iterator();

		while(iterator.hasNext()) {
			State state = iterator.next();
			if(packageIds.indexOf(state.getPackageId()) > -1) {
				continue;
			}

			packageIds.add(state.getPackageId());
			uniquePackageStates.add(state);
		}

		return uniquePackageStates;
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

	public List<State> findFailablePackagesAfterStateChangeDate(Date stateChangeDate) {
		List<State> uniqueChangedPackages = findPackagesChangedAfterStateChangeDate(stateChangeDate);
		List<State> uniqueFailablePackages = new ArrayList<>();
		Iterator<State> iterator = uniqueChangedPackages.iterator();

		while(iterator.hasNext()) {
			State state = iterator.next();

			log.info("URI: CustomStateRepository.findFailablePackagesAfterStateChangeDate | " +
					"MSG: reviewing package state for potential failure | PKGID: " + state.getPackageId() + " | " +
					"PKGSTATE: " + state.getState() + " | PKGLFU: " + state.getLargeUploadChecked() + " | " +
 					"AFTERMILLIS: " + stateChangeDate.getTime());

			if(metadataReceivedState.equals(state.getState()) && !"true".equals(state.getLargeUploadChecked())) {
				uniqueFailablePackages.add(state);
				log.info("URI: CustomStateRepository.findFailablePackagesAfterStateChangeDate | " +
						"MSG: added package to failure check | PKGID: " + state.getPackageId() + " | " +
						"PKGSTATE: " + state.getState() + " | PKGLFU: " + state.getLargeUploadChecked() + " | " +
						"AFTERMILLIS: " + stateChangeDate.getTime());
			}
		}

		return uniqueFailablePackages;
	}

	public State findPackageByIdAndByState(String packageId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where(STATE_FIELD).is(state).and(PACKAGE_ID_FIELD).is(packageId));
		return mongoTemplate.findOne(query, State.class);
	}
}
