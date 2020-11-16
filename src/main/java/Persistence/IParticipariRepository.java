package Persistence;

import Domain.Participare;

public interface IParticipariRepository extends IRepository<Integer, Participare> {

    public int nrParticipariProba(String proba);
    public Participare findLastAdded();

}
