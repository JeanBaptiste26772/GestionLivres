package bf.isge.gsn.service;

import bf.isge.gsn.dto.LoginDto;
import bf.isge.gsn.model.User;

public interface AuthService {

    /**
     * Authentifie un utilisateur
     */
    boolean login(LoginDto loginDTO);

    /**
     * Déconnecte l'utilisateur actuel
     */
    void logout();

    /**
     * Vérifie si un utilisateur est authentifié
     */
    boolean isAuthenticated();

    /**
     * Vérifie l'authentification (lance une exception si non authentifié)
     */
    void checkAuthentication();

    /**
     * Récupère l'utilisateur actuellement connecté
     */
    User getCurrentUser();

    /**
     * Récupère le nom d'utilisateur de l'utilisateur connecté
     */
    String getCurrentUsername();
}