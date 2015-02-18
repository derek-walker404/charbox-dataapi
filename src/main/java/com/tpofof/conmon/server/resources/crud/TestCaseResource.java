package com.tpofof.conmon.server.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.pofof.conmon.model.TestCase;
import com.tpofof.conmon.server.managers.TestCaseManager;

@Path("/testcases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestCaseResource extends GenericCrudResource<TestCase, TestCaseManager> {

	public TestCaseResource(TestCaseManager man) {
		super(man, TestCase.class);
	}
}
