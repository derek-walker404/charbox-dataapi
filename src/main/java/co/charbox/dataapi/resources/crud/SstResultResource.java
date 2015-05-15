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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.LocationProvider;
import co.charbox.core.mm.MaxMindService;
import co.charbox.dataapi.auth.ManageAssetAuthValidator;
import co.charbox.dataapi.managers.SstResultManager;
import co.charbox.domain.model.SstResults;
import co.charbox.domain.model.auth.IAuthModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.tpofof.core.utils.Config;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpCodeException;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/sst")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SstResultResource extends AbstractAuthProtectedCrudResource<SstResults, String, SstResultManager, IAuthModel> {

	@Autowired private LocationProvider locationProvider;
	@Autowired private MaxMindService mm;
	@Autowired private ManageAssetAuthValidator authValidator;
	@Autowired private Config config;
	
	@Autowired
	public SstResultResource(SstResultManager man) {
		super(man, SstResults.class);
	}
	
	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return authValidator;
	}
	
	@POST
	@Override
	public JsonNode post(@Auth IAuthModel auth, SstResults model, @Context HttpServletRequest request) throws HttpCodeException {
		validate(auth, null, CREATE);
		String serviceIp = model.getServerLocation().getIp();
		if (serviceIp == null || serviceIp.isEmpty()) {
			// might be a proxy or local host, but something is better than nothing.
			String overrideIp = config.getString("location.ip.override");
			serviceIp = overrideIp == null ? request.getRemoteAddr() : overrideIp;
		}
		String clientIp = model.getDeviceInfo().getConnection().getIp();
		if (clientIp == null || clientIp.isEmpty()) {
			// might be a proxy or local host, but something is better than nothing.
			clientIp = config.getString("location.ip.override");
		}
		clientIp = config.getString("location.client.override", clientIp);
		model.setServerLocation(locationProvider.getLocation(serviceIp));
		model.setDeviceInfo(mm.get(clientIp));
		return super.post(auth, model, request);
	}
}
