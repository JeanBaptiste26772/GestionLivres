package bf.isge.gsn.service;

import bf.isge.gsn.dto.CreateLivreDto;
import bf.isge.gsn.dto.LivreDto;
import bf.isge.gsn.dto.UpdateLivreDto;

import java.util.List;

public interface LivreService {

    List<LivreDto> getAllLivres();

    List<LivreDto> getActiveLivres();

    LivreDto getLivreById(Integer id);

    LivreDto createLivre(CreateLivreDto createDTO);

    LivreDto updateLivre(Integer id, UpdateLivreDto updateDTO);

    void deleteLivre(Integer id);

    LivreDto archiveLivre(Integer id);

    LivreDto unarchiveLivre(Integer id);

    List<LivreDto> searchByTitre(String titre);

    List<LivreDto> searchByAuteur(String auteur);

    List<LivreDto> getLivresByCurrentUser();
}