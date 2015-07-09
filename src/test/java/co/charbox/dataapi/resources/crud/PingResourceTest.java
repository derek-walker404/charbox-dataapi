package co.charbox.dataapi.resources.crud;

import co.charbox.dataapi.managers.PingResultsManager;
import co.charbox.domain.model.PingResultModel;
import co.charbox.domain.model.test.PingResultsModelProvider;

public class PingResourceTest extends CharbotCrudResourceTest<PingResultModel, PingResultsManager, PingResultsResource, PingResultsModelProvider>{

	@Override
	protected Class<PingResultsResource> getResourceClass() {
		return PingResultsResource.class;
	}

	@Override
	protected Class<PingResultsManager> getManClass() {
		return PingResultsManager.class;
	}

	@Override
	protected Class<PingResultsModelProvider> getProClass() {
		return PingResultsModelProvider.class;
	}

	@Override
	protected Class<PingResultModel> getModelClass() {
		return PingResultModel.class;
	}

	@Override
	protected int getDefaultLimit() {
		return getMan().getDefualtLimit();
	}
}
