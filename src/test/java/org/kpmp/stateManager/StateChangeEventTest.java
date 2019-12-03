package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StateChangeEventTest {

	private StateChangeEvent event;

	@Before
	public void setUp() throws Exception {
		event = new StateChangeEvent("origin", "packageId", "state");
	}

	@After
	public void tearDown() throws Exception {
		event = null;
	}

	@Test
	public void testConstructor() throws Exception {
		event = new StateChangeEvent("origin2", "packageId2", "state2");
		assertEquals("origin2", event.getOrigin());
		assertEquals("packageId2", event.getPackageId());
		assertEquals("state2", event.getState());
	}

	@Test
	public void testSetOrigin() {
		event.setOrigin("localhost");
		assertEquals("localhost", event.getOrigin());
	}

	@Test
	public void testSetPackageId() {
		event.setPackageId("package id 2");
		assertEquals("package id 2", event.getPackageId());
	}

	@Test
	public void testSetState() {
		event.setState("failed");
		assertEquals("failed", event.getState());
	}

}
