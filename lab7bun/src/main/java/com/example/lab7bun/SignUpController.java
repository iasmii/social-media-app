package com.example.lab7bun;
import com.example.lab7bun.domain.Utilizator;
import com.example.lab7bun.domain.validators.UtilizatorValidator;
import com.example.lab7bun.domain.validators.ValidationException;
import com.example.lab7bun.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    public Button creeazaCont;
    public Button logInButton;
    @FXML
    private TextField nume;
    @FXML
    private TextField prenume;
    @FXML
    private TextField password;
    @FXML
    private TextField password_confirm;
    private UtilizatorValidator utilizatorValidator = new UtilizatorValidator();

    public void displayErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    @FXML
    protected void onCreeazaContClick(ActionEvent event) throws IOException {
        Utilizator utilizator = new Utilizator(nume.getText(), prenume.getText(), password.getText());
        try {
            utilizatorValidator.validate(utilizator);
        }
        catch (ValidationException e){
            displayErrorAlert("Error Occurred", "Eroare la inregistrare", e.getMessage());
        }
        if(!password.getText().equals(password_confirm.getText())){
            displayErrorAlert("Error Occurred", "Eroare la inregistrare", "Parolele nu coincid!");
        }
        else{
            try {
                Service.getInstance().adaugaUtilizator(utilizator.getNume(), utilizator.getPrenume(), utilizator.getParola());
            } catch (Exception e) {
                displayErrorAlert("Error Occurred", "Eroare la adaugare", e.getMessage());
            }
            FXMLLoader stageLoader = new FXMLLoader(MainApp.class.getResource("Aplicatie.fxml"));
            stageLoader.setLocation(getClass().getResource("/com/example/lab7bun/Aplicatie.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            AnchorPane layout=stageLoader.load();
            Scene scene = new Scene(layout);
            stage.setScene(scene);

            AplicatieController AplicatieController = stageLoader.getController();
            AplicatieController.initAplicatie(utilizator);
            stage.show();
        }
    }
    public void inapoiLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/lab7bun/LogIn.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        AnchorPane logInLayout = stageLoader.load();
        Scene scene = new Scene(logInLayout);
        stage.setScene(scene);

        LogInController logInController = stageLoader.getController();

        stage.show();
    }
}
