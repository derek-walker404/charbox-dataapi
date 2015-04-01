package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import co.charbox.dataapi.managers.HeartbeatManager;
import co.charbox.domain.model.Heartbeat;

@Path("/hb")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeartbeatResource extends AbstractCrudResource<Heartbeat, HeartbeatManager> {

	public HeartbeatResource(HeartbeatManager man) {
		super(man, Heartbeat.class);
	}
}
