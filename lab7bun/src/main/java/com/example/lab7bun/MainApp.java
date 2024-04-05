package com.example.lab7bun;

import com.example.lab7bun.domain.validators.CerereValidator;
import com.example.lab7bun.domain.validators.MesajValidator;
import com.example.lab7bun.domain.validators.PrietenieValidator;
import com.example.lab7bun.repository.*;
import com.example.lab7bun.domain.validators.UtilizatorValidator;

import com.example.lab7bun.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public static Service service;
    void setService(Service service){
        this.service=service;
    }

    //public Service getService(){
    //   return service;
    //}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "1234";

        UtilizatorDBRepository utilizatorRepo = new UtilizatorDBRepository(url, username, password, new UtilizatorValidator());
        PrietenieDBRepository prietenieRepo = new PrietenieDBRepository(url, username, password, new PrietenieValidator(), utilizatorRepo);
        MesajDBRepository msgRepo = new MesajDBRepository(url,username,password,new MesajValidator(),utilizatorRepo);
        CerereDBRepository cerereRepo=new CerereDBRepository(url,username,password,new CerereValidator(),utilizatorRepo);

        Service service = Service.getInstance();

        initView(primaryStage);
        primaryStage.show();
    }
    private void initView(Stage primaryStage) throws IOException{
        /*
        FXMLLoader stageLoader = new FXMLLoader(MainApp.class.getResource("hello-view.fxml"));
        //stageLoader.setLocation(getClass().getResource("C:/Users/USER/IdeaProjects/lab7/src/main/resources/com/example/lab7/hello-view.fxml"));

        AnchorPane layout=stageLoader.load();
        primaryStage.setScene(new Scene(layout));
        primaryStage.setTitle("App");
        */
        FXMLLoader stageLoader = new FXMLLoader(MainApp.class.getResource("LogIn.fxml"));
        stageLoader.setLocation(getClass().getResource("/com/example/lab7bun/LogIn.fxml"));
        AnchorPane LogInLayout = stageLoader.load();
        primaryStage.setScene(new Scene(LogInLayout));
        primaryStage.setTitle("App");

        LogInController logInController = stageLoader.getController();
    }
}