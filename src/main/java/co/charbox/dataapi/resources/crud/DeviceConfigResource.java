package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import co.charbox.dataapi.managers.DeviceConfigurationManager;

import com.pofof.conmon.model.DeviceConfiguration;

@Path("/configs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceConfigResource extends GenericCrudResource<DeviceConfiguration, DeviceConfigurationManager> {
	
	public DeviceConfigResource(DeviceConfigurationManager man) {
		super(man, DeviceConfiguration.class);
	}
}
