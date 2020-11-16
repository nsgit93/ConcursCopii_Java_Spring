package Persistence;

import Domain.Participant;
import Persistence.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;



public class RepositoryJdbcParticipanti implements IParticipantiRepository {
    private static final Logger logger= LogManager.getLogger();
    private JdbcUtils dbUtils;
    private String dbURL;


    public RepositoryJdbcParticipanti(Properties props){
        logger.info("Initializing RepositoryJdbcParticipanti with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public int size() {
        logger.traceEntry();
        //Connection con=dbUtils.getConnection();
        try {
//            Connection con = DriverManager.getConnection("jdbc:sqlite:D:\\Facultate Informatica\\Anul II\\Semestrul II\\Medii de proiectare si programare\\Laborator\\BazaDate\\ConcursCopii");
            Connection con = dbUtils.getConnection();
            try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Participanti")) {
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
    public void save(Participant entity) {
        logger.traceEntry("saving participant {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Participanti(Nume,Varsta) values (?,?)")){
            preStmt.setString(1,entity.getNume());
            preStmt.setInt(2,entity.getVarsta());
//            preStmt.setInt(3,entity.getNrParticipari());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting paticipant with id {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Participanti where ID=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Participant entity) {
        logger.traceEntry("updating paticipant with id {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Participanti set NumarParticipari=? where ID=?")){
            preStmt.setInt(1,entity.getNrParticipari());
            preStmt.setInt(2,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public Participant findOne(Integer integer) {
        logger.traceEntry("finding participant with id {} ",integer);
        //Connection con=dbUtils.getConnection();

        try {

//            Connection con = DriverManager.getConnection("jdbc:sqlite:D:\\Facultate Informatica\\Anul II\\Semestrul II\\Medii de proiectare si programare\\Laborator\\BazaDate\\ConcursCopii");
            Connection con = dbUtils.getConnection();

            try (PreparedStatement preStmt = con.prepareStatement("select * from Participanti where ID=?")) {
                preStmt.setInt(1, integer);
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        Integer id = result.getInt("ID");
                        String nume = result.getString("Nume");
                        int varsta = result.getInt("Varsta");
                        int nrParticipari = result.getInt("NumarParticipari");
                        Participant participant = new Participant(id, nume, varsta, nrParticipari);
                        logger.traceExit(participant);
                        return participant;
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        logger.traceExit("No participant found with id {}", integer);

        return null;
    }

    @Override
    public ArrayList<Participant> findAll() {
//        logger.();
        Connection con=dbUtils.getConnection();
        ArrayList<Participant> participanti=new ArrayList<Participant>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Participanti")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ID");
                    String nume = result.getString("Nume");
                    int varsta = result.getInt("Varsta");
                    int nrParticipari = result.getInt("NumarParticipari");
                    Participant participant = new Participant(id, nume, varsta, nrParticipari);
                    participanti.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(participanti);
        return participanti;
    }

    public ArrayList<Participant> findParticipantiByProba(String proba){
        Connection con=dbUtils.getConnection();
        ArrayList<Participant> participanti=new ArrayList<Participant>();
        try(PreparedStatement preStmt=con.prepareStatement
                ("select p.ID,p.Nume,p.Varsta,p.NumarParticipari from Participanti p \n" +
                "inner join Participari on\n" +
                "p.ID = Participari.IDparticipant\n" +
                "where Participari.Proba = ?" +
                        "group by p.ID"))
        {
            preStmt.setString(1,proba);
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ID");
                    String nume = result.getString("Nume");
                    int varsta = result.getInt("Varsta");
                    int nrParticipari = result.getInt("NumarParticipari");
                    Participant participant = new Participant(id, nume, varsta, nrParticipari);
                    participanti.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(participanti);
        return participanti;
    }

    public ArrayList<Participant> findParticipantiByVarsta(String categorieVarsta){
        Connection con=dbUtils.getConnection();
        ArrayList<Participant> participanti=new ArrayList<Participant>();
        try(PreparedStatement preStmt=con.prepareStatement
                ("select p.ID,p.Nume,p.Varsta,p.NumarParticipari from Participanti p \n" +
                        "inner join Participari on\n" +
                        "p.ID = Participari.IDparticipant\n" +
                        "where Participari.CategorieVarsta = ?" +
                        "group by p.ID"))
        {
            preStmt.setString(1,categorieVarsta);
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ID");
                    String nume = result.getString("Nume");
                    int varsta = result.getInt("Varsta");
                    int nrParticipari = result.getInt("NumarParticipari");
                    Participant participant = new Participant(id, nume, varsta, nrParticipari);
                    participanti.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(participanti);
        return participanti;
    }

    public ArrayList<Participant> findParticipantiByProba_Varsta(String proba, String categorieVarsta){
        Connection con=dbUtils.getConnection();
        ArrayList<Participant> participanti=new ArrayList<Participant>();
        try(PreparedStatement preStmt=con.prepareStatement
                ("select p.ID,p.Nume,p.Varsta,p.NumarParticipari from Participanti p \n" +
                        "inner join Participari on\n" +
                        "p.ID = Participari.IDparticipant\n" +
                        "where Participari.CategorieVarsta = ? and Participari.Proba=?" +
                        "group by p.ID"))
        {
            preStmt.setString(1,categorieVarsta);
            preStmt.setString(2,proba);
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("ID");
                    String nume = result.getString("Nume");
                    int varsta = result.getInt("Varsta");
                    int nrParticipari = result.getInt("NumarParticipari");
                    Participant participant = new Participant(id, nume, varsta, nrParticipari);
                    participanti.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(participanti);
        return participanti;
    }

    @Override
    public Participant findLastAdded() {
        logger.traceEntry("finding last added participant ");
        //Connection con=dbUtils.getConnection();

        try {

//            Connection con = DriverManager.getConnection("jdbc:sqlite:D:\\Facultate Informatica\\Anul II\\Semestrul II\\Medii de proiectare si programare\\Laborator\\BazaDate\\ConcursCopii");
            Connection con = dbUtils.getConnection();

            try (PreparedStatement preStmt = con.prepareStatement("select * from Participanti\n" +
                    "order by ID desc\n" +
                    "limit 1")) {
                try (ResultSet result = preStmt.executeQuery()) {
                    if (result.next()) {
                        Integer id = result.getInt("ID");
                        String nume = result.getString("Nume");
                        int varsta = result.getInt("Varsta");
                        int nrParticipari = result.getInt("NumarParticipari");
                        Participant participant = new Participant(id, nume, varsta, nrParticipari);
                        logger.traceExit(participant);
                        return participant;
                    }
                }
            } catch (SQLException ex) {
                logger.error(ex);
                System.out.println("Error DB " + ex);
            }
        }catch (Exception ignored){

        }
        logger.traceExit("No participant found");

        return null;
    }

}

