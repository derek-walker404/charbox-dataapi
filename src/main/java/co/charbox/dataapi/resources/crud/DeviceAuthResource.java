package co.charbox.dataapi.resources.crud;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.AdminAuthValidator;
import co.charbox.dataapi.managers.DeviceAuthManager;
import co.charbox.domain.model.auth.AdminAuthModel;
import co.charbox.domain.model.auth.DeviceAuthModel;
import co.charbox.domain.model.auth.IAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/auth")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceAuthResource extends AbstractAuthProtectedCrudResource<DeviceAuthModel, String, DeviceAuthManager, EsQuery, QueryBuilder, SortBuilder, IAuthModel> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	@Autowired private AdminAuthValidator authValidator;
	
	@Autowired
	public DeviceAuthResource(DeviceAuthManager man) {
		super(man, DeviceAuthModel.class);
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
	
	@Path("/validate/device")
	@GET
	@Timed
	public JsonNode validateDevice(@Auth IAuthModel auth) throws HttpCodeException {
		if (!auth.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (auth.to(DeviceAuthModel.class) == null) {
			throw new HttpUnauthorizedException("Not authorized as device");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
	
	@Path("/validate/admin")
	@GET
	@Timed
	public JsonNode validateAdmin(@Auth IAuthModel auth) throws HttpCodeException {
		if (!auth.isActivated()) {
			throw new HttpUnauthorizedException("Credentials are not activated");
		}
		if (auth.to(AdminAuthModel.class) == null) {
			throw new HttpUnauthorizedException("Not authorized as admin");
		}
		return responseUtils.success(responseUtils.rawData("valid", true));
	}
}
