
package sudoku;

import sudoku.windows.GameWindow;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;


public final class SudokuLabel extends JLabel{
    private Options options = Options.getInstance();
    /**Цвет выбора клетки в основном режиме ввода*/
    private Color colorSelectedMain         = options.getColorMain();
    /**Цвет выбора клетки во вспомогательном режиме ввода*/
    private Color colorSelectedAux          = options.getColorAux();
    /**Цвет текста стандартный введенного значения*/
    private final Color colorTextDefault          = Color.BLACK;
    /**Цвет текста нередактируемых значений*/
    private final Color colorTextUneditable       = Color.blue.darker();
    /**Цвет текста ошибочных значений*/
    private final Color colorTextError            = Color.RED;
    /**Цвет фона ошибочного значения*/
    private final Color colorErrorBackground      = Color.RED;
    /**Цвет фона заблокированного значения*/
    private final Color colorBlockedBackground    = Color.GRAY;
    /**Цвет фона незаблокированного значения*/
    private final Color colorUnlockedBackground   = Color.WHITE;
    /**Цвет, с которым мы работаем. Зависит от режима ввода основной/вспомогательный.
    * По умолчанию высавлен на основной */
    private Color colorSelectedActual             = colorSelectedMain;
    private boolean blocked, selected;
    private int number, trueValue, row, column, area, rowArea, columnArea;
    private final Font fontMain      = new Font("Bradley Hand ITC", Font.BOLD, 40);
    private final Font fontBlocked   = new Font("Tahoma", Font.PLAIN, 40);
    private final Font fontAuxiliary = new Font("Tahoma", Font.PLAIN, 11);
    private String tempString, stringValueMain, stringValueAuxiliary;
    private final Border unblockedBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
    private final Border blockedBorder = BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
    

    public SudokuLabel() {
        
        stringValueAuxiliary = new String();
        stringValueMain = " ";
        tempString = new String();
        setFont(fontMain);
        
        setBorder(unblockedBorder);
        selected = false;
        blocked = false;
        setOpaque(true);
        setText("");
//        setBackground(Color.white);
        setHorizontalAlignment(JLabel.CENTER);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton()==1  && (!blocked || options.isBlockedSelectable())) 
                    setSelected();
                
        }});
    }
    
    //--------------------    GETTERS    --------------------//
    public String getAuxiliaryString() {   return stringValueAuxiliary;   }
    public String getMainString()      {   return stringValueMain;        }
    public int    getTrueValue()       {   return trueValue;              }
    public int    getNumber()          {   return number;                 }
    
    public boolean isBlocked()  {  return blocked;  }
    public boolean isSelected() {  return selected; }
    
    //--------------------    SETTERS    --------------------//
    /**Метод задает верное значение клетки
     * @param trueValue значение от 1 до 9*/
    public void setTrueValue(int trueValue) {   this.trueValue = trueValue;    }
    
    /**Метод задает номер клетки в игровом поле и расчитывает значения ряда,
     * столбца и групп стобцов и строк для этой клетки
     * @param number номер клетки */
    public void setNumber(int number)       {   
        this.number = number; 
        this.row = number/9;
        this.column = number %9;
        this.rowArea = number/27;
        this.columnArea = (number - row*9)/3;
        this.area = 3 * rowArea + columnArea;
        }
    
    /**Метод задает пользовательское основное значение клетки. Выполняет проверку на верность или ошибочность заполнения
     * @param s пользовательское значение*/
      public void setValueMain(String s){
        setText(s);
        stringValueMain = s;
        setForeground(Color.black);
        setFont(fontMain);
        for (SudokuLabel label : GameWindow.getGameField()) {
            if(label.blocked) label.setForeground(colorTextUneditable);
            else label.setForeground(colorTextDefault);
        }
        for (SudokuLabel label : GameWindow.getGameField()) {
            for (int i = label.row*9; i < label.row*9 + 9; i++) {
            if(!label.stringValueMain.isEmpty() && i != label.number && GameWindow.getGameField().get(i).stringValueMain.equals(label.stringValueMain)){
                label.setErrorForeground();
                GameWindow.getGameField().get(i).setErrorForeground();
            }}
            for (int i = label.column; i < label.column+73; i+=9) {
                if(!label.stringValueMain.isEmpty() &&i!=label.number && GameWindow.getGameField().get(i).stringValueMain.equals(label.stringValueMain)){
                    label.setErrorForeground();
                    GameWindow.getGameField().get(i).setErrorForeground();
            }}
            for (int i = 0; i < 81; i++) {
                if(!label.stringValueMain.isEmpty() &&i!=label.number && label.area ==  GameWindow.getGameField().get(i).area 
                        &&GameWindow.getGameField().get(i).stringValueMain.equals(label.stringValueMain)){
                   label.setErrorForeground();
                   GameWindow.getGameField().get(i).setErrorForeground();
                }
            }
        }
    }

    public void updateColors(){
        
        colorSelectedMain   = options.getColorMain();
        colorSelectedAux    = options.getColorAux();
        colorSelectedActual = GameWindow.isAuxInput() ? colorSelectedAux : colorSelectedMain;

    }
      
    /**Метод задает значение вспомогательной строки-шпаргалки
     * @param text - значение вспомогательной строки*/
    public void setValueAuxiliary(String text){
        if(stringValueMain.isEmpty()){
            stringValueAuxiliary = text;
            setFont(fontAuxiliary);
            int counter = 0;
            StringBuilder stb = new StringBuilder("<html>");
            
            for(char c: stringValueAuxiliary.toCharArray()){
                stb.append(' ');
                stb.append(c);
                stb.append(' ');
                counter++;
                if(counter==3){
                    counter=0;
                    stb.append("<br>");
                }
            }
            stb.append("</html>");
            setText(stb.toString());
        }
    }
    /** Выставляем цвет фона в зависимоти от типа ввода основной/вспомогательный 
     * @param auxInput - FALSE - если основной, TRUE - если вспомогательный*/
    public void setBackgroundColor (boolean auxInput){
        for (SudokuLabel l : GameWindow.getGameField()) {
            if(auxInput) l.colorSelectedActual = colorSelectedAux;
            else l.colorSelectedActual = colorSelectedMain;
        }
        setBackground(colorSelectedActual);
    }
    
    public void showTrueValues(boolean flag){
        if(flag){
            tempString = getText();
            setText(Integer.toString(trueValue));
            }
        else
            setText(tempString);
    }
    public void setErrorForeground(){
        if(!blocked) setForeground(colorTextError);
        else setForeground(colorTextError.darker());
    }
        
    //--------------------    BACKGND    --------------------//
    /**Метод задает цвет фона клетки при ошибочном значении во время
     * проверки результатов*/
    public void setErrorBackground(){
       if(!blocked) setBackground(colorErrorBackground);
       else setBackground(colorErrorBackground.darker());
    }
    
    /**Метод задает цвет фона клетки при верном заполнении всей таблицы 
     * во время проверки результатов*/
    public void setVictoryBackground(){
        if(!blocked) setBackground(Color.GREEN);
        else setBackground(Color.GREEN.darker());
    }
    //--------------------    DATAWRK    --------------------//
    /**Метод делает клетку нередактируемой и открывает заданное значение
     * @param value значение, которое будет отображаться в заблокированной клетке*/
    public void setBlocked(int value){
        if(blocked) return;
        blocked = true;
        setFont(fontBlocked);
        setBorder(blockedBorder);
        setForeground(colorTextUneditable);
        setText(Integer.toString(value));
        stringValueMain = Integer.toString(value);
        trueValue = value;
       
    } 
    public boolean checkValue(){
        return stringValueMain.equals(String.valueOf(trueValue));
    }
    public void setSelected(){
//        System.out.println(("number " + number));
//        System.out.println(("row " + row));
//        System.out.println(("column " + column));
//        System.out.println(("rArea " + rowArea));
//        System.out.println(("cArea " + columnArea));
//        System.out.println(("area " + area));
//        System.out.println("==========================");
        if(isEnabled()){
        for (SudokuLabel label : GameWindow.getGameField()) 
            if(label.getText().equals("?")) label.setText("");
        if(!blocked || options.isBlockedSelectable()) {
            for (SudokuLabel label : GameWindow.getGameField()) {
                label.selected = false;
                if(label.isBlocked()) label.setBackground(options.isDarkerBackground() ? colorBlockedBackground : colorUnlockedBackground);
                else label.setBackground(colorUnlockedBackground);
                
            }
            setBackground(!blocked ? colorSelectedActual : colorSelectedActual.darker().darker());
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            selected = true;
            for (SudokuLabel label : GameWindow.getGameField()) {
                if(label.getText().isEmpty() && !label.getAuxiliaryString().isEmpty()){
                    label.setFont(fontAuxiliary);
                    label.setValueAuxiliary(label.getAuxiliaryString());
                }
            }
        }}}
  
    public void clear(){
        stringValueAuxiliary = new String();
        stringValueMain = "";
        tempString = new String();
        setFont(fontMain);
        selected = false;
        blocked = false;
        setText("");
        setBackground(Color.white);
        setHorizontalAlignment(JLabel.CENTER);
    }
}
