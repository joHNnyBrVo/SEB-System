
package MainProgram;

import Database.dbConnection;
import ReportGenerator.ExcelGenerator;
import ReportGenerator.PDFGenerator;
import Home.StaffLogin;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import raven.cell.TableActionCellEditor1;
import raven.cell.TableActionCellEditor2;
import raven.cell.TableActionCellRender1;
import raven.cell.TableActionCellRender2;
import raven.cell.TableActionEvent;

//
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.BorderLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;


public class table extends javax.swing.JFrame {

    private boolean isDialogOpen = false;
    private Connection conn;
//    private int adminID;
    int staffID = Session.getStaffID();
    private TableActionEvent event;

    public table() {
        initComponents();
//        this.adminID = adminID;

        //DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();

            // Fetch username from the admin table
            String query = "SELECT employee_id FROM staff WHERE staff_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, staffID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String employeeID = rs.getString("employee_id");
                // Set the fetched username
                usernameTxt.setText(employeeID);
            } else {
                usernameTxt.setText("Unknown Admin");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(table.class.getName()).log(Level.SEVERE, null, ex);
            usernameTxt.setText("Error Loading Username");
        }
        displayBorrowRecords();

        //Refresh Table
        startPeriodicUpdate();

        //Search
        addSearchListener(searchBorrowRecordsTxt, this::searchBorrowRecords);
    }
    
   


    //Display Inventory
    public void displayBorrowRecords() {
        String query = "SELECT id, studentID, studentName, course, yearlvl, equipmentID, equipmentName, quantity, borrowDate, returnDate, status FROM borrow_transaction";

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String studentID = rs.getString("studentID");
                String studentName = rs.getString("studentName");
                String course = rs.getString("course");
                int yearlevel = rs.getInt("yearlvl");
                String equipmentID = rs.getString("equipmentID");
                String equipmentName = rs.getString("equipmentName");
                int quantity = rs.getInt("quantity");
                String borrowDate = rs.getString("borrowDate");
                String returnDate = rs.getString("returnDate");
                String status = rs.getString("status");

                model.addRow(new Object[]{id, studentID, studentName, course, yearlevel, equipmentID, equipmentName, quantity, borrowDate, returnDate, status});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Search Borrow Records
    public void searchBorrowRecords() {
        String searchBorrow = searchBorrowRecordsTxt.getText().trim();
        String query = "SELECT studentID, studentName, course, yearlvl, equipmentID, equipmentName, quantity, borrowDate, returnDate, status "
                + "FROM borrow_transaction "
                + "WHERE studentID LIKE ? OR studentName LIKE ? OR equipmentID LIKE ? OR equipmentName LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchBorrow + "%");
            pstmt.setString(2, "%" + searchBorrow + "%");
            pstmt.setString(3, "%" + searchBorrow + "%");
            pstmt.setString(4, "%" + searchBorrow + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                model.setRowCount(0);

                boolean hasResults = false;
                int index = 1;

                while (rs.next()) {
                    hasResults = true;
                    String studentID = rs.getString("studentID");
                    String studentName = rs.getString("studentName");
                    String course = rs.getString("course");
                    int yearlevel = rs.getInt("yearlvl");
                    String equipmentID = rs.getString("equipmentID");
                    String equipmentName = rs.getString("equipmentName");
                    int quantity = rs.getInt("quantity");
                    String borrowDate = rs.getString("borrowDate");
                    String returnDate = rs.getString("returnDate");
                    String status = rs.getString("status");

                    model.addRow(new Object[]{index, studentID, studentName, course, yearlevel, equipmentID,
                        equipmentName, quantity, borrowDate, returnDate, status, "", ""});
                    index++;
                }

                if (!hasResults) {
                    Object[] noResultRow = new Object[model.getColumnCount()];
                    noResultRow[0] = "No results found";
                    for (int i = 1; i < model.getColumnCount(); i++) {
                        noResultRow[i] = "";
                    }
                    model.addRow(noResultRow);

                    // Remove renderers and editors for action columns
                    borrowTable.getColumnModel().getColumn(11).setCellRenderer(null);
                    borrowTable.getColumnModel().getColumn(11).setCellEditor(null);
                    borrowTable.getColumnModel().getColumn(12).setCellRenderer(null);
                    borrowTable.getColumnModel().getColumn(12).setCellEditor(null);
                    borrowTable.setEnabled(false);
                } else {
                    // Restore renderers and editors for action columns
                    borrowTable.getColumnModel().getColumn(11).setCellRenderer(new TableActionCellRender1());
                    borrowTable.getColumnModel().getColumn(11).setCellEditor(new TableActionCellEditor1(event));
                    borrowTable.getColumnModel().getColumn(12).setCellRenderer(new TableActionCellRender2());
                    borrowTable.getColumnModel().getColumn(12).setCellEditor(new TableActionCellEditor2(event));
                    borrowTable.setEnabled(true);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSearchListener(javax.swing.JTextField textField, Runnable searchAction) {
        currentSearchAction = searchAction; // Store the search action
        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                currentSearchText = textField.getText();
                searchAction.run();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                currentSearchText = textField.getText();
                searchAction.run();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                currentSearchText = textField.getText();
                searchAction.run();
            }
        });
    }

    private String currentSearchText = "";
    private Runnable currentSearchAction = null;

// Modify the startPeriodicUpdate method
    private void startPeriodicUpdate() {
        Thread updateThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5000);
                    SwingUtilities.invokeLater(() -> {
                        if (currentSearchText.isEmpty()) {
                            displayBorrowRecords();
                        } else if (currentSearchAction != null) {
                            // Reapply search to get updated results
                            currentSearchAction.run();
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        usernameTxt = new javax.swing.JLabel();
        logoutBtn = new javax.swing.JButton();
        searchBorrowRecordsTxt = new javax.swing.JTextField();
        borrowScrollPane = new javax.swing.JScrollPane();
        borrowTable = new javax.swing.JTable();
        excelBtn = new javax.swing.JButton();
        csvBtn1 = new javax.swing.JButton();
        pdfBtn = new javax.swing.JButton();
        filter = new javax.swing.JComboBox<>();
        Btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1035, 710));

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user (1).png"))); // NOI18N

        usernameTxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        usernameTxt.setForeground(new java.awt.Color(255, 255, 255));
        usernameTxt.setText("EMPLOYEE ID");

        logoutBtn.setText("Logout");
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(usernameTxt)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(usernameTxt)))
                .addGap(249, 249, 249)
                .addComponent(logoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(240, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 620));
        mainPanel.add(searchBorrowRecordsTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 10, 270, -1));

        borrowScrollPane.setPreferredSize(new java.awt.Dimension(1052, 402));

        borrowTable.setBackground(new java.awt.Color(255, 255, 255));
        borrowTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        borrowTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Student ID", "Name of Student", "Course", "Year Level", "Equipment ID", "Name of Equipment", "Quantity", "Borrowed Date", "Expected Return Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        borrowTable.setMaximumSize(new java.awt.Dimension(1052, 455));
        borrowTable.setPreferredSize(new java.awt.Dimension(1052, 455));
        borrowTable.setRowHeight(40);
        borrowScrollPane.setViewportView(borrowTable);

        mainPanel.add(borrowScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 940, 320));

        excelBtn.setText("Generate Excel");
        excelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelBtnActionPerformed(evt);
            }
        });
        mainPanel.add(excelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, -1, -1));

        csvBtn1.setText("Generate CSV");
        csvBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvBtn1ActionPerformed(evt);
            }
        });
        mainPanel.add(csvBtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, -1, -1));

        pdfBtn.setText("Generate PDF");
        pdfBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfBtnActionPerformed(evt);
            }
        });
        mainPanel.add(pdfBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, -1, -1));

        filter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Borrowed", "Returned", "Overdue" }));
        mainPanel.add(filter, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 10, 100, -1));

        Btn.setText("Popup");
        Btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnActionPerformed(evt);
            }
        });
        mainPanel.add(Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, 90, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
//        try {
//            String updateSql = "UPDATE staff SET is_active = false WHERE employee_id = ?"; // Changed to employee_id
//            PreparedStatement updatePst = conn.prepareStatement(updateSql);
//            updatePst.setString(1, employeeID); // Use employeeID
//            updatePst.executeUpdate();
//
//            JOptionPane.showMessageDialog(this, "You have been logged out!");
//            this.dispose();  // Close the current window
//            try { 
//                StaffLogin login = new StaffLogin();
//                login.setVisible(true);
//            } catch (IOException ex) {
//                Logger.getLogger(table.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }//GEN-LAST:event_logoutBtnActionPerformed

    private void excelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excelBtnActionPerformed
//        ExcelGenerator.getInstance().generateExcel(borrowTable, "Borrow Records Report");
    }//GEN-LAST:event_excelBtnActionPerformed

    private void csvBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvBtn1ActionPerformed
//        CSVGenerator.getInstance().generateCSV(borrowTable, "Borrow Records Report");
    }//GEN-LAST:event_csvBtn1ActionPerformed

    private void pdfBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfBtnActionPerformed
//        PDFGenerator.getInstance().generatePDF(borrowTable, "Borrow Records Report");
    }//GEN-LAST:event_pdfBtnActionPerformed

    private void BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnActionPerformed
//        // Dim the parent window
//        if (isDialogOpen) {
//            return;
//        }
//
//        try {
//            isDialogOpen = true;
//
//            // Create glass pane
//            JPanel glassPane = new JPanel() {
//                @Override
//                protected void paintComponent(Graphics g) {
//                    super.paintComponent(g);
//                    g.setColor(new Color(0, 0, 0, 128));
//                    g.fillRect(0, 0, getWidth(), getHeight());
//                }
//            };
//            glassPane.setOpaque(false);
//            this.setGlassPane(glassPane);
//            glassPane.setVisible(true);
//
//            AddItemPopup popup = new AddItemPopup(this, true, null);
//            popup.addWindowListener(new java.awt.event.WindowAdapter() {
//                @Override
//                public void windowClosed(java.awt.event.WindowEvent e) {
//                    glassPane.setVisible(false);
//                    isDialogOpen = false;
//                }
//            });
//            popup.setVisible(true);
//        } catch (IOException ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            isDialogOpen = false;  // Reset flag in case of error
//        }
    }//GEN-LAST:event_BtnActionPerformed

    public static void main(String args[]) {
    FlatMacLightLaf.setup();
//        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
//        } catch (Exception ex) {
//            System.err.println("Failed to initialize FlatLaf");
//        }
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
        java.util.logging.Logger.getLogger(table.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(table.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(table.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(table.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new table().setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn;
    private javax.swing.JScrollPane borrowScrollPane;
    private javax.swing.JTable borrowTable;
    private javax.swing.JButton csvBtn1;
    private javax.swing.JButton excelBtn;
    private javax.swing.JComboBox<String> filter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton pdfBtn;
    private javax.swing.JTextField searchBorrowRecordsTxt;
    private javax.swing.JLabel usernameTxt;
    // End of variables declaration//GEN-END:variables
}
