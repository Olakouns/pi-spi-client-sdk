# PI-SPI Java SDK 🚀

# PI-SPI Java SDK 🚀

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Olakouns_pi-spi-client-sdk&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Olakouns_pi-spi-client-sdk)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Olakouns_pi-spi-client-sdk&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Olakouns_pi-spi-client-sdk)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Olakouns_pi-spi-client-sdk&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Olakouns_pi-spi-client-sdk)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Olakouns_pi-spi-client-sdk&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Olakouns_pi-spi-client-sdk)


Le SDK Java officiel pour l'intégration avec l'**API Business de PI-SPI (BCEAO)**. Ce SDK simplifie la gestion de l'authentification OAuth2, le filtrage complexe et les appels aux services.

## 📦 Installation

Ajoutez la dépendance suivante à votre fichier `pom.xml` :

```xml
<dependency>
    <groupId>io.github.olakouns</groupId>
    <artifactId>pi-spi-admin-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🛠️ Utilisation Rapide

### 1. Initialisation du Client

Le client utilise un Builder pour configurer l'accès à l'API de manière fluide.

```java
// Exemple
PiSpiClient client = PiSpiClientBuilder.builder()
        .serverUrl("https://api.pi-spi.org")
        .clientId("votre-client-id")
        .clientSecret("votre-client-secret")
        .apiKey("votre-client-key")
        .build();
```
Pour des configurations avancées, veuillez consulter ce [lien](https://github.com/Olakouns/pi-spi-client-sdk/wiki/Configuration)

### 2. Requêtes avec Filtrage et Pagination

Grâce au `ListQueryBuilder` et au `FilterBuilder`, effectuer des recherches complexes devient trivial.

```java
//Exemple
PagedResponse<CompteRepresentation> comptes = client.api().comptes().query()
    .filter(f -> f
            .eq("statut", "ACTIF")
            .gt("solde", 5000)
    )
    .sort("dateCreation", false) // Tri descendant (sort=-dateCreation)
    .page("1")
    .size(10)
    .execute();
```

## ⚠️ Gestion des Exceptions

Le SDK distingue deux types d'erreurs pour une meilleure robustesse :

* **`PiSpiApiException`** : Erreurs retournées par le serveur (ex: 401 Unauthorized, 400 Bad Request avec détails des paramètres invalides).
* **`PiSpiException`** : Erreurs locales de validation ou de configuration interne au SDK.

```java
try {
    client.api().comptes().findByNumero("ID-INVALIDE");
} catch (PiSpiApiException e) {
    System.err.println("Erreur API (" + e.getStatus() + ") : " + e.getErrorResponse().getDetail());
} catch (PiSpiException e) {
    System.err.println("Erreur SDK : " + e.getMessage());
}

```

## 📑 Documentation

Pour plus de détails, consultez notre documentation complète : [ici](https://github.com/Olakouns/pi-spi-client-sdk/wiki)
## 🤝 Contribution

Les contributions sont les bienvenues ! Veuillez consulter le fichier [CONTRIBUTING](https://github.com/Olakouns/pi-spi-client-sdk?tab=contributing-ov-file) pour connaître la marche à suivre.

## ⚖️ Licence

Distribué sous la licence **Apache 2.0**. Voir le fichier `LICENSE` à la racine du projet pour plus de détails.

---

**Développé avec ❤️ par [Razacki KOUNASSO**](https://github.com/Olakouns)

---
