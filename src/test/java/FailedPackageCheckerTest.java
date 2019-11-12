import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kpmp.FailedPackageChecker;
import org.kpmp.stateManager.StateService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class FailedPackageCheckerTest {

    @Mock
    private StateService service;
    private FailedPackageChecker packageChecker;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        packageChecker = new FailedPackageChecker(service);
    }

    @After
    public void tearDown() throws Exception {
        packageChecker = null;
    }

    @Test
    public void testGetTimeSinceLastModified() throws Exception {
        assertEquals(42, packageChecker.getTimeSinceLastModified(System.currentTimeMillis() - 42));
    }

}
