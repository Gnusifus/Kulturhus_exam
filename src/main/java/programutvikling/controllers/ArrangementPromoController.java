package programutvikling.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import programutvikling.base.Arrangement;

public class ArrangementPromoController{
    private Arrangement showArrangement;

    @FXML Label arrangementTittel;
    @FXML Text arrangementText;

    public void setArrangement(Arrangement arrangement){
        showArrangement = arrangement;
        setContent();
    }

    public void setContent(){
        arrangementTittel.setText(showArrangement.getNavn());
        arrangementText.setText(showArrangement.toPromo());
    }

}
