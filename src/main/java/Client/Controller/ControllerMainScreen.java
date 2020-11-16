package Client.Controller;

import Domain.Organizator;
import Domain.Participant;
import Domain.Participare;
import Services.IObserver;
import Services.IService;
import Services.ServiceException;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ControllerMainScreen extends UnicastRemoteObject implements Serializable, ControlledScreen, Initializable, IObserver {

    private ScreenSwitcher screenSwitcher;
    private IService serv;
    private Organizator organizator;


    @FXML
    private TableView<Participant> tableParticipanti;

    @FXML
    private TableView<Participare> tableParticipari;

    @FXML
    private TextField textNume, textVarsta, textNrPart;

    @FXML
    private ComboBox<String> probaBox, probaFilterBox,varstaFilterBox,probaNrBox;

    @FXML
    private CheckBox checkProbaFiltrare,checkVarstaFiltrare;

    @FXML
    private Button btnAdaugaProba, btnAdaugaParticipant;

    public ControllerMainScreen() throws RemoteException {
    }


    public Organizator getOrganizator() {
        return organizator;
    }

    public void setOrganizator(Organizator organizator) {
        this.organizator = organizator;
    }

    public void setService(IService service) {
        this.serv = service;
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb){
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                logout(new ActionEvent());
            }
        });
        probaFilterBox.setDisable(true);
        varstaFilterBox.setDisable(true);
        textNrPart.setEditable(false);
        btnAdaugaProba.setDisable(true);
        btnAdaugaParticipant.setDisable(true);
        tableParticipanti.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        tableParticipari.setSelectionModel(null);
        tableParticipanti.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV)->setTextFields(newV));
        textNume.textProperty().addListener((obs, oldv, newv)->{
            if(!textNume.getText().equals("") &&
                    !textVarsta.getText().equals("") &&
                    !newv.equals("")) {
                btnAdaugaParticipant.setDisable(false);
                if (probaBox.getSelectionModel().getSelectedItem() != null)
                        btnAdaugaProba.setDisable(false);
            }
            else
                btnAdaugaParticipant.setDisable(true);
        });
        textVarsta.textProperty().addListener((obs, oldv, newv)->{
            if(!textNume.getText().equals("") &&
                    !textVarsta.getText().equals("") &&
                    !newv.equals("")) {
                btnAdaugaParticipant.setDisable(false);
                if (probaBox.getSelectionModel().getSelectedItem() != null)
                    btnAdaugaProba.setDisable(false);
            }
            else
                btnAdaugaParticipant.setDisable(true);
        });

        checkProbaFiltrare.setOpacity(1);
        checkVarstaFiltrare.setOpacity(1);
        checkProbaFiltrare.setOnAction(ev->{
            if(!checkProbaFiltrare.isSelected()){
                probaFilterBox.setDisable(true);
                probaFilterBox.getSelectionModel().clearSelection();
            }
            else {
                probaFilterBox.setDisable(false);
            }
        });
        checkVarstaFiltrare.setOnAction(ev->{
            if(!checkVarstaFiltrare.isSelected()){
                varstaFilterBox.setDisable(true);
                varstaFilterBox.getSelectionModel().clearSelection();
            }
            else {
                varstaFilterBox.setDisable(false);
            }
        });
        probaBox.setOnAction(ev->{
            String proba = probaBox.getSelectionModel().getSelectedItem();
            if (proba != null)
                btnAdaugaProba.setDisable(false);
        });
        probaFilterBox.setOnAction(ev->{
            String proba = probaFilterBox.getSelectionModel().getSelectedItem();
            if (proba != null) {
                disableAdding();
                if (checkVarstaFiltrare.isSelected()) {
                    String categorieVarsta = varstaFilterBox.getSelectionModel().getSelectedItem();
                    if (categorieVarsta != null) {
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipantiProbaVarsta(proba, categorieVarsta));
                    }
                    else {
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipantiProba(proba));
                    }
                } else {
                    tableParticipanti.getItems().clear();
                    loadParticipantiTableData(serv.getParticipantiProba(proba));
                }
            }
            else {
                if (checkVarstaFiltrare.isSelected()) {
                    String categorieVarsta = varstaFilterBox.getSelectionModel().getSelectedItem();
                    if (categorieVarsta != null) {
                        disableAdding();
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipantiVarsta(categorieVarsta));
                    }
                    else {
                        enableAdding();
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipanti());
                    }
                }
                else {
                    enableAdding();
                    tableParticipanti.getItems().clear();
                    loadParticipantiTableData(serv.getParticipanti());
                }
            }
        });
        varstaFilterBox.setOnAction(ev->{
            String categorieVarsta = varstaFilterBox.getSelectionModel().getSelectedItem();
            System.out.println(categorieVarsta);
            if (categorieVarsta != null) {
                disableAdding();
                if (checkProbaFiltrare.isSelected()) {
                    String proba = probaFilterBox.getSelectionModel().getSelectedItem();
                    if (proba != null) {
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipantiProbaVarsta(proba, categorieVarsta));
                    }
                    else {
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipantiVarsta(categorieVarsta));
                    }
                } else {
                    tableParticipanti.getItems().clear();
                    loadParticipantiTableData(serv.getParticipantiVarsta(categorieVarsta));
                }
            }
            else {
                if (checkProbaFiltrare.isSelected()) {
                    String proba = probaFilterBox.getSelectionModel().getSelectedItem();

                    if (proba != null) {
                        disableAdding();
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipantiProba(proba));

                    }
                    else {
                        enableAdding();
                        tableParticipanti.getItems().clear();
                        loadParticipantiTableData(serv.getParticipanti());
                    }
                }
                else {
                    enableAdding();
                    tableParticipanti.getItems().clear();
                    loadParticipantiTableData(serv.getParticipanti());
                }
            }
        });
        probaNrBox.setOnAction(ev->{
            String proba = probaNrBox.getSelectionModel().getSelectedItem();
            if (proba!=null){
                int numar = serv.numarParticipariProba(proba);
                textNrPart.setText(String.valueOf(numar));
            }
            else {
                textNrPart.setText("");
            }
        });
    }


    public void loadTableData() {
        loadParticipantiTableData(serv.getParticipanti());
        loadParticipariTableData(serv.getParticipari());
    }

    private void loadParticipantiTableData(Iterable<Participant> listaParticipanti){
        tableParticipanti.getItems().clear();
        ((List<Participant>)listaParticipanti).forEach(p -> tableParticipanti.getItems().add(p));
    }

    private void loadParticipariTableData(Iterable<Participare> listaParticipari){
        tableParticipari.getItems().clear();
        ((List<Participare>)listaParticipari).forEach(p -> tableParticipari.getItems().add(p));
    }

    @Override
    public void setScreenParent(ScreenSwitcher screenPage) {
        screenSwitcher = screenPage;
    }

    private void setTextFields(Participant participant){
        if (participant==null)
            clearFields();
        else {
            textNume.setText(participant.getNume());
            textVarsta.setText(String.valueOf(participant.getVarsta()));
        }
    }

    private void clearFields(){
        textNume.setText("");
        textVarsta.setText("");
    }

    public void logout(ActionEvent actionEvent) {
        try {
            serv.logout(organizator,null);
            screenSwitcher.loadScreen(ScreenFramework.screenLogin,ScreenFramework.screenMainFile,serv,null);
            screenSwitcher.setScreen(ScreenFramework.screenLogin);
            screenSwitcher.unloadScreen(ScreenFramework.screenMain);
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());;
        }
    }

    public void addParticipare(ActionEvent actionEvent)  throws ServiceException{
        Participant participant = tableParticipanti.getSelectionModel().getSelectedItem();
        String proba = probaBox.getSelectionModel().getSelectedItem();
        try{
            serv.adaugaParticipare(participant,proba);
            Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION);
            mesaj.setContentText("Participare adaugata cu succes!");
            mesaj.showAndWait();

        }
        catch (ServiceException se){
            Alert mesaj = new Alert(Alert.AlertType.WARNING);
            mesaj.setContentText(se.getMessage());
            mesaj.showAndWait();
        }
        probaBox.getSelectionModel().clearSelection();
        tableParticipanti.getSelectionModel().clearSelection();
        btnAdaugaProba.setDisable(true);
        tableParticipanti.getSelectionModel().clearSelection();
        btnAdaugaProba.setDisable(true);
        clearFields();
    }


    public void addParticipant(ActionEvent actionEvent) throws ServiceException{
        String nume = textNume.getText();
        int varsta = Integer.parseInt(textVarsta.getText());
        Participant participant = new Participant(1,nume,varsta,0);
        try {
            serv.adaugaParticipant(participant);
            Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION);
            mesaj.setContentText("Participant adaugat cu succes!");
            mesaj.showAndWait();

//            loadParticipantiTableData(serv.getParticipanti());
        } catch (ValidationException | ServiceException ve){
            Alert mesaj = new Alert(Alert.AlertType.ERROR);
            mesaj.setContentText(ve.getMessage());
            mesaj.showAndWait();
        }
        tableParticipanti.getSelectionModel().clearSelection();
        btnAdaugaParticipant.setDisable(true);
        btnAdaugaProba.setDisable(true);
        textNrPart.setText("");
        clearFields();
//        probaBox.setDisable(true);
        probaBox.getSelectionModel().clearSelection();
    }

    @Override
    public void participantAdaugat(Participant participant) {
        tableParticipanti.getItems().add(participant);
    }

    @Override
    public void participareAdaugata(Participare participare, Participant updatat, int nrComoara, int nrDesen, int nrPoezie){
        tableParticipari.getItems().add(participare);
        System.out.println("ControllerMain: Participant updated --->"+updatat);
        for (int i=0; i<tableParticipanti.getItems().size();i++)
            if (tableParticipanti.getItems().get(i).getId().equals(updatat.getId())){
                tableParticipanti.getItems().remove(i);
                tableParticipanti.getItems().add(i,updatat);
                break;
            }
        String probaNr = probaNrBox.getSelectionModel().getSelectedItem();
        if(probaNr!=null && probaNr.equals(participare.getProba())) {
            System.out.println("Proba selectata ---->"+probaNr);
            switch (probaNr) {
                case "Cautare comoara":
                    updateNrParticipariField(nrComoara);
                    break;
                case "Poezie":
                    updateNrParticipariField(nrPoezie);
                    break;
                case "Desen":
                    updateNrParticipariField(nrDesen);
                    break;
            }
        }
    }

    private void updateNrParticipariField(int numar){
        try{
            textNrPart.setText(String.valueOf(numar));
            textNrPart.setStyle("-fx-text-fill: red;-fx-border-color: red");
            TimeUnit.MILLISECONDS.sleep(500);
            textNrPart.setStyle("-fx-text-fill: black;-fx-border-color: transparent");
            TimeUnit.MILLISECONDS.sleep(500);
            textNrPart.setStyle("-fx-text-fill: red;-fx-border-color: red");
            TimeUnit.MILLISECONDS.sleep(500);
            textNrPart.setStyle("-fx-text-fill: black;-fx-border-color: transparent");
            TimeUnit.MILLISECONDS.sleep(500);
            textNrPart.setStyle("-fx-text-fill: red;-fx-border-color: red");
            TimeUnit.MILLISECONDS.sleep(500);
            textNrPart.setStyle("-fx-text-fill: black;-fx-border-color: transparent");
        }catch (InterruptedException ignored){}
    }

    private void disableAdding(){
        textNume.setEditable(false);
        textVarsta.setEditable(false);
        probaBox.setDisable(true);
        clearFields();
    }

    private void enableAdding(){
        textNume.setEditable(true);
        textVarsta.setEditable(true);
        probaBox.setDisable(false);


    }
}


