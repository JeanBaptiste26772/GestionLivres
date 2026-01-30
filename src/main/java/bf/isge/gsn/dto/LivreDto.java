package bf.isge.gsn.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivreDto {
    private Integer identifiant;
    private String titre;
    private String auteur;
    private Double prix;
    private LocalDate createdAt;
    private boolean archived;
    private String createdByUsername;  // AJOUT: champ manquant utilisé dans le template

    // Méthode pour compatibilité (au cas où certains endroits utilisent 'id' au lieu de 'identifiant')
    public Integer getId() {
        return identifiant;
    }

    public void setId(Integer id) {
        this.identifiant = id;
    }
}