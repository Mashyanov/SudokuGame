package sudoku;
 
import sudoku.languages.LanguageListener;
import java.awt.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sudoku.languages.*;


public class MyLocalisation {
    private static MyLocalisation localisation = null;
    
    public final static int RUSSIAN = 0;
    public final static int ENGLISH = 1;
    public final static int ITALIAN = 2;
    private Language language;
    private ArrayList<LanguageListener> listeners = new ArrayList<>();
   
    private MyLocalisation(Language language) {   this.language = language;    }
    
    public static final MyLocalisation getInstance(){
        if(localisation==null) localisation = new MyLocalisation(new LanguageRussian());
        return localisation;
    }
    
    public final void addLanguageListener(LanguageListener listener){
        listeners.add(listener);
    }
    
    public final void fireLanguageChanged(){
        for (LanguageListener listener : listeners) {
            listener.languageChanged();
        }
    }
        
    public Language getLanguage() {   return language;    }
        
    public void setLanguage(Language language) {
        this.language = language;
        fireLanguageChanged();
        }

    public void setLanguage(int language) {
        switch (language){
            case RUSSIAN:
                this.language = new LanguageRussian(); 
                break;
            case ENGLISH:
                this.language = new LanguageEnglish(); 
                break;
            case ITALIAN:
                this.language = new LanguageItalian();
                break;
        }
        fireLanguageChanged();
    }
    /*OPTIONS WINDOW*/
    
    private final String languageStringRus = "";
    private final String languageStringEng = "";
    
    private final String aboutStringRus = "<html><b>Игра Судоку</b> автор - Александр Машьянов<br><b>E-mail:</b> mashyanov1987@gmail.com<br><b>версия 1.1</b>(январь 2019-ого)</html>";
    
    
    private final String cancelButtonRus = "Отмена";
    private final String cancelButtonEng = "Cancel";
    
    private final String saveButtonRus = "";
    private final String saveButtonEng = "<html><b>Save</b></html>";
    
    private final String newGameRus = "<html><b>New Game</b></html>";
    private final String newGameEng = "<html><b>New Game</b></html>";
    
    private final String exitRus = "<html><b>New Game</b></html>";
    private final String exitGameEng = "Exit";
    
    private final String checkButtonRus = "Check it!";
    private final String checkButtonEng = "Check it!";

    
        
//

   

}
