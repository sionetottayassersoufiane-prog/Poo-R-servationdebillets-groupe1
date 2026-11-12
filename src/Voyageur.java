import java.util.Objects;

public class Voyageur {
    private final int numero;
    private final String nom;
    private final String email;

    public Voyageur(int numero, String nom, String email) {
        if (numero <= 0) {
            throw new IllegalArgumentException("le numero du voyageur doit etre strictement positif");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("le nom du voyageur est vide");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("l'adresse e-mail du voyageur est invalide");
        }
        this.numero = numero;
        this.nom = nom;
        this.email = email;
    }

    public int getNumero() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voyageur)) {
            return false;
        }
        Voyageur autre = (Voyageur) o;
        return numero == autre.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Voyageur " + numero + " : " + nom + " (" + email + ")";
    }
}
