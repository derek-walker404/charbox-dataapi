package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.ManageAssetAuthValidator;
import co.charbox.dataapi.managers.HeartbeatManager;
import co.charbox.domain.model.Heartbeat;
import co.charbox.domain.model.auth.IAuthModel;

import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/hb")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeartbeatResource extends AbstractAuthProtectedCrudResource<Heartbeat, String, HeartbeatManager, EsQuery, QueryBuilder, SortBuilder, IAuthModel> {

	@Autowired private ManageAssetAuthValidator authValidator;
	
	@Autowired
	public HeartbeatResource(HeartbeatManager man) {
		super(man, Heartbeat.class);
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
