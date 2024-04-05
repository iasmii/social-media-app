package com.example.lab7bun;
import com.example.lab7bun.domain.Utilizator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.lab7bun.service.Service;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LogInController {
    public Button logInButton;
    @FXML
    private TextField nume;

    @FXML
    private TextField prenume;

    @FXML
    private PasswordField password;

    public void displayErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    @FXML
    protected void onLogInButtonCLick(ActionEvent event) throws IOException{
        Utilizator utilizator=Service.getInstance().cautaNume(nume.getText(),prenume.getText());
        if(utilizator==null){
            displayErrorAlert("Error Occurred", "Eroare la logare", "Utilizatorul nu exista!");
        }else if(!Service.getInstance().verifyPassword(password.getText(), utilizator.getParola())){
            System.out.println(password.getText());
            System.out.println(utilizator.getParola());
            System.out.println(Service.getInstance().cripteaza(password.getText()));
            displayErrorAlert("Error Occurred", "Eroare la logare", "Parola invalida!");
        }
        else{
            FXMLLoader stageLoader = new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/example/lab7bun/Aplicatie.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane appLayout = stageLoader.load();
            Scene scene = new Scene(appLayout);
            stage.setScene(scene);

            AplicatieController appController = stageLoader.getController();
            appController.initAplicatie(utilizator);

            stage.show();
        }
    }

    @FXML
    public void onSignInClick(ActionEvent event) throws IOException {
        try{
            FXMLLoader stageLoader = new FXMLLoader();
            stageLoader.setLocation(getClass().getResource("/com/example/lab7bun/SignUp.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane singUpLayout = stageLoader.load();
            Scene scene = new Scene(singUpLayout);
            stage.setScene(scene);


            SignUpController signUpController = stageLoader.getController();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
