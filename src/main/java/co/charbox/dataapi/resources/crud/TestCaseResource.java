package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.TestCaseManager;
import co.charbox.domain.model.TestCase;

import com.tpofof.dwa.resources.AbstractCrudResource;

@Path("/testcases")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestCaseResource extends AbstractCrudResource<TestCase, String, TestCaseManager> {

	@Autowired
	public TestCaseResource(TestCaseManager man) {
		super(man, TestCase.class);
	}
}
