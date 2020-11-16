package Persistence;

import Domain.Participare;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class RepositoryHibernateParticipari implements IParticipariRepository {

    private static SessionFactory sessionFactory;

    public RepositoryHibernateParticipari(){
        initialize();
    }

    static void initialize(){
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println("Exceptie"+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @Override
    public int nrParticipariProba(String proba) {
        return 0;
    }

    @Override
    public Participare findLastAdded() {
        ArrayList<Participare> participari = new ArrayList<Participare>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participari = (ArrayList<Participare>) session
                        .createQuery("from Participare p order by p.id desc", Participare.class)
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
        //close();
        return participari.get(0);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Participare entity) throws RepositoryException {
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

                Participare participare = session.createQuery("from Participare as p where p.id = :ID", Participare.class)
                        .setParameter("ID",id)
                        .setMaxResults(1)
                        .uniqueResult();
                session.delete(participare);
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
    public void update(Integer id, Participare entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.createQuery("update Participare p " +
                        "set p.categorieVarsta = :catVarsta, p.proba = :prob " +
                        "where p.id = :ID")
                        .setParameter("catVarsta",entity.getCategorieVarsta())
                        .setParameter("prob",entity.getProba())
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
    public Participare findOne(Integer id) {
        ArrayList<Participare> participari= new ArrayList<Participare>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participari = (ArrayList<Participare>) session
                        .createQuery("from Participare p " +
                                "where p.id = :ID", Participare.class)
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
        //close();
        return participari.get(0);
    }

    @Override
    public ArrayList<Participare> findAll() {
        ArrayList<Participare> participari = new ArrayList<Participare>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                participari = (ArrayList<Participare>) session
                        .createQuery("from Participare", Participare.class)
                        .list();
                tx.commit();
            }catch(RuntimeException ex){
                //System.out.println("Repo participari --- Runtime exception");
                if (tx!=null) {
                    tx.rollback();
                    //System.out.println("Repo participari --- rollback");
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return participari;
    }
}
