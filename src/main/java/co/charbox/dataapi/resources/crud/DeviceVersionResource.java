package co.charbox.dataapi.resources.crud;

import io.dropwizard.auth.Auth;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.DeviceVersionManager;
import co.charbox.domain.model.DeviceVersionModel;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/versions")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceVersionResource extends CharbotAuthProtectedCrudResource<DeviceVersionModel, DeviceVersionManager> {
	
	@Autowired private CharbotRoleValidator authValidator;
	
	@Autowired
	public DeviceVersionResource(DeviceVersionManager man) {
		super(man, DeviceVersionModel.class);
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
		case COUNT:
		case DELETE:
		case READ:
		case READ_ONE:
		case UPDATE:
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole());
		}
		authValidator.validate(auth, assetKey, requiredRoles);
	}
	
	@Path("/upgrade/{version}")
	@GET
	public Response upgrade(@Auth CharbotAuthModel auth, @PathParam("version") String version) {
		authValidator.validate(auth, null, Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getDeviceRole()));
		DeviceVersionModel upgradeVersion = getManager().canUpgrade(getContext(auth), version);
		return res().success(
				upgradeVersion != null
				? res().modelData(upgradeVersion)
				: res().rawData("canUpgrade", false)
				); 
	}
}
