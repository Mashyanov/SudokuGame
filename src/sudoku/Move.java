package sudoku;

import java.io.Serializable;

/**Класс, описывающий ход в игре*/
public class Move implements Serializable{
    /**Изменяемая этим ходом SudokuLabel*/
    private final SudokuLabel label;
    /**Строка с основным значением клетки*/
    private final String mainString;
    /**Строка со вспомогательным занчением клетки*/
    private final String auxString;
    /**FALSE если ход менял основное значение, TRUE - если вспомогательное*/
    private final boolean auxMove;

    public Move(SudokuLabel label, String value, boolean auxMove) {
        this.label = label;
        this.auxMove = auxMove;
        value = value.replaceFirst("<html>", "");
        value = value.replaceFirst("</html>", "");
        value = value.replaceAll(" ", "");
        value = value.replaceAll("<br>", "");
        if(!auxMove){
            this.mainString = value;
            this.auxString  = null;
        }
        else{
            this.mainString = null;
            this.auxString  = value;
        }
    }

    public boolean isAuxMove() {
        return auxMove;
    }
        
    public SudokuLabel getLabel() {
        return label;
    }

    public String getValue() {
        if(!auxMove) return mainString;
        return auxString;
    }

    @Override
    public String toString() {
        return " mainString=" + mainString + ", auxString=" + auxString + ", auxMove=" + auxMove + '}';
    }
    
}
