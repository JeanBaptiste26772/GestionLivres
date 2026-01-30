package bf.isge.gsn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "livres")
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifiant;
    private String titre;
    private String auteur;
    private double prix;

    private LocalDate createdAt;
    private boolean archived;
    @ManyToOne
    private User createdBy;



    @PrePersist
    public void initializeCreatedAt() {
        this.createdAt = LocalDate.now();
    }
}