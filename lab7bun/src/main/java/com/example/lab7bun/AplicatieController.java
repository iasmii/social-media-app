package com.example.lab7bun;

import com.example.lab7bun.domain.Cerere;
import com.example.lab7bun.domain.Mesaj;
import com.example.lab7bun.domain.Prietenie;
import com.example.lab7bun.domain.Utilizator;
import com.example.lab7bun.domain.validators.ValidationException;
import com.example.lab7bun.service.Service;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

import static java.util.Collections.addAll;

public class AplicatieController implements Initializable{
    @FXML
    private TextField statistica;
    @FXML
    private TextField mesaj;
    @FXML
    private Text numeUtilizator;

    private Utilizator user;

    @FXML
    private ListView<Prietenie> friendshipList;

    @FXML
    private ListView<Utilizator> userList;

    @FXML
    private ListView<Mesaj> mesajeList;

    @FXML
    private ListView<Cerere> cerereListPrimit;

    @FXML
    private ListView<Cerere> cerereListTrimis;

    @FXML
    private ListView<Utilizator> statisticiList;

    @FXML
    private TextField nrinregistrariUser;

    @FXML
    private TextField paginaCurentaUser;

    private int inregistrariUser=3;

    private int paginaUser=1;
    @FXML
    private TextField nrinregistrariPrietenie;

    @FXML
    private TextField paginaCurentaPrietenie;

    private int inregistrariPrietenie=3;

    private int paginaPrietenie=1;

    private final ObservableList<Prietenie> friendsObs = FXCollections.observableArrayList();

    private final ObservableList<Utilizator> userObs = FXCollections.observableArrayList();

    private final ObservableList<Mesaj> msjObs = FXCollections.observableArrayList();

    private final ObservableList<Cerere> cerereObsPrimit = FXCollections.observableArrayList();
    private final ObservableList<Cerere> cerereObsTrimis = FXCollections.observableArrayList();

    private final ObservableList<Utilizator> statisticiObs = FXCollections.observableArrayList();

    public List<Prietenie> getPrietenie() throws Exception {
        //Iterable<Prietenie> prieteni = Service.getInstance().getAllPrietenie();
        Iterable<Prietenie> prieteni = Service.getInstance().getPrieteniePaginati(inregistrariPrietenie, paginaPrietenie, user.getId());
        List<Prietenie> listapr = StreamSupport.stream(prieteni.spliterator(), false).toList();
        return listapr;
    }

    public List<Prietenie> getPrietenieInit() throws Exception {
        //Iterable<Prietenie> prieteni = Service.getInstance().getAllPrietenie();
        Iterable<Prietenie> prieteni = Service.getInstance().getPrieteniePaginatiInit(inregistrariPrietenie, paginaPrietenie);
        //List<Prietenie> listapr = StreamSupport.stream(prieteni.spliterator(), false).toList();
        List<Prietenie> listapr = StreamSupport.stream(prieteni.spliterator(), false)
                .filter(prietenie -> Objects.equals(prietenie.getUtilizator1(), user) || Objects.equals(prietenie.getUtilizator2(), user))
                .toList();
        return listapr;
    }

    public List<Utilizator> getUtilizator() throws Exception {
        //Iterable<Utilizator> utilizatori = Service.getInstance().getAllUtilizatori();
        Iterable<Utilizator> utilizatori = Service.getInstance().getUtilizatoriPaginati(inregistrariUser, paginaUser);
        //List<Utilizator> listaut = StreamSupport.stream(utilizatori.spliterator(), false).filter(utilizator->!Objects.equals(utilizator,this.user)).toList();
        List<Utilizator> listaut = StreamSupport.stream(utilizatori.spliterator(), false).toList();
        return listaut;
    }

    public List<Mesaj> getMesaje() {
        Iterable<Mesaj> mesaje = Service.getInstance().getAllMesaje();
        List<Mesaj> listamsj = StreamSupport.stream(mesaje.spliterator(), false)
                .filter(mesaj -> Objects.equals(mesaj.getFrom(), user) || Objects.equals(mesaj.getTo(), user))
                .toList();
        return listamsj;
    }

    public List<Cerere> getCerereTrimis() {
        Iterable<Cerere> cereri = Service.getInstance().getAllCereri();
        List<Cerere> listacereri = StreamSupport.stream(cereri.spliterator(), false).filter(cerere -> Objects.equals(cerere.getUtilizator1(),this.user)).toList();
        return listacereri;
    }
    public List<Cerere> getCererePrimit() {
        Iterable<Cerere> cereri = Service.getInstance().getAllCereri();
        List<Cerere> listacereri = StreamSupport.stream(cereri.spliterator(), false).filter(cerere -> Objects.equals(cerere.getUtilizator2(),this.user)).toList();
        return listacereri;
    }

    public void initAplicatie(Utilizator user){
        this.user=user;
        numeUtilizator.setText(user.getNume()+" "+user.getPrenume());
        try{
            friendsObs.addAll(this.getPrietenie());
            System.out.println(this.getPrietenie());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        try {
            userObs.setAll(this.getUtilizator());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        cerereObsPrimit.addAll(this.getCererePrimit());
        cerereObsTrimis.addAll(this.getCerereTrimis());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            friendsObs.addAll(this.getPrietenieInit());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        try {
            userObs.addAll(this.getUtilizator());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        msjObs.addAll(this.getMesaje());
        cerereObsPrimit.addAll(this.getCererePrimit());
        cerereObsTrimis.addAll(this.getCerereTrimis());
        friendshipList.setItems(friendsObs);
        userList.setItems(userObs);
        mesajeList.setItems(msjObs);
        cerereListPrimit.setItems(cerereObsPrimit);
        cerereListTrimis.setItems(cerereObsTrimis);
    }

    public void actualizeaza() {
        friendsObs.clear();
        userObs.clear();
        cerereObsPrimit.clear();
        cerereObsTrimis.clear();
        try {
            friendsObs.addAll(this.getPrietenie());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        try {
            userObs.addAll(this.getUtilizator());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        cerereObsPrimit.addAll(this.getCererePrimit());
        cerereObsTrimis.addAll(this.getCerereTrimis());
        userList.setItems(userObs);
        friendshipList.setItems(friendsObs);
        cerereListPrimit.setItems(cerereObsPrimit);
        cerereListTrimis.setItems(cerereObsTrimis);
    }

    public void displayErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public void handleDeletePrietenie(ActionEvent actionEvent) {
        Prietenie selectedLine = friendshipList.getSelectionModel().getSelectedItem();
        if (selectedLine != null) {
            String numeUser1 = selectedLine.getUtilizator1().getNume();
            String prenumeUser1 = selectedLine.getUtilizator1().getPrenume();
            String numeUser2 = selectedLine.getUtilizator2().getNume();
            String prenumeUser2 = selectedLine.getUtilizator2().getPrenume();
            try {
                Service.getInstance().stergePrieten(numeUser1,prenumeUser1,numeUser2,prenumeUser2);

            } catch (ValidationException e) {
                //System.out.println("Eroare: " + e.getMessage());
                displayErrorAlert("Error Occurred", "Eroare la stergere", e.getMessage());
            } catch (Exception e) {
                //System.out.println("Eroare: " +
                // e.getMessage());
                displayErrorAlert("Error Occurred", "Eroare la stergere", e.getMessage());
            }
            this.actualizeaza();
        } else {
            return;
            //displayErrorAlert("Error Occurred", "Selectare incorectă", "Nu a fost selectată nicio cerere.");
        }
    }
    public void actualizeazaMsj() {
        msjObs.clear();
        msjObs.setAll(this.getMesaje());
        mesajeList.setItems(msjObs);
    }

    public void handleTrimiteMesaj(ActionEvent actionEvent) {
        Utilizator selectedLineUser = userList.getSelectionModel().getSelectedItem();
        if (selectedLineUser != null) {
            String numeUser2 = selectedLineUser.getNume();
            String prenumeUser2 = selectedLineUser.getPrenume();
            try {
                String text = mesaj.getText();
                Mesaj selectedLineMesaj = mesajeList.getSelectionModel().getSelectedItem();
                Long reply;
                if (selectedLineMesaj==null) {
                    reply = -1L;
                } else {
                    reply = selectedLineMesaj.getId();
                }
                Service.getInstance().trimiteMesaj(user.getNume(), user.getPrenume(), numeUser2, prenumeUser2, text, reply);
            } catch (ValidationException e) {
                //System.out.println("Eroare: " + e.getMessage());
                this.displayErrorAlert("Error Occurred", "Eroare trimitere mesaj", e.getMessage());
            } catch (Exception e) {
                //System.out.println("Eroare: " + e.getMessage());
                this.displayErrorAlert("Error Occurred", "Eroare trimitere mesaj", e.getMessage());
            }
            this.actualizeazaMsj();
        }
    }

    public void afiseazaMsj(List<Mesaj> lista) {
        msjObs.clear();
        msjObs.setAll(lista);
        mesajeList.setItems(msjObs);
    }

    public void handleConversatie(ActionEvent actionEvent) {
        Utilizator selectedLineUser = userList.getSelectionModel().getSelectedItem();
        if (selectedLineUser != null) {
            String numeUser2 = selectedLineUser.getNume();
            String prenumeUser2 = selectedLineUser.getPrenume();
            try {
                this.afiseazaMsj(Service.getInstance().conversatie(user.getNume(), user.getPrenume(), numeUser2, prenumeUser2));
            } catch (ValidationException e) {
                //System.out.println("Eroare: " + e.getMessage());
                this.displayErrorAlert("Error Occurred", "Eroare conversatie", e.getMessage());
            } catch (Exception e) {
                //System.out.println("Eroare: " + e.getMessage());
                this.displayErrorAlert("Error Occurred", "Eroare conversatie", e.getMessage());
            }
        }
    }

    public void handleExitConv(ActionEvent actionEvent) {
        this.actualizeazaMsj();
    }

    public void handleTrimiteCerere(ActionEvent actionEvent) {
        Utilizator selectedLine = userList.getSelectionModel().getSelectedItem();
        if (selectedLine != null) {
            String numeUser2 = selectedLine.getNume();
            String prenumeUser2 = selectedLine.getPrenume();
            try {
                Service.getInstance().creeazaCerere(user.getNume(), user.getPrenume(), numeUser2, prenumeUser2);

            } catch (ValidationException e) {
                //System.out.println("Eroare: " + e.getMessage());
                displayErrorAlert("Error Occurred", "Eroare la trimitere cerere", e.getMessage());
            } catch (Exception e) {
                //System.out.println("Eroare: " + e.getMessage());
                displayErrorAlert("Error Occurred", "Eroare la trimitere cerere", e.getMessage());
            }
            this.actualizeaza();
        }
    }

    public void handleAcceptaCerere(ActionEvent actionEvent) {
        Cerere selectedLine = cerereListPrimit.getSelectionModel().getSelectedItem();
        if (selectedLine != null) {
            String numeUser1 = selectedLine.getUtilizator1().getNume();
            String prenumeUser1 = selectedLine.getUtilizator1().getPrenume();
            String numeUser2 = user.getNume();
            String prenumeUser2 = user.getPrenume();

            try {
                Service.getInstance().acceptaCerere(numeUser1, prenumeUser1, numeUser2, prenumeUser2);

            } catch (ValidationException e) {
                displayErrorAlert("Error Occurred", "Eroare la acceptare cerere", e.getMessage());
            } catch (Exception e) {
                displayErrorAlert("Error Occurred", "Eroare la acceptare cerere", e.getMessage());
            }
            this.actualizeaza();
        } else {
            displayErrorAlert("Error Occurred", "Selectare incorectă", "Nu a fost selectată nicio cerere.");
        }
    }

    public void handleRespingeCerere(ActionEvent actionEvent) {
        Cerere selectedLine = cerereListPrimit.getSelectionModel().getSelectedItem();
        if (selectedLine != null) {
            String numeUser1 = selectedLine.getUtilizator1().getNume();
            String prenumeUser1 = selectedLine.getUtilizator1().getPrenume();
            String numeUser2 = user.getNume();
            String prenumeUser2 = user.getPrenume();

            try {
                Service.getInstance().respingeCerere(numeUser1, prenumeUser1, numeUser2, prenumeUser2);

            } catch (ValidationException e) {
                displayErrorAlert("Error Occurred", "Eroare la respingere cerere", e.getMessage());
            } catch (Exception e) {
                displayErrorAlert("Error Occurred", "Eroare la respingere cerere", e.getMessage());
            }
            this.actualizeaza();
        } else {
            displayErrorAlert("Error Occurred", "Selectare incorectă", "Nu a fost selectată nicio cerere.");
        }
    }

    public void handleStergeCerere(ActionEvent actionEvent) {
        Cerere selectedLine = cerereListPrimit.getSelectionModel().getSelectedItem();
        if (selectedLine != null) {
            String numeUser1 = user.getNume();
            String prenumeUser1 = user.getPrenume();
            String numeUser2 = selectedLine.getUtilizator1().getNume();
            String prenumeUser2 = selectedLine.getUtilizator1().getPrenume();
            try {
                Service.getInstance().stergeCerere(numeUser1, prenumeUser1, numeUser2, prenumeUser2);

            } catch (ValidationException e) {
                displayErrorAlert("Error Occurred", "Eroare la cancel cerere", e.getMessage());
            } catch (Exception e) {
                displayErrorAlert("Error Occurred", "Eroare la cancel cerere", e.getMessage());
            }
            this.actualizeaza();
        } else {
            displayErrorAlert("Error Occurred", "Selectare incorectă", "Nu a fost selectată nicio cerere.");
        }
    }

    public void handlePaginaUser(ActionEvent actionEvent) {
        try {
            String nrInregistratiStr= nrinregistrariUser.getText();
            String paginaCurentaUserStr = paginaCurentaUser.getText();
            if(!nrinregistrariUser.getText().isEmpty()){
                inregistrariUser= Integer.parseInt(nrInregistratiStr);
            }
            if(!paginaCurentaUser.getText().isEmpty()) {
                paginaUser = Integer.parseInt(paginaCurentaUserStr);
            }
            this.actualizeaza();
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la schimbare pagina", e.getMessage());
        }
    }

    public void handleInapoiUser(ActionEvent actionEvent) {
        if (paginaUser>2) paginaUser=paginaUser-1;
        else{
            displayErrorAlert("Error Occurred", "Eroare la paginare", "Nu exista pagina pt inapoi!");
        }
        actualizeaza();
    }

    public void handleInainteUser(ActionEvent actionEvent) {
        paginaUser=paginaUser+1;
        actualizeaza();
    }

    public void handlePaginaPrietenie(ActionEvent actionEvent) {
        try {
            String nrInregistratiStr= nrinregistrariPrietenie.getText();
            String paginaCurentaStr = paginaCurentaPrietenie.getText();
            if(!nrinregistrariPrietenie.getText().isEmpty()){
                inregistrariPrietenie= Integer.parseInt(nrInregistratiStr);
            }
            if(!paginaCurentaPrietenie.getText().isEmpty()) {
                paginaPrietenie = Integer.parseInt(paginaCurentaStr);
            }
            this.actualizeaza();
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la schimbare pagina", e.getMessage());
        }
    }

    public void handleInapoiPrietenie(ActionEvent actionEvent) {
        if (paginaPrietenie>2) paginaPrietenie=paginaPrietenie-1;
        else{
            displayErrorAlert("Error Occurred", "Eroare la paginare", "Nu exista pagina pt inapoi!");
        }
        actualizeaza();
    }

    public void handleInaintePrietenie(ActionEvent actionEvent) {
        paginaPrietenie=paginaPrietenie+1;
        actualizeaza();
    }

    public void deleteAccount(ActionEvent actionEvent) throws Exception {
        Service.getInstance().stergeUtilizator(user.getNume(),user.getPrenume());

        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/lab7bun/LogIn.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        AnchorPane logInLayout = stageLoader.load();
        Scene scene = new Scene(logInLayout);
        stage.setScene(scene);

        LogInController logInController = stageLoader.getController();

        stage.show();
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader stageLoader = new FXMLLoader();
        stageLoader.setLocation(getClass().getResource("/com/example/lab7bun/LogIn.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();

        AnchorPane logInLayout = stageLoader.load();
        Scene scene = new Scene(logInLayout);
        stage.setScene(scene);

        LogInController logInController = stageLoader.getController();

        stage.show();
    }

    public void afiseaza(List<Utilizator> lista) {
        statisticiObs.clear();
        statisticiObs.setAll(lista);
        statisticiList.setItems(statisticiObs);
    }

    public void displayRezultat(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public void handleComunitateSociabila(ActionEvent actionEvent) {
        try {
            this.displayRezultat("Info", "Rezultat", "Membrii din cea mai activa comunitate: ");
            ArrayList<ArrayList<Utilizator>> result = Service.getInstance().comunitateSociabila();
            afiseaza(Service.getInstance().celMaiMareArray(result));
        } catch (Exception e) {
            this.displayErrorAlert("Error Occurred", "Eroare comunitate sociabila", e.getMessage());
        }
    }

    public void handleNrComunitati(ActionEvent actionEvent) {
        try {
            Integer numar_comunitati = Service.getInstance().nrComunitati();
            this.displayRezultat("Info", "Rezultat", "Nr de comunitati: " + numar_comunitati);
        } catch (Exception e) {
            this.displayErrorAlert("Error Occurred", "Eroare nr de comunitati", e.getMessage());
        }
    }

    public void handleNrMinPrieteni(ActionEvent actionEvent) {
        try {
            String str = statistica.getText();
            int n = Integer.parseInt(str);
            this.displayRezultat("Info", "Rezultat", "Utilizatorii cu minim " + n + " prieteni:");
            afiseaza(Service.getInstance().PrieteniMinimPredicat(n));
        } catch (Exception e) {
            this.displayErrorAlert("Error Occurred", "Eroare nr minim prieteni", e.getMessage());
        }
    }

    public void handleLunaPrietenie(ActionEvent actionEvent) {
        try {
            String nume = user.getNume();
            String prenume = user.getPrenume();
            String luna = statistica.getText();
            this.displayRezultat("Info", "Rezultat", "Prietenii din: " + luna);
            afiseaza(Service.getInstance().prieteniLuna(nume, prenume, luna));
        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare prieteni din luna", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare prieteni din luna", e.getMessage());
        }
    }

    public void handleStrUser(ActionEvent actionEvent) {
        try {
            String string = statistica.getText();
            this.displayRezultat("Info", "Rezultat", "Utilizatorii cu : " + string + " in nume");
            afiseaza(Service.getInstance().utilizatoriStr(string));
        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Utilizatori cu string", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Utilizatori cu string", e.getMessage());
        }
    }

    public void handleRefresh(ActionEvent actionEvent) {
        ArrayList<Utilizator>lista=new ArrayList<>();
        this.afiseaza(lista);
    }
}
