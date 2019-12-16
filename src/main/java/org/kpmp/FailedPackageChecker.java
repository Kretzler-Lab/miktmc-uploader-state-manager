package org.kpmp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kpmp.stateManager.State;
import org.kpmp.stateManager.StateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = { "org.kpmp" })
public class FailedPackageChecker implements CommandLineRunner {

	private StateService stateService;

	private static final Log log = LogFactory.getLog(FailedPackageChecker.class);

	@Value("${state.service.host}")
	private String stateServiceHost;
	@Value("${state.service.endpoint}")
	private String stateServiceEndpoint;
	@Value("${package.state.upload.failed}")
	private String uploadFailedState;
	@Value("${package.state.upload.succeeded}")
	private String uploadSucceededState;
	@Value("${file.base.path}")
	private String basePath;
	@Value("${package.state.checker.timeout}")
	private long timeout;
	@Value("${package.state.checker.checkaftermillis}")
	private long checkAfterMillis;

	private RestTemplate restTemplate;

	public FailedPackageChecker(StateService stateService, RestTemplate restTemplate) {
		this.stateService = stateService;
		this.restTemplate = restTemplate;
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FailedPackageChecker.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

	public long getPackageLastModified(String packageId) {
		String path = basePath + File.separator + "package_" + packageId + File.separator;
		File file = new File(path);
		return file.lastModified();
	}

	public long getTimeSinceLastModified(long lastModified) {
		return System.currentTimeMillis() - lastModified;
	}

	public void sendStateChange(State state) {
		restTemplate.postForObject(stateServiceHost + stateServiceEndpoint + "/unknown", state, String.class);
	}

	public boolean packageDidFail(State state) {
		long lastModified = getPackageLastModified(state.getPackageId());
		return packageDidFail(state, lastModified);
	}

	public boolean packageDidFail(State state, long lastModified) {
		long timeSinceLastModified = getTimeSinceLastModified(lastModified);
		return timeSinceLastModified > timeout;
	}

	@Override
	public void run(String... args) throws Exception {

		Date checkAfterDate = new Date(new Date().getTime() - checkAfterMillis);
		List<State> states = stateService.findFailablePackagesAfterStateChangeDate(checkAfterDate);
		List<State> failedPackages = new ArrayList<>();

		log.info("URI: FailedPackageChecker.run | MSG: Checking " + states.size() + " packages for failure | " +
				"AFTERMILLIS: " + checkAfterDate.getTime());

		for (State state : states) {
			if (!stateService.isPackageFailed(state.getPackageId())
					&& !stateService.isPackageSucceeded(state.getPackageId())) {
				failedPackages.add(state);
			}
		}

		log.info("URI: FailedPackageChecker.run | MSG: Found " + failedPackages.size() + " stale packages; setting state...");

		for (State state : failedPackages) {
			if (packageDidFail(state)) {
				State failedState = new State();
				failedState.setPackageId(state.getPackageId());
				failedState.setState(uploadFailedState);
				failedState.setLargeUploadChecked(state.getLargeUploadChecked());
				failedState.setStateChangeDate(new Date());
				failedState.setCodicil("Failed stale package check");
				sendStateChange(failedState);
			}
		}

		log.info("URI: FailedPackageChecker.run | MSG: Done");
	}
}
