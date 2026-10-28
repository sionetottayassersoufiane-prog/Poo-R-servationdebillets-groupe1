# Réservation de tickets de voyage : Jalon 1 : modèle et invariants

Projet de programmation orientée objet, IIAA511, 2iE. Enseignant : Dr Babacar LEYE. Groupe 01. Java 17.

## L'équipe

- SIONE Totta Yasser Soufiane, matricule 20260050
- NADEMBEGA Ingrid Océane, matricule 20240189
- NIKIEMA Pengdwendé Hanifah, matricule 20260303

Dépôt GitHub du groupe : https://github.com/sionetottayassersoufiane-prog/poo-reservationdebillets-groupe01

## Le sujet en bref

Une agence de transport propose des trajets en bus, en train et en avion au départ de Ouagadougou. Un voyageur réserve une place sur un trajet, ce qui crée un ticket. Le ticket peut être annulé tant que le délai du trajet le permet (24 h avant le départ pour un bus, 48 h pour un train, 72 h pour un vol), et il passe à l'état Payé quand le départ arrive sans annulation.

Les données restent en mémoire pendant l'exécution. Il n'y a ni interface graphique, ni base de données, ni réseau.

## Ce que contient ce rendu

Ce premier jalon pose les trois classes du noyau : `Voyageur`, `Trajet` et `Ticket`, avec l'énumération `StatutTicket`. Chaque classe garde son état privé et refuse dès la construction toute donnée qui casserait ses invariants. Il n'y a pas encore d'héritage, de collection ni d'exception personnalisée, ces notions arrivent aux jalons suivants.

Tag attendu sur GitHub : `jalon-1`.

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

- `Voyageur` : numéro, nom, adresse e-mail. Tout est immuable après la création.
- `Trajet` : numéro, villes, date, heure, capacité, places libres, heures restantes avant le départ et délai d'annulation. Il sait réserver et libérer une place.
- `Ticket` : relie un voyageur à un trajet, porte un prix et un statut. Il sait s'annuler et se valider au départ.
- `StatutTicket` : RESERVE, PAYE, ANNULE.
- `Main` : onze essais reproductibles qui affichent OK ou ECHEC.

## Les invariants

- Un numéro est toujours strictement positif.
- Un nom n'est jamais vide et une adresse e-mail contient un arobase.
- Les villes de départ et d'arrivée sont renseignées et différentes.
- La capacité est strictement positive et le nombre de places libres reste entre 0 et la capacité.
- Un ticket est toujours lié à un voyageur et à un trajet, et son prix n'est jamais négatif.
- Un ticket ne passe de RESERVE à ANNULE ou PAYE qu'une seule fois, et un refus laisse le ticket et le trajet inchangés.

## Les essais

Les essais sont dans `Main` et affichent OK ou ECHEC avec un bilan à la fin. JUnit n'est introduit qu'au jalon 4.

## Deux choix à défendre

1. **Le délai d'annulation est donné au constructeur du trajet.** Pour l'instant `Trajet` reçoit simplement 24, 48 ou 72. Nous savons que ce n'est pas idéal, car le type de transport n'est pas représenté. Nous l'avons gardé ainsi parce que l'héritage n'est étudié qu'au jalon 2, et nous le remplacerons alors par une méthode redéfinie dans chaque sous-classe.
2. **Le statut du ticket est une énumération et seul `Ticket` peut le changer.** Un `String` libre aurait permis d'écrire n'importe quoi. Avec `StatutTicket` et un attribut privé, les seules transitions possibles sont celles de `annuler()` et de `validerAuto()`.

## Une difficulté rencontrée

Dans le constructeur de `Ticket`, nous avions d'abord rempli les champs puis appelé `trajet.reserverPlace()`. Si le trajet était complet, l'exception partait mais l'objet était déjà à moitié construit. En relisant la section 2.5 du CM 1 (valider avant de modifier), nous avons déplacé tous les contrôles et la réservation de la place avant la première affectation d'un champ.

## Répartition du travail

- SIONE Totta Yasser Soufiane : `Trajet` et `Ticket`.
- NADEMBEGA Ingrid Océane : `Main` et les essais reproductibles.
- NIKIEMA Pengdwendé Hanifah : `Voyageur`, README et diagramme de classes.

Chaque membre pousse ses propres commits avec son compte GitHub.

## Sources consultées

CM 1 (objets, encapsulation, constructeurs) et TD 1 du Dr LEYE. Le diagramme a été dessiné avec Graphviz (fichier source `docs/diagramme-classes.dot`).

## Suite

Au jalon 2, `Trajet` devient abstraite avec `TrajetBus`, `TrajetTrain` et `TrajetVol`, et se compose d'un itinéraire et d'un horaire.
