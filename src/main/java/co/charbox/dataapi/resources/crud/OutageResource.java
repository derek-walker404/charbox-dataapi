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

import co.charbox.dataapi.managers.OutageManager;
import co.charbox.domain.model.Outage;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.dwa.resources.AbstractCrudResource;
import com.tpofof.dwa.utils.RequestUtils;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/outages")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OutageResource extends AbstractCrudResource<Outage, String, OutageManager> {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RequestUtils requestUtils;
	
	@Autowired
	public OutageResource(OutageManager man) {
		super(man, Outage.class);
	}
	
	@Path("/recent")
	@GET
	@Timed
	public JsonNode getRecentHour(@QueryParam("startTime") Optional<String> startTimeParam,
			@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) {
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
		ResultsSet<Outage> outages = getManager().getRecentOutages(startTime, requestUtils.limit(limit), requestUtils.offset(offset));
		return responseUtils.success(responseUtils.listData(outages));
	}
}
