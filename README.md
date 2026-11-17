# Réservation de tickets de voyage : Jalon 4 : exceptions, tests, recette et application finale

Projet de programmation orientée objet, IIAA511, 2iE. Enseignant : Dr Babacar LEYE. Groupe 01. Java 17.

## L'équipe

- SIONE Totta Yasser Soufiane, matricule 20260050
- NADEMBEGA Ingrid Océane, matricule 20240189
- NIKIEMA Pengdwendé Hanifah, matricule 20260303

Dépôt GitHub du groupe : https://github.com/sionetottayassersoufiane-prog/Poo-R-servationdebillets-groupe1

## Le sujet en bref

Une agence de transport propose des trajets en bus, en train et en avion au départ de Ouagadougou. Un voyageur réserve une place sur un trajet, ce qui crée un ticket. Le ticket peut être annulé tant que le délai du trajet le permet (24 h avant le départ pour un bus, 48 h pour un train, 72 h pour un vol), et il passe à l'état Payé quand le départ arrive sans annulation.

Les données restent en mémoire pendant l'exécution. Il n'y a ni interface graphique, ni base de données, ni réseau.

## Ce que contient ce rendu

`ServiceReservation` orchestre les opérations (réserver, annuler, valider le départ) et garantit qu'un refus ne laisse rien à moitié modifié. Cinq exceptions métier remplacent les booléens et les `null` du jalon 3 pour les échecs qui comptent, et `IllegalArgumentException` reste réservée aux arguments invalides. Un menu en ligne de commande, `MenuConsole`, permet d'utiliser l'agence sans toucher au code : consulter les trajets, chercher une ville, enregistrer un voyageur, réserver, annuler, valider un départ et voir le bilan. La suite JUnit 5 compte 64 tests.

Le jalon 4 est aussi la version finale du projet, rendue le 20 novembre 2026. Il n'y a donc qu'un seul rendu et un seul tag.

Tag attendu sur GitHub : `jalon4`.

## Lancer le programme

Démonstration automatique :

```
mvn compile
java -cp target/classes Main
```

Menu interactif :

```
java -cp target/classes Main menu
```

Sans Maven :

```
javac -d out src/*.java
java -cp out Main menu
```

Tests :

```
mvn test
```

## Les classes

- `ServiceReservation` : enregistre les voyageurs, ajoute les trajets, réserve, annule, valide les départs, calcule le bilan.
- `ReservationException` et ses cinq filles : `ElementIntrouvableException`, `DoublonException`, `TrajetCompletException`, `ReservationInterditeException`, `AnnulationImpossibleException`.
- Le modèle (`Voyageur`, `Trajet` et ses trois sous-classes, `Itineraire`, `Horaire`, `Ticket`) et le catalogue sont ceux du jalon 3. `Ticket` gagne `peutEtreAnnule()`.
- `MenuConsole` : lit les choix au clavier et affiche les résultats. Il ne contient aucune règle métier, il appelle seulement `ServiceReservation` et affiche les refus.
- `Main` : sans argument, joue le scénario de démonstration avec des refus ; avec l'argument `menu`, lance le menu sur des données de départ.
- `test` : six classes de test, dont `MenuConsoleTest` qui simule des saisies au clavier.

## Les invariants

- Une réservation refusée ne crée aucun ticket et n'occupe aucune place.
- Une annulation refusée laisse le ticket et la place comme avant.
- Un voyageur ne peut pas avoir deux tickets actifs (réservé ou payé) sur le même trajet, mais peut réserver de nouveau après une annulation.
- Un numéro de voyageur, une adresse e-mail ou un numéro de trajet déjà pris est refusé sans rien modifier.
- Les numéros de ticket sont successifs et ne sautent pas lors d'un refus.
- Une saisie du menu qui n'est pas un nombre est refusée sans rien modifier et sans arrêter le programme.
- Un refus du service s'affiche sous la forme « Operation refusee : ... » et le menu continue.
- Tous les invariants des jalons précédents restent vrais.

## Les tests

La suite JUnit 5 compte 64 tests : cas normaux, cas limites et refus. Pour chaque refus important, le test vérifie le type d'exception et l'état après l'appel (nombre de places, nombre de tickets, statut). La trace de recette est dans `docs/recette.md` et la note de conception dans `docs/note-conception.md`.

## Trois choix à défendre

1. **Le service orchestre, les entités gardent leurs invariants.** `ServiceReservation` vérifie dans un ordre précis (prix, voyageur, trajet, double réservation, trajet complet) avant de créer le `Ticket`, qui est le seul à occuper la place. Même si quelqu'un contournait le service, `Ticket` et `Trajet` refuseraient eux-mêmes un état interdit.
2. **Cinq exceptions métier vérifiées, et rien de plus.** Chaque exception correspond à une réaction différente de l'appelant : objet introuvable, doublon, trajet complet, réservation interdite, annulation impossible. Un argument invalide (prix négatif, valeur nulle) reste une `IllegalArgumentException`, comme demandé au TD 4.
3. **Le menu est séparé de la logique métier.** `MenuConsole` reçoit un `InputStream` et un `PrintStream`, ce qui permet de le tester avec des saisies simulées. Toutes les règles restent dans `ServiceReservation` et dans les entités. Le périmètre reste celui du sujet : données en mémoire, sans sauvegarde ni file d'attente.

## Une difficulté rencontrée

Garantir qu'un refus ne change rien nous a demandé de revoir l'ordre des instructions de `reserver`. Notre première version incrémentait le numéro de ticket avant de créer le ticket, donc un refus faisait sauter un numéro. Nous avons déplacé l'incrémentation après la création et ajouté le test `lesNumerosDeTicketSontSuccessifs` ainsi que des tests qui comparent l'état avant et après chaque refus.

Tester un menu qui attend des saisies au clavier n'était pas évident au début. En faisant recevoir à `MenuConsole` un flux d'entrée et un flux de sortie au lieu de `System.in` et `System.out`, nous avons pu écrire des tests avec un `ByteArrayInputStream` et lire ce qui s'affiche dans un `ByteArrayOutputStream`.

## Répartition du travail

- SIONE Totta Yasser Soufiane : les exceptions, `ServiceReservation`, `MenuConsole` et l'intégration dans `Main`.
- NADEMBEGA Ingrid Océane : les tests de `ServiceReservation` et du catalogue, `MenuConsoleTest`, la démonstration dans `Main`.
- NIKIEMA Pengdwendé Hanifah : les tests de `Voyageur`, `Trajet` et `Ticket`, la note de conception, la trace de recette, README et diagramme final.

Chaque membre pousse ses propres commits avec son compte GitHub.

## Sources consultées

CM 1 à CM 5 et TD 1 à TD 4 du Dr LEYE. Diagramme dessiné avec Graphviz (source `docs/diagramme-classes.dot`).

## Suite

Le dossier `livrables-moodle` (à côté du dossier du projet dans le zip) contient le rapport PDF, la présentation PDF et la matrice PME à déposer sur Moodle. Il reste la capsule vidéo MP4 (sur clé USB) et le retour individuel de chaque membre.
