package solution.com.lattmat.convertor;

import org.modelmapper.ModelMapper;
import solution.com.lattmat.dto.RoleDto;
import solution.com.lattmat.entity.Role;

public class RoleConverter {

    private static ModelMapper modelMapper = new ModelMapper();

    public static RoleDto entityToDto(Role role){
        return modelMapper.map(role, RoleDto.class);
    }

    public static Role dtoToEntity(RoleDto dto){
        return modelMapper.map(dto, Role.class);
    }

}
