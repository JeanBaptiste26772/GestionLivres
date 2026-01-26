package bf.isge.gsn.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLivreDto {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 2, max = 200, message = "Le titre doit contenir entre 2 et 200 caractères")
    private String titre;

    @NotBlank(message = "L'auteur est obligatoire")
    @Size(min = 2, max = 100, message = "L'auteur doit contenir entre 2 et 100 caractères")
    private String auteur;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être positif")
    @DecimalMax(value = "100000.0", message = "Le prix ne peut pas dépasser 100000")
    private Double prix;

    private Boolean archived;
}