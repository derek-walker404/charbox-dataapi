package co.charbox.dataapi.resources.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.model.auth.AdminAuthModel;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tpofof.core.App;
import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.jdbc.JooqConnectionProvider;
import com.tpofof.core.data.dao.test.IModelProvider;
import com.tpofof.core.managers.IModelManager;
import com.tpofof.core.utils.Config;
import com.tpofof.core.utils.json.JsonUtils;

public abstract class CharbotCrudResourceTest<ModelT extends IPersistentModel<ModelT, Integer>, ModelManagerT extends IModelManager<ModelT,Integer,CharbotSearchContext,CharbotSearchContext>, ResourceT extends CharbotAuthProtectedCrudResource<ModelT, ModelManagerT>, ProviderT extends IModelProvider<ModelT, Integer>> {

	private static JsonUtils json;
	private static JooqConnectionProvider connPro;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		json = App.getContext().getBean(JsonUtils.class);
		Config config = App.getContext().getBean(Config.class);
		connPro = App.getContext().getBean(JooqConnectionProvider.class);
		connPro.configure(config.getString("db.test.url.base"), 
				config.getString("db.test.name"),
				config.getString("db.params"),
				config.getString("db.test.username"), 
				config.getString("db.test.password"));
	}
	
	protected abstract Class<ResourceT> getResourceClass();
	
	protected ResourceT getResource() {
		return App.getContext().getBean(getResourceClass());
	}
	
	protected abstract Class<ModelManagerT> getManClass();
	
	protected ModelManagerT getMan() {
		return App.getContext().getBean(getManClass());
	}
	
	protected abstract Class<ProviderT> getProClass();
	
	protected ProviderT getPro() {
		return App.getContext().getBean(getProClass());
	}
	
	protected JsonUtils getJson() {
		return json;
	}
	
	protected abstract Class<ModelT> getModelClass();
	
	protected CharbotAuthModel getAuthModel() {
		return new AdminAuthModel();
	}
	
	protected Optional<Integer> getLimit(Integer limit) {
		return limit != null ? Optional.of(limit) : Optional.<Integer>absent();
	}
	
	protected Optional<Integer> getOffset(Integer offset) {
		return offset != null ? Optional.of(offset) : Optional.<Integer>absent();
	}
	
	protected Optional<String> getSort(String sort) {
		return sort != null ? Optional.of(sort) : Optional.<String>absent();
	}
	
	protected HttpServletRequest getRequest() {
		return Mockito.mock(HttpServletRequest.class);
	}
	
	protected void validateMinResponse(JsonNode node) {
		assertNotNull(node);
		assertNotNull(node.get("success"));
		assertTrue(node.get("success").asBoolean());
		assertNotNull(node.get("status"));
		assertEquals(200, node.get("status").asInt());
	}
	
	protected void validateBaseResponse(JsonNode node) {
		validateMinResponse(node);
		JsonNode data = node.get("data");
		assertNotNull(data);
		assertNotNull(data.get("type"));
	}
	
	protected List<ModelT> validateCollectionResponse(JsonNode node, int limit, int offset) {
		validateBaseResponse(node);
		JsonNode data = node.get("data");
		assertEquals("collection", data.get("type").asText());
		JsonNode collection = data.get("collection");
		assertNotNull(collection);
		assertTrue(collection.isArray());
		assertNotNull(data.get("count"));
		assertEquals(data.get("count").asInt(), collection.size());
		JsonNode next = data.get("next");
		assertNotNull(next);
		assertNotNull(next.get("hasMore"));
		assertNotNull(next.get("limit"));
		assertEquals(limit, next.get("limit").asInt());
		assertNotNull(next.get("offset"));
		assertEquals(offset + limit, next.get("offset").asInt());
		assertNotNull(next.get("total"));
		return json.fromJsonListResponse(node.toString(), getModelClass());
	}

	protected int validateCountResponse(JsonNode node) {
		validateBaseResponse(node);
		JsonNode data = node.get("data");
		assertEquals("data", data.get("type").asText());
		assertNotNull(data.get("data"));
		return data.get("data").asInt();
	}
	
	protected ModelT validateModelResponse(JsonNode node) {
		validateBaseResponse(node);
		JsonNode data = node.get("data");
		assertNotNull(data.get("type"));
		assertEquals("model", data.get("type").asText());
		assertNotNull(data.get("model"));
		return json.fromJsonResponse(node.toString(), getModelClass());
	}
	
	protected abstract int getDefaultLimit();
	
	@Test
	public void testFindModels() {
		Integer limit = null;
		Integer offset = null;
		JsonNode response = (JsonNode) getResource().findModels(getAuthModel(), getLimit(limit), getOffset(offset), getSort(null), getRequest()).getEntity();
		validateCollectionResponse(response, getDefaultLimit(), 0);
		
		limit = 10;
		offset = 0;
		response = (JsonNode) getResource().findModels(getAuthModel(), getLimit(limit), getOffset(offset), getSort(null), getRequest()).getEntity();
		validateCollectionResponse(response, limit, offset);
		assertNotNull(getMan().insert(null, getPro().getModel(null)));
		
		limit = 1;
		offset = 0;
		response = (JsonNode) getResource().findModels(getAuthModel(), getLimit(limit), getOffset(offset), getSort(null), getRequest()).getEntity();
		List<ModelT> modelList = validateCollectionResponse(response, limit, offset);
		assertNotNull(modelList);
		assertTrue(!modelList.isEmpty());
		assertNotNull(modelList.get(0));
		
		limit = 1;
		offset = 1;
		response = (JsonNode) getResource().findModels(getAuthModel(), getLimit(limit), getOffset(offset), getSort(null), getRequest()).getEntity();
		validateCollectionResponse(response, limit, offset);
	}

	@Test
	public void testCount() {
		JsonNode response = (JsonNode) getResource().count(getAuthModel(), getRequest()).getEntity();
		validateCountResponse(response);
		int initialCount = response.get("data").get("data").asInt();
		assertNotNull(getMan().insert(null, getPro().getModel(null)));
		response = (JsonNode) getResource().count(getAuthModel(), getRequest()).getEntity();
		validateCountResponse(response);
		int actualCount = response.get("data").get("data").asInt();
		assertEquals(initialCount + 1, actualCount);
	}

	@Test
	public void testFindModel() {
		ModelT expected = getMan().insert(null, getPro().getModel(null));
		assertNotNull(expected);
		JsonNode response = (JsonNode) getResource().findModel(getAuthModel(), expected.getId(), getRequest()).getEntity();
		ModelT actual = validateModelResponse(response);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testPost() {
		ModelT expected = getPro().getModel(null);
		JsonNode response = (JsonNode) getResource().post(getAuthModel(), expected, getRequest()).getEntity();
		ModelT actual = validateModelResponse(response);
		expected.setId(actual.getId());
	}

	@Test
	public void testPostBulk() {
		List<ModelT> models = Lists.newArrayList();
		for (int i=0;i<10;i++) {
			models.add(getPro().getModel(null));
		}
		JsonNode response = (JsonNode) getResource().postBulk(getAuthModel(), models, getRequest()).getEntity();
		List<ModelT> insertedModels = validateCollectionResponse(response, models.size(), 0);
		for (int i=0;i<10;i++) {
			ModelT expected = models.get(i);
			ModelT actual = insertedModels.get(i);
			expected.setId(actual.getId());
			assertEquals(expected, actual);
		}
	}

	@Test(expected=NotImplementedException.class)
	public void testUpdate() {
		ModelT model = getPro().getModel(null);
		model.setId(0);
		getResource().update(getAuthModel(), 0, model, getRequest());
	}

	@Test
	public void testUpdateBulk() {
		@SuppressWarnings("unchecked")
		List<ModelT> models = Lists.newArrayList(getPro().getModel(null));
		getResource().updateBulk(getAuthModel(), models, getRequest());
	}

	@Test
	public void testDelete() {
		ModelT model = getMan().insert(null, getPro().getModel(null));
		assertNotNull(model);
		assertNotNull(getMan().find(null, model.getId()));
		getResource().delete(getAuthModel(), model.getId(), getRequest());
		assertNull(getMan().find(null, model.getId()));
	}

	@Test
	public void testDeleteBulk() {
		List<Integer> ids = Lists.newArrayList();
		for (int i=0;i<10;i++) {
			ModelT inserted = getMan().insert(null, getPro().getModel(null));
			assertNotNull(inserted);
			ids.add(inserted.getId());
		}
		JsonNode response = (JsonNode) getResource().deleteBulk(getAuthModel(), ids, getRequest()).getEntity();
		validateBaseResponse(response);
		for (int i=0;i<10;i++) {
			assertNull(getMan().find(null, ids.get(i)));
		}
	}

	@AfterClass
	public static void cleanUpAfterClass() {
		connPro.dropDatabase();
	}
}
