package solution.com.lattmat.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import solution.com.lattmat.convertor.RoleConverter;
import solution.com.lattmat.dto.RoleDto;
import solution.com.lattmat.entity.Role;
import solution.com.lattmat.enumeration.UserRoleType;
import solution.com.lattmat.exception.domain.UserRoleNotFoundException;
import solution.com.lattmat.repo.RoleRepository;
import solution.com.lattmat.service.RoleService;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Override
    public RoleDto getRoleByRoleType(UserRoleType userRoleType) {
        Role role = roleRepository.findRoleByRoleType(userRoleType)
                .orElseGet(() -> roleRepository.save(new Role(userRoleType)));

        return RoleConverter.entityToDto(role);
    }

}
