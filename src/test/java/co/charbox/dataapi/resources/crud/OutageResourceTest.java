package co.charbox.dataapi.resources.crud;

import co.charbox.dataapi.managers.OutageManager;
import co.charbox.domain.model.OutageModel;
import co.charbox.domain.model.test.OutageModelProvider;

public class OutageResourceTest extends CharbotCrudResourceTest<OutageModel, OutageManager, OutageResource, OutageModelProvider> {

	@Override
	protected Class<OutageResource> getResourceClass() {
		return OutageResource.class;
	}

	@Override
	protected Class<OutageManager> getManClass() {
		return OutageManager.class;
	}

	@Override
	protected Class<OutageModelProvider> getProClass() {
		return OutageModelProvider.class;
	}

	@Override
	protected Class<OutageModel> getModelClass() {
		return OutageModel.class;
	}

	@Override
	protected int getDefaultLimit() {
		return getMan().getDefualtLimit();
	}

}
