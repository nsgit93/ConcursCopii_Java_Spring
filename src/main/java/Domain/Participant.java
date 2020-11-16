package Domain;

import java.io.Serializable;

public class Participant implements Identifiable<Integer>, Comparable<Participant>, Serializable {
    private int id;
    private int varsta;
    private int nrParticipari;
    private String nume;


    public Participant(int id, String nume, int varsta, int nrParticipari){
        this.id = id;
        this.nume=nume;
        this.varsta=varsta;
        this.nrParticipari = nrParticipari;
    }

    public Participant() {
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public int getNrParticipari() {
        return nrParticipari;
    }

    public void setNrParticipari(int nrParticipari) {
        this.nrParticipari = nrParticipari;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }


    @Override
    public int compareTo(Participant participant) {
        return nume.compareTo(participant.nume);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + nume + '\'' +
                "varsta='" + varsta + '\'' +
                "nrParticipari='" + nrParticipari + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }



}
