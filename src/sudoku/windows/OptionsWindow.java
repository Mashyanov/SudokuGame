
package sudoku.windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sudoku.*;
import sudoku.languages.LanguageListener;
import sudoku.languages.LanguageRussian;

public class OptionsWindow extends javax.swing.JFrame implements LanguageListener{
    
    private final Options options;
    private Color colorMain, colorAux;
    private MyLocalisation localisation;
    
    public OptionsWindow() {
        initComponents();
        setTitle("Настройки игры");
        options = Options.getInstance();
        localisation = MyLocalisation.getInstance();
        languageBox.setSelectedIndex(options.getLanguage());
        colorMain= options.getColorMain();
        colorAux = options.getColorAux();
        autoCheckBox.setSelected(options.isAutoCheck());
        blockedSelectableBox.setSelected(options.isBlockedSelectable());
        darkerBackgroundBox.setSelected(options.isDarkerBackground());
        colorMainLabel.setBackground(colorMain);
        colorMainLabel.setBorder(BorderFactory.createLineBorder(colorMain.darker().darker()));
        colorMainLabel.setText("");
        colorAuxLabel.setBackground(colorAux);
        colorAuxLabel.setBorder(BorderFactory.createLineBorder(colorAux.darker().darker()));
        colorAuxLabel.setText("");
        
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b); 
        if(b) {
            localisation.addLanguageListener(this);
            languageChanged();
        }
    }
    
    
     @Override
    public void languageChanged() {
        labelLanguage.setText(localisation.getLanguage().getLabelLanguage());
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        autoCheckBox = new javax.swing.JCheckBox();
        blockedSelectableBox = new javax.swing.JCheckBox();
        darkerBackgroundBox = new javax.swing.JCheckBox();
        buttonSave = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonDefault = new javax.swing.JButton();
        colorMainLabel = new javax.swing.JLabel();
        colorAuxLabel = new javax.swing.JLabel();
        colorMainTextLabel = new javax.swing.JLabel();
        colorAuxTextLabel = new javax.swing.JLabel();
        languageBox = new javax.swing.JComboBox<>();
        labelLanguage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        autoCheckBox.setText("Предлагать проверку при заполнении поля");

        blockedSelectableBox.setText("Открытые клетки выделяются");

        darkerBackgroundBox.setText("Изменять фон открытых клеток");

        buttonSave.setText("Сохранить");
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveActionPerformed(evt);
            }
        });

        buttonCancel.setText("Отмена");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonDefault.setText("По-умолчанию");
        buttonDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDefaultActionPerformed(evt);
            }
        });

        colorMainLabel.setText("Main");
        colorMainLabel.setOpaque(true);
        colorMainLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMainLabelMouseClicked(evt);
            }
        });

        colorAuxLabel.setText("AUX");
        colorAuxLabel.setOpaque(true);
        colorAuxLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorAuxLabelMouseClicked(evt);
            }
        });

        colorMainTextLabel.setText("Цвет основного курсора");

        colorAuxTextLabel.setText("Цвет курсора-шпаргалки");

        languageBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Русский", "English", "Italiano" }));

        labelLanguage.setText("Язык:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(blockedSelectableBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(darkerBackgroundBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(colorMainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(colorMainTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(colorAuxLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(colorAuxTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autoCheckBox)
                            .addComponent(buttonDefault, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelLanguage)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(languageBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(languageBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelLanguage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(autoCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(blockedSelectableBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(darkerBackgroundBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(colorMainLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(colorMainTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(colorAuxLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(colorAuxTextLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(buttonDefault)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSave)
                    .addComponent(buttonCancel))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveActionPerformed
    
        options.setAutoCheck(autoCheckBox.isSelected());
        options.setBlockedSelectable(blockedSelectableBox.isSelected());
        options.setDarkerBackground(darkerBackgroundBox.isSelected());
        options.setColorMain(colorMain);
        options.setColorAux(colorAux);
        options.setLanguage(languageBox.getSelectedIndex());
        localisation.setLanguage(languageBox.getSelectedIndex());
        if(languageBox.getSelectedIndex()==2){
            JOptionPane.showMessageDialog(this, "итальянский еще не сделан\nВыставляем внезапно русский язык", "FIASCO", JOptionPane.ERROR_MESSAGE);
            localisation.setLanguage(new LanguageRussian());
        }
        options.setLanguage(MyLocalisation.RUSSIAN);
        options.saveToFile();
        
        GameWindow.repaintGameField();
        dispose();
    
        
    }//GEN-LAST:event_buttonSaveActionPerformed

    private void colorMainLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorMainLabelMouseClicked
        colorMain = JColorChooser.showDialog(this, "Выбор цвета основного курсора", options.getColorMain());
        colorMainLabel.setBackground(colorMain);
        colorMainLabel.setBorder(BorderFactory.createLineBorder(colorMain.darker().darker()));
    }//GEN-LAST:event_colorMainLabelMouseClicked

    private void colorAuxLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorAuxLabelMouseClicked
        colorAux = JColorChooser.showDialog(this, "Выбор цвета курсора-шпаргалки", options.getColorMain());
        colorAuxLabel.setBackground(colorAux);
        colorAuxLabel.setBorder(BorderFactory.createLineBorder(colorAux.darker().darker()));
    }//GEN-LAST:event_colorAuxLabelMouseClicked

    private void buttonDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDefaultActionPerformed
        autoCheckBox.setSelected(false);
        blockedSelectableBox.setSelected(true);
        darkerBackgroundBox.setSelected(true);
        colorMain = new Color(-8731663);
        colorMainLabel.setBackground(colorMain);
        colorMainLabel.setBorder(BorderFactory.createLineBorder(colorMain.darker().darker()));
        colorMainLabel.setText("");
        colorAux = new Color(-13108);
        colorAuxLabel.setBackground(colorAux);
        colorAuxLabel.setBorder(BorderFactory.createLineBorder(colorAux.darker().darker()));
        colorAuxLabel.setText("");
    }//GEN-LAST:event_buttonDefaultActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoCheckBox;
    private javax.swing.JCheckBox blockedSelectableBox;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonDefault;
    private javax.swing.JButton buttonSave;
    private javax.swing.JLabel colorAuxLabel;
    private javax.swing.JLabel colorAuxTextLabel;
    private javax.swing.JLabel colorMainLabel;
    private javax.swing.JLabel colorMainTextLabel;
    private javax.swing.JCheckBox darkerBackgroundBox;
    private javax.swing.JLabel labelLanguage;
    private javax.swing.JComboBox<String> languageBox;
    // End of variables declaration//GEN-END:variables

   
}
