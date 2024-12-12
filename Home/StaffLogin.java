package Home;

import PopupModal.AddItemPopup;
import MainProgram.AppUtilities;
import MainProgram.Session;
import MainProgram.StaffDashboard;
import Database.dbConnection;
import MainProgram.table;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Image;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;

public class StaffLogin extends javax.swing.JFrame {

    private Connection conn;

    public StaffLogin() throws IOException {
        initComponents();
        setIconLogo();

        //DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Set Title
        this.setTitle("Staff Portal");
        this.setLocationRelativeTo(null);

        //Instance AppUtilities (Singleton Code)
        AppUtilities utilities = new AppUtilities();

        utilities.customFontStyle(loginBtn, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(signinLabel, "Poppins-SemiBold.ttf", 24f);
        utilities.customFontStyle(systemTitle, "Poppins-Bold.ttf", 28f);

        //Placeholder
        utilities.textPlaceholders(employee_id, "Employee ID");
        utilities.textPlaceholders(password, "Password");
        utilities.customFontStyle(employee_id, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(password, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(showPass, "Poppins-Regular.ttf", 14f);
    }

    private void clearFields() {
        employee_id.setText("");
        password.setText("");
        
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    //Set icon logo to java frame
    public final void setIconLogo() {
        Image iconLogo = new ImageIcon(getClass().getResource("/icons/SEB_System.png")).getImage();
        this.setIconImage(iconLogo);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        loginPanel = new MainProgram.RoundedPanel();
        jLabel1 = new javax.swing.JLabel();
        signinLabel = new javax.swing.JLabel();
        employee_id = new javax.swing.JTextField();
        loginBtn = new javax.swing.JButton();
        showPass = new javax.swing.JCheckBox();
        roundedPanel1 = new MainProgram.RoundedPanel();
        systemTitle = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        loginPanel.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user (1).png"))); // NOI18N

        signinLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        signinLabel.setForeground(new java.awt.Color(51, 51, 51));
        signinLabel.setText("STAFF SIGN-IN");

        loginBtn.setBackground(new java.awt.Color(13, 110, 244));
        loginBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        loginBtn.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn.setText("Login");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        showPass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        showPass.setForeground(new java.awt.Color(51, 51, 51));
        showPass.setText("Show Password");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });

        roundedPanel1.setBackground(new java.awt.Color(8, 194, 255));
        roundedPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        systemTitle.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        systemTitle.setForeground(new java.awt.Color(255, 255, 255));
        systemTitle.setText("Sports Equipment Borrowing System");
        roundedPanel1.add(systemTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, -1, 50));

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(showPass)
                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(155, 155, 155))
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(312, 312, 312)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                .addContainerGap(264, Short.MAX_VALUE)
                .addComponent(signinLabel)
                .addGap(252, 252, 252))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(signinLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showPass)
                .addGap(18, 18, 18)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1026, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainPanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
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

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        String EmployeeID = employee_id.getText();
        String Password = password.getText();

        if (EmployeeID.equals("") && Password.equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill all the fields");
        } else if (EmployeeID.equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill out employee ID");
        } else if (Password.equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill out password");
        } else if (EmployeeID.length() < 8) {
            JOptionPane.showMessageDialog(null, "Employee ID must be at least 8 characters long");
        } else if (Password.length() < 8) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long");
        } else {
            try {
                String sql = "SELECT password, is_active, is_online, staff_id FROM staff WHERE employee_id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, EmployeeID);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    boolean isActive = rs.getBoolean("is_active");

                    if (!isActive) {
                        JOptionPane.showMessageDialog(null, "Your account has been deactivated. Please contact the admin.", "Account Deactivated", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int staffID = rs.getInt("staff_id");
                        if (dbPassword.equals(Password) || dbPassword.equals(hashPassword(Password))) {
                            String updateSql = "UPDATE staff SET is_online = TRUE WHERE employee_id = ?";
                            PreparedStatement updatePst = conn.prepareStatement(updateSql);
                            updatePst.setString(1, EmployeeID);
                            updatePst.executeUpdate();
                            updatePst.close();

                            Session.setStaffID(staffID);

                            JOptionPane.showMessageDialog(this, "Login Successful!");
                            clearFields();

                            StaffDashboard staffDashboard = new StaffDashboard();
                            staffDashboard.setVisible(true);
//                            table t = new table();
//                            t.setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Wrong Employee ID or Password! Please try again!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong Employee ID or Password! Please try again!", "Error", JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                pst.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(StaffLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_loginBtnActionPerformed

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        if (showPass.isSelected()) {
            password.setEchoChar((char) 0);
        } else {
            password.setEchoChar('•');
        }
        password.repaint();
    }//GEN-LAST:event_showPassActionPerformed

    public static void main(String args[]) {
        //Set up flatlaf theme
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new StaffLogin().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(StaffLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField employee_id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginBtn;
    private MainProgram.RoundedPanel loginPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField password;
    private MainProgram.RoundedPanel roundedPanel1;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JLabel signinLabel;
    private javax.swing.JLabel systemTitle;
    // End of variables declaration//GEN-END:variables
}
