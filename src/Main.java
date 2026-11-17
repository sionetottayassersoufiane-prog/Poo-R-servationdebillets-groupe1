import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ServiceReservation service = new ServiceReservation(new CatalogueTrajetsMemoire());
        boolean modeMenu = args.length > 0 && args[0].equals("menu");
        try {
            preparer(service);
            if (modeMenu) {
                new MenuConsole(service, System.in, System.out).lancer();
                return;
            }
            scenarioReservation(service);
            scenarioRefus(service);
            scenarioDepart(service);
            bilan(service);
        } catch (ReservationException e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
        System.out.println();
        System.out.println("Fin de la demonstration.");
    }

    private static void titre(String texte) {
        System.out.println();
        System.out.println("=== " + texte + " ===");
    }

    private static void preparer(ServiceReservation service) throws ReservationException {
        titre("Preparation : voyageurs et trajets");
        service.enregistrerVoyageur(new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com"));
        service.enregistrerVoyageur(new Voyageur(2, "NADEMBEGA Ingrid Oceane", "ingrid@gmail.com"));
        service.enregistrerVoyageur(new Voyageur(3, "NIKIEMA Pengdwende Hanifah", "hanifah@gmail.com"));

        service.ajouterTrajet(new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-12-10", "08:00", 2, 120, "STAF"));
        service.ajouterTrajet(new TrajetTrain(102, "Ouagadougou", "Banfora", "2026-12-05", "05:30", 100, 100, "SIT 12"));
        service.ajouterTrajet(new TrajetVol(103, "Ouagadougou", "Abidjan", "2026-12-08", "07:00", 1, 60, "AH 512"));
        service.ajouterTrajet(new TrajetBus(104, "Ouagadougou", "Koudougou", "2026-11-30", "09:00", 30, 10, "TSR"));

        for (Trajet trajet : service.tousLesTrajets()) {
            System.out.println(trajet);
        }
    }

    private static void scenarioReservation(ServiceReservation service) throws ReservationException {
        titre("Reservations qui reussissent");
        Ticket premier = service.reserver(1, 101, 8000);
        System.out.println(premier);
        Ticket second = service.reserver(2, 101, 8000);
        System.out.println(second);
        System.out.println(service.chercherTrajet(101));

        System.out.println();
        System.out.println("Recherche du mot-cle \"banfora\" :");
        List<Trajet> trouves = service.rechercherTrajets("banfora");
        for (Trajet trajet : trouves) {
            System.out.println("   " + trajet);
        }
    }

    private static void scenarioRefus(ServiceReservation service) throws ReservationException {
        titre("Refus : l'etat ne change pas");
        int ticketsAvant = service.nombreTickets();
        int placesAvant = service.chercherTrajet(101).getPlacesDisponibles();

        try {
            service.reserver(3, 101, 8000);
        } catch (TrajetCompletException e) {
            System.out.println("Trajet complet : " + e.getMessage());
        }
        try {
            service.reserver(1, 101, 8000);
        } catch (ReservationInterditeException e) {
            System.out.println("Double reservation : " + e.getMessage());
        }
        try {
            service.reserver(99, 101, 8000);
        } catch (ElementIntrouvableException e) {
            System.out.println("Voyageur absent : " + e.getMessage());
        }
        try {
            service.reserver(3, 102, -5);
        } catch (IllegalArgumentException e) {
            System.out.println("Prix invalide : " + e.getMessage());
        }
        try {
            service.reserver(3, 103, 250000);
            service.annuler(3);
        } catch (AnnulationImpossibleException e) {
            System.out.println("Annulation hors delai : " + e.getMessage());
        }
        System.out.println("Tickets avant : " + ticketsAvant + ", apres les refus : " + service.nombreTickets()
                + " (le seul nouveau ticket est celui du vol 103)");
        System.out.println("Places du trajet 101 avant : " + placesAvant + ", apres : "
                + service.chercherTrajet(101).getPlacesDisponibles());
    }

    private static void scenarioDepart(ServiceReservation service) throws ReservationException {
        titre("Annulation dans le delai puis depart");
        service.reserver(3, 102, 6000);
        Ticket aAnnuler = service.chercherTicket(4);
        System.out.println("Avant : " + aAnnuler);
        service.annuler(4);
        System.out.println("Apres : " + aAnnuler);
        System.out.println(service.chercherTrajet(102));

        int valides = service.validerDepart(101);
        System.out.println(valides + " ticket(s) valide(s) au depart du trajet 101");
    }

    private static void bilan(ServiceReservation service) {
        titre("Bilan");
        Map<StatutTicket, Integer> compteurs = service.compterParStatut();
        for (Map.Entry<StatutTicket, Integer> entree : compteurs.entrySet()) {
            System.out.println(entree.getKey() + " : " + entree.getValue());
        }
        System.out.println("Chiffre d'affaires (tickets payes) : " + service.chiffreAffaires() + " FCFA");
    }
}
