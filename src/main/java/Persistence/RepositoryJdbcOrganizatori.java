package Persistence;

import Domain.Organizator;
import Persistence.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class RepositoryJdbcOrganizatori implements IOrganizatoriRepository {
    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils dbUtils;
    private String dbURL;


    public RepositoryJdbcOrganizatori(Properties props){
        logger.info("Initializing RepositoryJdbcOrganizatori with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public int size() {
        logger.traceEntry();
        try {
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Organizatori")) {
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
    public void save(Organizator entity) {
        logger.traceEntry("saving organizator {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Organizatori values (?,?,?)")){
            preStmt.setString(1,entity.getNume());
            preStmt.setString(2,entity.getUserName());
            preStmt.setString(3,entity.getParola());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting organizator with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Organizatori where ID=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Organizator entity) {

    }

    @Override
    public Organizator findOne(Integer integer) {
        logger.traceEntry("finding organizator with id {} ",integer);
        try {
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("select * from Organizatori where ID=?")) {
                preStmt.setInt(1, integer);
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        Integer id = result.getInt("ID");
                        String nume = result.getString("Nume");
                        String userName = result.getString("User");
                        String parola = result.getString("Parola");
                        Organizator organizator = new Organizator(id, nume, userName, parola);
                        logger.traceExit(organizator);
                        return organizator;
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        logger.traceExit("No organizator found with id {}", integer);
        return null;
    }

    @Override
    public ArrayList<Organizator> findAll() {
//        logger.();
        Connection con=dbUtils.getConnection();
        ArrayList<Organizator> organizatori = new ArrayList<Organizator>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Organizatori")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ID");
                    String nume = result.getString("Nume");
                    String userName = result.getString("User");
                    String parola = result.getString("Parola");
                    Organizator organizator = new Organizator(id, nume, userName, parola);
                    organizatori.add(organizator);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(organizatori);
        return organizatori;
    }


    @Override
    public Organizator findOnebyUserName(String user) {
        logger.traceEntry("finding organizator by username "+user);
        try {
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("select * from Organizatori where User=?")) {
                preStmt.setString(1, user);
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        Integer id = result.getInt("ID");
                        String nume = result.getString("Nume");
                        String userName = result.getString("User");
                        String parola = result.getString("Parola");
                        Organizator organizator = new Organizator(id, nume, userName, parola);
                        logger.traceExit(organizator);
                        return organizator;
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        logger.traceExit("No organizator found with username "+ user);
        return null;
    }
}
