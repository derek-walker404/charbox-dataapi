package co.charbox.dataapi.resources.crud;

import co.charbox.domain.model.auth.CharbotAuthModel;

import com.google.common.base.Optional;
import com.tpofof.core.data.IPersistentModel;
import com.tpofof.core.data.dao.context.PrincipalSearchContext;
import com.tpofof.core.managers.IModelManager;
import com.tpofof.dwa.resources.AbstractAuthProtectedCrudResource;
import com.tpofof.dwa.resources.IDwaResource;

public abstract class CharbotAuthProtectedCrudResource<ModelT extends IPersistentModel<ModelT, Integer>, ManagerT extends IModelManager<ModelT, Integer, PrincipalSearchContext, PrincipalSearchContext>> 
		extends AbstractAuthProtectedCrudResource<ModelT, Integer, ManagerT, CharbotAuthModel, PrincipalSearchContext, PrincipalSearchContext>
		implements IDwaResource {

	public CharbotAuthProtectedCrudResource(ManagerT man, Class<ModelT> modelClass) {
		super(man, modelClass);
	}

	@Override
	protected PrincipalSearchContext getContext(CharbotAuthModel auth, Optional<Integer> limit, Optional<Integer> offset, Optional<String> sort) {
		return PrincipalSearchContext.builder()
				.window(req().searchWindow(limit, offset))
				.sort(req().simpleSort(sort))
				.build();
	}

	@Override
	protected PrincipalSearchContext getAuthContext(CharbotAuthModel auth) {
		return PrincipalSearchContext.builder()
				.build();
	}
}
