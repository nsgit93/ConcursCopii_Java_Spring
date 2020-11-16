package Persistence;

import Domain.Participant;

import java.util.ArrayList;

public interface IParticipantiRepository extends IRepository<Integer, Participant> {
    public ArrayList<Participant> findParticipantiByProba(String proba);
    public ArrayList<Participant> findParticipantiByVarsta(String categorieVarsta);
    public ArrayList<Participant> findParticipantiByProba_Varsta(String proba, String categorieVarsta);
    public Participant findLastAdded();
}
