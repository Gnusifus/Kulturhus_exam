package programutvikling.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import programutvikling.base.*;
import programutvikling.base.exceptions.InvalidEmailException;
import programutvikling.base.exceptions.InvalidNameException;
import programutvikling.base.exceptions.InvalidNumberException;
import programutvikling.base.exceptions.InvalidUrlException;
import programutvikling.base.filehandling.CsvReader;
import programutvikling.base.filehandling.CsvWriter;
import programutvikling.base.filehandling.JobjReader;
import programutvikling.base.filehandling.JobjWriter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import static programutvikling.base.Lokale.getLokale;

public class MainController implements Initializable {
    @FXML private AnchorPane anchorPane;

    @FXML private ProgressBar kontaktpersonProgress;
    @FXML private ProgressBar arrangementProgress;
    @FXML private ProgressBar billettProgress;

    @FXML private TableView<Kontaktperson> kontaktpersonTableView;
    @FXML private Label kontaktpersonMelding;

    //Kontaktperson
    //textfields
    @FXML private TextField fornavnTxtField;
    @FXML private TextField etternavnTxtField;
    @FXML private TextField emailTxtField;
    @FXML private TextField tlfTxtField;
    @FXML private TextField virksomhetTxtField;
    @FXML private TextField nettsideTxtField;
    @FXML private TextArea opplysningerTxtArea;
    @FXML private TextField sokFelt;

    //buttons
    @FXML private Button leggTilKonBtn;
    @FXML private Button slettKonBtn;
    @FXML private Button endreKonBtn;
    @FXML private Button lagreKonBtn;


    //Arrangement
    @FXML private TableView<Arrangement> arrangementTableView;
    @FXML private Label arrangementMelding;

    //input
    @FXML private TextField navnTxtField;
    @FXML private TextField varighetTxtField;
    @FXML private TextField artistTxtField;
    @FXML private TextField programTxtField;
    @FXML private DatePicker datoTxtField;
    @FXML private ComboBox<Kontaktperson> kontaktpersonComboBox;
    @FXML private ComboBox<Lokale> lokaleComboBox;

    //buttons
    @FXML private Button leggTilArrBtn;
    @FXML private Button slettArrBtn;
    @FXML private Button endreArrBtn;
    @FXML private Button lagreArrBtn;

    //Billett
    @FXML private TableView<Billett> billettTableView;
    @FXML private Label billettMelding;

    //input
    @FXML private ComboBox arrangementComboBox;
    @FXML private TextField prisTxtField;
    @FXML private TextField plassTxtField;
    @FXML private TextField billettTlfTxtField;

    //buttons
    @FXML private Button lagreBillettBtn;
    @FXML private Button slettBillettBtn;
    @FXML private Button endreBillettBtn;
    @FXML private Button leggTilBillettBtn;

    //Lokale
    @FXML private TableView<Lokale> lokaleTableView;

    //tablecolumns
    @FXML private TableColumn<Lokale, String> lokNavnCol;
    @FXML private TableColumn<Lokale, String> lokTypeCol;
    @FXML private TableColumn<Lokale, Integer> lokAntallPlasserCol;

    //Arrangement, for factory
    @FXML private TableColumn navnCol;
    @FXML private TableColumn lokaleCol;
    @FXML private TableColumn datoCol;
    @FXML private TableColumn varighetCol;
    @FXML private TableColumn kontaktpersonCol;
    @FXML private TableColumn utsolgtCol;
    @FXML private TableColumn artisterCol;
    @FXML private TableColumn programCol;
    @FXML private TableColumn antallPlasserCol;

    //Billett, for factory
    @FXML TableColumn billettArrCol;
    @FXML TableColumn billettPrisCol;
    @FXML TableColumn billettPlassCol;
    @FXML TableColumn billettTlfCol;


    //Søkefelt
    @FXML private TextField sokBillFelt;
    @FXML private TextField sokKonFelt;
    @FXML private TextField sokArgFelt;

    //Validering
    @FXML private Label ugyldigFornavn;
    @FXML private Label ugyldigEtternavn;
    @FXML private Label ugyldigEmail;
    @FXML private Label ugyldigTelefonnummer;
    @FXML private Label ugyldigNettside;
    @FXML private Label ugyldigArrangementNavn;
    @FXML private Label ugyldigVarighet;
    @FXML private Label ugyldigArtist;
    @FXML private Label ugyldigProgram;
    @FXML private Label ugyldigPris;
    @FXML private Label ugyldigPlass;
    @FXML private Label ugyldigBillettTlf;


    AlertBoxes alert = new AlertBoxes();
    FilVelger filVelger = new FilVelger();
    CsvWriter csvWriter = new CsvWriter();
    JobjWriter jobjWriter = new JobjWriter();

    ObservableList<Kontaktperson> sokKonList = FXCollections.observableArrayList();
    ObservableList<Arrangement> sokArgList = FXCollections.observableArrayList();
    ObservableList<Billett> sokBillList = FXCollections.observableArrayList();

    private ExecutorService service = Executors.newSingleThreadExecutor();
    public void readFromFileThread(String type, File file) {
        disableButtons(true);

        ReaderThread readTask = new ReaderThread(null, type, file);
        arrangementProgress.progressProperty().bind(readTask.progressProperty());
        kontaktpersonProgress.progressProperty().bind(readTask.progressProperty());
        billettProgress.progressProperty().bind(readTask.progressProperty());

        readTask.setOnSucceeded(event -> {
            ObservableList<?> list = readTask.getValue();
            if(type.toLowerCase().equals("kontaktperson")){
                kontaktpersonTableView.setItems((ObservableList<Kontaktperson>) list);
                kontaktpersonMelding.setText("Lesing av " + file.getName() + " fullført!");
                sokKonList.setAll ((Collection<? extends Kontaktperson>) list) ;
            }
            if(type.toLowerCase().equals("arrangement")){
                arrangementTableView.setItems((ObservableList<Arrangement>) list);
                arrangementMelding.setText("Lesing av " + file.getName() + " fullført!");
                sokArgList.setAll((Collection<? extends Arrangement>) list);
            }
            if(type.toLowerCase().equals("billett")){
                billettTableView.setItems((ObservableList<Billett>) list);
                billettMelding.setText("Lesing av " + file.getName() + " fullført!");
                sokBillList.setAll((Collection<? extends Billett>) list);
            }
            disableButtons(false);
        });

        readTask.setOnFailed(event -> {
            disableButtons(false);
            AlertBoxes alert = new AlertBoxes();
            alert.info("Feil", "Kunne ikke lese fra fil", readTask.getException().getMessage());
        });
        service.execute(readTask);
    }

    public void disableButtons(Boolean shouldDisable){
        leggTilKonBtn.setDisable(shouldDisable);
        endreKonBtn.setDisable(shouldDisable);
        slettKonBtn.setDisable(shouldDisable);
        lagreKonBtn.setDisable(shouldDisable);
        kontaktpersonProgress.setVisible(shouldDisable);

        leggTilArrBtn.setDisable(shouldDisable);
        endreArrBtn.setDisable(shouldDisable);
        slettArrBtn.setDisable(shouldDisable);
        lagreArrBtn.setDisable(shouldDisable);
        arrangementProgress.setVisible(shouldDisable);

        leggTilBillettBtn.setDisable(shouldDisable);
        endreBillettBtn.setDisable(shouldDisable);
        slettBillettBtn.setDisable(shouldDisable);
        lagreBillettBtn.setDisable(shouldDisable);
        billettProgress.setVisible(shouldDisable);
    }

    ObservableList<Kontaktperson> myList = FXCollections.observableArrayList();

    /* KONTAKTPERSON -----------------------------------------------------------------------------------------------------*/

    public void lagreKonBtnPress(ActionEvent actionEvent) throws IOException {
        ObservableList<Kontaktperson> kontaktpersoner = kontaktpersonTableView.getItems();
        File file = filVelger.addKonFileChooser(kontaktpersoner, anchorPane);
        try {
            if (file != null) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                String csvExtension = ".csv";
                String jobjExtension = ".jobj";

                if (fileExtension.toLowerCase().equals(csvExtension)) {
                    csvWriter.kontaktpersonWrite(kontaktpersoner, file);
                }
                if (fileExtension.toLowerCase().equals(jobjExtension)) {
                    jobjWriter.kontaktpersonWrite(kontaktpersoner, file);
                }
                clearKontaktpersonFields();
                kontaktpersonMelding.setText("Liste lagret til " + file.getName());
            }
        }
        catch (Exception e) {
            alert.info("Kunne ikke skrive til fil!",
                    "Noe gikk galt under skrivringen av kontaktperson til filen " + file.getName(),
                    "Prøv igjen");
        }
        setValuesComboBox();
    }

    public void slettKonBtnPress() {
        Kontaktperson valgtKontaktperson = kontaktpersonTableView.getSelectionModel().getSelectedItem();
        if (valgtKontaktperson != null) {
            ObservableList<Kontaktperson> kontaktperson, alleKontaktpersoner = kontaktpersonTableView.getItems();
            kontaktperson = kontaktpersonTableView.getSelectionModel().getSelectedItems();
            if (alert.bekreft("Slette kontaktperson", "Du er i ferd med å slette en kontaktperson fra listen", "Ønsker du å gjøre dette?")) {
                kontaktperson.forEach(alleKontaktpersoner::remove);
                kontaktpersonMelding.setText("Du har ulagrede endringer i listen");
            }
        }
        else {
            alert.info("Ikke valgt kontaktperson!",
                    "Du må velge en kontaktperson i listen som du vil slette",
                    "Er listen tom så må du laste inn en fil med kontaktpersoner");
        }
    }

    public void endreKonBtnPress() {
        Kontaktperson valgtKontaktperson = kontaktpersonTableView.getSelectionModel().getSelectedItem();

        if (valgtKontaktperson != null) {
            kontaktpersonMelding.setText("");
            fornavnTxtField.setText(valgtKontaktperson.getFornavn());
            etternavnTxtField.setText(valgtKontaktperson.getEtternavn());
            emailTxtField.setText(valgtKontaktperson.getEmail());
            tlfTxtField.setText(valgtKontaktperson.getTelefonnummer());
            virksomhetTxtField.setText(valgtKontaktperson.getVirksomhet());
            nettsideTxtField.setText(valgtKontaktperson.getNettside());
            opplysningerTxtArea.setText(valgtKontaktperson.getOpplysninger());

            ObservableList<Kontaktperson> kontaktperson, alleKontaktpersoner = kontaktpersonTableView.getItems();
            kontaktperson = kontaktpersonTableView.getSelectionModel().getSelectedItems();
            kontaktperson.forEach(alleKontaktpersoner::remove);
            kontaktpersonMelding.setText("Du har ulagrede endringer i listen");

        }
        else {
            alert.info("Ikke valgt kontaktperson!",
                    "Du må velge en kontaktperson i listen som du vil slette",
                    "Er listen tom så må du laste inn en fil med kontaktpersoner");
        }
    }

    public boolean kontaktPersValidert() throws InvalidNumberException, InvalidNameException, InvalidEmailException, InvalidUrlException {
        boolean inputValidert = false;
        boolean tlfNrLengde = ValideringHandler.telefonValidering(tlfTxtField, ugyldigTelefonnummer, "Telefonnummer må være 8 siffer !");
        boolean fornavn = ValideringHandler.tekstAlfabet(fornavnTxtField, ugyldigFornavn, "Kun bokstaver er gyldig !");
        boolean etternavn = ValideringHandler.tekstAlfabet(etternavnTxtField, ugyldigEtternavn, "Kun bokstaver er gyldig !");
        boolean email = ValideringHandler.emailFormat(emailTxtField, ugyldigEmail, "skriv gyldig email addresse !!!");
        boolean nettside = ValideringHandler.nettsideFormat(nettsideTxtField, ugyldigNettside, "skriv gyldig nettadress ");

        if (tlfNrLengde == true && fornavn == true && etternavn == true && email == true && nettside == true) {
            inputValidert = true;
        }

        return inputValidert;
    }

    public void leggTilKonBtnPress() throws InvalidNumberException, InvalidUrlException, InvalidNameException, InvalidEmailException {
        if ((kontaktPersValidert())) {
            ObservableList<Kontaktperson> data = FXCollections.observableArrayList(kontaktpersonTableView.getItems());
            data.add(new Kontaktperson(fornavnTxtField.getText(),
                    etternavnTxtField.getText(),
                    emailTxtField.getText(),
                    tlfTxtField.getText(),
                    virksomhetTxtField.getText(),
                    nettsideTxtField.getText(),
                    opplysningerTxtArea.getText()));
            clearKontaktpersonFields();
            kontaktpersonMelding.setText("Du har ulagrede endringer i listen");

            kontaktpersonTableView.setItems(data);
            sokKonList.setAll(data);
        }
    }

    public void opneKonFilBtnPress(ActionEvent actionEvent) throws IOException {
        File file = filVelger.openFileChooser(anchorPane, "kontaktpersoner");
        if (file != null) {
            readFromFileThread("kontaktperson", file);
        }
        else {
            alert.info("Kunne ikke lese fra fil!",
                    "Kunne ikke lese kontaktpersoner fra filen " + file.getName(),
                    "Se til at filen inneholder kontaktperson-elementer");
        }
    }

    public void clearKontaktpersonFields() {
        fornavnTxtField.clear();
        etternavnTxtField.clear();
        emailTxtField.clear();
        tlfTxtField.clear();
        nettsideTxtField.clear();
        opplysningerTxtArea.clear();
        virksomhetTxtField.clear();
    }

    /* ARRANGEMENT -------------------------------------------------------------------------------------------------------*/
    public void lagreArrBtnPress(ActionEvent actionEvent) throws IOException {
        ObservableList<Arrangement> arrangementer = arrangementTableView.getItems();
        File file = filVelger.addArrFileChooser(arrangementer, anchorPane);
        try {
            if (file != null) {
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                String csvExtension = ".csv";
                String jobjExtension = ".jobj";

                if (fileExtension.equals(csvExtension)) {
                    csvWriter.arrangementWrite(arrangementer, file);
                }
                if (fileExtension.equals(jobjExtension)) {
                    jobjWriter.arrangementWrite(arrangementer, file);
                }
                clearArrangementFields();
                arrangementMelding.setText("Liste lagret til " + file.getName());
            }
            setValuesComboBox();
        }
        catch (Exception e) {
            alert.info("Kunne ikke skrive til fil!",
                    "Noe gikk galt under lagring av arrangement-liste til filen " + file.getName(),
                    "Prøv igjen.");
        }
        setValuesComboBox();
    }

    public boolean arrangementValidert() throws InvalidNameException {
        boolean inputValidert = false;
        boolean varighet = ValideringHandler.tall(varighetTxtField,ugyldigVarighet,"Varighet er kun siffer");
        boolean program = ValideringHandler.tekstAlfabet(programTxtField,ugyldigProgram,"Program kan kun inneholde bokstaver");
        if (varighet == true && program==true) {
            inputValidert = true;
        }

        return inputValidert;
    }

    public void leggTilArrBtnPress(ActionEvent e) throws IOException, InvalidNameException {
        if (arrangementValidert()) {
            ObservableList<Arrangement> data = FXCollections.observableArrayList(arrangementTableView.getItems());

            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String date = datoTxtField.getValue().format(pattern);
            LocalDate localDate = LocalDate.parse(date, pattern);

            int varighet = Integer.parseInt(varighetTxtField.getText());

            Arrangement arrangement = new Arrangement(lokaleComboBox.getValue(),
                    kontaktpersonComboBox.getValue(),
                    navnTxtField.getText(),
                    localDate,
                    artistTxtField.getText(),
                    varighet,
                    programTxtField.getText(),
                    false);

            data.add(arrangement);
            clearArrangementFields();
            arrangementMelding.setText("Du har ulagrede endringer i listen");

            arrangementTableView.setItems(data);
            sokArgList.setAll(data);
        }
    }

    public void slettArrBtnPress() {
        Arrangement valgtArrangement = arrangementTableView.getSelectionModel().getSelectedItem();
        if (valgtArrangement != null) {
            ObservableList<Arrangement> arrangement, alleArrangementer = arrangementTableView.getItems();
            arrangement = arrangementTableView.getSelectionModel().getSelectedItems();
            if (alert.bekreft("Slette arrangement", "Du er i ferd med å slette et arrangement fra listen", "Ønsker du å gjøre dette?")) {
                arrangement.forEach(alleArrangementer::remove);
                arrangementMelding.setText("Du har ulagrede endringer i listen");
            }
        }
        else {
            alert.info("Ikke valgt arrangement!",
                    "Du må velge et arrangement i listen som du vil slette",
                    "Er listen tom så må du laste inn en fil med arrangementer");
        }
    }

    public void opneArrFilBtnPress() throws IOException {
        File file = filVelger.openFileChooser(anchorPane, "arrangementer");
        try {
            if (file != null) {
                readFromFileThread("arrangement", file);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            alert.info("Kunne ikke lese fra fil!",
                    "Noe gikk galt under lesingen av filen " + file.getName(),
                    "Se til at filen inneholder arrangement-elementer.");
        }
    }

    public void clickArr() {
        Arrangement arrangement = arrangementTableView.getSelectionModel().getSelectedItem();
        Parent root = null;
        if (arrangement != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                root = fxmlLoader.load(getClass().getResource("../arrangementPromo.fxml").openStream());

                ArrangementPromoController controller = fxmlLoader.getController();
                controller.setArrangement(arrangement);

            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Scene scene = new Scene(root);
            Stage arrPromo = new Stage();
            arrPromo.setTitle("Om arrangementet...");
            arrPromo.setScene(scene);

            arrPromo.initModality(Modality.WINDOW_MODAL);
            arrPromo.show();
        }
    }

    public void endreArrBtnPress() {
        Arrangement valgtArrangmenet = arrangementTableView.getSelectionModel().getSelectedItem();
        if (valgtArrangmenet != null) {
            navnTxtField.setText(valgtArrangmenet.getNavn());
            lokaleComboBox.setValue(valgtArrangmenet.getLokale());
            datoTxtField.setValue(valgtArrangmenet.getDato());
            varighetTxtField.setText(String.valueOf(valgtArrangmenet.getVarighet()));
            kontaktpersonComboBox.setValue(valgtArrangmenet.getKontaktperson());
            artistTxtField.setText(valgtArrangmenet.getArtist());
            programTxtField.setText(valgtArrangmenet.getProgram());

            ObservableList<Arrangement> arrangement, alleArrangementer = arrangementTableView.getItems();
            arrangement = arrangementTableView.getSelectionModel().getSelectedItems();
            arrangement.forEach(alleArrangementer::remove);
            arrangementMelding.setText("Du har ulagrede endringer i listen");
        }
        else {
            alert.info("Ikke valgt arrangement!",
                    "Du må velge et arrangement i listen som du vil endre.",
                    "Er listen tom så må du laste inn en fil med arrangementer.");
        }
    }

    public void clearArrangementFields() {
        navnTxtField.clear();
        varighetTxtField.clear();
        artistTxtField.clear();
        programTxtField.clear();
        datoTxtField.setValue(null);
        kontaktpersonComboBox.setValue(null);
        lokaleComboBox.setValue(null);
    }

    /*BILLETT-----------------------------------------------------------------------------------------------------------*/
    public void lagreBillettBtnPress() {
        ObservableList<Billett> billetter = billettTableView.getItems();
        if (billetter.isEmpty()) {
            alert.info("Ingen elementer",
                    "Kan ikke lagre en tom liste til fil",
                    "Du må legge til elementer i listen før du lagrer");
        }
        else {
            File file = filVelger.addBillettFileChooser(billetter, anchorPane);
            try {
                if (file != null) {
                    String fileName = file.getName();
                    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                    String csvExtension = ".csv";
                    String jobjExtension = ".jobj";

                    if (fileExtension.equals(csvExtension)) {
                        csvWriter.billettWrite(billetter, file);
                    }
                    if (fileExtension.equals(jobjExtension)) {
                        jobjWriter.billettWrite(billetter, file);
                    }
                    clearBillettFields();
                    billettMelding.setText("Liste lagret til " + file.getName());
                }
                setValuesComboBox();
            } catch (Exception ex) {
                alert.info("Kunne ikke skrive til filen!",
                        "Noe gikk galt...",
                        "Kunne ikke skrive til filen " + file.getName());
            }
        }
    }

    public void opneBillettFilBtnPress() throws IOException {
        File file = filVelger.openFileChooser(anchorPane, "billetter");
        try {
            if (file != null) {
                readFromFileThread("billett", file);
            }
        } catch (Exception e) {
            alert.info("Kan ikke lese fra fil!",
                    "Noe gikk galt under lesing av filen " + file.getName(),
                    "Se til at filen inneholder billett-elementer");
        }
    }

    public void endreBillettBtnPress() {
        Billett valgtBillett = billettTableView.getSelectionModel().getSelectedItem();
        if (valgtBillett != null) {
            billettMelding.setText("");
            prisTxtField.setText(String.valueOf(valgtBillett.getPris()));
            plassTxtField.setText(String.valueOf(valgtBillett.getPlass()));
            billettTlfTxtField.setText(valgtBillett.getKjoperTlf());

            ObservableList<Billett> billett, alleBilletter = billettTableView.getItems();
            billett = billettTableView.getSelectionModel().getSelectedItems();
            billett.forEach(alleBilletter::remove);
            billettMelding.setText("Du har ulagrede endringer i listen");

        } else {
            alert.info("Ikke valgt billett!",
                    "Du må velge en billett i listen som du vil endre",
                    "Er listen tom så må du laste inn en fil med billetter");
        }
    }

    public void slettBillettBtnPress() {
        Billett valgtBillett = billettTableView.getSelectionModel().getSelectedItem();
        if (valgtBillett != null) {
            ObservableList<Billett> billett, alleBilletter = billettTableView.getItems();
            billett = billettTableView.getSelectionModel().getSelectedItems();
            if (alert.bekreft("Slette billett", "Du er i ferd med å slette en billett fra listen", "Ønsker du å gjøre dette?")) {
                billett.forEach(alleBilletter::remove);
                billettMelding.setText("Du har ulagrede endringer i listen");
            }
        } else {
            alert.info("Ikke valgt billett!",
                    "Du må velge en billett i listen som du vil slette.",
                    "Er listen tom så må du laste inn en fil med billetter.");
        }
    }

    public void leggTilBillettBtnPress() throws InvalidNumberException {
        if (billettValidert()) {
            ObservableList<Billett> data = FXCollections.observableArrayList(billettTableView.getItems());
            data.add(new Billett((Arrangement) arrangementComboBox.getValue(),
                    Integer.parseInt(prisTxtField.getText()),
                    Integer.parseInt(plassTxtField.getText()),
                    billettTlfTxtField.getText()));
            clearBillettFields();
            billettMelding.setText("Du har ulagrede endringer i listen");

            billettTableView.setItems(data);
            sokBillList.setAll(data);
        }
    }

    public boolean billettValidert() throws InvalidNumberException {
        boolean inputValidert = false;
        boolean pris = ValideringHandler.tall(prisTxtField,ugyldigPris,"Pris kan kun inneholde siffer");
        boolean plass = ValideringHandler.tall(plassTxtField,ugyldigPlass,"Plassnummer kan kun inneholde siffer");
        boolean billtlf = ValideringHandler.telefonValidering(billettTlfTxtField,ugyldigBillettTlf,"Telefon nummer er kun 8 siffer");
        if (pris == true && plass == true && billtlf == true) {
            inputValidert = true;
        }
        return inputValidert;
    }

    public void clearBillettFields() {
        arrangementComboBox.setValue(null);
        prisTxtField.clear();
        plassTxtField.clear();
        billettTlfTxtField.clear();
    }

    /*LOKALE--------------------------------------------------------------------------------------------------------------*/
    public void listLokaler() {
        lokNavnCol.setCellValueFactory(new PropertyValueFactory<>("navn"));
        lokTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        lokAntallPlasserCol.setCellValueFactory(new PropertyValueFactory<>("antallPlasser"));
        try {
            lokaleTableView.setItems(getLokale());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*ANNET----------------------------------------------------------------------------------------------------------*/

    /*
    DEAFULT FILER, brukes i comboboxer

    Kontaktperson Csv-fil = src/main/filer/kontaktpersoner.csv
    Kontaktperson Jobj-fil = src/main/filer/kontaktpersoner.jobj

    Arrangement Csv-fil = src/main/filer/arrangementer.csv
    Arrangement Jobj-fil = src/main/filer/arrangementer.jobj

    Billett Csv-fil = src/main/filer/billetter.csv
    Billett Jobj-fil = src/main/filer/billetter.jobj
    */

    public void setValuesComboBox() {
        ComboboxValues comboboxValues = new ComboboxValues();
        comboboxValues.setLokaleCombobox(lokaleComboBox);
        comboboxValues.setKontaktpersonComboBox(kontaktpersonComboBox);
        comboboxValues.setArrangementComboBox(arrangementComboBox);
    }

    @FXML
    public void sokArrangement(){
        FilteredList<Arrangement> filteredList = new FilteredList<>(sokArgList, e -> true);
        sokArgFelt.setOnKeyReleased(e->{
            sokArgFelt.textProperty().addListener(((observableVerdi, gammel, ny) ->{
                filteredList.setPredicate((Predicate<? super Arrangement>) arrangement ->{
                    if (ny == null || ny.isEmpty()){
                        return true;
                    }
                    String lowercaseFilter = ny.toLowerCase();
                    if (arrangement.getNavn().toLowerCase().contains(lowercaseFilter) ||
                            arrangement.getDato().toString().toLowerCase().contains(lowercaseFilter) ||
                            arrangement.getLokale().toString().toLowerCase().contains(lowercaseFilter) ||
                            arrangement.getKontaktperson().toString().toLowerCase().contains(lowercaseFilter) ||
                            arrangement.getArtist().toLowerCase().contains(lowercaseFilter) ||
                            arrangement.getProgram().toLowerCase().contains(lowercaseFilter)
                    ){
                        return true;
                    }
                    return false;
                });

            } ));

            SortedList<Arrangement> sortertData = new SortedList<>(filteredList);
            sortertData.comparatorProperty().bind(arrangementTableView.comparatorProperty());
            arrangementTableView.setItems(sortertData);
        });
    }

    @FXML
    public void sokKontaktPerson() {
        FilteredList<Kontaktperson> filteredList = new FilteredList<>(sokKonList, e -> true);
        sokKonFelt.setOnKeyReleased(e->{
            sokKonFelt.textProperty().addListener(((observableVerdi, gammel, ny) ->{
                filteredList.setPredicate((Predicate<? super Kontaktperson>) kontaktperson ->{
                    if (ny == null || ny.isEmpty()){
                        return true;
                    }
                    String lowercaseFilter = ny.toLowerCase();
                    if (kontaktperson.getFornavn().toLowerCase().contains(lowercaseFilter) ||
                            kontaktperson.getEtternavn().toLowerCase().contains(lowercaseFilter) ||
                            kontaktperson.getEmail().toLowerCase().contains(lowercaseFilter) ||
                            kontaktperson.getTelefonnummer().toLowerCase().contains(lowercaseFilter) ||
                            kontaktperson.getVirksomhet().toLowerCase().contains(lowercaseFilter) ||
                            kontaktperson.getNettside().toLowerCase().contains(lowercaseFilter)){
                        return true;
                    }
                    return false;
                });

            } ));

            SortedList<Kontaktperson> sortertData = new SortedList<>(filteredList);
            sortertData.comparatorProperty().bind(kontaktpersonTableView.comparatorProperty());
            kontaktpersonTableView.setItems(sortertData);
        });

    }

    @FXML
    public void sokBillett(){
        FilteredList<Billett> filteredList = new FilteredList<>(sokBillList, e -> true);
        sokBillFelt.setOnKeyReleased(e->{
            sokBillFelt.textProperty().addListener(((observableVerdi, gammel, ny) ->{
                filteredList.setPredicate((Predicate<? super Billett>) billett ->{
                    if (ny == null || ny.isEmpty()){
                        return true;
                    }
                    String lowercaseFilter = ny.toLowerCase();
                    if (billett.getArrangement().toString().toLowerCase().contains(lowercaseFilter) ||
                            ny.equals(billett.getPris())|| ny.equals(billett.getPlass()) ||
                            billett.getKjoperTlf().toLowerCase().contains(lowercaseFilter)){
                        return true;
                    }
                    return false;
                });

            } ));

            SortedList<Billett> sortertData = new SortedList<>(filteredList);
            sortertData.comparatorProperty().bind(billettTableView.comparatorProperty());
            billettTableView.setItems(sortertData);
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listLokaler();
        setValuesComboBox();

        //Gjør datoer bak i tid utilgjengelige i datepicker
        datoTxtField.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate idag = LocalDate.now();

                setDisable(empty || date.compareTo(idag) < 0 );
            }
        });

        //Arrangement
        navnCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) a -> {
            if (a.getValue() != null) {
                return new SimpleStringProperty(a.getValue().getNavn());
            } else {
                return new SimpleStringProperty("Uten navn");
            }
        });

        lokaleCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) l -> {
            if (l.getValue() != null) {
                return new SimpleStringProperty(l.getValue().getLokale().toString());
            } else {
                return new SimpleStringProperty("Uten lokale");
            }
        });

        datoCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) a -> {
            if (a.getValue() != null) {
                return new SimpleStringProperty(String.valueOf(a.getValue().getDato()));
            } else {
                return new SimpleStringProperty("Uten dato");
            }
        });

        varighetCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) a -> {
            if (a.getValue() != null) {
                return new SimpleStringProperty(String.valueOf(a.getValue().getVarighet()));
            } else {
                return new SimpleStringProperty("Uten varighet");
            }
        });

        kontaktpersonCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) k -> {
            if (k.getValue() != null) {
                return new SimpleStringProperty(k.getValue().getKontaktperson().toString());
            } else {
                return new SimpleStringProperty("Uten kontaktperson");
            }
        });

        utsolgtCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) a -> {
            if (a.getValue() != null) {
                return new SimpleStringProperty(a.getValue().checkUtsolgt());
            } else {
                return new SimpleStringProperty("Uten kontaktperson");
            }
        });

        artisterCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) a -> {
            if (a.getValue() != null) {
                return new SimpleStringProperty(a.getValue().getArtist());
            } else {
                return new SimpleStringProperty("Uten artister");
            }
        });

        programCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) a -> {
            if (a.getValue() != null) {
                return new SimpleStringProperty(a.getValue().getProgram());
            } else {
                return new SimpleStringProperty("Uten program");
            }
        });

        antallPlasserCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Arrangement, String>, ObservableValue<String>>) l -> {
            if (l.getValue() != null) {
                return new SimpleStringProperty(String.valueOf(l.getValue().getLokale().getAntallPlasser()));
            } else {
                return new SimpleStringProperty("Uten antall plasser");
            }
        });

        //Billett
        billettArrCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Billett, String>, ObservableValue<String>>) a -> {
            if (a.getValue() != null) {
                return new SimpleStringProperty(a.getValue().getArrangement().getNavn());
            } else {
                return new SimpleStringProperty("Uten navn");
            }
        });

        billettPrisCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Billett, String>, ObservableValue<String>>) b -> {
            if (b.getValue() != null) {
                return new SimpleStringProperty(String.valueOf(b.getValue().getPris()));
            } else {
                return new SimpleStringProperty("Uten pris");
            }
        });

        billettPlassCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Billett, String>, ObservableValue<String>>) b -> {
            if (b.getValue() != null) {
                return new SimpleStringProperty(String.valueOf(b.getValue().getPlass()));
            } else {
                return new SimpleStringProperty("Uten plass");
            }
        });

        billettTlfCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Billett, String>, ObservableValue<String>>) b -> {
            if (b.getValue() != null) {
                return new SimpleStringProperty(b.getValue().getKjoperTlf());
            } else {
                return new SimpleStringProperty("Uten telefonnummer");
            }
        });
    }
}