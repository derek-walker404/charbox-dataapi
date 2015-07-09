package co.charbox.dataapi.resources.crud;

import co.charbox.dataapi.managers.DeviceVersionManager;
import co.charbox.domain.model.DeviceVersionModel;
import co.charbox.domain.model.test.DeviceVersionModelProvider;

public class DeviceVersionResourceTest extends CharbotCrudResourceTest<DeviceVersionModel, DeviceVersionManager, DeviceVersionResource, DeviceVersionModelProvider> {

	protected Class<DeviceVersionResource> getResourceClass() {
		return DeviceVersionResource.class;
	}

	@Override
	protected Class<DeviceVersionManager> getManClass() {
		return DeviceVersionManager.class;
	}

	@Override
	protected Class<DeviceVersionModelProvider> getProClass() {
		return DeviceVersionModelProvider.class;
	}

	@Override
	protected Class<DeviceVersionModel> getModelClass() {
		return DeviceVersionModel.class;
	}

	@Override
	protected int getDefaultLimit() {
		return getMan().getDefualtLimit();
	}

}
