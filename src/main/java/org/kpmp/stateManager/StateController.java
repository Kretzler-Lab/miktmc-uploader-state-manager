package org.kpmp.stateManager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${package.state.longpoll.timeout}")
	Long longPollTimeoutMillis;

	@Autowired
	public StateController(StateService stateService) {
		this.stateService = stateService;
	}

	@RequestMapping(value = "/v1/state/host/{host}", method = RequestMethod.POST)
	public @ResponseBody String setState(@RequestBody State state, @PathVariable("host") String origin,
			HttpServletRequest request) {
		String largeFilesChecked = null == state.getLargeUploadChecked() ? "null" : state.getLargeUploadChecked();
		log.info("URI: " + request.getRequestURI() + " | PKGID: " + state.getPackageId() + " | MSG: Saving new state: "
				+ state.getState() + " | largeFilesChecked: " + largeFilesChecked);
		return stateService.setState(state, origin);
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

	@RequestMapping(value = "/v1/state/stateDisplayMap", method = RequestMethod.GET)
	public @ResponseBody List<StateDisplay> getStateDisplays(HttpServletRequest request) {
		log.info("URI: " + request.getRequestURI() + " | MSG: Retrieving state map");
		return stateService.getAllStateDisplays();
	}

	@RequestMapping(value = "/v1/state/events/{afterTime}", method = RequestMethod.GET)
	public @ResponseBody DeferredResult<List<State>> getStateEvents(@PathVariable("afterTime") String afterTime,
			HttpServletRequest request) {
		Date stateChangeDate = new Date(new Long(afterTime));

		log.info("URI: " + request.getRequestURI() + " | MSG: Long poll for events after " + stateChangeDate);

		String timeOutResp = "{\"timeout\": true}";
		DeferredResult<List<State>> deferredResult = new DeferredResult<>(longPollTimeoutMillis, timeOutResp);
		CompletableFuture.runAsync(() -> {
			try {
				List<State> result = stateService.findPackagesChangedAfterStateChangeDate(stateChangeDate);

				while (result.size() == 0) {
					TimeUnit.SECONDS.sleep(2);
					result = stateService.findPackagesChangedAfterStateChangeDate(stateChangeDate);
				}

				log.info("URI: " + request.getRequestURI() + " | MSG: Long poll returning " + result.size()
						+ " records");

				deferredResult.setResult(result);
			} catch (Exception ex) {
				log.error("URI: " + request.getRequestURI() + "| MSG: Long polling encountered error: "
						+ ex.getMessage());
			}
		});

		return deferredResult;
	}
}
