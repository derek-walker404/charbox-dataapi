package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.TimerResultManager;
import co.charbox.domain.mm.LocationProvider;
import co.charbox.domain.model.TimerResult;

import com.tpofof.dwa.resources.AbstractCrudResource;

@Path("/results")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TimerResultResource extends AbstractCrudResource<TimerResult, String, TimerResultManager> {

	private LocationProvider locationProvider;
	
	@Autowired
	public TimerResultResource(TimerResultManager man, LocationProvider locationProvider) {
		super(man, TimerResult.class);
		this.locationProvider = locationProvider;
	}
	
//	@POST
//	@Timed
//	@Override
//	public JsonNode post(TimerResult model,
//			@Context HttpServletRequest request) throws HttpInternalServerErrorException {
//		if (model.getClientLocation().getIp() == null || model.getClientLocation().getIp().isEmpty()) {
//			// might be a proxy or local host, but something is better than nothing.
//			model.getClientLocation().setIp(request.getRemoteAddr());
//		}
//		model.setServerLocation(locationProvider.getLocation(model.getServerLocation().getIp()));
//		model.setClientLocation(locationProvider.getLocation(model.getClientLocation().getIp()));
//		return super.post(model, request);
//	}
}
