public class Itineraire {
    private final String villeDepart;
    private final String villeArrivee;

    public Itineraire(String villeDepart, String villeArrivee) {
        if (villeDepart == null || villeDepart.isBlank() || villeArrivee == null || villeArrivee.isBlank()) {
            throw new IllegalArgumentException("les villes de depart et d'arrivee sont obligatoires");
        }
        if (villeDepart.equalsIgnoreCase(villeArrivee)) {
            throw new IllegalArgumentException("la ville de depart et la ville d'arrivee doivent etre differentes");
        }
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    @Override
    public String toString() {
        return villeDepart + " -> " + villeArrivee;
    }
}
