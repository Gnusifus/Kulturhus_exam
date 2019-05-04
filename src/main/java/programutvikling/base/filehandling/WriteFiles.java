package programutvikling.base.filehandling;

import javafx.collections.ObservableList;
import programutvikling.base.Arrangement;
import programutvikling.base.Billett;
import programutvikling.base.Kontaktperson;

import java.io.File;
import java.io.IOException;

public interface WriteFiles {
    //Kontaktperson
    void kontaktpersonWrite(ObservableList<Kontaktperson> kontaktpersonList, File file) throws IOException;

    //Arrangement
    void arrangementWrite(ObservableList<Arrangement> arrangementList, File file) throws IOException;

    //Billett
    void billettWrite(ObservableList<Billett> billettList, File file) throws IOException;
}
