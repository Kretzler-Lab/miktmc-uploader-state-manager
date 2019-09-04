package org.kpmp.stateManager;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
		state.setStateChangeDate(new Date());
		return stateService.setState(state);
	}

}
