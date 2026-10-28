public class Main {
    private static int total = 0;
    private static int reussis = 0;

    private static void noter(String titre, boolean reussi) {
        total = total + 1;
        if (reussi) {
            reussis = reussis + 1;
        }
        String verdict = "ECHEC";
        if (reussi) {
            verdict = "OK";
        }
        System.out.println("Essai " + total + " : " + titre + " -> " + verdict);
    }

    public static void main(String[] args) {
        System.out.println("=== Jalon 1 : modele et invariants ===");
        essaisVoyageur();
        essaisTrajet();
        essaisTicket();
        System.out.println();
        System.out.println(reussis + " essai(s) reussi(s) sur " + total);
    }

    private static void essaisVoyageur() {
        System.out.println();
        System.out.println("--- Voyageur ---");
        Voyageur sione = new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com");
        System.out.println(sione);
        noter("un voyageur valide garde son numero et son nom", sione.getNumero() == 1 && sione.getNom().startsWith("SIONE"));

        boolean nomRefuse = false;
        try {
            new Voyageur(2, "   ", "ingrid@example.com");
        } catch (IllegalArgumentException e) {
            nomRefuse = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("un nom vide est refuse", nomRefuse);

        boolean emailRefuse = false;
        try {
            new Voyageur(3, "NIKIEMA Pengdwende Hanifah", "hanifah.example.com");
        } catch (IllegalArgumentException e) {
            emailRefuse = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("une adresse e-mail sans arobase est refusee", emailRefuse);
    }

    private static void essaisTrajet() {
        System.out.println();
        System.out.println("--- Trajet ---");
        Trajet bobo = new Trajet(101, "Ouagadougou", "Bobo-Dioulasso", "2026-11-20", "08:00", 40, 120, 24);
        System.out.println(bobo);
        noter("un trajet valide commence avec toutes ses places libres", bobo.getPlacesDisponibles() == 40 && !bobo.estComplet());

        boolean villesRefusees = false;
        try {
            new Trajet(102, "Ouagadougou", "ouagadougou", "2026-11-20", "09:00", 40, 120, 24);
        } catch (IllegalArgumentException e) {
            villesRefusees = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("un depart et une arrivee identiques sont refuses", villesRefusees);

        boolean capaciteRefusee = false;
        try {
            new Trajet(103, "Ouagadougou", "Koudougou", "2026-11-20", "10:00", 0, 120, 24);
        } catch (IllegalArgumentException e) {
            capaciteRefusee = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("une capacite nulle est refusee", capaciteRefusee);
    }

    private static void essaisTicket() {
        System.out.println();
        System.out.println("--- Ticket ---");
        Voyageur sione = new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@example.com");
        Voyageur ingrid = new Voyageur(2, "NADEMBEGA Ingrid Oceane", "ingrid@example.com");

        Trajet bobo = new Trajet(101, "Ouagadougou", "Bobo-Dioulasso", "2026-11-20", "08:00", 40, 120, 24);
        Ticket premier = new Ticket(1, sione, bobo, 8000);
        System.out.println(premier);
        noter("reserver un ticket occupe une place du trajet", bobo.getPlacesDisponibles() == 39 && premier.getStatut() == StatutTicket.RESERVE);

        Trajet petit = new Trajet(102, "Ouagadougou", "Koudougou", "2026-11-21", "07:00", 1, 100, 24);
        new Ticket(2, sione, petit, 3000);
        boolean completRefuse = false;
        try {
            new Ticket(3, ingrid, petit, 3000);
        } catch (IllegalStateException e) {
            completRefuse = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("un trajet complet refuse un nouveau ticket sans changer son etat", completRefuse && petit.estComplet() && petit.getPlacesDisponibles() == 0);

        Trajet proche = new Trajet(103, "Ouagadougou", "Fada N'Gourma", "2026-10-21", "06:00", 5, 10, 24);
        Ticket tardif = new Ticket(4, ingrid, proche, 4000);
        boolean annulationRefusee = false;
        try {
            tardif.annuler();
        } catch (IllegalStateException e) {
            annulationRefusee = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("une annulation hors delai est refusee et le ticket reste reserve",
                annulationRefusee && tardif.getStatut() == StatutTicket.RESERVE && proche.getPlacesDisponibles() == 4);

        premier.annuler();
        noter("une annulation dans le delai libere la place", premier.getStatut() == StatutTicket.ANNULE && bobo.getPlacesDisponibles() == 40);

        tardif.validerAuto();
        boolean secondeValidationRefusee = false;
        try {
            tardif.validerAuto();
        } catch (IllegalStateException e) {
            secondeValidationRefusee = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("un ticket valide au depart passe a PAYE et ne peut pas etre valide deux fois",
                tardif.getStatut() == StatutTicket.PAYE && secondeValidationRefusee);
    }
}
