package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StateServiceTest {

	@Mock
	private StateRepository stateRepository;
	private StateService service;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		service = new StateService(stateRepository);
	}

	@After
	public void tearDown() throws Exception {
		service = null;
	}

	@Test
	public void testSetState() {
		State state = mock(State.class);
		State returnState = mock(State.class);
		when(returnState.getId()).thenReturn("id");
		when(stateRepository.save(state)).thenReturn(returnState);

		String stateId = service.setState(state);

		assertEquals("id", stateId);
		verify(stateRepository).save(state);
	}

}
