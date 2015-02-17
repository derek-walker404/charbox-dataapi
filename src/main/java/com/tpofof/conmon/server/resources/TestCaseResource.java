package com.tpofof.conmon.server.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.mongo.TestCaseDAO;

@Path("/testcases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestCaseResource {

	private TestCaseDAO dao;
	
	public TestCaseResource(TestCaseDAO dao) {
		this.dao = dao;
	}
	
	@GET
	@Timed
	public JsonNode getTestCases(@QueryParam("limit") Optional<Integer> limit,
			@QueryParam("offset") Optional<Integer> offset) {
		int limitVal = limit.isPresent() ? limit.get() : 20;
		int offsetVal = offset.isPresent() ? offset.get() : 0;
		List<TestCase> cases = dao.getTestCases(limitVal, offsetVal);
		return ResponseUtils.success(ResponseUtils.listData(cases, limitVal, offsetVal));
	}
	
	@Path("/{_id}")
	@GET
	@Timed
	public JsonNode getTestCase(@PathParam("_id") String id) {
		TestCase testCase = dao.getTestCase(id);
		return testCase == null ?
				ResponseUtils.failure("Could not find test case with id " + id, 404)
				: ResponseUtils.success(ResponseUtils.modelData(testCase));
	}
	
	@POST
	@Timed
	public JsonNode postTestCase(TestCase tc) {
		TestCase insertedTc = dao.postTestCase(tc);
		return insertedTc == null ?
				ResponseUtils.failure("Could not create test case.")
				: ResponseUtils.success(ResponseUtils.modelData(insertedTc)); 
	}
}
