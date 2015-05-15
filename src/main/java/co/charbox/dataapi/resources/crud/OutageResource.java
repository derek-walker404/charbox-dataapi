package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.ManageAssetAuthValidator;
import co.charbox.dataapi.managers.OutageManager;
import co.charbox.domain.model.Outage;
import co.charbox.domain.model.auth.IAuthModel;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/outages")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OutageResource extends AbstractAuthProtectedCrudResource<Outage, String, OutageManager, IAuthModel> {

	@Autowired private ManageAssetAuthValidator authValidator;
	
	@Autowired
	public OutageResource(OutageManager man) {
		super(man, Outage.class);
	}

	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return authValidator;
	}
	
	@Path("/recent")
	@GET
	@Timed
	public JsonNode getRecentHour(@QueryParam("startTime") Optional<String> startTimeParam,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset,
			@QueryParam("sort") Optional<String> sort) {
		DateTime startTime = new DateTime();
		if (startTimeParam.isPresent()) {
			String startTimeString = startTimeParam.get().trim().toUpperCase();
			if (startTimeString.matches("\\d+[HDWMY]")) {
				int quantity = Integer.parseInt(startTimeString.substring(0, startTimeString.length()-1));
				switch (startTimeString.charAt(startTimeString.length() - 1)) {
				case 'H':
					startTime = startTime.minusHours(quantity);
					break;
				case 'D':
					startTime = startTime.minusDays(quantity);
					break;
				case 'W':
					startTime = startTime.minusWeeks(quantity);
					break;
				case 'M':
					startTime = startTime.minusMonths(quantity);
					break;
				case 'Y':
					startTime = startTime.minusYears(quantity);
					break;
				}
			} else {
				startTime = startTime.minusHours(1);
			}
		}
		ResultsSet<Outage> outages = getManager().getRecentOutages(startTime, req().searchWindow(limit, offset));
		return res().success(res().listData(outages));
	}
}
