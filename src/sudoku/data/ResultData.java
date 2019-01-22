
package sudoku.data;

import java.util.LinkedList;
import javax.swing.AbstractListModel;


public class ResultData extends AbstractListModel<String>{
   private LinkedList<String> list = new LinkedList<>();

    public LinkedList<String> getList() {
        return list;
    }

    public void fireContentsChanged(){
        fireContentsChanged(this, 0, list.size());
    }
    
    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public String getElementAt(int index) {
        return list.get(index);
    } 
}
