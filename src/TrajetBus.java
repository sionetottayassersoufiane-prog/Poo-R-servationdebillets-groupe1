public class TrajetBus extends Trajet {
    private final String compagnie;

    public TrajetBus(int numero, String villeDepart, String villeArrivee, String date, String heureDepart,
                     int capacite, int heuresAvantDepart, String compagnie) {
        super(numero, villeDepart, villeArrivee, date, heureDepart, capacite, heuresAvantDepart);
        if (compagnie == null || compagnie.isBlank()) {
            throw new IllegalArgumentException("la compagnie du bus est obligatoire");
        }
        this.compagnie = compagnie;
    }

    @Override
    public int delaiAnnulationHeures() {
        return 24;
    }

    @Override
    public String getType() {
        return "Bus";
    }

    public String getCompagnie() {
        return compagnie;
    }

    @Override
    public String toString() {
        return super.toString() + ", compagnie " + compagnie;
    }
}
