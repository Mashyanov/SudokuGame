
package sudoku.windows;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import sudoku.languages.LanguageListener;
import sudoku.Move;
import sudoku.MyLocalisation;
import sudoku.Options;
import sudoku.Result;
import sudoku.ResultsTable;
import sudoku.SavedGame;
import sudoku.SudokuLabel;

public class GameWindow extends javax.swing.JFrame implements LanguageListener{

private static boolean gameWasStarted, cheats;
private static int difficultyCustom;
/**Флаг-костыль, исправляющий залипание клетки с открытым верным значением при 
 * длительном удержании пробела*/
private boolean spaceIsPressed = false;
private boolean  playerWasCheating;
private String difficulty;
private Timer timerClock, timerLoaded, timerFilled;
private Clock clock;
File optionsFile = new File("options.dat");
private final Stack<Move> moves = new Stack();
private final Options options = Options.getInstance();
private final MyLocalisation localisation = MyLocalisation.getInstance();
private final File savedGameFile = new File("defaultSavedGame.ssg");
private final ResultsTable resultsTable = new ResultsTable();
private final File savedGamesFolder = new File("savedGames");
private int checkResultsCounter, difficultyDefault = 1;
private final Random random = new Random();
private final static LinkedList<SudokuLabel> GAMEFIELD = new LinkedList<>();
private final JMenuBar menuBar = new JMenuBar();
private final JMenu menuGame    = new JMenu(localisation.getLanguage().getMenuGame());
private final JMenu menuOptions = new JMenu(localisation.getLanguage().getMenuOptions());
private final JMenu menuHelp    = new JMenu("?");
//НАБОР УРОВНЕЙ СЛОЖНОСТИ
private Object[] obj = new Object[5];

//menuGame JMenuItems
private final JMenuItem itemNewGame  = new JMenuItem(localisation.getLanguage().getNewGameString());
private final JMenuItem itemCancel   = new JMenuItem(localisation.getLanguage().getCancel());
private final JMenuItem itemControls = new JMenuItem(localisation.getLanguage().getItemControls());
private final JMenuItem itemExit     = new JMenuItem(localisation.getLanguage().getExitString());
//menuOptions JMenuItems
private final JMenuItem itemOptions  = new JMenuItem(localisation.getLanguage().getMenuOptions());
//menuHelp JMenuItems
private final JMenuItem itemAbout    = new JMenuItem(localisation.getLanguage().getItemAbout());
private final JMenuItem itemBest     = new JMenuItem(localisation.getLanguage().getItemBest());
private final JMenuItem itemRules    = new JMenuItem(localisation.getLanguage().getItemRules());
private final JMenuItem itemAnswer   = new JMenuItem(localisation.getLanguage().getItemAnswer());

    //---------------------------------------------------------------------//
    //-----------------------    CONSTRUCTOR   ----------------------------//
    //---------------------------------------------------------------------//
    public GameWindow()  {
        initComponents();
        
        if(!savedGamesFolder.exists()) savedGamesFolder.mkdir();
        menuBar.add(menuGame);
        menuBar.add(menuOptions);
        menuBar.add(menuHelp);
        
        menuGame.add(itemNewGame);
        menuGame.add(itemCancel);
        menuGame.addSeparator();
        menuGame.add(itemExit);
        
        menuOptions.add(itemOptions);
        
        menuHelp.add(itemRules);
        menuHelp.add(itemControls);
        menuHelp.add(itemBest);
        menuHelp.addSeparator();
        menuHelp.add(itemAnswer);
        menuHelp.addSeparator();
        menuHelp.add(itemAbout);
        // menuGame ActionListeners
        itemNewGame.addActionListener( (e) -> {startGame();        });
        itemCancel.addActionListener(  (e) -> {/********************************/   });
        itemExit.addActionListener(    (e) -> {windowClosing();       });
        // menuOptions ActionListeners
        itemRules.addActionListener(   (e) -> {JOptionPane.showMessageDialog(this, new RulesPanel(), localisation.getLanguage().getItemRules(), JOptionPane.PLAIN_MESSAGE);});
        // menuHelp ActionListeners
        itemOptions.addActionListener( (e) -> {new sudoku.windows.OptionsWindow().setVisible(true); });
        itemControls.addActionListener((e) -> {JOptionPane.showMessageDialog(this, "CONTROLS WILL BE ADDED HERE");        });
        itemAnswer.addActionListener(  (e) -> {  /********************************/      });
        itemBest.addActionListener(    (e) -> {JOptionPane.showMessageDialog(this, new ResultPanel(gameWasStarted ? difficulty : localisation.getLanguage().getMedium()), localisation.getLanguage().getItemBest(), JOptionPane.PLAIN_MESSAGE);  });
        itemAbout.addActionListener(   (e) -> {UIManager.put("OptionPane.okButtonText", "Ок!");
                                              JOptionPane.showMessageDialog(this, localisation.getLanguage().getAboutString(), localisation.getLanguage().getAboutTitle(), JOptionPane.PLAIN_MESSAGE);        });
                       
        setJMenuBar(menuBar);
        gameWasStarted = false;
        checkResultsCounter = 0;
        
        timeLabel.setFont(new Font("Tahoma", 1, 30));
        timeLabel.setOpaque(false);
        timeLabel.setForeground(Color.DARK_GRAY);
        timeLabel.setText("00:00:00");
      
        //РЯД НОМЕР 1
        GAMEFIELD.add(label_1_1_1);
        GAMEFIELD.add(label_1_1_2);
        GAMEFIELD.add(label_1_1_3);
        GAMEFIELD.add(label_2_1_1);
        GAMEFIELD.add(label_2_1_2);
        GAMEFIELD.add(label_2_1_3);
        GAMEFIELD.add(label_3_1_1);
        GAMEFIELD.add(label_3_1_2);
        GAMEFIELD.add(label_3_1_3);
        //РЯД НОМЕР 2
        GAMEFIELD.add(label_1_2_1);
        GAMEFIELD.add(label_1_2_2);
        GAMEFIELD.add(label_1_2_3);
        GAMEFIELD.add(label_2_2_1);
        GAMEFIELD.add(label_2_2_2);
        GAMEFIELD.add(label_2_2_3);
        GAMEFIELD.add(label_3_2_1);
        GAMEFIELD.add(label_3_2_2);
        GAMEFIELD.add(label_3_2_3);
        //РЯД НОМЕР 3
        GAMEFIELD.add(label_1_3_1);
        GAMEFIELD.add(label_1_3_2);
        GAMEFIELD.add(label_1_3_3);
        GAMEFIELD.add(label_2_3_1);
        GAMEFIELD.add(label_2_3_2);
        GAMEFIELD.add(label_2_3_3);
        GAMEFIELD.add(label_3_3_1);
        GAMEFIELD.add(label_3_3_2);
        GAMEFIELD.add(label_3_3_3);
        //РЯД НОМЕР 4
        GAMEFIELD.add(label_4_1_1);
        GAMEFIELD.add(label_4_1_2);
        GAMEFIELD.add(label_4_1_3);
        GAMEFIELD.add(label_5_1_1);
        GAMEFIELD.add(label_5_1_2);
        GAMEFIELD.add(label_5_1_3);
        GAMEFIELD.add(label_6_1_1);
        GAMEFIELD.add(label_6_1_2);
        GAMEFIELD.add(label_6_1_3);
        //РЯД НОМЕР 5
        GAMEFIELD.add(label_4_2_1);
        GAMEFIELD.add(label_4_2_2);
        GAMEFIELD.add(label_4_2_3);
        GAMEFIELD.add(label_5_2_1);
        GAMEFIELD.add(label_5_2_2);
        GAMEFIELD.add(label_5_2_3);
        GAMEFIELD.add(label_6_2_1);
        GAMEFIELD.add(label_6_2_2);
        GAMEFIELD.add(label_6_2_3);
        //РЯД НОМЕР 6
        GAMEFIELD.add(label_4_3_1);
        GAMEFIELD.add(label_4_3_2);
        GAMEFIELD.add(label_4_3_3);
        GAMEFIELD.add(label_5_3_1);
        GAMEFIELD.add(label_5_3_2);
        GAMEFIELD.add(label_5_3_3);
        GAMEFIELD.add(label_6_3_1);
        GAMEFIELD.add(label_6_3_2);
        GAMEFIELD.add(label_6_3_3);
        //РЯД НОМЕР 7
        GAMEFIELD.add(label_7_1_1);
        GAMEFIELD.add(label_7_1_2);
        GAMEFIELD.add(label_7_1_3);
        GAMEFIELD.add(label_8_1_1);
        GAMEFIELD.add(label_8_1_2);
        GAMEFIELD.add(label_8_1_3);
        GAMEFIELD.add(label_9_1_1);
        GAMEFIELD.add(label_9_1_2);
        GAMEFIELD.add(label_9_1_3);
        //РЯД НОМЕР 8
        GAMEFIELD.add(label_7_2_1);
        GAMEFIELD.add(label_7_2_2);
        GAMEFIELD.add(label_7_2_3);
        GAMEFIELD.add(label_8_2_1);
        GAMEFIELD.add(label_8_2_2);
        GAMEFIELD.add(label_8_2_3);
        GAMEFIELD.add(label_9_2_1);
        GAMEFIELD.add(label_9_2_2);
        GAMEFIELD.add(label_9_2_3);
        //РЯД НОМЕР 9
        GAMEFIELD.add(label_7_3_1);
        GAMEFIELD.add(label_7_3_2);
        GAMEFIELD.add(label_7_3_3);
        GAMEFIELD.add(label_8_3_1);
        GAMEFIELD.add(label_8_3_2);
        GAMEFIELD.add(label_8_3_3);
        GAMEFIELD.add(label_9_3_1);
        GAMEFIELD.add(label_9_3_2);
        GAMEFIELD.add(label_9_3_3);
        
        for (int i = 0; i < 81; i++) {
            GAMEFIELD.get(i).setNumber(i);
            GAMEFIELD.get(i).setEnabled(false);
        }
        buttonInputMethod.setEnabled(false);
        buttonCheck.setEnabled(false);
        buttonShow.setEnabled(false);
        jLayeredPane1.grabFocus();
        
        

        if(savedGameFile.exists()){
            timerLoaded = new Timer(500, new Notification(Notification.LOADED_GAME));
            loadGame("defaultSavedGame");
            notificationLabel.setForeground(Color.GREEN.darker());
            notificationLabel.setFont(new Font("Tahoma", 1, 20));
            savedGameFile.delete();
            timerLoaded.start();
        }
        localisation.setLanguage(options.getLanguage());
    }
    /**Метод перерисовывает игровое поле. Используовать следует для применения изменений,
     вносимых в настройки игры.*/
    public static final void repaintGameField(){
        for (SudokuLabel label : GAMEFIELD) label.updateColors(); //Обновляем данные о цветах всем SudokuLabel
        if(gameWasStarted) {
            buttonInputMethod.setSelected(!buttonInputMethod.isSelected());// Меняем статус кнопки дважды, чтоб и stateChanged() 
            buttonInputMethod.setSelected(!buttonInputMethod.isSelected());// сработал, и кнопка осталась в том же положении
            getSelectedLabel().setSelected(); //Через повторный выбор уже выбранной клетки засавляем её обновить свой цвет в поле. 
        }
        
    }
    
    public final void updateLanguage(){
        
    }
    //---------------------------------------------------------------------//
    //-------------------------    SETTERS    -----------------------------//
    //---------------------------------------------------------------------//
    
    public static final void setCustomDifficulty(int number){   difficultyCustom  = number;    }
    public static final void setCheats(boolean cheats)      {   GameWindow.cheats = cheats;    }
    
    //---------------------------------------------------------------------//
    //-------------------------    GETTERS    -----------------------------//
    //---------------------------------------------------------------------//
    
    /** @return пользовался ли игрок подсказками**/
    public static boolean isCheating()     { return cheats;          }
    /** @return была ли начата игра**/
    public static boolean gameWasStarted() { return gameWasStarted;  }
    /** @return TRUE - если вводим строку-шпаргалку, FALSE - если вводим основное значение*/
    public static boolean isAuxInput() { return buttonInputMethod.isSelected(); }

    /** @return игровое поле, представляющее собой коллекцию SudokuLabel**/
    public static final LinkedList<SudokuLabel> getGameField() {return GAMEFIELD; }
    /** @return активную SudokuLabel**/
    public static final SudokuLabel getSelectedLabel(){
        for (SudokuLabel label : GAMEFIELD) if(label.isSelected()) return label;
        return null;
    }
    
    //---------------------------------------------------------------------//
    //----------------------    GAME CREATION    --------------------------//
    //---------------------------------------------------------------------//
    
    /**Рисуем базовую верную таблицу*/
    private void createBasicTable(){
        SudokuLabel l;
        for (SudokuLabel label : GAMEFIELD) label.clear();
             
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                l = GAMEFIELD.get(i*9 + j);
                l.setTrueValue((l.getNumber() + i*3 + i/3)%9+1);
    }}}
    
    /**Перемешиваем ее разными методами много раз подряд*/
    private void randomizeTable(){
        for (int i = 0; i < 100; i++) {
            switch(random.nextInt(9)){
                case 0:
                    transposeWholeTable();
                    break;
                case 1:case 5:
                    swapColumsSmall();
                    break;
                case 2:case 6:
                    swapColumsArea();
                    break;
                case 3:case 7:
                    swapRowsSmall();
                    break;
                case 4:case 8:
                    swapRowsArea();
                    break;
    }}}
    
    /**Открываем часть значений*/
    private void prepareFinalTable(){
        int number, openedValues;
        
        switch(difficulty){
            case "Легкая":
                openedValues = 36;
                cheats = true;
                buttonShow.setEnabled(true);
                break;
            case "Средняя":
                openedValues = 26;
                cheats = true;
                buttonShow.setEnabled(true);
                break;
            case "Тяжелая":
                openedValues = 20;
                cheats = true;
                buttonShow.setEnabled(true);
                break;
            case "Тяжелая и без подсказок":
                openedValues = 20;
                cheats = false;
                buttonShow.setEnabled(false);
                break;
            case "Своя...":
                JOptionPane.showMessageDialog(this, new CustomDifficultyPanel(), localisation.getLanguage().getCustomMessage(), JOptionPane.PLAIN_MESSAGE);
                openedValues = difficultyCustom;
                buttonShow.setEnabled(cheats);
                break;
            default:
                difficulty = "Легкая";
                openedValues = 36;
                buttonShow.setEnabled(true);
                break;
        }
        
        for (int i = 0; i < openedValues; i++) {
            number = random.nextInt(81);
            if(!GAMEFIELD.get(number).isBlocked()){
                GAMEFIELD.get(number).setBlocked(GAMEFIELD.get(number).getTrueValue());
            }
            else i--;
        }
        for (SudokuLabel sudokuLabel : GAMEFIELD) 
            sudokuLabel.setEnabled(true);
        buttonCheck.setEnabled(true);
    }
    
    //---------------------------------------------------------------------//
    //----------------------    TABLE  MIXING    --------------------------//
    //---------------------------------------------------------------------//
    
    /**Меняем местами два блока столбцов*/
    private void swapColumsArea(){
        int area2, tempInt, area1= random.nextInt(3);
        do  area2 = random.nextInt(3);  while (area2==area1);//чтоб не менялся местами с самим собой
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                tempInt = GAMEFIELD.get(j*9 + i + area1*3 ).getTrueValue();
                GAMEFIELD.get(j*9 + i + area1*3 ).setTrueValue(GAMEFIELD.get(j*9 + i + area2*3 ).getTrueValue());
                GAMEFIELD.get(j*9 + i + area2*3 ).setTrueValue(tempInt);
    }}}
    
    /**Меняем местами два столбца*/
    private void swapColumsSmall(){
        int area = random.nextInt(3);
        int col1 = random.nextInt(3);
        int col2, tempInt;
        do  col2= random.nextInt(3); while (col2==col1);
        for (int i = 0; i < 9; i++) {
            tempInt = GAMEFIELD.get(i*9 + col1 + area*3).getTrueValue();
            GAMEFIELD.get(i*9 + col1+ area*3).setTrueValue(GAMEFIELD.get(i*9 + col2+ area*3).getTrueValue());
            GAMEFIELD.get(i*9 + col2+ area*3).setTrueValue(tempInt);
    }}
    
    /**Меняем местами два блока рядов*/
    private void swapRowsArea(){
        int area2, tempInt, area1= random.nextInt(3);
        do  area2= random.nextInt(3); while (area2==area1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
               tempInt = GAMEFIELD.get(j + i*9 + 27*area1).getTrueValue();
               GAMEFIELD.get(j + i*9 + 27*area1).setTrueValue(GAMEFIELD.get(j + i*9 + 27*area2).getTrueValue());
               GAMEFIELD.get(j + i*9 + 27*area2).setTrueValue(tempInt);
    }}}    
    
    /**Меняем местами два ряда*/
    private void swapRowsSmall(){
        int row2, tempInt;
        int area = random.nextInt(3);
        int row1 = random.nextInt(3);
        do  row2= random.nextInt(3); while (row2==row1);
        for (int i = 0; i < 9; i++) {
        tempInt = GAMEFIELD.get(i + 9*row1 + 27*area).getTrueValue();
        GAMEFIELD.get(i + 9*row1 + 27*area).setTrueValue(GAMEFIELD.get(i + 9*row2 + 27*area).getTrueValue());
        GAMEFIELD.get(i + 9*row2 + 27*area).setTrueValue(tempInt);
    }}
    
    /**Транспонируем таблицу целиком*/
    private void transposeWholeTable(){
        int tempInt;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j <9 - i; j++) {
                tempInt = GAMEFIELD.get(j+10*i).getTrueValue();
                GAMEFIELD.get(j + 10*i).setTrueValue(GAMEFIELD.get(j*9 + i*10).getTrueValue());
                GAMEFIELD.get(j * 9 + i * 10).setTrueValue(tempInt);
    }}}
    
    //---------------------------------------------------------------------//
    //-----------------------    GAME METHODS    --------------------------//
    //---------------------------------------------------------------------//
    
    /**Метод начала новой игры. Скидывает все  в ноль и создает новое игровое поле*/
    private void startGame(){
        Icon icon = null;
        UIManager.put("OptionPane.yesButtonText"   , localisation.getLanguage().getYes()   );
        UIManager.put("OptionPane.noButtonText"    , localisation.getLanguage().getNo()   );
        if(!gameWasStarted || JOptionPane.showConfirmDialog(this, localisation.getLanguage().getBeginNewGameQuestion(), 
                localisation.getLanguage().getBeginNewGameTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == 0){
        checkResultsCounter = 0;
        playerWasCheating = false;
        if(timerClock!=null)    timerClock.stop();
        obj[0] = "Легкая";
        obj[1] = "Средняя";
        obj[2] = "Тяжелая";
        obj[3] = "Тяжелая и без подсказок";
        obj[4] = "Своя...";
           
        difficulty = new String();
        
        UIManager.put("OptionPane.okButtonText"    ,localisation.getLanguage().getBegin());
        UIManager.put("OptionPane.cancelButtonText",localisation.getLanguage().getCancel());
        difficulty = (String) JOptionPane.showInputDialog(this, localisation.getLanguage().getChooseDifficulty(),
                localisation.getLanguage().getNewGameString(), JOptionPane.PLAIN_MESSAGE, icon, obj, obj[difficultyDefault]);

        if(difficulty!=null){
            createBasicTable();
            randomizeTable();
            prepareFinalTable();
            buttonShow.setSelected(false);
            buttonCheck.setEnabled(false);
            buttonInputMethod.setEnabled(true);
            gameWasStarted = true;
            jLayeredPane1.grabFocus();
            for (SudokuLabel label : GAMEFIELD) {
                if(!label.isBlocked()){
                    label.setSelected();
                    break;
                }
            }
            clock = new Clock();
            timerClock = new Timer(100, clock);
            timerClock.start();
    }}}
    
    /**Метод проверяет, заполнена ли таблица полностью. Если да - предлагает проверить результаты методом checkResults()*/
    private void checkTableFilled(){
        boolean result = true;
        for (SudokuLabel l : GAMEFIELD) {
            if(l.getMainString().isEmpty()) {
                result = false;
                break;
            }}
            if(result){
                buttonCheck.setEnabled(true);
                timerFilled = new Timer(300, new Notification(Notification.FILLED_FIELD));
                timerFilled.start();
            }
            else
        if(result && options.isAutoCheck()){
            
            UIManager.put("OptionPane.yesButtonText"   , localisation.getLanguage().getYes()    );
            UIManager.put("OptionPane.noButtonText"    , localisation.getLanguage().getNo()     );
            
            switch( JOptionPane.showConfirmDialog(this, localisation.getLanguage().getTableFilledMessage(), 
                    localisation.getLanguage().getTableFilledTitle(), JOptionPane.YES_NO_OPTION, 
                    JOptionPane.PLAIN_MESSAGE)){
                case 0:
                    checkResults();
                    break;
                case 2:
                    options.setAutoCheck(false);
                    //questionWindow.setSelected(false);
                    break;
    }}}
    
    /**Изменяем значение строки-шпаргалки, добавляя значение char или убирая его, 
    если такое уже есть в строке  */
    private void changeAuxString(char value){
        String aux = getSelectedLabel().getAuxiliaryString();
        /**Если TRUE - символ добавляется в строку шпаргалку. Если FALSE - значит
         * такой символ уже есть и мы его удаляем*/
        boolean addChar = true;
        if(aux.isEmpty()) getSelectedLabel().setValueAuxiliary(String.valueOf(value));
        else{
            for(char c : aux.toCharArray())
                if(c == value) {
                    addChar = false;
                    break;
                }
            if(addChar){
                aux +=String.valueOf(value);
                char [] tempChar = aux.toCharArray();
                Arrays.sort(tempChar);
                getSelectedLabel().setValueAuxiliary(String.copyValueOf(tempChar));
            }               
            else{
                aux=aux.replaceFirst(String.valueOf(value), "");
                getSelectedLabel().setValueAuxiliary(aux);
    }}}
    
    /** Отменяем последний ход */
    private void cancelLastMove(){
        Move move;
        if(!moves.empty()){
            move = moves.pop();
            buttonInputMethod.setSelected(move.isAuxMove());
            move.getLabel().setSelected();
            if(!move.isAuxMove())
                move.getLabel().setValueMain(move.getValue());
            else{
                move.getLabel().setValueMain("");
                move.getLabel().setValueAuxiliary(move.getValue());
    }}}
    
     /**Сохраняем игру*/
    private void saveGame(){
    ObjectOutputStream oos = null;
        try {
        int labelNumer =-1;
        if(getSelectedLabel()!=null)
            labelNumer = getSelectedLabel().getNumber();
        SavedGame game = new SavedGame(GAMEFIELD, clock.currentTime, checkResultsCounter, cheats, playerWasCheating, buttonInputMethod.isSelected(), labelNumer, moves);
        if(!savedGameFile.exists()) savedGameFile.createNewFile();
        oos = new ObjectOutputStream(new FileOutputStream(savedGameFile));
        oos.writeObject(game);
        oos.close();
    } catch (IOException ex) {
        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
    } finally{
        try {
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
    }}}
    
    /**Метод загружаем сохраненную игру.
     * @param name имя сохраненной игры без ".ssg"*/
    public final void loadGame(String name){
        ObjectInputStream ois = null;
    try {
        ois = new ObjectInputStream(new FileInputStream(new File(name+".ssg")));
        SavedGame game = (SavedGame) ois.readObject();
        ois.close();
        if(!gameWasStarted){
            for (SudokuLabel sudokuLabel : GAMEFIELD) 
                sudokuLabel.setEnabled(true);
            
            gameWasStarted = true;
            clock = new Clock();
            timerClock = new Timer(100, clock);
            timerClock.start();
            buttonInputMethod.setEnabled(true);
            
        }
        for (int i = 0; i < 81; i++) {
            GAMEFIELD.get(i).clear();
            GAMEFIELD.get(i).setTrueValue(game.getGameField().get(i).getTrueValue());
            GAMEFIELD.get(i).setValueAuxiliary(game.getGameField().get(i).getAuxiliaryString());
            GAMEFIELD.get(i).setValueMain(game.getGameField().get(i).getMainString());
            if(game.getGameField().get(i).isBlocked()) GAMEFIELD.get(i).setBlocked(Integer.parseInt(game.getGameField().get(i).getMainString()));
            
        }
        clock.loadTime = game.getTime();
        moves.clear();
        if(!game.getMoves().empty())
            for (Move move : game.getMoves()) 
                moves.push(move);
        cheats = game.cheats();
        if(game.getSelectedLabel()!=-1) GAMEFIELD.get(game.getSelectedLabel()).setSelected();
        buttonInputMethod.setSelected(game.isAuxMode());
        buttonShow.setEnabled(cheats);
        checkTableFilled();
       
 
    } catch (FileNotFoundException ex) {
        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException | ClassNotFoundException ex) {
        Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            ois.close();
        } catch (IOException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
    }}}
    
    /** Проверяем правильностьи полноту заполнения ответов игроком */
    private void checkResults(){
        checkResultsCounter++;
        boolean success= true;
        for (SudokuLabel l : GAMEFIELD) {
            if(!l.getMainString().isEmpty() && !l.checkValue() && !l.isBlocked()){
                success = false;
                l.setErrorBackground();
            }
            if(l.getMainString().isEmpty()){
                success = false;
                l.setFont(new Font("Tahoma", Font.PLAIN, 40));
                l.setText("?");
            }
        }
        if(!success){
            UIManager.put("OptionPane.okButtonText"    , "ОК!");
            JOptionPane.showMessageDialog(this, localisation.getLanguage().getWrongResultMessage(), localisation.getLanguage().getWrongResultTitle(), JOptionPane.ERROR_MESSAGE);
            getSelectedLabel().setSelected();//Эта строка-костыль сбрасывает вопросительные знаки
        }
        
        //---------------------------------------------------------------------//
        //-----------------------    PLAYER'VE WON   --------------------------//
        //---------------------------------------------------------------------//
        
        else{
            timerClock.stop();
            Result result = new Result(clock.hh * 3600 + clock.mm * 60 + clock.ss, difficulty, Calendar.getInstance());
            if(!playerWasCheating && !difficulty.equals("Своя...") && 
                    resultsTable.getSize(difficulty) < options.MAX_RESULTS || 
                     !difficulty.equals("Своя...") && result.getTime() < resultsTable.getWorstResult(difficulty).getTime())
                resultsTable.addResult(result);
            
            String congratulations ="Но правда только с " + checkResultsCounter +"-ой попытки...\nЗато не подглядывая в ответы! :)";
            if(checkResultsCounter == 1 && !playerWasCheating)congratulations = "И даже с первой попытки!!!";
            if(playerWasCheating) congratulations = "В следющий раз только постарайся в ответы не подглядывать ;)";
            gameWasStarted = false;
            for (SudokuLabel label : GAMEFIELD) 
               label.setVictoryBackground();
            UIManager.put("OptionPane.okButtonText"    , "OK!!! :)");
            JOptionPane.showMessageDialog(this,localisation.getLanguage().getAllISCorrect() +"\n"+congratulations,
                    localisation.getLanguage().getLookAtYourselfTitle(), JOptionPane.WARNING_MESSAGE);
            UIManager.put("OptionPane.yesButtonText"   , localisation.getLanguage().getYes()   );
            UIManager.put("OptionPane.noButtonText"    , localisation.getLanguage().getNo()  );
            if(JOptionPane.showConfirmDialog(this, localisation.getLanguage().getPlayAgain(), localisation.getLanguage().getLookAtYourselfTitle(), JOptionPane.YES_NO_OPTION)==0)
                startGame();
            else System.exit(0);
        }
        jLayeredPane1.grabFocus();
    }
   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        panel_1_1 = new javax.swing.JPanel();
        label_1_1_2 = new SudokuLabel();
        label_1_1_1 = new SudokuLabel();
        label_1_1_3 = new SudokuLabel();
        label_1_2_1 = new SudokuLabel();
        label_1_2_2 = new SudokuLabel();
        label_1_2_3 = new SudokuLabel();
        label_1_3_1 = new SudokuLabel();
        label_1_3_2 = new SudokuLabel();
        label_1_3_3 = new SudokuLabel();
        panel_1_2 = new javax.swing.JPanel();
        label_2_1_1 = new SudokuLabel();
        label_2_3_3 = new SudokuLabel();
        label_2_1_2 = new SudokuLabel();
        label_2_1_3 = new SudokuLabel();
        label_2_2_3 = new SudokuLabel();
        label_2_2_2 = new SudokuLabel();
        label_2_2_1 = new SudokuLabel();
        label_2_3_1 = new SudokuLabel();
        label_2_3_2 = new SudokuLabel();
        panel_1_3 = new javax.swing.JPanel();
        label_3_1_1 = new SudokuLabel();
        label_3_3_3 = new SudokuLabel();
        label_3_1_2 = new SudokuLabel();
        label_3_1_3 = new SudokuLabel();
        label_3_2_3 = new SudokuLabel();
        label_3_2_2 = new SudokuLabel();
        label_3_2_1 = new SudokuLabel();
        label_3_3_1 = new SudokuLabel();
        label_3_3_2 = new SudokuLabel();
        panel_3_1 = new javax.swing.JPanel();
        label_7_1_1 = new SudokuLabel();
        label_7_3_3 = new SudokuLabel();
        label_7_1_2 = new SudokuLabel();
        label_7_1_3 = new SudokuLabel();
        label_7_2_3 = new SudokuLabel();
        label_7_2_2 = new SudokuLabel();
        label_7_2_1 = new SudokuLabel();
        label_7_3_1 = new SudokuLabel();
        label_7_3_2 = new SudokuLabel();
        panel_3_2 = new javax.swing.JPanel();
        label_8_1_1 = new SudokuLabel();
        label_8_3_3 = new SudokuLabel();
        label_8_1_2 = new SudokuLabel();
        label_8_1_3 = new SudokuLabel();
        label_8_2_3 = new SudokuLabel();
        label_8_2_2 = new SudokuLabel();
        label_8_2_1 = new SudokuLabel();
        label_8_3_1 = new SudokuLabel();
        label_8_3_2 = new SudokuLabel();
        panel_3_3 = new javax.swing.JPanel();
        label_9_1_1 = new SudokuLabel();
        label_9_3_3 = new SudokuLabel();
        label_9_1_2 = new SudokuLabel();
        label_9_1_3 = new SudokuLabel();
        label_9_2_3 = new SudokuLabel();
        label_9_2_2 = new SudokuLabel();
        label_9_2_1 = new SudokuLabel();
        label_9_3_1 = new SudokuLabel();
        label_9_3_2 = new SudokuLabel();
        panel_2_1 = new javax.swing.JPanel();
        label_4_1_1 = new SudokuLabel();
        label_4_3_3 = new SudokuLabel();
        label_4_1_2 = new SudokuLabel();
        label_4_1_3 = new SudokuLabel();
        label_4_2_3 = new SudokuLabel();
        label_4_2_2 = new SudokuLabel();
        label_4_2_1 = new SudokuLabel();
        label_4_3_1 = new SudokuLabel();
        label_4_3_2 = new SudokuLabel();
        panel_2_2 = new javax.swing.JPanel();
        label_5_1_1 = new SudokuLabel();
        label_5_3_3 = new SudokuLabel();
        label_5_1_2 = new SudokuLabel();
        label_5_1_3 = new SudokuLabel();
        label_5_2_3 = new SudokuLabel();
        label_5_2_2 = new SudokuLabel();
        label_5_2_1 = new SudokuLabel();
        label_5_3_1 = new SudokuLabel();
        label_5_3_2 = new SudokuLabel();
        panel_2_3 = new javax.swing.JPanel();
        label_6_1_1 = new SudokuLabel();
        label_6_3_3 = new SudokuLabel();
        label_6_1_2 = new SudokuLabel();
        label_6_1_3 = new SudokuLabel();
        label_6_2_3 = new SudokuLabel();
        label_6_2_2 = new SudokuLabel();
        label_6_2_1 = new SudokuLabel();
        label_6_3_1 = new SudokuLabel();
        label_6_3_2 = new SudokuLabel();
        buttonCheck = new javax.swing.JButton();
        buttonNewGame = new javax.swing.JButton();
        buttonShow = new javax.swing.JToggleButton();
        buttonExit = new javax.swing.JButton();
        timeLabel = new SudokuLabel();
        borderPanelVertical = new javax.swing.JPanel();
        borderPanelHorizontal = new javax.swing.JPanel();
        buttonInputMethod = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        notificationLabel = new SudokuLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("СуДоКу");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLayeredPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        jLayeredPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLayeredPane1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jLayeredPane1KeyReleased(evt);
            }
        });
        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_1_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_1_1.setOpaque(false);
        panel_1_1.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_1_1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_1_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_1_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_1_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_1_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_1_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_1_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_1_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_1_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        label_1_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_1.add(label_1_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        jLayeredPane1.add(panel_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, -1, -1));

        panel_1_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_1_2.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_1_2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_2_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_2_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_2_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_2_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_2_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_2_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_2_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_2_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_2_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_2.add(label_2_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 1, -1, -1));

        panel_1_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_1_3.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_1_3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_3_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_3_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_3_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_3_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_3_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_3_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_3_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_3_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_3_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_1_3.add(label_3_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(301, 1, -1, -1));

        panel_3_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_3_1.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_3_1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_7_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_7_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_7_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_7_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_7_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_7_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_7_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_7_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_7_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_1.add(label_7_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 301, -1, -1));

        panel_3_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_3_2.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_3_2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_8_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_8_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_8_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_8_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_8_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_8_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_8_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_8_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_8_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_2.add(label_8_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 301, -1, -1));

        panel_3_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_3_3.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_3_3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_9_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_9_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_9_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_9_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_9_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_9_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_9_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_9_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_9_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_3_3.add(label_9_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(301, 301, -1, -1));

        panel_2_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_2_1.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_2_1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_4_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_4_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_4_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_4_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_4_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_4_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_4_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_4_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_4_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_1.add(label_4_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 151, -1, -1));

        panel_2_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_2_2.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_2_2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_5_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_5_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_5_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_5_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_5_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_5_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_5_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_5_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_5_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_2.add(label_5_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 151, -1, -1));

        panel_2_3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4));
        panel_2_3.setPreferredSize(new java.awt.Dimension(150, 150));
        panel_2_3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_6_1_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, -1, -1));

        label_6_3_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_3_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 102, -1, -1));

        label_6_1_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 2, -1, -1));

        label_6_1_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 2, -1, -1));

        label_6_2_3.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(102, 52, -1, -1));

        label_6_2_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 52, -1, -1));

        label_6_2_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 52, -1, -1));

        label_6_3_1.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_3_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 102, -1, -1));

        label_6_3_2.setPreferredSize(new java.awt.Dimension(50, 50));
        panel_2_3.add(label_6_3_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 102, -1, -1));

        jLayeredPane1.add(panel_2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(301, 151, -1, -1));

        buttonCheck.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        buttonCheck.setText("Проверить!");
        buttonCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCheckActionPerformed(evt);
            }
        });

        buttonNewGame.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        buttonNewGame.setText("Новая игра");
        buttonNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNewGameActionPerformed(evt);
            }
        });

        buttonShow.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        buttonShow.setText("Показать ответ");
        buttonShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowActionPerformed(evt);
            }
        });

        buttonExit.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        buttonExit.setText("Выход");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonExitActionPerformed(evt);
            }
        });

        timeLabel.setText("clockLabel");

        borderPanelVertical.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout borderPanelVerticalLayout = new javax.swing.GroupLayout(borderPanelVertical);
        borderPanelVertical.setLayout(borderPanelVerticalLayout);
        borderPanelVerticalLayout.setHorizontalGroup(
            borderPanelVerticalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        borderPanelVerticalLayout.setVerticalGroup(
            borderPanelVerticalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 449, Short.MAX_VALUE)
        );

        borderPanelHorizontal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout borderPanelHorizontalLayout = new javax.swing.GroupLayout(borderPanelHorizontal);
        borderPanelHorizontal.setLayout(borderPanelHorizontalLayout);
        borderPanelHorizontalLayout.setHorizontalGroup(
            borderPanelHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 449, Short.MAX_VALUE)
        );
        borderPanelHorizontalLayout.setVerticalGroup(
            borderPanelHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        buttonInputMethod.setBackground(new java.awt.Color(102, 255, 102));
        buttonInputMethod.setFont(new java.awt.Font("Vrinda", 0, 110)); // NOI18N
        buttonInputMethod.setText("O");
        buttonInputMethod.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonInputMethod.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                buttonInputMethodStateChanged(evt);
            }
        });

        jButton1.setText("S");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("L");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        notificationLabel.setBackground(new java.awt.Color(214, 217, 223));
        notificationLabel.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(buttonNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(buttonInputMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(buttonCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(buttonShow))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)))
                        .addGap(18, 18, 18)
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(borderPanelVertical, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                                .addComponent(borderPanelHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(notificationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonInputMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonShow, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1)))
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borderPanelVertical, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(borderPanelHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(notificationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void buttonCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCheckActionPerformed
       checkResults();
    }//GEN-LAST:event_buttonCheckActionPerformed

    private void buttonNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNewGameActionPerformed
        startGame();
    }//GEN-LAST:event_buttonNewGameActionPerformed

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b); 
        if(b) {
            localisation.addLanguageListener(this);
            languageChanged();
        }
    }

    
    
    @Override
    public final void languageChanged() {
        System.out.println("languageChanged()");
        //КНОПКИ
        buttonNewGame.setText(localisation.getLanguage().getNewGameString());
        buttonCheck.setText(localisation.getLanguage().getCheck());
        buttonExit.setText(localisation.getLanguage().getExitString());
        
        //МЕНЮ
        menuGame.setText(localisation.getLanguage().getMenuGame());
        menuOptions.setText(localisation.getLanguage().getMenuOptions());
        itemNewGame.setText(localisation.getLanguage().getNewGameString());
        itemCancel.setText(localisation.getLanguage().getCancelLastMove());
        itemExit.setText(localisation.getLanguage().getExitString());
        itemOptions.setText(localisation.getLanguage().getMenuOptions());
        itemRules.setText(localisation.getLanguage().getItemRules());
        itemControls.setText(localisation.getLanguage().getItemControls());
        itemBest.setText(localisation.getLanguage().getItemBest());
        itemAnswer.setText(localisation.getLanguage().getItemAnswer());
        itemAbout.setText(localisation.getLanguage().getAboutTitle());
    }
   
    
    /**Метод закрытия программы*/
    private void windowClosing(){
   
    UIManager.put("OptionPane.yesButtonText"       , localisation.getLanguage().getYes()    );
    UIManager.put("OptionPane.noButtonText"        , localisation.getLanguage().getNo()   ); 
    if(gameWasStarted){
        UIManager.put("OptionPane.yesButtonText"       , "<html><b>"+localisation.getLanguage().getSaveGameYes()+"</b></html>"    );
        UIManager.put("OptionPane.noButtonText"        , localisation.getLanguage().getSaveGameNo()   );
        UIManager.put("OptionPane.cancelButtonText"    , localisation.getLanguage().getCancel()   );
        switch(JOptionPane.showConfirmDialog(this, localisation.getLanguage().getSaveGameQuestion(),
                localisation.getLanguage().getExitTitle(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)){
            case 0:
                saveGame();
                System.exit(0);
                break;
            case 1:
                if(savedGameFile.exists()) savedGameFile.delete();
                System.exit(0);
                break;
        }
    }
    else if(JOptionPane.showConfirmDialog(this, localisation.getLanguage().getExitMessage(),
                localisation.getLanguage().getExitTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)==0)
        System.exit(0);
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        windowClosing();
    }//GEN-LAST:event_formWindowClosing

    private void buttonShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowActionPerformed
        GAMEFIELD.forEach((l) -> {   l.showTrueValues(buttonShow.isSelected());   });
        playerWasCheating = true;
        jLayeredPane1.grabFocus();
    }//GEN-LAST:event_buttonShowActionPerformed
   
    private void jLayeredPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLayeredPane1KeyPressed
        int temp = 0;
        if(getSelectedLabel()!=null){
            switch(evt.getKeyCode()){
                case 49:case 97:// КЛАВИШИ 1
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected()));
                        if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("1");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('1');
                    }
                    break;
                case 50:case 98:// КЛАВИШИ 2
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected()));
                        if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("2");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('2');
                    }
                    break;
                case 51:case 99:// КЛАВИШИ 3
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected()));
                        if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("3");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('3');
                    }
                    break;
                case 52:case 100:// КЛАВИШИ 4
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected() && !getSelectedLabel().getAuxiliaryString().isEmpty()));
                        if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("4");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('4');
                    }
                    break;
                case 53:case 101:// КЛАВИШИ 5
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected()));
                        if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("5");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('5');
                    }
                    break;
                case 54:case 102:// КЛАВИШИ 6
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected()));
                        if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("6");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('6');
                    }
                    break;
                case 55:case 103:// КЛАВИШИ 7
                    if(!getSelectedLabel().isBlocked()){
                            moves.push(new Move(getSelectedLabel(), 
                                    getSelectedLabel().getText(), 
                                    buttonInputMethod.isSelected()));
                           if(!buttonInputMethod.isSelected()){
                                getSelectedLabel().setValueMain("7");
                                checkTableFilled();
                            break;
                        }
                        changeAuxString('7');
                    }
                    break;
                case 56:case 104:// КЛАВИШИ 8
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected()));
                       if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("8");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('8');
                    }
                    break;
                case 57:case 105:// КЛАВИШИ 9
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                buttonInputMethod.isSelected()));
                       if(!buttonInputMethod.isSelected()){
                            getSelectedLabel().setValueMain("9");
                            checkTableFilled();
                            break;
                        }
                        changeAuxString('9');
                    }
                    break;
                case 8:case 127:// КЛАВИШИ DEL,BACKSPACE,
                    if(!getSelectedLabel().isBlocked()){
                        moves.push(new Move(getSelectedLabel(), 
                                getSelectedLabel().getText(), 
                                getSelectedLabel().getMainString().isEmpty()));
                        if(!getSelectedLabel().getMainString().isEmpty()) {
                            getSelectedLabel().setValueMain("");
                            if(!getSelectedLabel().getAuxiliaryString().isEmpty()){
                                getSelectedLabel().setValueAuxiliary(getSelectedLabel().getAuxiliaryString());
                                buttonInputMethod.setSelected(true);
                            }
                        }
                        else if(!getSelectedLabel().getAuxiliaryString().isEmpty())
                            getSelectedLabel().setValueAuxiliary(getSelectedLabel().getAuxiliaryString().substring(0, getSelectedLabel().getAuxiliaryString().length()-1));
                    }
                    break;
                case 32: //ПРОБЕЛ
                    if(cheats && !spaceIsPressed && !getSelectedLabel().isBlocked()){
                        spaceIsPressed = true;
                        playerWasCheating = true;
                        getSelectedLabel().showTrueValues(true);
                    }
                    else
                        evt.consume();
                    break;
                    
                case 10://ENTER
                    buttonInputMethod.setSelected(!buttonInputMethod.isSelected());
                    break;

                case 37: case 65://LEFT
                    if(getSelectedLabel().getNumber() > 0){
                        if(!options.isBlockedSelectable()){
                            while (GAMEFIELD.get(getSelectedLabel().getNumber() - 1 - temp).isBlocked()) {            
                                if(getSelectedLabel().getNumber()-1-temp ==0) break;
                                temp++;
                        }}
                        GAMEFIELD.get(getSelectedLabel().getNumber()-1 - temp).setSelected();
                    }
                    break;

                case 38:case 87://UP
                    if(getSelectedLabel().getNumber()/9>0){
                        if(!options.isBlockedSelectable()){
                            while(GAMEFIELD.get(getSelectedLabel().getNumber() - 9 - temp).isBlocked()){
                                if((getSelectedLabel().getNumber() - 9 - temp)/9==0) break;
                                temp+=9;
                        }}
                        GAMEFIELD.get(getSelectedLabel().getNumber() - 9 - temp).setSelected();
                    }
                    break;

                case 39:case 68://RIGHT
                    if(getSelectedLabel().getNumber()<80){
                        if(!options.isBlockedSelectable()){
                            while (GAMEFIELD.get(getSelectedLabel().getNumber() + 1 + temp).isBlocked()) {            
                                if(getSelectedLabel().getNumber()+1+temp ==80) break;
                                temp++;
                        }}
                        GAMEFIELD.get(getSelectedLabel().getNumber()+1 + temp).setSelected();
                    }
                    break;

                case 40:case 83://DOWN
                     if(getSelectedLabel().getNumber()<72){
                        if(!options.isBlockedSelectable()){
                            while(GAMEFIELD.get(getSelectedLabel().getNumber() + 9 + temp).isBlocked()){
                                if((getSelectedLabel().getNumber() + 9 + temp)>=72) break;
                                temp+=9;
                        }}
                        GAMEFIELD.get(getSelectedLabel().getNumber() +9 + temp).setSelected();
                    }
                    break;
            }    
        }
    }//GEN-LAST:event_jLayeredPane1KeyPressed

    private void jLayeredPane1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLayeredPane1KeyReleased
        if(evt.getKeyCode() == 32 && cheats && !getSelectedLabel().isBlocked()) {
            getSelectedLabel().showTrueValues(false);
            spaceIsPressed = false;
        }
    }//GEN-LAST:event_jLayeredPane1KeyReleased
    private void buttonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonExitActionPerformed
//        UIManager.put("OptionPane.yesButtonText"   , "Да"    );
//        UIManager.put("OptionPane.noButtonText"    , "Нет"   );
//        if(JOptionPane.showConfirmDialog(this, "Выйти из игры?\nРезультаты будут потеряны",
//                "Выходим?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE)==0)
//            System.exit(0);
    windowClosing();

    }//GEN-LAST:event_buttonExitActionPerformed

    private void buttonInputMethodStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_buttonInputMethodStateChanged
         if(!buttonInputMethod.isSelected()){
            buttonInputMethod.setText("O");
            buttonInputMethod.setBackground(options.getColorMain());
            buttonInputMethod.setFont(new Font("Vrinda", 0, 110));
        }
        else{
            buttonInputMethod.setBackground(options.getColorAux());
            buttonInputMethod.setText("<html>O O O <br> O O O <br> O O O</html>");
            buttonInputMethod.setFont(new Font("Vrinda", 0, 28));
        }
        if(getSelectedLabel()!=null)getSelectedLabel().setBackgroundColor(buttonInputMethod.isSelected());
        jLayeredPane1.grabFocus();
    }//GEN-LAST:event_buttonInputMethodStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

JOptionPane.showMessageDialog(this, new CustomDifficultyPanel());
        jLayeredPane1.grabFocus();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadGame("defaultSavedGame");
        jLayeredPane1.grabFocus();
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
               new GameWindow().setVisible(true);
            }
        });
   
    
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel borderPanelHorizontal;
    private javax.swing.JPanel borderPanelVertical;
    private javax.swing.JButton buttonCheck;
    private javax.swing.JButton buttonExit;
    private static javax.swing.JToggleButton buttonInputMethod;
    private javax.swing.JButton buttonNewGame;
    private javax.swing.JToggleButton buttonShow;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLayeredPane jLayeredPane1;
    private SudokuLabel label_1_1_1;
    private SudokuLabel label_1_1_2;
    private SudokuLabel label_1_1_3;
    private SudokuLabel label_1_2_1;
    private SudokuLabel label_1_2_2;
    private SudokuLabel label_1_2_3;
    private SudokuLabel label_1_3_1;
    private SudokuLabel label_1_3_2;
    private SudokuLabel label_1_3_3;
    private SudokuLabel label_2_1_1;
    private SudokuLabel label_2_1_2;
    private SudokuLabel label_2_1_3;
    private SudokuLabel label_2_2_1;
    private SudokuLabel label_2_2_2;
    private SudokuLabel label_2_2_3;
    private SudokuLabel label_2_3_1;
    private SudokuLabel label_2_3_2;
    private SudokuLabel label_2_3_3;
    private SudokuLabel label_3_1_1;
    private SudokuLabel label_3_1_2;
    private SudokuLabel label_3_1_3;
    private SudokuLabel label_3_2_1;
    private SudokuLabel label_3_2_2;
    private SudokuLabel label_3_2_3;
    private SudokuLabel label_3_3_1;
    private SudokuLabel label_3_3_2;
    private SudokuLabel label_3_3_3;
    private SudokuLabel label_4_1_1;
    private SudokuLabel label_4_1_2;
    private SudokuLabel label_4_1_3;
    private SudokuLabel label_4_2_1;
    private SudokuLabel label_4_2_2;
    private SudokuLabel label_4_2_3;
    private SudokuLabel label_4_3_1;
    private SudokuLabel label_4_3_2;
    private SudokuLabel label_4_3_3;
    private SudokuLabel label_5_1_1;
    private SudokuLabel label_5_1_2;
    private SudokuLabel label_5_1_3;
    private SudokuLabel label_5_2_1;
    private SudokuLabel label_5_2_2;
    private SudokuLabel label_5_2_3;
    private SudokuLabel label_5_3_1;
    private SudokuLabel label_5_3_2;
    private SudokuLabel label_5_3_3;
    private SudokuLabel label_6_1_1;
    private SudokuLabel label_6_1_2;
    private SudokuLabel label_6_1_3;
    private SudokuLabel label_6_2_1;
    private SudokuLabel label_6_2_2;
    private SudokuLabel label_6_2_3;
    private SudokuLabel label_6_3_1;
    private SudokuLabel label_6_3_2;
    private SudokuLabel label_6_3_3;
    private SudokuLabel label_7_1_1;
    private SudokuLabel label_7_1_2;
    private SudokuLabel label_7_1_3;
    private SudokuLabel label_7_2_1;
    private SudokuLabel label_7_2_2;
    private SudokuLabel label_7_2_3;
    private SudokuLabel label_7_3_1;
    private SudokuLabel label_7_3_2;
    private SudokuLabel label_7_3_3;
    private SudokuLabel label_8_1_1;
    private SudokuLabel label_8_1_2;
    private SudokuLabel label_8_1_3;
    private SudokuLabel label_8_2_1;
    private SudokuLabel label_8_2_2;
    private SudokuLabel label_8_2_3;
    private SudokuLabel label_8_3_1;
    private SudokuLabel label_8_3_2;
    private SudokuLabel label_8_3_3;
    private SudokuLabel label_9_1_1;
    private SudokuLabel label_9_1_2;
    private SudokuLabel label_9_1_3;
    private SudokuLabel label_9_2_1;
    private SudokuLabel label_9_2_2;
    private SudokuLabel label_9_2_3;
    private SudokuLabel label_9_3_1;
    private SudokuLabel label_9_3_2;
    private SudokuLabel label_9_3_3;
    private SudokuLabel notificationLabel;
    private javax.swing.JPanel panel_1_1;
    private javax.swing.JPanel panel_1_2;
    private javax.swing.JPanel panel_1_3;
    private javax.swing.JPanel panel_2_1;
    private javax.swing.JPanel panel_2_2;
    private javax.swing.JPanel panel_2_3;
    private javax.swing.JPanel panel_3_1;
    private javax.swing.JPanel panel_3_2;
    private javax.swing.JPanel panel_3_3;
    private SudokuLabel timeLabel;
    // End of variables declaration//GEN-END:variables
    
    class Notification implements ActionListener{
        private int type, counter = 0;
        private final Color defaultColor = buttonCheck.getBackground();
        public static final int LOADED_GAME  = 0;
        public static final int FILLED_FIELD = 1;

        public Notification(int type) {   this.type = type;     }

        public int getType() {   return type;     }
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (type){
                case 0:
                    if(counter%2 == 0)notificationLabel.setText(localisation.getLanguage().getSavedGameWasLoaded());
                    else notificationLabel.setText("");
                    if (counter == 9) { 
                        
                        timerLoaded.stop();
                    }
                    counter++;
                    break;
                case 1:
                    if(counter%2 == 0)  buttonCheck.setBackground(options.getColorMain());
                    else buttonCheck.setBackground(options.getColorAux());
                    counter++;
                    break;
                       
           }
        }
    }
    
    class Clock implements ActionListener{

        Calendar calendar = Calendar.getInstance();
        long startTime=calendar.getTimeInMillis();
        long currentTime, loadTime = 0;
        int hh, mm,ss;
        StringBuilder stb = new StringBuilder();
         @Override
        public void actionPerformed(ActionEvent e) {
            stb = new StringBuilder();
            currentTime = loadTime + (Calendar.getInstance().getTimeInMillis() - startTime)/1000;
            hh = (int)(currentTime / 3600);
            mm = (int)((currentTime%3600)/60);
            ss = (int)((currentTime%3600)%60);
            if(hh<10) stb.append('0');
            stb.append(hh);
            stb.append(':');
            if(mm<10) stb.append('0');
            stb.append(mm);
            stb.append(':');
            if(ss<10)stb.append('0');
            stb.append(ss);
            timeLabel.setText(stb.toString());
    }}}
   
