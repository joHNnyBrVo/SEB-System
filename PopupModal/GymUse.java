package PopupModal;

import Database.dbConnection;
import MainProgram.AppUtilities;
import MainProgram.Session;
import MainProgram.StaffDashboard;
import PopupModal.AddItemPopup;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Dialog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class GymUse extends javax.swing.JDialog {

    private Connection conn;
    private StaffDashboard checkInOutTable;

    public GymUse(java.awt.Frame parent, boolean modal, StaffDashboard checkInOutTable) throws IOException {
        super(parent, modal);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();

        setLocationRelativeTo(parent);
        this.checkInOutTable = checkInOutTable;
        initializeButtonGroup();

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
        utilities.customFontStyle(submitCheckInBtn, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(submitCheckOutBtn, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(checkInBtn, "Poppins-Regular.ttf", 12f);
        utilities.customFontStyle(checkOutBtn, "Poppins-Regular.ttf", 12f);
        utilities.customFontStyle(checkInLabel, "Poppins-Regular.ttf", 18f);
        utilities.customFontStyle(checkOutLabel, "Poppins-Regular.ttf", 18f);

        //Placeholder
        utilities.textPlaceholders(txtCheckinInstrucName, "Instructor Name");
        utilities.textPlaceholders(txtCheckOutInstrucName, "Instructor Name");
        utilities.textPlaceholders(txtCheckinClass, "Enter Class/Activity");
        utilities.textPlaceholders(txtCheckOutClass, "Enter Class/Activity");

        utilities.customFontStyle(txtCheckinInstrucName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtCheckOutInstrucName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtCheckinClass, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtCheckinInstrucName, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(txtCheckOutClass, "Poppins-Regular.ttf", 16f);
        
        //Default Selected Radio Button
        checkInBtn.setSelected(true);
    }

    private void checkForActiveSessions() {
        try {
            String sql = "SELECT instructor_name, class_activity FROM checkin_table WHERE is_active = TRUE LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String instructor = rs.getString("instructor_name");
                String activity = rs.getString("class_activity");

                // Set the fields and disable them
                txtCheckOutInstrucName.setText(instructor);
                txtCheckOutClass.setText(activity);
                txtCheckOutInstrucName.setEnabled(false);
                txtCheckOutClass.setEnabled(false);
            } else {
                // No active sessions found, clear and enable the fields
                txtCheckOutInstrucName.setText("");
                txtCheckOutClass.setText("");
                txtCheckOutInstrucName.setEnabled(true);
                txtCheckOutClass.setEnabled(true);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(GymUse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeButtonGroup() {
        ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
        buttonGroup.add(checkInBtn);
        buttonGroup.add(checkOutBtn);
    }

    private void checkinClearFields() {
        txtCheckinInstrucName.setText("");
        txtCheckinClass.setText("");
    }

    private void checkOutClearFields() {
        txtCheckOutInstrucName.setText("");
        txtCheckOutClass.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerTxt = new javax.swing.JLabel();
        checkInOutContainer = new javax.swing.JPanel();
        checkInPanel = new javax.swing.JPanel();
        checkInLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtCheckinInstrucName = new javax.swing.JTextField();
        submitCheckInBtn = new MainProgram.Button();
        txtCheckinClass = new javax.swing.JTextField();
        checkOutPanel = new javax.swing.JPanel();
        checkOutLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        submitCheckOutBtn = new MainProgram.Button();
        txtCheckOutInstrucName = new javax.swing.JTextField();
        txtCheckOutClass = new javax.swing.JTextField();
        checkInBtn = new javax.swing.JRadioButton();
        checkOutBtn = new javax.swing.JRadioButton();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(400, 390));

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setPreferredSize(new java.awt.Dimension(400, 390));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerPanel.setBackground(new java.awt.Color(8, 194, 255));

        headerTxt.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        headerTxt.setForeground(new java.awt.Color(255, 255, 255));
        headerTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update.png"))); // NOI18N
        headerTxt.setText("Time-In/Out Form");
        headerTxt.setPreferredSize(new java.awt.Dimension(145, 30));

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, -1));

        checkInOutContainer.setBackground(new java.awt.Color(255, 255, 255));
        checkInOutContainer.setPreferredSize(new java.awt.Dimension(400, 330));
        checkInOutContainer.setLayout(new java.awt.CardLayout());

        checkInPanel.setBackground(new java.awt.Color(255, 255, 255));

        checkInLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        checkInLabel.setForeground(new java.awt.Color(0, 0, 0));
        checkInLabel.setText("Time-In Details");

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        submitCheckInBtn.setBackground(new java.awt.Color(13, 110, 244));
        submitCheckInBtn.setForeground(new java.awt.Color(255, 255, 255));
        submitCheckInBtn.setText("Submit");
        submitCheckInBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                submitCheckInBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                submitCheckInBtnMouseReleased(evt);
            }
        });
        submitCheckInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitCheckInBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout checkInPanelLayout = new javax.swing.GroupLayout(checkInPanel);
        checkInPanel.setLayout(checkInPanelLayout);
        checkInPanelLayout.setHorizontalGroup(
            checkInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkInPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(checkInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(checkInPanelLayout.createSequentialGroup()
                        .addComponent(checkInLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(checkInPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(checkInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCheckinInstrucName, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitCheckInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCheckinClass, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        checkInPanelLayout.setVerticalGroup(
            checkInPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkInPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkInLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtCheckinInstrucName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtCheckinClass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(submitCheckInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        checkInOutContainer.add(checkInPanel, "card2");

        checkOutPanel.setBackground(new java.awt.Color(255, 255, 255));
        checkOutPanel.setPreferredSize(new java.awt.Dimension(400, 330));

        checkOutLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        checkOutLabel.setForeground(new java.awt.Color(0, 0, 0));
        checkOutLabel.setText("Time-Out Details");

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        submitCheckOutBtn.setBackground(new java.awt.Color(13, 110, 244));
        submitCheckOutBtn.setForeground(new java.awt.Color(255, 255, 255));
        submitCheckOutBtn.setText("Submit");
        submitCheckOutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                submitCheckOutBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                submitCheckOutBtnMouseReleased(evt);
            }
        });
        submitCheckOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitCheckOutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout checkOutPanelLayout = new javax.swing.GroupLayout(checkOutPanel);
        checkOutPanel.setLayout(checkOutPanelLayout);
        checkOutPanelLayout.setHorizontalGroup(
            checkOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkOutPanelLayout.createSequentialGroup()
                .addGroup(checkOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(checkOutPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(checkOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(checkOutPanelLayout.createSequentialGroup()
                                .addComponent(checkOutLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator2)))
                    .addGroup(checkOutPanelLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(checkOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(checkOutPanelLayout.createSequentialGroup()
                                .addGap(181, 181, 181)
                                .addComponent(submitCheckOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCheckOutInstrucName)
                            .addComponent(txtCheckOutClass))
                        .addGap(0, 27, Short.MAX_VALUE)))
                .addContainerGap())
        );
        checkOutPanelLayout.setVerticalGroup(
            checkOutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkOutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkOutLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(txtCheckOutInstrucName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtCheckOutClass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(submitCheckOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        checkInOutContainer.add(checkOutPanel, "card3");

        mainPanel.add(checkInOutContainer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 400, 330));

        checkInBtn.setForeground(new java.awt.Color(0, 0, 0));
        checkInBtn.setText("Time-In");
        checkInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkInBtnActionPerformed(evt);
            }
        });
        mainPanel.add(checkInBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, -1, -1));

        checkOutBtn.setForeground(new java.awt.Color(0, 0, 0));
        checkOutBtn.setText("Time-Out");
        checkOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkOutBtnActionPerformed(evt);
            }
        });
        mainPanel.add(checkOutBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void checkInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkInBtnActionPerformed
        AppUtilities utilities = new AppUtilities();
        utilities.setTabPanel(checkInPanel, true);
        utilities.setTabPanel(checkOutPanel, false);
    }//GEN-LAST:event_checkInBtnActionPerformed

    private void checkOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkOutBtnActionPerformed
        AppUtilities utilities = new AppUtilities();
        utilities.setTabPanel(checkOutPanel, true);
        utilities.setTabPanel(checkInPanel, false);
        // Check for active sessions when switching to checkout panel
        checkForActiveSessions();
    }//GEN-LAST:event_checkOutBtnActionPerformed

    private void submitCheckOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitCheckOutBtnActionPerformed
        String InstructorName = txtCheckOutInstrucName.getText();
        String Class = txtCheckOutClass.getText();

        if (InstructorName.isEmpty() && Class.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (InstructorName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Instructor Name field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (Class.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Class/Activity field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int staffID = Session.getStaffID();

            String checkInstructorSql = "SELECT id FROM checkin_table WHERE instructor_name = ? AND class_activity = ? AND is_active = TRUE";
            PreparedStatement pstmtCheck = conn.prepareStatement(checkInstructorSql);
            pstmtCheck.setString(1, InstructorName);
            pstmtCheck.setString(2, Class);
            ResultSet rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                int checkinId = rs.getInt("id");

                String sql = "INSERT INTO checkout_table (checkin_id, instructor_name, class_activity, checkout_time, staff_id) "
                        + "VALUES (?, ?, ?, NOW(), ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, checkinId);
                pstmt.setString(2, InstructorName);
                pstmt.setString(3, Class);
                pstmt.setInt(4, staffID);

                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
                    String updateCheckinSql = "UPDATE checkin_table SET is_active = FALSE WHERE id = ?";
                    PreparedStatement pstmtUpdate = conn.prepareStatement(updateCheckinSql);
                    pstmtUpdate.setInt(1, checkinId);
                    int rowsUpdated = pstmtUpdate.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, InstructorName + " successfully checked out!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        checkOutClearFields();
                        checkInOutTable.displayCheckInOut();
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update check-in record.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    pstmtUpdate.close();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to record checkout transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                pstmt.close();
            } else {
                JOptionPane.showMessageDialog(this, "No active check-in record found for this instructor and class activity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
            pstmtCheck.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_submitCheckOutBtnActionPerformed

    private void submitCheckInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitCheckInBtnActionPerformed
        String InstructorName = txtCheckinInstrucName.getText();
        String Class = txtCheckinClass.getText();

        if (InstructorName.isEmpty() && Class.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (InstructorName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Instructor Name field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (Class.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Class/Activity field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int staffID = Session.getStaffID();

            String sql = "INSERT INTO checkin_table (instructor_name, class_activity, checkin_time, staff_id) "
                    + "VALUES (?, ?, NOW(), ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, InstructorName);
            pstmt.setString(2, Class);
            pstmt.setInt(3, staffID);  // Insert staff_id

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, InstructorName + " successfully check-in!", "Success", JOptionPane.INFORMATION_MESSAGE);
                checkinClearFields();
                checkInOutTable.displayCheckInOut();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to record transaction.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            pstmt.close();
        } catch (SQLException e) {
            if (e.getMessage().contains("There is already an active check-in.")) {
                JOptionPane.showMessageDialog(this, "There is already an active check-in. Please check out first.", "Message", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "An error occurred. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_submitCheckInBtnActionPerformed

    private void submitCheckInBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitCheckInBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(submitCheckInBtn, "#0B5CC9");
    }//GEN-LAST:event_submitCheckInBtnMousePressed

    private void submitCheckInBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitCheckInBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(submitCheckInBtn, "#0D6EF4");
    }//GEN-LAST:event_submitCheckInBtnMouseReleased

    private void submitCheckOutBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitCheckOutBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(submitCheckOutBtn, "#0B5CC9");
    }//GEN-LAST:event_submitCheckOutBtnMousePressed

    private void submitCheckOutBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitCheckOutBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(submitCheckOutBtn, "#0D6EF4");
    }//GEN-LAST:event_submitCheckOutBtnMouseReleased

    public static void main(String args[]) {
        FlatMacLightLaf.setup();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StaffDashboard checkInOutTable = new StaffDashboard(); //Pwede tangtangon ang nullif naay wrong

                    GymUse dialog = new GymUse(new javax.swing.JFrame(), true, checkInOutTable);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            dialog.dispose();
                        }
                    });
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(GymUse.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton checkInBtn;
    private javax.swing.JLabel checkInLabel;
    private javax.swing.JPanel checkInOutContainer;
    private javax.swing.JPanel checkInPanel;
    private javax.swing.JRadioButton checkOutBtn;
    private javax.swing.JLabel checkOutLabel;
    private javax.swing.JPanel checkOutPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTxt;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private MainProgram.Button submitCheckInBtn;
    private MainProgram.Button submitCheckOutBtn;
    private javax.swing.JTextField txtCheckOutClass;
    private javax.swing.JTextField txtCheckOutInstrucName;
    private javax.swing.JTextField txtCheckinClass;
    private javax.swing.JTextField txtCheckinInstrucName;
    // End of variables declaration//GEN-END:variables
}
