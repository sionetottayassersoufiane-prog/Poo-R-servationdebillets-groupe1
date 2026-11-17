import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class VoyageurTest {

    @Test
    void unVoyageurValideGardeSesInformations() {
        Voyageur voyageur = new Voyageur(1, "SIONE Totta Yasser Soufiane", "sione@gmail.com");
        assertEquals(1, voyageur.getNumero());
        assertEquals("SIONE Totta Yasser Soufiane", voyageur.getNom());
        assertEquals("sione@gmail.com", voyageur.getEmail());
    }

    @Test
    void unNomVideEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> new Voyageur(1, "  ", "a@gmail.com"));
    }

    @Test
    void uneAdresseSansArobaseEstRefusee() {
        assertThrows(IllegalArgumentException.class, () -> new Voyageur(1, "Awa", "awa.gmail.com"));
    }

    @Test
    void unNumeroNulEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> new Voyageur(0, "Awa", "awa@gmail.com"));
    }

    @Test
    void deuxVoyageursDeMemeNumeroSontEgauxAvecLeMemeHashCode() {
        Voyageur premier = new Voyageur(5, "Awa", "awa@gmail.com");
        Voyageur copie = new Voyageur(5, "Awa", "awa@gmail.com");
        assertEquals(premier, copie);
        assertEquals(premier.hashCode(), copie.hashCode());
    }

    @Test
    void deuxVoyageursDeNumerosDifferentsNeSontPasEgaux() {
        Voyageur premier = new Voyageur(5, "Awa", "awa@gmail.com");
        Voyageur autre = new Voyageur(6, "Awa", "awa@gmail.com");
        assertNotEquals(premier, autre);
    }
}
