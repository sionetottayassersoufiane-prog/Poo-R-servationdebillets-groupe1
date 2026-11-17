import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuConsole {
    private final ServiceReservation service;
    private final Scanner entree;
    private final PrintStream sortie;

    public MenuConsole(ServiceReservation service, InputStream flux, PrintStream sortie) {
        if (service == null || flux == null || sortie == null) {
            throw new IllegalArgumentException("le menu a besoin d'un service, d'une entree et d'une sortie");
        }
        this.service = service;
        this.entree = new Scanner(flux);
        this.sortie = sortie;
    }

    public void lancer() {
        boolean continuer = true;
        while (continuer && entree.hasNextLine()) {
            afficherMenu();
            String choix = entree.nextLine().trim();
            try {
                continuer = traiter(choix);
            } catch (ReservationException e) {
                sortie.println("Operation refusee : " + e.getMessage());
            } catch (IllegalArgumentException e) {
                sortie.println("Saisie refusee : " + e.getMessage());
            }
        }
        sortie.println("Au revoir.");
    }

    private void afficherMenu() {
        sortie.println();
        sortie.println("===== Agence de voyage =====");
        sortie.println("1. Voir les trajets disponibles");
        sortie.println("2. Rechercher un trajet par ville");
        sortie.println("3. Enregistrer un voyageur");
        sortie.println("4. Reserver un ticket");
        sortie.println("5. Annuler un ticket");
        sortie.println("6. Voir les tickets d'un voyageur");
        sortie.println("7. Valider le depart d'un trajet");
        sortie.println("8. Bilan de l'agence");
        sortie.println("0. Quitter");
        sortie.print("Votre choix : ");
    }

    private boolean traiter(String choix) throws ReservationException {
        switch (choix) {
            case "1":
                voirTrajets();
                return true;
            case "2":
                rechercherTrajets();
                return true;
            case "3":
                enregistrerVoyageur();
                return true;
            case "4":
                reserver();
                return true;
            case "5":
                annuler();
                return true;
            case "6":
                voirTickets();
                return true;
            case "7":
                validerDepart();
                return true;
            case "8":
                bilan();
                return true;
            case "0":
                return false;
            default:
                sortie.println("Choix invalide : tapez un chiffre entre 0 et 8.");
                return true;
        }
    }

    private String lireTexte(String invite) {
        sortie.print(invite + " : ");
        if (!entree.hasNextLine()) {
            throw new IllegalArgumentException("la saisie est terminee");
        }
        return entree.nextLine().trim();
    }

    private int lireEntier(String invite) {
        String texte = lireTexte(invite);
        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("un nombre entier est attendu pour : " + invite);
        }
    }

    private void voirTrajets() {
        List<Trajet> disponibles = service.trajetsDisponibles();
        if (disponibles.isEmpty()) {
            sortie.println("Aucun trajet disponible.");
            return;
        }
        for (Trajet trajet : disponibles) {
            sortie.println(trajet);
        }
    }

    private void rechercherTrajets() {
        String motCle = lireTexte("Ville recherchee");
        List<Trajet> trouves = service.rechercherTrajets(motCle);
        if (trouves.isEmpty()) {
            sortie.println("Aucun trajet ne correspond a \"" + motCle + "\".");
            return;
        }
        for (Trajet trajet : trouves) {
            sortie.println(trajet);
        }
    }

    private void enregistrerVoyageur() throws ReservationException {
        int numero = lireEntier("Numero du voyageur");
        String nom = lireTexte("Nom complet");
        String email = lireTexte("Adresse e-mail");
        Voyageur voyageur = new Voyageur(numero, nom, email);
        service.enregistrerVoyageur(voyageur);
        sortie.println("Voyageur enregistre : " + voyageur);
    }

    private void reserver() throws ReservationException {
        int numeroVoyageur = lireEntier("Numero du voyageur");
        int numeroTrajet = lireEntier("Numero du trajet");
        int prix = lireEntier("Prix en FCFA");
        Ticket ticket = service.reserver(numeroVoyageur, numeroTrajet, prix);
        sortie.println("Reservation faite : " + ticket);
    }

    private void annuler() throws ReservationException {
        int numeroTicket = lireEntier("Numero du ticket");
        service.annuler(numeroTicket);
        sortie.println("Ticket annule : " + service.chercherTicket(numeroTicket));
    }

    private void voirTickets() throws ReservationException {
        int numeroVoyageur = lireEntier("Numero du voyageur");
        List<Ticket> tickets = service.ticketsDuVoyageur(numeroVoyageur);
        if (tickets.isEmpty()) {
            sortie.println("Ce voyageur n'a aucun ticket.");
            return;
        }
        for (Ticket ticket : tickets) {
            sortie.println(ticket);
        }
    }

    private void validerDepart() throws ReservationException {
        int numeroTrajet = lireEntier("Numero du trajet");
        int valides = service.validerDepart(numeroTrajet);
        sortie.println(valides + " ticket(s) valide(s) au depart.");
    }

    private void bilan() {
        Map<StatutTicket, Integer> compteurs = service.compterParStatut();
        for (Map.Entry<StatutTicket, Integer> entreeCompteur : compteurs.entrySet()) {
            sortie.println(entreeCompteur.getKey() + " : " + entreeCompteur.getValue());
        }
        sortie.println("Chiffre d'affaires (tickets payes) : " + service.chiffreAffaires() + " FCFA");
    }
}
