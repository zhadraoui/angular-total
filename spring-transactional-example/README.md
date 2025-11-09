# Spring `@Transactional` Playground

Ce module Maven démontre la plupart des cas d'usage autour de `@Transactional` dans Spring Boot :

- **Propagation `REQUIRED`** pour une opération métier classique (`transferRequired`).
- **`REQUIRES_NEW`** pour écrire un audit indépendant de la transaction principale (`AuditService`).
- **`REQUIRES_NEW`** pour accorder un bonus dans une transaction indépendante qui peut être annulée (`BonusService`).
- **`SUPPORTS` + `readOnly=true`** pour des lectures optimisées (`ReportingService`).
- **`NOT_SUPPORTED`** pour exécuter un traitement hors transaction (`NonTransactionalService`).
- **Timeout & règles de rollback personnalisées** via `@Transactional(timeout = 1, rollbackFor = TimeoutException.class)`.
- **Exceptions checked** déclenchant un rollback explicite (`TimeoutException`).

## Lancer l'exemple localement

```bash
mvn spring-boot:run -pl spring-transactional-example -am
```

Les logs montrent chaque scénario déclenché au démarrage de l'application.

## Créer un nouveau dépôt GitHub

1. Initialiser un dépôt Git local à la racine du module :
   ```bash
   cd spring-transactional-example
   git init
   git add .
   git commit -m "Initial commit"
   ```
2. Créer un dépôt vide sur GitHub (via l'interface web ou l'outil `gh repo create`).
3. Lier le dépôt local au dépôt GitHub fraîchement créé :
   ```bash
   git remote add origin git@github.com:VOTRE_COMPTE/transactional-demo.git
   git push -u origin main
   ```

> 💡 Remplacez `VOTRE_COMPTE` et `transactional-demo` par vos propres valeurs.

## Structure

```
spring-transactional-example/
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java/com/example/transaction
    │   │   ├── TransactionalDemoApplication.java
    │   │   ├── domain/Account.java
    │   │   ├── repository/AccountRepository.java
    │   │   └── service
    │   │       ├── AuditService.java
    │   │       ├── BonusService.java
    │   │       ├── NonTransactionalService.java
    │   │       ├── ReportingService.java
    │   │       ├── ScenarioRunner.java
    │   │       └── TransferService.java
    │   └── resources/application.yml
    └── test (à compléter selon vos besoins)
```

Vous pouvez enrichir l'exemple avec des tests d'intégration (`@DataJpaTest`, `@SpringBootTest`) pour valider le comportement transactionnel.
