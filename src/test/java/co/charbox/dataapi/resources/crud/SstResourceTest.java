package co.charbox.dataapi.resources.crud;

import co.charbox.dataapi.managers.SstResultManager;
import co.charbox.domain.model.SstResultsModel;
import co.charbox.domain.model.test.SstResultsModelProvider;

public class SstResourceTest extends CharbotCrudResourceTest<SstResultsModel, SstResultManager, SstResultResource, SstResultsModelProvider> {

	@Override
	protected Class<SstResultResource> getResourceClass() {
		return SstResultResource.class;
	}

	@Override
	protected Class<SstResultManager> getManClass() {
		return SstResultManager.class;
	}

	@Override
	protected Class<SstResultsModelProvider> getProClass() {
		return SstResultsModelProvider.class;
	}

	@Override
	protected Class<SstResultsModel> getModelClass() {
		return SstResultsModel.class;
	}

	@Override
	protected int getDefaultLimit() {
		return getMan().getDefualtLimit();
	}
}
