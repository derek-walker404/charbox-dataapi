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
import javax.ws.rs.core.Response;

import org.elasticsearch.common.collect.Sets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.LocationProvider;
import co.charbox.core.mm.MaxMindService;
import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.PingResultsManager;
import co.charbox.domain.model.PingResultModel;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.mm.ConnectionInfoModel;

import com.tpofof.core.utils.Config;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/pingres")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PingResultsResource extends CharbotAuthProtectedCrudResource<PingResultModel, PingResultsManager> {

	@Autowired private CharbotRoleValidator authValidator;
	@Autowired private LocationProvider locationProvider;
	@Autowired private MaxMindService mm;
	@Autowired private Config config;
	
	@Autowired
	public PingResultsResource(PingResultsManager man) {
		super(man, PingResultModel.class);
	}

	@Override
	protected IAuthValidator<CharbotAuthModel, Integer, AuthRequestPermisionType> getValidator() {
		return null;
	}
	
	@Override
	protected void validate(CharbotAuthModel auth, Integer assetKey, AuthRequestPermisionType permType) throws HttpUnauthorizedException {
		Set<RoleModel> requiredRoles = Sets.newHashSet();
		switch (permType) {
		case CREATE:
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getDeviceRole());
			break;
		case COUNT:
		case DELETE:
		case READ:
		case READ_ONE:
		case UPDATE:
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole());
		}
		authValidator.validate(auth, assetKey, requiredRoles);
	}
	
	@Override
	@POST
	public Response post(@Auth CharbotAuthModel authModel, PingResultModel model, 
			@Context HttpServletRequest request) throws HttpCodeException {
		String serviceIp = model.getServerLocation() != null ? model.getServerLocation().getIp() : null;
		if (serviceIp == null || serviceIp.isEmpty()) {
			// might be a proxy or local host, but something is better than nothing.
			String overrideIp = config.getString("location.ip.override");
			serviceIp = overrideIp == null ? request.getRemoteAddr() : overrideIp;
		}
		String clientIp = model.getConnectionInfo() != null && model.getConnectionInfo().getConnection() != null && model.getConnectionInfo().getConnection().getIp() != null
				? model.getConnectionInfo().getConnection().getIp()
				: request.getRemoteAddr(); // TODO: do I let the device choose its own ip?
		if (clientIp == null || clientIp.isEmpty() || "127.0.0.1".equals(clientIp)) {
			// might be a proxy or local host, but something is better than nothing.
			clientIp = config.getString("location.ip.override");
		}
		clientIp = config.getString("location.ip.override", clientIp);
		serviceIp = config.getString("client.ip.override", serviceIp);
		model.setServerLocation(locationProvider.getLocation(serviceIp));
		ConnectionInfoModel connInfo = mm.get(clientIp);
		if (connInfo != null) {
			model.setConnectionInfo(connInfo);
		}
		model.setStartTime(new DateTime());
		return super.post(authModel, model, request);
	}
}
