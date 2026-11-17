import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ServiceReservationTest {

    private ServiceReservation creerService() throws ReservationException {
        ServiceReservation service = new ServiceReservation(new CatalogueTrajetsMemoire());
        service.enregistrerVoyageur(new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com"));
        service.enregistrerVoyageur(new Voyageur(2, "NADEMBEGA Ingrid Oceane", "ingrid@gmail.com"));
        service.enregistrerVoyageur(new Voyageur(3, "NIKIEMA Pengdwende Hanifah", "hanifah@gmail.com"));
        service.ajouterTrajet(new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-12-10", "08:00", 2, 120, "STAF"));
        service.ajouterTrajet(new TrajetVol(103, "Ouagadougou", "Abidjan", "2026-12-08", "07:00", 5, 60, "AH 512"));
        service.ajouterTrajet(new TrajetBus(104, "Ouagadougou", "Koudougou", "2026-11-30", "09:00", 30, 10, "TSR"));
        return service;
    }

    @Test
    void reserverCreeUnTicketReserveEtRetireUnePlace() throws ReservationException {
        ServiceReservation service = creerService();
        Ticket ticket = service.reserver(1, 101, 8000);
        assertEquals(1, ticket.getNumero());
        assertEquals(StatutTicket.RESERVE, ticket.getStatut());
        assertEquals(1, service.chercherTrajet(101).getPlacesDisponibles());
        assertEquals(1, service.nombreTickets());
    }

    @Test
    void lesNumerosDeTicketSontSuccessifs() throws ReservationException {
        ServiceReservation service = creerService();
        Ticket premier = service.reserver(1, 101, 8000);
        Ticket second = service.reserver(2, 101, 8000);
        assertEquals(1, premier.getNumero());
        assertEquals(2, second.getNumero());
    }

    @Test
    void unPrixNegatifEstRefuseSansRienChanger() throws ReservationException {
        ServiceReservation service = creerService();
        assertThrows(IllegalArgumentException.class, () -> service.reserver(1, 101, -10));
        assertEquals(0, service.nombreTickets());
        assertEquals(2, service.chercherTrajet(101).getPlacesDisponibles());
    }

    @Test
    void unVoyageurAbsentEstRefuseSansRienChanger() throws ReservationException {
        ServiceReservation service = creerService();
        assertThrows(ElementIntrouvableException.class, () -> service.reserver(99, 101, 8000));
        assertEquals(0, service.nombreTickets());
        assertEquals(2, service.chercherTrajet(101).getPlacesDisponibles());
    }

    @Test
    void unTrajetAbsentEstRefuseSansRienChanger() throws ReservationException {
        ServiceReservation service = creerService();
        assertThrows(ElementIntrouvableException.class, () -> service.reserver(1, 999, 8000));
        assertEquals(0, service.nombreTickets());
    }

    @Test
    void unTrajetCompletEstRefuseSansCreerDeTicket() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        service.reserver(2, 101, 8000);
        assertThrows(TrajetCompletException.class, () -> service.reserver(3, 101, 8000));
        assertEquals(2, service.nombreTickets());
        assertEquals(0, service.chercherTrajet(101).getPlacesDisponibles());
    }

    @Test
    void reserverDeuxFoisLeMemeTrajetEstRefuseSansOccuperUneSecondePlace() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        assertThrows(ReservationInterditeException.class, () -> service.reserver(1, 101, 8000));
        assertEquals(1, service.nombreTickets());
        assertEquals(1, service.chercherTrajet(101).getPlacesDisponibles());
    }

    @Test
    void apresUneAnnulationLeVoyageurPeutReserverDeNouveau() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        service.annuler(1);
        Ticket nouveau = service.reserver(1, 101, 8000);
        assertEquals(StatutTicket.RESERVE, nouveau.getStatut());
        assertEquals(1, service.chercherTrajet(101).getPlacesDisponibles());
    }

    @Test
    void annulerDansLeDelaiLibereLaPlace() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        service.annuler(1);
        assertEquals(StatutTicket.ANNULE, service.chercherTicket(1).getStatut());
        assertEquals(2, service.chercherTrajet(101).getPlacesDisponibles());
    }

    @Test
    void annulerHorsDelaiEstRefuseSansChangerLEtat() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 103, 250000);
        assertThrows(AnnulationImpossibleException.class, () -> service.annuler(1));
        assertEquals(StatutTicket.RESERVE, service.chercherTicket(1).getStatut());
        assertEquals(4, service.chercherTrajet(103).getPlacesDisponibles());
    }

    @Test
    void annulerDeuxFoisLeMemeTicketEstRefuse() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        service.annuler(1);
        assertThrows(AnnulationImpossibleException.class, () -> service.annuler(1));
        assertEquals(2, service.chercherTrajet(101).getPlacesDisponibles());
    }

    @Test
    void annulerUnTicketAbsentEstRefuse() throws ReservationException {
        ServiceReservation service = creerService();
        assertThrows(ElementIntrouvableException.class, () -> service.annuler(42));
    }

    @Test
    void validerLeDepartPasseLesTicketsReservesEnPayes() throws ReservationException {
        ServiceReservation service = creerService();
        service.ajouterTrajet(new TrajetBus(105, "Ouagadougou", "Ziniare", "2026-11-17", "08:00", 2, 0, "TSR"));
        service.reserver(1, 105, 8000);
        service.reserver(2, 105, 8000);
        service.reserver(3, 101, 8000);
        service.annuler(3);
        assertEquals(2, service.validerDepart(105));
        assertEquals(StatutTicket.PAYE, service.chercherTicket(1).getStatut());
        assertEquals(StatutTicket.PAYE, service.chercherTicket(2).getStatut());
        assertEquals(StatutTicket.ANNULE, service.chercherTicket(3).getStatut());
    }

    @Test
    void validerLeDepartAvantLHeureEstRefuseSansRienChanger() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        assertThrows(ReservationInterditeException.class, () -> service.validerDepart(101));
        assertEquals(StatutTicket.RESERVE, service.chercherTicket(1).getStatut());
        assertEquals(1, service.chercherTrajet(101).getPlacesDisponibles());
        assertEquals(1, service.nombreTickets());
        assertEquals(0, service.chiffreAffaires());
    }

    @Test
    void validerLeDepartDUnTrajetAbsentEstRefuse() throws ReservationException {
        ServiceReservation service = creerService();
        assertThrows(ElementIntrouvableException.class, () -> service.validerDepart(999));
    }

    @Test
    void unEmailDejaUtiliseEstRefuseSansAjouterDeVoyageur() throws ReservationException {
        ServiceReservation service = creerService();
        Voyageur intrus = new Voyageur(4, "Intrus", "SIONE@gmail.com");
        assertThrows(DoublonException.class, () -> service.enregistrerVoyageur(intrus));
        assertEquals(3, service.nombreVoyageurs());
    }

    @Test
    void unNumeroDeVoyageurDejaPrisEstRefuse() throws ReservationException {
        ServiceReservation service = creerService();
        Voyageur intrus = new Voyageur(1, "Intrus", "intrus@gmail.com");
        assertThrows(DoublonException.class, () -> service.enregistrerVoyageur(intrus));
        assertEquals(3, service.nombreVoyageurs());
    }

    @Test
    void unTrajetDejaPresentEstRefuseSansRemplacerLAncien() throws ReservationException {
        ServiceReservation service = creerService();
        Trajet ancien = service.chercherTrajet(101);
        Trajet intrus = new TrajetBus(101, "Ouagadougou", "Koudougou", "2026-12-11", "09:00", 20, 120, "TSR");
        assertThrows(DoublonException.class, () -> service.ajouterTrajet(intrus));
        assertSame(ancien, service.chercherTrajet(101));
        assertEquals(3, service.tousLesTrajets().size());
    }

    @Test
    void leChiffreDAffairesNeCompteQueLesTicketsPayes() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        service.reserver(2, 101, 6000);
        service.ajouterTrajet(new TrajetBus(105, "Ouagadougou", "Ziniare", "2026-11-17", "08:00", 5, 0, "TSR"));
        service.reserver(1, 105, 8000);
        service.reserver(2, 105, 6000);
        assertEquals(0, service.chiffreAffaires());
        service.validerDepart(105);
        assertEquals(14000, service.chiffreAffaires());
    }

    @Test
    void compterParStatutDonneUneLignePourChaqueStatut() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        service.reserver(2, 101, 8000);
        service.annuler(2);
        Map<StatutTicket, Integer> compteurs = service.compterParStatut();
        assertEquals(1, compteurs.get(StatutTicket.RESERVE).intValue());
        assertEquals(1, compteurs.get(StatutTicket.ANNULE).intValue());
        assertEquals(0, compteurs.get(StatutTicket.PAYE).intValue());
    }

    @Test
    void lesTicketsDUnVoyageurSontRetrouves() throws ReservationException {
        ServiceReservation service = creerService();
        service.reserver(1, 101, 8000);
        service.reserver(1, 103, 250000);
        service.reserver(2, 101, 8000);
        List<Ticket> tickets = service.ticketsDuVoyageur(1);
        assertEquals(2, tickets.size());
    }

    @Test
    void lesTicketsDUnVoyageurAbsentSontRefuses() throws ReservationException {
        ServiceReservation service = creerService();
        assertThrows(ElementIntrouvableException.class, () -> service.ticketsDuVoyageur(99));
    }
}
