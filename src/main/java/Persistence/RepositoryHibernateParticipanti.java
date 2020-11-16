package Persistence;

import Domain.Participant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class RepositoryHibernateParticipanti implements IParticipantiRepository {

    private static SessionFactory sessionFactory;

    public RepositoryHibernateParticipanti(){
        initialize();
    }

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }


    @Override
    public ArrayList<Participant> findParticipantiByProba(String proba) {
        ArrayList<Participant> participanti= new ArrayList<Participant>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                String queryString = "select participant from Participant participant, Participare participare " +
                        "where participant.id = participare.idParticipant and participare.proba = :Proba";
                participanti = (ArrayList<Participant>) session
                        .createQuery(queryString, Participant.class)
                        .setParameter("Proba",proba)
                        .list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("close");
        return participanti;

    }

    @Override
    public ArrayList<Participant> findParticipantiByVarsta(String categorieVarsta) {
        ArrayList<Participant> participanti= new ArrayList<Participant>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                String queryString = "select participant from Participant participant, Participare participare " +
                        "where participant.id = participare.idParticipant and participare.categorieVarsta = :Varsta";
                participanti = (ArrayList<Participant>) session
                        .createQuery(queryString, Participant.class)
                        .setParameter("Varsta",categorieVarsta)
                        .list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("close");
        return participanti;
    }

    @Override
    public ArrayList<Participant> findParticipantiByProba_Varsta(String proba, String categorieVarsta) {
        ArrayList<Participant> participanti= new ArrayList<Participant>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                String queryString = "select participant from Participant participant, Participare participare " +
                        "where participant.id = participare.idParticipant " +
                        "and participare.proba = :Proba and participare.categorieVarsta = :Varsta";
                participanti = (ArrayList<Participant>) session
                        .createQuery(queryString, Participant.class)
                        .setParameter("Proba",proba)
                        .setParameter("Varsta", categorieVarsta)
                        .list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("close");
        return participanti;
    }

    @Override
    public Participant findLastAdded() {
        ArrayList<Participant> participant= new ArrayList<Participant>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participant = (ArrayList<Participant>) session
                        .createQuery("from Participant p order by p.id desc", Participant.class)
                        .setMaxResults(1)
                        .list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return participant.get(0);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Participant entity) throws RepositoryException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Participant participant = session.createQuery("from Participant as p where p.id = :ID", Participant.class)
                        .setParameter("ID",id)
                        .setMaxResults(1)
                        .uniqueResult();
                session.delete(participant);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Integer id, Participant entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.createQuery("update Participant p set p.nrParticipari = :nrPart where p.id = :ID")
                        .setParameter("nrPart",entity.getNrParticipari())
                        .setParameter("ID",id)
                        .executeUpdate();
                tx.commit();

            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public Participant findOne(Integer id) {
        ArrayList<Participant> participant= new ArrayList<Participant>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participant = (ArrayList<Participant>) session
                        .createQuery("from Participant p where p.id = :ID", Participant.class)
                        .setParameter("ID", id)
                        .list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return participant.get(0);
    }

    @Override
    public ArrayList<Participant> findAll() {
        ArrayList<Participant> participanti= new ArrayList<Participant>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participanti = (ArrayList<Participant>) session
                        .createQuery("from Participant", Participant.class).list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return participanti;
    }
}
