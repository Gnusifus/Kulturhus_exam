package programutvikling.base.filehandling;

import javafx.collections.ObservableList;
import programutvikling.base.Arrangement;
import programutvikling.base.Billett;
import programutvikling.base.Kontaktperson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter implements WriteFiles {
    private static final String devider = ",";
    private static final String newLine = "\n";

    private static final String kontaktHeader = "Fornavn, Etternavn, E-post, Telefonnummer, Virksomhet, Nettside, Opplysninger";
    @Override
    public void kontaktpersonWrite(ObservableList<Kontaktperson> kontaktpersonList, File file) throws IOException {
        FileWriter writer = null;
        try{
            writer = new FileWriter(file);
            writer.write(kontaktHeader + newLine);
            for(Kontaktperson kontaktpers : kontaktpersonList){
                writer.append(kontaktpers.getFornavn());
                writer.append(devider);
                writer.append(kontaktpers.getEtternavn());
                writer.append(devider);
                writer.append(kontaktpers.getEmail());
                writer.append(devider);
                writer.append(kontaktpers.getTelefonnummer());
                writer.append(devider);
                writer.append(kontaktpers.getVirksomhet());
                writer.append(devider);
                writer.append(kontaktpers.getNettside());
                writer.append(devider);
                writer.append(kontaktpers.getOpplysninger());
                writer.append(newLine);
            }
        }

        finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("Error, kunne ikke lukke filen");
                e.printStackTrace();
            }
        }
    }

    private static final String arrHeader = "Lokale_Navn, Lokale_Type, Lokale_Plasser, " +
                                            "Kontaktperson_Fornavn, Kontaktperson_Etternavn, Kontaktperson_Email, Kontaktperson_Telefonnummer, " +
                                            "Kontaktperson_Virksomhet, Kontaktperson_Nettside, Kontaktperson_Opplysninger, Navn, Dato, Artist, Varighet, Program, Utsolgt";
    @Override
    public void arrangementWrite(ObservableList<Arrangement> arrangementList, File file) throws IOException {
        FileWriter writer = null;

        try{
            writer = new FileWriter(file);
            writer.write(arrHeader + newLine);
            for(Arrangement arr : arrangementList){
                String artisterUtenKomma = arr.getArtist().replace(",",";");

                //Lokale
                writer.append(arr.getLokale().getNavn());
                writer.append(devider);
                writer.append(arr.getLokale().getType());
                writer.append(devider);
                writer.append(String.valueOf(arr.getLokale().getAntallPlasser()));
                writer.append(devider);
                //Kontaktperson
                writer.append(arr.getKontaktperson().getFornavn());
                writer.append(devider);
                writer.append(arr.getKontaktperson().getEtternavn());
                writer.append(devider);
                writer.append(arr.getKontaktperson().getEmail());
                writer.append(devider);
                writer.append(arr.getKontaktperson().getTelefonnummer());
                writer.append(devider);
                writer.append(arr.getKontaktperson().getVirksomhet());
                writer.append(devider);
                writer.append(arr.getKontaktperson().getNettside());
                writer.append(devider);
                writer.append(arr.getKontaktperson().getOpplysninger());
                writer.append(devider);
                //Arrangement
                writer.append(arr.getNavn());
                writer.append(devider);
                writer.append(arr.getDato().toString());
                writer.append(devider);
                writer.append(artisterUtenKomma);
                writer.append(devider);
                writer.append(String.valueOf(arr.getVarighet()));
                writer.append(devider);
                writer.append(arr.getProgram());
                writer.append(devider);
                writer.append(arr.getUtsolgt().toString());
                writer.append(newLine);
            }
        }
        finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("Error, kunne ikke lukke filen");
                e.printStackTrace();
            }
        }
    }

    private static final String billettHeader = "Arrangement_Navn, Arrangement_Dato, Arrangement_Artist, Arrangement_Varighet, " +
                                                "Arrangement_Program, Arrangement_Utsolgt, Pris, Plass, Kjoper_telefonnummer";
    @Override
    public void billettWrite(ObservableList<Billett> billettList, File file) throws IOException {
        FileWriter writer = null;

        try{
            writer = new FileWriter(file);
            writer.write(billettHeader + newLine);
            for(Billett bill : billettList){
                //Arrangement
                writer.append(bill.getArrangement().getNavn());
                writer.append(devider);
                writer.append(bill.getArrangement().getDato().toString());
                writer.append(devider);
                writer.append(bill.getArrangement().getArtist());
                writer.append(devider);
                writer.append(String.valueOf(bill.getArrangement().getVarighet()));
                writer.append(devider);
                writer.append(bill.getArrangement().getProgram());
                writer.append(devider);
                writer.append(bill.getArrangement().getUtsolgt().toString());
                writer.append(devider);
                //Billett
                writer.append(String.valueOf(bill.getPris()));
                writer.append(devider);
                writer.append(String.valueOf(bill.getPlass()));
                writer.append(devider);
                writer.append(bill.getKjoperTlf());
                writer.append(newLine);
            }
        }
        finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("Error, kunne ikke lukke filen");
                e.printStackTrace();
            }
        }
    }
}
