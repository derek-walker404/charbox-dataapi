package co.charbox.dataapi.resources.crud.auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.AdminAuthValidator;
import co.charbox.dataapi.managers.auth.ServerAuthManager;
import co.charbox.domain.model.auth.IAuthModel;
import co.charbox.domain.model.auth.ServerAuthModel;

import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/serverauth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceAuthResource extends AbstractAuthProtectedCrudResource<ServerAuthModel, String, ServerAuthManager, EsQuery, QueryBuilder, SortBuilder, IAuthModel> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private AdminAuthValidator authValidator;
	
	@Autowired
	public ServiceAuthResource(ServerAuthManager man) {
		super(man, ServerAuthModel.class);
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
