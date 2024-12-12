package raven.cell;

import MainProgram.AppUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ReturnAction extends javax.swing.JPanel {

    public ReturnAction() throws IOException {
        initComponents();
        
        //Instance AppUtilities
        AppUtilities utilities = new AppUtilities();
        
        //Custom fonstyle
        utilities.customFontStyle(returnBtn, "Poppins-SemiBold.ttf", 12f);
    }
    
    public void initEvent(TableActionEvent event, int row) {
        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.onReturnBorrowtBtn(row);
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        returnBtn = new javax.swing.JButton();

        returnBtn.setBackground(new java.awt.Color(8, 194, 255));
        returnBtn.setForeground(new java.awt.Color(255, 255, 255));
        returnBtn.setText("Mark as return");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(returnBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(returnBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton returnBtn;
    // End of variables declaration//GEN-END:variables
}
