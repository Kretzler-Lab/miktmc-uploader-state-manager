package org.kpmp.stateManager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
import org.springframework.web.context.request.async.DeferredResult;

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

	@RequestMapping(value = "/v1/state/events/{afterTime}", method = RequestMethod.GET)
	public @ResponseBody DeferredResult<List<State>> getStateEvents(@PathVariable("afterTime") String afterTime, HttpServletRequest request) {
        Date stateChangeDate = new Date(new Long(afterTime));

		log.info("URI: " + request.getRequestURI() + " | MSG: Long poll for events after " + stateChangeDate);

		// Timeout after 1 minute
		Long timeOutInMilliSec = 60000L;
		String timeOutResp = "{\"timeout\": true}";
		DeferredResult<List<State>> deferredResult = new DeferredResult<>(timeOutInMilliSec,timeOutResp);
		CompletableFuture.runAsync(()->{
			try {
				List<State> result = stateService.findPackagesChangedAfterStateChangeDate(stateChangeDate);

				while (result.size() == 0) {
					TimeUnit.SECONDS.sleep(2);
					result = stateService.findPackagesChangedAfterStateChangeDate(stateChangeDate);
				}

				log.info("URI: " + request.getRequestURI() + " | MSG: Long poll returning " + result.size() + " records");

				//Useful for debugging; remove when we're done with spike / KPMP-1255
                log.info("first record's stateChange Date asa UTC ms: " + result.get(0).getStateChangeDate().getTime());

				//set result after completing task to return response to client
				deferredResult.setResult(result);
			}catch (Exception ex){
			}
		});

		return deferredResult;
	}
}
