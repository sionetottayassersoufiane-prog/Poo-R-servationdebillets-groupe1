import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TicketTest {

    private Voyageur voyageur() {
        return new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com");
    }

    private Trajet bus(int capacite, int heuresAvantDepart) {
        return new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-12-10", "08:00", capacite, heuresAvantDepart, "STAF");
    }

    @Test
    void creerUnTicketOccupeUnePlaceEtLeMetEnReserve() {
        Trajet trajet = bus(10, 120);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        assertEquals(StatutTicket.RESERVE, ticket.getStatut());
        assertEquals(9, trajet.getPlacesDisponibles());
    }

    @Test
    void unPrixNegatifEstRefuseSansOccuperDePlace() {
        Trajet trajet = bus(10, 120);
        assertThrows(IllegalArgumentException.class, () -> new Ticket(1, voyageur(), trajet, -1));
        assertEquals(10, trajet.getPlacesDisponibles());
    }

    @Test
    void unTicketSurUnTrajetCompletEstRefuse() {
        Trajet trajet = bus(1, 120);
        new Ticket(1, voyageur(), trajet, 8000);
        assertThrows(IllegalStateException.class, () -> new Ticket(2, voyageur(), trajet, 8000));
        assertEquals(0, trajet.getPlacesDisponibles());
    }

    @Test
    void annulerDansLeDelaiLibereLaPlace() {
        Trajet trajet = bus(10, 120);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        ticket.annuler();
        assertEquals(StatutTicket.ANNULE, ticket.getStatut());
        assertEquals(10, trajet.getPlacesDisponibles());
    }

    @Test
    void annulerHorsDelaiEstRefuseSansChangerLEtat() {
        Trajet trajet = bus(10, 5);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        assertThrows(IllegalStateException.class, ticket::annuler);
        assertEquals(StatutTicket.RESERVE, ticket.getStatut());
        assertEquals(9, trajet.getPlacesDisponibles());
    }

    @Test
    void annulerDeuxFoisEstRefuseSansLibererUneSecondePlace() {
        Trajet trajet = bus(10, 120);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        ticket.annuler();
        assertThrows(IllegalStateException.class, ticket::annuler);
        assertEquals(10, trajet.getPlacesDisponibles());
    }

    @Test
    void validerAuDepartPasseLeTicketEnPaye() {
        Ticket ticket = new Ticket(1, voyageur(), bus(10, 0), 8000);
        ticket.validerAuto();
        assertEquals(StatutTicket.PAYE, ticket.getStatut());
        assertFalse(ticket.peutEtreAnnule());
    }

    @Test
    void validerAvantLeDepartEstRefuseSansChangerLeTicketNiLesPlaces() {
        Trajet trajet = bus(10, 120);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        assertThrows(IllegalStateException.class, ticket::validerAuto);
        assertEquals(StatutTicket.RESERVE, ticket.getStatut());
        assertEquals(9, trajet.getPlacesDisponibles());
    }

    @Test
    void validerUneHeureAvantLeDepartEstEncoreRefuse() {
        Trajet trajet = bus(10, 1);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        assertThrows(IllegalStateException.class, ticket::validerAuto);
        assertEquals(StatutTicket.RESERVE, ticket.getStatut());
        assertEquals(9, trajet.getPlacesDisponibles());
    }

    @Test
    void validerUnTicketAnnuleAvantLeDepartEstRefuse() {
        Trajet trajet = bus(10, 120);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        ticket.annuler();
        assertThrows(IllegalStateException.class, ticket::validerAuto);
        assertEquals(StatutTicket.ANNULE, ticket.getStatut());
        assertEquals(10, trajet.getPlacesDisponibles());
    }

    @Test
    void annulerExactementAuLimiteDuBusEstRefuse() {
        Trajet trajet = bus(10, 24);
        Ticket ticket = new Ticket(1, voyageur(), trajet, 8000);
        assertFalse(ticket.peutEtreAnnule());
        assertThrows(IllegalStateException.class, ticket::annuler);
        assertEquals(StatutTicket.RESERVE, ticket.getStatut());
        assertEquals(9, trajet.getPlacesDisponibles());
    }

    @Test
    void validerUnTicketAnnuleEstRefuse() {
        Ticket ticket = new Ticket(1, voyageur(), bus(10, 120), 8000);
        ticket.annuler();
        assertThrows(IllegalStateException.class, ticket::validerAuto);
        assertEquals(StatutTicket.ANNULE, ticket.getStatut());
    }
}
