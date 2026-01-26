package bf.isge.gsn.service;

import bf.isge.gsn.dto.LoginDto;
import bf.isge.gsn.dto.UserDto;
import bf.isge.gsn.model.User;

public interface UserService {

    UserDto login(LoginDto loginDTO);

    void logout();

    User getCurrentUser();

    UserDto getCurrentUserDTO();

    boolean isAuthenticated();
}