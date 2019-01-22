
package sudoku;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Options implements Serializable{
    private static Options OPTIONS = null;
    public final int MAX_RESULTS = 15;
    private final File optionsFile = new File("options.dat");
    private boolean autoCheck, blockedSelectable, darkerBackground;
    private Color colorMain, colorAux;
    /** Хранилище системных настроек<br>
     * <table {border: 1px solid grey;}>
     * <tr><b>№ строки</b> <th> Значение</th></tr>
     * <tr><center>0</center><td> Предлагать проверка заполненного поля       (TRUE/FALSE)  </td></tr>
     * <tr><center>1</center><td> Возможность выбирать заблокированные клетки (TRUE/FALSE)  </td></tr>
     * <tr><center>2</center><td> Фон заблокированных клеток темнее           (TRUE/FALSE)  </td></tr>
     * <tr><center>3</center><td> RGB-код цвета основного курсора                           </td></tr>
     * <tr><center>4</center><td> RGB-код цвета курсора-шпаргалки                           </td></tr>
     * <tr><center>5</center><td> Версия локализации               (0(for Rus)/1(for Eng))  </td></tr>
     * </table>*/
    private ArrayList<String> data;
    
    private Options() {
        try {
        
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(optionsFile));
                data = (ArrayList<String>) ois.readObject();
                ois.close();
                for (String string : data) {
                    System.out.println(string);
            }
                
                autoCheck         = data.get(0).equals("true");
                blockedSelectable = data.get(1).equals("true");
                darkerBackground  = data.get(2).equals("true");
                colorMain = new Color(Integer.parseInt(data.get(3)));
                colorAux  = new Color(Integer.parseInt(data.get(4)));
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }}
    
    public void saveToFile(){
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(optionsFile));
            oos.writeObject(data);
            oos.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }}
    
    public static final Options getInstance(){
        if (OPTIONS == null) OPTIONS = new Options();
        return OPTIONS;
    }

    public void setAutoCheck(boolean autoCheck) {
        this.autoCheck = autoCheck;
        data.set(0, autoCheck ? "true" : "false");
    }
    
    public void setBlockedSelectable(boolean blockedSelectable) {
        this.blockedSelectable = blockedSelectable;
        data.set(1, blockedSelectable ? "true" : "false");
    }
    
    public void setDarkerBackground(boolean darkerBackground) {
        this.darkerBackground = darkerBackground;
        data.set(2, this.darkerBackground ? "true" : "false");
    }
    
    public void setColorMain(Color colorMain) {
        this.colorMain = colorMain; 
        data.set(3, String.valueOf(colorMain.getRGB()));
    }
    
    public void setColorAux(Color colorAux)   {   
        this.colorAux  = colorAux;  
        data.set(4, String.valueOf(colorAux.getRGB()));
    }
    
    public void setLanguage(int language){
        data.set(5, String.valueOf(language));
    }
                 
    public boolean isAutoCheck()         {  return autoCheck;         }
    public boolean isBlockedSelectable() {  return blockedSelectable; }
    public boolean isDarkerBackground()  {  return darkerBackground;  }
    public Color   getColorMain()        {  return colorMain;         }
    public Color   getColorAux()         {  return colorAux;          }
    public int     getLanguage()         {  return data.get(5).equals("0") ? MyLocalisation.RUSSIAN : MyLocalisation.ENGLISH;       }
}
