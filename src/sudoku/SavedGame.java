
package sudoku;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Stack;
/**Класс, описывающий сохраненную игру*/
public class SavedGame implements Serializable{
private final Stack <Move> moves;    
private final LinkedList<SudokuLabel> gameField;
private final long time;
private final int checkResultsCounter, selectedLabel;
private final boolean playerWasCheating, cheats, auxMode;

    public SavedGame(LinkedList<SudokuLabel> gameField, long time, int checkResultsCounter, 
            boolean cheats, boolean playerWasCheating, boolean auxMode, int selectedLabel, Stack moves) {
        this.gameField = gameField;
        this.time = time;
        this.checkResultsCounter = checkResultsCounter;
        this.playerWasCheating = playerWasCheating;
        this.cheats = cheats;
        this.auxMode = auxMode;
        this.selectedLabel = selectedLabel;
        this.moves = moves;
    }

    public LinkedList<SudokuLabel> getGameField() { return gameField;          }
    public long getTime()                         { return time;               }
    public int getCheckResultsCounter()           { return checkResultsCounter;}
    public boolean isPlayerWasCheating()          { return playerWasCheating;  }
    public boolean cheats()                       { return cheats;             }
    public boolean isAuxMode()                    { return auxMode;            }
    public int getSelectedLabel()                 { return selectedLabel;      }
    public Stack<Move> getMoves()                 { return moves;              }
    
    
}
