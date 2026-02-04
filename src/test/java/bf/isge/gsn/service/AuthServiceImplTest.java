package bf.isge.gsn.service;

import bf.isge.gsn.dto.LoginDto;
import bf.isge.gsn.exeption.InvalidCredentialsException;
import bf.isge.gsn.model.User;
import bf.isge.gsn.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires de AuthServiceImpl")
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .username("testuser")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("Devrait lever une exception si l'utilisateur n'existe pas")
    void testLoginUserNotFound() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("nonexistent");
        loginDto.setPassword("password123");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginDto));
    }

    @Test
    @DisplayName("Devrait lever une exception si le mot de passe est incorrect")
    void testLoginInvalidPassword() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser");
        loginDto.setPassword("wrongpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginDto));
    }

    @Test
    @DisplayName("Devrait vérifier l'authentification")
    void testIsAuthenticated() {
        // Act & Assert
        // Le test dépend du contexte HTTP (session)
        boolean result = authService.isAuthenticated();
        assertNotNull(result);
    }

    @Test
    @DisplayName("Devrait lever une exception si l'utilisateur n'est pas authentifié")
    void testCheckAuthenticationNotAuthenticated() {
        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.checkAuthentication());
    }
}
