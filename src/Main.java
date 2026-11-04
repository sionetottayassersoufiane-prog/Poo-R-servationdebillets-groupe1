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
        System.out.println("=== Jalon 2 : composition et polymorphisme ===");
        essaisVoyageur();
        essaisTrajet();
        essaisTicket();
        essaisComposition();
        essaisPolymorphisme();
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
            new Voyageur(2, "   ", "ingrid@gmail.com");
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
        Trajet bobo = new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-11-20", "08:00", 40, 120, "STAF");
        System.out.println(bobo);
        noter("un trajet valide commence avec toutes ses places libres", bobo.getPlacesDisponibles() == 40 && !bobo.estComplet());

        boolean villesRefusees = false;
        try {
            new TrajetBus(102, "Ouagadougou", "ouagadougou", "2026-11-20", "09:00", 40, 120, "STAF");
        } catch (IllegalArgumentException e) {
            villesRefusees = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("un depart et une arrivee identiques sont refuses", villesRefusees);

        boolean capaciteRefusee = false;
        try {
            new TrajetBus(103, "Ouagadougou", "Koudougou", "2026-11-20", "10:00", 0, 120, "STAF");
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

        Trajet bobo = new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-11-20", "08:00", 40, 120, "STAF");
        Ticket premier = new Ticket(1, sione, bobo, 8000);
        System.out.println(premier);
        noter("reserver un ticket occupe une place du trajet", bobo.getPlacesDisponibles() == 39 && premier.getStatut() == StatutTicket.RESERVE);

        Trajet petit = new TrajetBus(102, "Ouagadougou", "Koudougou", "2026-11-21", "07:00", 1, 100, "TSR");
        new Ticket(2, sione, petit, 3000);
        boolean completRefuse = false;
        try {
            new Ticket(3, ingrid, petit, 3000);
        } catch (IllegalStateException e) {
            completRefuse = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("un trajet complet refuse un nouveau ticket sans changer son etat", completRefuse && petit.estComplet() && petit.getPlacesDisponibles() == 0);

        Trajet proche = new TrajetBus(103, "Ouagadougou", "Fada N'Gourma", "2026-10-21", "06:00", 5, 10, "STAF");
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

    private static void essaisComposition() {
        System.out.println();
        System.out.println("--- Composition : un trajet est fait d'un itineraire et d'un horaire ---");
        Trajet vol = new TrajetVol(201, "Ouagadougou", "Abidjan", "2026-11-25", "07:00", 150, 200, "AH 512");
        System.out.println(vol);
        noter("le trajet expose les villes de son itineraire et l'heure de son horaire",
                vol.getVilleDepart().equals("Ouagadougou") && vol.getVilleArrivee().equals("Abidjan") && vol.getHeureDepart().equals("07:00"));

        boolean horaireRefuse = false;
        try {
            new Horaire("2026-11-25", " ", 10);
        } catch (IllegalArgumentException e) {
            horaireRefuse = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("un horaire sans heure est refuse par sa propre classe", horaireRefuse);
    }

    private static void essaisPolymorphisme() {
        System.out.println();
        System.out.println("--- Polymorphisme : une meme methode, un comportement selon le trajet ---");
        Trajet[] trajets = new Trajet[3];
        trajets[0] = new TrajetBus(301, "Ouagadougou", "Ouahigouya", "2026-11-26", "09:00", 50, 60, "STAF");
        trajets[1] = new TrajetTrain(302, "Ouagadougou", "Banfora", "2026-11-26", "05:30", 200, 60, "SIT 12");
        trajets[2] = new TrajetVol(303, "Ouagadougou", "Marseille", "2026-11-26", "22:00", 180, 60, "AF 900");
        for (Trajet trajet : trajets) {
            System.out.println(trajet.getType() + " : annulation possible jusqu'a " + trajet.delaiAnnulationHeures()
                    + "h avant le depart, encore possible ? " + trajet.annulationEncorePossible());
        }
        noter("via une reference Trajet, les delais valent 24, 48 et 72 heures",
                trajets[0].delaiAnnulationHeures() == 24 && trajets[1].delaiAnnulationHeures() == 48 && trajets[2].delaiAnnulationHeures() == 72);
        noter("avec 60h avant le depart, seuls le bus et le train acceptent encore l'annulation",
                trajets[0].annulationEncorePossible() && trajets[1].annulationEncorePossible() && !trajets[2].annulationEncorePossible());

        Voyageur hanifah = new Voyageur(3, "NIKIEMA Pengdwende Hanifah", "hanifah@gmail.com");
        Ticket ticketVol = new Ticket(10, hanifah, trajets[2], 250000);
        boolean refuse = false;
        try {
            ticketVol.annuler();
        } catch (IllegalStateException e) {
            refuse = true;
            System.out.println("   refus : " + e.getMessage());
        }
        noter("le ticket n'a pas besoin de connaitre le type du trajet pour refuser l'annulation d'un vol",
                refuse && ticketVol.getStatut() == StatutTicket.RESERVE);
    }
}
