package bf.isge.gsn.mapper;

import bf.isge.gsn.dto.CreateLivreDto;
import bf.isge.gsn.dto.LivreDto;
import bf.isge.gsn.dto.UpdateLivreDto;
import bf.isge.gsn.model.Livre;
import bf.isge.gsn.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires de LivreMapper")
class LivreMapperTest {

    private LivreMapper livreMapper;
    private Livre livre;
    private User user;

    @BeforeEach
    void setUp() {
        livreMapper = new LivreMapper();

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
    }

    @Test
    @DisplayName("Devrait convertir une entité Livre en LivreDto")
    void testToDTO() {
        // Act
        LivreDto result = livreMapper.toDTO(livre);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getIdentifiant());
        assertEquals("Clean Code", result.getTitre());
        assertEquals("Robert Martin", result.getAuteur());
        assertEquals(45.99, result.getPrix());
        assertFalse(result.isArchived());
        assertEquals("testuser", result.getCreatedByUsername());
    }

    @Test
    @DisplayName("Devrait retourner null si le livre est null")
    void testToDTOWithNullLivre() {
        // Act
        LivreDto result = livreMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Devrait convertir une liste d'entités en liste de DTOs")
    void testToDTOList() {
        // Arrange
        Livre livre2 = Livre.builder()
                .identifiant(2)
                .titre("Design Patterns")
                .auteur("Gang of Four")
                .prix(55.99)
                .archived(false)
                .build();

        List<Livre> livres = Arrays.asList(livre, livre2);

        // Act
        List<LivreDto> result = livreMapper.toDTOList(livres);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Clean Code", result.get(0).getTitre());
        assertEquals("Design Patterns", result.get(1).getTitre());
    }

    @Test
    @DisplayName("Devrait retourner null si la liste est null")
    void testToDTOListWithNullList() {
        // Act
        List<LivreDto> result = livreMapper.toDTOList(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Devrait convertir un CreateLivreDto en entité Livre")
    void testToEntity() {
        // Arrange
        CreateLivreDto createDto = new CreateLivreDto();
        createDto.setTitre("Clean Code");
        createDto.setAuteur("Robert Martin");
        createDto.setPrix(45.99);

        // Act
        Livre result = livreMapper.toEntity(createDto, user);

        // Assert
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitre());
        assertEquals("Robert Martin", result.getAuteur());
        assertEquals(45.99, result.getPrix());
        assertFalse(result.isArchived());
        assertEquals(user, result.getCreatedBy());
    }

    @Test
    @DisplayName("Devrait retourner null si le CreateLivreDto est null")
    void testToEntityWithNullDto() {
        // Act
        Livre result = livreMapper.toEntity(null, user);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Devrait mettre à jour une entité Livre à partir d'un UpdateLivreDto")
    void testUpdateEntityFromDTO() {
        // Arrange
        UpdateLivreDto updateDto = new UpdateLivreDto();
        updateDto.setTitre("Clean Code: A Handbook of Agile Software Craftsmanship");
        updateDto.setAuteur("Robert Martin");
        updateDto.setPrix(50.00);
        updateDto.setArchived(true);

        // Act
        livreMapper.updateEntityFromDTO(updateDto, livre);

        // Assert
        assertEquals("Clean Code: A Handbook of Agile Software Craftsmanship", livre.getTitre());
        assertEquals(50.00, livre.getPrix());
        assertTrue(livre.isArchived());
    }

    @Test
    @DisplayName("Devrait ne rien faire si le UpdateLivreDto est null")
    void testUpdateEntityFromDTOWithNullDto() {
        // Arrange
        Livre originalLivre = Livre.builder()
                .titre("Clean Code")
                .auteur("Robert Martin")
                .prix(45.99)
                .build();

        // Act
        livreMapper.updateEntityFromDTO(null, originalLivre);

        // Assert
        assertEquals("Clean Code", originalLivre.getTitre());
        assertEquals(45.99, originalLivre.getPrix());
    }

    @Test
    @DisplayName("Devrait gérer les livres avec createdBy null")
    void testToDTOWithNullCreatedBy() {
        // Arrange
        Livre livreWithoutUser = Livre.builder()
                .identifiant(1)
                .titre("Clean Code")
                .auteur("Robert Martin")
                .prix(45.99)
                .archived(false)
                .createdBy(null)
                .build();

        // Act
        LivreDto result = livreMapper.toDTO(livreWithoutUser);

        // Assert
        assertNotNull(result);
        assertEquals("Inconnu", result.getCreatedByUsername());
    }
}
