
package sudoku;

import java.io.Serializable;
import java.util.Calendar;

public class Result implements Serializable{
    private final int time;
    private final String difficulty;
    private final Calendar date;
    private String name;

    public int      getTime()        {  return time;          }
    public String   getDifficulty()  {  return difficulty;    }
    public String   getName()        {  return name;          }
    public Calendar getDate()        {  return date;          }

    public void setName(String name) {  this.name = name;     }
    
    public Result(int time, String difficulty, Calendar date) {
        this.time = time;
        this.difficulty = difficulty;
        this.date = date;
        this.name = "anonimus";
    }

    
    
    
    @Override
    public String toString() {
        return "Result{" + "time=" + time + ", difficulty=" + difficulty + ", name=" + name + ", date=" + date.getTime().getTime() + '}';
    }

    
    
}
