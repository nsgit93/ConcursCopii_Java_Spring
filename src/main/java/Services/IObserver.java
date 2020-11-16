package Services;

import Domain.Participant;
import Domain.Participare;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IObserver extends Remote {
     void participantAdaugat(Participant participant) throws RemoteException;
     void participareAdaugata(Participare participare, Participant updatedParticipant, int nrComoara, int nrDesen, int nrPoezie) throws RemoteException;

}
