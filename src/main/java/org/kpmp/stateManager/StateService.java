package org.kpmp.stateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService {

	private CustomStateRepository stateRepository;

	@Autowired
	public StateService(CustomStateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	public String setState(State state) {
		State savedState = stateRepository.save(state);
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

	public List<State> getAllCurrentStates() {
		List<State> states = new ArrayList<State>();
		List<String> packageIds = stateRepository.findAllPackageIds();
		for (String packageId : packageIds) {
			State state = stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc(packageId);
			states.add(state);
		}

		return states;
	}

}
