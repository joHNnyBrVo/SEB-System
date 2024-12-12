package MainProgram;

import Database.dbConnection;
import PopupModal.AddItemPopup;
import ReportGenerator.ChartPDFExporter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import java.awt.BasicStroke;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.ui.TextAnchor;

public class test extends javax.swing.JFrame {

    private javax.swing.JComboBox<String> timeFrameComboBox;
    private javax.swing.JComboBox<String> chartTypeComboBox;
    private Connection conn;

    public test() {
        initComponents();
        startPeriodicUpdate();

        //DB Connection
        dbConnection db = new dbConnection();

        try {
            conn = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddItemPopup.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Add action listeners to existing combo boxes
        dateRange.addActionListener(e -> createTransactionTimeChart());
        typeChart.addActionListener(e -> createTransactionTimeChart());

        createTransactionTimeChart();
        createOverdueChart();
        createEquipmentUsageChart();
    }

    private void createTransactionTimeChart() {
        String timeFrame = (String) dateRange.getSelectedItem();
        String chartType = (String) typeChart.getSelectedItem();

        String query;
        if ("Weekly".equals(timeFrame)) {
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
        } else {
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
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");

            while (rs.next()) {
                String date = dateFormat.format(rs.getDate("Weekly".equals(timeFrame) ? "week_start" : "date"));
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

            // Increase chart size
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(500, 300)); // Increased from 394, 237

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

        // Basic styling
        renderer.setSeriesPaint(0, new Color(0, 123, 255));  // Blue for Returned
        renderer.setSeriesPaint(1, new Color(40, 167, 69));  // Green for Borrowed
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));

        // Make data points visible
        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultShapesFilled(true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));

        // Configure value labels
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_CENTER)
        );

        plot.setRenderer(renderer);

        // Configure range axis (y-axis)
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.15);  // Add space above the highest point
        rangeAxis.setLowerMargin(0.15);  // Add space below the lowest point

        // Configure domain axis (x-axis)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.4);  // Space between categories
        domainAxis.setLowerMargin(0.05);    // Space on left
        domainAxis.setUpperMargin(0.05);    // Space on right

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

        // Colors for bars
        renderer.setSeriesPaint(0, new Color(0, 123, 255));  // Blue for Borrowed
        renderer.setSeriesPaint(1, new Color(40, 167, 69));  // Green for Returned
        renderer.setBarPainter(new StandardBarPainter());

        // Configure bars
        renderer.setItemMargin(0.0);  // Space between bars in same category
        plot.setRenderer(renderer);

        // Configure value labels
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultPositiveItemLabelPosition(
                new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER)
        );

        // Configure domain axis (x-axis)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.4);  // Increase space between date groups
        domainAxis.setLowerMargin(0.05);
        domainAxis.setUpperMargin(0.05);

        // Configure range axis (y-axis)
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        rangeAxis.setUpperMargin(0.15);  // Add space above bars for labels

        // Set maximum bar width
        renderer.setMaximumBarWidth(0.1);  // Adjust this value as needed

        return chart;
    }

    // Assume createOverdueChart() and createEquipmentUsageChart() are defined elsewhere
    ///////////////////////////////////
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
            chartPanel.setPreferredSize(new Dimension(360, 270));

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
            chartPanel.setPreferredSize(new Dimension(360, 270));

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
                            // Save the current state of the chart
                            int selectedDateRangeIndex = dateRange.getSelectedIndex();
                            int selectedChartTypeIndex = typeChart.getSelectedIndex();

                            // Update the charts
                            createTransactionTimeChart();
                            createEquipmentUsageChart();
                            createOverdueChart();

                            // Restore the previous state of the chart
                            dateRange.setSelectedIndex(selectedDateRangeIndex);
                            typeChart.setSelectedIndex(selectedChartTypeIndex);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        graphPanel = new javax.swing.JPanel();
        equipmentChartPanel = new javax.swing.JPanel();
        overdueChartPanel = new javax.swing.JPanel();
        overdueChartPanel1 = new javax.swing.JPanel();
        typeChart = new javax.swing.JComboBox<>();
        dateRange = new javax.swing.JComboBox<>();
        reportBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1350, 710));
        setSize(new java.awt.Dimension(1350, 710));

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout graphPanelLayout = new javax.swing.GroupLayout(graphPanel);
        graphPanel.setLayout(graphPanelLayout);
        graphPanelLayout.setHorizontalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        graphPanelLayout.setVerticalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        mainPanel.add(graphPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 21, 394, -1));

        javax.swing.GroupLayout equipmentChartPanelLayout = new javax.swing.GroupLayout(equipmentChartPanel);
        equipmentChartPanel.setLayout(equipmentChartPanelLayout);
        equipmentChartPanelLayout.setHorizontalGroup(
            equipmentChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 410, Short.MAX_VALUE)
        );
        equipmentChartPanelLayout.setVerticalGroup(
            equipmentChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );

        mainPanel.add(equipmentChartPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 410, -1));

        javax.swing.GroupLayout overdueChartPanelLayout = new javax.swing.GroupLayout(overdueChartPanel);
        overdueChartPanel.setLayout(overdueChartPanelLayout);
        overdueChartPanelLayout.setHorizontalGroup(
            overdueChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        overdueChartPanelLayout.setVerticalGroup(
            overdueChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        mainPanel.add(overdueChartPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, -1, 237));

        javax.swing.GroupLayout overdueChartPanel1Layout = new javax.swing.GroupLayout(overdueChartPanel1);
        overdueChartPanel1.setLayout(overdueChartPanel1Layout);
        overdueChartPanel1Layout.setHorizontalGroup(
            overdueChartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        overdueChartPanel1Layout.setVerticalGroup(
            overdueChartPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        mainPanel.add(overdueChartPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 320, -1, 237));

        typeChart.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Line Chart", "Bar Chart" }));
        mainPanel.add(typeChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 90, 130, -1));

        dateRange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Daily", "Weekly", " ", " " }));
        mainPanel.add(dateRange, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 50, 130, -1));

        reportBtn.setText("Generate Report");
        reportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBtnActionPerformed(evt);
            }
        });
        mainPanel.add(reportBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 160, 140, -1));

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

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtnActionPerformed
        ChartPDFExporter.exportCharts(graphPanel, equipmentChartPanel, overdueChartPanel, this);
    }//GEN-LAST:event_reportBtnActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new test().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> dateRange;
    private javax.swing.JPanel equipmentChartPanel;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel overdueChartPanel;
    private javax.swing.JPanel overdueChartPanel1;
    private javax.swing.JButton reportBtn;
    private javax.swing.JComboBox<String> typeChart;
    // End of variables declaration//GEN-END:variables
}
