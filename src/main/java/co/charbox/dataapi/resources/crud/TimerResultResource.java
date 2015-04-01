package co.charbox.dataapi.resources.crud;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import co.charbox.dataapi.data.location.LocationProvider;
import co.charbox.dataapi.managers.TimerResultManager;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.pofof.conmon.model.TimerResult;

@Path("/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimerResultResource extends GenericCrudResource<TimerResult, TimerResultManager> {

	public TimerResultResource(TimerResultManager man) {
		super(man, TimerResult.class);
	}
	
	@POST
	@Timed
	@Override
	public JsonNode post(TimerResult model,
			@Context HttpServletRequest request) {
		if (model.getClientLocation().getIp() == null || model.getClientLocation().getIp().isEmpty()) {
			// might be a proxy or local host, but something is better than nothing.
			model.getClientLocation().setIp(request.getRemoteAddr());
		}
		model.setServerLocation(LocationProvider.getLocation(model.getServerLocation().getIp()));
		model.setClientLocation(LocationProvider.getLocation(model.getClientLocation().getIp()));
		return super.post(model, request);
	}
}
