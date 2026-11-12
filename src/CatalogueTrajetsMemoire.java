import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CatalogueTrajetsMemoire implements CatalogueTrajets {
    private final Map<Integer, Trajet> trajets;

    public CatalogueTrajetsMemoire() {
        this.trajets = new HashMap<>();
    }

    @Override
    public boolean ajouter(Trajet trajet) {
        if (trajet == null) {
            throw new IllegalArgumentException("impossible d'ajouter un trajet nul");
        }
        if (trajets.containsKey(trajet.getNumero())) {
            return false;
        }
        trajets.put(trajet.getNumero(), trajet);
        return true;
    }

    @Override
    public Trajet trouver(int numero) {
        return trajets.get(numero);
    }

    @Override
    public boolean retirer(int numero) {
        return trajets.remove(numero) != null;
    }

    @Override
    public boolean estVide() {
        return trajets.isEmpty();
    }

    @Override
    public int taille() {
        return trajets.size();
    }

    @Override
    public List<Trajet> parcourir() {
        List<Trajet> copie = new ArrayList<>(trajets.values());
        Collections.sort(copie);
        return copie;
    }

    @Override
    public List<Trajet> trajetsDisponibles() {
        List<Trajet> resultat = new ArrayList<>();
        for (Trajet trajet : trajets.values()) {
            if (!trajet.estComplet()) {
                resultat.add(trajet);
            }
        }
        Collections.sort(resultat);
        return resultat;
    }

    @Override
    public List<Trajet> rechercherParVille(String motCle) {
        if (motCle == null || motCle.isBlank()) {
            throw new IllegalArgumentException("le mot-cle de recherche est vide");
        }
        String motif = motCle.trim().toLowerCase();
        List<Trajet> resultat = new ArrayList<>();
        for (Trajet trajet : trajets.values()) {
            boolean departTrouve = trajet.getVilleDepart().toLowerCase().contains(motif);
            boolean arriveeTrouvee = trajet.getVilleArrivee().toLowerCase().contains(motif);
            if (departTrouve || arriveeTrouvee) {
                resultat.add(trajet);
            }
        }
        Collections.sort(resultat);
        return resultat;
    }

    @Override
    public Map<String, List<Trajet>> regrouperParType() {
        Map<String, List<Trajet>> groupes = new TreeMap<>();
        for (Trajet trajet : parcourir()) {
            List<Trajet> groupe = groupes.get(trajet.getType());
            if (groupe == null) {
                groupe = new ArrayList<>();
                groupes.put(trajet.getType(), groupe);
            }
            groupe.add(trajet);
        }
        return groupes;
    }
}
