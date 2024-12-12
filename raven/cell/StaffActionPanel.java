package raven.cell;

import MainProgram.AppUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StaffActionPanel extends javax.swing.JPanel {

    public StaffActionPanel() throws IOException {
        initComponents();
        
        //Instance AppUtilities
        AppUtilities utilities = new AppUtilities();
        
        //Custom fonstyle
        utilities.customFontStyle(activeBtn, "Poppins-SemiBold.ttf", 12f);
        utilities.customFontStyle(diactiveBtn, "Poppins-SemiBold.ttf", 12f);
        utilities.customFontStyle(removeBtn, "Poppins-SemiBold.ttf", 12f);
    }
    
public void initEvent(TableActionEvent event, int row) {
    activeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.onActiveBtn(row);
            }
        });
        
        diactiveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.onDiactiveBtn(row);
            }
        });
        
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.onRemoveBtn(row);
            }
        });
}
       
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        activeBtn = new javax.swing.JButton();
        diactiveBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();

        activeBtn.setBackground(new java.awt.Color(8, 194, 255));
        activeBtn.setForeground(new java.awt.Color(255, 255, 255));
        activeBtn.setText("Activate");

        diactiveBtn.setBackground(new java.awt.Color(8, 194, 255));
        diactiveBtn.setForeground(new java.awt.Color(255, 255, 255));
        diactiveBtn.setText("Diactivate");

        removeBtn.setBackground(new java.awt.Color(255, 0, 0));
        removeBtn.setForeground(new java.awt.Color(255, 255, 255));
        removeBtn.setText("Remove");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(activeBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(diactiveBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(removeBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(activeBtn)
                    .addComponent(diactiveBtn)
                    .addComponent(removeBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activeBtn;
    private javax.swing.JButton diactiveBtn;
    private javax.swing.JButton removeBtn;
    // End of variables declaration//GEN-END:variables


}
