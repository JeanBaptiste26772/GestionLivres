# GestionLivres - API REST Gestion de Bibliothèque

## Vue d'ensemble
GestionLivres est une application Spring Boot permettant de gérer une bibliothèque de livres avec un système d'authentification utilisateur, des opérations CRUD et une interface web.

## Architecture

### Architecture en couches
L'application suit une architecture en **3 couches** :

```
┌─────────────────────────────┐
│   Couche Présentation       │
│   (Controllers)             │
├─────────────────────────────┤
│   Couche Métier             │
│   (Services + Mappers)      │
├─────────────────────────────┤
│   Couche Données            │
│   (Repositories + Models)   │
├─────────────────────────────┤
│   Base de Données (H2)      │
└─────────────────────────────┘
```

### Structure des dossiers

- **`model/`** : Entités JPA (Livre, User)
- **`dto/`** : Data Transfer Objects (requêtes/réponses)
- **`mapper/`** : Convertisseurs Entity ↔ DTO
- **`repository/`** : Accès aux données (JPA Repositories)
- **`service/`** : Logique métier
  - Interfaces (AuthService, LivreService, UserService)
  - Implémentations (Impl)
- **`web/`** : Contrôleurs web (formulaires Thymeleaf)
- **`rest/`** : Contrôleurs REST (API JSON)
- **`exception/`** : Exceptions métier personnalisées
- **`resources/`** : Configuration et templates HTML

## Choix techniques

### Stack technologique
- **Framework** : Spring Boot 3.5.8
- **Langage** : Java 17
- **Build** : Maven
- **ORM** : JPA/Hibernate
- **Base de données** : H2 (persistante, fichiers dans `./data/`)
- **Web** : Spring MVC + Thymeleaf
- **API REST** : Spring REST + OpenAPI/Swagger
- **Validation** : Spring Validation
- **Utils** : Lombok
- **Documentation API** : SpringDoc (Swagger UI)

### Justifications des choix

| Choix | Raison |
|-------|--------|
| **Spring Boot** | Framework complet pour applications Web/REST, configuration automatique, écosystème riche |
| **H2 Persistant** | Base de données légère, intégrée, fichiers de données persistants, idéale pour développement/test |
| **JPA/Hibernate** | ORM standard Java, requêtes typées, gestion automatique des relations |
| **Thymeleaf** | Templating côté serveur, intégration native Spring, templating naturel |
| **DTOs** | Séparation entre modèles internes et exposition API, versioning plus facile |
| **Mappers** | Conversion propre Entity ↔ DTO, logique centralisée |
| **Repositories** | Abstraction de la persistance, requêtes réutilisables |
| **Services** | Logique métier centralisée, testabilité, réutilisabilité |
| **Exceptions personnalisées** | Gestion d'erreurs spécifique au domaine |

## Fonctionnalités

### Authentification
- Login/Logout avec validation
- Exceptions personnalisées (InvalidCredentialsException)

### Gestion des Livres
- **CRUD complet** : Créer, Lire, Mettre à jour, Supprimer
- **Champs** : Titre, Auteur, Prix, Date de création, Statut d'archivage
- **Relation** : Livre créé par un utilisateur
- **API REST** : Endpoints JSON avec validation

### Gestion des Utilisateurs
- Création de comptes
- Persistance des données utilisateur

## Points clés de la configuration

### application.yaml
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/testdb    # Base persistante
  h2:
    console:
      enabled: true                     # Console H2 accessible
```

### Console H2
Accès via : `http://localhost:8080/h2-console`

### Documentation API
Swagger UI disponible via : `http://localhost:8080/swagger-ui.html`

Pour accéder directement aux interfaces:  `http://localhost:8080 et utiliser Nom d'utilisateur: "admin" et Mot de passe: "admin123"

### Membres du groupes

- OUEDRAOGO Souleymane;
- OUEDRAOGO Téga-Wendé Jean Baptiste;
- SOUBEIGA Bénéwendé Sosthène Franklin

