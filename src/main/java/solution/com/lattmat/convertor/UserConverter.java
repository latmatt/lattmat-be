package solution.com.lattmat.convertor;

import org.modelmapper.ModelMapper;
import solution.com.lattmat.dto.UserDto;
import solution.com.lattmat.model.Users;

public class UserConverter {

    private static ModelMapper modelMapper = new ModelMapper();

    public static UserDto entityToDto(Users user){
        return modelMapper.map(user, UserDto.class);
    }

    public static Users dtoToEntity(UserDto user){
        return modelMapper.map(user, Users.class);
    }

}
