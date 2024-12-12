package MainProgram;

import Database.dbConnection;
import PopupModal.AddItemPopup;
import ReportGenerator.CSVGenerator;
import ReportGenerator.ExcelGenerator;
import ReportGenerator.PDFGenerator;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class test1 extends javax.swing.JFrame {

    private Connection conn;

    public test1() throws IOException {
        initComponents();

        //DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Add action listener for the filter
        borrowHistoryFilter.addActionListener(e -> filterBorrowHistory());
        equipmentTypeFilter.addActionListener(e -> filterInventory());

        addFocusListenerToSearch(searchHistoryBtn, borrowHistoryFilter, "All");
        addFocusListenerToSearch(searchInventory, equipmentTypeFilter, "All");

        displayBorrowTransactionHistory();
        displayInventory();

        addSearchListener(searchHistoryBtn, this::searchBorrowHistory);
        startPeriodicUpdate();

        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Instance AppUtilities
        AppUtilities utilities = new AppUtilities();

        //Borrow History Table Design
        JTableHeader borroHistoryTableHeader = borrowArchivedTable.getTableHeader();
        utilities.customFontStyle(borroHistoryTableHeader, "Poppins-Bold.ttf", 14f);
        borroHistoryTableHeader.setBackground(Color.LIGHT_GRAY);
        borroHistoryTableHeader.setForeground(Color.BLACK);

        utilities.customFontStyle(borrowArchivedTable, "Poppins-Regular.ttf", 14f);

        setupBorrowArchivedTable();
    }

    private void setupBorrowArchivedTable() {
        borrowArchivedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        borrowArchivedTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        borrowArchivedTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        borrowArchivedTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        borrowArchivedTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        borrowArchivedTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(8).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(9).setPreferredWidth(100);
        borrowArchivedTable.getColumnModel().getColumn(10).setPreferredWidth(150);

        borrowArchivedTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        borrowArchivedScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        borrowArchivedScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        borrowArchivedTable.setShowGrid(true);
        borrowArchivedTable.setGridColor(Color.LIGHT_GRAY);
        borrowArchivedTable.setIntercellSpacing(new java.awt.Dimension(1, 1));

        borrowArchivedTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        });
    }

    //Display Borrow Transaction History
    public void displayBorrowTransactionHistory() {
        String query = """
            SELECT 
                log_id, 
                student_name, 
                equipment_name, 
                quantity_borrowed, 
                btl.transaction_status,
                action_type,
                borrow_date, 
                due_date, 
                return_date,
                s.first_name AS staff_first_name,
                action_timestamp
            FROM borrow_transaction_logs btl
            JOIN borrow_transaction bt ON btl.transaction_id = bt.transaction_id
            JOIN staff s ON bt.staff_id = s.staff_id
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) borrowArchivedTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int logId = rs.getInt("log_id");
                String studentName = rs.getString("student_name");
                String equipmentName = rs.getString("equipment_name");
                int quantity = rs.getInt("quantity_borrowed");
                String status = rs.getString("transaction_status");
                if (status == null) {
                    status = "No status change";
                }
                String actionType = rs.getString("action_type");
                String borrowDate = rs.getString("borrow_date");
                String dueDate = rs.getString("due_date");
                String returnDate = rs.getString("return_date");
                if (returnDate == null) {
                    returnDate = "Not returned yet";
                }
                String staffFirstName = rs.getString("staff_first_name");
                String actionTimestamp = rs.getString("action_timestamp");

                model.addRow(new Object[]{logId, studentName, equipmentName, quantity, status, actionType, borrowDate, dueDate, returnDate, staffFirstName, actionTimestamp
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Display Inventory
    public void displayInventory() {
        String query = "SELECT id, equipmentID, equipmentName, type, quantity FROM inventory";

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String equipmentID = rs.getString("equipmentID");
                String equipmentName = rs.getString("equipmentName");
                String type = rs.getString("type");
                int quantity = rs.getInt("quantity");
                model.addRow(new Object[]{id, equipmentID, equipmentName, type, quantity});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Method To Refresh Table
    private String currentSearchText = "";
    private Runnable currentSearchAction = null;

    private void startPeriodicUpdate() {
        Thread updateThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(5000);
                    SwingUtilities.invokeLater(() -> {
                        if (currentSearchText.isEmpty()) {
                            displayInventory();
                            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
                            if (selectedStatus.equals("All")) {
                                displayBorrowTransactionHistory();
                            } else {
                                filterBorrowHistory();
                            }

                            String selectedType = (String) equipmentTypeFilter.getSelectedItem();
                            if (selectedType.equals("All")) {
                                displayInventory();
                            } else {
                                filterInventory();
                            }
                        } else if (currentSearchAction != null) {
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

    //
    private void addFocusListenerToSearch(JTextField textField, JComboBox<?> comboBox, String defaultValue) {
        textField.addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                comboBox.setSelectedItem(defaultValue);
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // Leave empty if no action needed when focus is lost
            }
        });
    }

    public void searchBorrowHistory() {
        String searchHistory = searchHistoryBtn.getText().trim();
        String query = """
            SELECT 
                log_id, 
                student_name, 
                equipment_name, 
                quantity_borrowed, 
                btl.transaction_status,
                action_type,
                borrow_date, 
                due_date, 
                return_date,
                s.first_name AS staff_first_name,
                action_timestamp
            FROM borrow_transaction_logs btl
            JOIN borrow_transaction bt ON btl.transaction_id = bt.transaction_id
            JOIN staff s ON bt.staff_id = s.staff_id
            WHERE bt.student_name LIKE ? 
            OR bt.equipment_name LIKE ? 
            OR btl.action_type LIKE ?
            OR btl.transaction_status LIKE ?
            OR s.first_name LIKE ?
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchHistory + "%");
            pstmt.setString(2, "%" + searchHistory + "%");
            pstmt.setString(3, "%" + searchHistory + "%");
            pstmt.setString(4, "%" + searchHistory + "%");
            pstmt.setString(5, "%" + searchHistory + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) borrowArchivedTable.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    int logId = rs.getInt("log_id");
                    String studentName = rs.getString("student_name");
                    String equipmentName = rs.getString("equipment_name");
                    int quantity = rs.getInt("quantity_borrowed");
                    String status = rs.getString("transaction_status");
                    if (status == null) {
                        status = "No status change";
                    }
                    String actionType = rs.getString("action_type");
                    String borrowDate = rs.getString("borrow_date");
                    String dueDate = rs.getString("due_date");
                    String returnDate = rs.getString("return_date");
                    if (returnDate == null) {
                        returnDate = "Not returned yet";
                    }
                    String staffFirstName = rs.getString("staff_first_name");
                    String actionTimestamp = rs.getString("action_timestamp");

                    model.addRow(new Object[]{logId, studentName, equipmentName, quantity, status, actionType, borrowDate, dueDate, returnDate, staffFirstName, actionTimestamp});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //
    private void addSearchListener(javax.swing.JTextField textField, Runnable searchAction) {
        currentSearchAction = searchAction;
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

    private void filterBorrowHistory() {
        String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();

        if (selectedStatus.equals("All")) {
            displayBorrowTransactionHistory();
            return;
        }

        String query = """
        SELECT 
            log_id, 
            student_name, 
            equipment_name, 
            quantity_borrowed, 
            btl.transaction_status,
            action_type,
            borrow_date, 
            due_date, 
            return_date,
            s.first_name AS staff_first_name,
            action_timestamp
        FROM borrow_transaction_logs btl
        JOIN borrow_transaction bt ON btl.transaction_id = bt.transaction_id
        JOIN staff s ON bt.staff_id = s.staff_id
        WHERE btl.transaction_status = ?
    """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedStatus);

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) borrowArchivedTable.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    int logId = rs.getInt("log_id");
                    String studentName = rs.getString("student_name");
                    String equipmentName = rs.getString("equipment_name");
                    int quantity = rs.getInt("quantity_borrowed");
                    String status = rs.getString("transaction_status");
                    String actionType = rs.getString("action_type");
                    String borrowDate = rs.getString("borrow_date");
                    String dueDate = rs.getString("due_date");
                    String returnDate = rs.getString("return_date");
                    if (returnDate == null) {
                        returnDate = "Not returned yet";
                    }
                    String staffFirstName = rs.getString("staff_first_name");
                    String actionTimestamp = rs.getString("action_timestamp");

                    model.addRow(new Object[]{
                        logId,
                        studentName,
                        equipmentName,
                        quantity,
                        status,
                        actionType,
                        borrowDate,
                        dueDate,
                        returnDate,
                        staffFirstName,
                        actionTimestamp
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterInventory() {
        String selectedType = (String) equipmentTypeFilter.getSelectedItem();

        if (selectedType.equals("All")) {
            displayInventory();
            return;
        }

        String query = "SELECT id, equipmentID, equipmentName, type, quantity FROM inventory WHERE type = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedType);

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
                model.setRowCount(0);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String equipmentID = rs.getString("equipmentID");
                    String equipmentName = rs.getString("equipmentName");
                    String type = rs.getString("type");
                    int quantity = rs.getInt("quantity");
                    model.addRow(new Object[]{id, equipmentID, equipmentName, type, quantity, null});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        searchHistoryBtn = new javax.swing.JTextField();
        borrowHistoryFilter = new javax.swing.JComboBox<>();
        csvBtn = new javax.swing.JButton();
        pdfBtn = new javax.swing.JButton();
        excelBtn = new javax.swing.JButton();
        invenScrollPane = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        equipmentTypeFilter = new javax.swing.JComboBox<>();
        searchInventory = new javax.swing.JTextField();
        borrowArchivedScrollPane = new raven.scroll.win11.ScrollPaneWin11();
        borrowArchivedTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 470));
        setSize(new java.awt.Dimension(1000, 470));

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        mainPanel.add(searchHistoryBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 50, 240, 30));

        borrowHistoryFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Borrowed", "Overdue", "Returned" }));
        mainPanel.add(borrowHistoryFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 140, 30));

        csvBtn.setText("Generate CSV");
        csvBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvBtnActionPerformed(evt);
            }
        });
        mainPanel.add(csvBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        pdfBtn.setText("Generate PDF");
        pdfBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfBtnActionPerformed(evt);
            }
        });
        mainPanel.add(pdfBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, -1, -1));

        excelBtn.setText("Generate Excel");
        excelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelBtnActionPerformed(evt);
            }
        });
        mainPanel.add(excelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, -1, -1));

        inventoryTable.setBackground(new java.awt.Color(255, 255, 255));
        inventoryTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        inventoryTable.setForeground(new java.awt.Color(0, 0, 0));
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Equipment ID", "Name of Equipment", "Type", "Total Quantity", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventoryTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        inventoryTable.setRowHeight(40);
        invenScrollPane.setViewportView(inventoryTable);

        mainPanel.add(invenScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 1000, 220));

        equipmentTypeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Ball Sport", "Rackets or Bats", "Protective Gear", "Nets or Goals" }));
        mainPanel.add(equipmentTypeFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 140, 30));
        mainPanel.add(searchInventory, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 420, 340, 30));

        borrowArchivedTable.setBackground(new java.awt.Color(255, 255, 255));
        borrowArchivedTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Log ID", "Student Name", "Equipment Name", "Quantity Borrowed", "Status", "Action Type", "Borrowed Date", "Due Date", "Return Date", "Staff Name", "Date of Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowArchivedTable.setRowHeight(40);
        borrowArchivedScrollPane.setViewportView(borrowArchivedTable);

        mainPanel.add(borrowArchivedScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 940, 230));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void csvBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvBtnActionPerformed
        try {
            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
            String fileName = selectedStatus.equals("All")
                    ? "Borrow_Records_Report"
                    : "Borrow_Records_" + selectedStatus + "_Report";

            CSVGenerator.generateFromTable(borrowArchivedTable, fileName, this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_csvBtnActionPerformed

    private void pdfBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfBtnActionPerformed
        try {
            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
            String fileName = selectedStatus.equals("All")
                    ? "Borrow_Records_Report"
                    : "Borrow_Records_" + selectedStatus + "_Report";

            PDFGenerator.generateFromTable(borrowArchivedTable, fileName, this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_pdfBtnActionPerformed

    private void excelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excelBtnActionPerformed
        try {
            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
            String fileName = selectedStatus.equals("All")
                    ? "Borrow_Records_Report"
                    : "Borrow_Records_" + selectedStatus + "_Report";

            ExcelGenerator.generateFromTable(borrowArchivedTable, fileName, this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating Excel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_excelBtnActionPerformed

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
                    new test1().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(test1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.scroll.win11.ScrollPaneWin11 borrowArchivedScrollPane;
    private javax.swing.JTable borrowArchivedTable;
    private javax.swing.JComboBox<String> borrowHistoryFilter;
    private javax.swing.JButton csvBtn;
    private javax.swing.JComboBox<String> equipmentTypeFilter;
    private javax.swing.JButton excelBtn;
    private javax.swing.JScrollPane invenScrollPane;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton pdfBtn;
    private javax.swing.JTextField searchHistoryBtn;
    private javax.swing.JTextField searchInventory;
    // End of variables declaration//GEN-END:variables
}
