package programutvikling.base.filehandling;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programutvikling.base.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvReader implements ReadFiles {
    private static final String devider = ",";

    @Override
    public ObservableList<Kontaktperson> kontaktpersonRead(String path) throws IOException, UnsupportedOperationException, ClassNotFoundException {
        List<Kontaktperson> kontaktpersonList = new ArrayList<>();
        BufferedReader reader = null;

        try{
            String line = "";

            reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();

            //Sjekker at header matcher ønsket objekt.
            if(!line.isEmpty() && !line.startsWith("Fornavn" + devider)) {
                throw new UnsupportedOperationException("Fant ingen kontaktpersoner i filen, prøv en annen fil");
            }

            while((line = reader.readLine()) != null){
                String[] kontaktpersonElement = line.split(devider);
                if (kontaktpersonElement.length > 1){
                    Kontaktperson kontaktperson = new Kontaktperson(kontaktpersonElement[0], //Fornavn
                                                                    kontaktpersonElement[1], //Etternavn
                                                                    kontaktpersonElement[2], //Email
                                                                    kontaktpersonElement[3], //Telefonnummer
                                                                    kontaktpersonElement[4], //Virksomhet
                                                                    kontaktpersonElement[5], //Nettside
                                                                    kontaktpersonElement[6]); //Opplysninger

                    kontaktpersonList.add(kontaktperson);
                }
            }
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Kunne ikke lukke filen");
                e.printStackTrace();
            }
        }
        ObservableList<Kontaktperson> kontaktpersonOut = FXCollections.observableArrayList(kontaktpersonList);
        return kontaktpersonOut;
    }

    @Override
    public ObservableList<Arrangement> arrangementRead(String path) throws UnsupportedOperationException, IOException {
        List<Arrangement> arrangementList = new ArrayList<>();
        BufferedReader reader = null;

        try{
            String line = "";

            reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();

            //Sjekker at header matcher ønsket objekt.
            if(!line.isEmpty() && !line.startsWith("Lokale_Navn" + devider)) {
                throw new UnsupportedOperationException("Fant ingen arrangementer i filen, prøv en annen fil");
            }

            while((line = reader.readLine()) != null){
                String[] arrangementElement = line.split(devider);
                if (arrangementElement.length > 0){
                    Lokale lokale = new Lokale(arrangementElement[0], //LoaleNavn
                                                arrangementElement[1], //Type
                                                Integer.parseInt(arrangementElement[2])); //Antall

                    Kontaktperson kontaktperson = new Kontaktperson(arrangementElement[3], arrangementElement[4], arrangementElement[5],
                                                                    arrangementElement[6], arrangementElement[7], arrangementElement[8],
                                                                    arrangementElement[9]);

                    Arrangement arrangement = new Arrangement(lokale, kontaktperson, arrangementElement[10], LocalDate.parse(arrangementElement[11]),
                                                                arrangementElement[12].replace(";", ","), Integer.parseInt(arrangementElement[13]), arrangementElement[14],
                                                                Boolean.parseBoolean(arrangementElement[15]));
                    arrangementList.add(arrangement);
                }
            }
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Kunne ikke lukke filen");
                e.printStackTrace();
            }
        }
        ObservableList<Arrangement> outArrangement = FXCollections.observableArrayList(arrangementList);
        return outArrangement;
    }

    @Override
    public ObservableList<Billett> billettRead(String path) throws IOException, UnsupportedOperationException {
        List<Billett> billettList = new ArrayList<>();
        BufferedReader reader = null;

        try{
            String line = "";

            reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();

            //Sjekker at header matcher ønsket objekt.
            if(!line.isEmpty() && !line.startsWith("Arrangement_Navn" + devider)) {
                throw new UnsupportedOperationException("Fant ingen billetter i filen, prøv en annen fil");
            }

            while((line = reader.readLine()) != null){
                String[] billettElement = line.split(devider);
                if (billettElement.length > 1){

                    Arrangement arrangement = new Arrangement(billettElement[0], //Navn
                                                            LocalDate.parse(billettElement[1]), //Dato
                                                            billettElement[2], //Artist
                                                            Integer.parseInt(billettElement[3]), //Varighet
                                                            billettElement[4], //Program
                                                            Boolean.parseBoolean(billettElement[5])); //Utsolgt

                    Billett billett = new Billett(arrangement, //Arrangmenet
                                                Integer.parseInt(billettElement[6]), //Pris
                                                Integer.parseInt(billettElement[7]), //Plass
                                                billettElement[8]); //Kjoper telefonnummer
                    billettList.add(billett);
                }
            }
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Kunne ikke lukke filen");
                e.printStackTrace();
            }
        }
        ObservableList<Billett> billettOut = FXCollections.observableArrayList(billettList);
        return billettOut;
    }
}
