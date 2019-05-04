package programutvikling.base;

import programutvikling.base.filehandling.ObservableHelper;
import programutvikling.base.filehandling.Observer;
import programutvikling.base.filehandling.Observerbar;

import java.io.Serializable;

public class Kontaktperson implements Serializable, Observerbar {
    private static final long serialVersionUID = 1;
    private ObservableHelper observableHandler = new ObservableHelper();
    private String fornavn;
    private String etternavn;
    private String email;
    private String telefonnummer;
    private String virksomhet;
    private String nettside;
    private String opplysninger;

    public Kontaktperson(String fornavn, String etternavn, String email, String telefonnummer, String virksomhet, String nettside, String opplysninger) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.email = email;
        this.telefonnummer = telefonnummer;
        this.virksomhet = virksomhet;
        this.nettside = nettside;
        this.opplysninger = opplysninger;
    }


    public Kontaktperson() {}

    public String getFornavn() {
        return fornavn;
    }
    public String getEtternavn() {
        return etternavn;
    }
    public String getEmail() {
        return email;
    }
    public String getTelefonnummer() {
        return telefonnummer;
    }
    public String getVirksomhet() {
        return virksomhet;
    }
    public String getNettside() {
        return nettside;
    }
    public String getOpplysninger() {
        return opplysninger;
    }

    @Override
    public String toString() {
        return fornavn + " " + etternavn + ", tlf. " + telefonnummer;
    }

    @Override
    public void observe(Observer o) {
        observableHandler.add(o);
    }

}
