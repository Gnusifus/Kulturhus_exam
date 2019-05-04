package programutvikling.base.filehandling;

import javafx.collections.ObservableList;
import programutvikling.base.Arrangement;
import programutvikling.base.Billett;
import programutvikling.base.Kontaktperson;

import java.io.IOException;

public interface ReadFiles {
    //Kontaktperson
    ObservableList<Kontaktperson> kontaktpersonRead(String path) throws IOException, ClassNotFoundException;

    //Arrangement
    ObservableList<Arrangement> arrangementRead(String path) throws IOException;

    //Billett
    ObservableList<Billett> billettRead(String path) throws IOException;
}
