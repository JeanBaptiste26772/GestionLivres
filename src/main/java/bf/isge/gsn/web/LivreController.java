package bf.isge.gsn.web;

import bf.isge.gsn.dto.CreateLivreDto;
import bf.isge.gsn.dto.LivreDto;
import bf.isge.gsn.dto.UpdateLivreDto;
import bf.isge.gsn.exeption.BookNotFoundException;
import bf.isge.gsn.service.AuthService;
import bf.isge.gsn.service.LivreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/livres")
@RequiredArgsConstructor
@Slf4j
public class LivreController {

    private final LivreService livreService;
    private final AuthService authService;

    @GetMapping
    public String listLivres(Model model) {
        log.info("WEB: Affichage de la liste des livres");
        model.addAttribute("livres", livreService.getActiveLivres());
        model.addAttribute("authenticated", authService.isAuthenticated());
        if (authService.isAuthenticated()) {
            model.addAttribute("username", authService.getCurrentUsername());
        }
        model.addAttribute("totalLivres", livreService.getActiveLivres());
        return "livres";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model, RedirectAttributes redirectAttributes) {
        try {
            authService.checkAuthentication();
            model.addAttribute("livre", new CreateLivreDto());
            model.addAttribute("isEdit", false);
            return "livre-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Vous devez vous connecter pour ajouter un livre");
            return "redirect:/login";
        }
    }

    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            authService.checkAuthentication();
            LivreDto livre = livreService.getLivreById(id);

            // Convertir LivreDTO en UpdateLivreDTO
            UpdateLivreDto updateDTO = new UpdateLivreDto();
            updateDTO.setTitre(livre.getTitre());
            updateDTO.setAuteur(livre.getAuteur());
            updateDTO.setPrix(livre.getPrix());
            updateDTO.setArchived(livre.isArchived());

            model.addAttribute("livre", updateDTO);
            model.addAttribute("livreId", id);
            model.addAttribute("isEdit", true);
            return "livre-form";
        } catch (BookNotFoundException e) {
            log.error("Livre non trouvé: {}", id);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/livres";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Vous devez vous connecter pour modifier un livre");
            return "redirect:/login";
        }
    }

    @PostMapping("/enregistrer")
    public String createLivre(@Valid @ModelAttribute("livre") CreateLivreDto livre,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            authService.checkAuthentication();

            if (result.hasErrors()) {
                log.warn("Erreurs de validation lors de la création d'un livre");
                model.addAttribute("isEdit", false);
                return "livre-form";
            }

            livreService.createLivre(livre);
            log.info("Livre créé avec succès: {}", livre.getTitre());
            redirectAttributes.addFlashAttribute("success", "Livre créé avec succès !");
            return "redirect:/livres";
        } catch (Exception e) {
            log.error("Erreur lors de la création du livre", e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création du livre");
            return "redirect:/livres";
        }
    }

    @PostMapping("/modifier/{id}")
    public String updateLivre(@PathVariable Integer id,
                              @Valid @ModelAttribute("livre") UpdateLivreDto livre,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            authService.checkAuthentication();

            if (result.hasErrors()) {
                log.warn("Erreurs de validation lors de la modification du livre: {}", id);
                model.addAttribute("livreId", id);
                model.addAttribute("isEdit", true);
                return "livre-form";
            }

            livreService.updateLivre(id, livre);
            log.info("Livre modifié avec succès: {}", id);
            redirectAttributes.addFlashAttribute("success", "Livre modifié avec succès !");
            return "redirect:/livres";
        } catch (BookNotFoundException e) {
            log.error("Livre non trouvé: {}", id);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/livres";
        } catch (Exception e) {
            log.error("Erreur lors de la modification du livre", e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la modification du livre");
            return "redirect:/livres";
        }
    }

    @GetMapping("/supprimer/{id}")
    public String deleteLivre(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            authService.checkAuthentication();
            livreService.deleteLivre(id);
            log.info("Livre supprimé avec succès: {}", id);
            redirectAttributes.addFlashAttribute("success", "Livre supprimé avec succès !");
        } catch (BookNotFoundException e) {
            log.error("Livre non trouvé: {}", id);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du livre", e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du livre");
        }
        return "redirect:/livres";
    }

    @GetMapping("/archiver/{id}")
    public String archiveLivre(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            authService.checkAuthentication();
            livreService.archiveLivre(id);
            log.info("Livre archivé avec succès: {}", id);
            redirectAttributes.addFlashAttribute("success", "Livre archivé avec succès !");
        } catch (BookNotFoundException e) {
            log.error("Livre non trouvé: {}", id);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de l'archivage du livre", e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'archivage du livre");
        }
        return "redirect:/livres";
    }
}