package MainProgram;

import Database.dbConnection;
import PopupModal.editInventoryPopup;
import PopupModal.AddNewStaffPopup;
import PopupModal.AddItemPopup;
import ReportGenerator.ExcelGenerator;
import ReportGenerator.PDFGenerator;
import Home.AdminLogin;
import Home.Homepage;
import Home.LoginInterface;
import ReportGenerator.CSVGenerator;
import ReportGenerator.ChartPDFExporter;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import raven.cell.StaffTableActionCellEditor;
import raven.cell.StaffTableActionCellRender;
import raven.cell.TableActionCellEditor;
import raven.cell.TableActionCellRender;
import raven.cell.TableActionEvent;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.swing.JScrollPane;

public class AdminDashboard extends javax.swing.JFrame {

    private boolean isDialogOpen = false;
    private Connection conn;

    public AdminDashboard() throws IOException {
        initComponents();
        int adminID = Session.getAdminID();

        //DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();

            // Fetch username from the admin table
            String query = "SELECT employee_id, username FROM admin WHERE admin_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, adminID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String employeeID = rs.getString("employee_id");
                String username = rs.getString("username");
                // Set the fetched username
                employeeIDTxt.setText("ID: " + employeeID);
                usernameTxt.setText(username);
            } else {
                employeeIDTxt.setText("Unknown");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(table.class.getName()).log(Level.SEVERE, null, ex);
            employeeIDTxt.setText("Error Loading");
        }

        //Display Inventory to Table
        displayInventory();
        displayBorrowRecords();
        displayCheckInOut();
        displayStaffAccount();

        displayBorrowTransactionHistory();

        //Dashboard Panel
        updateStaffMemberCount();
        updateTotalEquipmentCount();
        updateBorrowReturnedCount();
        updateOverdueTransactionCount();
        updateCheckInCount();
        updateCheckOutCount();

        //Chart
        createTransactionTimeChart();
        createEquipmentUsageChart();
        createOverdueChart();

        //Refresh Table
        startPeriodicUpdate();

        //System Icon
        setIconLogo();

        //Instance AppUtilities
        AppUtilities utilities = new AppUtilities();

        //Custom fonstyle
        utilities.customFontStyle(headerLabel, "Poppins-Bold.ttf", 20f);
        utilities.customFontStyle(employeeIDTxt, "Poppins-SemiBold.ttf", 18f);

        //Menu fontstyle
        utilities.customFontStyle(dashboardLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(staffLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(equipmentLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(transacLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(gymlogLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(chartLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(historyTxtLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(logoutLabel, "Poppins-Regular.ttf", 16f);

        //Content FontStyle
        utilities.customFontStyle(dashLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(inventoryLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(StaffLabel, "Poppins-Bold.ttf", 24f);
        utilities.customFontStyle(recordsLabel, "Poppins-Bold.ttf", 24f);

        //Dashboard Panel Fonstyle
        utilities.customFontStyle(staffTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(totalEquipTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(totalEquipTxt1, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(borrowedTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(overdueTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(overdueTxt1, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(timeInTxt, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(timeOutTxt, "Poppins-SemiBold.ttf", 20f);

        utilities.customFontStyle(countStaff, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countEquipment, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countBorrowReturn, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countOverdue, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countTimeIn, "Poppins-SemiBold.ttf", 20f);
        utilities.customFontStyle(countTimeOut, "Poppins-SemiBold.ttf", 20f);

        //Search
        utilities.customFontStyle(searchInventoryTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchInventoryTxt);
        utilities.customFontStyle(equipmentTypeFilter, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(filterLabel1, "Poppins-Regular.ttf", 16f);

        utilities.customFontStyle(searchBorrowRecordsTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchBorrowRecordsTxt);
        utilities.customFontStyle(borrowFilter, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(filterLabel2, "Poppins-Regular.ttf", 16f);

        utilities.customFontStyle(searchCheckInOutTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchCheckInOutTxt);

        utilities.customFontStyle(staffAccountSearch, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(staffAccountSearch);

        utilities.customFontStyle(searchHistoryTxt, "Poppins-Regular.ttf", 16f);
        utilities.initializePlaceholders(searchHistoryTxt);
        utilities.customFontStyle(borrowHistoryFilter, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(filterLabel, "Poppins-Regular.ttf", 16f);
        utilities.customFontStyle(generateFileLabel, "Poppins-Regular.ttf", 16f);

        //Custon Table FonstStyle
        utilities.customFontStyle(inventoryTable, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(borrowTable, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(checkInOutTable, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(staffAccountTable, "Poppins-Regular.ttf", 14f);

        utilities.customFontStyle(borrowArchivedTable, "Poppins-Regular.ttf", 14f);

        //New Staff Account Table Design
        JTableHeader staffAccountTableHeader = staffAccountTable.getTableHeader();
        utilities.customFontStyle(staffAccountTableHeader, "Poppins-Bold.ttf", 14f);
        staffAccountTableHeader.setBackground(Color.LIGHT_GRAY);
        staffAccountTableHeader.setForeground(Color.BLACK);

        //Inventory Table Design
        JTableHeader inventoryTableHeader = inventoryTable.getTableHeader();
        utilities.customFontStyle(inventoryTableHeader, "Poppins-Bold.ttf", 14f);
        inventoryTableHeader.setBackground(Color.LIGHT_GRAY);
        inventoryTableHeader.setForeground(Color.BLACK);

        //Borrow Equipment Table Design
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

        //Popup Button
        utilities.customFontStyle(popupBtn, "Poppins-SemiBold.ttf", 16f);

        utilities.customFontStyle(generateReportBtn, "Poppins-SemiBold.ttf", 16f);
        utilities.customFontStyle(borrowHistoryPDFBtn, "Poppins-SemiBold.ttf", 12f);
        utilities.customFontStyle(borrowHistoryExcelBtn, "Poppins-SemiBold.ttf", 12f);
        utilities.customFontStyle(borrowHistoryCSVBtn, "Poppins-SemiBold.ttf", 12f);

        //Popup Create Staff Account
        utilities.customFontStyle(addNewStaffBtn, "Poppins-SemiBold.ttf", 16f);

        this.setTitle("SEB System - Admin");
        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //Inventory Table 
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onInventoryEditBtn(int row) {
                if (isDialogOpen) {
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
                String equipmentID = model.getValueAt(row, 1).toString();
                String equipmentName = model.getValueAt(row, 2).toString();
                String equipmentType = model.getValueAt(row, 3).toString();
                String equipmentQuantity = model.getValueAt(row, 4).toString();

                try {
                    isDialogOpen = true;

                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(inventoryTable);

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

                    editInventoryPopup popup = new editInventoryPopup(parentFrame, true);
                    popup.setFields(equipmentID, equipmentName, equipmentType, equipmentQuantity);
                    popup.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            glassPane.setVisible(false);
                            isDialogOpen = false;
                        }
                    });
                    popup.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    isDialogOpen = false;
                }

                displayInventory();
            }

            @Override
            public void onInventoryDeleteBtn(int row) {
                if (inventoryTable.isEditing()) {
                    inventoryTable.getCellEditor().stopCellEditing();
                }

                int id = (int) inventoryTable.getValueAt(row, 0);

                if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM inventory WHERE id = ?")) {
                        pstmt.setInt(1, id);
                        if (pstmt.executeUpdate() > 0) {
                            ((DefaultTableModel) inventoryTable.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(null, "Item deleted successfully.");
                            displayInventory();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Unable to delete item.", "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void onBorrowEditBtn(int row) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onBorrowDeleteBtn(int row) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onReturnBorrowtBtn(int row) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onActiveBtn(int row) {
                if (staffAccountTable.isEditing()) {
                    staffAccountTable.getCellEditor().stopCellEditing();
                }

                int staff_id = (int) staffAccountTable.getValueAt(row, 0);
                if (row != -1) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to activate this staff member?", "Confirm Activation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try (PreparedStatement pstmtCheck = conn.prepareStatement("SELECT is_active FROM staff WHERE staff_id = ?")) {
                            pstmtCheck.setInt(1, staff_id);
                            ResultSet rs = pstmtCheck.executeQuery();

                            if (rs.next()) {
                                if (rs.getBoolean("is_active")) {
                                    JOptionPane.showMessageDialog(null, "This staff member is already active.", "Already Active", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    try (PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE staff SET is_active = TRUE WHERE staff_id = ?")) {
                                        pstmtUpdate.setInt(1, staff_id);
                                        if (pstmtUpdate.executeUpdate() > 0) {
                                            staffAccountTable.setValueAt("TRUE", row, 5);  // Assuming column 5 holds 'is_active'
                                            JOptionPane.showMessageDialog(null, "Staff member activated successfully.");
                                            displayStaffAccount();
                                        }
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            @Override
            public void onDiactiveBtn(int row) {
                if (staffAccountTable.isEditing()) {
                    staffAccountTable.getCellEditor().stopCellEditing();
                }

                int staff_id = (int) staffAccountTable.getValueAt(row, 0);

                if (row != -1) {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure you want to deactivate this staff member?", "Confirm Deactivation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try (PreparedStatement pstmtCheck = conn.prepareStatement("SELECT is_active FROM staff WHERE staff_id = ?")) {
                            pstmtCheck.setInt(1, staff_id);
                            ResultSet rs = pstmtCheck.executeQuery();

                            if (rs.next()) {
                                if (!rs.getBoolean("is_active")) {
                                    JOptionPane.showMessageDialog(null, "This staff member is already diactivated.", "Already diactived", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    try (PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE staff SET is_active = FALSE WHERE staff_id = ?")) {
                                        pstmtUpdate.setInt(1, staff_id);
                                        if (pstmtUpdate.executeUpdate() > 0) {
                                            staffAccountTable.setValueAt("FALSE", row, 5);  // Assuming column 5 holds 'is_active'
                                            JOptionPane.showMessageDialog(null, "Staff member deactivated successfully.");
                                            displayStaffAccount();
                                        }
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            @Override
            public void onRemoveBtn(int row) {
                if (staffAccountTable.isEditing()) {
                    staffAccountTable.getCellEditor().stopCellEditing();
                }

                int employee_id = (int) staffAccountTable.getValueAt(row, 0);

                if (JOptionPane.showConfirmDialog(null, "\"Are you sure you want to remove this staff member?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM staff WHERE staff_id = ?")) {
                        pstmt.setInt(1, employee_id);
                        if (pstmt.executeUpdate() > 0) {
                            ((DefaultTableModel) staffAccountTable.getModel()).removeRow(row);
                            JOptionPane.showMessageDialog(null, "Staff removed successfully.");
                            displayStaffAccount();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Unable to remove staff member.", "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void onBorrowViewBtn(int row) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        inventoryTable.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        inventoryTable.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event));

        staffAccountTable.getColumnModel().getColumn(6).setCellRenderer(new StaffTableActionCellRender());
        staffAccountTable.getColumnModel().getColumn(6).setCellEditor(new StaffTableActionCellEditor(event));

        // Add action listener for the filter
        borrowHistoryFilter.addActionListener(e -> filterBorrowHistory());
        equipmentTypeFilter.addActionListener(e -> filterInventory());
        borrowFilter.addActionListener(e -> filterBorrowRecords());

        // Add focus listener to search field
        addFocusListenerToSearch(searchHistoryTxt, borrowHistoryFilter, "All");
        addFocusListenerToSearch(searchInventoryTxt, equipmentTypeFilter, "All");
        addFocusListenerToSearch(searchBorrowRecordsTxt, borrowFilter, "All");

        //Chart ComboBox
        // Add action listeners to existing combo boxes
        utilities.customFontStyle(dateRange, "Poppins-Regular.ttf", 14f);
        utilities.customFontStyle(typeChart, "Poppins-Regular.ttf", 14f);
        // Add action listeners to existing combo boxes
        dateRange.addActionListener(e -> createTransactionTimeChart());
        typeChart.addActionListener(e -> createTransactionTimeChart());

        //Search
        addSearchListener(searchInventoryTxt, this::searchInventory);
        addSearchListener(searchBorrowRecordsTxt, this::searchBorrowRecords);
        addSearchListener(searchCheckInOutTxt, this::searchCheckInOut);
        addSearchListener(staffAccountSearch, this::searchStaffAccount);
        addSearchListener(searchHistoryTxt, this::searchBorrowHistory);

        //Disable Edit Inventory Entry Field
        editInventoryPopup disableEntry = new editInventoryPopup(this, true);
        disableEntry.disableEntry();

        setupTableColumns();
    }

    private void setupTableColumns() {
        // Staff Account Table
        staffAccountTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        createStaffAccountScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        createStaffAccountScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        staffAccountTable.getColumnModel().getColumn(0).setPreferredWidth(80);   // Row Number
        staffAccountTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // Employee ID
        staffAccountTable.getColumnModel().getColumn(2).setPreferredWidth(250);  // Name
        staffAccountTable.getColumnModel().getColumn(3).setPreferredWidth(250);  // Email
        staffAccountTable.getColumnModel().getColumn(4).setPreferredWidth(120);  // Status
        staffAccountTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Online Status
        staffAccountTable.getColumnModel().getColumn(6).setPreferredWidth(300);  // Action

        // Borrow Records Table - Fixed version
        borrowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        borrowTable.setRowHeight(40);

        borrowScrollPane.setMaximumSize(null);
        borrowScrollPane.setPreferredSize(null);
        borrowScrollPane.setMinimumSize(null);

        borrowScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        borrowScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        borrowTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        borrowTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        borrowTable.getColumnModel().getColumn(2).setPreferredWidth(220);
        borrowTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        borrowTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        borrowTable.getColumnModel().getColumn(5).setPreferredWidth(180);
        borrowTable.getColumnModel().getColumn(6).setPreferredWidth(180);
        borrowTable.getColumnModel().getColumn(7).setPreferredWidth(180);
        borrowTable.getColumnModel().getColumn(8).setPreferredWidth(150);
        borrowTable.getColumnModel().getColumn(9).setPreferredWidth(180);

        int totalWidth = 0;
        for (int i = 0; i < borrowTable.getColumnCount(); i++) {
            totalWidth += borrowTable.getColumnModel().getColumn(i).getPreferredWidth();
        }

        int totalHeight = borrowTable.getRowHeight() * borrowTable.getRowCount();

        borrowTable.setPreferredScrollableViewportSize(new Dimension(
                borrowScrollPane.getWidth(), borrowTable.getRowHeight() * 10));

        borrowTable.setPreferredSize(new Dimension(totalWidth, totalHeight));

        borrowScrollPane.revalidate();
        borrowTable.getTableHeader().setResizingAllowed(true);
        borrowTable.getTableHeader().setReorderingAllowed(false);

        // Borrow History Table Configuration
        borrowArchivedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        borrowArchivedTable.setRowHeight(40);

        borrowArchivedScrollPane.setMaximumSize(null);
        borrowArchivedScrollPane.setPreferredSize(null);
        borrowArchivedScrollPane.setMinimumSize(null);

        borrowArchivedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        borrowArchivedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        borrowArchivedTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        borrowArchivedTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        borrowArchivedTable.getColumnModel().getColumn(2).setPreferredWidth(220);
        borrowArchivedTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        borrowArchivedTable.getColumnModel().getColumn(5).setPreferredWidth(140);
        borrowArchivedTable.getColumnModel().getColumn(6).setPreferredWidth(190);
        borrowArchivedTable.getColumnModel().getColumn(7).setPreferredWidth(190);
        borrowArchivedTable.getColumnModel().getColumn(8).setPreferredWidth(190);
        borrowArchivedTable.getColumnModel().getColumn(9).setPreferredWidth(150);
        borrowArchivedTable.getColumnModel().getColumn(10).setPreferredWidth(180);

        int totalWidth1 = 0;
        for (int i = 0; i < borrowArchivedTable.getColumnCount(); i++) {
            totalWidth1 += borrowArchivedTable.getColumnModel().getColumn(i).getPreferredWidth();
        }

        int totalHeight1 = borrowArchivedTable.getRowHeight() * borrowArchivedTable.getRowCount();

        borrowArchivedTable.setPreferredScrollableViewportSize(new Dimension(
                borrowArchivedScrollPane.getWidth(), borrowArchivedTable.getRowHeight() * 10));

        borrowArchivedTable.setPreferredSize(new Dimension(totalWidth1, totalHeight1));

        borrowArchivedScrollPane.revalidate();
        borrowArchivedTable.getTableHeader().setResizingAllowed(true);
        borrowArchivedTable.getTableHeader().setReorderingAllowed(false);
    }

    //Dashboard Panel 1
    private int totalStaffMemberCount = 0;

    private void updateStaffMemberCount() {
        String query = "SELECT COUNT(*) AS total FROM staff";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalStaffMemberCount = rs.getInt("total");
                countStaff.setText(String.valueOf(totalStaffMemberCount));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving total equipment count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 2
    private int totalEquipmentCount = 0;

    private void updateTotalEquipmentCount() {
        String query = "SELECT COUNT(*) AS total FROM inventory";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalEquipmentCount = rs.getInt("total");
                countEquipment.setText(String.valueOf(totalEquipmentCount));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving total equipment count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 3
    private int recentBorrowReturned = 0;

    private void updateBorrowReturnedCount() {
        String query = "SELECT COUNT(*) AS total FROM borrow_transaction";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                recentBorrowReturned = rs.getInt("total");
                countBorrowReturn.setText(String.valueOf(recentBorrowReturned));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving total equipment count: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    //Dashboard Panel 4
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

    //Display Staff Account
    public void displayStaffAccount() {
        String query = "SELECT staff_id, employee_id, first_name, last_name, email, "
                + "CASE WHEN is_active = TRUE THEN 'Actived' ELSE 'Diactivated' END AS is_active, "
                + "CASE WHEN is_online = TRUE THEN 'Online' ELSE 'Offline' END AS is_online, "
                + "date_added "
                + "FROM staff";

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) staffAccountTable.getModel();
            model.setRowCount(0);

            int rowCount = 1;
            while (rs.next()) {
                int id = rs.getInt("staff_id");
                String Employee_ID = rs.getString("employee_id");
                String Firstname = rs.getString("first_name");
                String Lastname = rs.getString("last_name");
                String Email = rs.getString("email");
                String status = rs.getString("is_active");
                String online = rs.getString("is_online");
                model.addRow(new Object[]{rowCount++, Employee_ID, Firstname + " " + Lastname, Email, status, online, null});
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

    //Display Borrow records
    public void displayBorrowRecords() {
        String query = """
    SELECT 
        bt.transaction_id, 
        bt.student_name, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        bt.transaction_status, 
        bt.borrow_date, 
        bt.due_date, 
        bt.return_date, 
        s.first_name AS staff_first_name, 
        bt.created_at 
    FROM borrow_transaction bt
    JOIN staff s ON bt.staff_id = s.staff_id
    """;

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
            model.setRowCount(0);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

            while (rs.next()) {
                int transactionID = rs.getInt("transaction_id");
                String studentName = rs.getString("student_name");
                String equipmentName = rs.getString("equipment_name");
                int quantity = rs.getInt("quantity_borrowed");
                String status = rs.getString("transaction_status");
                String borrowDate = rs.getString("borrow_date");
                String dueDate = rs.getString("due_date");
                String returnDate = rs.getString("return_date");
                String staffFirstName = rs.getString("staff_first_name");
                String dateAdded = rs.getString("created_at");

                String formattedBorrowDate = borrowDate;
                String formattedDueDate = dueDate;
                String formattedReturnDate = returnDate;
                String formattedDateAdded = dateAdded;

                try {
                    formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                    formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                    formattedDateAdded = outputFormat.format(inputFormat.parse(dateAdded));
                    if (returnDate != null) {
                        formattedReturnDate = outputFormat.format(inputFormat.parse(returnDate));
                    } else {
                        formattedReturnDate = "Not returned yet";
                    }
                } catch (ParseException e) {
                    System.out.println("Date parsing failed for transaction: " + transactionID);
                }

                model.addRow(new Object[]{transactionID, studentName, equipmentName, quantity, status,
                    formattedBorrowDate, formattedDueDate, formattedReturnDate, staffFirstName, formattedDateAdded});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
                + "    ELSE 'Inactivate' "
                + "END AS Is_Active "
                + "FROM checkin_table "
                + "LEFT JOIN checkout_table "
                + "ON checkin_table.id = checkout_table.checkin_id";

        try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) checkInOutTable.getModel();
            model.setRowCount(0);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

            int rowNumber = 1;
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
                    System.out.println("Date parsing failed for checkin ID: " + checkinId);
                }

                model.addRow(new Object[]{rowNumber++, instructor, activity, formattedCheckinTime, formattedCheckoutTime, status});
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

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

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
                String staffFirstName = rs.getString("staff_first_name");
                String actionTimestamp = rs.getString("action_timestamp");

                String formattedBorrowDate = borrowDate;
                String formattedDueDate = dueDate;
                String formattedReturnDate = returnDate;
                String formattedActionTimestamp = actionTimestamp;

                try {
                    formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                    formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                    formattedActionTimestamp = outputFormat.format(inputFormat.parse(actionTimestamp));
                    if (returnDate != null && !returnDate.equals("Not returned yet")) {
                        formattedReturnDate = outputFormat.format(inputFormat.parse(returnDate));
                    } else {
                        formattedReturnDate = "Not returned yet";
                    }
                } catch (ParseException e) {
                    System.out.println("Date parsing failed for log ID: " + logId);
                }

                model.addRow(new Object[]{logId, studentName, equipmentName, quantity, status, actionType,
                    formattedBorrowDate, formattedDueDate, formattedReturnDate, staffFirstName, formattedActionTimestamp});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //CHART
    private void createEquipmentUsageChart() {
        String query = """
        SELECT 
            equipment_name,
            COUNT(*) as borrow_count
        FROM borrow_transaction
        WHERE borrow_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
        GROUP BY equipment_name
        ORDER BY borrow_count DESC
        LIMIT 10
    """;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String equipment = rs.getString("equipment_name");
                int count = rs.getInt("borrow_count");
                dataset.addValue(count, "Borrows", equipment);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Most Borrowed Equipment",
                    "Equipment",
                    "Number of Borrows",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            chart.setBackgroundPaint(Color.WHITE);

            try {
                Font titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Bold.ttf")).deriveFont(16f);
                chart.getTitle().setFont(titleFont);

                CategoryPlot plot = chart.getCategoryPlot();

                Font axisFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Regular.ttf")).deriveFont(12f);
                plot.getDomainAxis().setLabelFont(axisFont);
                plot.getDomainAxis().setTickLabelFont(axisFont);

                plot.getRangeAxis().setLabelFont(axisFont);
                plot.getRangeAxis().setTickLabelFont(axisFont);

                plot.setBackgroundPaint(Color.WHITE);
                plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
                plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

                BarRenderer renderer = (BarRenderer) plot.getRenderer();
                renderer.setSeriesPaint(0, new Color(40, 167, 69));
                renderer.setBarPainter(new StandardBarPainter());

            } catch (FontFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading custom fonts: " + e.getMessage(),
                        "Font Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
            }

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(487, 290));

            equipmentChartPanel.removeAll();
            equipmentChartPanel.setLayout(new BorderLayout());
            equipmentChartPanel.add(chartPanel, BorderLayout.CENTER);
            equipmentChartPanel.revalidate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating equipment usage chart: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createOverdueChart() {
        String query = """
        SELECT 
            transaction_status,
            COUNT(*) as status_count
        FROM borrow_transaction
        GROUP BY transaction_status
    """;

        DefaultPieDataset dataset = new DefaultPieDataset();
        int totalCount = 0;
        Map<String, Integer> statusCounts = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String status = rs.getString("transaction_status");
                int count = rs.getInt("status_count");
                totalCount += count;
                statusCounts.put(status, count);
            }

            for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
                String status = entry.getKey();
                int count = entry.getValue();
                double percentage = (count * 100.0) / totalCount;
                dataset.setValue(status, count);
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Transaction Status Distribution",
                    dataset,
                    true,
                    true,
                    false
            );

            chart.setBackgroundPaint(Color.WHITE);

            try {
                Font titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Bold.ttf")).deriveFont(16f);
                chart.getTitle().setFont(titleFont);

                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setBackgroundPaint(Color.WHITE);

                plot.setSectionPaint("Borrowed", Color.decode("#00BFA6"));
                plot.setSectionPaint("Overdue", Color.decode("#FF6D00"));
                plot.setSectionPaint("Returned", Color.decode("#3D5AFE"));

                Font labelFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Regular.ttf")).deriveFont(12f);
                plot.setLabelFont(labelFont);

                plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                        "{0}\n{1} ({2})",
                        new DecimalFormat("0"),
                        new DecimalFormat("0.0%")
                ));

                plot.setLabelBackgroundPaint(new Color(255, 255, 255, 180));
                plot.setLabelOutlinePaint(null);
                plot.setLabelShadowPaint(null);
                plot.setLabelLinksVisible(true);

            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading custom fonts: " + e.getMessage(),
                        "Font Error", JOptionPane.ERROR_MESSAGE);
            }

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(440, 290));

            overdueChartPanel.removeAll();
            overdueChartPanel.setLayout(new BorderLayout());
            overdueChartPanel.add(chartPanel, BorderLayout.CENTER);
            overdueChartPanel.revalidate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating overdue chart: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTransactionTimeChart() {
        String timeFrame = (String) dateRange.getSelectedItem();
        String chartType = (String) typeChart.getSelectedItem();

        String query;
        switch (timeFrame) {
            case "Daily":
                query = """
                SELECT 
                    DATE(borrow_date) as date,
                    COUNT(*) as transaction_count,
                    'Borrowed' as transaction_type
                FROM borrow_transaction
                WHERE borrow_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
                GROUP BY date
                UNION ALL
                SELECT 
                    DATE(return_date) as date,
                    COUNT(*) as transaction_count,
                    'Returned' as transaction_type
                FROM borrow_transaction
                WHERE return_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
                AND return_date IS NOT NULL
                GROUP BY date
                ORDER BY date
            """;
                break;

            case "Weekly":
                query = """
                SELECT 
                    DATE(DATE_SUB(borrow_date, INTERVAL WEEKDAY(borrow_date) DAY)) as week_start,
                    COUNT(*) as transaction_count,
                    'Borrowed' as transaction_type
                FROM borrow_transaction
                WHERE borrow_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
                GROUP BY week_start
                UNION ALL
                SELECT 
                    DATE(DATE_SUB(return_date, INTERVAL WEEKDAY(return_date) DAY)) as week_start,
                    COUNT(*) as transaction_count,
                    'Returned' as transaction_type
                FROM borrow_transaction
                WHERE return_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
                AND return_date IS NOT NULL
                GROUP BY week_start
                ORDER BY week_start
            """;
                break;

            case "Monthly":
                query = """
                SELECT 
                    DATE_FORMAT(borrow_date, '%Y-%m-01') as month_start,
                    COUNT(*) as transaction_count,
                    'Borrowed' as transaction_type
                FROM borrow_transaction
                WHERE borrow_date >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
                GROUP BY month_start
                UNION ALL
                SELECT 
                    DATE_FORMAT(return_date, '%Y-%m-01') as month_start,
                    COUNT(*) as transaction_count,
                    'Returned' as transaction_type
                FROM borrow_transaction
                WHERE return_date >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
                AND return_date IS NOT NULL
                GROUP BY month_start
                ORDER BY month_start
            """;
                break;

            default:
                query = "";
                break;
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            SimpleDateFormat dateFormat;
            String dateColumn;

            switch (timeFrame) {
                case "Daily":
                    dateFormat = new SimpleDateFormat("MMM dd");
                    dateColumn = "date";
                    break;
                case "Weekly":
                    dateFormat = new SimpleDateFormat("MMM dd");
                    dateColumn = "week_start";
                    break;
                case "Monthly":
                    dateFormat = new SimpleDateFormat("MMM yyyy");
                    dateColumn = "month_start";
                    break;
                default:
                    dateFormat = new SimpleDateFormat("MMM dd");
                    dateColumn = "date";
            }

            while (rs.next()) {
                String date = dateFormat.format(rs.getDate(dateColumn));
                int count = rs.getInt("transaction_count");
                String type = rs.getString("transaction_type");
                dataset.addValue(count, type, date);
            }

            JFreeChart chart;
            if ("Bar Chart".equals(chartType)) {
                chart = createBarChart(dataset);
            } else {
                chart = createLineChart(dataset);
            }

            chart.setBackgroundPaint(Color.WHITE);
            chart.getTitle().setFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Bold.ttf")).deriveFont(18f));

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.WHITE);
            plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            plot.setOutlineVisible(false);

            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setTickLabelFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Regular.ttf")).deriveFont(12f));
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setTickLabelFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/fonts/Poppins-Regular.ttf")).deriveFont(12f));

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(710, 296));
            chartPanel.setMouseWheelEnabled(true);

            graphPanel.removeAll();
            graphPanel.setLayout(new BorderLayout());
            graphPanel.add(chartPanel, BorderLayout.CENTER);
            graphPanel.revalidate();

        } catch (SQLException | FontFormatException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating chart: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JFreeChart createLineChart(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(
                "Transactions Over Time",
                "Date",
                "Number of Transactions",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();

        renderer.setSeriesPaint(0, new Color(0, 123, 255));
        renderer.setSeriesPaint(1, new Color(40, 167, 69));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));

        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultShapesFilled(true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_CENTER)
        );

        plot.setRenderer(renderer);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.15);
        rangeAxis.setLowerMargin(0.15);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.4);
        domainAxis.setLowerMargin(0.05);
        domainAxis.setUpperMargin(0.05);

        return chart;
    }

    private JFreeChart createBarChart(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Transactions Over Time",
                "Date",
                "Number of Transactions",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer();

        renderer.setSeriesPaint(0, new Color(0, 123, 255));
        renderer.setSeriesPaint(1, new Color(40, 167, 69));
        renderer.setBarPainter(new StandardBarPainter());

        renderer.setItemMargin(0.0);
        plot.setRenderer(renderer);

        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER)
        );

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.4);
        domainAxis.setLowerMargin(0.05);
        domainAxis.setUpperMargin(0.05);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.15);

        renderer.setMaximumBarWidth(0.1);

        return chart;
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
                            displayStaffAccount();

                            //Dashboard Panel
                            updateStaffMemberCount();
                            updateTotalEquipmentCount();
                            updateBorrowReturnedCount();
                            updateOverdueTransactionCount();
                            updateCheckInCount();
                            updateCheckOutCount();

                            //Chart
                            createTransactionTimeChart();
                            createEquipmentUsageChart();
                            createOverdueChart();

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

    //Search Staff Account
    public void searchStaffAccount() {
        String searchKeyword = staffAccountSearch.getText().trim();
        String query = "SELECT staff_id, employee_id, first_name, last_name, email, "
                + "CASE WHEN is_active = TRUE THEN 'Actived' ELSE 'Diactivated' END AS is_active, "
                + "CASE WHEN is_online = TRUE THEN 'Online' ELSE 'Offline' END AS is_online "
                + "FROM staff "
                + "WHERE employee_id LIKE ? "
                + "OR first_name LIKE ? "
                + "OR last_name LIKE ? "
                + "OR email LIKE ? "
                + "OR CONCAT(first_name, ' ', last_name) LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + searchKeyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) staffAccountTable.getModel();
                model.setRowCount(0);

                int rowCount = 1;
                while (rs.next()) {
                    String Employee_ID = rs.getString("employee_id");
                    String Firstname = rs.getString("first_name");
                    String Lastname = rs.getString("last_name");
                    String Email = rs.getString("email");
                    String status = rs.getString("is_active");
                    String online = rs.getString("is_online");
                    model.addRow(new Object[]{rowCount++, Employee_ID, Firstname + " " + Lastname, Email, status, online, null});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Search Inventory
    public void searchInventory() {
        String searchKeyword = searchInventoryTxt.getText().trim();
        String query = "SELECT id, equipmentID, equipmentName, type, quantity FROM inventory WHERE equipmentID LIKE ? OR equipmentName LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchKeyword + "%");
            pstmt.setString(2, "%" + searchKeyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
                model.setRowCount(0);

                int rowNumber = 1;
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String equipmentID = rs.getString("equipmentID");
                    String equipmentName = rs.getString("equipmentName");
                    String type = rs.getString("type");
                    int quantity = rs.getInt("quantity");
                    model.addRow(new Object[]{rowNumber++, equipmentID, equipmentName, type, quantity});
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
        bt.student_name, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        bt.transaction_status, 
        bt.borrow_date, 
        bt.due_date, 
        bt.return_date, 
        s.first_name AS staff_first_name, 
        bt.created_at 
    FROM borrow_transaction bt
    JOIN staff s ON bt.staff_id = s.staff_id
    WHERE bt.student_name LIKE ? OR bt.equipment_name LIKE ?
    """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + searchBorrow + "%");
            pstmt.setString(2, "%" + searchBorrow + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                model.setRowCount(0);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                while (rs.next()) {
                    int transactionID = rs.getInt("transaction_id");
                    String studentName = rs.getString("student_name");
                    String equipmentName = rs.getString("equipment_name");
                    int quantity = rs.getInt("quantity_borrowed");
                    String status = rs.getString("transaction_status");
                    String borrowDate = rs.getString("borrow_date");
                    String dueDate = rs.getString("due_date");
                    String returnDate = rs.getString("return_date");
                    String staffFirstName = rs.getString("staff_first_name");
                    String dateAdded = rs.getString("created_at");

                    String formattedBorrowDate = borrowDate;
                    String formattedDueDate = dueDate;
                    String formattedReturnDate = returnDate;
                    String formattedDateAdded = dateAdded;

                    try {
                        formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                        formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                        formattedDateAdded = outputFormat.format(inputFormat.parse(dateAdded));
                        if (returnDate != null) {
                            formattedReturnDate = outputFormat.format(inputFormat.parse(returnDate));
                        } else {
                            formattedReturnDate = "Not returned yet";
                        }
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for transaction: " + transactionID);
                    }

                    model.addRow(new Object[]{transactionID, studentName, equipmentName, quantity, status,
                        formattedBorrowDate, formattedDueDate, formattedReturnDate, staffFirstName, formattedDateAdded});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
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
                        System.out.println("Date parsing failed for row: " + rowNumber);
                    }

                    model.addRow(new Object[]{rowNumber++, instructor, activity, formattedCheckinTime, formattedCheckoutTime, status});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Search Borrow History
    public void searchBorrowHistory() {
        String searchHistory = searchHistoryTxt.getText().trim();
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

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

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
                    String staffFirstName = rs.getString("staff_first_name");
                    String actionTimestamp = rs.getString("action_timestamp");

                    String formattedBorrowDate = borrowDate;
                    String formattedDueDate = dueDate;
                    String formattedReturnDate = returnDate;
                    String formattedActionTimestamp = actionTimestamp;

                    try {
                        formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                        formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                        formattedActionTimestamp = outputFormat.format(inputFormat.parse(actionTimestamp));
                        if (returnDate != null && !returnDate.equals("Not returned yet")) {
                            formattedReturnDate = outputFormat.format(inputFormat.parse(returnDate));
                        } else {
                            formattedReturnDate = "Not returned yet";
                        }
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for log ID: " + logId);
                    }

                    model.addRow(new Object[]{logId, studentName, equipmentName, quantity, status, actionType,
                        formattedBorrowDate, formattedDueDate, formattedReturnDate, staffFirstName, formattedActionTimestamp});
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

    //Inventory Filter
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
        bt.student_name, 
        bt.equipment_name, 
        bt.quantity_borrowed, 
        bt.transaction_status, 
        bt.borrow_date, 
        bt.due_date, 
        bt.return_date, 
        s.first_name AS staff_first_name, 
        bt.created_at 
    FROM borrow_transaction bt
    JOIN staff s ON bt.staff_id = s.staff_id
    WHERE bt.transaction_status = ?
    """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedStatus);

            try (ResultSet rs = pstmt.executeQuery()) {
                DefaultTableModel model = (DefaultTableModel) borrowTable.getModel();
                model.setRowCount(0);

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

                while (rs.next()) {
                    int transactionID = rs.getInt("transaction_id");
                    String studentName = rs.getString("student_name");
                    String equipmentName = rs.getString("equipment_name");
                    int quantity = rs.getInt("quantity_borrowed");
                    String status = rs.getString("transaction_status");
                    String borrowDate = rs.getString("borrow_date");
                    String dueDate = rs.getString("due_date");
                    String returnDate = rs.getString("return_date");
                    String staffFirstName = rs.getString("staff_first_name");
                    String dateAdded = rs.getString("created_at");

                    String formattedBorrowDate = borrowDate;
                    String formattedDueDate = dueDate;
                    String formattedReturnDate = returnDate;
                    String formattedDateAdded = dateAdded;

                    try {
                        formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                        formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                        formattedDateAdded = outputFormat.format(inputFormat.parse(dateAdded));
                        if (returnDate != null) {
                            formattedReturnDate = outputFormat.format(inputFormat.parse(returnDate));
                        } else {
                            formattedReturnDate = "Not returned yet";
                        }
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for transaction: " + transactionID);
                    }

                    model.addRow(new Object[]{transactionID, studentName, equipmentName, quantity, status,
                        formattedBorrowDate, formattedDueDate, formattedReturnDate, staffFirstName, formattedDateAdded});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Borrow History Filter
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

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

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
                    String staffFirstName = rs.getString("staff_first_name");
                    String actionTimestamp = rs.getString("action_timestamp");

                    String formattedBorrowDate = borrowDate;
                    String formattedDueDate = dueDate;
                    String formattedReturnDate = returnDate;
                    String formattedActionTimestamp = actionTimestamp;

                    try {
                        formattedBorrowDate = outputFormat.format(inputFormat.parse(borrowDate));
                        formattedDueDate = outputFormat.format(inputFormat.parse(dueDate));
                        formattedActionTimestamp = outputFormat.format(inputFormat.parse(actionTimestamp));
                        if (returnDate != null && !returnDate.equals("Not returned yet")) {
                            formattedReturnDate = outputFormat.format(inputFormat.parse(returnDate));
                        } else {
                            formattedReturnDate = "Not returned yet";
                        }
                    } catch (ParseException e) {
                        System.out.println("Date parsing failed for log ID: " + logId);
                    }

                    model.addRow(new Object[]{logId, studentName, equipmentName, quantity, status, actionType,
                        formattedBorrowDate, formattedDueDate, formattedReturnDate, staffFirstName, formattedActionTimestamp});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    //set icon to java frame
    public final void setIconLogo() {
        Image iconLogo = new ImageIcon(getClass().getResource("/icons/SEB_System.png")).getImage();
        this.setIconImage(iconLogo);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        menuPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        usernameTxt = new javax.swing.JLabel();
        sidebarPanel = new javax.swing.JPanel();
        lineSeparator = new javax.swing.JSeparator();
        dashboardMenuBtn = new javax.swing.JPanel();
        dashboardLabel = new javax.swing.JLabel();
        staffMenuBtn = new javax.swing.JPanel();
        staffLabel = new javax.swing.JLabel();
        transacMenuBtn = new javax.swing.JPanel();
        transacLabel = new javax.swing.JLabel();
        equipmentMenuBtn = new javax.swing.JPanel();
        equipmentLabel = new javax.swing.JLabel();
        gymlogMenuBtn = new javax.swing.JPanel();
        gymlogLabel = new javax.swing.JLabel();
        chartMenuBtn = new javax.swing.JPanel();
        chartLabel = new javax.swing.JLabel();
        historyMenuBtn = new javax.swing.JPanel();
        historyTxtLabel = new javax.swing.JLabel();
        logoutMenuBtn = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        logoutLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        employeeIDTxt = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        dashboardPanel = new javax.swing.JPanel();
        dashLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        roundedPanel2 = new MainProgram.RoundedPanel();
        countStaff = new javax.swing.JLabel();
        staffTxt = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        roundedPanel1 = new MainProgram.RoundedPanel();
        countEquipment = new javax.swing.JLabel();
        totalEquipTxt = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        totalEquipTxt1 = new javax.swing.JLabel();
        roundedPanel3 = new MainProgram.RoundedPanel();
        countBorrowReturn = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        borrowedTxt = new javax.swing.JLabel();
        roundedPanel4 = new MainProgram.RoundedPanel();
        timeInTxt = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        countTimeIn = new javax.swing.JLabel();
        roundedPanel5 = new MainProgram.RoundedPanel();
        overdueTxt1 = new javax.swing.JLabel();
        overdueTxt = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        countOverdue = new javax.swing.JLabel();
        roundedPanel6 = new MainProgram.RoundedPanel();
        countTimeOut = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        timeOutTxt = new javax.swing.JLabel();
        staffPanel = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        StaffLabel = new javax.swing.JLabel();
        addNewStaffBtn = new MainProgram.Button();
        createStaffAccountScrollPane = new javax.swing.JScrollPane();
        staffAccountTable = new javax.swing.JTable();
        staffAccountSearch = new javax.swing.JTextField();
        equipmentPanel = new javax.swing.JPanel();
        popupBtn = new MainProgram.Button();
        jSeparator1 = new javax.swing.JSeparator();
        inventoryLabel = new javax.swing.JLabel();
        searchInventoryTxt = new javax.swing.JTextField();
        invenScrollPane = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        equipmentTypeFilter = new javax.swing.JComboBox<>();
        filterLabel1 = new javax.swing.JLabel();
        transacPanel = new javax.swing.JPanel();
        recordsLabel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        searchBorrowRecordsTxt = new javax.swing.JTextField();
        borrowScrollPane = new javax.swing.JScrollPane();
        borrowTable = new javax.swing.JTable();
        borrowFilter = new javax.swing.JComboBox<>();
        filterLabel2 = new javax.swing.JLabel();
        gymlogPanel = new javax.swing.JPanel();
        gymInOutLabel = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        gymChecInOutScrollPane = new javax.swing.JScrollPane();
        checkInOutTable = new javax.swing.JTable();
        searchCheckInOutTxt = new javax.swing.JTextField();
        chartPanel = new javax.swing.JPanel();
        gymInOutLabel1 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        equipmentChartPanel = new javax.swing.JPanel();
        overdueChartPanel = new javax.swing.JPanel();
        graphPanel = new javax.swing.JPanel();
        dateRange = new javax.swing.JComboBox<>();
        typeChart = new javax.swing.JComboBox<>();
        generateReportBtn = new MainProgram.Button();
        historyPanel = new javax.swing.JPanel();
        historyLabel = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        borrowArchivedScrollPane = new javax.swing.JScrollPane();
        borrowArchivedTable = new javax.swing.JTable();
        searchHistoryTxt = new javax.swing.JTextField();
        borrowHistoryFilter = new javax.swing.JComboBox<>();
        borrowHistoryCSVBtn = new MainProgram.Button();
        borrowHistoryPDFBtn = new MainProgram.Button();
        borrowHistoryExcelBtn = new MainProgram.Button();
        generateFileLabel = new javax.swing.JLabel();
        filterLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("adminFrame"); // NOI18N
        setSize(new java.awt.Dimension(1350, 710));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setMinimumSize(new java.awt.Dimension(1350, 710));
        mainPanel.setPreferredSize(new java.awt.Dimension(1350, 705));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerPanel.setBackground(new java.awt.Color(13, 146, 244));
        headerPanel.setPreferredSize(new java.awt.Dimension(1350, 50));

        headerLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        headerLabel.setForeground(new java.awt.Color(255, 255, 255));
        headerLabel.setText("SPORTS EQUIPMENT BORROWING SYSTEM");

        menuPanel.setBackground(new java.awt.Color(13, 146, 244));
        menuPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuPanelMouseClicked(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/menu.png"))); // NOI18N

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel7))
        );

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/account.png"))); // NOI18N

        usernameTxt.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        usernameTxt.setForeground(new java.awt.Color(240, 240, 240));
        usernameTxt.setText("Admin");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(headerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 434, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameTxt)
                .addGap(70, 70, 70))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(headerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addComponent(menuPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(usernameTxt)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 1150, 50));

        sidebarPanel.setBackground(new java.awt.Color(34, 45, 50));
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lineSeparator.setBackground(new java.awt.Color(255, 255, 255));
        lineSeparator.setForeground(new java.awt.Color(255, 255, 255));
        sidebarPanel.add(lineSeparator, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 230, 10));

        dashboardMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
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
                .addComponent(dashboardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        dashboardMenuBtnLayout.setVerticalGroup(
            dashboardMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashboardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(dashboardMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 230, -1));

        staffMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        staffMenuBtn.setForeground(new java.awt.Color(255, 255, 255));
        staffMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                staffMenuBtnMousePressed(evt);
            }
        });

        staffLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        staffLabel.setForeground(new java.awt.Color(255, 255, 255));
        staffLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/staff2.png"))); // NOI18N
        staffLabel.setText("Staff Management ");

        javax.swing.GroupLayout staffMenuBtnLayout = new javax.swing.GroupLayout(staffMenuBtn);
        staffMenuBtn.setLayout(staffMenuBtnLayout);
        staffMenuBtnLayout.setHorizontalGroup(
            staffMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(staffMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(staffLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        staffMenuBtnLayout.setVerticalGroup(
            staffMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(staffMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(staffLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(staffMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 230, -1));

        transacMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        transacMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                transacMenuBtnMousePressed(evt);
            }
        });

        transacLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        transacLabel.setForeground(new java.awt.Color(255, 255, 255));
        transacLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contract.png"))); // NOI18N
        transacLabel.setText("Borrowing Records");

        javax.swing.GroupLayout transacMenuBtnLayout = new javax.swing.GroupLayout(transacMenuBtn);
        transacMenuBtn.setLayout(transacMenuBtnLayout);
        transacMenuBtnLayout.setHorizontalGroup(
            transacMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transacMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transacLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        transacMenuBtnLayout.setVerticalGroup(
            transacMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transacMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transacLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(transacMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 230, 40));

        equipmentMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        equipmentMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                equipmentMenuBtnMousePressed(evt);
            }
        });

        equipmentLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        equipmentLabel.setForeground(new java.awt.Color(255, 255, 255));
        equipmentLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventory.png"))); // NOI18N
        equipmentLabel.setText("Manage Inventory");

        javax.swing.GroupLayout equipmentMenuBtnLayout = new javax.swing.GroupLayout(equipmentMenuBtn);
        equipmentMenuBtn.setLayout(equipmentMenuBtnLayout);
        equipmentMenuBtnLayout.setHorizontalGroup(
            equipmentMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(equipmentMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(equipmentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        equipmentMenuBtnLayout.setVerticalGroup(
            equipmentMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(equipmentMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(equipmentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(equipmentMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 230, -1));

        gymlogMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        gymlogMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                gymlogMenuBtnMousePressed(evt);
            }
        });

        gymlogLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        gymlogLabel.setForeground(new java.awt.Color(255, 255, 255));
        gymlogLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/today.png"))); // NOI18N
        gymlogLabel.setText("Gym Usage Log");

        javax.swing.GroupLayout gymlogMenuBtnLayout = new javax.swing.GroupLayout(gymlogMenuBtn);
        gymlogMenuBtn.setLayout(gymlogMenuBtnLayout);
        gymlogMenuBtnLayout.setHorizontalGroup(
            gymlogMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gymlogMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gymlogLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        gymlogMenuBtnLayout.setVerticalGroup(
            gymlogMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gymlogMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gymlogLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(gymlogMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 230, -1));

        chartMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        chartMenuBtn.setPreferredSize(new java.awt.Dimension(230, 40));
        chartMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                chartMenuBtnMousePressed(evt);
            }
        });

        chartLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        chartLabel.setForeground(new java.awt.Color(255, 255, 255));
        chartLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/leaderboard.png"))); // NOI18N
        chartLabel.setText("Charts");

        javax.swing.GroupLayout chartMenuBtnLayout = new javax.swing.GroupLayout(chartMenuBtn);
        chartMenuBtn.setLayout(chartMenuBtnLayout);
        chartMenuBtnLayout.setHorizontalGroup(
            chartMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        chartMenuBtnLayout.setVerticalGroup(
            chartMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(chartMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, -1, -1));

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
                .addComponent(historyTxtLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        historyMenuBtnLayout.setVerticalGroup(
            historyMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historyTxtLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        sidebarPanel.add(historyMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, -1, -1));

        logoutMenuBtn.setBackground(new java.awt.Color(34, 45, 50));
        logoutMenuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logoutMenuBtnMousePressed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N

        logoutLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        logoutLabel.setForeground(new java.awt.Color(255, 255, 255));
        logoutLabel.setText("Logout");

        javax.swing.GroupLayout logoutMenuBtnLayout = new javax.swing.GroupLayout(logoutMenuBtn);
        logoutMenuBtn.setLayout(logoutMenuBtnLayout);
        logoutMenuBtnLayout.setHorizontalGroup(
            logoutMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        logoutMenuBtnLayout.setVerticalGroup(
            logoutMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutMenuBtnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logoutMenuBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(logoutLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        sidebarPanel.add(logoutMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 670, 230, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user (1).png"))); // NOI18N
        sidebarPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 70, 80));

        employeeIDTxt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        employeeIDTxt.setForeground(new java.awt.Color(255, 255, 255));
        employeeIDTxt.setText("ADMIN");
        sidebarPanel.add(employeeIDTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        mainPanel.add(sidebarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 710));

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setLayout(new java.awt.CardLayout());

        dashboardPanel.setBackground(new java.awt.Color(255, 255, 255));
        dashboardPanel.setPreferredSize(new java.awt.Dimension(1150, 580));
        dashboardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dashLabel.setBackground(new java.awt.Color(255, 255, 255));
        dashLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        dashLabel.setForeground(new java.awt.Color(0, 0, 0));
        dashLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/grid.png"))); // NOI18N
        dashLabel.setText("DASHBOARD");
        dashboardPanel.add(dashLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        dashboardPanel.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(44, 68, 1040, 20));

        roundedPanel2.setBackground(new java.awt.Color(51, 255, 51));
        roundedPanel2.setForeground(new java.awt.Color(33, 33, 33));
        roundedPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countStaff.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countStaff.setForeground(new java.awt.Color(33, 33, 33));
        countStaff.setText("0");
        roundedPanel2.add(countStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        staffTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        staffTxt.setForeground(new java.awt.Color(33, 33, 33));
        staffTxt.setText("Staff Members ");
        roundedPanel2.add(staffTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/groupStaff.png"))); // NOI18N
        roundedPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        dashboardPanel.add(roundedPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 95, 320, 140));

        roundedPanel1.setBackground(new java.awt.Color(255, 204, 51));
        roundedPanel1.setForeground(new java.awt.Color(33, 33, 33));
        roundedPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countEquipment.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countEquipment.setForeground(new java.awt.Color(33, 33, 33));
        countEquipment.setText("0");
        roundedPanel1.add(countEquipment, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        totalEquipTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        totalEquipTxt.setForeground(new java.awt.Color(33, 33, 33));
        totalEquipTxt.setText("Total Equipment ");
        roundedPanel1.add(totalEquipTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 80, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventory_2.png"))); // NOI18N
        roundedPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

        totalEquipTxt1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        totalEquipTxt1.setForeground(new java.awt.Color(33, 33, 33));
        totalEquipTxt1.setText("Available");
        roundedPanel1.add(totalEquipTxt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 105, -1, -1));

        dashboardPanel.add(roundedPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 95, 340, 140));

        roundedPanel3.setBackground(new java.awt.Color(51, 204, 255));
        roundedPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countBorrowReturn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countBorrowReturn.setForeground(new java.awt.Color(33, 33, 33));
        countBorrowReturn.setText("0");
        roundedPanel3.add(countBorrowReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/borrowed.png"))); // NOI18N
        roundedPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        borrowedTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        borrowedTxt.setForeground(new java.awt.Color(33, 33, 33));
        borrowedTxt.setText("Total Transactions");
        roundedPanel3.add(borrowedTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 80, -1, -1));

        dashboardPanel.add(roundedPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 95, 320, 140));

        roundedPanel4.setBackground(new java.awt.Color(255, 153, 51));
        roundedPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        timeInTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        timeInTxt.setForeground(new java.awt.Color(33, 33, 33));
        timeInTxt.setText("Total Time-In");
        roundedPanel4.add(timeInTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 80, -1, -1));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update1.png"))); // NOI18N
        roundedPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, -1));

        countTimeIn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countTimeIn.setForeground(new java.awt.Color(33, 33, 33));
        countTimeIn.setText("0");
        roundedPanel4.add(countTimeIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        dashboardPanel.add(roundedPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 265, 340, 140));

        roundedPanel5.setBackground(new java.awt.Color(0, 204, 51));
        roundedPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        overdueTxt1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        overdueTxt1.setForeground(new java.awt.Color(33, 33, 33));
        overdueTxt1.setText("Transactions");
        roundedPanel5.add(overdueTxt1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 105, -1, -1));

        overdueTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        overdueTxt.setForeground(new java.awt.Color(33, 33, 33));
        overdueTxt.setText("Total Overdue ");
        roundedPanel5.add(overdueTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, -1, -1));

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/schedule.png"))); // NOI18N
        roundedPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        countOverdue.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countOverdue.setForeground(new java.awt.Color(33, 33, 33));
        countOverdue.setText("0");
        roundedPanel5.add(countOverdue, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        dashboardPanel.add(roundedPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 265, 320, 140));

        roundedPanel6.setBackground(new java.awt.Color(0, 153, 204));
        roundedPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        countTimeOut.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        countTimeOut.setForeground(new java.awt.Color(33, 33, 33));
        countTimeOut.setText("0");
        roundedPanel6.add(countTimeOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, 60));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/update2.png"))); // NOI18N
        roundedPanel6.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, -1));

        timeOutTxt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        timeOutTxt.setForeground(new java.awt.Color(33, 33, 33));
        timeOutTxt.setText("Total Time-Out");
        roundedPanel6.add(timeOutTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, -1, -1));

        dashboardPanel.add(roundedPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 265, 320, 140));

        contentPanel.add(dashboardPanel, "card2");

        staffPanel.setBackground(new java.awt.Color(255, 255, 255));

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        StaffLabel.setBackground(new java.awt.Color(255, 255, 255));
        StaffLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        StaffLabel.setForeground(new java.awt.Color(0, 0, 0));
        StaffLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/groupIcon.png"))); // NOI18N
        StaffLabel.setText("STAFF ACCOUNT");

        addNewStaffBtn.setBackground(new java.awt.Color(13, 110, 244));
        addNewStaffBtn.setForeground(new java.awt.Color(255, 255, 255));
        addNewStaffBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        addNewStaffBtn.setText("Add New Staff");
        addNewStaffBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addNewStaffBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                addNewStaffBtnMouseReleased(evt);
            }
        });
        addNewStaffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewStaffBtnActionPerformed(evt);
            }
        });

        staffAccountTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Employee ID", "Full Name", "Email", "Active Status", "Online Status", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        staffAccountTable.setRowHeight(40);
        createStaffAccountScrollPane.setViewportView(staffAccountTable);
        if (staffAccountTable.getColumnModel().getColumnCount() > 0) {
            staffAccountTable.getColumnModel().getColumn(0).setPreferredWidth(15);
        }

        javax.swing.GroupLayout staffPanelLayout = new javax.swing.GroupLayout(staffPanel);
        staffPanel.setLayout(staffPanelLayout);
        staffPanelLayout.setHorizontalGroup(
            staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(staffPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StaffLabel)
                    .addGroup(staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, staffPanelLayout.createSequentialGroup()
                            .addComponent(addNewStaffBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(staffAccountSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator3)
                        .addComponent(createStaffAccountScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1039, Short.MAX_VALUE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        staffPanelLayout.setVerticalGroup(
            staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(staffPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(StaffLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(staffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewStaffBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staffAccountSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(createStaffAccountScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        contentPanel.add(staffPanel, "card3");

        equipmentPanel.setBackground(new java.awt.Color(255, 255, 255));
        equipmentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        popupBtn.setBackground(new java.awt.Color(13, 110, 244));
        popupBtn.setForeground(new java.awt.Color(255, 255, 255));
        popupBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        popupBtn.setText("Add New");
        popupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                popupBtnMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                popupBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                popupBtnMouseReleased(evt);
            }
        });
        popupBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupBtnActionPerformed(evt);
            }
        });
        equipmentPanel.add(popupBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 150, 50));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        equipmentPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 1060, 10));

        inventoryLabel.setBackground(new java.awt.Color(255, 255, 255));
        inventoryLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        inventoryLabel.setForeground(new java.awt.Color(0, 0, 0));
        inventoryLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/inventoryIcon.png"))); // NOI18N
        inventoryLabel.setText("SPORTS INVENTORY");
        equipmentPanel.add(inventoryLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        searchInventoryTxt.setMargin(new java.awt.Insets(4, 10, 4, 6));
        searchInventoryTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchInventoryTxtActionPerformed(evt);
            }
        });
        equipmentPanel.add(searchInventoryTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 100, 370, 40));

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
        if (inventoryTable.getColumnModel().getColumnCount() > 0) {
            inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(10);
            inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(15);
            inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(15);
        }

        equipmentPanel.add(invenScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 1060, 450));

        equipmentTypeFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Ball Sport", "Rackets or Bats", "Protective Gear", "Nets or Goals" }));
        equipmentPanel.add(equipmentTypeFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(565, 100, 150, 40));

        filterLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        filterLabel1.setForeground(new java.awt.Color(51, 51, 51));
        filterLabel1.setText("Filter:");
        equipmentPanel.add(filterLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 100, -1, 40));

        contentPanel.add(equipmentPanel, "card4");

        transacPanel.setBackground(new java.awt.Color(255, 255, 255));
        transacPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        recordsLabel.setBackground(new java.awt.Color(255, 255, 255));
        recordsLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        recordsLabel.setForeground(new java.awt.Color(0, 0, 0));
        recordsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contract2.png"))); // NOI18N
        recordsLabel.setText("BORROWING RECORDS");
        transacPanel.add(recordsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        transacPanel.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 72, 1035, 20));

        searchBorrowRecordsTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBorrowRecordsTxtActionPerformed(evt);
            }
        });
        transacPanel.add(searchBorrowRecordsTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 98, 370, 40));

        borrowScrollPane.setMaximumSize(new java.awt.Dimension(1052, 456));
        borrowScrollPane.setMinimumSize(new java.awt.Dimension(1052, 456));
        borrowScrollPane.setPreferredSize(new java.awt.Dimension(1200, 456));

        borrowTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        borrowTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Transac. ID", "Student Name", "Equipment Name", "Quantity Borrowed", "Transaction Status", "Borrow Date", "Due Date", "Return Date", "Processed By", "Date Added"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowTable.setPreferredSize(new java.awt.Dimension(1035, 425));
        borrowTable.setRowHeight(40);
        borrowScrollPane.setViewportView(borrowTable);
        if (borrowTable.getColumnModel().getColumnCount() > 0) {
            borrowTable.getColumnModel().getColumn(0).setPreferredWidth(15);
            borrowTable.getColumnModel().getColumn(3).setPreferredWidth(15);
            borrowTable.getColumnModel().getColumn(8).setPreferredWidth(18);
        }

        transacPanel.add(borrowScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 190, 1040, 440));

        borrowFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Borrowed", "Overdue", "Returned" }));
        transacPanel.add(borrowFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 98, 140, 40));

        filterLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        filterLabel2.setForeground(new java.awt.Color(51, 51, 51));
        filterLabel2.setText("Filter:");
        transacPanel.add(filterLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 100, -1, 40));

        contentPanel.add(transacPanel, "card5");

        gymlogPanel.setBackground(new java.awt.Color(255, 255, 255));

        gymInOutLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        gymInOutLabel.setForeground(new java.awt.Color(0, 0, 0));
        gymInOutLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/today2.png"))); // NOI18N
        gymInOutLabel.setText("CHECK-IN/OUT");

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));

        checkInOutTable.setForeground(new java.awt.Color(0, 0, 0));
        checkInOutTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Instructor", "Class/Activity", "Checkin Time", "Checkout Time", "Status"
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

        javax.swing.GroupLayout gymlogPanelLayout = new javax.swing.GroupLayout(gymlogPanel);
        gymlogPanel.setLayout(gymlogPanelLayout);
        gymlogPanelLayout.setHorizontalGroup(
            gymlogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gymlogPanelLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(gymlogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gymInOutLabel)
                    .addGroup(gymlogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(gymChecInOutScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchCheckInOutTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        gymlogPanelLayout.setVerticalGroup(
            gymlogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gymlogPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(gymInOutLabel)
                .addGap(18, 18, 18)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchCheckInOutTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(gymChecInOutScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        contentPanel.add(gymlogPanel, "card6");

        chartPanel.setBackground(new java.awt.Color(255, 255, 255));
        chartPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gymInOutLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        gymInOutLabel1.setForeground(new java.awt.Color(0, 0, 0));
        gymInOutLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/leaderboard2.png"))); // NOI18N
        gymInOutLabel1.setText("CHARTS");
        chartPanel.add(gymInOutLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 386, -1));

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        chartPanel.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 1040, 20));

        equipmentChartPanel.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout equipmentChartPanelLayout = new javax.swing.GroupLayout(equipmentChartPanel);
        equipmentChartPanel.setLayout(equipmentChartPanelLayout);
        equipmentChartPanelLayout.setHorizontalGroup(
            equipmentChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );
        equipmentChartPanelLayout.setVerticalGroup(
            equipmentChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );

        chartPanel.add(equipmentChartPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 490, 290));

        overdueChartPanel.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout overdueChartPanelLayout = new javax.swing.GroupLayout(overdueChartPanel);
        overdueChartPanel.setLayout(overdueChartPanelLayout);
        overdueChartPanelLayout.setHorizontalGroup(
            overdueChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        overdueChartPanelLayout.setVerticalGroup(
            overdueChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );

        chartPanel.add(overdueChartPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 360, 440, 290));

        javax.swing.GroupLayout graphPanelLayout = new javax.swing.GroupLayout(graphPanel);
        graphPanel.setLayout(graphPanelLayout);
        graphPanelLayout.setHorizontalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
        );
        graphPanelLayout.setVerticalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        chartPanel.add(graphPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 710, 240));

        dateRange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", "Monthly" }));
        chartPanel.add(dateRange, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 90, 130, 30));

        typeChart.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Line Chart", "Bar Chart" }));
        chartPanel.add(typeChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 130, 130, 30));

        generateReportBtn.setBackground(new java.awt.Color(13, 110, 244));
        generateReportBtn.setForeground(new java.awt.Color(255, 255, 255));
        generateReportBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/description.png"))); // NOI18N
        generateReportBtn.setText("Generate Report");
        generateReportBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                generateReportBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                generateReportBtnMouseReleased(evt);
            }
        });
        generateReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportBtnActionPerformed(evt);
            }
        });
        chartPanel.add(generateReportBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 20, 190, -1));

        contentPanel.add(chartPanel, "card8");

        historyPanel.setBackground(new java.awt.Color(255, 255, 255));
        historyPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        historyLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        historyLabel.setForeground(new java.awt.Color(0, 0, 0));
        historyLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clipboardClock.png"))); // NOI18N
        historyLabel.setText("BORROW TRANSACTION HISTORY");
        historyPanel.add(historyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 13, -1, -1));

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        historyPanel.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 65, 1052, 20));

        borrowArchivedScrollPane.setPreferredSize(new java.awt.Dimension(1052, 456));
        borrowArchivedScrollPane.setViewportView(null);

        borrowArchivedTable.setBackground(new java.awt.Color(255, 255, 255));
        borrowArchivedTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Log ID", "Student Name", "Equipment Name", "Quantity Borrowed", "Status", "Action Type", "Borrowed Date", "Due Date", "Return Date", "Processed By", "Date of Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        borrowArchivedTable.setPreferredSize(new java.awt.Dimension(1290, 409));
        borrowArchivedTable.setRowHeight(40);
        borrowArchivedScrollPane.setViewportView(borrowArchivedTable);

        historyPanel.add(borrowArchivedScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 202, -1, 430));

        searchHistoryTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchHistoryTxtActionPerformed(evt);
            }
        });
        historyPanel.add(searchHistoryTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 97, 360, 42));

        borrowHistoryFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Borrowed", "Overdue", "Returned" }));
        historyPanel.add(borrowHistoryFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(574, 97, 140, 42));

        borrowHistoryCSVBtn.setBackground(new java.awt.Color(13, 110, 244));
        borrowHistoryCSVBtn.setForeground(new java.awt.Color(255, 255, 255));
        borrowHistoryCSVBtn.setText("CSV");
        borrowHistoryCSVBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                borrowHistoryCSVBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                borrowHistoryCSVBtnMouseReleased(evt);
            }
        });
        borrowHistoryCSVBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowHistoryCSVBtnActionPerformed(evt);
            }
        });
        historyPanel.add(borrowHistoryCSVBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 149, 74, -1));

        borrowHistoryPDFBtn.setBackground(new java.awt.Color(13, 110, 244));
        borrowHistoryPDFBtn.setForeground(new java.awt.Color(255, 255, 255));
        borrowHistoryPDFBtn.setText("PDF");
        borrowHistoryPDFBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                borrowHistoryPDFBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                borrowHistoryPDFBtnMouseReleased(evt);
            }
        });
        borrowHistoryPDFBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowHistoryPDFBtnActionPerformed(evt);
            }
        });
        historyPanel.add(borrowHistoryPDFBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 149, 74, -1));

        borrowHistoryExcelBtn.setBackground(new java.awt.Color(13, 110, 244));
        borrowHistoryExcelBtn.setForeground(new java.awt.Color(255, 255, 255));
        borrowHistoryExcelBtn.setText("EXCEL");
        borrowHistoryExcelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                borrowHistoryExcelBtnMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                borrowHistoryExcelBtnMouseReleased(evt);
            }
        });
        borrowHistoryExcelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowHistoryExcelBtnActionPerformed(evt);
            }
        });
        historyPanel.add(borrowHistoryExcelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 149, 74, -1));

        generateFileLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        generateFileLabel.setForeground(new java.awt.Color(51, 51, 51));
        generateFileLabel.setText("Generate File:");
        historyPanel.add(generateFileLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 145, 112, 40));

        filterLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        filterLabel.setForeground(new java.awt.Color(51, 51, 51));
        filterLabel.setText("Filter:");
        historyPanel.add(filterLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 97, 48, 40));

        contentPanel.add(historyPanel, "card7");

        mainPanel.add(contentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 1150, 660));

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void dashboardMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(dashboardPanel, true);
        utilities.setTabPanel(staffPanel, false);
        utilities.setTabPanel(equipmentPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymlogPanel, false);
        utilities.setTabPanel(chartPanel, false);
        utilities.setTabPanel(historyPanel, false);
    }//GEN-LAST:event_dashboardMenuBtnMousePressed

    private void staffMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_staffMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(staffPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(equipmentPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymlogPanel, false);
        utilities.setTabPanel(chartPanel, false);
        utilities.setTabPanel(historyPanel, false);
    }//GEN-LAST:event_staffMenuBtnMousePressed

    private void equipmentMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_equipmentMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(equipmentPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(staffPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymlogPanel, false);
        utilities.setTabPanel(chartPanel, false);
        utilities.setTabPanel(historyPanel, false);
    }//GEN-LAST:event_equipmentMenuBtnMousePressed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formMousePressed

    private void transacMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transacMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(transacPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(staffPanel, false);
        utilities.setTabPanel(equipmentPanel, false);
        utilities.setTabPanel(gymlogPanel, false);
        utilities.setTabPanel(chartPanel, false);
        utilities.setTabPanel(historyPanel, false);
    }//GEN-LAST:event_transacMenuBtnMousePressed

    private void gymlogMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gymlogMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(gymlogPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(staffPanel, false);
        utilities.setTabPanel(equipmentPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(chartPanel, false);
        utilities.setTabPanel(historyPanel, false);
    }//GEN-LAST:event_gymlogMenuBtnMousePressed

    private void logoutMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(logoutMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);

        try {
            int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                // Get adminID from the Session class
                int adminID = Session.getAdminID();  // Retrieve adminID from Session

                // Update the database to set is_active to false for the admin
                String updateSql = "UPDATE admin SET is_online = false WHERE admin_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateSql);
                updatePst.setInt(1, adminID);
                updatePst.executeUpdate();

                JOptionPane.showMessageDialog(this, "You have been logged out!");
                this.dispose();
                try {
//                    AdminLogin login = new AdminLogin();
                    Homepage login = new Homepage();
                    login.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(table.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Logout cancelled.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_logoutMenuBtnMousePressed

    private void menuPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPanelMouseClicked

    }//GEN-LAST:event_menuPanelMouseClicked

    private void popupBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupBtnActionPerformed
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
            this.setGlassPane(glassPane);
            glassPane.setVisible(true);

            AddItemPopup popup = new AddItemPopup(this, true, this);
            popup.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    glassPane.setVisible(false);
                    isDialogOpen = false;
                }
            });
            popup.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            isDialogOpen = false;
        }
    }//GEN-LAST:event_popupBtnActionPerformed

    private void popupBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_popupBtnMouseClicked

    }//GEN-LAST:event_popupBtnMouseClicked

    private void searchInventoryTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchInventoryTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchInventoryTxtActionPerformed

    private void popupBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_popupBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(popupBtn, "#0B5CC9");
    }//GEN-LAST:event_popupBtnMousePressed

    private void popupBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_popupBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(popupBtn, "#0D6EF4");
    }//GEN-LAST:event_popupBtnMouseReleased

    private void addNewStaffBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNewStaffBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(addNewStaffBtn, "#0B5CC9");
    }//GEN-LAST:event_addNewStaffBtnMousePressed

    private void addNewStaffBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNewStaffBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(addNewStaffBtn, "#0D6EF4");
    }//GEN-LAST:event_addNewStaffBtnMouseReleased

    private void addNewStaffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewStaffBtnActionPerformed
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
            this.setGlassPane(glassPane);
            glassPane.setVisible(true);

            AddNewStaffPopup popup = new AddNewStaffPopup(this, true, this);
            popup.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    glassPane.setVisible(false);
                    isDialogOpen = false;
                }
            });
            popup.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            isDialogOpen = false;
        }
    }//GEN-LAST:event_addNewStaffBtnActionPerformed

    private void historyMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historyMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(historyPanel, true);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(staffPanel, false);
        utilities.setTabPanel(equipmentPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymlogPanel, false);
        utilities.setTabPanel(chartPanel, false);

    }//GEN-LAST:event_historyMenuBtnMousePressed

    private void searchBorrowRecordsTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBorrowRecordsTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBorrowRecordsTxtActionPerformed

    private void borrowHistoryCSVBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowHistoryCSVBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(borrowHistoryCSVBtn, "#0B5CC9");
    }//GEN-LAST:event_borrowHistoryCSVBtnMousePressed

    private void borrowHistoryCSVBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowHistoryCSVBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(borrowHistoryCSVBtn, "#0D6EF4");
    }//GEN-LAST:event_borrowHistoryCSVBtnMouseReleased

    private void borrowHistoryCSVBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowHistoryCSVBtnActionPerformed
        try {
            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
            String fileName = selectedStatus.equals("All")
                    ? "Borrow_Records_Report"
                    : "Borrow_Records_" + selectedStatus + "_Report";

            CSVGenerator.generateFromTable(borrowArchivedTable, fileName, this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_borrowHistoryCSVBtnActionPerformed

    private void borrowHistoryPDFBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowHistoryPDFBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(borrowHistoryPDFBtn, "#0B5CC9");
    }//GEN-LAST:event_borrowHistoryPDFBtnMousePressed

    private void borrowHistoryPDFBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowHistoryPDFBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(borrowHistoryPDFBtn, "#0D6EF4");
    }//GEN-LAST:event_borrowHistoryPDFBtnMouseReleased

    private void borrowHistoryPDFBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowHistoryPDFBtnActionPerformed
        try {
            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
            String fileName = selectedStatus.equals("All")
                    ? "Borrow_Records_Report"
                    : "Borrow_Records_" + selectedStatus + "_Report";

            PDFGenerator.generateFromTable(borrowArchivedTable, fileName, this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }    }//GEN-LAST:event_borrowHistoryPDFBtnActionPerformed

    private void borrowHistoryExcelBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowHistoryExcelBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(borrowHistoryExcelBtn, "#0B5CC9");
    }//GEN-LAST:event_borrowHistoryExcelBtnMousePressed

    private void borrowHistoryExcelBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowHistoryExcelBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(borrowHistoryExcelBtn, "#0D6EF4");
    }//GEN-LAST:event_borrowHistoryExcelBtnMouseReleased

    private void borrowHistoryExcelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowHistoryExcelBtnActionPerformed
        try {
            String selectedStatus = (String) borrowHistoryFilter.getSelectedItem();
            String fileName = selectedStatus.equals("All")
                    ? "Borrow_Records_Report"
                    : "Borrow_Records_" + selectedStatus + "_Report";

            ExcelGenerator.generateFromTable(borrowArchivedTable, fileName, this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error generating Excel: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_borrowHistoryExcelBtnActionPerformed

    private void chartMenuBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chartMenuBtnMousePressed
        AppUtilities utilities = new AppUtilities();

        // Hover Function
        utilities.setHoverMenuBtn(chartMenuBtn);
        utilities.resetHoverMenuBtn(dashboardMenuBtn);
        utilities.resetHoverMenuBtn(staffMenuBtn);
        utilities.resetHoverMenuBtn(transacMenuBtn);
        utilities.resetHoverMenuBtn(equipmentMenuBtn);
        utilities.resetHoverMenuBtn(gymlogMenuBtn);
        utilities.resetHoverMenuBtn(historyMenuBtn);
        utilities.resetHoverMenuBtn(logoutMenuBtn);

        // Set visibility for panels
        utilities.setTabPanel(chartPanel, true);
        utilities.setTabPanel(historyPanel, false);
        utilities.setTabPanel(dashboardPanel, false);
        utilities.setTabPanel(staffPanel, false);
        utilities.setTabPanel(equipmentPanel, false);
        utilities.setTabPanel(transacPanel, false);
        utilities.setTabPanel(gymlogPanel, false);
    }//GEN-LAST:event_chartMenuBtnMousePressed

    private void generateReportBtnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generateReportBtnMousePressed
        AppUtilities utilities = new AppUtilities();
        utilities.setHoverBtn(generateReportBtn, "#0B5CC9");
    }//GEN-LAST:event_generateReportBtnMousePressed

    private void generateReportBtnMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generateReportBtnMouseReleased
        AppUtilities utilities = new AppUtilities();
        utilities.resetHoverBtn(generateReportBtn, "#0D6EF4");
    }//GEN-LAST:event_generateReportBtnMouseReleased

    private void generateReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportBtnActionPerformed
        ChartPDFExporter.exportCharts(graphPanel, equipmentChartPanel, overdueChartPanel, this);
    }//GEN-LAST:event_generateReportBtnActionPerformed

    private void searchHistoryTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchHistoryTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchHistoryTxtActionPerformed

    //Main Method
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
//                    new AdminDashboard().setVisible(true);
                    new Homepage().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(AdminLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel StaffLabel;
    private MainProgram.Button addNewStaffBtn;
    private javax.swing.JScrollPane borrowArchivedScrollPane;
    private javax.swing.JTable borrowArchivedTable;
    private javax.swing.JComboBox<String> borrowFilter;
    private MainProgram.Button borrowHistoryCSVBtn;
    private MainProgram.Button borrowHistoryExcelBtn;
    private javax.swing.JComboBox<String> borrowHistoryFilter;
    private MainProgram.Button borrowHistoryPDFBtn;
    private javax.swing.JScrollPane borrowScrollPane;
    private javax.swing.JTable borrowTable;
    private javax.swing.JLabel borrowedTxt;
    private javax.swing.JLabel chartLabel;
    private javax.swing.JPanel chartMenuBtn;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JTable checkInOutTable;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel countBorrowReturn;
    private javax.swing.JLabel countEquipment;
    private javax.swing.JLabel countOverdue;
    private javax.swing.JLabel countStaff;
    private javax.swing.JLabel countTimeIn;
    private javax.swing.JLabel countTimeOut;
    private javax.swing.JScrollPane createStaffAccountScrollPane;
    private javax.swing.JLabel dashLabel;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel dashboardMenuBtn;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JComboBox<String> dateRange;
    private javax.swing.JLabel employeeIDTxt;
    private javax.swing.JPanel equipmentChartPanel;
    private javax.swing.JLabel equipmentLabel;
    private javax.swing.JPanel equipmentMenuBtn;
    private javax.swing.JPanel equipmentPanel;
    private javax.swing.JComboBox<String> equipmentTypeFilter;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JLabel filterLabel1;
    private javax.swing.JLabel filterLabel2;
    private javax.swing.JLabel generateFileLabel;
    private MainProgram.Button generateReportBtn;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JScrollPane gymChecInOutScrollPane;
    private javax.swing.JLabel gymInOutLabel;
    private javax.swing.JLabel gymInOutLabel1;
    private javax.swing.JLabel gymlogLabel;
    private javax.swing.JPanel gymlogMenuBtn;
    private javax.swing.JPanel gymlogPanel;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel historyLabel;
    private javax.swing.JPanel historyMenuBtn;
    private javax.swing.JPanel historyPanel;
    private javax.swing.JLabel historyTxtLabel;
    private javax.swing.JScrollPane invenScrollPane;
    private javax.swing.JLabel inventoryLabel;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator lineSeparator;
    private javax.swing.JLabel logoutLabel;
    private javax.swing.JPanel logoutMenuBtn;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JPanel overdueChartPanel;
    private javax.swing.JLabel overdueTxt;
    private javax.swing.JLabel overdueTxt1;
    private MainProgram.Button popupBtn;
    private javax.swing.JLabel recordsLabel;
    private MainProgram.RoundedPanel roundedPanel1;
    private MainProgram.RoundedPanel roundedPanel2;
    private MainProgram.RoundedPanel roundedPanel3;
    private MainProgram.RoundedPanel roundedPanel4;
    private MainProgram.RoundedPanel roundedPanel5;
    private MainProgram.RoundedPanel roundedPanel6;
    private javax.swing.JTextField searchBorrowRecordsTxt;
    private javax.swing.JTextField searchCheckInOutTxt;
    private javax.swing.JTextField searchHistoryTxt;
    private javax.swing.JTextField searchInventoryTxt;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JTextField staffAccountSearch;
    private javax.swing.JTable staffAccountTable;
    private javax.swing.JLabel staffLabel;
    private javax.swing.JPanel staffMenuBtn;
    private javax.swing.JPanel staffPanel;
    private javax.swing.JLabel staffTxt;
    private javax.swing.JLabel timeInTxt;
    private javax.swing.JLabel timeOutTxt;
    private javax.swing.JLabel totalEquipTxt;
    private javax.swing.JLabel totalEquipTxt1;
    private javax.swing.JLabel transacLabel;
    private javax.swing.JPanel transacMenuBtn;
    private javax.swing.JPanel transacPanel;
    private javax.swing.JComboBox<String> typeChart;
    private javax.swing.JLabel usernameTxt;
    // End of variables declaration//GEN-END:variables
}
