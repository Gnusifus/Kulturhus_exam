package programutvikling.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programutvikling.base.filehandling.ObservableHelper;
import programutvikling.base.filehandling.Observer;
import programutvikling.base.filehandling.Observerbar;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Lokale implements Serializable, Observerbar {
    private static final long serialVersionUID = 1;
    private ObservableHelper observableHandler = new ObservableHelper();
    private String lokaleNavn;
    private String type;
    private int antallPlasser;

    public Lokale(String navn, String type, int antallPlasser) {
        this.lokaleNavn = navn;
        this.type = type;
        this.antallPlasser = antallPlasser;
    }

    public static ObservableList<Lokale> getLokale() throws IOException {
        Lokale lokale1 = new Lokale("Glitre", "Kinosal", 90);
        Lokale lokale2 = new Lokale("Superb", "Kinosal", 45);
        Lokale lokale3 = new Lokale("Klangen", "Teatersal", 150);
        Lokale lokale4 = new Lokale("Dovregubben", "Forsamlingssal", 70);

        List<Lokale> lokaleList = new ArrayList<>();
        lokaleList.add(lokale1);
        lokaleList.add(lokale2);
        lokaleList.add(lokale3);
        lokaleList.add(lokale4);

        ObservableList<Lokale> lokaleOut = FXCollections.observableArrayList(lokaleList);
        return lokaleOut;
    }

    public String getNavn() {
        return lokaleNavn;
    }

    public void setNavn(String navn) {
        this.lokaleNavn = navn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAntallPlasser() {
        return antallPlasser;
    }

    public void setAntallPlasser(int antallPlasser) {
        this.antallPlasser = antallPlasser;
    }

    @Override
    public String toString() {
        return type + " : " + lokaleNavn + " (" + antallPlasser + " plasser)";
    }

    @Override
    public void observe(Observer o) {
        observableHandler.add(o);
    }
}
