package MainProgram;

import PopupModal.GymUse;
import Database.dbConnection;
import Home.Homepage;
import PopupModal.editBorrowPopup;
import PopupModal.ViewBorrowItem;
import PopupModal.BorrowPopup;
import Home.LoginInterface;
import Home.StaffLogin;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import raven.cell.TableActionCellEditor1;
import raven.cell.TableActionCellEditor2;
import raven.cell.TableActionCellRender1;
import raven.cell.TableActionCellRender2;
import raven.cell.TableActionEvent;

public class StaffDashboard extends javax.swing.JFrame {

    private boolean isDialogOpen = false;
    private Connection conn;

    public StaffDashboard() throws IOException {
        initComponents();
        int staffID = Session.getStaffID();

        setIconLogo();

        // DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();

            String query = "SELECT employee_id, first_name, last_name FROM staff WHERE staff_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, staffID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String employeeID = rs.getString("employee_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                employeeIDTxt.setText("ID: " + employeeID);
                fnametxt.setText(firstName + ",");
                lnametxt.setText(lastName);
                staffName.setText(firstName);
            } else {
                employeeIDTxt.setText("Unknown");
                fnametxt.setText("Unknown");
                lnametxt.setText("Unknown");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
            employeeIDTxt.setText("Error Loading Employee ID");
            fnametxt.setText("Error Loading Firstname");
            lnametxt.setText("Error Loading Lastname");
        }

        //Display Table
        displayBorrowRecords();
        displayInventory();
        displayCheckInOut();

        displayBorrowTransactionHistory();

        //Dashboard Panel
        updateTotalEquipmentCount();
        updateBorrowReturnedCount();
        updateOverdueTransactionCount();
        updateEquipmentDueTodayCount();
        updateCheckInCount();
        updateCheckOutCount();

        //Refresh Table
        startPeriodicUpdate();

        //Instance AppUtilities
        AppUtilities utilities = new AppUtilities();

        //Custom fonstyle
        utilities.customFontStyle(headerLabel, "Poppins-Bold.ttf", 20f);
        utilities.customFontStyle(employeeIDTxt, "Poppins-SemiBold.ttf", 18f);
        utilities.customFontStyle(fnametxt, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(lnametxt, "Poppins-Regular.ttf", 14f);

        //Sidebar Menu fontstyle
        utilities.customFontStyle(dashboardLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(inventoryLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(transacLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(gymLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(historyTxtLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(logoutLabel, "Poppins-Regular.ttf", 16f);

        //Content FontStyle
        utilities.customFontStyle(dashLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(inventLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(transLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(gymInOutLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(historyLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(settingLabel, "Poppins-Bold.ttf", 24f);

        //Dashboard Panel Fonstyle
        utilities.customFontStyle(totalEquipTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(borrowedTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(totalOvedueTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(dueTodayTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(checkInTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(checkOutTxt, "Poppins-SemiBold.ttf", 20f);

        utilities.customFontStyle(countEquipment, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countTransactions, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countOverdue, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countDueToday, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countTimeIn, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countTimeOut, "Poppins-SemiBold.ttf", 20f);

        //Setting Panel
        utilities.customFontStyle(changePassLabel, "Poppins-SemiBold.ttf", 20f);

        utilities.textPlaceholders(oldPassword, "Old Password");
        utilities.textPlaceholders(newPassword, "New Password");
        utilities.textPlaceholders(confirmPassword, "Confrim New Password");
        utilities.customFontStyle(oldPassword, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(newPassword, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(confirmPassword, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(showPass, "Poppins-Regular.ttf", 14f);

        utilities.customFontStyle(changePassBtn, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(clearBtn, "Poppins-Regular.ttf", 16f);

        //Borrow Popup
        utilities.customFontStyle(borrowBtn, "Poppins-Regular.ttf", 16f);

        //CheckInOut Popup
        utilities.customFontStyle(checkInOutBtn, "Poppins-Regular.ttf", 16f);

        //Search
        utilities.customFontStyle(searchBorrowRecordsTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchBorrowRecordsTxt);
        utilities.customFontStyle(borrowFilter, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(filterLabel2, "Poppins-Regular.ttf", 16f);

        utilities.customFontStyle(searchInventoryTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchInventoryTxt);
        utilities.customFontStyle(equipmentTypeFilter, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(filterLabel1, "Poppins-Regular.ttf", 16f);

        utilities.customFontStyle(searchHistoryTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchHistoryTxt);
        utilities.customFontStyle(borrowHistoryFilter, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(filterLabel, "Poppins-Regular.ttf", 16f);

        utilities.customFontStyle(searchCheckInOutTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchCheckInOutTxt);

        //Table Fontsize
        utilities.customFontStyle(inventoryTable, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(borrowTable, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(checkInOutTable, "Poppins-Regular.ttf", 14f);

        utilities.customFontStyle(borrowArchivedTable, "Poppins-Regular.ttf", 14f);

        //Inventory Table Design
        JTableHeader inventoryTableHeader = inventoryTable.getTableHeader();
        utilities.customFontStyle(inventoryTableHeader, "Poppins-Bold.ttf", 14f);
        inventoryTableHeader.setBackground(Color.LIGHT_GRAY);
        inventoryTableHeader.setForeground(Color.BLACK);

        //Borrow Table Design
        JTableHeader borrowTableHeader = borrowTable.getTableHeader();
        utilities.customFontStyle(borrowTableHeader, "Poppins-Bold.ttf", 14f);
        borrowTableHeader.setBackground(Color.LIGHT_GRAY);
        borrowTableHeader.setForeground(Color.BLACK);

        //CheckInOutTable Design
        JTableHeader checkInOutTableHeader = checkInOutTable.getTableHeader();
        utilities.customFontStyle(checkInOutTableHeader, "Poppins-Bold.ttf", 14f);
        checkInOutTableHeader.setBackground(Color.LIGHT_GRAY);
        checkInOutTableHeader.setForeground(Color.BLACK);

        //Borrow History Table Design
        JTableHeader borroHistoryTableHeader = borrowArchivedTable.getTableHeader();
        utilities.customFontStyle(borroHistoryTableHeader, "Poppins-Bold.ttf", 14f);
        borroHistoryTableHeader.setBackground(Color.LIGHT_GRAY);
        borroHistoryTableHeader.setForeground(Color.BLACK);

        //Set Title
        this.setTitle("SEB System - Staff");
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Borrow Table
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onBorrowEditBtn(int row) {
                if (isDialogOpen) {
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                int transactionID = Integer.parseInt(model.getValueAt(row, 0).toString());
                String studentName = model.getValueAt(row, 1).toString();
                String equipmentName = model.getValueAt(row, 2).toString();
                String equipmentQuantity = model.getValueAt(row, 3).toString();

                try {
                    isDialogOpen = true;
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(borrowTable);
                    JPanel glassPane = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.setColor(new Color(0, 0, 0, 128));
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                    };
                    glassPane.setOpaque(false);
                    parentFrame.setGlassPane(glassPane);
                    glassPane.setVisible(true);

                    editBorrowPopup popup = new editBorrowPopup(parentFrame, true, StaffDashboard.this);
                    popup.setFields(transactionID, studentName, equipmentName, equipmentQuantity);
                    popup.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            glassPane.setVisible(false);
                            isDialogOpen = false;
                        }
                    });
                    popup.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    isDialogOpen = false;
                }

            }

            @Override
            public void onBorrowDeleteBtn(int row) {
                if (borrowTable.isEditing()) {
                    borrowTable.getCellEditor().stopCellEditing();
                }

                int id = (int) borrowTable.getValueAt(row, 0);
                String status = (String) borrowTable.getValueAt(row, 6);

                if (status.equalsIgnoreCase("borrowed") || status.equalsIgnoreCase("overdue")) {
                    JOptionPane.showMessageDialog(null,
                            "Cannot delete records with status 'Borrowed' or 'Overdue'.",
                            "Delete Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM borrow_transaction WHERE transaction_id = ?")) {
                        pstmt.setInt(1, id);
                        if (pstmt.executeUpdate() > 0) {
                            ((DefaultTableModel) borrowTable.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(null, "Item deleted successfully.");
                            displayBorrowRecords();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Unable to delete item.", "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void onInventoryDeleteBtn(int row) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onInventoryEditBtn(int row) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onReturnBorrowtBtn(int row) {
                if (borrowTable.isEditing()) {
                    borrowTable.getCellEditor().stopCellEditing();
                }

                int id = (int) borrowTable.getValueAt(row, 0);

                try (PreparedStatement checkStatusStmt = conn.prepareStatement("SELECT transaction_status FROM borrow_transaction WHERE transaction_id = ?")) {
                    checkStatusStmt.setInt(1, id);
                    ResultSet rs = checkStatusStmt.executeQuery();

                    if (rs.next()) {
                        String currentStatus = rs.getString("transaction_status");

                        if ("Returned".equalsIgnoreCase(currentStatus)) {
                            JOptionPane.showMessageDialog(null, "This item has already been marked as returned.", "Already Returned", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No record found for the selected transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to mark this item as returned?", "Confirm Return", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try (PreparedStatement pstmt = conn.prepareStatement("UPDATE borrow_transaction SET transaction_status = 'Returned', return_date = NOW() WHERE transaction_id = ?")) {
                        pstmt.setInt(1, id);

                        if (pstmt.executeUpdate() > 0) {
                            borrowTable.setValueAt("Returned", row, 8);
                            JOptionPane.showMessageDialog(null, "Item marked as returned successfully.");
                            displayBorrowRecords();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Unable to mark item as returned.", "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void onActiveBtn(int row) {

            }

            @Override
            public void onDiactiveBtn(int row) {

            }

            @Override
            public void onRemoveBtn(int row) {

            }

            @Override
            public void onBorrowViewBtn(int row) {
                if (isDialogOpen) {
                    return;
                }

                try {
                    isDialogOpen = true;

                    JPanel glassPane = new JPanel() {
                        {
                            setBackground(new Color(0, 0, 0, 128));
                        }

                        @Override
                        public void paintComponent(Graphics g) {
                            g.setColor(getBackground());
                            g.fillRect(0, 0, getWidth(), getHeight());
                        }
                    };

                    glassPane.setLayout(null);
                    glassPane.setOpaque(false);
                    setGlassPane(glassPane);

                    SwingUtilities.invokeLater(() -> {
                        glassPane.setVisible(true);

                        try {
                            DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                            String transactionId = model.getValueAt(row, 0).toString();
                            String studentName = model.getValueAt(row, 1).toString();
                            String equipmentName = model.getValueAt(row, 2).toString();
                            String quantity = model.getValueAt(row, 3).toString();
                            String borrowDate = model.getValueAt(row, 4).toString();
                            String dueDate = model.getValueAt(row, 5).toString();
                            String status = model.getValueAt(row, 6).toString();

                            ViewBorrowItem viewPopup = new ViewBorrowItem(StaffDashboard.this, true);
                            viewPopup.addWindowListener(new java.awt.event.WindowAdapter() {
                                @Override
                                public void windowClosed(java.awt.event.WindowEvent e) {
                                    glassPane.setVisible(false);
                                    isDialogOpen = false;
                                }
                            });
                            viewPopup.setFields(transactionId, studentName, equipmentName, quantity, borrowDate, dueDate, status);
                            viewPopup.setVisible(true);
                        } catch (IOException ex) {
                            Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
                            isDialogOpen = false;
                        }
                    });
                } catch (Exception ex) {
                    Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    isDialogOpen = false;
                }
            }
        };
        borrowTable.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender1());
        borrowTable.getColumnModel().getColumn(8).setCellEditor(new TableActionCellEditor1(event));

        borrowTable.getColumnModel().getColumn(9).setCellRenderer(new TableActionCellRender2());
        borrowTable.getColumnModel().getColumn(9).setCellEditor(new TableActionCellEditor2(event));

        //Search
        addSearchListener(searchInventoryTxt, this::searchInventory);
        addSearchListener(searchBorrowRecordsTxt, this::searchBorrowRecords);
        addSearchListener(searchCheckInOutTxt, this::searchCheckInOut);
        addSearchListener(searchHistoryTxt, this::searchBorrowHistory);

        // Add action listener for the filter
        borrowHistoryFilter.addActionListener(e -> filterBorrowHistory());
        equipmentTypeFilter.addActionListener(e -> filterInventory());
        borrowFilter.addActionListener(e -> filterBorrowRecords());

        // Add focus listener to search field
        addFocusListenerToSearch(searchHistoryTxt, borrowHistoryFilter, "All");
        addFocusListenerToSearch(searchInventoryTxt, equipmentTypeFilter, "All");
        addFocusListenerToSearch(searchBorrowRecordsTxt, borrowFilter, "All");

        setupTableColumns();
    }

    private void setupTableColumns() {
        // Borrow Table Configuration
        borrowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        borrowScrollPane.setMaximumSize(null);
        borrowScrollPane.setPreferredSize(null);
        borrowScrollPane.setMinimumSize(null);
        borrowScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        borrowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        borrowTable.setRowHeight(40);
        borrowTable.getTableHeader().setPreferredSize(new Dimension(
                borrowTable.getTableHeader().getPreferredSize().width, 30));

        borrowTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        borrowTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        borrowTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        borrowTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        borrowTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        borrowTable.getColumnModel().getColumn(5).setPreferredWidth(220);
        borrowTable.getColumnModel().getColumn(6).setPreferredWidth(220);
        borrowTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        borrowTable.getColumnModel().getColumn(8).setPreferredWidth(150);
        borrowTable.getColumnModel().getColumn(9).setPreferredWidth(130);

        int totalWidth = 0;
        for (int i = 0; i < borrowTable.getColumnCount(); i++) {
            totalWidth += borrowTable.getColumnModel().getColumn(i).getPreferredWidth();
        }

        borrowTable.setPreferredScrollableViewportSize(new Dimension(
                borrowScrollPane.getWidth(), borrowTable.getRowHeight() * 10));

        borrowTable.setPreferredSize(new Dimension(totalWidth,
                borrowTable.getRowHeight() * borrowTable.getRowCount()));

        borrowScrollPane.revalidate();
        borrowTable.getTableHeader().setResizingAllowed(true);
        borrowTable.getTableHeader().setReorderingAllowed(false);

        // Borrow History Table Configuration
        borrowArchivedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        borrowArchivedTable.setRowHeight(40);  // Set consistent row height

        borrowArchivedScrollPane.setMaximumSize(null);
        borrowArchivedScrollPane.setPreferredSize(null);
        borrowArchivedScrollPane.setMinimumSize(null);

        borrowArchivedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        borrowArchivedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        borrowArchivedTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        borrowArchivedTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        borrowArchivedTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        borrowArchivedTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        borrowArchivedTable.getColumnModel().getColumn(5).setPreferredWidth(180);
        borrowArchivedTable.getColumnModel().getColumn(6).setPreferredWidth(180);
        borrowArchivedTable.getColumnModel().getColumn(7).setPreferredWidth(120);
        borrowArchivedTable.getColumnModel().getColumn(8).setPreferredWidth(180);

        int totalWidth1 = 0;
        for (int i = 0; i < borrowArchivedTable.getColumnCount(); i++) {
            totalWidth1 += borrowArchivedTable.getColumnModel().getColumn(i).getPreferredWidth();
        }

        int totalHeight = borrowArchivedTable.getRowHeight() * borrowArchivedTable.getRowCount();

        borrowArchivedTable.setPreferredScrollableViewportSize(new Dimension(
                borrowArchivedScrollPane.getWidth(), borrowArchivedTable.getRowHeight() * 10));

        borrowArchivedTable.setPreferredSize(new Dimension(totalWidth1, totalHeight));

        borrowArchivedScrollPane.revalidate();
        JTableHeader archivedHeader = borrowArchivedTable.getTableHeader();
        archivedHeader.setResizingAllowed(true);
        archivedHeader.setReorderingAllowed(false);
    }

    //Dashboard Panel 1
    private int totalEquipmentCount = 0;

    private void updateTotalEquipmentCount() {
        String query = "SELECT COUNT(*) AS total FROM inventory";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalEquipmentCount = rs.getInt("total");
                countEquipment.setText(String.valueOf(totalEquipmentCount)); // Set only the number as text in jLabel1
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving total equipment count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 2
    private int recentBorrowReturned = 0;

    private void updateBorrowReturnedCount() {
        String query = "SELECT COUNT(*) AS total FROM borrow_transaction";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                recentBorrowReturned = rs.getInt("total");
                countTransactions.setText(String.valueOf(recentBorrowReturned));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving total equipment count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 3
    private int overdueTransactionCount = 0;

    private void updateOverdueTransactionCount() {
        String query = """
        SELECT COUNT(*) AS total 
        FROM borrow_transaction 
        WHERE transaction_status = 'Overdue' 
        AND return_date IS NULL 
        AND due_date < NOW()
    """;

        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                overdueTransactionCount = rs.getInt("total");
                countOverdue.setText(String.valueOf(overdueTransactionCount));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving overdue transactions count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 4
    private int equipmentDueTodayCount = 0;

    private void updateEquipmentDueTodayCount() {
        String query = """
        SELECT COUNT(*) AS total 
        FROM borrow_transaction 
        WHERE transaction_status = 'Borrowed' 
        AND return_date IS NULL 
        AND DATE(due_date) = CURRENT_DATE()
    """;

        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                equipmentDueTodayCount = rs.getInt("total");
                countDueToday.setText(String.valueOf(equipmentDueTodayCount));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving equipment due today count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 5
    private int totalCheckIn = 0;

    private void updateCheckInCount() {
        String query = "SELECT COUNT(*) AS total FROM checkin_table";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalCheckIn = rs.getInt("total");
                countTimeIn.setText(String.valueOf(totalCheckIn));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving total equipment count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 6
    private int totalCheckOut = 0;

    private void updateCheckOutCount() {
        String query = "SELECT COUNT(*) AS total FROM checkout_table";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalCheckOut = rs.getInt("total");
                countTimeOut.setText(String.valueOf(totalCheckOut));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving total equipment count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Display Envintory
    public void displayInventory() {
        String query = "SELECT equipmentID, equipmentName, type, quantity, dateAdded FROM inventory";

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
            model.setRowCount(0);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

            int numRow = 1;
            while (rs.next()) {
                String equipmentID = rs.getString("equipmentID");
                String equipmentName = rs.getString("equipmentName");
                String type = rs.getString("type");
                int quantity = rs.getInt("quantity");
                String dateAdded = rs.getString("dateAdded");

                String formattedDate = dateAdded;
                try {
                    formattedDate = outputFormat.format(inputFormat.parse(dateAdded));
                } catch (ParseException e) {
                    System.out.println("Date parsing failed for: " + dateAdded);
                }

                model.addRow(new Object[]{numRow++, equipmentID, equipmentName, type, quantity, formattedDate});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Display Inventory
    public void displayBorrowRecords() {
        String query = """
        SELECT 
            bt.transaction_id, 
            bt.student_id, 
            bt.student_name, 
            bt.course_name, 
            bt.year_level, 
            bt.equipment_id, 
            bt.equipment_name, 
            bt.quantity_borrowed, 
            bt.borrow_date, 
            bt.due_date, 
            bt.transaction_status,
            s.staff_id,
            s.first_name AS staff_first_name
        FROM borrow_transaction bt
        JOIN staff s ON bt.staff_id = s.staff_id
    """;

        try {
            int staffID = Session.getStaffID();
            try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                model.setRowCount(0);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                while (rs.next()) {
                    int id = rs.getInt("transaction_id");
                    String studentName = rs.getString("student_name");
                    String equipmentName = rs.getString("equipment_name");
                    int quantity = rs.getInt("quantity_borrowed");
                    String borrowDate = rs.getString("borrow_date");
                    String dueDate = rs.getString("due_date");
                    String status = rs.getString("transaction_status");
                    int transactionStaffId = rs.getInt("staff_id");
                    String staffDisplay = (transactionStaffId == staffID) ? "You" : rs.getString("staff_first_name");

                    String formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                    String formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));

                    model.addRow(new Object[]{id, studentName, equipmentName, quantity, status, formattedBorrowDate, formattedDueDate, staffDisplay});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing date: " + e.getMessage(), "Date Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Display CheckInOut Table
    public void displayCheckInOut() {
        String query = "SELECT "
                + "checkin_table.id AS Checkin_ID, "
                + "checkin_table.instructor_name AS Instructor, "
                + "checkin_table.class_activity AS Activity, "
                + "checkin_table.checkin_time AS Checkin_Time, "
                + "COALESCE(checkout_table.checkout_time, 'No Checkout') AS Checkout_Time, "
                + "CASE "
                + "    WHEN checkin_table.is_active = TRUE THEN 'Active' "
                + "    ELSE 'Inactive' "
                + "END AS Is_Active "
                + "FROM checkin_table "
                + "LEFT JOIN checkout_table "
                + "ON checkin_table.id = checkout_table.checkin_id";

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) checkInOutTable.getModel();
            model.setRowCount(0);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

            int numRow = 1;
            while (rs.next()) {
                int checkinId = rs.getInt("Checkin_ID");
                String instructor = rs.getString("Instructor");
                String activity = rs.getString("Activity");
                String checkinTime = rs.getString("Checkin_Time");
                String checkoutTime = rs.getString("Checkout_Time");
                String status = rs.getString("Is_Active");

                String formattedCheckinTime = checkinTime;
                String formattedCheckoutTime = checkoutTime;

                try {
                    formattedCheckinTime = outputFormat.format(inputFormat.parse(checkinTime));
                    if (!checkoutTime.equals("No Checkout")) {
                        formattedCheckoutTime = outputFormat.format(inputFormat.parse(checkoutTime));
                    }
                } catch (ParseException e) {
                    System.out.println("Date parsing failed for: " + checkinTime + " or " + checkoutTime);
                }

                model.addRow(new Object[]{numRow++, instructor, activity, formattedCheckinTime, formattedCheckoutTime, status});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Display Borrow Transaction History
    public void displayBorrowTransactionHistory() {
        String query = """
    SELECT 
        log_id, 
        bt.student_name, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        btl.transaction_status, 
        bt.borrow_date, 
        bt.due_date, 
        bt.return_date, 
        s.staff_id,
        s.first_name AS staff_first_name, 
        btl.action_timestamp 
    FROM borrow_transaction bt 
    JOIN borrow_transaction_logs btl ON bt.transaction_id = btl.transaction_id 
    JOIN staff s ON btl.staff_id = s.staff_id
    """;

        try {
            int staffID = Session.getStaffID();
            try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) borrowArchivedTable.getModel();
                model.setRowCount(0);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                while (rs.next()) {
                    int log_id = rs.getInt("log_id");
                    String studentName = rs.getString("student_name");
                    String equipmentName = rs.getString("equipment_name");
                    int quantity = rs.getInt("quantity_borrowed");
                    String status = rs.getString("transaction_status");
                    String borrowDate = rs.getString("borrow_date");
                    String dueDate = rs.getString("due_date");
                    int transactionStaffId = rs.getInt("staff_id");
                    String staffDisplay = (transactionStaffId == staffID) ? "You" : rs.getString("staff_first_name");
                    String actionTimestamp = rs.getString("action_timestamp");

                    String formattedBorrowDate = borrowDate;
                    String formattedDueDate = dueDate;
                    String formattedActionTimestamp = actionTimestamp;

                    try {
                        formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                        formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                        formattedActionTimestamp = outputFormat.format(inputFormat.parse(actionTimestamp));
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for transaction log: " + log_id);
                    }

                    model.addRow(new Object[]{log_id, studentName, equipmentName, quantity, status,
                        formattedBorrowDate, formattedDueDate, staffDisplay, formattedActionTimestamp});
                }
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
                            //Table
                            displayCheckInOut();
                            displayBorrowTransactionHistory();

                            //Dashboard Panel
                            updateTotalEquipmentCount();
                            updateBorrowReturnedCount();
                            updateOverdueTransactionCount();
                            updateEquipmentDueTodayCount();
                            updateCheckInCount();
                            updateCheckOutCount();

                            String selectedType = (String) equipmentTypeFilter.getSelectedItem();
                            if (selectedType.equals("All")) {
                                displayInventory();
                            } else {
                                filterInventory();
                            }

                            String selectedBorrowStatus = (String) borrowFilter.getSelectedItem();
                            if (selectedBorrowStatus.equals("All")) {
                                displayBorrowRecords();;
                            } else {
                                filterBorrowRecords();
                            }

                            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
                            if (selectedStatus.equals("All")) {
                                displayBorrowTransactionHistory();
                            } else {
                                filterBorrowHistory();
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

    //Search Focus
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

    //Search Inventory
    public void searchInventory() {
        String searchKeyword = searchInventoryTxt.getText().trim();
        String query = "SELECT id, equipmentID, equipmentName, type, quantity, dateAdded FROM inventory WHERE equipmentID LIKE ? OR equipmentName LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchKeyword + "%");
            pstmt.setString(2, "%" + searchKeyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
                model.setRowCount(0);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                int rowNumber = 1;
                while (rs.next()) {
                    String equipmentID = rs.getString("equipmentID");
                    String equipmentName = rs.getString("equipmentName");
                    String type = rs.getString("type");
                    int quantity = rs.getInt("quantity");
                    String date = rs.getString("dateAdded");

                    String formattedDate = date;
                    try {
                        formattedDate = outputFormat.format(inputFormat.parse(date));
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for: " + date);
                    }

                    model.addRow(new Object[]{rowNumber++, equipmentID, equipmentName, type, quantity, formattedDate});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Search Borrow Records
    public void searchBorrowRecords() {
        String searchBorrow = searchBorrowRecordsTxt.getText().trim();
        String query = """
    SELECT 
        bt.transaction_id, 
        bt.student_id, 
        bt.student_name, 
        bt.course_name, 
        bt.year_level, 
        bt.equipment_id, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        bt.borrow_date, 
        bt.due_date, 
        bt.transaction_status,
        s.staff_id,
        s.first_name AS staff_first_name 
    FROM borrow_transaction bt 
    JOIN staff s ON bt.staff_id = s.staff_id 
    WHERE bt.student_name LIKE ? OR bt.equipment_name LIKE ?
    """;

        try {
            int staffID = Session.getStaffID();
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, "%" + searchBorrow + "%");
                pstmt.setString(2, "%" + searchBorrow + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                    model.setRowCount(0);

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                    while (rs.next()) {
                        int id = rs.getInt("transaction_id");
                        String studentName = rs.getString("student_name");
                        String equipmentName = rs.getString("equipment_name");
                        int quantity = rs.getInt("quantity_borrowed");
                        String borrowDate = rs.getString("borrow_date");
                        String dueDate = rs.getString("due_date");
                        String status = rs.getString("transaction_status");
                        int transactionStaffId = rs.getInt("staff_id");
                        String staffDisplay = (transactionStaffId == staffID) ? "You" : rs.getString("staff_first_name");

                        String formattedBorrowDate = borrowDate;
                        String formattedDueDate = dueDate;
                        try {
                            formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                            formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                        } catch (ParseException e) {
                            System.out.println("Date parsing failed for transaction: " + id);
                        }

                        model.addRow(new Object[]{id, studentName, equipmentName, quantity, status,
                            formattedBorrowDate, formattedDueDate, staffDisplay});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Search Checkin/Checkout
    public void searchCheckInOut() {
        String searchKeyword = searchCheckInOutTxt.getText().trim();
        String query = "SELECT "
                + "checkin_table.id AS Checkin_ID, "
                + "checkin_table.instructor_name AS Instructor, "
                + "checkin_table.class_activity AS Activity, "
                + "checkin_table.checkin_time AS Checkin_Time, "
                + "COALESCE(checkout_table.checkout_time, 'No Checkout') AS Checkout_Time, "
                + "CASE "
                + "    WHEN checkin_table.is_active = TRUE THEN 'Active' "
                + "    ELSE 'Inactive' "
                + "END AS Is_Active "
                + "FROM checkin_table "
                + "LEFT JOIN checkout_table "
                + "ON checkin_table.id = checkout_table.checkin_id "
                + "WHERE checkin_table.instructor_name LIKE ? "
                + "OR checkin_table.checkin_time LIKE ? "
                + "OR checkout_table.checkout_time LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchKeyword + "%");
            pstmt.setString(2, "%" + searchKeyword + "%");
            pstmt.setString(3, "%" + searchKeyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) checkInOutTable.getModel();
                model.setRowCount(0);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                int rowNumber = 1;
                while (rs.next()) {
                    String instructor = rs.getString("Instructor");
                    String activity = rs.getString("Activity");
                    String checkinTime = rs.getString("Checkin_Time");
                    String checkoutTime = rs.getString("Checkout_Time");
                    String status = rs.getString("Is_Active");

                    String formattedCheckinTime = checkinTime;
                    String formattedCheckoutTime = checkoutTime;

                    try {
                        formattedCheckinTime = outputFormat.format(inputFormat.parse(checkinTime));
                        if (!checkoutTime.equals("No Checkout")) {
                            formattedCheckoutTime = outputFormat.format(inputFormat.parse(checkoutTime));
                        }
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for: " + checkinTime + " or " + checkoutTime);
                    }

                    model.addRow(new Object[]{rowNumber++, instructor, activity, formattedCheckinTime, formattedCheckoutTime, status});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchBorrowHistory() {
        String searchHistory = searchHistoryTxt.getText().trim();
        String query = """
    SELECT 
        log_id, 
        bt.student_name, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        btl.transaction_status, 
        bt.borrow_date, 
        bt.due_date, 
        bt.return_date, 
        s.staff_id,
        s.first_name AS staff_first_name, 
        btl.action_timestamp 
    FROM borrow_transaction bt 
    JOIN borrow_transaction_logs btl ON bt.transaction_id = btl.transaction_id 
    JOIN staff s ON btl.staff_id = s.staff_id 
    WHERE bt.student_name LIKE ? 
    OR bt.equipment_name LIKE ? 
    OR btl.transaction_status LIKE ? 
    OR s.first_name LIKE ?
    """;

        try {
            int staffID = Session.getStaffID();
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, "%" + searchHistory + "%");
                pstmt.setString(2, "%" + searchHistory + "%");
                pstmt.setString(3, "%" + searchHistory + "%");
                pstmt.setString(4, "%" + searchHistory + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) borrowArchivedTable.getModel();
                    model.setRowCount(0);

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                    while (rs.next()) {
                        int log_id = rs.getInt("log_id");
                        String studentName = rs.getString("student_name");
                        String equipmentName = rs.getString("equipment_name");
                        int quantity = rs.getInt("quantity_borrowed");
                        String status = rs.getString("transaction_status");
                        String borrowDate = rs.getString("borrow_date");
                        String dueDate = rs.getString("due_date");
                        int transactionStaffId = rs.getInt("staff_id");
                        String staffDisplay = (transactionStaffId == staffID) ? "You" : rs.getString("staff_first_name");
                        String actionTimestamp = rs.getString("action_timestamp");

                        String formattedBorrowDate = borrowDate;
                        String formattedDueDate = dueDate;
                        String formattedActionTimestamp = actionTimestamp;

                        try {
                            formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                            formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                            formattedActionTimestamp = outputFormat.format(inputFormat.parse(actionTimestamp));
                        } catch (ParseException e) {
                            System.out.println("Date parsing failed for transaction log: " + log_id);
                        }

                        model.addRow(new Object[]{log_id, studentName, equipmentName, quantity, status,
                            formattedBorrowDate, formattedDueDate, staffDisplay, formattedActionTimestamp});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Search Event Listener
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

    //Inventory Filter
    private void filterInventory() {
        String selectedType = (String) equipmentTypeFilter.getSelectedItem();

        if (selectedType.equals("All")) {
            displayInventory();
            return;
        }

        String query = "SELECT equipmentID, equipmentName, type, quantity, dateAdded FROM inventory WHERE type = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedType);

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
                model.setRowCount(0);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                int numRow = 1;
                while (rs.next()) {
                    String equipmentID = rs.getString("equipmentID");
                    String equipmentName = rs.getString("equipmentName");
                    String type = rs.getString("type");
                    int quantity = rs.getInt("quantity");
                    String dateAdded = rs.getString("dateAdded");

                    String formattedDateAdded = dateAdded;
                    try {
                        formattedDateAdded = outputFormat.format(inputFormat.parse(dateAdded));
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for: " + dateAdded);
                    }

                    model.addRow(new Object[]{numRow++, equipmentID, equipmentName, type, quantity, formattedDateAdded});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Borrow Filter
    private void filterBorrowRecords() {
        String selectedStatus = (String) borrowFilter.getSelectedItem();

        if (selectedStatus.equals("All")) {
            displayBorrowRecords();
            return;
        }

        String query = """
    SELECT 
        bt.transaction_id, 
        bt.student_id, 
        bt.student_name, 
        bt.course_name, 
        bt.year_level, 
        bt.equipment_id, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        bt.borrow_date, 
        bt.due_date, 
        bt.transaction_status,
        s.staff_id,
        s.first_name AS staff_first_name
    FROM borrow_transaction bt
    JOIN staff s ON bt.staff_id = s.staff_id
    WHERE bt.transaction_status = ?
    """;

        try {
            int staffID = Session.getStaffID();
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, selectedStatus);

                try (ResultSet rs = pstmt.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                    model.setRowCount(0);

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                    while (rs.next()) {
                        int id = rs.getInt("transaction_id");
                        String studentName = rs.getString("student_name");
                        String equipmentName = rs.getString("equipment_name");
                        int quantity = rs.getInt("quantity_borrowed");
                        String borrowDate = rs.getString("borrow_date");
                        String dueDate = rs.getString("due_date");
                        String status = rs.getString("transaction_status");
                        int transactionStaffId = rs.getInt("staff_id");
                        String staffDisplay = (transactionStaffId == staffID) ? "You" : rs.getString("staff_first_name");

                        String formattedBorrowDate = borrowDate;
                        String formattedDueDate = dueDate;

                        try {
                            formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                            formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                        } catch (ParseException e) {
                            System.out.println("Date parsing failed for transaction: " + id);
                        }

                        model.addRow(new Object[]{id, studentName, equipmentName, quantity, status,
                            formattedBorrowDate, formattedDueDate, staffDisplay});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
        bt.student_name, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        btl.transaction_status, 
        bt.borrow_date, 
        bt.due_date, 
        bt.return_date, 
        s.staff_id,
        s.first_name AS staff_first_name, 
        btl.action_timestamp 
    FROM borrow_transaction bt 
    JOIN borrow_transaction_logs btl ON bt.transaction_id = btl.transaction_id 
    JOIN staff s ON btl.staff_id = s.staff_id 
    WHERE btl.transaction_status = ?
    """;

        try {
            int staffID = Session.getStaffID();
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, selectedStatus);

                try (ResultSet rs = pstmt.executeQuery()) {
                    DefaultTableModel model = (DefaultTableModel) borrowArchivedTable.getModel();
                    model.setRowCount(0);

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                    while (rs.next()) {
                        int log_id = rs.getInt("log_id");
                        String studentName = rs.getString("student_name");
                        String equipmentName = rs.getString("equipment_name");
                        int quantity = rs.getInt("quantity_borrowed");
                        String status = rs.getString("transaction_status");
                        String borrowDate = rs.getString("borrow_date");
                        String dueDate = rs.getString("due_date");
                        int transactionStaffId = rs.getInt("staff_id");
                        String staffDisplay = (transactionStaffId == staffID) ? "You" : rs.getString("staff_first_name");
                        String actionTimestamp = rs.getString("action_timestamp");

                        String formattedBorrowDate = borrowDate;
                        String formattedDueDate = dueDate;
                        String formattedActionTimestamp = actionTimestamp;

                        try {
                            formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                            formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                            formattedActionTimestamp = outputFormat.format(inputFormat.parse(actionTimestamp));
                        } catch (ParseException e) {
                            System.out.println("Date parsing failed for transaction log: " + log_id);
                        }

                        model.addRow(new Object[]{log_id, studentName, equipmentName, quantity, status,
                            formattedBorrowDate, formattedDueDate, staffDisplay, formattedActionTimestamp});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        oldPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }

    //Check Hash Password
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
        headerPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        staffName = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        sidebarPanel = new javax.swing.JPanel();
        lineSeparator = new javax.swing.JSeparator();
        dashboardMenuBtn = new javax.swing.JPanel();
        dashboardLabel = new javax.swing.JLabel();
        inventoryMenuBtn = new javax.swing.JPanel();
        inventoryLabel = new javax.swing.JLabel();
        transacMenuBtn = new javax.swing.JPanel();
        transacLabel = new javax.swing.JLabel();
        gymMenuBtn = new javax.swing.JPanel();
        gymLabel = new javax.swing.JLabel();
        historyMenuBtn = new javax.swing.JPanel();
        historyTxtLabel = new javax.swing.JLabel();
        logoutMenuBtn = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        logoutLabel = new javax.swing.JLabel();
        employeeIDTxt = new javax.swing.JLabel();
        userProfile = new javax.swing.JLabel();
        lnametxt = new javax.swing.JLabel();
        fnametxt = new javax.swing.JLabel();
        settingBtn = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        dashLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        roundedPanel1 = new MainProgram.RoundedPanel();
        countEquipment = new javax.swing.JLabel();
        totalEquipTxt = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        roundedPanel2 = new MainProgram.RoundedPanel();
        countTransactions = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        borrowedTxt = new javax.swing.JLabel();
        roundedPanel6 = new MainProgram.RoundedPanel();
        countOverdue = new javax.swing.JLabel();
        totalOvedueTxt = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        roundedPanel7 = new MainProgram.RoundedPanel();
        countDueToday = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        dueTodayTxt = new javax.swing.JLabel();
        roundedPanel8 = new MainProgram.RoundedPanel();
        checkOutTxt = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        countTimeOut = new javax.swing.JLabel();
        roundedPanel4 = new MainProgram.RoundedPanel();
        checkInTxt = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        countTimeIn = new javax.swing.JLabel();
        inventoryPanel = new javax.swing.JPanel();
        inventLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        searchInventoryTxt = new javax.swing.JTextField();
        inventoryScrollPane = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        equipmentTypeFilter = new javax.swing.JComboBox<>();
        filterLabel1 = new javax.swing.JLabel();
        transacPanel = new javax.swing.JPanel();
        transLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        borrowBtn = new MainProgram.Button();
        searchBorrowRecordsTxt = new javax.swing.JTextField();
        borrowScrollPane = new javax.swing.JScrollPane();
        borrowTable = new javax.swing.JTable();
        borrowFilter = new javax.swing.JComboBox<>();
        filterLabel2 = new javax.swing.JLabel();
        gymPanel = new javax.swing.JPanel();
        gymInOutLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        checkInOutBtn = new MainProgram.Button();
        gymChecInOutScrollPane = new javax.swing.JScrollPane();
        checkInOutTable = new javax.swing.JTable();
        searchCheckInOutTxt = new javax.swing.JTextField();
        historyPanel = new javax.swing.JPanel();
        historyLabel = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        borrowArchivedScrollPane = new javax.swing.JScrollPane();
        borrowArchivedTable = new javax.swing.JTable();
        searchHistoryTxt = new javax.swing.JTextField();
        borrowHistoryFilter = new javax.swing.JComboBox<>();
        filterLabel = new javax.swing.JLabel();
        staffAccountPanel = new javax.swing.JPanel();
        settingLabel = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        roundedPanel3 = new MainProgram.RoundedPanel();
        changePassBtn = new MainProgram.Button();
        roundedPanel5 = new MainProgram.RoundedPanel();
        changePassLabel = new javax.swing.JLabel();
        clearBtn = new MainProgram.Button();
        oldPassword = new javax.swing.JPasswordField();
        newPassword = new javax.swing.JPasswordField();
        confirmPassword = new javax.swing.JPasswordField();
        showPass = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1350, 710));

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerPanel.setBackground(new java.awt.Color(13, 146, 244));
        headerPanel.setPreferredSize(new java.awt.Dimension(1375, 50));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/menu.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/account.png"))); // NOI18N

        staffName.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        staffName.setForeground(new java.awt.Color(240, 240, 240));
        staffName.setText("Staff");

        headerLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        headerLabel.setForeground(new java.awt.Color(255, 255, 255));
        headerLabel.setText("SPORTS EQUIPMENT BORROWING SYSTEM");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 429, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staffName, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, headerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(staffName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addComponent(headerLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 1145, -1));

        sidebarPanel.setBackground(new java.awt.Color(34, 45, 50));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(230, 620));
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lineSeparator.setBackground(new java.awt.Color(255, 255, 255));
        lineSeparator.setForeground(new java.awt.Color(255, 255, 255));
        sidebarPanel.add(lineSeparator, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 230, 10));

        dashboardMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        dashboardMenuBtn.setPreferredSize(new java.awt.Dimension(230, 40));
        dashboardMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dashboardMenuBtnMousePressed(evt);
            }
        });

        dashboardLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        dashboardLabel.setForeground(new java.awt.Color(255, 255, 255));
        dashboardLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/dashboard.png"))); // NOI18N
        dashboardLabel.setText("Dashboard");

        javax.swing.GroupLayout dashboardMenuBtnLayout = new javax.swing.GroupLayout(dashboardMenuBtn);
        dashboardMenuBtn.setLayout(dashboardMenuBtnLayout);
        dashboardMenuBtnLayout.setHorizontalGroup(
            dashboardMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashboardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        dashboardMenuBtnLayout.setVerticalGroup(
            dashboardMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashboardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        sidebarPanel.add(dashboardMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, -1, -1));

        inventoryMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        inventoryMenuBtn.setPreferredSize(new java.awt.Dimension(230, 40));
        inventoryMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                inventoryMenuBtnMousePressed(evt);
            }
        });

        inventoryLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        inventoryLabel.setForeground(new java.awt.Color(255, 255, 255));
        inventoryLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventory.png"))); // NOI18N
        inventoryLabel.setText("Sports Inventory ");

        javax.swing.GroupLayout inventoryMenuBtnLayout = new javax.swing.GroupLayout(inventoryMenuBtn);
        inventoryMenuBtn.setLayout(inventoryMenuBtnLayout);
        inventoryMenuBtnLayout.setHorizontalGroup(
            inventoryMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        inventoryMenuBtnLayout.setVerticalGroup(
            inventoryMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(inventoryMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 230, -1));

        transacMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        transacMenuBtn.setPreferredSize(new java.awt.Dimension(230, 40));
        transacMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                transacMenuBtnMousePressed(evt);
            }
        });

        transacLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        transacLabel.setForeground(new java.awt.Color(255, 255, 255));
        transacLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contract.png"))); // NOI18N
        transacLabel.setText("Manage Transaction");

        javax.swing.GroupLayout transacMenuBtnLayout = new javax.swing.GroupLayout(transacMenuBtn);
        transacMenuBtn.setLayout(transacMenuBtnLayout);
        transacMenuBtnLayout.setHorizontalGroup(
            transacMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transacMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transacLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        transacMenuBtnLayout.setVerticalGroup(
            transacMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transacMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transacLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(transacMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 230, -1));

        gymMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        gymMenuBtn.setPreferredSize(new java.awt.Dimension(230, 40));
        gymMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                gymMenuBtnMousePressed(evt);
            }
        });

        gymLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        gymLabel.setForeground(new java.awt.Color(255, 255, 255));
        gymLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/today.png"))); // NOI18N
        gymLabel.setText("Gym Access Log");

        javax.swing.GroupLayout gymMenuBtnLayout = new javax.swing.GroupLayout(gymMenuBtn);
        gymMenuBtn.setLayout(gymMenuBtnLayout);
        gymMenuBtnLayout.setHorizontalGroup(
            gymMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gymMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gymLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        gymMenuBtnLayout.setVerticalGroup(
            gymMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gymMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gymLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(gymMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 230, -1));

        historyMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        historyMenuBtn.setPreferredSize(new java.awt.Dimension(230, 40));
        historyMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                historyMenuBtnMousePressed(evt);
            }
        });

        historyTxtLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        historyTxtLabel.setForeground(new java.awt.Color(255, 255, 255));
        historyTxtLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/borrow.png"))); // NOI18N
        historyTxtLabel.setText("Transaction History");

        javax.swing.GroupLayout historyMenuBtnLayout = new javax.swing.GroupLayout(historyMenuBtn);
        historyMenuBtn.setLayout(historyMenuBtnLayout);
        historyMenuBtnLayout.setHorizontalGroup(
            historyMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historyTxtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        historyMenuBtnLayout.setVerticalGroup(
            historyMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historyTxtLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(historyMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, -1, -1));

        logoutMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        logoutMenuBtn.setPreferredSize(new java.awt.Dimension(230, 40));
        logoutMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logoutMenuBtnMousePressed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N

        logoutLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        logoutLabel.setForeground(new java.awt.Color(255, 255, 255));
        logoutLabel.setText("Logout");

        javax.swing.GroupLayout logoutMenuBtnLayout = new javax.swing.GroupLayout(logoutMenuBtn);
        logoutMenuBtn.setLayout(logoutMenuBtnLayout);
        logoutMenuBtnLayout.setHorizontalGroup(
            logoutMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        logoutMenuBtnLayout.setVerticalGroup(
            logoutMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logoutMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(logoutLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        sidebarPanel.add(logoutMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 670, 230, -1));

        employeeIDTxt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        employeeIDTxt.setForeground(new java.awt.Color(255, 255, 255));
        employeeIDTxt.setText("STAFF");
        sidebarPanel.add(employeeIDTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        userProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user (1).png"))); // NOI18N
        sidebarPanel.add(userProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 15, 70, 80));

        lnametxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lnametxt.setForeground(new java.awt.Color(255, 255, 255));
        lnametxt.setText("LNAME");
        sidebarPanel.add(lnametxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 55, -1, -1));

        fnametxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        fnametxt.setForeground(new java.awt.Color(255, 255, 255));
        fnametxt.setText("FNAME");
        sidebarPanel.add(fnametxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 35, -1, -1));

        settingBtn.setBackground(new java.awt.Color(34, 45, 50));
        settingBtn.setForeground(new java.awt.Color(255, 255, 255));
        settingBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/settings.png"))); // NOI18N
        settingBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        settingBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingBtnActionPerformed(evt);
            }
        });
        sidebarPanel.add(settingBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 30, 30));

        mainPanel.add(sidebarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 710));

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setPreferredSize(new java.awt.Dimension(1120, 660));
        contentPanel.setLayout(new java.awt.CardLayout());

        dashboardPanel.setBackground(new java.awt.Color(255, 255, 255));
        dashboardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dashLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        dashLabel.setForeground(new java.awt.Color(0, 0, 0));
        dashLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/grid.png"))); // NOI18N
        dashLabel.setText("DASHBOARD");
        dashboardPanel.add(dashLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, 40));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        dashboardPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 68, 1062, 20));

        roundedPanel1.setBackground(new java.awt.Color(255, 204, 51));
        roundedPanel1.setForeground(new java.awt.Color(33, 33, 33));
        roundedPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countEquipment.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countEquipment.setForeground(new java.awt.Color(33, 33, 33));
        countEquipment.setText("0");
        roundedPanel1.add(countEquipment, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        totalEquipTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        totalEquipTxt.setForeground(new java.awt.Color(33, 33, 33));
        totalEquipTxt.setText("Available Equipment");
        roundedPanel1.add(totalEquipTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventory_2.png"))); // NOI18N
        roundedPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        dashboardPanel.add(roundedPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 95, 320, 140));

        roundedPanel2.setBackground(new java.awt.Color(0, 153, 204));
        roundedPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countTransactions.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countTransactions.setForeground(new java.awt.Color(33, 33, 33));
        countTransactions.setText("0");
        roundedPanel2.add(countTransactions, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/autorenew.png"))); // NOI18N
        roundedPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

        borrowedTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        borrowedTxt.setForeground(new java.awt.Color(33, 33, 33));
        borrowedTxt.setText("Active Transactions");
        roundedPanel2.add(borrowedTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, -1, -1));

        dashboardPanel.add(roundedPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 95, 340, 140));

        roundedPanel6.setBackground(new java.awt.Color(255, 204, 51));
        roundedPanel6.setForeground(new java.awt.Color(33, 33, 33));
        roundedPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countOverdue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countOverdue.setForeground(new java.awt.Color(33, 33, 33));
        countOverdue.setText("0");
        roundedPanel6.add(countOverdue, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        totalOvedueTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        totalOvedueTxt.setForeground(new java.awt.Color(33, 33, 33));
        totalOvedueTxt.setText("Overdue Transactions");
        roundedPanel6.add(totalOvedueTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/schedule.png"))); // NOI18N
        roundedPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        dashboardPanel.add(roundedPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 95, 320, 140));

        roundedPanel7.setBackground(new java.awt.Color(0, 153, 204));
        roundedPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countDueToday.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countDueToday.setForeground(new java.awt.Color(33, 33, 33));
        countDueToday.setText("0");
        roundedPanel7.add(countDueToday, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/event.png"))); // NOI18N
        roundedPanel7.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        dueTodayTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        dueTodayTxt.setForeground(new java.awt.Color(33, 33, 33));
        dueTodayTxt.setText("Equipment Due Today");
        roundedPanel7.add(dueTodayTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        dashboardPanel.add(roundedPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 320, 140));

        roundedPanel8.setBackground(new java.awt.Color(0, 153, 204));
        roundedPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        checkOutTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        checkOutTxt.setForeground(new java.awt.Color(33, 33, 33));
        checkOutTxt.setText("Total Time-Out");
        roundedPanel8.add(checkOutTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, -1, -1));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update2.png"))); // NOI18N
        roundedPanel8.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        countTimeOut.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countTimeOut.setForeground(new java.awt.Color(33, 33, 33));
        countTimeOut.setText("0");
        roundedPanel8.add(countTimeOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        dashboardPanel.add(roundedPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 270, 320, 140));

        roundedPanel4.setBackground(new java.awt.Color(255, 153, 51));
        roundedPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        checkInTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        checkInTxt.setForeground(new java.awt.Color(33, 33, 33));
        checkInTxt.setText("Total Time-In");
        roundedPanel4.add(checkInTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update1.png"))); // NOI18N
        roundedPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, -1));

        countTimeIn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countTimeIn.setForeground(new java.awt.Color(33, 33, 33));
        countTimeIn.setText("0");
        roundedPanel4.add(countTimeIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        dashboardPanel.add(roundedPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 270, 340, 140));

        contentPanel.add(dashboardPanel, "card2");

        inventoryPanel.setBackground(new java.awt.Color(255, 255, 255));

        inventLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        inventLabel.setForeground(new java.awt.Color(0, 0, 0));
        inventLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventoryIcon.png"))); // NOI18N
        inventLabel.setText("SPORTS INVENTORY");

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        searchInventoryTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchInventoryTxtActionPerformed(evt);
            }
        });

        inventoryTable.setBackground(new java.awt.Color(255, 255, 255));
        inventoryTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Equipment ID", "Name of equipment", "Type", "Quantity Left", "Date Added"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventoryTable.setRowHeight(40);
        inventoryScrollPane.setViewportView(inventoryTable);
        if (inventoryTable.getColumnModel().getColumnCount() > 0) {
            inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(15);
            inventoryTable.getColumnModel().getColumn(0).setHeaderValue("No.");
        }

        equipmentTypeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Ball Sport", "Rackets or Bats", "Protective Gear", "Nets or Goals" }));

        filterLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        filterLabel1.setForeground(new java.awt.Color(51, 51, 51));
        filterLabel1.setText("Filter:");

        javax.swing.GroupLayout inventoryPanelLayout = new javax.swing.GroupLayout(inventoryPanel);
        inventoryPanel.setLayout(inventoryPanelLayout);
        inventoryPanelLayout.setHorizontalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPanelLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inventLabel)
                    .addGroup(inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(inventoryPanelLayout.createSequentialGroup()
                            .addComponent(filterLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(equipmentTypeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(searchInventoryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1051, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inventoryScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1051, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        inventoryPanelLayout.setVerticalGroup(
            inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(inventLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(inventoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchInventoryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(equipmentTypeFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(inventoryScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        contentPanel.add(inventoryPanel, "card3");

        transacPanel.setBackground(new java.awt.Color(255, 255, 255));

        transLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        transLabel.setForeground(new java.awt.Color(0, 0, 0));
        transLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contract2.png"))); // NOI18N
        transLabel.setText("BORROW TRANSACTIONS");

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        borrowBtn.setBackground(new java.awt.Color(13, 110, 244));
        borrowBtn.setForeground(new java.awt.Color(255, 255, 255));
        borrowBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        borrowBtn.setText("Borrow Item");
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

        searchBorrowRecordsTxt.setMargin(new java.awt.Insets(4, 10, 4, 6));

        borrowScrollPane.setPreferredSize(new java.awt.Dimension(1052, 456));

        borrowTable.setBackground(new java.awt.Color(255, 255, 255));
        borrowTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        borrowTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Transac. ID", "Student Name", "Equipment Name", "Quantity Borrowed", "Status", "Borrowed Date", "Due Date", "Processed By", "Action", "Return Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowTable.setMaximumSize(new java.awt.Dimension(1052, 455));
        borrowTable.setPreferredSize(new java.awt.Dimension(1052, 455));
        borrowTable.setRowHeight(40);
        borrowScrollPane.setViewportView(borrowTable);
        if (borrowTable.getColumnModel().getColumnCount() > 0) {
            borrowTable.getColumnModel().getColumn(0).setPreferredWidth(15);
            borrowTable.getColumnModel().getColumn(1).setPreferredWidth(40);
            borrowTable.getColumnModel().getColumn(2).setPreferredWidth(30);
            borrowTable.getColumnModel().getColumn(3).setPreferredWidth(15);
            borrowTable.getColumnModel().getColumn(4).setPreferredWidth(20);
            borrowTable.getColumnModel().getColumn(5).setPreferredWidth(40);
            borrowTable.getColumnModel().getColumn(6).setPreferredWidth(40);
            borrowTable.getColumnModel().getColumn(9).setPreferredWidth(50);
        }

        borrowFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Borrowed", "Overdue", "Returned" }));

        filterLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        filterLabel2.setForeground(new java.awt.Color(51, 51, 51));
        filterLabel2.setText("Filter:");

        javax.swing.GroupLayout transacPanelLayout = new javax.swing.GroupLayout(transacPanel);
        transacPanel.setLayout(transacPanelLayout);
        transacPanelLayout.setHorizontalGroup(
            transacPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transacPanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(transacPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transLabel)
                    .addGroup(transacPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 1052, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(transacPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, transacPanelLayout.createSequentialGroup()
                                .addComponent(borrowBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(filterLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(borrowFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(searchBorrowRecordsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(borrowScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        transacPanelLayout.setVerticalGroup(
            transacPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transacPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(transLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(transacPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borrowBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBorrowRecordsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borrowFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(borrowScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        contentPanel.add(transacPanel, "card4");

        gymPanel.setBackground(new java.awt.Color(255, 255, 255));
        gymPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gymInOutLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        gymInOutLabel.setForeground(new java.awt.Color(0, 0, 0));
        gymInOutLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/today2.png"))); // NOI18N
        gymInOutLabel.setText("GYM ACCESS LOG (TIME-IN/OUT)");
        gymPanel.add(gymInOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, 40));

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        gymPanel.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 69, 1053, 20));

        checkInOutBtn.setBackground(new java.awt.Color(13, 110, 244));
        checkInOutBtn.setForeground(new java.awt.Color(255, 255, 255));
        checkInOutBtn.setText("Time-In/Out");
        checkInOutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                checkInOutBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                checkInOutBtnMouseReleased(evt);
            }
        });
        checkInOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkInOutBtnActionPerformed(evt);
            }
        });
        gymPanel.add(checkInOutBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 95, 160, -1));

        checkInOutTable.setForeground(new java.awt.Color(0, 0, 0));
        checkInOutTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Log ID", "Instructor", "Class/Activity", "Time-In", "Time-Out", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        checkInOutTable.setRowHeight(40);
        gymChecInOutScrollPane.setViewportView(checkInOutTable);
        if (checkInOutTable.getColumnModel().getColumnCount() > 0) {
            checkInOutTable.getColumnModel().getColumn(0).setPreferredWidth(15);
        }

        gymPanel.add(gymChecInOutScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 173, 1050, 445));
        gymPanel.add(searchCheckInOutTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(726, 95, 370, 41));

        contentPanel.add(gymPanel, "card5");

        historyPanel.setBackground(new java.awt.Color(255, 255, 255));

        historyLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        historyLabel.setForeground(new java.awt.Color(0, 0, 0));
        historyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clipboardClock.png"))); // NOI18N
        historyLabel.setText("BORROW TRANSACTION HISTORY");

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));

        borrowArchivedTable.setBackground(new java.awt.Color(255, 255, 255));
        borrowArchivedTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Log ID", "Student Name", "Equipment Name", "Quantity Borrowed", "Status", "Borrowed Date", "Due Date", "Processed By", "Date of Action "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowArchivedTable.setRowHeight(40);
        borrowArchivedScrollPane.setViewportView(borrowArchivedTable);

        borrowHistoryFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Borrowed", "Overdue", "Returned" }));

        filterLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        filterLabel.setForeground(new java.awt.Color(51, 51, 51));
        filterLabel.setText("Filter:");

        javax.swing.GroupLayout historyPanelLayout = new javax.swing.GroupLayout(historyPanel);
        historyPanel.setLayout(historyPanelLayout);
        historyPanelLayout.setHorizontalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(borrowArchivedScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1046, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(historyPanelLayout.createSequentialGroup()
                            .addGap(47, 47, 47)
                            .addComponent(historyLabel))
                        .addGroup(historyPanelLayout.createSequentialGroup()
                            .addGap(47, 47, 47)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 1046, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(historyPanelLayout.createSequentialGroup()
                            .addGap(527, 527, 527)
                            .addComponent(filterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(borrowHistoryFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(searchHistoryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(52, 52, 52))
        );
        historyPanelLayout.setVerticalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(historyLabel)
                .addGap(6, 6, 6)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchHistoryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borrowHistoryFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(borrowArchivedScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        contentPanel.add(historyPanel, "card7");

        staffAccountPanel.setBackground(new java.awt.Color(255, 255, 255));
        staffAccountPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        settingLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        settingLabel.setForeground(new java.awt.Color(0, 0, 0));
        settingLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/settingIcon.png"))); // NOI18N
        settingLabel.setText("CHANGE PASSWORD SETTINGS");
        staffAccountPanel.add(settingLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, 40));

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        staffAccountPanel.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 1062, 20));

        roundedPanel3.setBackground(new java.awt.Color(204, 204, 204));

        changePassBtn.setBackground(new java.awt.Color(13, 110, 244));
        changePassBtn.setForeground(new java.awt.Color(255, 255, 255));
        changePassBtn.setText("Change Password");
        changePassBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                changePassBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                changePassBtnMouseReleased(evt);
            }
        });
        changePassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePassBtnActionPerformed(evt);
            }
        });

        roundedPanel5.setBackground(new java.awt.Color(8, 194, 255));

        changePassLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        changePassLabel.setForeground(new java.awt.Color(255, 255, 255));
        changePassLabel.setText("Change Password");

        javax.swing.GroupLayout roundedPanel5Layout = new javax.swing.GroupLayout(roundedPanel5);
        roundedPanel5.setLayout(roundedPanel5Layout);
        roundedPanel5Layout.setHorizontalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(changePassLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        roundedPanel5Layout.setVerticalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(changePassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addContainerGap())
        );

        clearBtn.setForeground(new java.awt.Color(51, 51, 51));
        clearBtn.setText("Clear");
        clearBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                clearBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                clearBtnMouseReleased(evt);
            }
        });
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        oldPassword.setPreferredSize(new java.awt.Dimension(370, 40));

        newPassword.setPreferredSize(new java.awt.Dimension(370, 40));

        confirmPassword.setPreferredSize(new java.awt.Dimension(370, 40));

        showPass.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        showPass.setForeground(new java.awt.Color(51, 51, 51));
        showPass.setText("Show Password");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel3Layout = new javax.swing.GroupLayout(roundedPanel3);
        roundedPanel3.setLayout(roundedPanel3Layout);
        roundedPanel3Layout.setHorizontalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundedPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(showPass)
                    .addComponent(oldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(roundedPanel3Layout.createSequentialGroup()
                        .addComponent(changePassBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );
        roundedPanel3Layout.setVerticalGroup(
            roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel3Layout.createSequentialGroup()
                .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(oldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(confirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(showPass)
                .addGap(18, 18, 18)
                .addGroup(roundedPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(changePassBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(clearBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );

        staffAccountPanel.add(roundedPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 630, 350));

        contentPanel.add(staffAccountPanel, "card6");

        mainPanel.add(contentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 1145, 660));

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
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function 
        utilities.setHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(inventoryMenuBtn);
        utilities.resetHoverMenuBtn(gymMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels 
        utilities.setTabPanel(dashboardPanel, true);
        utilities.setTabPanel(inventoryPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymPanel, false);
        utilities.setTabPanel(historyPanel, false);
        utilities.setTabPanel(staffAccountPanel, false);
    }//GEN-LAST:event_dashboardMenuBtnMousePressed

    private void inventoryMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(inventoryMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(gymMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(inventoryPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymPanel, false);
        utilities.setTabPanel(historyPanel, false);
        utilities.setTabPanel(staffAccountPanel, false);
    }//GEN-LAST:event_inventoryMenuBtnMousePressed

    private void transacMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transacMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(inventoryMenuBtn);
        utilities.resetHoverMenuBtn(gymMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(transacPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(inventoryPanel, false);
        utilities.setTabPanel(gymPanel, false);
        utilities.setTabPanel(historyPanel, false);
        utilities.setTabPanel(staffAccountPanel, false);
    }//GEN-LAST:event_transacMenuBtnMousePressed

    private void gymMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gymMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(gymMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(inventoryMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels 
        utilities.setTabPanel(gymPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(inventoryPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(historyPanel, false);
        utilities.setTabPanel(staffAccountPanel, false);
    }//GEN-LAST:event_gymMenuBtnMousePressed

    private void logoutMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(logoutMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(inventoryMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(gymMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);

        int response;
        response = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {

            try {
                // Get the staffID from Session
                int staffID = Session.getStaffID();  // Retrieve staffID from Session

                // Update the database to set is_online = false for the staff
                String updateSql = "UPDATE staff SET is_online = false WHERE staff_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateSql);
                updatePst.setInt(1, staffID);  // Use staffID from Session
                updatePst.executeUpdate();
                updatePst.close();

                JOptionPane.showMessageDialog(this, "You have been logged out!");

                // Close the current window and open the StaffLogin screen
                this.dispose();

//                StaffLogin login = new StaffLogin();
                Homepage login = new Homepage();
                login.setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_logoutMenuBtnMousePressed

    private void borrowBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowBtnActionPerformed
        if (isDialogOpen) {
            return;
        }

        try {
            isDialogOpen = true;

            JPanel glassPane = new JPanel() {
                {
                    setBackground(new Color(0, 0, 0, 128));
                }

                @Override
                public void paintComponent(Graphics g) {
                    g.setColor(getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            glassPane.setLayout(null);
            glassPane.setOpaque(false);
            setGlassPane(glassPane);

            SwingUtilities.invokeLater(() -> {
                glassPane.setVisible(true);
                BorrowPopup popup = null;
                try {
                    popup = new BorrowPopup(this, true, this);
                    popup.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            glassPane.setVisible(false);
                            isDialogOpen = false;
                        }
                    });
                    popup.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    isDialogOpen = false;
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
            isDialogOpen = false;
        }
    }//GEN-LAST:event_borrowBtnActionPerformed

    private void checkInOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkInOutBtnActionPerformed
        if (isDialogOpen) {
            return;
        }

        try {
            isDialogOpen = true;

            JPanel glassPane = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(0, 0, 0, 128));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            glassPane.setOpaque(false);
            setGlassPane(glassPane);
            glassPane.setVisible(true);

            GymUse gymPopup = new GymUse(this, true, this);
            gymPopup.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    glassPane.setVisible(false);
                    isDialogOpen = false;
                }
            });
            gymPopup.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(StaffDashboard.class.getName()).log(Level.SEVERE, null, ex);
            isDialogOpen = false;
        }
    }//GEN-LAST:event_checkInOutBtnActionPerformed

    private void checkInOutBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkInOutBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(checkInOutBtn, "#0B5CC9");
    }//GEN-LAST:event_checkInOutBtnMousePressed

    private void checkInOutBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkInOutBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(checkInOutBtn, "#0D6EF4");
    }//GEN-LAST:event_checkInOutBtnMouseReleased

    private void borrowBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(borrowBtn, "#0B5CC9");
    }//GEN-LAST:event_borrowBtnMousePressed

    private void borrowBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(borrowBtn, "#0D6EF4");
    }//GEN-LAST:event_borrowBtnMouseReleased

    private void settingBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingBtnActionPerformed
        // Set visibility for panels 
        AppUtilities utilities = new AppUtilities();
        utilities.setTabPanel(staffAccountPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(inventoryPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymPanel, false);
        utilities.setTabPanel(historyPanel, false);
    }//GEN-LAST:event_settingBtnActionPerformed

    private void searchInventoryTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchInventoryTxtActionPerformed

    }//GEN-LAST:event_searchInventoryTxtActionPerformed

    private void changePassBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePassBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(changePassBtn, "#0B5CC9");
    }//GEN-LAST:event_changePassBtnMousePressed

    private void changePassBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePassBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(changePassBtn, "#0D6EF4");
    }//GEN-LAST:event_changePassBtnMouseReleased

    private void clearBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(clearBtn, "#CCCCCC");
        clearBtn.setForeground(Color.decode("#FFFFFF"));
    }//GEN-LAST:event_clearBtnMousePressed

    private void clearBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(clearBtn, "#FFFFFF");
        clearBtn.setForeground(Color.decode("#333333"));
    }//GEN-LAST:event_clearBtnMouseReleased

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        clearFields();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void changePassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePassBtnActionPerformed
        String oldPass = oldPassword.getText();
        String newPass = newPassword.getText();
        String confirmPass = confirmPassword.getText();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPass.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Retrieve staffID from the Session
            int staffID = Session.getStaffID();

            // Fetch the current password from the database using staffID
            String query = "SELECT password FROM staff WHERE staff_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, staffID); // Use staffID from Session
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                boolean isOldPasswordCorrect = storedPassword.equals(oldPass) || storedPassword.equals(hashPassword(oldPass));

                if (!isOldPasswordCorrect) {
                    JOptionPane.showMessageDialog(this, "Old password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String hashedNewPass = hashPassword(newPass);
                if (hashedNewPass == null) {
                    JOptionPane.showMessageDialog(this, "Error hashing the new password.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update the password in the database
                String updateSql = "UPDATE staff SET password = ? WHERE staff_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateSql);
                updatePst.setString(1, hashedNewPass);
                updatePst.setInt(2, staffID); // Use staffID from Session
                updatePst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Password successfully changed!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(table.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error changing password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_changePassBtnActionPerformed

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        if (showPass.isSelected()) {
            confirmPassword.setEchoChar((char) 0);
        } else {
            confirmPassword.setEchoChar('•');
        }
        confirmPassword.repaint();
    }//GEN-LAST:event_showPassActionPerformed

    private void historyMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historyMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function 
        utilities.setHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(inventoryMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(gymMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels 
        utilities.setTabPanel(historyPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(inventoryPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymPanel, false);
        utilities.setTabPanel(staffAccountPanel, false);
    }//GEN-LAST:event_historyMenuBtnMousePressed

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
//                    new StaffDashboard().setVisible(true);
                    new Homepage().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(StaffLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane borrowArchivedScrollPane;
    private javax.swing.JTable borrowArchivedTable;
    private MainProgram.Button borrowBtn;
    private javax.swing.JComboBox<String> borrowFilter;
    private javax.swing.JComboBox<String> borrowHistoryFilter;
    private javax.swing.JScrollPane borrowScrollPane;
    private javax.swing.JTable borrowTable;
    private javax.swing.JLabel borrowedTxt;
    private MainProgram.Button changePassBtn;
    private javax.swing.JLabel changePassLabel;
    private MainProgram.Button checkInOutBtn;
    private javax.swing.JTable checkInOutTable;
    private javax.swing.JLabel checkInTxt;
    private javax.swing.JLabel checkOutTxt;
    private MainProgram.Button clearBtn;
    private javax.swing.JPasswordField confirmPassword;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel countDueToday;
    private javax.swing.JLabel countEquipment;
    private javax.swing.JLabel countOverdue;
    private javax.swing.JLabel countTimeIn;
    private javax.swing.JLabel countTimeOut;
    private javax.swing.JLabel countTransactions;
    private javax.swing.JLabel dashLabel;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel dashboardMenuBtn;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel dueTodayTxt;
    private javax.swing.JLabel employeeIDTxt;
    private javax.swing.JComboBox<String> equipmentTypeFilter;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JLabel filterLabel1;
    private javax.swing.JLabel filterLabel2;
    private javax.swing.JLabel fnametxt;
    private javax.swing.JScrollPane gymChecInOutScrollPane;
    private javax.swing.JLabel gymInOutLabel;
    private javax.swing.JLabel gymLabel;
    private javax.swing.JPanel gymMenuBtn;
    private javax.swing.JPanel gymPanel;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel historyLabel;
    private javax.swing.JPanel historyMenuBtn;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JLabel historyTxtLabel;
    private javax.swing.JLabel inventLabel;
    private javax.swing.JLabel inventoryLabel;
    private javax.swing.JPanel inventoryMenuBtn;
    private javax.swing.JPanel inventoryPanel;
    private javax.swing.JScrollPane inventoryScrollPane;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator lineSeparator;
    private javax.swing.JLabel lnametxt;
    private javax.swing.JLabel logoutLabel;
    private javax.swing.JPanel logoutMenuBtn;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField newPassword;
    private javax.swing.JPasswordField oldPassword;
    private MainProgram.RoundedPanel roundedPanel1;
    private MainProgram.RoundedPanel roundedPanel2;
    private MainProgram.RoundedPanel roundedPanel3;
    private MainProgram.RoundedPanel roundedPanel4;
    private MainProgram.RoundedPanel roundedPanel5;
    private MainProgram.RoundedPanel roundedPanel6;
    private MainProgram.RoundedPanel roundedPanel7;
    private MainProgram.RoundedPanel roundedPanel8;
    private javax.swing.JTextField searchBorrowRecordsTxt;
    private javax.swing.JTextField searchCheckInOutTxt;
    private javax.swing.JTextField searchHistoryTxt;
    private javax.swing.JTextField searchInventoryTxt;
    private javax.swing.JButton settingBtn;
    private javax.swing.JLabel settingLabel;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JPanel staffAccountPanel;
    private javax.swing.JLabel staffName;
    private javax.swing.JLabel totalEquipTxt;
    private javax.swing.JLabel totalOvedueTxt;
    private javax.swing.JLabel transLabel;
    private javax.swing.JLabel transacLabel;
    private javax.swing.JPanel transacMenuBtn;
    private javax.swing.JPanel transacPanel;
    private javax.swing.JLabel userProfile;
    // End of variables declaration//GEN-END:variables
}
