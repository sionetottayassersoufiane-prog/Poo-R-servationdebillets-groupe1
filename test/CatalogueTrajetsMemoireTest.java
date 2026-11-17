import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CatalogueTrajetsMemoireTest {

    private final Trajet bobo = new TrajetBus(101, "Ouagadougou", "Bobo-Dioulasso", "2026-12-10", "08:00", 40, 120, "STAF");
    private final Trajet banfora = new TrajetTrain(102, "Ouagadougou", "Banfora", "2026-12-05", "05:30", 200, 100, "SIT 12");
    private final Trajet marseille = new TrajetVol(103, "Ouagadougou", "Marseille", "2026-12-08", "22:00", 1, 90, "AF 900");

    private CatalogueTrajets catalogueRempli() {
        CatalogueTrajets catalogue = new CatalogueTrajetsMemoire();
        catalogue.ajouter(bobo);
        catalogue.ajouter(banfora);
        catalogue.ajouter(marseille);
        return catalogue;
    }

    @Test
    void unCatalogueNeufEstVide() {
        CatalogueTrajets catalogue = new CatalogueTrajetsMemoire();
        assertTrue(catalogue.estVide());
        assertEquals(0, catalogue.taille());
    }

    @Test
    void ajouterUnTrajetLeRendRetrouvable() {
        CatalogueTrajets catalogue = new CatalogueTrajetsMemoire();
        assertTrue(catalogue.ajouter(bobo));
        assertSame(bobo, catalogue.trouver(101));
    }

    @Test
    void ajouterUnNumeroDejaPresentEstRefuseSansRemplacerLAncien() {
        CatalogueTrajets catalogue = catalogueRempli();
        Trajet intrus = new TrajetBus(101, "Ouagadougou", "Koudougou", "2026-12-11", "09:00", 20, 120, "TSR");
        assertFalse(catalogue.ajouter(intrus));
        assertEquals(3, catalogue.taille());
        assertSame(bobo, catalogue.trouver(101));
    }

    @Test
    void ajouterUnTrajetNulEstRefuse() {
        CatalogueTrajets catalogue = new CatalogueTrajetsMemoire();
        assertThrows(IllegalArgumentException.class, () -> catalogue.ajouter(null));
        assertTrue(catalogue.estVide());
    }

    @Test
    void chercherUnNumeroAbsentRenvoieNull() {
        assertNull(catalogueRempli().trouver(999));
    }

    @Test
    void retirerUnTrajetPresentLeSupprime() {
        CatalogueTrajets catalogue = catalogueRempli();
        assertTrue(catalogue.retirer(102));
        assertNull(catalogue.trouver(102));
        assertEquals(2, catalogue.taille());
    }

    @Test
    void retirerUnTrajetAbsentNeChangeRien() {
        CatalogueTrajets catalogue = catalogueRempli();
        assertFalse(catalogue.retirer(999));
        assertEquals(3, catalogue.taille());
    }

    @Test
    void leParcoursEstTrieParDatePuisParHeure() {
        List<Trajet> parcours = catalogueRempli().parcourir();
        assertSame(banfora, parcours.get(0));
        assertSame(marseille, parcours.get(1));
        assertSame(bobo, parcours.get(2));
    }

    @Test
    void viderLeParcoursRecuNeModifiePasLeCatalogue() {
        CatalogueTrajets catalogue = catalogueRempli();
        catalogue.parcourir().clear();
        assertEquals(3, catalogue.taille());
        assertEquals(3, catalogue.parcourir().size());
    }

    @Test
    void laRechercheParVilleIgnoreLaCasse() {
        List<Trajet> resultat = catalogueRempli().rechercherParVille("BOBO");
        assertEquals(1, resultat.size());
        assertSame(bobo, resultat.get(0));
    }

    @Test
    void laRechercheParVilleTrouveAussiLaVilleDeDepart() {
        assertEquals(3, catalogueRempli().rechercherParVille("ouaga").size());
    }

    @Test
    void uneRechercheSansMotCleEstRefusee() {
        CatalogueTrajets catalogue = catalogueRempli();
        assertThrows(IllegalArgumentException.class, () -> catalogue.rechercherParVille("  "));
    }

    @Test
    void leRegroupementParTypeSepareBusTrainEtVol() {
        Map<String, List<Trajet>> groupes = catalogueRempli().regrouperParType();
        assertEquals(3, groupes.size());
        assertSame(bobo, groupes.get("Bus").get(0));
        assertSame(banfora, groupes.get("Train").get(0));
        assertSame(marseille, groupes.get("Vol").get(0));
    }

    @Test
    void unTrajetCompletNEstPlusDansLesTrajetsDisponibles() {
        CatalogueTrajets catalogue = catalogueRempli();
        marseille.reserverPlace();
        List<Trajet> disponibles = catalogue.trajetsDisponibles();
        assertEquals(2, disponibles.size());
        assertFalse(disponibles.contains(marseille));
    }
}
