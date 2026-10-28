public class Ticket {
    private final int numero;
    private final Voyageur voyageur;
    private final Trajet trajet;
    private final int prix;
    private StatutTicket statut;

    public Ticket(int numero, Voyageur voyageur, Trajet trajet, int prix) {
        if (numero <= 0) {
            throw new IllegalArgumentException("le numero du ticket doit etre strictement positif");
        }
        if (voyageur == null || trajet == null) {
            throw new IllegalArgumentException("un ticket est lie a un voyageur et a un trajet");
        }
        if (prix < 0) {
            throw new IllegalArgumentException("le prix d'un ticket ne peut pas etre negatif");
        }
        trajet.reserverPlace();
        this.numero = numero;
        this.voyageur = voyageur;
        this.trajet = trajet;
        this.prix = prix;
        this.statut = StatutTicket.RESERVE;
    }

    public void annuler() {
        if (statut != StatutTicket.RESERVE) {
            throw new IllegalStateException("seul un ticket reserve peut etre annule");
        }
        if (!trajet.annulationEncorePossible()) {
            throw new IllegalStateException("le delai d'annulation de ce trajet est depasse");
        }
        statut = StatutTicket.ANNULE;
        trajet.libererPlace();
    }

    public void validerAuto() {
        if (statut != StatutTicket.RESERVE) {
            throw new IllegalStateException("seul un ticket reserve peut etre valide au depart");
        }
        statut = StatutTicket.PAYE;
    }

    public int getNumero() {
        return numero;
    }

    public Voyageur getVoyageur() {
        return voyageur;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public int getPrix() {
        return prix;
    }

    public StatutTicket getStatut() {
        return statut;
    }

    @Override
    public String toString() {
        return "Ticket " + numero + " : " + voyageur.getNom() + ", trajet " + trajet.getNumero()
                + " (" + trajet.getVilleDepart() + " -> " + trajet.getVilleArrivee() + "), "
                + prix + " FCFA, statut " + statut;
    }
}
