package co.charbox.dataapi.auth;

import org.springframework.stereotype.Component;

import co.charbox.domain.model.RoleModel;

import com.tpofof.dwa.auth.RoleValidator;

@Component
public class CharbotRoleValidator extends RoleValidator<RoleModel> {

}
