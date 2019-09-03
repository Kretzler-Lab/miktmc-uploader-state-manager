package org.kpmp.stateManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService {

	private StateRepository stateRepository;

	@Autowired
	public StateService(StateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	public String setState(State state) {
		State savedState = stateRepository.save(state);
		return savedState.getId();
	}

}
