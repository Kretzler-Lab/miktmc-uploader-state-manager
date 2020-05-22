package org.kpmp.stateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StateService {

	private CustomStateRepository stateRepository;
	@Value("${package.state.upload.failed}")
	private String uploadFailedState;
	@Value("${package.state.upload.succeeded}")
	private String uploadSucceededState;
	private NotificationHandler notificationHandler;
	private StateDisplayRepository stateDisplayRepo;

	@Autowired
	public StateService(CustomStateRepository stateRepository, NotificationHandler notificationHandler,
			StateDisplayRepository stateDisplayRepo) {
		this.stateRepository = stateRepository;
		this.notificationHandler = notificationHandler;
		this.stateDisplayRepo = stateDisplayRepo;
	}

	@CacheEvict(value = "states", allEntries = true)
	public String setState(State state, String origin) {
		State savedState = stateRepository.save(state);
		notificationHandler.sendNotification(state.getPackageId(), state.getState(), origin, state.getCodicil());
		return savedState.getId();
	}

	public State getState(String packageId) {
		State state = stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc(packageId);
		return state;
	}

	public List<State> findPackagesChangedAfterStateChangeDate(Date stateChangeDate) {
		List<State> states = stateRepository.findPackagesChangedAfterStateChangeDate(stateChangeDate);
		return states;
	}

	public List<State> findFailablePackagesAfterStateChangeDate(Date stateChangeDate) {
		List<State> states = stateRepository.findFailablePackagesAfterStateChangeDate(stateChangeDate);
		return states;
	}

	public Boolean isPackageSucceeded(String packageId) {
		State succeededState = stateRepository.findPackageByIdAndByState(packageId, uploadSucceededState);
		return succeededState != null;
	}

	public Boolean isPackageFailed(String packageId) {
		State failedState = stateRepository.findPackageByIdAndByState(packageId, uploadFailedState);
		return failedState != null;
	}

	@Cacheable(value = "states")
	public List<State> getAllCurrentStates() {
		List<State> states = new ArrayList<State>();
		List<String> packageIds = stateRepository.findAllPackageIds();
		for (String packageId : packageIds) {
			State state = stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc(packageId);
			states.add(state);
		}

		return states;
	}

	public List<StateDisplay> getAllStateDisplays() {
		return stateDisplayRepo.findAll();
	}

}
