package co.charbox.dataapi.resources;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.JobSchedule;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Maps;
import com.tpofof.dwa.auth.RoleValidator;
import com.tpofof.dwa.utils.ResponseUtils;

@Component
@Path("/schedules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobScheduleResource {

	@Autowired private ResponseUtils responseUtils;
	@Autowired private RoleValidator authValidator;
	
	@POST
	public JsonNode newSchedule() {
		Map<String, String> schedules = Maps.newHashMap();
		List<JobSchedule> jobs = JobSchedule.getAllJobs();
		for (JobSchedule job : jobs) {
			schedules.put(job.getName(), job.getSchedule());
		}
		return responseUtils.success(responseUtils.stringMap("schedules", schedules));
	}
}
