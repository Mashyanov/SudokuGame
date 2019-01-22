
package sudoku.windows;

import sudoku.MyLocalisation;
import sudoku.languages.LanguageListener;


public class RulesPanel extends javax.swing.JPanel implements LanguageListener{
private int page;
private final MyLocalisation localisation = MyLocalisation.getInstance();
    public RulesPanel() {
        initComponents();
        page = 0;
        fillThePanel();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if(aFlag){
            localisation.addLanguageListener(this);
            languageChanged();
        }
    }
    
        @Override
    public void languageChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pictureLabel = new javax.swing.JLabel();
        nextButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        pictureLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        pictureLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pictureLabel.setText("jLabel1");
        pictureLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        nextButton.setText("Далее>>>");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        backButton.setText("<<<Назад");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pictureLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                .addComponent(nextButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pictureLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(nextButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        page++;
        fillThePanel();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        page--;
        fillThePanel();
    }//GEN-LAST:event_backButtonActionPerformed

    private void fillThePanel(){
        switch(page){
            case 0:
                backButton.setEnabled(false);
                pictureLabel.setText(Integer.toString(page));
                break;
            case 1:
                backButton.setEnabled(true);
                pictureLabel.setText(Integer.toString(page));
                break;
            case 2:
                pictureLabel.setText(Integer.toString(page));
                break;
            case 3:
                nextButton.setEnabled(true);
                pictureLabel.setText(Integer.toString(page));
                break;
            case 4:
                nextButton.setEnabled(false);
                pictureLabel.setText(Integer.toString(page));
                break;
                
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel pictureLabel;
    // End of variables declaration//GEN-END:variables

   
    

}
