import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ServiceReservation {
    private final CatalogueTrajets catalogue;
    private final Map<Integer, Voyageur> voyageurs;
    private final Set<String> emailsUtilises;
    private final List<Ticket> tickets;
    private int prochainNumeroTicket;

    public ServiceReservation(CatalogueTrajets catalogue) {
        if (catalogue == null) {
            throw new IllegalArgumentException("le service a besoin d'un catalogue de trajets");
        }
        this.catalogue = catalogue;
        this.voyageurs = new HashMap<>();
        this.emailsUtilises = new HashSet<>();
        this.tickets = new ArrayList<>();
        this.prochainNumeroTicket = 1;
    }

    public void enregistrerVoyageur(Voyageur voyageur) throws DoublonException {
        if (voyageur == null) {
            throw new IllegalArgumentException("impossible d'enregistrer un voyageur nul");
        }
        String email = voyageur.getEmail().toLowerCase();
        if (voyageurs.containsKey(voyageur.getNumero())) {
            throw new DoublonException("le voyageur numero " + voyageur.getNumero() + " existe deja");
        }
        if (emailsUtilises.contains(email)) {
            throw new DoublonException("l'adresse " + voyageur.getEmail() + " est deja utilisee par un autre voyageur");
        }
        voyageurs.put(voyageur.getNumero(), voyageur);
        emailsUtilises.add(email);
    }

    public void ajouterTrajet(Trajet trajet) throws DoublonException {
        if (trajet == null) {
            throw new IllegalArgumentException("impossible d'ajouter un trajet nul");
        }
        if (!catalogue.ajouter(trajet)) {
            throw new DoublonException("le trajet numero " + trajet.getNumero() + " existe deja");
        }
    }

    public Ticket reserver(int numeroVoyageur, int numeroTrajet, int prix)
            throws ElementIntrouvableException, TrajetCompletException, ReservationInterditeException {
        if (prix < 0) {
            throw new IllegalArgumentException("le prix d'une reservation ne peut pas etre negatif");
        }
        Voyageur voyageur = chercherVoyageur(numeroVoyageur);
        Trajet trajet = chercherTrajet(numeroTrajet);
        if (possedeUnTicketActif(voyageur, trajet)) {
            throw new ReservationInterditeException(voyageur.getNom() + " a deja un ticket sur le trajet " + numeroTrajet);
        }
        if (trajet.estComplet()) {
            throw new TrajetCompletException("le trajet " + numeroTrajet + " est complet");
        }
        Ticket ticket = new Ticket(prochainNumeroTicket, voyageur, trajet, prix);
        tickets.add(ticket);
        prochainNumeroTicket = prochainNumeroTicket + 1;
        return ticket;
    }

    public void annuler(int numeroTicket) throws ElementIntrouvableException, AnnulationImpossibleException {
        Ticket ticket = chercherTicket(numeroTicket);
        if (ticket.getStatut() != StatutTicket.RESERVE) {
            throw new AnnulationImpossibleException("le ticket " + numeroTicket + " n'est pas reserve, son statut est "
                    + ticket.getStatut());
        }
        if (!ticket.getTrajet().annulationEncorePossible()) {
            throw new AnnulationImpossibleException("le ticket " + numeroTicket + " ne peut plus etre annule : il faut "
                    + "plus de " + ticket.getTrajet().delaiAnnulationHeures() + "h avant le depart");
        }
        ticket.annuler();
    }

    public int validerDepart(int numeroTrajet) throws ElementIntrouvableException, ReservationInterditeException {
        Trajet trajet = chercherTrajet(numeroTrajet);
        if (!trajet.departAtteint()) {
            throw new ReservationInterditeException("le depart du trajet " + numeroTrajet
                    + " n'est pas encore atteint (dans " + trajet.getHeuresAvantDepart() + "h) : aucun ticket valide");
        }
        int valides = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getTrajet().equals(trajet) && ticket.getStatut() == StatutTicket.RESERVE) {
                ticket.validerAuto();
                valides = valides + 1;
            }
        }
        return valides;
    }

    public List<Ticket> ticketsDuVoyageur(int numeroVoyageur) throws ElementIntrouvableException {
        Voyageur voyageur = chercherVoyageur(numeroVoyageur);
        List<Ticket> resultat = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getVoyageur().equals(voyageur)) {
                resultat.add(ticket);
            }
        }
        return resultat;
    }

    public Map<StatutTicket, Integer> compterParStatut() {
        Map<StatutTicket, Integer> compteurs = new TreeMap<>();
        for (StatutTicket statut : StatutTicket.values()) {
            compteurs.put(statut, 0);
        }
        for (Ticket ticket : tickets) {
            compteurs.put(ticket.getStatut(), compteurs.get(ticket.getStatut()) + 1);
        }
        return compteurs;
    }

    public int chiffreAffaires() {
        int total = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getStatut() == StatutTicket.PAYE) {
                total = total + ticket.getPrix();
            }
        }
        return total;
    }

    public List<Trajet> tousLesTrajets() {
        return catalogue.parcourir();
    }

    public List<Trajet> trajetsDisponibles() {
        return catalogue.trajetsDisponibles();
    }

    public List<Trajet> rechercherTrajets(String motCle) {
        return catalogue.rechercherParVille(motCle);
    }

    public Voyageur chercherVoyageur(int numero) throws ElementIntrouvableException {
        Voyageur voyageur = voyageurs.get(numero);
        if (voyageur == null) {
            throw new ElementIntrouvableException("le voyageur numero " + numero + " est introuvable");
        }
        return voyageur;
    }

    public Trajet chercherTrajet(int numero) throws ElementIntrouvableException {
        Trajet trajet = catalogue.trouver(numero);
        if (trajet == null) {
            throw new ElementIntrouvableException("le trajet numero " + numero + " est introuvable");
        }
        return trajet;
    }

    public Ticket chercherTicket(int numero) throws ElementIntrouvableException {
        for (Ticket ticket : tickets) {
            if (ticket.getNumero() == numero) {
                return ticket;
            }
        }
        throw new ElementIntrouvableException("le ticket numero " + numero + " est introuvable");
    }

    public int nombreVoyageurs() {
        return voyageurs.size();
    }

    public int nombreTickets() {
        return tickets.size();
    }

    private boolean possedeUnTicketActif(Voyageur voyageur, Trajet trajet) {
        for (Ticket ticket : tickets) {
            boolean memeVoyageur = ticket.getVoyageur().equals(voyageur);
            boolean memeTrajet = ticket.getTrajet().equals(trajet);
            boolean actif = ticket.getStatut() == StatutTicket.RESERVE || ticket.getStatut() == StatutTicket.PAYE;
            if (memeVoyageur && memeTrajet && actif) {
                return true;
            }
        }
        return false;
    }
}
