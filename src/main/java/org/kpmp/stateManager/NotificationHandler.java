package org.kpmp.stateManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class NotificationHandler {

	@Value("${notification.service.host}")
	private String notificationServiceHost;
	@Value("${notification.endpoint}")
	private String notificationEndpoint;
	private RestTemplate restTemplate;

	public NotificationHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void sendNotification(String packageId, String state, String origin, String codicil) {

		restTemplate.postForObject(notificationServiceHost + notificationEndpoint,
				new StateChangeEvent(origin, packageId, state, codicil), Boolean.class);

	}

}
