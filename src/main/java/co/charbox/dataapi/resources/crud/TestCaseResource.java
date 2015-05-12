package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.ReadAssetAuthValidator;
import co.charbox.dataapi.managers.TestCaseManager;
import co.charbox.domain.model.TestCase;
import co.charbox.domain.model.auth.IAuthModel;

import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/testcases")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestCaseResource extends AbstractAuthProtectedCrudResource<TestCase, String, TestCaseManager, EsQuery, QueryBuilder, SortBuilder, IAuthModel> {

	@Autowired private ReadAssetAuthValidator authValidator;
	
	@Autowired
	public TestCaseResource(TestCaseManager man) {
		super(man, TestCase.class);
	}

	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return authValidator;
	}
	
	@Override
	protected EsQuery getDefaultQuery(int limit, int offset) {
		return EsQuery.builder()
				.limit(limit)
				.offset(offset)
				.build();
	}
}
