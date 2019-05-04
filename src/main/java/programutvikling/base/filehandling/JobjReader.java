package programutvikling.base.filehandling;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import programutvikling.base.Arrangement;
import programutvikling.base.Billett;
import programutvikling.base.Kontaktperson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JobjReader implements ReadFiles {
    JobjWriter jobjWriter = new JobjWriter();
    @Override
    public ObservableList<Kontaktperson> kontaktpersonRead(String path) throws IOException, ClassNotFoundException {
        Path filepath = Paths.get(path);
        try {
            InputStream in = Files.newInputStream(filepath);
            FileInputStream fin = new FileInputStream(filepath.toFile());
            ObjectInputStream ois = new ObjectInputStream(fin);
            List<Kontaktperson> list = (List<Kontaktperson>) ois.readObject();

            return FXCollections.observableList(list);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            if (e.getClass().equals(EOFException.class)){
                jobjWriter.kontaktpersonWrite(FXCollections.emptyObservableList(), filepath.toFile());
            }else {
                e.printStackTrace();
            }
        }
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<Arrangement> arrangementRead(String path) throws IOException {
        Path filepath = Paths.get(path);
        try {
            InputStream in = Files.newInputStream(filepath);
            FileInputStream fin = new FileInputStream(filepath.toFile());
            ObjectInputStream ois = new ObjectInputStream(fin);
            List<Arrangement> list = (List<Arrangement>) ois.readObject();

            return FXCollections.observableList(list);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            if (e.getClass().equals(EOFException.class)){
                jobjWriter.arrangementWrite(FXCollections.emptyObservableList(), filepath.toFile());
            }else {
                e.printStackTrace();
            }
        }
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<Billett> billettRead(String path) throws IOException {
        Path filepath = Paths.get(path);
        try {
            InputStream in = Files.newInputStream(filepath);
            FileInputStream fin = new FileInputStream(filepath.toFile());
            ObjectInputStream ois = new ObjectInputStream(fin);
            List<Billett> list = (List<Billett>) ois.readObject();

            return FXCollections.observableList(list);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            if (e.getClass().equals(EOFException.class)){
                jobjWriter.billettWrite(FXCollections.emptyObservableList(), filepath.toFile());
            }else {
                e.printStackTrace();
            }
        }
        return FXCollections.emptyObservableList();
    }
}
