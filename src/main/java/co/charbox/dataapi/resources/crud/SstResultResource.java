package co.charbox.dataapi.resources.crud;

import static com.tpofof.dwa.resources.AuthRequestPermisionType.CREATE;
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
import co.charbox.dataapi.managers.SstResultManager;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.SstResultsModel;
import co.charbox.domain.model.auth.CharbotAuthModel;
import co.charbox.domain.model.mm.ConnectionInfoModel;

import com.tpofof.core.utils.Config;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/sst")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SstResultResource extends CharbotAuthProtectedCrudResource<SstResultsModel, SstResultManager> {

	@Autowired private LocationProvider locationProvider;
	@Autowired private MaxMindService mm;
	@Autowired private CharbotRoleValidator authValidator;
	@Autowired private Config config;
	
	@Autowired
	public SstResultResource(SstResultManager man) {
		super(man, SstResultsModel.class);
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
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getServiceRole("SST"));
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
	
	@POST
	@Override
	public Response post(@Auth CharbotAuthModel auth, SstResultsModel model, @Context HttpServletRequest request) throws HttpCodeException {
		validate(auth, null, CREATE);
		String serviceIp = model.getServerLocation().getIp();
		if (serviceIp == null || serviceIp.isEmpty()) {
			// might be a proxy or local host, but something is better than nothing.
			String overrideIp = config.getString("location.ip.override");
			serviceIp = overrideIp == null ? request.getRemoteAddr() : overrideIp;
		}
		String clientIp = model.getDeviceInfo().getConnection().getIp();
		if (clientIp == null || clientIp.isEmpty() || "127.0.0.1".equals(clientIp)) {
			// might be a proxy or local host, but something is better than nothing.
			clientIp = config.getString("location.ip.override");
		}
		clientIp = config.getString("location.client.override", clientIp);
		serviceIp = config.getString("location.client.override", serviceIp);
		model.setServerLocation(locationProvider.getLocation(serviceIp));
		ConnectionInfoModel connInfo = mm.get(clientIp);
		if (connInfo != null) {
			model.setDeviceInfo(connInfo);
		}
		model.setStartTime(new DateTime());
		return super.post(auth, model, request);
	}
}
