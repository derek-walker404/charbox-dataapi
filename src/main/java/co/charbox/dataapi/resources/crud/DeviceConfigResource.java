package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tpofof.dwa.resources.AbstractCrudResource;

import co.charbox.dataapi.managers.DeviceConfigurationManager;
import co.charbox.domain.model.DeviceConfiguration;

@Path("/configs")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceConfigResource extends AbstractCrudResource<DeviceConfiguration, String, DeviceConfigurationManager> {
	
	@Autowired
	public DeviceConfigResource(DeviceConfigurationManager man) {
		super(man, DeviceConfiguration.class);
	}
}
