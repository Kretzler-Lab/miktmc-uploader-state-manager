package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StateControllerTest {

	@Mock
	private StateService stateService;
	private StateController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new StateController(stateService);
	}

	@After
	public void tearDown() throws Exception {
		controller = null;
	}

	@Test
	public void testSetState() {
		State state = mock(State.class);
		when(stateService.setState(state)).thenReturn("id");
		String stateId = controller.setState(state, mock(HttpServletRequest.class));

		assertEquals("id", stateId);
		verify(stateService).setState(state);
		verify(state).setStateChangeDate(any(Date.class));
	}

}
