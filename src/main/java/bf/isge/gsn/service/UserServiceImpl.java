package bf.isge.gsn.service;

import bf.isge.gsn.dto.LoginDto;
import bf.isge.gsn.dto.UserDto;
import bf.isge.gsn.exeption.InvalidCredentialsException;
import bf.isge.gsn.exeption.UserNotFoundException;
import bf.isge.gsn.mapper.UserMapper;
import bf.isge.gsn.model.User;
import bf.isge.gsn.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String SESSION_USER_KEY = "CURRENT_USER_ID";

    @Override
    public UserDto login(LoginDto loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Nom d'utilisateur ou mot de passe incorrect"));

        // ⚠️ EN PRODUCTION: Utiliser BCryptPasswordEncoder
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new InvalidCredentialsException("Nom d'utilisateur ou mot de passe incorrect");
        }

        // Stocker l'ID de l'utilisateur dans la session
        HttpSession session = getSession();
        session.setAttribute(SESSION_USER_KEY, user.getId());

        return userMapper.toDTO(user);
    }

    @Override
    public void logout() {
        HttpSession session = getSession();
        session.invalidate();
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        HttpSession session = getSession();
        Integer userId = (Integer) session.getAttribute(SESSION_USER_KEY);

        if (userId == null) {
            throw new InvalidCredentialsException("Vous devez vous connecter");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUserDTO() {
        User user = getCurrentUser();
        return userMapper.toDTO(user);
    }

    @Override
    public boolean isAuthenticated() {
        try {
            HttpSession session = getSession();
            Integer userId = (Integer) session.getAttribute(SESSION_USER_KEY);
            return userId != null;
        } catch (Exception e) {
            return false;
        }
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}