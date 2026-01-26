package bf.isge.gsn.mapper;

import bf.isge.gsn.dto.CreateLivreDto;
import bf.isge.gsn.dto.LivreDto;
import bf.isge.gsn.dto.UpdateLivreDto;
import bf.isge.gsn.model.Livre;
import bf.isge.gsn.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LivreMapper {

    /**
     * Convertit une entité Livre en LivreDTO
     */
    public LivreDto toDTO(Livre livre) {
        if (livre == null) {
            return null;
        }

        return LivreDto.builder()
                .identifiant(livre.getIdentifiant())
                .titre(livre.getTitre())
                .auteur(livre.getAuteur())
                .prix(livre.getPrix())
                .createdAt(livre.getCreatedAt())
                .archived(livre.isArchived())
                .build();
    }

    /**
     * Convertit une liste d'entités Livre en liste de LivreDTO
     */
    public List<LivreDto> toDTOList(List<Livre> livres) {
        if (livres == null) {
            return null;
        }

        return livres.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit un CreateLivreDTO en entité Livre
     */
    public Livre toEntity(CreateLivreDto dto, User createdBy) {
        if (dto == null) {
            return null;
        }

        return Livre.builder()
                .titre(dto.getTitre())
                .auteur(dto.getAuteur())
                .prix(dto.getPrix())
                .archived(false)
                .createdBy(createdBy)
                .build();
    }

    /**
     * Met à jour une entité Livre à partir d'un UpdateLivreDTO
     */
    public void updateEntityFromDTO(UpdateLivreDto dto, Livre livre) {
        if (dto == null || livre == null) {
            return;
        }

        livre.setTitre(dto.getTitre());
        livre.setAuteur(dto.getAuteur());
        livre.setPrix(dto.getPrix());

        if (dto.getArchived() != null) {
            livre.setArchived(dto.getArchived());
        }
    }
}