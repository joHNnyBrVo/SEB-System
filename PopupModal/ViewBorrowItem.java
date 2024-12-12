package PopupModal;

import MainProgram.AppUtilities;
import Database.dbConnection;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Dialog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ViewBorrowItem extends javax.swing.JDialog {

    private Connection conn;

    public ViewBorrowItem(java.awt.Frame parent, boolean modal) throws IOException {
        super(parent, modal);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();

        setLocationRelativeTo(parent);

        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PopupModal.AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PopupModal.AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        AppUtilities utilities = new AppUtilities();

        utilities.customFontStyle(headerTxt, "Poppins-Bold.ttf", 18f);
        utilities.customFontStyle(studInfoLabel, "Poppins-SemiBold.ttf", 18f);
        utilities.customFontStyle(equipDetailLabel, "Poppins-SemiBold.ttf", 18f);

        utilities.customFontStyle(transactionID, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(studentID, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(studentName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(course, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(yearLevel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(equipmentID, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(EquipmentName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(quantity, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(borrowDate, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(dueDate, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(status, "Poppins-Regular.ttf", 16f);
    }

    public void setFields(String transactionId, String studentName, String equipmentName,
            String quantity, String borrowDate, String dueDate, String status) {
        try {
            String sql = "SELECT * FROM borrow_transaction WHERE transaction_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, transactionId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                transactionID.setText("Transaction ID: " + rs.getString("transaction_id"));

                studentID.setText("Student ID: " + rs.getString("student_id"));
                this.studentName.setText("Student Name: " + rs.getString("student_name"));
                course.setText("Course: " + rs.getString("course_name"));
                yearLevel.setText("Year Level: " + rs.getString("year_level"));

                equipmentID.setText("Equipment ID: " + rs.getString("equipment_id"));
                EquipmentName.setText("Equipment Name: " + rs.getString("equipment_name"));
                this.quantity.setText("Quantity Borrowed: " + rs.getString("quantity_borrowed"));

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a");
                this.borrowDate.setText("Borrow Date: " + dateFormat.format(rs.getTimestamp("borrow_date")));
                this.dueDate.setText("Due Date: " + dateFormat.format(rs.getTimestamp("due_date")));
                this.status.setText("Status: " + rs.getString("transaction_status"));
            }

            rs.close();
            pst.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerTxt = new javax.swing.JLabel();
        transactionID = new javax.swing.JLabel();
        studentID = new javax.swing.JLabel();
        studentName = new javax.swing.JLabel();
        course = new javax.swing.JLabel();
        yearLevel = new javax.swing.JLabel();
        equipmentID = new javax.swing.JLabel();
        EquipmentName = new javax.swing.JLabel();
        quantity = new javax.swing.JLabel();
        borrowDate = new javax.swing.JLabel();
        dueDate = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        studInfoLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        equipDetailLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(840, 471));
        setType(java.awt.Window.Type.POPUP);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerPanel.setBackground(new java.awt.Color(8, 194, 255));

        headerTxt.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        headerTxt.setForeground(new java.awt.Color(255, 255, 255));
        headerTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/borrow.png"))); // NOI18N
        headerTxt.setText("Borrowed Details");
        headerTxt.setPreferredSize(new java.awt.Dimension(145, 30));

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(540, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        transactionID.setBackground(new java.awt.Color(255, 255, 255));
        transactionID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        transactionID.setForeground(new java.awt.Color(51, 51, 51));
        transactionID.setText("Transaction ID");
        mainPanel.add(transactionID, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 109, -1, -1));

        studentID.setBackground(new java.awt.Color(255, 255, 255));
        studentID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        studentID.setForeground(new java.awt.Color(51, 51, 51));
        studentID.setText("Student ID");
        mainPanel.add(studentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 136, -1, -1));

        studentName.setBackground(new java.awt.Color(255, 255, 255));
        studentName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        studentName.setForeground(new java.awt.Color(51, 51, 51));
        studentName.setText("Student Name");
        mainPanel.add(studentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 136, -1, -1));

        course.setBackground(new java.awt.Color(255, 255, 255));
        course.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        course.setForeground(new java.awt.Color(51, 51, 51));
        course.setText("Course");
        mainPanel.add(course, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 163, -1, -1));

        yearLevel.setBackground(new java.awt.Color(255, 255, 255));
        yearLevel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        yearLevel.setForeground(new java.awt.Color(51, 51, 51));
        yearLevel.setText(" Year Level");
        mainPanel.add(yearLevel, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 163, -1, -1));

        equipmentID.setBackground(new java.awt.Color(255, 255, 255));
        equipmentID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        equipmentID.setForeground(new java.awt.Color(51, 51, 51));
        equipmentID.setText("Equipment ID");
        mainPanel.add(equipmentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, -1, -1));

        EquipmentName.setBackground(new java.awt.Color(255, 255, 255));
        EquipmentName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        EquipmentName.setForeground(new java.awt.Color(51, 51, 51));
        EquipmentName.setText("Equipment Name");
        mainPanel.add(EquipmentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 270, -1, -1));

        quantity.setBackground(new java.awt.Color(255, 255, 255));
        quantity.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        quantity.setForeground(new java.awt.Color(51, 51, 51));
        quantity.setText("Quantity");
        mainPanel.add(quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 297, -1, -1));

        borrowDate.setBackground(new java.awt.Color(255, 255, 255));
        borrowDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        borrowDate.setForeground(new java.awt.Color(51, 51, 51));
        borrowDate.setText("Borrowed Date");
        mainPanel.add(borrowDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 324, -1, -1));

        dueDate.setBackground(new java.awt.Color(255, 255, 255));
        dueDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dueDate.setForeground(new java.awt.Color(51, 51, 51));
        dueDate.setText("Due Date");
        mainPanel.add(dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 324, -1, -1));

        status.setBackground(new java.awt.Color(255, 255, 255));
        status.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        status.setForeground(new java.awt.Color(51, 51, 51));
        status.setText("Status");
        mainPanel.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 297, -1, -1));

        studInfoLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        studInfoLabel.setForeground(new java.awt.Color(0, 0, 0));
        studInfoLabel.setText("Student Information");
        mainPanel.add(studInfoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 21));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        mainPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 820, 16));

        equipDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        equipDetailLabel.setForeground(new java.awt.Color(0, 0, 0));
        equipDetailLabel.setText("Equipment Details");
        mainPanel.add(equipDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, 21));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        mainPanel.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 820, 16));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        FlatMacLightLaf.setup();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewBorrowItem dialog = new ViewBorrowItem(new javax.swing.JFrame(), true);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(ViewBorrowItem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel EquipmentName;
    private javax.swing.JLabel borrowDate;
    private javax.swing.JLabel course;
    private javax.swing.JLabel dueDate;
    private javax.swing.JLabel equipDetailLabel;
    private javax.swing.JLabel equipmentID;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel quantity;
    private javax.swing.JLabel status;
    private javax.swing.JLabel studInfoLabel;
    private javax.swing.JLabel studentID;
    private javax.swing.JLabel studentName;
    private javax.swing.JLabel transactionID;
    private javax.swing.JLabel yearLevel;
    // End of variables declaration//GEN-END:variables
}
