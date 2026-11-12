public class TrajetTrain extends Trajet {
    private final String numeroTrain;

    public TrajetTrain(int numero, String villeDepart, String villeArrivee, String date, String heureDepart,
                       int capacite, int heuresAvantDepart, String numeroTrain) {
        super(numero, villeDepart, villeArrivee, date, heureDepart, capacite, heuresAvantDepart);
        if (numeroTrain == null || numeroTrain.isBlank()) {
            throw new IllegalArgumentException("le numero du train est obligatoire");
        }
        this.numeroTrain = numeroTrain;
    }

    @Override
    public int delaiAnnulationHeures() {
        return 48;
    }

    @Override
    public String getType() {
        return "Train";
    }

    public String getNumeroTrain() {
        return numeroTrain;
    }

    @Override
    public String toString() {
        return super.toString() + ", train " + numeroTrain;
    }
}
