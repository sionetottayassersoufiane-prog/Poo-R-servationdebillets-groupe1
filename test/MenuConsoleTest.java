import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

class MenuConsoleTest {

    private ServiceReservation creerService() throws ReservationException {
        ServiceReservation service = new ServiceReservation(new CatalogueTrajetsMemoire());
        service.enregistrerVoyageur(new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com"));
        service.ajouterTrajet(new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-12-10", "08:00", 2, 120, "STAF"));
        return service;
    }

    private String jouer(ServiceReservation service, String saisie) {
        ByteArrayOutputStream tampon = new ByteArrayOutputStream();
        MenuConsole menu = new MenuConsole(service, new ByteArrayInputStream(saisie.getBytes()), new PrintStream(tampon));
        menu.lancer();
        return tampon.toString();
    }

    @Test
    void quitterAfficheAuRevoir() throws ReservationException {
        String sortie = jouer(creerService(), "0\n");
        assertTrue(sortie.contains("Au revoir."));
    }

    @Test
    void unChoixInconnuEstSignaleSansArreterLeMenu() throws ReservationException {
        String sortie = jouer(creerService(), "abc\n0\n");
        assertTrue(sortie.contains("Choix invalide"));
        assertTrue(sortie.contains("Au revoir."));
    }

    @Test
    void reserverParLeMenuCreeUnTicket() throws ReservationException {
        ServiceReservation service = creerService();
        String sortie = jouer(service, "4\n1\n101\n8000\n0\n");
        assertEquals(1, service.nombreTickets());
        assertTrue(sortie.contains("Reservation faite"));
    }

    @Test
    void unPrixQuiNEstPasUnNombreEstRefuseSansCreerDeTicket() throws ReservationException {
        ServiceReservation service = creerService();
        String sortie = jouer(service, "4\n1\n101\nbeaucoup\n0\n");
        assertEquals(0, service.nombreTickets());
        assertTrue(sortie.contains("Saisie refusee"));
    }

    @Test
    void annulerUnTicketInconnuAfficheUnRefus() throws ReservationException {
        String sortie = jouer(creerService(), "5\n42\n0\n");
        assertTrue(sortie.contains("Operation refusee"));
        assertTrue(sortie.contains("introuvable"));
    }

    @Test
    void enregistrerUnVoyageurParLeMenuAugmenteLeNombreDeVoyageurs() throws ReservationException {
        ServiceReservation service = creerService();
        jouer(service, "3\n2\nNIKIEMA Pengdwende Hanifah\nhanifa@gmail.com\n0\n");
        assertEquals(2, service.nombreVoyageurs());
    }
}
