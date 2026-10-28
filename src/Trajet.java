public class Trajet {
    private final int numero;
    private final String villeDepart;
    private final String villeArrivee;
    private final String date;
    private final String heureDepart;
    private final int capacite;
    private int placesDisponibles;
    private final int heuresAvantDepart;
    private final int delaiAnnulationHeures;

    public Trajet(int numero, String villeDepart, String villeArrivee, String date, String heureDepart,
                  int capacite, int heuresAvantDepart, int delaiAnnulationHeures) {
        if (numero <= 0) {
            throw new IllegalArgumentException("le numero du trajet doit etre strictement positif");
        }
        if (villeDepart == null || villeDepart.isBlank() || villeArrivee == null || villeArrivee.isBlank()) {
            throw new IllegalArgumentException("les villes de depart et d'arrivee sont obligatoires");
        }
        if (villeDepart.equalsIgnoreCase(villeArrivee)) {
            throw new IllegalArgumentException("la ville de depart et la ville d'arrivee doivent etre differentes");
        }
        if (date == null || date.isBlank() || heureDepart == null || heureDepart.isBlank()) {
            throw new IllegalArgumentException("la date et l'heure de depart sont obligatoires");
        }
        if (capacite <= 0) {
            throw new IllegalArgumentException("la capacite doit etre strictement positive");
        }
        if (heuresAvantDepart < 0) {
            throw new IllegalArgumentException("le delai avant le depart ne peut pas etre negatif");
        }
        if (delaiAnnulationHeures <= 0) {
            throw new IllegalArgumentException("le delai d'annulation doit etre strictement positif");
        }
        this.numero = numero;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.date = date;
        this.heureDepart = heureDepart;
        this.capacite = capacite;
        this.placesDisponibles = capacite;
        this.heuresAvantDepart = heuresAvantDepart;
        this.delaiAnnulationHeures = delaiAnnulationHeures;
    }

    public void reserverPlace() {
        if (placesDisponibles == 0) {
            throw new IllegalStateException("le trajet " + numero + " est complet");
        }
        placesDisponibles = placesDisponibles - 1;
    }

    public void libererPlace() {
        if (placesDisponibles == capacite) {
            throw new IllegalStateException("aucune place n'est occupee sur le trajet " + numero);
        }
        placesDisponibles = placesDisponibles + 1;
    }

    public boolean estComplet() {
        return placesDisponibles == 0;
    }

    public boolean annulationEncorePossible() {
        return heuresAvantDepart > delaiAnnulationHeures;
    }

    public int getNumero() {
        return numero;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public String getDate() {
        return date;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public int getHeuresAvantDepart() {
        return heuresAvantDepart;
    }

    public int getDelaiAnnulationHeures() {
        return delaiAnnulationHeures;
    }

    @Override
    public String toString() {
        return "Trajet " + numero + " : " + villeDepart + " -> " + villeArrivee
                + ", le " + date + " a " + heureDepart + ", "
                + placesDisponibles + " place(s) libre(s) sur " + capacite
                + ", depart dans " + heuresAvantDepart + "h";
    }
}
