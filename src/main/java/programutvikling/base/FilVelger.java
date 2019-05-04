package programutvikling.base;

import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import programutvikling.controllers.MainController;

import java.io.File;
import java.io.IOException;

public class FilVelger {
    AlertBoxes alert = new AlertBoxes();

    public File addKonFileChooser(ObservableList<Kontaktperson> kontaktpersoner, AnchorPane anchorPane) throws IOException {
        MainController main = new MainController();
        if (kontaktpersoner.isEmpty()) {
            alert.info("Ingen elementer", "Kan ikke lagre en tom liste til fil", "Du må legge til elementer i listen før du lagrer");
        }
        else {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Lagre kontaktpersoner til fil");
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            fileChooser.setInitialFileName("kontaktpersoner");
            fileChooser.setInitialDirectory(new File("src/main/filer"));

            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("csv file", "*.csv"),
                    new FileChooser.ExtensionFilter("jobj file", "*.jobj"));

            File file = fileChooser.showSaveDialog(stage);
            return file;
        }
        return null;
    }

    public File addArrFileChooser(ObservableList<Arrangement> arrangementer, AnchorPane anchorPane){
        MainController main = new MainController();
        if (arrangementer.isEmpty()) {
            alert.info("Ingen elementer", "Kan ikke lagre en tom liste til fil", "Du må legge til elementer i listen før du lagrer");
        }
        else {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Lagre arrangementer til fil");
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            fileChooser.setInitialFileName("arrangementer");
            fileChooser.setInitialDirectory(new File("src/main/filer"));

            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("csv file", "*.csv"),
                    new FileChooser.ExtensionFilter("jobj file", "*.jobj"));

            File file = fileChooser.showSaveDialog(stage);
            return file;
        }
        return null;
    }


    public File addBillettFileChooser(ObservableList<Billett> billetter, AnchorPane anchorPane){
        MainController main = new MainController();
        if (billetter.isEmpty()) {
            alert.info("Ingen elementer", "Kan ikke lagre en tom liste til fil", "Du må legge til elementer i listen før du lagrer");
        }
        else {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Lagre billetter til fil");
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            fileChooser.setInitialFileName("billetter");
            fileChooser.setInitialDirectory(new File("src/main/filer"));

            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("csv file", "*.csv"),
                    new FileChooser.ExtensionFilter("jobj file", "*.jobj"));

            File file = fileChooser.showSaveDialog(stage);
            return file;
        }
        return null;
    }

    public File openFileChooser(AnchorPane anchorPane, String element) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Åpne fil med " + element);
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        fileChooser.setInitialDirectory(new File("src/main/filer"));

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("csv file", "*.csv"),
                new FileChooser.ExtensionFilter("jobj file", "*.jobj"));

        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
}
