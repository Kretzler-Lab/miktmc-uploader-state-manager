package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StateTest {

	private State state;

	@Before
	public void setUp() throws Exception {
		state = new State();
	}

	@After
	public void tearDown() throws Exception {
		state = null;
	}

	@Test
	public void testSetStateChangeDate() {
		Date stateChangeDate = new Date();

		state.setStateChangeDate(stateChangeDate);

		assertEquals(stateChangeDate, state.getStateChangeDate());
	}

	@Test
	public void testSetCodicil() {
		state.setCodicil("codicil");
		assertEquals("codicil", state.getCodicil());
	}

	@Test
	public void testSetState() {
		state.setState("new state");
		assertEquals("new state", state.getState());
	}

	@Test
	public void testSetPackageId() {
		state.setPackageId("packageId");
		assertEquals("packageId", state.getPackageId());
	}

	@Test
	public void testId() throws Exception {
		state.setId("id");
		assertEquals("id", state.getId());
	}

}
