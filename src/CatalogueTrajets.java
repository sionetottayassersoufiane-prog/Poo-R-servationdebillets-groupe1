import java.util.List;
import java.util.Map;

public interface CatalogueTrajets {
    boolean ajouter(Trajet trajet);

    Trajet trouver(int numero);

    boolean retirer(int numero);

    boolean estVide();

    int taille();

    List<Trajet> parcourir();

    List<Trajet> trajetsDisponibles();

    List<Trajet> rechercherParVille(String motCle);

    Map<String, List<Trajet>> regrouperParType();
}
