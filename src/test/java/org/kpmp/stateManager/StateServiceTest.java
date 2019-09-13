package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StateServiceTest {

	@Mock
	private CustomStateRepository stateRepository;
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

	@Test
	public void testGetState() throws Exception {
		State returnedState = mock(State.class);
		when(stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc("packageId")).thenReturn(returnedState);

		assertEquals(returnedState, service.getState("packageId"));
		verify(stateRepository).findFirstByPackageIdOrderByStateChangeDateDesc("packageId");
	}

	@Test
	public void testGetAllCurrentStates() throws Exception {
		when(stateRepository.findAllPackageIds()).thenReturn(Arrays.asList("packageId1", "packageId2"));
		State state1 = mock(State.class);
		State state2 = mock(State.class);
		when(stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc("packageId1")).thenReturn(state1);
		when(stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc("packageId2")).thenReturn(state2);

		List<State> allCurrentStates = service.getAllCurrentStates();

		assertEquals(2, allCurrentStates.size());
		assertEquals(true, allCurrentStates.contains(state1));
		assertEquals(true, allCurrentStates.contains(state2));
		verify(stateRepository).findAllPackageIds();
		verify(stateRepository).findFirstByPackageIdOrderByStateChangeDateDesc("packageId1");
		verify(stateRepository).findFirstByPackageIdOrderByStateChangeDateDesc("packageId2");
	}

	@Test
	public void testFindPackagesChangedAfterStateChangeDate() throws Exception {
		State state1 = mock(State.class);
		Date firstQueryDate = new Date();
		TimeUnit.MILLISECONDS.sleep(10);
		Date changeDate = new Date();
		TimeUnit.MILLISECONDS.sleep(10);

		state1.setStateChangeDate(changeDate);
		List<State> stateList = new ArrayList<>();
		stateList.add(state1);
		when(stateRepository.findPackagesChangedAfterStateChangeDate(firstQueryDate)).thenReturn(stateList);

		List<State> allCurrentStates = service.findPackagesChangedAfterStateChangeDate(firstQueryDate);

		assertEquals(1, allCurrentStates.size());
		assertEquals(true, allCurrentStates.contains(state1));
	}
}
