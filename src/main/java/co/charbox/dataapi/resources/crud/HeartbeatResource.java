package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.HeartbeatManager;
import co.charbox.domain.model.Heartbeat;

import com.tpofof.dwa.resources.AbstractCrudResource;

@Path("/hb")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeartbeatResource extends AbstractCrudResource<Heartbeat, String, HeartbeatManager> {

	@Autowired
	public HeartbeatResource(HeartbeatManager man) {
		super(man, Heartbeat.class);
	}
}
