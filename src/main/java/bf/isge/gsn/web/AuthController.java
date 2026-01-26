package bf.isge.gsn.web;

import bf.isge.gsn.dto.LoginDto;
import bf.isge.gsn.exeption.InvalidCredentialsException;
import bf.isge.gsn.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/")
    public String home() {
        if (authService.isAuthenticated()) {
            return "redirect:/livres";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (authService.isAuthenticated()) {
            return "redirect:/livres";
        }
        model.addAttribute("loginDTO", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginDto loginDTO,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "login";
        }

        try {
            authService.login(loginDTO);
            log.info("Connexion réussie pour l'utilisateur: {}", loginDTO.getUsername());
            redirectAttributes.addFlashAttribute("success", "Connexion réussie ! Bienvenue " + loginDTO.getUsername());
            return "redirect:/livres";
        } catch (InvalidCredentialsException e) {
            log.warn("Échec de connexion pour l'utilisateur: {}", loginDTO.getUsername());
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        String username = "";
        try {
            username = authService.getCurrentUsername();
        } catch (Exception ignored) {}

        authService.logout();
        log.info("Déconnexion de l'utilisateur: {}", username);
        redirectAttributes.addFlashAttribute("success", "Déconnexion réussie !");
        return "redirect:/login";
    }
}