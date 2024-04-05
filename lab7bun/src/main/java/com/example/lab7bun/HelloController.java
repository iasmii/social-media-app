package com.example.lab7bun;


import com.example.lab7bun.domain.Cerere;
import com.example.lab7bun.domain.Mesaj;
import com.example.lab7bun.domain.Prietenie;
import com.example.lab7bun.domain.Utilizator;
import com.example.lab7bun.domain.validators.ValidationException;
import com.example.lab7bun.service.Service;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public class HelloController implements Initializable {
    @FXML
    private ListView<Prietenie> friendshipList;

    @FXML
    private ListView<Utilizator> userList;

    @FXML
    private ListView<Mesaj> mesajeList;

    @FXML
    private ListView<Cerere> cerereList;

    @FXML
    private TextField nume1;

    @FXML
    private TextField prenume1;

    @FXML
    private TextField nume2;

    @FXML
    private TextField prenume2;

    @FXML
    private TextField nrluna;

    @FXML
    private TextField str;

    @FXML
    private TextField mesaj;

    @FXML
    private TextField reply;

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

    private final ObservableList<Cerere> cerereObs = FXCollections.observableArrayList();

    private Service service;


    public void setService(Service service) {
        this.service = service;
    }

    public Service getService() {
        return service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        msjObs.addAll(this.getMesaje());
        cerereObs.addAll(this.getCerere());
        friendshipList.setItems(friendsObs);
        userList.setItems(userObs);
        mesajeList.setItems(msjObs);
        cerereList.setItems(cerereObs);
    }

    public List<Prietenie> getPrietenie() throws Exception {
        //Iterable<Prietenie> prieteni = Service.getInstance().getAllPrietenie();
        Iterable<Prietenie> prieteni = Service.getInstance().getPrieteniePaginati(inregistrariPrietenie, paginaPrietenie, 1L);
        List<Prietenie> listapr = StreamSupport.stream(prieteni.spliterator(), false).toList();
        return listapr;
    }

    public List<Utilizator> getUtilizator() throws Exception {
        //Iterable<Utilizator> utilizatori = Service.getInstance().getAllUtilizatori();
        Iterable<Utilizator> utilizatori = Service.getInstance().getUtilizatoriPaginati(inregistrariUser, paginaUser);
        List<Utilizator> listaut = StreamSupport.stream(utilizatori.spliterator(), false).toList();
        return listaut;
    }

    public List<Mesaj> getMesaje() {
        Iterable<Mesaj> mesaje = Service.getInstance().getAllMesaje();
        List<Mesaj> listamsj = StreamSupport.stream(mesaje.spliterator(), false).toList();
        return listamsj;
    }

    public List<Cerere> getCerere() {
        Iterable<Cerere> cereri = Service.getInstance().getAllCereri();
        List<Cerere> listacereri = StreamSupport.stream(cereri.spliterator(), false).toList();
        return listacereri;
    }

    public void actualizeaza() {
        friendsObs.clear();
        userObs.clear();
        cerereObs.clear();
        try {
            friendsObs.setAll(this.getPrietenie());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        try {
            userObs.setAll(this.getUtilizator());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        cerereObs.setAll(this.getCerere());
        userList.setItems(userObs);
        friendshipList.setItems(friendsObs);
        cerereList.setItems(cerereObs);
    }

    public void actualizeazaMsj() {
        msjObs.clear();
        msjObs.setAll(this.getMesaje());
        mesajeList.setItems(msjObs);
    }

    public void afiseaza(List<Utilizator> lista) {
        friendsObs.clear();
        userObs.clear();
        try {
            friendsObs.setAll(this.getPrietenie());
        } catch (Exception e) {
            displayErrorAlert("Error Occurred", "Eroare la paginare", e.getMessage());
        }
        userObs.setAll(lista);
        userList.setItems(userObs);
        friendshipList.setItems(friendsObs);
    }

    public void afiseazaMsj(List<Mesaj> lista) {
        msjObs.clear();
        msjObs.setAll(lista);
        mesajeList.setItems(msjObs);
    }

    public void displayErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public void displayRezultat(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    public void handleAddUser(ActionEvent actionEvent) {
        String nume = nume1.getText();
        String prenume = prenume1.getText();
        String parola = str.getText();
        try {
            Service.getInstance().adaugaUtilizator(nume, prenume,parola);

        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la adaugare", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la adaugare", e.getMessage());
        }
        this.actualizeaza();
    }

    public void handleUpdateUser(ActionEvent actionEvent) {
        String nume = nume1.getText();
        String prenume = prenume1.getText();
        String numeNou = nume2.getText();
        String prenumeNou = prenume2.getText();
        try {
            Service.getInstance().modificaUtilizator(nume, prenume, numeNou, prenumeNou);
        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la update", e.getMessage());
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
            //displayErrorAlert("Error Occurred", "Eroare la update", e.getMessage());
        }
        this.actualizeaza();
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        String nume = nume1.getText();
        String prenume = prenume1.getText();
        try {
            Service.getInstance().stergeUtilizator(nume, prenume);

        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la stergere", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la stergere", e.getMessage());
        }
        this.actualizeaza();
    }

    public void handleAddPrietenie(ActionEvent actionEvent) {
        String numeUser1 = nume1.getText();
        String prenumeUser1 = prenume1.getText();
        String numeUser2 = nume2.getText();
        String prenumeUser2 = prenume2.getText();
        try {
            Service.getInstance().adaugaPrieten(numeUser1, prenumeUser1, numeUser2, prenumeUser2);

        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la adaugare", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la adaugare", e.getMessage());
        }
        this.actualizeaza();
    }

    public void handleDeletePrietenie(ActionEvent actionEvent) {
        String numeUser1 = nume1.getText();
        String prenumeUser1 = prenume1.getText();
        String numeUser2 = nume2.getText();
        String prenumeUser2 = prenume2.getText();
        try {
            Service.getInstance().stergePrieten(numeUser1, prenumeUser1, numeUser2, prenumeUser2);

        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la stergere", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la stergere", e.getMessage());
        }
        this.actualizeaza();
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
            String str = nrluna.getText();
            int n = Integer.parseInt(str);
            this.displayRezultat("Info", "Rezultat", "Utilizatorii cu minim " + n + " prieteni:");
            afiseaza(Service.getInstance().PrieteniMinimPredicat(n));
        } catch (Exception e) {
            this.displayErrorAlert("Error Occurred", "Eroare nr minim prieteni", e.getMessage());
        }
    }

    public void handleLunaPrietenie(ActionEvent actionEvent) {
        try {
            String nume = nume1.getText();
            String prenume = prenume1.getText();
            String luna = nrluna.getText();
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
            String string = str.getText();
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

    public void handleTrimiteMesaj(ActionEvent actionEvent) {
        try {
            String numeUser1 = nume1.getText();
            String prenumeUser1 = prenume1.getText();
            String numeUser2 = nume2.getText();
            String prenumeUser2 = prenume2.getText();
            String text = mesaj.getText();
            String replystr = reply.getText();
            Long reply;
            if (replystr.isEmpty()) {
                reply = -1L;
            } else {
                reply = Long.parseLong(replystr);
            }
            Service.getInstance().trimiteMesaj(numeUser1, prenumeUser1, numeUser2, prenumeUser2, text, reply);
        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare trimitere mesaj", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare trimitere mesaj", e.getMessage());
        }
        this.actualizeazaMsj();
    }

    public void handleConversatie(ActionEvent actionEvent) {
        try {
            String numeUser1 = nume1.getText();
            String prenumeUser1 = prenume1.getText();
            String numeUser2 = nume2.getText();
            String prenumeUser2 = prenume2.getText();
            this.afiseazaMsj(Service.getInstance().conversatie(numeUser1, prenumeUser1, numeUser2, prenumeUser2));
        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare conversatie", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare conversatie", e.getMessage());
        }
    }

    public void handleRefresh(ActionEvent actionEvent) {
        this.actualizeaza();
    }

    public void handleExitConv(ActionEvent actionEvent) {
        this.actualizeazaMsj();
    }

    public void handleTrimiteMesajMaiMultiUser(ActionEvent actionEvent) {
        try {
            String numeUser1 = nume1.getText();
            String prenumeUser1 = prenume1.getText();
            String iduri = str.getText();
            String text = mesaj.getText();
            String replystr = reply.getText();
            Long reply;
            if (replystr.isEmpty()) {
                reply = -1L;
            } else {
                reply = Long.parseLong(replystr);
            }
            Service.getInstance().forwardMesaj(numeUser1, prenumeUser1, iduri, text, reply);
        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare trimitere mesaj", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            this.displayErrorAlert("Error Occurred", "Eroare trimitere mesaj", e.getMessage());
        }
        this.actualizeazaMsj();
    }

    public void handleTrimiteCerere(ActionEvent actionEvent) {
        String numeUser1 = nume1.getText();
        String prenumeUser1 = prenume1.getText();
        String numeUser2 = nume2.getText();
        String prenumeUser2 = prenume2.getText();
        try {
            Service.getInstance().creeazaCerere(numeUser1, prenumeUser1, numeUser2, prenumeUser2);

        } catch (ValidationException e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la trimitere cerere", e.getMessage());
        } catch (Exception e) {
            //System.out.println("Eroare: " + e.getMessage());
            displayErrorAlert("Error Occurred", "Eroare la trimitere cerere", e.getMessage());
        }
        this.actualizeaza();
    }

    public void handleAcceptaCerere(ActionEvent actionEvent) {
        Cerere selectedLine = cerereList.getSelectionModel().getSelectedItem();
        if (selectedLine != null) {
            String numeUser1 = selectedLine.getUtilizator1().getNume();
            String prenumeUser1 = selectedLine.getUtilizator1().getPrenume();
            String numeUser2 = selectedLine.getUtilizator2().getNume();
            String prenumeUser2 = selectedLine.getUtilizator2().getPrenume();

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
        Cerere selectedLine = cerereList.getSelectionModel().getSelectedItem();
        if (selectedLine != null) {
            String numeUser1 = selectedLine.getUtilizator1().getNume();
            String prenumeUser1 = selectedLine.getUtilizator1().getPrenume();
            String numeUser2 = selectedLine.getUtilizator2().getNume();
            String prenumeUser2 = selectedLine.getUtilizator2().getPrenume();

            try {
                Service.getInstance().respingeCerere(numeUser1, prenumeUser1, numeUser2, prenumeUser2);

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
}