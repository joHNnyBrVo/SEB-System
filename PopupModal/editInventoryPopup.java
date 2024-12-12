package PopupModal;

import MainProgram.AppUtilities;
import MainProgram.AppUtilities;
import Database.dbConnection;
import Database.dbConnection;
import java.awt.Dialog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class editInventoryPopup extends javax.swing.JDialog {

    private Connection conn;

    public editInventoryPopup(java.awt.Frame parent, boolean modal) throws IOException {
        super(parent, modal);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        initComponents();
        
        setLocationRelativeTo(parent);
        
        disableEntry();

        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PopupModal.AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PopupModal.AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Instance AppUtilities (Singleton Code)
        AppUtilities utilities = new AppUtilities();

        //FontStyle
        utilities.customFontStyle(headerTxtPanel, "Poppins-Bold.ttf", 18f);
        utilities.customFontStyle(saveBtn, "Poppins-Regular.ttf", 16f);

        //Placeholder
        utilities.textPlaceholders(txtEquipID, "Equipment ID");
        utilities.textPlaceholders(txtEquipName, "Name of Equipment");
        utilities.textPlaceholders(txtEquipQnty, "Quantity");
        utilities.setupComboBoxPlaceholder(txtEquipType, "Select Equipment Type");

        utilities.customFontStyle(txtEquipID, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtEquipName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtEquipType, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtEquipQnty, "Poppins-Regular.ttf", 16f);
    }

    public void setFields(String equipmentID, String equipmentName, String equipmentType, String equipmentQuantity) {
        txtEquipID.setText(equipmentID);
        txtEquipName.setText(equipmentName);
        txtEquipType.setSelectedItem(equipmentType);
        txtEquipQnty.setText(equipmentQuantity);
    }

    private void clearFields() {
        txtEquipID.setText("");
        txtEquipName.setText("");
        txtEquipType.setSelectedIndex(0);
        txtEquipQnty.setText("");
    }
    
    public void disableEntry() {
        txtEquipID.setEnabled(false);
        txtEquipName.setEnabled(false);
        txtEquipType.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerTxtPanel = new javax.swing.JLabel();
        txtEquipID = new javax.swing.JTextField();
        txtEquipName = new javax.swing.JTextField();
        txtEquipQnty = new javax.swing.JTextField();
        txtEquipType = new javax.swing.JComboBox<>();
        saveBtn = new MainProgram.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(452, 481));

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        headerPanel.setBackground(new java.awt.Color(8, 194, 255));

        headerTxtPanel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        headerTxtPanel.setForeground(new java.awt.Color(255, 255, 255));
        headerTxtPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventory.png"))); // NOI18N
        headerTxtPanel.setText("Edit Equipment Inventory");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxtPanel)
                .addContainerGap(221, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxtPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        txtEquipType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ball Sport", "Rackets or Bats", "Protective Gear", "Nets or Goals" }));
        txtEquipType.setMinimumSize(new java.awt.Dimension(72, 40));

        saveBtn.setBackground(new java.awt.Color(13, 110, 244));
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setText("Save");
        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                saveBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                saveBtnMouseReleased(evt);
            }
        });
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtEquipID)
                        .addComponent(txtEquipName, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                        .addComponent(txtEquipQnty, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                        .addComponent(txtEquipType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(txtEquipID, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEquipName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEquipType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEquipQnty, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        String equipmentID = txtEquipID.getText();
        String equipmentName = txtEquipName.getText();
        String equipmentType = (String) txtEquipType.getSelectedItem();
        String equipmentQnty = txtEquipQnty.getText();

        if (equipmentName != null) {
            String equipNameLower = equipmentName.toLowerCase();
            if (equipmentType != null) {
                String equipTypeLower = equipmentType.toLowerCase();

                boolean isValid = false;

                if (equipNameLower.contains("basketball") && equipmentID.startsWith("BS-BB-001") && equipTypeLower.contains("ball sport")) {
                    isValid = true;
                } else if (equipNameLower.contains("volleyball") && equipmentID.startsWith("BS-VB-002") && equipTypeLower.contains("ball sport")) {
                    isValid = true;
                } else if (equipNameLower.contains("baseball") && equipmentID.startsWith("BS-BB-003") && equipTypeLower.contains("ball sport")) {
                    isValid = true;
                } else if (equipNameLower.contains("badminton racket") && equipmentID.startsWith("RB-BR-004") && equipTypeLower.contains("rackets or bats")) {
                    isValid = true;
                } else if (equipNameLower.contains("table tennis racket") && equipmentID.startsWith("RB-TP-005") && equipTypeLower.contains("rackets or bats")) {
                    isValid = true;
                } else if (equipNameLower.contains("baseball bat") && equipmentID.startsWith("RB-BB-006") && equipTypeLower.contains("rackets or bats")) {
                    isValid = true;
                } else if (equipNameLower.contains("soccer goal net") && equipmentID.startsWith("NG-SG-007") && equipTypeLower.contains("nets or goals")) {
                    isValid = true;
                } else if (equipNameLower.contains("volleyball net") && equipmentID.startsWith("NG-VN-008") && equipTypeLower.contains("nets or goals")) {
                    isValid = true;
                } else if (equipNameLower.contains("badminton net") && equipmentID.startsWith("NG-BN-009") && equipTypeLower.contains("nets or goals")) {
                    isValid = true;
                }

                if (!isValid) {
                    JOptionPane.showMessageDialog(this, "Invalid equipment name or ID mismatch.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (equipmentID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Equipment ID fields", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (equipmentName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Equipment Name fields", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (equipmentType.equalsIgnoreCase("Select Equipment Type")) {
                JOptionPane.showMessageDialog(this, "Please select equipment type", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (equipmentQnty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Quantity fields", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(equipmentQnty);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantity must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String sql = "UPDATE inventory SET equipmentName = ?, type = ?, quantity = ? WHERE equipmentID = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, equipmentName);
                pstmt.setString(2, equipmentType);
                pstmt.setInt(3, quantity);
                pstmt.setString(4, equipmentID);

                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Equipment successfully updated in inventory!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No record found with the given Equipment ID.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                }

                pstmt.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void saveBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(saveBtn, "#0B5CC9");
    }//GEN-LAST:event_saveBtnMousePressed

    private void saveBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(saveBtn, "#0D6EF4");
    }//GEN-LAST:event_saveBtnMouseReleased

    public static void main(String args[]) {

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    editInventoryPopup dialog = new editInventoryPopup(new javax.swing.JFrame(), true);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(editInventoryPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTxtPanel;
    private javax.swing.JPanel mainPanel;
    private MainProgram.Button saveBtn;
    private javax.swing.JTextField txtEquipID;
    private javax.swing.JTextField txtEquipName;
    private javax.swing.JTextField txtEquipQnty;
    private javax.swing.JComboBox<String> txtEquipType;
    // End of variables declaration//GEN-END:variables
}
