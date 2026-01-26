package bf.isge.gsn.service;

import bf.isge.gsn.dto.CreateLivreDto;
import bf.isge.gsn.dto.LivreDto;
import bf.isge.gsn.dto.UpdateLivreDto;
import bf.isge.gsn.exeption.BookNotFoundException;
import bf.isge.gsn.mapper.LivreMapper;
import bf.isge.gsn.model.Livre;
import bf.isge.gsn.repository.LivreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LivreServiceImpl implements LivreService {

    private final LivreRepository livreRepository;
    private final LivreMapper livreMapper;

    @Override
    public List<LivreDto> getAllLivres() {
        return livreRepository.findAll()
                .stream()
                .map(livreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LivreDto> getActiveLivres() {
        return livreRepository.findAll()
                .stream()
                .filter(livre -> !livre.isArchived())
                .map(livreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LivreDto getLivreById(Integer id) {
        Livre livre = livreRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));
        return livreMapper.toDTO(livre);
    }

    @Override
    public LivreDto createLivre(CreateLivreDto createDTO) {
        Livre livre = Livre.builder()
                .titre(createDTO.getTitre())
                .auteur(createDTO.getAuteur())
                .prix(createDTO.getPrix())
                .archived(false)
                .build();

        Livre savedLivre = livreRepository.save(livre);
        return livreMapper.toDTO(savedLivre);
    }

    @Override
    public LivreDto updateLivre(Integer id, UpdateLivreDto updateDTO) {
        Livre livre = livreRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));

        if (updateDTO.getTitre() != null) {
            livre.setTitre(updateDTO.getTitre());
        }
        if (updateDTO.getAuteur() != null) {
            livre.setAuteur(updateDTO.getAuteur());
        }
        if (updateDTO.getPrix() != null) {
            livre.setPrix(updateDTO.getPrix());
        }

        Livre updatedLivre = livreRepository.save(livre);
        return livreMapper.toDTO(updatedLivre);
    }

    @Override
    public void deleteLivre(Integer id) {
        if (!livreRepository.existsById(Long.valueOf(id))) {
            throw new BookNotFoundException("Livre non trouvé avec l'ID: " + id);
        }
        livreRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public LivreDto archiveLivre(Integer id) {
        Livre livre = livreRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));
        livre.setArchived(true);
        Livre archivedLivre = livreRepository.save(livre);
        return livreMapper.toDTO(archivedLivre);
    }

    @Override
    public LivreDto unarchiveLivre(Integer id) {
        Livre livre = livreRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new BookNotFoundException("Livre non trouvé avec l'ID: " + id));
        livre.setArchived(false);
        Livre unarchivedLivre = livreRepository.save(livre);
        return livreMapper.toDTO(unarchivedLivre);
    }

    @Override
    public List<LivreDto> searchByTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre)
                .stream()
                .map(livreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LivreDto> searchByAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur)
                .stream()
                .map(livreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LivreDto> getLivresByCurrentUser() {
        // TODO: Implémenter avec l'utilisateur authentifié
        return List.of();
    }
}