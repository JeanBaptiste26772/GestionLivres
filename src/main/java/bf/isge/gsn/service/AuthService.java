package bf.isge.gsn.service;

import bf.isge.gsn.dto.LoginDto;
import bf.isge.gsn.model.User;

public interface AuthService {
    boolean login(LoginDto loginDTO);
    void logout();
    boolean isAuthenticated();
    void checkAuthentication();
    User getCurrentUser();
    String getCurrentUsername();
}