package programutvikling.base;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import programutvikling.base.exceptions.InvalidEmailException;
import programutvikling.base.exceptions.InvalidNameException;
import programutvikling.base.exceptions.InvalidNumberException;
import programutvikling.base.exceptions.InvalidUrlException;

public class ValideringHandler  {


    public static boolean telefonValidering(TextField inputTextField, Label inputLabel, String validationText) throws InvalidNumberException{
        boolean isDataLength = true;
        String validationString = null;

        if (!inputTextField.getText().matches( "[0-9]{8}")) {
            isDataLength = false;
            validationString = validationText;
            try {
                throw new InvalidNumberException("Telefon nummer må inneholder 8 siffer");
            } catch (InvalidNumberException e) {
                //e.printStackTrace();
            }

        }else{
            validationString="";
        }
        inputLabel.setText(validationString);
        return isDataLength;

    }


    public static boolean tekstAlfabet(TextField inputTextField, Label inputLabel, String validationText) throws InvalidNameException {
        boolean isAlphabet = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[a-zA-ZæøåÆØÅ]+")) {
            isAlphabet = false;
            validationString = validationText;
            try {
                throw new InvalidNameException("må inneholde kun tekst.");
            } catch (InvalidNameException e) {
                e.printStackTrace();
            }

        }
        inputLabel.setText(validationString);

        System.out.println(inputTextField.getText().matches("[a-zA-ZæøåÆØÅ]+"));
        return isAlphabet;

    }

    public static boolean emailFormat(TextField inputTextField, Label inputLabel, String validationText) throws InvalidEmailException {
        boolean isEmail = true;
        String validationString = null;

        if (!inputTextField.getText().matches("^\\w?([a-zA-ZæøåÆØÅ])+([.-]?\\w+)*@\\w?([a-zA-ZæøåÆØÅ])+([.-]?\\w+)*(\\.\\w{2,3})+$")) {
            isEmail = false;
            validationString = validationText;
            try {
                throw new InvalidEmailException("email er ikke skrevet inn korrekt");
            } catch (InvalidEmailException e) {
                //e.printStackTrace();
            }

        }
        inputLabel.setText(validationString);
        return isEmail;

    }

    public static boolean nettsideFormat(TextField inputTextField, Label inputLabel, String validationText) throws InvalidUrlException{
        boolean nettsideKorrekt = true;
        String validationString = null;

        if (!inputTextField.getText().matches("^(https:\\/\\/www\\.|https:\\/\\/)?[a-zA-Z0-9]+([\\-\\.]{1}[a-zA-Z0-9]+)*\\.[a-zA-Z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")) {
            nettsideKorrekt = false;
            validationString = validationText;
            try {
                throw new InvalidUrlException("Nettside er ikke skrevet inn korrekt.");
            } catch (InvalidUrlException e) {
                //e.printStackTrace();
            }

        }
        inputLabel.setText(validationString);
        return nettsideKorrekt;

    }

    public static boolean tall(TextField inputTextField, Label inputLabel, String validationText) {
        boolean tall = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[0-9]+")) {
            tall = false;
            validationString = validationText;

        }
        inputLabel.setText(validationString);

        return tall;

    }
}
