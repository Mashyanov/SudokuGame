package sudoku;

import sudoku.windows.GameWindow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ResultsTable {
private ArrayList<Result> resultList;
private final File resultsFile = new File("results.dat");
private final Options options = Options.getInstance();

    public ResultsTable() {
        try {
            if(!resultsFile.exists()) 
                resultsFile.createNewFile();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(resultsFile));
            resultList = (ArrayList<Result>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
            
        } 
    }
    /**Метод возвращает размер спика лидеров по заданной сложности
     * @param difficulty - задаваемая сложность.
     * @return  размер спика лидеров по заданной сложности*/
    public int getSize(String difficulty){
        ArrayList<Result> tempList = new ArrayList<>();
        for (Result r : resultList) {
            if(r.getDifficulty().equals(difficulty)) tempList.add(r);
        }
        return tempList.size();
    }
    
    /**Метод получения худшего результата по заданной сложности
     * @param difficulty - заданная сложность
     * @return Result с наибольшим показателем времени по заданной сложности или NULL,
     * если список по сложности пуст*/
    public Result getWorstResult(String difficulty){
        if (resultList==null) return null;
        ArrayList<Result> tempList = new ArrayList<>();
        for (Result r : resultList) {
            
            if(r.getDifficulty().equals(difficulty)) tempList.add(r);
        }
        if(tempList.isEmpty()) return null;
        tempList.sort(new ResultComparator());
        return tempList.get(tempList.size()-1);
    }
    
    /**Добавляет новый результат в таблицу, удаляет худший по заданной сложности
     * @param result новый результат*/
    public void addResult(Result result){
    try {
        result.setName(JOptionPane.showInputDialog(new JPanel(), "Представьтесь", "Сохранение результата", JOptionPane.PLAIN_MESSAGE));
        if(result.getName()==null) return;
        if(resultsFile.exists()) resultsFile.delete();
        resultsFile.createNewFile();
        if(getWorstResult(result.getDifficulty())!=null && getList(result.getDifficulty()).size() >= options.MAX_RESULTS) 
            resultList.remove(getWorstResult(result.getDifficulty()));
        if(resultList == null) resultList = new ArrayList<>();
        resultList.add(result);       
   
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(resultsFile))) {
            oos.writeObject(resultList);
        }
    } catch (IOException ex ) {
        Logger.getLogger(ResultsTable.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    /**Метод бла бла бла
     * @param difficulty */
    public void clearList(String difficulty){
        ObjectOutputStream oos;
    try {
        //Делаем это через вспомогательную коллекцию, потому что, если пытаться удалить 
        //напрямую из коллекции через цикл FOR-EACH, у нас выскакивает 
        //ConcurrentModificationException
        ArrayList<Result> temp = new ArrayList<>();
        for (Result result : resultList)  
            temp.add(result);
        resultList.clear();
        for (Result result : temp) 
            if(!result.getDifficulty().equals(difficulty))
                resultList.add(result);
        oos = new ObjectOutputStream(new FileOutputStream(resultsFile));
        oos.writeObject(resultList);
        oos.close();
    } catch (FileNotFoundException ex) {
        Logger.getLogger(ResultsTable.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(ResultsTable.class.getName()).log(Level.SEVERE, null, ex);
    
    }
    }
    
    /**Метод получения списка лидеров по заданной сложности. Список уже отсортирован
     * @param difficulty - заданная сложность
     * @return список лучших результатов по заданной сложности или NULL, если список пуст */
    public ArrayList<Result> getList(String difficulty){
        ArrayList<Result> list = new ArrayList<>();
        for (Result r : resultList) {
            if(r.getDifficulty().equals(difficulty)) list.add(r);
        }
        if(list.isEmpty()) return null;
        list.sort(new ResultComparator());
    
                
        return list;
    }
    
    class ResultComparator implements Comparator<Result>{

        @Override
        public int compare(Result r1, Result r2) {
            int res = r1.getTime()-r2.getTime();
            if (res == 0) res = r1.getDate().compareTo(r2.getDate());
            return res;
        }        
    }
          
}
