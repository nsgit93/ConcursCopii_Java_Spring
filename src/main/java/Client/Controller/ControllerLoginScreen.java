package Client.Controller;

import Domain.Organizator;
import Services.IService;
import Services.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import org.graalvm.compiler.hotspot.meta.DefaultHotSpotLoweringProvider;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ControllerLoginScreen extends UnicastRemoteObject implements Serializable, ControlledScreen{
    private ScreenSwitcher screenSwitcher;
    private IService serv;

    @FXML
    private TextField textUser;
    @FXML
    private PasswordField textParola;
    @FXML
    private Label lblEroareLogare;

    public ControllerLoginScreen() throws RemoteException {
    }


    @Override
    public void setScreenParent(ScreenSwitcher screenPage) {
        screenSwitcher = screenPage;
    }

    public void setService(IService service) {
        this.serv = service;
    }

    @FXML
    private void initialize(){
    }

    public void logare(ActionEvent actionEvent) {
        String userName = textUser.getText();
        String parola = textParola.getText();
        Organizator org = new Organizator(userName,parola);
        try{
            screenSwitcher.loadScreen(ScreenFramework.screenMain,ScreenFramework.screenMainFile,serv,org);
            serv.login(org,screenSwitcher.getMainScreenController());
            screenSwitcher.setScreen(ScreenFramework.screenMain);
            screenSwitcher.getStage().setTitle("Main Window for user: "+userName);
            textParola.setText("");
            textUser.setText("");
        }
        catch (ServiceException ex){
            screenSwitcher.unloadScreen(ScreenFramework.screenMain);
            lblEroareLogare.setText(ex.getMessage());
            lblEroareLogare.setTextFill(Paint.valueOf("red"));
            textUser.setText("");
            textParola.setText("");
        }
    }

}
