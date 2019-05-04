package programutvikling.base;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import programutvikling.base.filehandling.CsvReader;
import programutvikling.base.filehandling.JobjReader;

import java.io.File;

public class ReaderThread extends Task<ObservableList<?>> {
    private Runnable runMeWhenDone;
    private final String type;
    private final File file;

    public ReaderThread(Runnable doneFunc, String type, File file) {
        this.runMeWhenDone = doneFunc;
        this.type = type;
        this.file = file;
    }

    @Override
    protected ObservableList<?> call() throws Exception {
        ObservableList<?> list = null;
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        try {
            if(fileExtension.toLowerCase().equals(".csv")){
                CsvReader csvReader = new CsvReader();
                if(type.toLowerCase().equals("kontaktperson")){
                    list = csvReader.kontaktpersonRead(file.getPath());
                }
                if(type.toLowerCase().equals("arrangement")){
                    list = csvReader.arrangementRead(file.getPath());
                }
                if(type.toLowerCase().equals("billett")){
                    list = csvReader.billettRead(file.getPath());
                }
            }
            if(fileExtension.toLowerCase().equals(".jobj")){
                JobjReader jobjReader = new JobjReader();
                if(type.toLowerCase().equals("kontaktperson")){
                    list = jobjReader.kontaktpersonRead(file.getPath());
                }
                if(type.toLowerCase().equals("arrangement")){
                    list = jobjReader.arrangementRead(file.getPath());
                }
                if(type.toLowerCase().equals("billett")){
                    list = jobjReader.billettRead(file.getPath());
                }
            }
            for (int p = 0; p < 100; p++) {
                Thread.sleep(50);
                updateProgress(p, 100);
            }
        } catch (InterruptedException e) {
            System.out.println("Tråd avbrutt");
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void succeeded() {
        if( runMeWhenDone != null ) {
            runMeWhenDone.run();
        }
    }
}