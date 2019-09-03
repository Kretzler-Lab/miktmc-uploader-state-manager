package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		State state = new State();
		when(stateService.setState(state)).thenReturn("id");
		String stateId = controller.setState(state);

		assertEquals("id", stateId);
		verify(stateService).setState(state);
	}

}
