package Persistence;

import Domain.Participare;
import Persistence.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class RepositoryJdbcParticipari implements IParticipariRepository {
    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils dbUtils;
    private String dbURL;


    public RepositoryJdbcParticipari(Properties props){
        logger.info("Initializing RepositoryJdbcParticipanti with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public int size() {
        logger.traceEntry();
        try {
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Participari")) {
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        logger.traceExit(result.getInt("SIZE"));
                        return result.getInt("SIZE");
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        return 0;
    }


    @Override
    public void save(Participare entity) throws RepositoryException{
        logger.traceEntry("saving participare {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement verifyStmt = con.prepareStatement("select count(*) as [SIZE] from Participari " +
                "where Participari.Proba=? and Participari.IDparticipant=?")) {
            verifyStmt.setString(1,entity.getProba());
            verifyStmt.setInt(2,entity.getIdParticipant());
            boolean exists=false;
            try (ResultSet result = verifyStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    exists = result.getInt("SIZE")==1;
                }
            }
            System.out.println("Repo ----> exists: "+exists);
            if (exists)
                throw new RepositoryException("Participantul cu ID-ul "+entity.getIdParticipant()+
                        " este deja inscris la proba <"+entity.getProba()+">");
            try (PreparedStatement preStmt = con.prepareStatement("insert into Participari(IDparticipant,Proba,CategorieVarsta) values (?,?,?)")) {
                preStmt.setInt(1, entity.getIdParticipant());
                preStmt.setString(2, entity.getProba());
                preStmt.setString(3, entity.getCategorieVarsta());
                int result = preStmt.executeUpdate();
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting participare with id {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Participari where ID=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Participare entity) {

    }

    @Override
    public Participare findOne(Integer integer) {
        logger.traceEntry("finding participare with id {} ",integer);
        try {
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("select * from Participari where ID=?")) {
                preStmt.setInt(1, integer);
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        Integer id = result.getInt("ID");
                        Integer idParticipant = result.getInt("IDparticipant");
                        String proba = result.getString("Proba");
                        String categorie = result.getString("CategorieVarsta");
                        Participare participare = new Participare(id, idParticipant, proba, categorie);
                        logger.traceExit(participare);
                        return participare;
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        logger.traceExit("No participare found with id {}", integer);

        return null;
    }

    @Override
    public ArrayList<Participare> findAll() {
        Connection con=dbUtils.getConnection();
        ArrayList<Participare> participari=new ArrayList<Participare>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Participari")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ID");
                    Integer idParticipant = result.getInt("IDparticipant");
                    String proba = result.getString("Proba");
                    String categorie = result.getString("CategorieVarsta");
                    Participare participare = new Participare(id, idParticipant, proba, categorie);
                    participari.add(participare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(participari);
        return participari;
    }

    @Override
    public int nrParticipariProba(String proba) {
        logger.traceEntry();
        try {
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement
                    ("select count(*) as [SIZE] from Participari where Participari.Proba=?"))
            {
                preStmt.setString(1,proba);
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        logger.traceExit(result.getInt("SIZE"));
                        return result.getInt("SIZE");
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        return 0;
    }

    @Override
    public Participare findLastAdded() {
        logger.traceEntry("finding last added participare");
        try {
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("select * from Participari\n" +
                    "order by ID desc\n" +
                    "limit 1")) {
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        Integer id = result.getInt("ID");
                        Integer idParticipant = result.getInt("IDparticipant");
                        String proba = result.getString("Proba");
                        String categorie = result.getString("CategorieVarsta");
                        Participare participare = new Participare(id, idParticipant, proba, categorie);
                        logger.traceExit(participare);
                        return participare;
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        logger.traceExit("No participare found");

        return null;
    }
}
