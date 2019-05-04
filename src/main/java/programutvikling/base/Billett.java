package programutvikling.base;

import programutvikling.base.filehandling.ObservableHelper;
import programutvikling.base.filehandling.Observer;
import programutvikling.base.filehandling.Observerbar;

import java.io.Serializable;

public class Billett implements Serializable, Observerbar {
    private static final long serialVersionUID = 1;
    private ObservableHelper observableHandler = new ObservableHelper();
    private Arrangement arrangement;
    private int pris;
    private int plass;
    private String kjoperTlf;

    public Billett(Arrangement arrangement, int pris, int plass, String kjoperTlf) {
        this.arrangement = arrangement;
        this.pris = pris;
        this.plass = plass;
        this.kjoperTlf = kjoperTlf;
    }

    public Arrangement getArrangement() {
        return arrangement;
    }
    public void setArrangement(Arrangement arrangement) {
        this.arrangement = arrangement;
    }
    public int getPris() {
        return pris;
    }
    public int getPlass() {
        return plass;
    }
    public String getKjoperTlf() {
        return kjoperTlf;
    }


    @Override
    public String toString() {
        return "Billett{" +
                "arrangement=" + arrangement +
                ", pris=" + pris +
                ", plass=" + plass +
                ", kjoperTlf='" + kjoperTlf + '\'' +
                '}';
    }

    @Override
    public void observe(Observer o) {
        observableHandler.add(o);
    }
}
