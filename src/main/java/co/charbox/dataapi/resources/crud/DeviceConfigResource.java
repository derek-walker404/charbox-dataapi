package co.charbox.dataapi.resources.crud;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.auth.DeviceViewAuthValidator;
import co.charbox.dataapi.managers.DeviceConfigurationManager;
import co.charbox.domain.model.DeviceConfiguration;
import co.charbox.domain.model.auth.IAuthModel;

import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.dwa.auth.IAuthValidator;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.AuthRequestPermisionType;

@Path("/configs")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceConfigResource extends AbstractAuthProtectedCrudResource<DeviceConfiguration, String, DeviceConfigurationManager, EsQuery, QueryBuilder, SortBuilder, IAuthModel> {
	
	@Autowired private DeviceViewAuthValidator authValidator;
	
	@Autowired
	public DeviceConfigResource(DeviceConfigurationManager man) {
		super(man, DeviceConfiguration.class);
	}
	
	@Override
	protected IAuthValidator<IAuthModel, String, AuthRequestPermisionType> getValidator() {
		return authValidator;
	}
	
	@Override
	protected EsQuery getDefaultQuery(int limit, int offset) {
		return EsQuery.builder()
				.limit(limit)
				.offset(offset)
				.build();
	}
}
