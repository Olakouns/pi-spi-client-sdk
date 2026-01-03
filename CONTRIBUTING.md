# Contributing to PI-SPI Java SDK

Merci de l'intérêt que vous portez à ce projet ! Voici comment vous pouvez contribuer.

## 🛠 Environnement de développement
- **Java 8** minimum.
- Un compte sur le portail PI-SPI pour les tests d'intégration (Sandbox).

## 🚀 Processus de Contribution
1. **Fork** le projet.
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/AmazingFeature`).
3. **Écrivez des tests** ! Aucun code ne sera accepté sans une couverture de tests adéquate dans le module `test-suite`.
4. Assurez-vous que le build passe : `mvn clean install`.
5. **Committez** vos changements (`git commit -m 'Add some AmazingFeature'`).
6. **Push** sur la branche (`git push origin feature/AmazingFeature`).
7. Ouvrez une **Pull Request**.

## 📝 Règles de codage
- Utilisez le style Java standard (CamelCase).
- Ajoutez des **JavaDocs** sur toutes les nouvelles méthodes publiques.
- Ne modifiez pas la version du projet dans le `pom.xml` (cela sera géré lors de la release).

## 🐛 Signaler un bug
Utilisez l'onglet [Issues](https://github.com/Olakouns/pi-spi-client-sdk/issues) pour signaler un bug ou suggérer une amélioration.