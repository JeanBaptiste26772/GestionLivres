package bf.isge.gsn.service;

import bf.isge.gsn.dto.CreateLivreDto;
import bf.isge.gsn.dto.LivreDto;
import bf.isge.gsn.dto.UpdateLivreDto;
import bf.isge.gsn.exeption.BookNotFoundException;
import bf.isge.gsn.mapper.LivreMapper;
import bf.isge.gsn.model.Livre;
import bf.isge.gsn.model.User;
import bf.isge.gsn.repository.LivreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires de LivreServiceImpl")
class LivreServiceImplTest {

    @Mock
    private LivreRepository livreRepository;

    @Mock
    private LivreMapper livreMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private LivreServiceImpl livreService;

    private Livre livre;
    private LivreDto livreDto;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .username("testuser")
                .password("password123")
                .build();

        livre = Livre.builder()
                .identifiant(1)
                .titre("Clean Code")
                .auteur("Robert Martin")
                .prix(45.99)
                .createdAt(LocalDate.now())
                .archived(false)
                .createdBy(user)
                .build();

        livreDto = LivreDto.builder()
                .identifiant(1)
                .titre("Clean Code")
                .auteur("Robert Martin")
                .prix(45.99)
                .createdAt(LocalDate.now())
                .archived(false)
                .createdByUsername("testuser")
                .build();
    }

    @Test
    @DisplayName("Devrait récupérer tous les livres")
    void testGetAllLivres() {
        // Arrange
        List<Livre> livres = Arrays.asList(livre);
        when(livreRepository.findAll()).thenReturn(livres);
        when(livreMapper.toDTO(livre)).thenReturn(livreDto);

        // Act
        List<LivreDto> result = livreService.getAllLivres();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitre());
        verify(livreRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer les livres actifs (non archivés)")
    void testGetActiveLivres() {
        // Arrange
        Livre archivedLivre = Livre.builder()
                .identifiant(2)
                .titre("Archived Book")
                .auteur("Author")
                .prix(20.0)
                .archived(true)
                .build();

        List<Livre> livres = Arrays.asList(livre, archivedLivre);
        when(livreRepository.findAll()).thenReturn(livres);
        when(livreMapper.toDTO(livre)).thenReturn(livreDto);

        // Act
        List<LivreDto> result = livreService.getActiveLivres();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isArchived());
    }

    @Test
    @DisplayName("Devrait récupérer un livre par son ID")
    void testGetLivreById() {
        // Arrange
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(livreMapper.toDTO(livre)).thenReturn(livreDto);

        // Act
        LivreDto result = livreService.getLivreById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitre());
        verify(livreRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Devrait lever une exception si le livre n'existe pas")
    void testGetLivreByIdNotFound() {
        // Arrange
        when(livreRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> livreService.getLivreById(999));
    }

    @Test
    @DisplayName("Devrait créer un nouveau livre")
    void testCreateLivre() {
        // Arrange
        CreateLivreDto createDto = new CreateLivreDto();
        createDto.setTitre("Clean Code");
        createDto.setAuteur("Robert Martin");
        createDto.setPrix(45.99);

        Livre createdLivre = new Livre();
        createdLivre.setIdentifiant(1);
        createdLivre.setTitre("Clean Code");
        createdLivre.setAuteur("Robert Martin");
        createdLivre.setPrix(45.99);
        createdLivre.setArchived(false);
        createdLivre.setCreatedBy(user);

        when(livreRepository.save(any(Livre.class))).thenReturn(createdLivre);
        when(livreMapper.toDTO(any(Livre.class))).thenReturn(livreDto);

        // Act
        LivreDto result = livreService.createLivre(createDto);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitre());
        verify(livreRepository, times(1)).save(any(Livre.class));
    }

    @Test
    @DisplayName("Devrait mettre à jour un livre existant")
    void testUpdateLivre() {
        // Arrange
        UpdateLivreDto updateDto = new UpdateLivreDto();
        updateDto.setTitre("Clean Code Updated");
        updateDto.setAuteur("Robert Martin");
        updateDto.setPrix(50.00);
        updateDto.setArchived(false);

        Livre livreFromDb = Livre.builder()
                .identifiant(1)
                .titre("Clean Code")
                .auteur("Robert Martin")
                .prix(45.99)
                .archived(false)
                .createdBy(user)
                .build();

        LivreDto updatedDto = new LivreDto();
        updatedDto.setIdentifiant(1);
        updatedDto.setTitre("Clean Code Updated");
        updatedDto.setAuteur("Robert Martin");
        updatedDto.setPrix(50.00);
        updatedDto.setArchived(false);
        updatedDto.setCreatedByUsername("testuser");

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livreFromDb));
        when(livreRepository.save(any(Livre.class))).thenReturn(livreFromDb);
        when(livreMapper.toDTO(any(Livre.class))).thenReturn(updatedDto);

        // Act
        LivreDto result = livreService.updateLivre(1, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code Updated", result.getTitre());
        verify(livreRepository, times(1)).findById(1L);
        verify(livreRepository, times(1)).save(any(Livre.class));
    }

    @Test
    @DisplayName("Devrait supprimer un livre")
    void testDeleteLivre() {
        // Arrange
        when(livreRepository.existsById(1L)).thenReturn(true);
        doNothing().when(livreRepository).deleteById(1L);

        // Act
        livreService.deleteLivre(1);

        // Assert
        verify(livreRepository, times(1)).existsById(1L);
        verify(livreRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Devrait archiver un livre")
    void testArchiveLivre() {
        // Arrange
        Livre archivedLivre = livre;
        archivedLivre.setArchived(true);

        LivreDto archivedDto = livreDto;
        archivedDto.setArchived(true);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(livreRepository.save(any(Livre.class))).thenReturn(archivedLivre);
        when(livreMapper.toDTO(archivedLivre)).thenReturn(archivedDto);

        // Act
        LivreDto result = livreService.archiveLivre(1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isArchived());
    }

    @Test
    @DisplayName("Devrait dés-archiver un livre")
    void testUnarchiveLivre() {
        // Arrange
        Livre archivedLivre = livre;
        archivedLivre.setArchived(true);

        Livre unarchivedLivre = livre;
        unarchivedLivre.setArchived(false);

        LivreDto unarchivedDto = livreDto;
        unarchivedDto.setArchived(false);

        when(livreRepository.findById(1L)).thenReturn(Optional.of(archivedLivre));
        when(livreRepository.save(any(Livre.class))).thenReturn(unarchivedLivre);
        when(livreMapper.toDTO(unarchivedLivre)).thenReturn(unarchivedDto);

        // Act
        LivreDto result = livreService.unarchiveLivre(1);

        // Assert
        assertNotNull(result);
        assertFalse(result.isArchived());
    }
}
