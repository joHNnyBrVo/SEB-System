package Home;

import PopupModal.AddItemPopup;
import MainProgram.AdminDashboard;
import MainProgram.AppUtilities;
import MainProgram.Session;
import MainProgram.StaffDashboard;
import Database.dbConnection;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Homepage extends javax.swing.JFrame {

    private Connection conn;

    public Homepage() throws IOException {
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
        this.setTitle("Login Portal");
        this.setLocationRelativeTo(null);

        AppUtilities utilities = new AppUtilities();

        utilities.customFontStyle(txt1, "Poppins-Bold.ttf", 18f);
        utilities.customFontStyle(txt2, "Poppins-Regular.ttf", 12f);
        utilities.customFontStyle(txt3, "Poppins-Regular.ttf", 12f);
        utilities.customFontStyle(txt4, "Poppins-Regular.ttf", 12f);

        utilities.customFontStyle(loginBtn, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(signinLabel, "Poppins-SemiBold.ttf", 24f);

        //Placeholder
        utilities.textPlaceholders(employee_id, "Employee ID");
        utilities.textPlaceholders(password, "Password");
        utilities.setupComboBoxPlaceholder(roleChoice, "Select Role");
        utilities.customFontStyle(employee_id, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(password, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(roleChoice, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(showPass, "Poppins-Regular.ttf", 12f);
    }

    private void clearFields() {
        employee_id.setText("");
        password.setText("");
        roleChoice.setSelectedIndex(0);
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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt1 = new javax.swing.JLabel();
        txt2 = new javax.swing.JLabel();
        txt3 = new javax.swing.JLabel();
        txt4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        signinLabel = new javax.swing.JLabel();
        employee_id = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        roleChoice = new javax.swing.JComboBox<>();
        showPass = new javax.swing.JCheckBox();
        loginBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));

        jLabel2.setBackground(new java.awt.Color(8, 194, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/sports_(1)-rGLBbIQPV-transformed.png"))); // NOI18N

        txt1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txt1.setForeground(new java.awt.Color(51, 51, 51));
        txt1.setText("Welcome Back!");

        txt2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt2.setForeground(new java.awt.Color(51, 51, 51));
        txt2.setText("\"Streamline your borrowing experience with ease!\"");

        txt3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt3.setForeground(new java.awt.Color(51, 51, 51));
        txt3.setText("Log in to get started and enjoy a seamless");

        txt4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt4.setForeground(new java.awt.Color(51, 51, 51));
        txt4.setText("borrowing experience.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(txt3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(txt4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txt2)
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txt1)
                        .addGap(105, 105, 105))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(89, 89, 89))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt4)
                .addGap(41, 41, 41)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt2)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user (1).png"))); // NOI18N

        signinLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        signinLabel.setForeground(new java.awt.Color(51, 51, 51));
        signinLabel.setText("SIGN-IN");

        roleChoice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin\t", "Staff" }));

        showPass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        showPass.setForeground(new java.awt.Color(51, 51, 51));
        showPass.setText("Show Password");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });

        loginBtn.setBackground(new java.awt.Color(13, 110, 244));
        loginBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        loginBtn.setForeground(new java.awt.Color(255, 255, 255));
        loginBtn.setText("Login");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(roleChoice, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showPass)
                            .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(155, 155, 155))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(signinLabel)
                        .addGap(140, 140, 140))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(signinLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roleChoice, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showPass)
                .addGap(18, 18, 18)
                .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        if (showPass.isSelected()) {
            password.setEchoChar((char) 0);
        } else {
            password.setEchoChar('•');
        }
        password.repaint();
    }//GEN-LAST:event_showPassActionPerformed

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        String EmployeeID = employee_id.getText();
        String Password = password.getText();
        String Role = (String) roleChoice.getSelectedItem();  // Get selected role

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
        } else if (Role == null || Role.equals("Select Role")) {
            JOptionPane.showMessageDialog(null, "Please select a role (Admin or Staff)");
        } else {
            try {
                if (Role.contains("Admin")) {
                    // Admin Login (prioritized)
                    String sql = "SELECT * FROM admin WHERE employee_id = ? AND password = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, EmployeeID);
                    pst.setString(2, Password);

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        int adminID = rs.getInt("admin_id");
                        String updateSql = "UPDATE admin SET is_online = TRUE WHERE employee_id = ?";
                        PreparedStatement updatePst = conn.prepareStatement(updateSql);
                        updatePst.setString(1, EmployeeID);
                        updatePst.executeUpdate();

                        Session.setAdminID(adminID);

                        JOptionPane.showMessageDialog(this, "Login Successful!");
                        clearFields();

                        AdminDashboard admin = new AdminDashboard();
                        admin.setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Wrong Employee ID or Password! Please try again!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    rs.close();
                    pst.close();

                } else if (Role.contains("Staff")) {
                    // Staff Login (second priority)
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
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(AdminLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_loginBtnActionPerformed

    public static void main(String args[]) {
        //Set up flatlaf theme
        FlatMacLightLaf.setup();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Homepage().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Homepage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField employee_id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField password;
    private javax.swing.JComboBox<String> roleChoice;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JLabel signinLabel;
    private javax.swing.JLabel txt1;
    private javax.swing.JLabel txt2;
    private javax.swing.JLabel txt3;
    private javax.swing.JLabel txt4;
    // End of variables declaration//GEN-END:variables
}
