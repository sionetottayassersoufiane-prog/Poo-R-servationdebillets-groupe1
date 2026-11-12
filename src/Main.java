import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        System.out.println("=== Jalon 3 : interfaces et collections ===");
        essaisVoyageur();
        essaisTrajet();
        essaisTicket();
        essaisComposition();
        essaisPolymorphisme();
        essaisIdentite();
        essaisCatalogue();
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
            new Voyageur(3, "NIKIEMA Pengdwende Hanifah", "hanifah.gmail.com");
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
        Voyageur sione = new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com");
        Voyageur ingrid = new Voyageur(2, "NADEMBEGA Ingrid Oceane", "ingrid@gmail.com");

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

    private static void essaisIdentite() {
        System.out.println();
        System.out.println("--- Identite : deux objets avec le meme numero sont la meme entite ---");
        Voyageur premier = new Voyageur(7, "KONATE Awa", "awa@gmail.com");
        Voyageur copie = new Voyageur(7, "KONATE Awa", "awa@gmail.com");
        System.out.println("equals : " + premier.equals(copie) + ", meme hashCode : " + (premier.hashCode() == copie.hashCode()));
        noter("deux voyageurs de meme numero sont egaux et ont le meme hashCode",
                premier.equals(copie) && premier.hashCode() == copie.hashCode());

        Set<Voyageur> ensemble = new HashSet<>();
        ensemble.add(premier);
        ensemble.add(copie);
        noter("un HashSet ne garde qu'un seul voyageur pour un meme numero", ensemble.size() == 1);

        Trajet bus = new TrajetBus(401, "Ouagadougou", "Koudougou", "2026-12-01", "07:00", 30, 150, "TSR");
        Trajet vol = new TrajetVol(401, "Ouagadougou", "Abidjan", "2026-12-02", "08:00", 120, 150, "AH 512");
        noter("deux trajets de meme numero sont consideres comme le meme trajet", bus.equals(vol) && bus.hashCode() == vol.hashCode());
    }

    private static void essaisCatalogue() {
        System.out.println();
        System.out.println("--- Catalogue de trajets (interface CatalogueTrajets) ---");
        CatalogueTrajets catalogue = new CatalogueTrajetsMemoire();
        noter("un catalogue neuf est vide", catalogue.estVide() && catalogue.taille() == 0);

        Trajet bobo = new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-12-10", "08:00", 40, 120, "STAF");
        Trajet banfora = new TrajetTrain(102, "Ouagadougou", "Banfora", "2026-12-05", "05:30", 200, 100, "SIT 12");
        Trajet marseille = new TrajetVol(103, "Ouagadougou", "Marseille", "2026-12-08", "22:00", 1, 90, "AF 900");
        boolean ajouts = catalogue.ajouter(bobo) && catalogue.ajouter(banfora) && catalogue.ajouter(marseille);
        noter("trois trajets differents sont ajoutes", ajouts && catalogue.taille() == 3);

        Trajet doublon = new TrajetBus(101, "Ouagadougou", "Koudougou", "2026-12-11", "09:00", 20, 120, "TSR");
        boolean doublonRefuse = !catalogue.ajouter(doublon);
        noter("un numero deja present est refuse sans remplacer l'ancien trajet",
                doublonRefuse && catalogue.taille() == 3 && catalogue.trouver(101) == bobo);

        noter("rechercher un numero present renvoie le trajet", catalogue.trouver(102) == banfora);
        noter("rechercher un numero absent renvoie null", catalogue.trouver(999) == null);

        List<Trajet> parcours = catalogue.parcourir();
        System.out.println("Parcours trie par date :");
        for (Trajet trajet : parcours) {
            System.out.println("   " + trajet);
        }
        noter("le parcours est trie par date puis par heure",
                parcours.get(0) == banfora && parcours.get(1) == marseille && parcours.get(2) == bobo);

        parcours.clear();
        noter("vider la liste recue ne modifie pas le catalogue", catalogue.taille() == 3 && catalogue.parcourir().size() == 3);

        List<Trajet> parMotCle = catalogue.rechercherParVille("BOBO");
        noter("la recherche par ville ignore la casse et ne modifie pas le catalogue",
                parMotCle.size() == 1 && parMotCle.get(0) == bobo && catalogue.taille() == 3);

        Map<String, List<Trajet>> groupes = catalogue.regrouperParType();
        System.out.println("Regroupement par type : " + groupes.keySet());
        noter("le regroupement par type donne un trajet de bus, de train et de vol",
                groupes.size() == 3 && groupes.get("Bus").size() == 1 && groupes.get("Train").size() == 1 && groupes.get("Vol").size() == 1);

        Voyageur sione = new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com");
        new Ticket(1, sione, marseille, 250000);
        List<Trajet> disponibles = catalogue.trajetsDisponibles();
        noter("un trajet complet n'apparait plus dans les trajets disponibles",
                disponibles.size() == 2 && !disponibles.contains(marseille));

        noter("retirer un trajet present le supprime du catalogue", catalogue.retirer(102) && catalogue.taille() == 2 && catalogue.trouver(102) == null);
        noter("retirer un trajet absent ne change rien", !catalogue.retirer(102) && catalogue.taille() == 2);
    }
}
