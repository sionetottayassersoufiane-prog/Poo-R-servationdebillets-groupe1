# Réservation de tickets de voyage : Jalon 3 : interfaces et collections

Projet de programmation orientée objet, IIAA511, 2iE. Enseignant : Dr Babacar LEYE. Groupe 01. Java 17.

## L'équipe

- SIONE Totta Yasser Soufiane, matricule 20260050
- NADEMBEGA Ingrid Océane, matricule 20240189
- NIKIEMA Pengdwendé Hanifah, matricule 20260303

Dépôt GitHub du groupe : https://github.com/sionetottayassersoufiane-prog/Poo-R-servationdebillets-groupe1.git

## Le sujet en bref

Une agence de transport propose des trajets en bus, en train et en avion au départ de Ouagadougou. Un voyageur réserve une place sur un trajet, ce qui crée un ticket. Le ticket peut être annulé tant que le délai du trajet le permet (24 h avant le départ pour un bus, 48 h pour un train, 72 h pour un vol), et il passe à l'état Payé quand le départ arrive sans annulation.

Les données restent en mémoire pendant l'exécution. Il n'y a ni interface graphique, ni base de données, ni réseau.

## Ce que contient ce rendu

Nous ajoutons l'interface `CatalogueTrajets`, implémentée par `CatalogueTrajetsMemoire` avec une `HashMap`. Les classes métier redéfinissent `equals` et `hashCode` avant tout stockage dans une collection de hachage, et `Trajet` devient comparable pour trier les parcours par date.

Tag attendu sur GitHub : `jalon-3`.

## Lancer le programme

```
javac -d out src/*.java
java -cp out Main
```

Avec Maven (Java 17 obligatoire) :

```
mvn compile
java -cp target/classes Main
```

## Les classes

- `CatalogueTrajets` (interface) : `ajouter`, `trouver`, `retirer`, `estVide`, `taille`, `parcourir`, `trajetsDisponibles`, `rechercherParVille`, `regrouperParType`.
- `CatalogueTrajetsMemoire` : stocke les trajets dans une `Map<Integer, Trajet>`.
- `Voyageur`, `Trajet`, `Ticket` : identité fondée sur le numéro (`equals` et `hashCode`). `Trajet` implémente `Comparable<Trajet>` (date, heure, numéro).
- Le reste du modèle est celui du jalon 2.
- `Main` : trente et un essais cumulés.

## Les invariants

- Un numéro de trajet n'existe qu'une fois dans le catalogue.
- Une opération refusée ne modifie pas le catalogue.
- Aucune méthode publique ne donne accès à la `Map` interne : `parcourir()`, `trajetsDisponibles()`, `rechercherParVille()` et `regrouperParType()` renvoient des copies.
- Deux objets de même numéro sont égaux et ont le même `hashCode`.
- Ajouter `null` est refusé, chercher un numéro absent renvoie `null`.

## Les essais

Les essais sont dans `Main` et affichent OK ou ECHEC avec un bilan à la fin. JUnit n'est introduit qu'au jalon 4.

## Deux choix à défendre

1. **Une `HashMap<Integer, Trajet>` pour le catalogue.** L'opération la plus fréquente est de retrouver un trajet par son numéro, ce qui est quasi immédiat avec un dictionnaire. Une `ArrayList` aurait demandé de parcourir tous les trajets. Le tri n'est fait qu'à la demande, sur une copie, avec `Collections.sort`.
2. **Un doublon est refusé sans remplacer l'ancien.** `ajouter` renvoie `false` et le catalogue ne bouge pas. Remplacer silencieusement un trajet déjà vendu pourrait faire perdre les places déjà réservées. Les exceptions seront introduites au jalon 4, c'est pourquoi le contrat passe pour l'instant par un booléen.

## Comparaison des structures étudiées

| Structure | Doublons | Ordre | Accès principal | Usage dans le projet |
|---|---|---|---|---|
| `ArrayList` | oui | ordre d'insertion | par position, recherche par valeur linéaire | listes triées renvoyées par `parcourir()` et `rechercherParVille()` |
| `HashSet` | non | non garanti | test d'appartenance rapide | essai sur l'unicité des voyageurs dans `Main` |
| `HashMap` | clés uniques | non garanti | par clé, quasi immédiat | **catalogue : numéro vers `Trajet` (choix retenu)** |
| `TreeMap` | clés uniques | trié par clé | par clé, logarithmique | `regrouperParType()`, types classés par ordre alphabétique |

Choix retenu : `HashMap<Integer, Trajet>` pour le catalogue. Choix écarté : une `ArrayList<Trajet>`, car retrouver un numéro oblige à parcourir toute la liste et que détecter un doublon demande une boucle supplémentaire.

Commit conseillé pour ce jalon : « Ajoute catalogue et collections », puis le tag `jalon-3`.

## Une difficulté rencontrée

Nous nous sommes demandé ce que veut dire « même trajet » quand un bus et un vol portent le même numéro. Nous avons décidé que l'identité est le numéro seul, comme pour les clés d'une `HashMap` vue au CM 3, et nous avons écrit un essai qui le vérifie. Nous avons aussi compris que renvoyer directement la liste interne laisserait l'appelant la vider, d'où les copies renvoyées.

## Répartition du travail

- SIONE Totta Yasser Soufiane : `CatalogueTrajetsMemoire` (recherche, regroupement, tri).
- NADEMBEGA Ingrid Océane : les essais du catalogue dans `Main`.
- NIKIEMA Pengdwendé Hanifah : `equals`, `hashCode`, `compareTo`, l'interface `CatalogueTrajets`, README et diagramme.

Chaque membre pousse ses propres commits avec son compte GitHub.

## Sources consultées

CM 3 (collections) et TD 3 du Dr LEYE. Diagramme dessiné avec Graphviz (source `docs/diagramme-classes.dot`).

## Suite

Au jalon 4, nous ajoutons `ServiceReservation`, les exceptions métier, l'atomicité et les tests JUnit.
