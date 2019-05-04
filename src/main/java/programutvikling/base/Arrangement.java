package programutvikling.base;

import programutvikling.base.filehandling.ObservableHelper;
import programutvikling.base.filehandling.Observer;
import programutvikling.base.filehandling.Observerbar;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Arrangement implements Serializable, Observerbar {
    private static final long serialVersionUID = 1;
    private ObservableHelper observableHandler = new ObservableHelper();
    private Lokale lokale;
    private Kontaktperson kontaktperson;
    private String navn;
    private LocalDate dato;
    private String artist;
    private int varighet;
    private String program;
    private Boolean utsolgt;

    public Arrangement(){};

    public Arrangement (String navn, LocalDate dato, String artist, int varighet, String program, Boolean utsolgt){
        this.navn = navn;
        this.dato = dato;
        this.artist = artist;
        this.varighet = varighet;
        this.program = program;
        this.utsolgt = utsolgt;
    }

    public Arrangement(Lokale lokale, Kontaktperson kontaktperson, String navn, LocalDate dato, String artist, int varighet, String program, Boolean utsolgt) {
        this.lokale = lokale;
        this.kontaktperson = kontaktperson;
        this.navn = navn;
        this.dato = dato;
        this.artist = artist;
        this.varighet = varighet;
        this.program = program;
        this.utsolgt = utsolgt;
    }

    public Lokale getLokale() {
        return lokale;
    }
    public Kontaktperson getKontaktperson() {
        return kontaktperson;
    }
    public void setKontaktperson(Kontaktperson kontaktperson) {
        this.kontaktperson = kontaktperson;
    }
    public String getNavn() {
        return navn;
    }
    public LocalDate getDato() {
        return dato;
    }
    public String getArtist() {
        return artist;
    }
    public int getVarighet() {
        return varighet;
    }
    public String getProgram() {
        return program;
    }
    public Boolean getUtsolgt() {
        return utsolgt;
    }

    public String checkUtsolgt(){
        if (utsolgt == true){
            return "Arrangementet er utsolgt";
        }
        else{
            return "Det er enda ledige billetter igjen";
        }
    }

    public String toPromo(){
        return navn + " arrangeres den " + dato + " på kulturhuset!\n\n" + "Program: " + program +
                "\n\n" + artist + " er blandt artisten(e) som kommer. \n\nLokale: " + lokale +
                "\n\nHar du spørsmål? Kontakt arrangementets kontaktperson:\n" + kontaktperson + "\n\n" +
                "Arrangementet har en estimert varighet på " + varighet + " minutter." + "\n\n" +
                checkUtsolgt() + "!\n\n";
    }

    @Override
    public String toString() {
        return navn + ", " + dato;
    }

    @Override
    public void observe(Observer o) {
        observableHandler.add(o);
    }
}
