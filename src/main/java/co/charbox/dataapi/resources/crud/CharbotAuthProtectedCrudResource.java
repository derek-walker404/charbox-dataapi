package co.charbox.dataapi.resources.crud;

import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.model.auth.CharbotAuthModel;

import com.google.common.base.Optional;
import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.managers.IModelManager;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.IDwaResource;

public abstract class CharbotAuthProtectedCrudResource<ModelT extends IPersistentModel<ModelT, Integer>, ManagerT extends IModelManager<ModelT, Integer, CharbotSearchContext, CharbotSearchContext>> 
		extends AbstractAuthProtectedCrudResource<ModelT, Integer, ManagerT, CharbotAuthModel, CharbotSearchContext, CharbotSearchContext>
		implements IDwaResource {

	public CharbotAuthProtectedCrudResource(ManagerT man, Class<ModelT> modelClass) {
		super(man, modelClass);
	}

	@Override
	protected CharbotSearchContext getContext(CharbotAuthModel auth, Optional<Integer> limit, Optional<Integer> offset, Optional<String> sort) {
		return CharbotSearchContext.builder()
				.window(req().searchWindow(limit, offset))
				.sort(req().simpleSort(sort))
				.principal(auth)
				.build();
	}

	@Override
	protected CharbotSearchContext getAuthContext(CharbotAuthModel auth) {
		return CharbotSearchContext.builder()
				.principal(auth)
				.build();
	}
}
