package PopupModal;

import MainProgram.AdminDashboard;
import MainProgram.AppUtilities;
import Database.dbConnection;
import PopupModal.AddItemPopup;
import java.awt.Dialog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class AddNewStaffPopup extends javax.swing.JDialog {
    private Connection conn;
    private AdminDashboard staffAccountTable;

    public AddNewStaffPopup(java.awt.Frame parent, boolean modal, AdminDashboard staffAccountTable) throws IOException {
        super(parent, modal);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        initComponents();
        
        setLocationRelativeTo(parent);
        
        this.staffAccountTable = staffAccountTable;

        //DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        AppUtilities utilities = new AppUtilities();

        //Fontstyle
        utilities.customFontStyle(headerTxt, "Poppins-Bold.ttf", 20f);
        utilities.customFontStyle(createAccountBtn, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(systemTxt, "Poppins-Bold.ttf", 18f);
        utilities.customFontStyle(textFieldTxt1, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(textFieldTxt2, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(textFieldTxt3, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(textFieldTxt4, "Poppins-Regular.ttf", 16f);

        //Placeholder
        utilities.textPlaceholders(firstNameTxt, "First Name");
        utilities.textPlaceholders(lastNameTxt, "Last Name");
        utilities.textPlaceholders(emailTxt, "Email Address");

        utilities.customFontStyle(firstNameTxt, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(lastNameTxt, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(emailTxt, "Poppins-Regular.ttf", 16f);

    }

    private void clearFields() {
        firstNameTxt.setText("");
        lastNameTxt.setText("");
        emailTxt.setText("");
    }

    private void sendEmail(String recipientEmail, String firstName, String lastName, String employeeId, String password) {
        String senderEmail = "sportsequipmentborrowing@gmail.com";
        String senderPassword = "bzctxpcoqgqnjwmk";
        String subject = "Welcome to the Sports Equipment Borrowing System!";
    String body = "Dear " + firstName + " " + lastName + ",\n\n"
                + "Your account has been created. Below are your login details:\n\n"
                + "Employee ID: " + employeeId + "\n"
                + "Temporary Password: " + password + "\n\n"
                + "Please log in at [login_page_url] and change your password immediately.\n\n"
                + "Best regards,\n"
                + "Sports Equipment Borrowing System Team";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, "Failed to send email: " + e.getMessage(), "Email Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerTxt = new javax.swing.JLabel();
        firstNameTxt = new javax.swing.JTextField();
        emailTxt = new javax.swing.JTextField();
        lastNameTxt = new javax.swing.JTextField();
        textFieldTxt1 = new javax.swing.JLabel();
        textFieldTxt2 = new javax.swing.JLabel();
        textFieldTxt3 = new javax.swing.JLabel();
        textFieldTxt4 = new javax.swing.JLabel();
        createAccountBtn = new MainProgram.Button();
        systemTxt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        headerPanel.setBackground(new java.awt.Color(8, 194, 255));

        headerTxt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        headerTxt.setForeground(new java.awt.Color(255, 255, 255));
        headerTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/account.png"))); // NOI18N
        headerTxt.setText("Staff Account Setup");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxt)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addContainerGap())
        );

        textFieldTxt1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        textFieldTxt1.setForeground(new java.awt.Color(0, 0, 0));
        textFieldTxt1.setText("Provide the staff member's details to create an account.");

        textFieldTxt2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        textFieldTxt2.setForeground(new java.awt.Color(0, 0, 0));
        textFieldTxt2.setText("Once the account is created, the staff member ");

        textFieldTxt3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        textFieldTxt3.setForeground(new java.awt.Color(0, 0, 0));
        textFieldTxt3.setText("will be able to manage and support the sports equipment");

        textFieldTxt4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        textFieldTxt4.setForeground(new java.awt.Color(0, 0, 0));
        textFieldTxt4.setText("borrowing system.");

        createAccountBtn.setBackground(new java.awt.Color(13, 110, 244));
        createAccountBtn.setForeground(new java.awt.Color(255, 255, 255));
        createAccountBtn.setText("Create Account");
        createAccountBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                createAccountBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                createAccountBtnMouseReleased(evt);
            }
        });
        createAccountBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccountBtnActionPerformed(evt);
            }
        });

        systemTxt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        systemTxt.setForeground(new java.awt.Color(0, 0, 0));
        systemTxt.setText("SPORTS EQUIPMENT BORROWING SYSTEM");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldTxt4)
                    .addComponent(textFieldTxt2)
                    .addComponent(systemTxt)
                    .addComponent(textFieldTxt3))
                .addGap(36, 36, 36)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textFieldTxt1)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(createAccountBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                                .addComponent(firstNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lastNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(emailTxt))))
                .addGap(0, 88, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldTxt1)
                    .addComponent(systemTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addComponent(textFieldTxt2)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(textFieldTxt3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(firstNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lastNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(0, 28, Short.MAX_VALUE)
                        .addComponent(emailTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(createAccountBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(textFieldTxt4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

    private void createAccountBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccountBtnActionPerformed
        String Firstname = firstNameTxt.getText();
        String Lastname = lastNameTxt.getText();
        String Email = emailTxt.getText();

        if (Firstname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Firstname field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Lastname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Lastname field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Email Address field", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO staff (first_name, last_name, email) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, Firstname);
            pstmt.setString(2, Lastname);
            pstmt.setString(3, Email);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                String fetchSql = "SELECT employee_id, password FROM staff WHERE email = ?";
                PreparedStatement fetchStmt = conn.prepareStatement(fetchSql);
                fetchStmt.setString(1, Email);

                ResultSet rs = fetchStmt.executeQuery();
                if (rs.next()) {
                    String employeeId = rs.getString("employee_id");
                    String password = rs.getString("password");

                    sendEmail(Email, Firstname, Lastname, employeeId, password);

                    JOptionPane.showMessageDialog(this, "Account created successfully! Login details sent via email.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    staffAccountTable.displayStaffAccount();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to fetch login details.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                fetchStmt.close();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_createAccountBtnActionPerformed

    private void createAccountBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createAccountBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(createAccountBtn, "#0B5CC9");
    }//GEN-LAST:event_createAccountBtnMousePressed

    private void createAccountBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createAccountBtnMouseReleased
       AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(createAccountBtn, "#0D6EF4");
    }//GEN-LAST:event_createAccountBtnMouseReleased

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddNewStaffPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewStaffPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewStaffPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewStaffPopup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminDashboard staffAccountTable = new AdminDashboard(); //wala ni cyay null dapat
                            
                    AddNewStaffPopup dialog = new AddNewStaffPopup(new javax.swing.JFrame(), true, staffAccountTable);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            dialog.dispose();
                        }
                    });
                    dialog.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(AddNewStaffPopup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private MainProgram.Button createAccountBtn;
    private javax.swing.JTextField emailTxt;
    private javax.swing.JTextField firstNameTxt;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel headerTxt;
    private javax.swing.JTextField lastNameTxt;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel systemTxt;
    private javax.swing.JLabel textFieldTxt1;
    private javax.swing.JLabel textFieldTxt2;
    private javax.swing.JLabel textFieldTxt3;
    private javax.swing.JLabel textFieldTxt4;
    // End of variables declaration//GEN-END:variables
}
