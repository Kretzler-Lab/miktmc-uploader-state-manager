package org.kpmp.stateManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StateController {

	private StateService stateService;
	private static final Log log = LogFactory.getLog(StateController.class);

	@Autowired
	public StateController(StateService stateService) {
		this.stateService = stateService;
	}

	@RequestMapping(value = "/v1/state", method = RequestMethod.POST)
	public @ResponseBody String setState(@RequestBody State state, HttpServletRequest request) {
		log.info("URI: " + request.getRequestURI() + " | PKGID: " + state.getPackageId() + " | MSG: Saving new state: "
				+ state.getState());
		return stateService.setState(state);
	}

	@RequestMapping(value = "/v1/state/{packageId}", method = RequestMethod.GET)
	public @ResponseBody State getState(@PathVariable("packageId") String packageId, HttpServletRequest request) {
		log.info("URI: " + request.getRequestURI() + " | PKGID: " + packageId + " | MSG: Retrieving most recent state");
		return stateService.getState(packageId);
	}

	@RequestMapping(value = "/v1/state", method = RequestMethod.GET)
	public @ResponseBody List<State> getStates(HttpServletRequest request) {
		log.info("URI: " + request.getRequestURI() + " | MSG: Retrieving most recent state for all packages");
		return stateService.getAllCurrentStates();
	}

}
