import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TrajetTest {

    private Trajet creerBus(int capacite, int heuresAvantDepart) {
        return new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-12-10", "08:00", capacite, heuresAvantDepart, "STAF");
    }

    @Test
    void unTrajetNeufAToutesSesPlacesLibres() {
        Trajet trajet = creerBus(40, 120);
        assertEquals(40, trajet.getPlacesDisponibles());
        assertFalse(trajet.estComplet());
    }

    @Test
    void reserverUnePlaceEnRetireUne() {
        Trajet trajet = creerBus(2, 120);
        trajet.reserverPlace();
        assertEquals(1, trajet.getPlacesDisponibles());
    }

    @Test
    void reserverSurUnTrajetCompletEstRefuseSansChangerLesPlaces() {
        Trajet trajet = creerBus(1, 120);
        trajet.reserverPlace();
        assertThrows(IllegalStateException.class, trajet::reserverPlace);
        assertEquals(0, trajet.getPlacesDisponibles());
        assertTrue(trajet.estComplet());
    }

    @Test
    void libererUnePlaceNonOccupeeEstRefuse() {
        Trajet trajet = creerBus(3, 120);
        assertThrows(IllegalStateException.class, trajet::libererPlace);
        assertEquals(3, trajet.getPlacesDisponibles());
    }

    @Test
    void uneCapaciteNulleEstRefusee() {
        assertThrows(IllegalArgumentException.class, () -> creerBus(0, 120));
    }

    @Test
    void desVillesIdentiquesSontRefusees() {
        assertThrows(IllegalArgumentException.class,
                () -> new TrajetBus(1, "Ouagadougou", "OUAGADOUGOU", "2026-12-10", "08:00", 10, 50, "STAF"));
    }

    @Test
    void lesDelaisDAnnulationDependentDuTypeDeTrajet() {
        Trajet bus = new TrajetBus(1, "Ouagadougou", "Koudougou", "2026-12-10", "08:00", 10, 50, "TSR");
        Trajet train = new TrajetTrain(2, "Ouagadougou", "Banfora", "2026-12-10", "08:00", 10, 50, "SIT 12");
        Trajet vol = new TrajetVol(3, "Ouagadougou", "Abidjan", "2026-12-10", "08:00", 10, 50, "AH 512");
        assertEquals(24, bus.delaiAnnulationHeures());
        assertEquals(48, train.delaiAnnulationHeures());
        assertEquals(72, vol.delaiAnnulationHeures());
    }

    @Test
    void avecCinquanteHeuresSeulLeVolRefuseEncoreLAnnulation() {
        Trajet bus = new TrajetBus(1, "Ouagadougou", "Koudougou", "2026-12-10", "08:00", 10, 50, "TSR");
        Trajet train = new TrajetTrain(2, "Ouagadougou", "Banfora", "2026-12-10", "08:00", 10, 50, "SIT 12");
        Trajet vol = new TrajetVol(3, "Ouagadougou", "Abidjan", "2026-12-10", "08:00", 10, 50, "AH 512");
        assertTrue(bus.annulationEncorePossible());
        assertTrue(train.annulationEncorePossible());
        assertFalse(vol.annulationEncorePossible());
    }

    @Test
    void lAnnulationExigeStrictementPlusQueLeDelai() {
        Trajet pile = new TrajetBus(1, "Ouagadougou", "Koudougou", "2026-12-10", "08:00", 10, 24, "TSR");
        Trajet juste = new TrajetBus(2, "Ouagadougou", "Koudougou", "2026-12-10", "08:00", 10, 25, "TSR");
        assertFalse(pile.annulationEncorePossible());
        assertTrue(juste.annulationEncorePossible());
    }
}
