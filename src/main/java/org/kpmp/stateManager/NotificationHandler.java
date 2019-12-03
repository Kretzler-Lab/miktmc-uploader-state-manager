package org.kpmp.stateManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

class NotificationHandler {

	private static final Log logger = LogFactory.getLog(NotificationHandler.class);
	@Value("${notification.service.host}")
	private String notificationServiceHost;
	@Value("${notification.endpoint}")
	private String notificationEndpoint;
	private RestTemplate restTemplate;

	public NotificationHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void sendNotification(String packageId, String state, String origin) {

		Boolean response = restTemplate.postForObject(notificationServiceHost + notificationEndpoint,
				new StateChangeEvent(origin, packageId, state), Boolean.class);

		if (!response) {
			logger.error("URI: NotificationHandler.sendNotification | PKGID: " + packageId
					+ " | MSG: Notification message failed to send.");
		}
	}

}
