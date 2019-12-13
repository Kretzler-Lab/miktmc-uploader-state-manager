import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kpmp.FailedPackageChecker;
import org.kpmp.stateManager.State;
import org.kpmp.stateManager.StateService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FailedPackageCheckerTest {

    @Mock
    private StateService service;
    @Mock
    RestTemplate restTemplate;
    private FailedPackageChecker packageChecker;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        packageChecker = new FailedPackageChecker(service, restTemplate);
    }

    @After
    public void tearDown() throws Exception {
        packageChecker = null;
    }

    @Test
    public void testGetTimeSinceLastModified() throws Exception {
        assertEquals(42, packageChecker.getTimeSinceLastModified(System.currentTimeMillis() - 42));
    }

    @Test
    public void testPackageDidFail() throws Exception {
        State state = new State();
        state.setLargeUploadChecked("false");

        long timeSinceLastModified = System.currentTimeMillis() - 59 * 1000; //29 * 60 * 1000;
        assertFalse(packageChecker.packageDidFail(state, timeSinceLastModified));

        timeSinceLastModified = System.currentTimeMillis() - 31 * 60 * 1000;
        assertTrue(packageChecker.packageDidFail(state, timeSinceLastModified));

        state.setLargeUploadChecked("true");
        assertFalse(packageChecker.packageDidFail(state, timeSinceLastModified));
    }
}
