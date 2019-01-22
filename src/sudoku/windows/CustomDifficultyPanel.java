
package sudoku.windows;

import sudoku.MyLocalisation;
import sudoku.languages.LanguageListener;


public class CustomDifficultyPanel extends javax.swing.JPanel implements LanguageListener{
    
    private final MyLocalisation localisation = MyLocalisation.getInstance();
    
    public CustomDifficultyPanel() {
        initComponents();
        GameWindow.setCustomDifficulty(slider.getValue());
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        slider = new javax.swing.JSlider();
        cheatsCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();

        slider.setMajorTickSpacing(5);
        slider.setMaximum(70);
        slider.setMinimum(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setValue(30);
        slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderStateChanged(evt);
            }
        });

        cheatsCheckBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cheatsCheckBox.setText("Разрешить подсказки");
        cheatsCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                cheatsCheckBoxStateChanged(evt);
            }
        });

        jLabel2.setText("Количество открытых клеток:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cheatsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cheatsCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderStateChanged
        GameWindow.setCustomDifficulty(slider.getValue());
        slider.setToolTipText(String.valueOf(slider.getValue()));
    }//GEN-LAST:event_sliderStateChanged

    private void cheatsCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_cheatsCheckBoxStateChanged
        GameWindow.setCheats(cheatsCheckBox.isSelected());
        System.out.println("123");
    }//GEN-LAST:event_cheatsCheckBoxStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cheatsCheckBox;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSlider slider;
    // End of variables declaration//GEN-END:variables

    @Override
    public void languageChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag); 
        if(aFlag){
            localisation.addLanguageListener(this);
            languageChanged();
        }
    }
    
}
