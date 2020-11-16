package Client;

import Client.Controller.*;
import Services.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;



public class StartClient extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";


    public void start(Stage stage) throws Exception {
        System.out.println("In start");
        ApplicationContext factory = new ClassPathXmlApplicationContext("CompetitionSpringClient.xml");
        IService server=(IService) factory.getBean("service");
        System.out.println("Obtained a reference to remote server");

        ScreenSwitcher screenSwitcher = new ScreenSwitcher();
        screenSwitcher.loadScreen(ScreenFramework.screenLogin, ScreenFramework.screenLoginFile,server,null);

        screenSwitcher.getStylesheets().addAll("style.css");

        screenSwitcher.setStage(stage);
        screenSwitcher.setScreen(ScreenFramework.screenLogin);


        Group root = new Group();
        root.getChildren().addAll(screenSwitcher);
        Scene scene = new Scene(root,700,600);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}


