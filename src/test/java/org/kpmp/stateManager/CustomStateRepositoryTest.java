package org.kpmp.stateManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CustomStateRepositoryTest {

	@Mock
	private StateRepository stateRepository;
	@Mock
	private MongoTemplate mongoTemplate;
	private CustomStateRepository customRepo;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		customRepo = new CustomStateRepository(stateRepository, mongoTemplate);
	}

	@After
	public void tearDown() throws Exception {
		customRepo = null;
	}

	@Test
	public void testSave() {
		State state = mock(State.class);
		State newState = mock(State.class);
		when(stateRepository.save(state)).thenReturn(newState);

		State savedState = customRepo.save(state);

		assertEquals(newState, savedState);
		verify(state).setStateChangeDate(any(Date.class));
		verify(stateRepository).save(state);
	}

	@Test
	public void testFindFirstByPackageIdOrderByStateChangeDateDesc() {
		State state = mock(State.class);
		when(stateRepository.findFirstByPackageIdOrderByStateChangeDateDesc("packageId")).thenReturn(state);

		State result = customRepo.findFirstByPackageIdOrderByStateChangeDateDesc("packageId");

		assertEquals(state, result);
		verify(stateRepository).findFirstByPackageIdOrderByStateChangeDateDesc("packageId");
	}

	@SuppressWarnings({ "unchecked" })
	@Test
	public void testFindAllPackageIds() throws Exception {
		DistinctIterable<String> resultIterator = mock(DistinctIterable.class);
		MongoCollection<Document> mongoCollection = mock(MongoCollection.class);
		when(mongoTemplate.getCollection("state")).thenReturn(mongoCollection);
		when(mongoCollection.distinct("packageId", String.class)).thenReturn(resultIterator);
		MongoCursor<String> cursor = mock(MongoCursor.class);
		when(resultIterator.iterator()).thenReturn(cursor);
		when(cursor.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
		when(cursor.next()).thenReturn("packageId1").thenReturn("packageId2");

		List<String> allPackageIds = customRepo.findAllPackageIds();

		assertEquals(2, allPackageIds.size());
		assertEquals(true, allPackageIds.contains("packageId1"));
		assertEquals(true, allPackageIds.contains("packageId2"));
	}

}
