package solution.com.lattmat.service;

import solution.com.lattmat.dto.RoleDto;
import solution.com.lattmat.enumeration.UserRoleType;

public interface RoleService {
    RoleDto getRoleByRoleType(UserRoleType userRoleType);
}
