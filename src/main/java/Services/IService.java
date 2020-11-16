package Services;

import Domain.Organizator;
import Domain.Participant;
import Domain.Participare;

import java.util.ArrayList;

public interface IService {
    //public void permiteAutentificare(String userName, String parola, IObserver client) throws ServiceException;

    public ArrayList<Participant> getParticipanti();

    public ArrayList<Participare> getParticipari();

    public void adaugaParticipare(Participant participare, String proba) throws ServiceException;

    public void adaugaParticipant(Participant participant) throws ServiceException;

    public ArrayList<Participant> getParticipantiProba(String proba);

    public ArrayList<Participant> getParticipantiVarsta(String categorieVarsta);

    public ArrayList<Participant> getParticipantiProbaVarsta(String proba, String categorieVarsta);

    public int numarParticipariProba(String proba);

    public void logout(Organizator organizator, IObserver client) throws ServiceException;

    public void login(Organizator org, IObserver client) throws ServiceException;
}
