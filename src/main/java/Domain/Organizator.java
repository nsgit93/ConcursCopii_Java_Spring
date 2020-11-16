package Domain;

import java.io.Serializable;

public class Organizator implements Identifiable<Integer>, Comparable<Organizator>, Serializable {
    private Integer id;
    private String nume;
    private String userName;
    private String parola;

    public Organizator(String userName, String parola){this(null,null,userName,parola);}

    public Organizator(Integer id, String nume, String userName, String parola) {
        this.id = id;
        this.nume = nume;
        this.userName = userName;
        this.parola = parola;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public int compareTo(Organizator organizator) {
        return userName.compareTo(organizator.userName);
    }

    @Override
    public String toString() {
        return "Organizator{" +
                "username='" + userName + '\'' +
                ", name='" + nume + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }

}
