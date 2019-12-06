package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

public class NotificationHandlerTest {

	@Mock
	private RestTemplate restTemplate;
	private NotificationHandler handler;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		handler = new NotificationHandler(restTemplate);
		ReflectionTestUtils.setField(handler, "notificationServiceHost", "host");
		ReflectionTestUtils.setField(handler, "notificationEndpoint", "/endpoint");
	}

	@After
	public void tearDown() throws Exception {
		handler = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testSendNotification() {
		when(restTemplate.postForObject(any(String.class), any(StateChangeEvent.class), any(Class.class)))
				.thenReturn(true);

		handler.sendNotification("packageId", "state", "origin", "codicil");

		ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<StateChangeEvent> stateChangeCaptor = ArgumentCaptor.forClass(StateChangeEvent.class);
		ArgumentCaptor<Class> returnCaptor = ArgumentCaptor.forClass(Class.class);
		verify(restTemplate).postForObject(urlCaptor.capture(), stateChangeCaptor.capture(), returnCaptor.capture());
		assertEquals("host/endpoint", urlCaptor.getValue());
		StateChangeEvent stateChangeEvent = stateChangeCaptor.getValue();
		assertEquals("packageId", stateChangeEvent.getPackageId());
		assertEquals("state", stateChangeEvent.getState());
		assertEquals("origin", stateChangeEvent.getOrigin());
		assertEquals("codicil", stateChangeEvent.getCodicil());
		assertEquals(Boolean.class, returnCaptor.getValue());
	}

}
