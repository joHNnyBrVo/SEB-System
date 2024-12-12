package PopupModal;

import MainProgram.AdminDashboard;
import MainProgram.AppUtilities;
import MainProgram.AppUtilities;
import MainProgram.Session;
import Database.dbConnection;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Dialog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class AddItemPopup extends javax.swing.JDialog {

    private Connection conn;
    private AdminDashboard inventoryTable;     //

    public AddItemPopup(java.awt.Frame parent, boolean modal, AdminDashboard inventoryTable) throws IOException {
        super(parent, modal);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();

        setLocationRelativeTo(parent);

        //
        this.inventoryTable = inventoryTable;

        //DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Instance AppUtilities (Singleton Code)
        AppUtilities utilities = new AppUtilities();

        //FontStyle
        utilities.customFontStyle(headerTxtPanel, "Poppins-Bold.ttf", 18f);
        utilities.customFontStyle(addBtn, "Poppins-Regular.ttf", 16f);

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

    private void clearFields() {
        txtEquipID.setText("");
        txtEquipName.setText("");
        txtEquipType.setSelectedIndex(0);
        txtEquipQnty.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerTxtPanel = new javax.swing.JLabel();
        addBtn = new MainProgram.Button();
        txtEquipID = new javax.swing.JTextField();
        txtEquipName = new javax.swing.JTextField();
        txtEquipType = new javax.swing.JComboBox<>();
        txtEquipQnty = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setMaximumSize(new java.awt.Dimension(466, 481));
        mainPanel.setPreferredSize(new java.awt.Dimension(466, 481));

        headerPanel.setBackground(new java.awt.Color(8, 194, 255));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerTxtPanel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        headerTxtPanel.setForeground(new java.awt.Color(255, 255, 255));
        headerTxtPanel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus.png"))); // NOI18N
        headerTxtPanel.setText("Add Equipment to Inventory");
        headerPanel.add(headerTxtPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, 32));

        addBtn.setBackground(new java.awt.Color(13, 110, 244));
        addBtn.setForeground(new java.awt.Color(255, 255, 255));
        addBtn.setText("Submit");
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                addBtnMouseReleased(evt);
            }
        });
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        txtEquipType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ball Sport", "Rackets or Bats", "Protective Gear", "Nets or Goals" }));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtEquipType, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEquipName, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEquipID, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEquipQnty, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(txtEquipID, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEquipName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEquipType, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEquipQnty, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(addBtn, "#0B5CC9");
    }//GEN-LAST:event_addBtnMousePressed

    private void addBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(addBtn, "#0D6EF4");
    }//GEN-LAST:event_addBtnMouseReleased

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        String equipmentID = txtEquipID.getText();
        String equipmentName = txtEquipName.getText();
        String equipmentType = (String) txtEquipType.getSelectedItem();
        String equipmentQnty = txtEquipQnty.getText();

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

        int quantity = 0;
        try {
            quantity = Integer.parseInt(equipmentQnty);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int adminID = Session.getAdminID();
        if (adminID <= 0) {
            JOptionPane.showMessageDialog(this, "Admin ID not found. Please log in again.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO inventory (equipmentID, equipmentName, type, quantity, admin_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, equipmentID);
            pstmt.setString(2, equipmentName);
            pstmt.setString(3, equipmentType);
            pstmt.setInt(4, quantity);
            pstmt.setInt(5, adminID);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Equipment successfully added to inventory!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                inventoryTable.displayInventory();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add equipment to inventory.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "This equipment already exists.", "Message", JOptionPane.WARNING_MESSAGE);
            } else {
                // For other SQL errors, show a generic error message
                JOptionPane.showMessageDialog(this, "An error occurred while adding the equipment.", "SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addBtnActionPerformed

    public static void main(String args[]) {
        //Set up flatlaf theme
        FlatMacLightLaf.setup();

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    AdminDashboard inventoryTable = new AdminDashboard(); //wala ni cyay null dapat

                    AddItemPopup dialog = new AddItemPopup(new javax.swing.JFrame(), true, inventoryTable);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            dialog.dispose();
                        }
                    });
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private MainProgram.Button addBtn;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTxtPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField txtEquipID;
    private javax.swing.JTextField txtEquipName;
    private javax.swing.JTextField txtEquipQnty;
    private javax.swing.JComboBox<String> txtEquipType;
    // End of variables declaration//GEN-END:variables
}
