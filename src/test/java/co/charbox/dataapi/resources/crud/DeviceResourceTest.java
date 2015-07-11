package co.charbox.dataapi.resources.crud;

import org.junit.Test;

import co.charbox.dataapi.managers.DeviceManager;
import co.charbox.domain.model.DeviceModel;
import co.charbox.domain.model.test.DeviceModelProvider;

import com.tpofof.dwa.error.HttpBadRequestException;

public class DeviceResourceTest extends CharbotCrudResourceTest<DeviceModel, DeviceManager, DeviceResource, DeviceModelProvider> {

	@Override
	protected Class<DeviceResource> getResourceClass() {
		return DeviceResource.class;
	}

	@Override
	protected Class<DeviceManager> getManClass() {
		return DeviceManager.class;
	}

	@Override
	protected Class<DeviceModelProvider> getProClass() {
		return DeviceModelProvider.class;
	}

	@Override
	protected Class<DeviceModel> getModelClass() {
		return DeviceModel.class;
	}

	@Override
	protected int getDefaultLimit() {
		return getMan().getDefualtLimit();
	}

	@Override
	@Test(expected=HttpBadRequestException.class)
	public void testPost() {
		getResource().post(getAuthModel(), null, getRequest());
	}
}
