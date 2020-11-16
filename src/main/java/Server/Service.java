package Server;

import Domain.Organizator;
import Domain.Participant;
import Domain.Participare;
import Persistence.IOrganizatoriRepository;
import Persistence.IParticipantiRepository;
import Persistence.IParticipariRepository;
import Persistence.RepositoryException;
import Services.IObserver;
import Services.IService;
import Services.ServiceException;
import Validator.ValidationException;
import Validator.ValidatorParticipant;
import Validator.IValidator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private IParticipantiRepository repoParticipanti;
    private IParticipariRepository repoParticipari;
    private IOrganizatoriRepository repoOrganizatori;
    private IValidator validator;
    private volatile boolean finished;


    private Map<String, IObserver> organizatoriLogati;

    private final int defaultThreadsNo=5;

    public Service(IParticipantiRepository repoParticipanti, IParticipariRepository repoParticipari, IOrganizatoriRepository repoOrganizatori){
        this.repoParticipanti = repoParticipanti;
        this.repoParticipari = repoParticipari;
        this.repoOrganizatori = repoOrganizatori;
        this.organizatoriLogati = new ConcurrentHashMap<>();
        finished = false;
    }

    public void setValidator(IValidator<Participant> validator){
        this.validator = validator;
    }

    @Override
    public void login(Organizator org, IObserver client) throws ServiceException {
        Organizator organizator = repoOrganizatori.findOnebyUserName(org.getUserName());
        if (organizator!=null){
            if(organizatoriLogati.get(organizator.getId().toString())!=null)
                throw new ServiceException("Sunteti deja logat!");
            else if(!organizator.getParola().equals(org.getParola()))
                throw new ServiceException("Parola incorecta!");
            organizatoriLogati.put(organizator.getId().toString(), client);
            System.out.println("Client logat: " + client);
        }
        else
            throw new ServiceException("User inexistent!");
    }

    @Override
    public synchronized void logout(Organizator organizator, IObserver client) throws ServiceException {
        Organizator org = repoOrganizatori.findOnebyUserName(organizator.getUserName());
        IObserver localClient = organizatoriLogati.remove(org.getId().toString());
        if (localClient==null)
            throw new ServiceException("User "+organizator.getId()+" is not logged in.");
    }

    public void adaugaParticipant(Participant participant) throws ServiceException{
        try {
            ((ValidatorParticipant) validator).validate(participant);

            repoParticipanti.save(participant);

            ArrayList<Organizator> organizatori = repoOrganizatori.findAll();

            ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

            for (Organizator organizator : organizatori) {
                IObserver client = organizatoriLogati.get(organizator.getId().toString());
                System.out.println("Notificare user logat --->"+client+" cu privire la adaugarea unui participant.");
                if (client != null)
                    executor.execute(() -> {

                            System.out.println("Notifying [" + organizator.getId() + "] participant [" + repoParticipanti.findLastAdded().getId() + "] was added.");
                        try {
                            client.participantAdaugat(repoParticipanti.findLastAdded());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    });

            }
            executor.shutdown();
        }
        catch (ValidationException | RepositoryException ve){
            throw new ServiceException(ve.getMessage());
        }
    }


    public void adaugaParticipare(Participant participant, String proba) throws ServiceException{
        int varsta = participant.getVarsta();
        String categorieVarsta = "";
        if (participant.getNrParticipari() < 2) {
            if (varsta >= 6 && varsta <= 8)
                categorieVarsta = "6-8";
            else if (varsta >= 9 && varsta <= 11)
                categorieVarsta = "9-11";
            else if (varsta >= 12 && varsta <= 15)
                categorieVarsta = "12-15";
            Participare participare = new Participare(0, participant.getId(), proba, categorieVarsta);
            try {
                repoParticipari.save(participare);
                Participant updated = new Participant(participant.getId(), participant.getNume(), participant.getVarsta(), participant.getNrParticipari() + 1);
                repoParticipanti.update(participant.getId(), updated);

                ArrayList<Organizator> organizatori = repoOrganizatori.findAll();

                ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

                for (Organizator organizator : organizatori) {
                    IObserver client = organizatoriLogati.get(organizator.getId().toString());
                    System.out.println("Notificare user logat --->" + client + " cu privire la adaugarea unei participari.");
                    if (client != null)
                        executor.execute(() -> {

                            System.out.println("Notifying [" + organizator.getId() + "] participation [" + repoParticipari.findLastAdded().getId() + "] was added and" +
                                    "participant [" + updated.getId() + " was updated: no. of participantions: " + updated.getNrParticipari());
                            try {
                                client.participareAdaugata(repoParticipari.findLastAdded(), updated, repoParticipari.nrParticipariProba("Cautare comoara"),
                                        repoParticipari.nrParticipariProba("Desen"), repoParticipari.nrParticipariProba("Poezie"));
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }


                        });

                }
                executor.shutdown();
            }catch (RepositoryException ex) {
                System.out.println("Service: "+ex.getMessage());
                throw new ServiceException(ex.getMessage());
            }
        }
        else
            throw new ServiceException("Participantul cu id-ul "+participant.getId()+" are deja 2 participari!");
    }


    public synchronized ArrayList<Participant> getParticipanti(){
        return repoParticipanti.findAll();
    }

    public synchronized ArrayList<Participare> getParticipari(){return repoParticipari.findAll();}


    public synchronized ArrayList<Participant> getParticipantiProba(String proba){
        return repoParticipanti.findParticipantiByProba(proba);
    }

    public synchronized ArrayList<Participant> getParticipantiVarsta(String categorieVarsta){
        return repoParticipanti.findParticipantiByVarsta(categorieVarsta);
    }

    public synchronized ArrayList<Participant> getParticipantiProbaVarsta(String proba, String categorieVarsta){
        return repoParticipanti.findParticipantiByProba_Varsta(proba,categorieVarsta);
    }

    public synchronized int numarParticipariProba(String proba){
        return repoParticipari.nrParticipariProba(proba);
    }



//    public Iterable<Participant> filtreazaVarsta(String categorie){
//
//    }

}
