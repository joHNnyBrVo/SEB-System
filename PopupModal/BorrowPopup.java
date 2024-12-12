package PopupModal;

import MainProgram.AppUtilities;
import MainProgram.Session;
import MainProgram.StaffDashboard;
import Database.dbConnection;
import PopupModal.AddItemPopup;
import com.formdev.flatlaf.FlatLightLaf;
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
import javax.swing.UIManager;

public class BorrowPopup extends javax.swing.JDialog {

    private Connection conn;
    private StaffDashboard borrowTable;

    public BorrowPopup(java.awt.Frame parent, boolean modal, StaffDashboard borrowTable) throws IOException {
        super(parent, modal);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        initComponents();
        
        setLocationRelativeTo(parent);
        
        this.borrowTable = borrowTable;

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

        utilities.customFontStyle(headerTxt, "Poppins-Bold.ttf", 18f);
        utilities.customFontStyle(borrowBtn, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(studInfoLabel, "Poppins-Regular.ttf", 18f);
        utilities.customFontStyle(equipDetailLabel, "Poppins-Regular.ttf", 18f);

        //Placeholder
        utilities.textPlaceholders(txtStudentID, "Student ID");
        utilities.textPlaceholders(txtStudentName, "Name of Student");
        utilities.setupComboBoxPlaceholder(txtCourse, "Select Course");
        utilities.setupComboBoxPlaceholder(txtYearlvl, "Select Year Level");
        utilities.setupComboBoxPlaceholder(txtEquipID, "Equipment ID");
        utilities.textPlaceholders(txtEquipName, "Name of Equipment");
        utilities.setupComboBoxPlaceholder(txtEquipQnty, "Quantity");

        //Fonstsyle
        utilities.customFontStyle(txtStudentID, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtStudentName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtCourse, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtYearlvl, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtEquipID, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtEquipName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtEquipQnty, "Poppins-Regular.ttf", 16f);
    }

    private void clearFields() {
        txtStudentID.setText("");
        txtStudentName.setText("");
        txtEquipName.setText("");
        txtCourse.setSelectedIndex(0);
        txtYearlvl.setSelectedIndex(0);
        txtEquipID.setSelectedIndex(0);
        txtEquipQnty.setSelectedIndex(0);
    }

    //Disable TextField
    public void disableFields() {
        txtStudentID.setEnabled(false);
        txtEquipID.setEnabled(false);
        txtStudentName.setEnabled(false);
        txtCourse.setEnabled(false);
        txtYearlvl.setEnabled(false);
        txtEquipName.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        headerTxt = new javax.swing.JLabel();
        txtStudentID = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        studInfoLabel = new javax.swing.JLabel();
        txtStudentName = new javax.swing.JTextField();
        txtCourse = new javax.swing.JComboBox<>();
        txtYearlvl = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        equipDetailLabel = new javax.swing.JLabel();
        txtEquipName = new javax.swing.JTextField();
        txtEquipID = new javax.swing.JComboBox<>();
        txtEquipQnty = new javax.swing.JComboBox<>();
        borrowBtn = new MainProgram.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        headerPanel.setBackground(new java.awt.Color(8, 194, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/borrow.png"))); // NOI18N

        headerTxt.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        headerTxt.setForeground(new java.awt.Color(255, 255, 255));
        headerTxt.setText("Borrow Equipment");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(headerTxt)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(headerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        studInfoLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        studInfoLabel.setForeground(new java.awt.Color(0, 0, 0));
        studInfoLabel.setText("Student Information");

        txtCourse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bachelor of Elementary Education (BEED)", "Bachelor of Physical Education (BPED)", "BS in Civil Engineering (BSCE)", "BS in Electrical Engineering (BSEE)", "BS in Hospitality Management (BSHM)", "BS in Information Technology (BSIT)", "BS in Industrial Technology Major in Culinary Arts (BS Ind. CA)" }));

        txtYearlvl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        equipDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        equipDetailLabel.setForeground(new java.awt.Color(0, 0, 0));
        equipDetailLabel.setText("Equipment Details");

        txtEquipID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BS-BB-001", "BS-VB-002 ", "BS-BB-003 ", "RB-BR-004 ", "RB-TP-005 ", "RB-BB-006 ", "NG-SG-007", "NG-VN-008 ", "NG-BN-009 ", " " }));

        txtEquipQnty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        borrowBtn.setBackground(new java.awt.Color(13, 110, 244));
        borrowBtn.setForeground(new java.awt.Color(255, 255, 255));
        borrowBtn.setText("Confirm Borrow");
        borrowBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                borrowBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                borrowBtnMouseReleased(evt);
            }
        });
        borrowBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(borrowBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(studInfoLabel)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtYearlvl, javax.swing.GroupLayout.Alignment.LEADING, 0, 176, Short.MAX_VALUE)
                                    .addComponent(txtStudentID, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addComponent(txtStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2)
                            .addComponent(equipDetailLabel)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(txtEquipID, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtEquipName, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtEquipQnty, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(23, Short.MAX_VALUE))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(studInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStudentName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(txtYearlvl, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(equipDetailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEquipName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEquipID, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEquipQnty, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(borrowBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
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

    private void borrowBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowBtnActionPerformed
        String StudentID = txtStudentID.getText();
        String StudentName = txtStudentName.getText();
        String Course = (String) txtCourse.getSelectedItem();
        String selectedYear = txtYearlvl.getSelectedItem().toString();
        String EquipID = (String) txtEquipID.getSelectedItem();
        String EquipName = txtEquipName.getText();
        String selectedQuantity = txtEquipQnty.getSelectedItem().toString();

        if (StudentID.isEmpty() && StudentName.isEmpty() && Course.equalsIgnoreCase("Select Course")
                && selectedYear.isEmpty() && (EquipID == null || EquipID.equals("Select Equipment ID")) && EquipName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (StudentID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Student ID field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (StudentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Student Name field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Course.equalsIgnoreCase("Select Course")) {
            JOptionPane.showMessageDialog(this, "Please select a course", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int Yearlvl = 0;
        try {
            Yearlvl = Integer.parseInt(selectedYear);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Year level", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (EquipID == null || EquipID.equals("Select Equipment ID")) {
            JOptionPane.showMessageDialog(this, "Please select Equipment ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (EquipName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Equipment Name field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (EquipName != null) {
            String equipNameLower = EquipName.toLowerCase();

            boolean isValid = false;

            if (equipNameLower.contains("basketball") && EquipID.startsWith("BS-BB-001")) {
                isValid = true;
            } else if (equipNameLower.contains("volleyball") && EquipID.startsWith("BS-VB-002")) {
                isValid = true;
            } else if (equipNameLower.contains("baseball") && EquipID.startsWith("BS-BB-003")) {
                isValid = true;
            } else if (equipNameLower.contains("badminton racket") && EquipID.startsWith("RB-BR-004")) {
                isValid = true;
            } else if (equipNameLower.contains("table tennis racket") && EquipID.startsWith("RB-TP-005")) {
                isValid = true;
            } else if (equipNameLower.contains("baseball bat") && EquipID.startsWith("RB-BB-006")) {
                isValid = true;
            } else if (equipNameLower.contains("soccer goal net") && EquipID.startsWith("NG-SG-007")) {
                isValid = true;
            } else if (equipNameLower.contains("volleyball net") && EquipID.startsWith("NG-VN-008")) {
                isValid = true;
            } else if (equipNameLower.contains("badminton net") && EquipID.startsWith("NG-BN-009")) {
                isValid = true;
            }

            if (!isValid) {
                JOptionPane.showMessageDialog(this, "Invalid equipment name or ID mismatch.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int quantity = 0;
        try {
            quantity = Integer.parseInt(selectedQuantity);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the logged-in staff ID from the Session class
        int staffID = Session.getStaffID();
        if (staffID <= 0) {
            JOptionPane.showMessageDialog(this, "Staff ID not found. Please log in again.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO borrow_transaction (student_id, student_name, course_name, year_level, equipment_id, equipment_name, quantity_borrowed, borrow_date, due_date, transaction_status, staff_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 1 MINUTE), 'Borrowed', ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, StudentID);
            pstmt.setString(2, StudentName);
            pstmt.setString(3, Course);
            pstmt.setInt(4, Yearlvl);
            pstmt.setString(5, EquipID);
            pstmt.setString(6, EquipName);
            pstmt.setInt(7, quantity);
            pstmt.setInt(8, staffID);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Transaction successfully recorded!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                borrowTable.displayBorrowRecords();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to record transaction.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            pstmt.close();
        } catch (SQLException e) {
            if (e.getMessage().contains("Not enough")) {
                JOptionPane.showMessageDialog(this, "Not enough inventory or equipment does not exist", "Message", JOptionPane.ERROR_MESSAGE);
            } else {
                // For other SQL errors, show a generic error message
                JOptionPane.showMessageDialog(this, "An error occurred while adding the equipment.", "SQL Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_borrowBtnActionPerformed

    private void borrowBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(borrowBtn, "#0B5CC9");
    }//GEN-LAST:event_borrowBtnMousePressed

    private void borrowBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(borrowBtn, "#0D6EF4");
    }//GEN-LAST:event_borrowBtnMouseReleased

    public static void main(String args[]) {
        //Set up flatlaf theme
        FlatMacLightLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StaffDashboard borrowTable = new StaffDashboard(); //Pwede tangtangon ang null if naay wrong

                    BorrowPopup dialog = new BorrowPopup(new javax.swing.JFrame(), true, borrowTable);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            dialog.dispose();
                        }
                    });
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(BorrowPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private MainProgram.Button borrowBtn;
    private javax.swing.JLabel equipDetailLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTxt;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel studInfoLabel;
    private javax.swing.JComboBox<String> txtCourse;
    private javax.swing.JComboBox<String> txtEquipID;
    private javax.swing.JTextField txtEquipName;
    private javax.swing.JComboBox<String> txtEquipQnty;
    private javax.swing.JTextField txtStudentID;
    private javax.swing.JTextField txtStudentName;
    private javax.swing.JComboBox<String> txtYearlvl;
    // End of variables declaration//GEN-END:variables
}
