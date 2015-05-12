package co.charbox.dataapi.resources.crud;

import static com.tpofof.dwa.resources.AuthRequestPermisionType.CREATE;
import io.dropwizard.auth.Auth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.ManageAssetAuthValidator;
import co.charbox.dataapi.managers.TimerResultManager;
import co.charbox.domain.mm.LocationProvider;
import co.charbox.domain.model.TimerResult;
import co.charbox.domain.model.auth.IAuthModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/results")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimerResultResource extends AbstractAuthProtectedCrudResource<TimerResult, String, TimerResultManager, EsQuery, QueryBuilder, SortBuilder, IAuthModel> {

	@Autowired private LocationProvider locationProvider;
	@Autowired private ManageAssetAuthValidator authValidator;
	
	@Autowired
	public TimerResultResource(TimerResultManager man) {
		super(man, TimerResult.class);
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
	
	@POST
	@Override
	public JsonNode post(@Auth IAuthModel auth, TimerResult model, @Context HttpServletRequest request) throws HttpCodeException {
		validate(auth, null, CREATE);
		if (model.getClientLocation().getIp() == null || model.getClientLocation().getIp().isEmpty()) {
			// might be a proxy or local host, but something is better than nothing.
			model.getClientLocation().setIp(request.getRemoteAddr());
		}
		model.setServerLocation(locationProvider.getLocation(model.getServerLocation().getIp()));
		model.setClientLocation(locationProvider.getLocation(model.getClientLocation().getIp()));
		return super.post(auth, model, request);
	}
}
