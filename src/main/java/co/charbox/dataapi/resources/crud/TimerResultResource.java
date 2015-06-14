package co.charbox.dataapi.resources.crud;

import io.dropwizard.auth.Auth;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.LocationProvider;
import co.charbox.dataapi.managers.TimerResultManager;
import co.charbox.domain.model.TimerResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.auth.RoleValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/results")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimerResultResource extends CharbotAuthProtectedCrudResource<TimerResult, TimerResultManager> {

	@Autowired private LocationProvider locationProvider;
	@Autowired private RoleValidator authValidator;
	
	@Autowired
	public TimerResultResource(TimerResultManager man) {
		super(man, TimerResult.class);
	}
	
	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return null;
	}
	
	@Override
	protected void validate(IAuthModel auth, String assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		Set<String> requiredRoles = Sets.newHashSet();
		switch (permType) {
		case COUNT:
		case CREATE:
		case DELETE:
		case READ:
		case READ_ONE:
		case UPDATE:
			requiredRoles = Sets.newHashSet("ADMIN");
		}
		authValidator.validate(auth, assetKey, requiredRoles);
	}
	
	@POST
	@Override
	public JsonNode post(@Auth IAuthModel auth, TimerResult model, @Context HttpServletRequest request) throws HttpCodeException {
		if (model.getClientLocation().getIp() == null || model.getClientLocation().getIp().isEmpty()) {
			// might be a proxy or local host, but something is better than nothing.
			model.getClientLocation().setIp(request.getRemoteAddr());
		}
		model.setServerLocation(locationProvider.getLocation(model.getServerLocation().getIp()));
		model.setClientLocation(locationProvider.getLocation(model.getClientLocation().getIp()));
		return super.post(auth, model, request);
	}
}
