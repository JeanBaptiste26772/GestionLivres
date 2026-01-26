package bf.isge.gsn.rest;

import bf.isge.gsn.dto.CreateLivreDto;
import bf.isge.gsn.dto.LivreDto;
import bf.isge.gsn.dto.UpdateLivreDto;
import bf.isge.gsn.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livres")
@RequiredArgsConstructor
@Tag(name = "Livres", description = "API de gestion des livres")
public class LivreRestController {

    private final LivreService livreService;

    @GetMapping
    @Operation(summary = "Récupérer tous les livres")
    public ResponseEntity<List<LivreDto>> getAllBooks() {
        return ResponseEntity.ok(livreService.getAllLivres());
    }



    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livre par son ID")
    public ResponseEntity<LivreDto> getLivreById(@PathVariable Integer id) {
        return ResponseEntity.ok(livreService.getLivreById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau livre")
    public ResponseEntity<LivreDto> createLivre(@Valid @RequestBody CreateLivreDto createDTO) {
        LivreDto savedLivre = livreService.createLivre(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLivre);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un livre existant")
    public ResponseEntity<LivreDto> updateLivre(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateLivreDto updateDTO) {
        LivreDto updatedLivre = livreService.updateLivre(id, updateDTO);
        return ResponseEntity.ok(updatedLivre);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livre")
    public ResponseEntity<Void> deleteLivre(@PathVariable Integer id) {
        livreService.deleteLivre(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archiver")
    @Operation(summary = "Archiver un livre")
    public ResponseEntity<LivreDto> archiveLivre(@PathVariable Integer id) {
        LivreDto archivedLivre = livreService.archiveLivre(id);
        return ResponseEntity.ok(archivedLivre);
    }

    @PatchMapping("/{id}/desarchiver")
    @Operation(summary = "Désarchiver un livre")
    public ResponseEntity<LivreDto> unarchiveLivre(@PathVariable Integer id) {
        LivreDto unarchivedLivre = livreService.unarchiveLivre(id);
        return ResponseEntity.ok(unarchivedLivre);
    }

    @GetMapping("/recherche/titre")
    @Operation(summary = "Rechercher des livres par titre")
    public ResponseEntity<List<LivreDto>> searchByTitre(@RequestParam String titre) {
        return ResponseEntity.ok(livreService.searchByTitre(titre));
    }

    @GetMapping("/recherche/auteur")
    @Operation(summary = "Rechercher des livres par auteur")
    public ResponseEntity<List<LivreDto>> searchByAuteur(@RequestParam String auteur) {
        return ResponseEntity.ok(livreService.searchByAuteur(auteur));
    }

    @GetMapping("/mes-livres")
    @Operation(summary = "Récupérer les livres de l'utilisateur connecté")
    public ResponseEntity<List<LivreDto>> getMyLivres() {
        return ResponseEntity.ok(livreService.getLivresByCurrentUser());
    }
}