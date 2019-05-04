package programutvikling.base;

import javafx.scene.control.ComboBox;
import programutvikling.base.filehandling.CsvReader;
import programutvikling.base.filehandling.JobjReader;

import java.io.IOException;

import static programutvikling.base.Lokale.getLokale;

public class ComboboxValues {
    private String kontaktpersonerCsvPath = "src/main/filer/kontaktpersoner.csv";
    private String kontaktpersonerJobjPath ="src/main/filer/kontaktpersoner.jobj";

    private String arrangementCsvPath = "src/main/filer/arrangementer.csv";
    private String arrangementJobjPath ="src/main/filer/arrangementer.jobj";

    CsvReader csvReader = new CsvReader();
    JobjReader jobjReader = new JobjReader();

    public void setKontaktpersonComboBox(ComboBox<Kontaktperson> kontaktpersonComboBox) {
        kontaktpersonComboBox.getItems().clear();
        try {
            Kontaktperson kon;
                 int konTellerCsv = csvReader.kontaktpersonRead(kontaktpersonerCsvPath).size();
                 for (int i = 0; i < konTellerCsv; i++) {
                     kon = csvReader.kontaktpersonRead(kontaktpersonerCsvPath).get(i);
                     kontaktpersonComboBox.getItems().add(kon);
                 }

                int konTellerJobj = jobjReader.kontaktpersonRead(kontaktpersonerJobjPath).size();
                for (int i = 0; i < konTellerJobj; i++) {
                    kon = jobjReader.kontaktpersonRead(kontaktpersonerJobjPath).get(i);
                    kontaktpersonComboBox.getItems().add(kon);
                }
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setLokaleCombobox(ComboBox<Lokale> lokaleCombobox){
        lokaleCombobox.getItems().clear();
        try{
            for (Lokale lok : getLokale()){
                lokaleCombobox.getItems().add(lok);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setArrangementComboBox(ComboBox<Arrangement> arrangementComboBoxCsv){
        arrangementComboBoxCsv.getItems().clear();
        try {
            Arrangement arr;
            int arraTellerCsv = csvReader.arrangementRead(arrangementCsvPath).size();
            for (int i = 0; i < arraTellerCsv; i++) {
                arr = csvReader.arrangementRead(arrangementCsvPath).get(i);
                arrangementComboBoxCsv.getItems().add(arr);
            }

            int arraTellerJobj = jobjReader.arrangementRead(arrangementJobjPath).size();
            for (int i = 0; i < arraTellerJobj; i++) {
                arr = jobjReader.arrangementRead(arrangementJobjPath).get(i);
                arrangementComboBoxCsv.getItems().add(arr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
