package co.charbox.dataapi.resources.crud;

import co.charbox.dataapi.managers.HeartbeatManager;
import co.charbox.domain.model.HeartbeatModel;
import co.charbox.domain.model.test.HeartbeatModelProvider;

public class HeartbeatResourceTest extends CharbotCrudResourceTest<HeartbeatModel, HeartbeatManager, HeartbeatResource, HeartbeatModelProvider> {

	@Override
	protected Class<HeartbeatResource> getResourceClass() {
		return HeartbeatResource.class;
	}

	@Override
	protected Class<HeartbeatManager> getManClass() {
		return HeartbeatManager.class;
	}

	@Override
	protected Class<HeartbeatModelProvider> getProClass() {
		return HeartbeatModelProvider.class;
	}

	@Override
	protected Class<HeartbeatModel> getModelClass() {
		return HeartbeatModel.class;
	}

	@Override
	protected int getDefaultLimit() {
		return getMan().getDefualtLimit();
	}

}
