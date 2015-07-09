package co.charbox.dataapi.resources.crud;

import com.google.common.base.Optional;
import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.managers.IModelManager;
import com.tpofof.core.security.IAuthModel;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.IDwaResource;

public abstract class CharbotAuthProtectedCrudResource<ModelT extends IPersistentModel<ModelT, Integer>, ManagerT extends IModelManager<ModelT, Integer, SimpleSearchContext, SimpleSearchContext>> 
		extends AbstractAuthProtectedCrudResource<ModelT, Integer, ManagerT, IAuthModel, SimpleSearchContext, SimpleSearchContext>
		implements IDwaResource {

	public CharbotAuthProtectedCrudResource(ManagerT man, Class<ModelT> modelClass) {
		super(man, modelClass);
	}

	@Override
	protected SimpleSearchContext getContext(IAuthModel auth, Optional<Integer> limit, Optional<Integer> offset, Optional<String> sort) {
		return SimpleSearchContext.builder()
				.window(req().searchWindow(limit, offset))
				.sort(req().simpleSort(sort))
				.build();
	}

	@Override
	protected SimpleSearchContext getAuthContext(IAuthModel auth) {
		return SimpleSearchContext.builder()
				.build();
	}
}
