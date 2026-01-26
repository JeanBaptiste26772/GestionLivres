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

}
