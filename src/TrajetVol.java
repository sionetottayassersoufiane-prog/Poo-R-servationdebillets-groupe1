public class TrajetVol extends Trajet {
    private final String numeroVol;

    public TrajetVol(int numero, String villeDepart, String villeArrivee, String date, String heureDepart,
                     int capacite, int heuresAvantDepart, String numeroVol) {
        super(numero, villeDepart, villeArrivee, date, heureDepart, capacite, heuresAvantDepart);
        if (numeroVol == null || numeroVol.isBlank()) {
            throw new IllegalArgumentException("le numero du vol est obligatoire");
        }
        this.numeroVol = numeroVol;
    }

    @Override
    public int delaiAnnulationHeures() {
        return 72;
    }

    @Override
    public String getType() {
        return "Vol";
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    @Override
    public String toString() {
        return super.toString() + ", vol " + numeroVol;
    }
}
