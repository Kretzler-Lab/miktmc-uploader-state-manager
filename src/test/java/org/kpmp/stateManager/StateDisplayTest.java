package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class StateDisplayTest {

	private StateDisplay stateDisplay;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		stateDisplay = new StateDisplay();
	}

	@After
	public void tearDown() throws Exception {
		stateDisplay = null;
	}

	@Test
	public void testSetStateDisplayInfo() {
		Document displayInfo = mock(Document.class);
		stateDisplay.setStateDisplayInfo(displayInfo);
		assertEquals(displayInfo, stateDisplay.getStateDisplayInfo());
	}

	@Test
	public void testSetId() {
		stateDisplay.setId("displayId");
		assertEquals("displayId", stateDisplay.getId());
	}

}
