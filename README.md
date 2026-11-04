# Réservation de tickets de voyage : Jalon 2 : composition et polymorphisme

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

`Trajet` devient une classe abstraite. Le bus, le train et le vol redéfinissent chacun `delaiAnnulationHeures()` et `getType()`, et `Ticket` n'a plus jamais besoin de connaître le type réel du trajet. Un trajet est aussi composé d'un `Itineraire` et d'un `Horaire`, deux petites classes qui protègent leurs propres invariants.

Tag attendu sur GitHub : `jalon-2`.

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

- `Voyageur`, `Ticket`, `StatutTicket` : inchangés par rapport au jalon 1.
- `Trajet` (abstraite) : numéro, capacité, places libres, un `Itineraire` et un `Horaire`. Méthodes abstraites `delaiAnnulationHeures()` et `getType()`.
- `TrajetBus` (24 h, compagnie), `TrajetTrain` (48 h, numéro de train), `TrajetVol` (72 h, numéro de vol).
- `Itineraire` : ville de départ et ville d'arrivée, toujours différentes.
- `Horaire` : date, heure de départ, heures restantes avant le départ.
- `Main` : seize essais cumulés, dont trois via une référence de type `Trajet`.

## Les invariants

- Tous les invariants du jalon 1 restent vrais, y compris les essais déjà écrits.
- Un `Itineraire` refuse deux villes identiques ou vides, un `Horaire` refuse une date ou une heure vide.
- Chaque sous-classe exige son information propre (compagnie, numéro de train, numéro de vol).
- Le délai d'annulation dépend uniquement du type réel du trajet.

## Les essais

Les essais sont dans `Main` et affichent OK ou ECHEC avec un bilan à la fin. JUnit n'est introduit qu'au jalon 4.

## Deux choix à défendre

1. **Héritage pour le type de transport, composition pour l'itinéraire et l'horaire.** « Un bus est un trajet » est vrai, et seuls le délai d'annulation et l'information propre changent. En revanche « un trajet est un itinéraire » est faux : un trajet a un itinéraire et un horaire, donc nous les avons composés.
2. **Pas de test de type dans `Ticket`.** Au lieu d'écrire `if (trajet instanceof TrajetVol)`, le ticket demande `trajet.annulationEncorePossible()`. Ajouter un quatrième type de transport demandera une nouvelle sous-classe et aucune modification de `Ticket`.

## Une difficulté rencontrée

Le plus délicat a été de ne pas casser les essais du jalon 1 en changeant le constructeur de `Trajet`. Nous avons gardé la même liste de paramètres (sauf le délai, désormais porté par la sous-classe) et nous avons créé `Itineraire` et `Horaire` à l'intérieur du constructeur, pour que leurs contrôles s'appliquent sans changer le code qui appelle `Trajet`.

## Répartition du travail

- SIONE Totta Yasser Soufiane : la hiérarchie `Trajet`, `TrajetBus`, `TrajetTrain`, `TrajetVol`.
- NADEMBEGA Ingrid Océane : `Itineraire`, `Horaire` et les essais de composition dans `Main`.
- NIKIEMA Pengdwendé Hanifah : essais de polymorphisme, README et diagramme de classes.

Chaque membre pousse ses propres commits avec son compte GitHub.

## Sources consultées

CM 2 (héritage, polymorphisme, interfaces) et TD 2 du Dr LEYE. Diagramme dessiné avec Graphviz (source `docs/diagramme-classes.dot`).

## Suite

Au jalon 3, nous ajoutons `equals` et `hashCode`, l'interface `CatalogueTrajets` et son implémentation avec une `Map`.
