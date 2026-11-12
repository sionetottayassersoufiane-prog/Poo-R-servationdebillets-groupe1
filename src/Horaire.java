public class Horaire {
    private final String date;
    private final String heureDepart;
    private final int heuresAvantDepart;

    public Horaire(String date, String heureDepart, int heuresAvantDepart) {
        if (date == null || date.isBlank() || heureDepart == null || heureDepart.isBlank()) {
            throw new IllegalArgumentException("la date et l'heure de depart sont obligatoires");
        }
        if (heuresAvantDepart < 0) {
            throw new IllegalArgumentException("le delai avant le depart ne peut pas etre negatif");
        }
        this.date = date;
        this.heureDepart = heureDepart;
        this.heuresAvantDepart = heuresAvantDepart;
    }

    public String getDate() {
        return date;
    }

    public String getHeureDepart() {
        return heureDepart;
    }

    public int getHeuresAvantDepart() {
        return heuresAvantDepart;
    }

    @Override
    public String toString() {
        return "le " + date + " a " + heureDepart;
    }
}
