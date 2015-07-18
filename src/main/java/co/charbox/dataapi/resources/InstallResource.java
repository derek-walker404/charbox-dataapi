package co.charbox.dataapi.resources;

import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.DeviceAuthManager;
import co.charbox.domain.model.auth.DeviceAuthModel;

import com.google.common.collect.Sets;
import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.auth.RoleValidator;
import com.tpofof.dwa.resources.IDwaResource;
import com.tpofof.dwa.utils.ResponseUtils;

@Component
@Path("/install")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InstallResource implements IDwaResource {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RoleValidator authValidator;
	@Autowired private DeviceAuthManager deviceAuthManager;
	
	@POST
	public Response newInstall(@Auth IAuthModel auth) {
		authValidator.validate(auth, null, Sets.newHashSet("ADMIN", "INSTALL"));
		DeviceAuthModel newAuth = deviceAuthManager.newInstall();
		return responseUtils.success(responseUtils.modelData(newAuth));
	}
}
