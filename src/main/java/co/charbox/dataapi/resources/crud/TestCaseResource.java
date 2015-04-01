package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import co.charbox.dataapi.managers.TestCaseManager;
import co.charbox.domain.model.TestCase;

@Path("/testcases")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestCaseResource extends AbstractCrudResource<TestCase, TestCaseManager> {

	public TestCaseResource(TestCaseManager man) {
		super(man, TestCase.class);
	}
}
