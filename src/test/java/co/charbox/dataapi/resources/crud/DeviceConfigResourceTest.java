package co.charbox.dataapi.resources.crud;

import co.charbox.dataapi.managers.DeviceConfigurationManager;
import co.charbox.domain.model.DeviceConfigurationModel;
import co.charbox.domain.model.test.DeviceConfigurationModelProvider;


public class DeviceConfigResourceTest extends CharbotCrudResourceTest<DeviceConfigurationModel, DeviceConfigurationManager, DeviceConfigResource, DeviceConfigurationModelProvider> {

	@Override
	protected Class<DeviceConfigResource> getResourceClass() {
		return DeviceConfigResource.class;
	}

	@Override
	protected Class<DeviceConfigurationManager> getManClass() {
		return DeviceConfigurationManager.class;
	}

	@Override
	protected Class<DeviceConfigurationModelProvider> getProClass() {
		return DeviceConfigurationModelProvider.class;
	}

	@Override
	protected Class<DeviceConfigurationModel> getModelClass() {
		return DeviceConfigurationModel.class;
	}

	@Override
	protected int getDefaultLimit() {
		return getMan().getDefualtLimit();
	}
}
