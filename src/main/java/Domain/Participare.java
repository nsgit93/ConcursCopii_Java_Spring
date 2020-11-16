package Domain;

import java.io.Serializable;

public class Participare implements Identifiable<Integer>, Comparable<Participare>, Serializable {
    private int id;
    private int idParticipant;
    private String proba;
    private String categorieVarsta;

    public Participare(int id, int idParticipant, String proba, String categorieVarsta) {
        this.id = id;
        this.idParticipant = idParticipant;
        this.proba = proba;
        this.categorieVarsta = categorieVarsta;
    }

    public Participare() {
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }

    public String getProba() {
        return proba;
    }

    public void setProba(String proba) {
        this.proba = proba;
    }

    public String getCategorieVarsta() {
        return categorieVarsta;
    }

    public void setCategorieVarsta(String categorieVarsta) {
        this.categorieVarsta = categorieVarsta;
    }

    @Override
    public int compareTo(Participare participare) {
        return categorieVarsta.compareTo(participare.categorieVarsta);
    }

    @Override
    public String toString() {
        return "Participare{" +
                "idParticipant='" + idParticipant + '\'' +
                "proba='" + proba + '\'' +
                "categorieVarsta='" + categorieVarsta + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }


}
