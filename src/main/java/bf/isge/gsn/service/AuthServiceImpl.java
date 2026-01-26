package bf.isge.gsn.service;

import bf.isge.gsn.dto.LoginDto;
import bf.isge.gsn.exeption.InvalidCredentialsException;
import bf.isge.gsn.model.User;
import bf.isge.gsn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private static final String SESSION_USER_KEY = "CURRENT_USER";

    @Override
    public boolean login(LoginDto loginDTO) {
        log.info("Tentative de connexion pour l'utilisateur: {}", loginDTO.getUsername());

        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Nom d'utilisateur ou mot de passe incorrect"));

        // ⚠️ EN PRODUCTION: Utiliser BCryptPasswordEncoder
        // Pour ce TP: comparaison simple
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new InvalidCredentialsException("Nom d'utilisateur ou mot de passe incorrect");
        }

        // Stocker l'utilisateur dans la session
        HttpSession session = getSession();
        session.setAttribute(SESSION_USER_KEY, user);

        log.info("Connexion réussie pour l'utilisateur: {}", user.getUsername());
        return true;
    }

    @Override
    public void logout() {
        log.info("Déconnexion de l'utilisateur");
        HttpSession session = getSession();
        session.invalidate();
    }

    @Override
    public boolean isAuthenticated() {
        try {
            HttpSession session = getSession();
            User user = (User) session.getAttribute(SESSION_USER_KEY);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void checkAuthentication() {
        if (!isAuthenticated()) {
            throw new InvalidCredentialsException("Vous devez vous connecter pour accéder à cette ressource");
        }
    }

    @Override
    public User getCurrentUser() {
        HttpSession session = getSession();
        User user = (User) session.getAttribute(SESSION_USER_KEY);

        if (user == null) {
            throw new InvalidCredentialsException("Aucun utilisateur connecté");
        }

        // Récupérer l'utilisateur depuis la base pour avoir les données à jour
        return userRepository.findById(user.getId())
                .orElseThrow(() -> new InvalidCredentialsException("Utilisateur introuvable"));
    }

    @Override
    public String getCurrentUsername() {
        User user = getCurrentUser();
        return user.getUsername();
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}