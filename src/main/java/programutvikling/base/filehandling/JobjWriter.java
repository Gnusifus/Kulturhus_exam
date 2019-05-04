package programutvikling.base.filehandling;

import javafx.collections.ObservableList;
import programutvikling.base.Arrangement;
import programutvikling.base.Billett;
import programutvikling.base.Kontaktperson;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class JobjWriter implements WriteFiles {
    @Override
    public void kontaktpersonWrite(ObservableList<Kontaktperson> kontaktpersonList, File file) throws IOException {
        Path filepath = file.toPath();
        OutputStream fos = Files.newOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(new ArrayList<Kontaktperson>(kontaktpersonList));
        oos.close();
    }

    @Override
    public void arrangementWrite(ObservableList<Arrangement> arrangementList, File file) throws IOException {
        Path filepath = file.toPath();
        OutputStream fos = Files.newOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(new ArrayList<Arrangement>(arrangementList));
        oos.close();
    }

    @Override
    public void billettWrite(ObservableList<Billett> billettList, File file) throws IOException {
        Path filepath = file.toPath();
        OutputStream fos = Files.newOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(new ArrayList<Billett>(billettList));
        oos.close();
    }
}
