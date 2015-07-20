package co.charbox.dataapi.resources.crud;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.MaxMindService;
import co.charbox.dataapi.auth.CharbotRoleValidator;
import co.charbox.dataapi.managers.ConnectionInfoManager;
import co.charbox.dataapi.managers.HeartbeatManager;
import co.charbox.domain.model.HeartbeatModel;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.tpofof.core.utils.Config;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.error.HttpUnauthorizedException;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/hb")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeartbeatResource extends CharbotAuthProtectedCrudResource<HeartbeatModel, HeartbeatManager> {

	@Autowired private CharbotRoleValidator authValidator;
	@Autowired private Config config;
	@Autowired private MaxMindService mm;
	@Autowired private ConnectionInfoManager ciMan;
	
	@Autowired
	public HeartbeatResource(HeartbeatManager man) {
		super(man, HeartbeatModel.class);
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
			requiredRoles = Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getDeviceRole()); // probably want to add asset role to this instead of device role.
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
}
