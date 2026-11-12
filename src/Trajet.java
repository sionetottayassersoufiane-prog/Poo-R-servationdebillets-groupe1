import java.util.Objects;

public abstract class Trajet implements Comparable<Trajet> {
    private final int numero;
    private final Itineraire itineraire;
    private final Horaire horaire;
    private final int capacite;
    private int placesDisponibles;

    public Trajet(int numero, String villeDepart, String villeArrivee, String date, String heureDepart,
                  int capacite, int heuresAvantDepart) {
        if (numero <= 0) {
            throw new IllegalArgumentException("le numero du trajet doit etre strictement positif");
        }
        if (capacite <= 0) {
            throw new IllegalArgumentException("la capacite doit etre strictement positive");
        }
        this.numero = numero;
        this.itineraire = new Itineraire(villeDepart, villeArrivee);
        this.horaire = new Horaire(date, heureDepart, heuresAvantDepart);
        this.capacite = capacite;
        this.placesDisponibles = capacite;
    }

    public abstract int delaiAnnulationHeures();

    public abstract String getType();

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
        return horaire.getHeuresAvantDepart() > delaiAnnulationHeures();
    }

    public int getNumero() {
        return numero;
    }

    public String getVilleDepart() {
        return itineraire.getVilleDepart();
    }

    public String getVilleArrivee() {
        return itineraire.getVilleArrivee();
    }

    public String getDate() {
        return horaire.getDate();
    }

    public String getHeureDepart() {
        return horaire.getHeureDepart();
    }

    public int getHeuresAvantDepart() {
        return horaire.getHeuresAvantDepart();
    }

    public int getCapacite() {
        return capacite;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trajet)) {
            return false;
        }
        Trajet autre = (Trajet) o;
        return numero == autre.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public int compareTo(Trajet autre) {
        int resultat = getDate().compareTo(autre.getDate());
        if (resultat != 0) {
            return resultat;
        }
        resultat = getHeureDepart().compareTo(autre.getHeureDepart());
        if (resultat != 0) {
            return resultat;
        }
        return Integer.compare(numero, autre.numero);
    }

    @Override
    public String toString() {
        return getType() + " " + numero + " : " + itineraire + ", " + horaire + ", "
                + placesDisponibles + " place(s) libre(s) sur " + capacite
                + ", depart dans " + horaire.getHeuresAvantDepart() + "h";
    }
}
