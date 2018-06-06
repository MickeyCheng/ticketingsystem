
package forms;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;



public class fmrTicketing extends javax.swing.JFrame {
ResultSet rs;
PreparedStatement pstmt;
Connection conn;
Date getTime;
Object getLink;
boolean add,edit;
int getMaxTicket,getMaxTixSequence;
public static String getType,getCategory;
String getAssigned,getNameAssigned,getAssignee,getEmail;
LocalDate today = LocalDate.now();
SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
String getDateFrom, getDateTo, getImagePath;
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public fmrTicketing() {
        initComponents();
        doConnect();
        disableTexts();
        clearTexts();
        fillComboClient();
        fillComboClientSearch();
        fillComboStatus();
        fillComboCategory();
        fillAssignedTo();
        fillType();
        tblTicket.setAutoCreateRowSorter(true);
        setLocationRelativeTo(null);
        fillTypeCategory();
        showTicketTable();
        sortTableTicket();
        sortTableSequence();
        fillPriority();
        txtUpdateTix.setLineWrap(true);
        txtUpdateTix.setWrapStyleWord(true);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        checkAdminLogged();
        dateTicket.setVisible(false);
//        cmbStatus.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if (cmbStatus.getSelectedItem().toString().equals("RESOLVED")){
//                    lblResolveStatus.setVisible(true);
//                    cmbResolveStatus.setVisible(true);
//                }else{
//                    lblResolveStatus.setVisible(false);
//                    cmbResolveStatus.setVisible(false);
//                }
//            }
//        });
    }
    private void getUserEmail(){
        try
        {
            pstmt = conn.prepareStatement("Select uc_email from tblUsercontrol where uc_username =?");
            pstmt.setString(1, cmbAssignedTo.getSelectedItem().toString());
            rs = pstmt.executeQuery();
            if (rs.next())
            {
                getEmail = rs.getString("uc_email");
            }
            pstmt.close();
        }catch(SQLException e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void checkAdminLogged(){
     if (frmLogin.isAdmin.equals("N")){
            mnuUserMaster.setEnabled(false);
            mnuProducts.setEnabled(false);
            mnuClientQandI.setEnabled(false);
            mnuQuoteGen.setEnabled(false);
            mnuInvoiceGen.setEnabled(false);
            mnuTasks.setEnabled(false);
            mnuReportsInvoiceReports.setEnabled(false);
            mnuReportsQuotation.setEnabled(false);
            mnuReportsReportMenu.setEnabled(false);
            mnuStaffPosition.setEnabled(false);
            mnuReportsReceiptReports.setEnabled(false);
            mnuModifyLogs.setEnabled(false);
            mnuTKReport.setEnabled(false);
        }
        if(frmLogin.username.equalsIgnoreCase("marlon ricafort")){
            mnuMoneyTracker.setEnabled(true);
            mnuMoneyTrackerReports.setEnabled(true);
        }else{
            mnuMoneyTracker.setEnabled(false);
            mnuMoneyTrackerReports.setEnabled(false);
        }
    }
    private void sortTableSequence(){
        tblSequence.getColumnModel().getColumn(0).setHeaderValue("ID");
        tblSequence.getColumnModel().getColumn(1).setHeaderValue("DATE");
        tblSequence.getColumnModel().getColumn(2).setHeaderValue("TIME");
        tblSequence.getColumnModel().getColumn(3).setHeaderValue("USER");
        tblSequence.getColumnModel().getColumn(4).setHeaderValue("NOTES");
    }
    private void sortTableTicket(){
        tblTicket.getColumnModel().getColumn(0).setHeaderValue("ID");
        tblTicket.getColumnModel().getColumn(1).setHeaderValue("CLIENT");
        tblTicket.getColumnModel().getColumn(2).setHeaderValue("TICKET DATE");        
        tblTicket.getColumnModel().getColumn(3).setHeaderValue("TYPE");
        tblTicket.getColumnModel().getColumn(4).setHeaderValue("DESCRIPTION");
        tblTicket.getColumnModel().getColumn(5).setHeaderValue("ASSIGNED");
        tblTicket.getColumnModel().getColumn(6).setHeaderValue("TYPE");
    }
    
    private void fillTablePending(){
    String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_status=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"PENDING");
                pstmt.setString(4,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
                sortTableTicket();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
    }
//    private void fillResolveStatus(){
//        cmbResolveStatus.removeAllItems();
//        cmbResolveStatus.addItem("AWAITING VENDOR");
//        cmbResolveStatus.addItem("AWAITING USER");
//        cmbResolveStatus.setSelectedIndex(-1);
//    }
    private void fillPriority(){
        cmbPriority.removeAllItems();
        cmbPriority.addItem("LOW");
        cmbPriority.addItem("MEDIUM");
        cmbPriority.addItem("HIGH");
        cmbPriority.addItem("CRITICAL");
        cmbPriority.setSelectedIndex(-1);
    }
    private void fillTypeCategory(){
        cmbTypeCategory.removeAllItems();
        cmbTypeCategory.addItem("ALL");
        cmbTypeCategory.addItem("ISSUE - TICKET");
        cmbTypeCategory.addItem("CHANGE REQUEST");
    }
    private void fillType(){
        cmbType.removeAllItems();
        cmbType.addItem("");
        cmbType.addItem("ISSUE - TICKET");
        cmbType.addItem("CHANGE REQUEST");
        cmbType.setSelectedIndex(-1);
    }
    private void fillAssignedTo(){
        cmbAssignedTo.removeAllItems();
        try{
            pstmt = conn.prepareStatement("SELECT uc_username from tblUserControl order by uc_username");
            rs = pstmt.executeQuery();
            while(rs.next()){
                cmbAssignedTo.addItem(rs.getString("uc_username"));
            }
            pstmt.close();
            cmbAssignedTo.setSelectedIndex(-1);
        }catch(SQLException e){
            e.getMessage();
        }   
    }
    private void fillComboCategory(){
        cmbCategory.removeAllItems();
        try{
            pstmt = conn.prepareStatement("Select * from tblticketcategory order by tc_id");
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbCategory.addItem(rs.getString("tc_name"));
            }
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();
        }
        
//        cmbCategory.addItem("");
//        cmbCategory.addItem("SYSTEM ISSUE");
//        cmbCategory.addItem("OPERATING SYSTEM");
//        cmbCategory.addItem("HARDWARE");
//        cmbCategory.addItem("SOFTWARE");
//        cmbCategory.addItem("NETWORKING");
//        cmbCategory.addItem("STATIONARY");
//        cmbCategory.addItem("OTHER");
        cmbCategory.setSelectedIndex(-1);
    }
    private void fillComboStatus(){
        if (frmLogin.isAdmin.equals("Y") || frmLogin.username.equals(getAssignee)){
            cmbStatus.removeAllItems();
            cmbStatus.addItem("");
            cmbStatus.addItem("PENDING");
            cmbStatus.addItem("RESOLVED");
            cmbStatus.addItem("CLOSED");
            cmbStatus.setSelectedIndex(-1);
        }else{
            cmbStatus.removeAllItems();
            cmbStatus.addItem("");
            cmbStatus.addItem("PENDING");
            cmbStatus.addItem("RESOLVED");
            cmbStatus.setSelectedIndex(-1);
        }
    }
    private void fillComboClientSearch()
    {
     cmbClientSearch.removeAllItems();
        try{
            pstmt = conn.prepareStatement("select cd_name from tblClientDetails order by cd_name");
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbClientSearch.addItem(rs.getString("cd_name"));
            }
            cmbClientSearch.setSelectedIndex(-1);
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void fillComboClient(){
        cmbClient.removeAllItems();
        try{
            pstmt = conn.prepareStatement("select cd_name from tblClientDetails order by cd_name");
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbClient.addItem(rs.getString("cd_name"));
            }
            cmbClient.setSelectedIndex(-1);
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void enableTexts(){
        txtDescription.setEditable(true);
        cmbAssignedTo.setEnabled(true);
        cmbCategory.setEnabled(true);
        cmbClient.setEnabled(true);
        cmbStatus.setEnabled(true);
        cmbType.setEnabled(true);
        cmbPriority.setEnabled(true);
//        cmbResolveStatus.setEnabled(true);
    }
    private void disableTexts(){
        txtDescription.setEditable(false);
        cmbAssignedTo.setEnabled(false);
        cmbCategory.setEnabled(false);
        cmbClient.setEnabled(false);
        cmbStatus.setEnabled(false);
        cmbType.setEnabled(false);
        cmbPriority.setEnabled(false);
//        cmbResolveStatus.setEnabled(false);
        cmbAssignedTo.setSelectedIndex(-1);
        cmbCategory.setSelectedIndex(-1);
        cmbClient.setSelectedIndex(-1);
        cmbPriority.setSelectedIndex(-1);
        cmbStatus.setSelectedIndex(-1);
        cmbType.setSelectedIndex(-1);
        cmbTypeCategory.setSelectedIndex(-1);
    }
    private void clearTexts(){
        txtDescription.setText("");
        txtAssignee.setText("");
        cmbAssignedTo.setSelectedIndex(-1);
        cmbCategory.setSelectedIndex(-1);
        cmbClient.setSelectedIndex(-1);
        cmbStatus.setSelectedIndex(-1);
        cmbType.setSelectedIndex(-1);
        lblID.setText("");
        cmbPriority.setSelectedIndex(-1);
    }   
    private void getNextTicketID(){
        try{
            pstmt = conn.prepareStatement("SELECT ti_id from tblTicket order by ti_id DESC LIMIT 1");
            rs = pstmt.executeQuery();
            if (rs.next()){
                getMaxTicket = rs.getInt("ti_id");
                getMaxTicket++;
            }else{
                getMaxTicket=1;
            }
            lblID.setText(String.valueOf(getMaxTicket));
        }catch(SQLException e){
            e.getMessage();
        }
    }
    public void doConnect(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
//        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
//        conn = DriverManager.getConnection("jdbc:mysql://192.168.1.13/dbticketing","iSoft","iSoft123");
        conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
    }catch(SQLException | ClassNotFoundException e){
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        db = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JButton();
        dateTicket = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTicket = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        dateTo = new com.toedter.calendar.JDateChooser();
        btnShow = new javax.swing.JButton();
        cmbTypeCategory = new javax.swing.JComboBox<>();
        cmbClientSearch = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblSequence = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtUpdateTix = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        txtAssignee = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cmbClient = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cmbAssignedTo = new javax.swing.JComboBox<>();
        lblID = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox<>();
        cmbPriority = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        lblAttachment = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuLogout = new javax.swing.JMenuItem();
        mnuExit = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnuUserMaster = new javax.swing.JMenuItem();
        mnuClientMaster = new javax.swing.JMenuItem();
        mnuProducts = new javax.swing.JMenuItem();
        mnuClientQandI = new javax.swing.JMenuItem();
        mnuStaffPosition = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        mnuDepartment = new javax.swing.JMenuItem();
        menu4 = new javax.swing.JMenu();
        mnuChangePassword = new javax.swing.JMenuItem();
        mnuDashboard = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        mnuTixPending = new javax.swing.JMenuItem();
        mnuTixResolve = new javax.swing.JMenuItem();
        mnuTixClose = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuPrintSelect = new javax.swing.JMenuItem();
        mnuReportsReportMenu = new javax.swing.JMenuItem();
        mnuReportsQuotation = new javax.swing.JMenuItem();
        mnuReportsInvoiceReports = new javax.swing.JMenuItem();
        mnuReportsReceiptReports = new javax.swing.JMenuItem();
        mnuMoneyTrackerReports = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        mnuQuoteGen = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        mnuInvoiceGen = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        mnuTasks = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        mnuMoneyTracker = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        mnuModifyLogs = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        mnuTKReport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDashboard.setText("DASHBOARD");
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });
        jPanel6.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 160, 40));

        dateTicket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateTicketMouseReleased(evt);
            }
        });
        jPanel6.add(dateTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 230, 30));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1320, 60));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblTicket.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTicket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTicketMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblTicket);

        jPanel7.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 120, 460, 290));

        jPanel8.setBackground(new java.awt.Color(153, 153, 153));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateFromMouseReleased(evt);
            }
        });
        jPanel8.add(dateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 140, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FROM:");
        jPanel8.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 60, 20));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("TO:");
        jPanel8.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 50, 20));

        dateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateToMouseReleased(evt);
            }
        });
        jPanel8.add(dateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 140, 30));

        btnShow.setText("SHOW");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });
        jPanel8.add(btnShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 70, 70));

        cmbTypeCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel8.add(cmbTypeCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 150, 30));

        cmbClientSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbClientSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClientSearchActionPerformed(evt);
            }
        });
        jPanel8.add(cmbClientSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 150, 30));

        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 460, 90));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 90, 480, 420));

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblSequence.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tblSequence.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSequenceMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblSequence);

        jPanel9.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 860, 120));

        txtUpdateTix.setColumns(20);
        txtUpdateTix.setRows(5);
        jScrollPane5.setViewportView(txtUpdateTix);

        jPanel9.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 10, 310, 120));

        jButton1.setText("UPDATE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 10, 100, 90));

        jButton3.setText("ATTACH");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 110, 100, -1));

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 1320, 140));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAdd.setText("ADD");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel5.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 90, 50));

        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel5.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 10, 90, 50));

        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel5.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 90, 50));

        btnSave.setText("SAVE");
        btnSave.setPreferredSize(new java.awt.Dimension(55, 23));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel5.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 85, 50));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 150, 250, 140));

        txtAssignee.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        txtAssignee.setText("TICKET ID:");
        jPanel4.add(txtAssignee, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 230, 30));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel3.setText("TICKET ID:");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 30));

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel4.setText("RAISED BY:");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 130, 30));

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel5.setText("TICKET DESCRIPTION:");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 180, 30));

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 250, 80));

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel7.setText("CLIENT:");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 30));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel8.setText("CATEGORY:");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 130, 30));

        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(cmbCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 230, 30));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel9.setText("PRIORITY:");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 130, 30));

        cmbClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(cmbClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 230, 30));

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel10.setText("ASSIGNED TO:");
        jPanel4.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 130, 30));

        cmbAssignedTo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(cmbAssignedTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 230, 30));

        lblID.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        lblID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblID.setText("TICKET ID:");
        jPanel4.add(lblID, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 80, 30));

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 230, 30));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel11.setText("TYPE:");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 130, 30));

        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(cmbType, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 230, 30));

        cmbPriority.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(cmbPriority, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 230, 30));

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Demi Cond", 0, 18)); // NOI18N
        jLabel12.setText("STATUS:");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 130, 30));

        jButton2.setText("ADD ATTACHMENT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 300, 250, 60));

        lblAttachment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAttachment.setText("IMAGE GOES HERE");
        lblAttachment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAttachmentMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblAttachmentMouseEntered(evt);
            }
        });
        jPanel4.add(lblAttachment, new org.netbeans.lib.awtextra.AbsoluteConstraints(654, 50, 150, 310));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 810, 400));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 830, 420));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1350, 670));

        jMenu1.setText("File");

        mnuLogout.setText("Log Out");
        mnuLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogoutActionPerformed(evt);
            }
        });
        jMenu1.add(mnuLogout);

        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        jMenu1.add(mnuExit);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Masters");

        mnuUserMaster.setText("User Master");
        mnuUserMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUserMasterActionPerformed(evt);
            }
        });
        jMenu3.add(mnuUserMaster);

        mnuClientMaster.setText("Client Master");
        mnuClientMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuClientMasterActionPerformed(evt);
            }
        });
        jMenu3.add(mnuClientMaster);

        mnuProducts.setText("Products/Services Master");
        mnuProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProductsActionPerformed(evt);
            }
        });
        jMenu3.add(mnuProducts);

        mnuClientQandI.setText("Client Q and I Master");
        mnuClientQandI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuClientQandIActionPerformed(evt);
            }
        });
        jMenu3.add(mnuClientQandI);

        mnuStaffPosition.setText("Staff Position Master");
        mnuStaffPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuStaffPositionActionPerformed(evt);
            }
        });
        jMenu3.add(mnuStaffPosition);

        jMenuItem5.setText("Ticket Category Master");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        mnuDepartment.setText("Department Master");
        mnuDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDepartmentActionPerformed(evt);
            }
        });
        jMenu3.add(mnuDepartment);

        jMenuBar1.add(jMenu3);

        menu4.setText("Tools");

        mnuChangePassword.setText("Change Password");
        mnuChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChangePasswordActionPerformed(evt);
            }
        });
        menu4.add(mnuChangePassword);

        mnuDashboard.setText("Dashboard");
        mnuDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDashboardActionPerformed(evt);
            }
        });
        menu4.add(mnuDashboard);

        jMenuBar1.add(menu4);

        jMenu5.setText("Tickets");

        mnuTixPending.setText("View Pending Tasks");
        mnuTixPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTixPendingActionPerformed(evt);
            }
        });
        jMenu5.add(mnuTixPending);

        mnuTixResolve.setText("View  Resolved Tasks");
        mnuTixResolve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTixResolveActionPerformed(evt);
            }
        });
        jMenu5.add(mnuTixResolve);

        mnuTixClose.setText("View Closed Tasks");
        mnuTixClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTixCloseActionPerformed(evt);
            }
        });
        jMenu5.add(mnuTixClose);

        jMenuBar1.add(jMenu5);

        jMenu2.setText("Reports");

        mnuPrintSelect.setText("Print Selected Item");
        mnuPrintSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPrintSelectActionPerformed(evt);
            }
        });
        jMenu2.add(mnuPrintSelect);

        mnuReportsReportMenu.setText("Report Menu");
        mnuReportsReportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReportsReportMenuActionPerformed(evt);
            }
        });
        jMenu2.add(mnuReportsReportMenu);

        mnuReportsQuotation.setText("Quotation Reports");
        mnuReportsQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReportsQuotationActionPerformed(evt);
            }
        });
        jMenu2.add(mnuReportsQuotation);

        mnuReportsInvoiceReports.setText("Invoice Reports");
        mnuReportsInvoiceReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReportsInvoiceReportsActionPerformed(evt);
            }
        });
        jMenu2.add(mnuReportsInvoiceReports);

        mnuReportsReceiptReports.setText("Receipt Reports");
        mnuReportsReceiptReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReportsReceiptReportsActionPerformed(evt);
            }
        });
        jMenu2.add(mnuReportsReceiptReports);

        mnuMoneyTrackerReports.setText("Money Tracker  Reports");
        mnuMoneyTrackerReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMoneyTrackerReportsActionPerformed(evt);
            }
        });
        jMenu2.add(mnuMoneyTrackerReports);

        jMenuItem23.setText("Time Keeping Report");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem23);

        jMenuItem25.setText("Client Tasks Report");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem25);

        jMenuBar1.add(jMenu2);

        mnuQuoteGen.setText("Accounts");

        jMenuItem2.setText("Quote Generator");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuQuoteGen.add(jMenuItem2);

        mnuInvoiceGen.setText("Invoice Generator");
        mnuInvoiceGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuInvoiceGenActionPerformed(evt);
            }
        });
        mnuQuoteGen.add(mnuInvoiceGen);

        jMenuItem20.setText("Receipt Generator");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        mnuQuoteGen.add(jMenuItem20);

        jMenuItem21.setText("LPO Generator");
        mnuQuoteGen.add(jMenuItem21);

        jMenuBar1.add(mnuQuoteGen);

        mnuTasks.setText("Tasks Viewer");

        jMenu10.setText("Unassigned Tickets");

        jMenuItem11.setText("View Unassigned Tickets");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem11);

        jMenuItem12.setText("Print Unassigned Tickets");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem12);

        mnuTasks.add(jMenu10);

        jMenu11.setText("Pending Tickets");

        jMenuItem13.setText("View Pending Tickets");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem13);

        jMenuItem14.setText("Print Pending Tickets");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem14);

        mnuTasks.add(jMenu11);

        jMenu12.setText("Resolved Tickets");

        jMenuItem16.setText("View Resolved Tickets");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem16);

        jMenuItem17.setText("Print Resolved Tickets");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem17);

        mnuTasks.add(jMenu12);

        jMenu13.setText("Closed Tickets");

        jMenuItem18.setText("View Closed Tickets");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem18);

        jMenuItem19.setText("Print Closed Tickets");
        jMenu13.add(jMenuItem19);

        mnuTasks.add(jMenu13);

        jMenu7.setText("Unassigned CRs");

        jMenuItem7.setText("View Unasssigned CRs");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem7);

        jMenuItem8.setText("Print Unassigned CRs");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem8);

        mnuTasks.add(jMenu7);

        jMenu8.setText("Pending CRs");

        jMenuItem1.setText("View Pending CRs");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem1);

        jMenuItem3.setText("Print Pending CRs");
        jMenu8.add(jMenuItem3);

        mnuTasks.add(jMenu8);

        jMenu14.setText("Resolved CRs");

        jMenuItem4.setText("View Resolved CRs");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem4);

        jMenuItem9.setText("Print Resolved CRs");
        jMenu14.add(jMenuItem9);

        mnuTasks.add(jMenu14);

        jMenu15.setText("Closed CRs");

        jMenuItem10.setText("View Closed CRs");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem10);

        jMenuItem15.setText("Print Closed CRs");
        jMenu15.add(jMenuItem15);

        mnuTasks.add(jMenu15);

        jMenuBar1.add(mnuTasks);

        mnuMoneyTracker.setText("Money Tracker");

        jMenuItem6.setText("Income/Expense");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        mnuMoneyTracker.add(jMenuItem6);

        jMenuBar1.add(mnuMoneyTracker);

        jMenu4.setText("HR");

        jMenuItem22.setText("Attendance");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem22);

        jMenuItem26.setText("Lunch In/Out");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem26);

        mnuModifyLogs.setText("Modify Logs");
        mnuModifyLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuModifyLogsActionPerformed(evt);
            }
        });
        jMenu4.add(mnuModifyLogs);

        jMenuItem24.setText("My Logs");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem24);

        mnuTKReport.setText("TK Report");
        mnuTKReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTKReportActionPerformed(evt);
            }
        });
        jMenu4.add(mnuTKReport);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateTicketMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateTicketMouseReleased

    }//GEN-LAST:event_dateTicketMouseReleased

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        enableTexts();
        add=true;
        edit=false;
        clearTexts();
        btnEdit.setEnabled(false);
        btnAdd.setEnabled(false);
        getNextTicketID();
        txtAssignee.setText(frmLogin.username);
        dateTicket.setDate(new Date());
        dateTicket.setEnabled(false);
    }//GEN-LAST:event_btnAddActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("isoftdevelopmentteam@gmail.com","iSoft123");
				}
			});
//        final String username = "isoftdevelopmentteam@gmail.com";
//        final String password = "iSoft123";
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props,
//          new javax.mail.Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                }
//          });
        getUserEmail();
        if(cmbStatus.getSelectedIndex() == -1 || cmbCategory.getSelectedIndex() == -1 || cmbClient.getSelectedIndex() == -1 
                 || dateTicket.getDate()==null || cmbType.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(this, "PLEASE COMPLETE ALL TICKET DETAILS","TICKET",JOptionPane.WARNING_MESSAGE);
        }else{
            if(add==true && edit==false){
                try{
                    String insertTicket = "INSERT INTO tblTicket (ti_id,ti_client,ti_dateRaised,ti_description,"
                            + "ti_status,ti_category,ti_assignee,ti_assigned,ti_type,ti_time,ti_priority,ti_attachment)"
                            + "values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstmt = conn.prepareStatement(insertTicket);
                    pstmt.setInt(1, getMaxTicket);
                    pstmt.setString(2, cmbClient.getSelectedItem().toString());
                    Date getDate = dateTicket.getDate();
                    pstmt.setString(3,String.valueOf(sdfDate.format(getDate)));
                    pstmt.setString(4,txtDescription.getText());
                    pstmt.setString(5,cmbStatus.getSelectedItem().toString());
                    pstmt.setString(6,cmbCategory.getSelectedItem().toString());
                    pstmt.setString(7,txtAssignee.getText());
                    String getAssigned;
                    if (cmbAssignedTo.getSelectedIndex()==-1){
                        getAssigned="UNASSIGNED";
                    }else{
                        getAssigned = cmbAssignedTo.getSelectedItem().toString();
                    }
                    pstmt.setString(8,getAssigned);
                    pstmt.setString(9,cmbType.getSelectedItem().toString());
                    getTime = new Date();
                    pstmt.setString(10, String.valueOf(sdfTime.format(getTime)));
                    pstmt.setString(11,cmbPriority.getSelectedItem().toString());
                    pstmt.setString(12,getImagePath);
                    pstmt.execute();
                    pstmt.close();
                    insertTicketSequence();
                    JOptionPane.showMessageDialog(this, "TICKED RAISED");
                    if (cmbStatus.getSelectedItem().toString() == "PENDING"){
                        try {
                            Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress("ess.jbartolome@gmail.com"));
                            message.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse(getEmail));
                            message.setSubject("iSoft Ticketing System - New Issue Raised");
                            message.setText("Hi " + cmbAssignedTo.getSelectedItem().toString() + "," 
                                    + "\n\n A ticket has been assigned to you for client " + cmbClient.getSelectedItem().toString() + "."   
                                    + "\n You can view details of the ticket on iSoft Ticketing System."
                                    + "\n\n\n Regards,"
                                    + "\n iSoft Information Technologies");
                            Transport.send(message);
                        } catch (MessagingException e) {
                                JOptionPane.showMessageDialog(this, e.getMessage());
                        }
                    }
                    disableTexts();
                    btnEdit.setEnabled(true);
                    btnAdd.setEnabled(true);
                    dateTicket.setDate(null);
                //Adding of ticket sequence update
                    insertTicketSequence();
                    Date getDateToday = new Date();
                    Date getTime = new Date();
                        try{
                            String insertTix= "INSERT INTO tblTicketSequence (ts_id, ts_tiID,"
                                    + "ts_tixUpdate,ts_date,ts_time,ts_addUser) values (?,?,?,?,?,?)";
                            pstmt = conn.prepareStatement(insertTix);
                            pstmt.setInt(1,getMaxTixSequence);
                            pstmt.setInt(2,Integer.valueOf(lblID.getText()));
                            pstmt.setString(3, "Ticket Generated");
                            pstmt.setString(4, sdfDate.format(getDateToday));
                            pstmt.setString(5, sdfTime.format(getTime));
                            pstmt.setString(6,frmLogin.username);
                            pstmt.execute();
                            pstmt.close();
                        }catch(SQLException e){
                            JOptionPane.showMessageDialog(this, e.getMessage());
                        }
                    clearTexts();
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }else if(add==false && edit==true){
                try{
                    String editTicket = "UPDATE tblTicket set ti_client=?,ti_resolvedate=?,ti_resolvetime=?,"
                            + "ti_status=?,ti_category=?,ti_assigned=?,ti_type=?,ti_priority=?,ti_attachment=?, "
                            + "ti_description=? where ti_id=?";
                    pstmt = conn.prepareStatement(editTicket);
                    pstmt.setString(1, cmbClient.getSelectedItem().toString());
                    Date getDate = new Date();
                    pstmt.setString(2,String.valueOf(sdfDate.format(getDate)));
                    pstmt.setString(3,String.valueOf(sdfTime.format(getDate)));
                    pstmt.setString(4,cmbStatus.getSelectedItem().toString());
                    pstmt.setString(5,cmbCategory.getSelectedItem().toString());
                    String getAssigned;
                    if (cmbAssignedTo.getSelectedIndex()==-1){
                        getAssigned="UNASSIGNED";
                    }else{
                        getAssigned = cmbAssignedTo.getSelectedItem().toString();
                    }
                    pstmt.setString(6,getAssigned);
                    pstmt.setString(7,cmbType.getSelectedItem().toString());
//                    if (cmbResolveStatus.getSelectedIndex() == -1){
//                        pstmt.setString(8,"");
//                    }else{
//                        pstmt.setString(8,cmbResolveStatus.getSelectedItem().toString());
//                    }
                    pstmt.setString(8,cmbPriority.getSelectedItem().toString());
                    pstmt.setString(9,getImagePath);
                    pstmt.setString(10, txtDescription.getText());
                    pstmt.setInt(11,Integer.valueOf(lblID.getText()));
                    pstmt.execute();
                    pstmt.close();
                    JOptionPane.showMessageDialog(this, "TICKED STATUS UPDATED");
                    if (cmbStatus.getSelectedItem().toString() == "PENDING"){
                        try {
                            Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress("ess.jbartolome@gmail.com"));
                            message.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse(getEmail));
                            message.setSubject("iSoft Ticketing System - Ticket has been assigned to you");
                            message.setText("Hi " + cmbAssignedTo.getSelectedItem().toString() + "," 
                                    + "\n\n A ticket has been assigned to you for client " + cmbClient.getSelectedItem().toString() + "."   
                                    + "\n You can view details of the ticket on iSoft Ticketing System."
                                    + "\n\n\n Regards,"
                                    + "\n iSoft Information Technologies");
                            Transport.send(message);
                        } catch (MessagingException e) {
                                JOptionPane.showMessageDialog(this, e.getMessage());
                        }
                    }
                    clearTexts();
                    disableTexts();
                    dateTicket.setDate(null);
                    btnEdit.setEnabled(true);
                    btnAdd.setEnabled(true);
//                    cmbResolveStatus.setVisible(false);
//                    lblResolveStatus.setVisible(false);
                    
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }
        }
        
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void insertTicketSequence(){  
        try{
            pstmt = conn.prepareStatement("Select ts_id from tblTicketSequence "
                    + "order by ts_id DESC LIMIT 1");
            rs = pstmt.executeQuery();
            if (rs.next()){
                getMaxTixSequence = rs.getInt(1);
                getMaxTixSequence++;
            }else{
                getMaxTixSequence =1;
            }
        }catch(SQLException e){
            e.getMessage();
        }        
    }
        
    private void dateFromMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateFromMouseReleased

    }//GEN-LAST:event_dateFromMouseReleased

    private void dateToMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateToMouseReleased

    }//GEN-LAST:event_dateToMouseReleased

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowActionPerformed
        Date getFromDate = dateFrom.getDate();
        Date getToDate = dateTo.getDate();
        int getindex = cmbClientSearch.getSelectedIndex();
        if (getFromDate == null && getToDate == null){
            JOptionPane.showMessageDialog(this, "PLEASE CHOOSE A DATE RANGE","TICKETING",JOptionPane.WARNING_MESSAGE);
        }else{
        if (cmbTypeCategory.getSelectedItem().equals("ISSUE - TICKET")){
            String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_type=? and ti_assigned=? order by ti_id";
            String fetchTicketDataAdmin = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_type=? order by ti_id";
            String fetchTicketDataWithClient = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_type=? and ti_client=? and ti_assigned=? order by ti_id";
            String fetchTicketDataWithClientAdmin = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_type=? and ti_client=? order by ti_id";
            try{
                if (cmbClientSearch.getSelectedIndex() == -1)
                {
                    if (frmLogin.isAdmin.equals("N")){
                        pstmt = conn.prepareStatement(fetchTicketData);
                        pstmt.setString(4,frmLogin.username);
                    }else{
                        pstmt = conn.prepareStatement(fetchTicketDataAdmin);
                    }    
                        pstmt.setString(1, String.valueOf(sdfDate.format(getFromDate)));
                        pstmt.setString(2, String.valueOf(sdfDate.format(getToDate)));
                        pstmt.setString(3,"ISSUE - TICKET");
                        rs = pstmt.executeQuery();
                        tblTicket.setModel(DbUtils.resultSetToTableModel(rs));    
                }else
                {
                     if (frmLogin.isAdmin.equals("N")){
                        pstmt = conn.prepareStatement(fetchTicketDataWithClient);
                        pstmt.setString(5,frmLogin.username);
                    }else{
                        pstmt = conn.prepareStatement(fetchTicketDataWithClientAdmin);
                    }    
                        pstmt.setString(1, String.valueOf(sdfDate.format(getFromDate)));
                        pstmt.setString(2, String.valueOf(sdfDate.format(getToDate)));
                        pstmt.setString(3,"ISSUE - TICKET");
                        pstmt.setString(4,cmbClientSearch.getSelectedItem().toString());
                        rs = pstmt.executeQuery();
                        tblTicket.setModel(DbUtils.resultSetToTableModel(rs));   
                }
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }else if(cmbTypeCategory.getSelectedItem().equals("ALL")){
            String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_assigned=? order by ti_id";
            String fetchTicketDataAdmin = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? order by ti_id";
            String fetchTicketDataWithClient = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_client=? and ti_assigned=? order by ti_id";
            String fetchTicketDataWithClientAdmin = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_client=? order by ti_id";
            try{
                if (cmbClientSearch.getSelectedIndex() == -1){
                    if (frmLogin.isAdmin.equals("N")){
                        pstmt = conn.prepareStatement(fetchTicketData);
                        pstmt.setString(3,frmLogin.username);
                    }else{
                        pstmt = conn.prepareStatement(fetchTicketDataAdmin);
                    }
                    pstmt.setString(1, String.valueOf(sdfDate.format(getFromDate)));
                    pstmt.setString(2, String.valueOf(sdfDate.format(getToDate)));
                    rs = pstmt.executeQuery();
                    tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                }else
                {
                     if (frmLogin.isAdmin.equals("N")){
                        pstmt = conn.prepareStatement(fetchTicketDataWithClient);
                        pstmt.setString(4,frmLogin.username);
                    }else{
                        pstmt = conn.prepareStatement(fetchTicketDataWithClientAdmin);
                    }    
                        pstmt.setString(1, String.valueOf(sdfDate.format(getFromDate)));
                        pstmt.setString(2, String.valueOf(sdfDate.format(getToDate)));
                        pstmt.setString(3,cmbClientSearch.getSelectedItem().toString());
                        rs = pstmt.executeQuery();
                        tblTicket.setModel(DbUtils.resultSetToTableModel(rs));   
                }
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }     
        }else if(cmbTypeCategory.getSelectedItem().equals("CHANGE REQUEST")){
            String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ?  and ti_type=? and ti_assigned=? order by ti_status";
            String fetchTicketDataAdmin = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ?  and ti_type=? order by ti_status";
              String fetchTicketDataWithClient = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_type=? and ti_client=? and ti_assigned=? order by ti_id";
            String fetchTicketDataWithClientAdmin = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_type=? and ti_client=? order by ti_id";
            try{
                if (cmbClientSearch.getSelectedIndex()==-1){
                    if (frmLogin.isAdmin.equals("N")){
                        pstmt = conn.prepareStatement(fetchTicketData); 
                        pstmt.setString(4,frmLogin.username);               
                    }else{
                        pstmt = conn.prepareStatement(fetchTicketDataAdmin);                
                    }
                    pstmt.setString(1, String.valueOf(sdfDate.format(getFromDate)));
                    pstmt.setString(2, String.valueOf(sdfDate.format(getToDate)));
                    pstmt.setString(3, "CHANGE REQUEST");
                    rs = pstmt.executeQuery();
                    tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                }else
                {
                     if (frmLogin.isAdmin.equals("N")){
                        pstmt = conn.prepareStatement(fetchTicketDataWithClient);
                        pstmt.setString(5,frmLogin.username);
                    }else{
                        pstmt = conn.prepareStatement(fetchTicketDataWithClientAdmin);
                    }    
                        pstmt.setString(1, String.valueOf(sdfDate.format(getFromDate)));
                        pstmt.setString(2, String.valueOf(sdfDate.format(getToDate)));
                        pstmt.setString(3,"CHANGE REQUEST");
                        pstmt.setString(4,cmbClientSearch.getSelectedItem().toString());
                        rs = pstmt.executeQuery();
                        tblTicket.setModel(DbUtils.resultSetToTableModel(rs));   
                }
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }     
        }  
        }
        sortTableTicket();
    }//GEN-LAST:event_btnShowActionPerformed
    private void showTicketTable(){
        if (frmDashboard.showQuery == "UNASSIGNED TICKETS") {
            String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_assigned=? and ti_type=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"UNASSIGNED");
                pstmt.setString(4,"ISSUE - TICKET");
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if(frmDashboard.showQuery == "PENDING TICKETS"){
                String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_assigned<>? and ti_status=? and ti_type=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"UNASSIGNED");
                pstmt.setString(4,"PENDING");
                pstmt.setString(5,"ISSUE - TICKET");
                pstmt.setString(6,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }  
            }else if( frmDashboard.showQuery =="RESOLVED TICKETS"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_status=? and ti_type=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"RESOLVED");
                pstmt.setString(4,"ISSUE - TICKET");
                pstmt.setString(5,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if( frmDashboard.showQuery =="UNASSIGNED REQUEST"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_type=? and ti_assigned=? and ti_status=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"CHANGE REQUEST");
                pstmt.setString(4,"UNASSIGNED");                
                pstmt.setString(5,"PENDING");                
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if(frmDashboard.showQuery == "PENDING REQUEST"){
                String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_assigned<>? and ti_status=? and ti_type=? and ti_assigned=? ";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"UNASSIGNED");
                pstmt.setString(4,"PENDING");
                pstmt.setString(5,"CHANGE REQUEST");
                pstmt.setString(6,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }  
            }else if( frmDashboard.showQuery =="RESOLVED REQUEST"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_status=? and ti_type=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"RESOLVED");
                pstmt.setString(4,"CHANGE REQUEST");
                pstmt.setString(5,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if( frmDashboard.showQuery =="CLOSED TICKETS"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_status=? and ti_type=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"CLOSED");
                pstmt.setString(4,"ISSUE - TICKET");
                pstmt.setString(5,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if( frmDashboard.showQuery =="CLOSED REQUEST"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_status=? and ti_type=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"CLOSED");
                pstmt.setString(4,"CHANGE REQUEST");
                pstmt.setString(5,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if( frmDashboard.showQuery =="ALL PENDING TIX"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_status=? and ti_type=? and ti_assigned<>?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"PENDING");
                pstmt.setString(4,"ISSUE - TICKET");
                pstmt.setString(5,"UNASSIGNED");
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if( frmDashboard.showQuery =="ALL PENDING REQ"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_dateRaised between ? and ? and ti_status=? and ti_type=? and ti_assigned<>?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1, String.valueOf(today.withDayOfMonth(1)));
                pstmt.setString(2, String.valueOf(today.withDayOfMonth(today.lengthOfMonth())));
                pstmt.setString(3,"PENDING");
                pstmt.setString(4,"CHANGE REQUEST");
                pstmt.setString(5,"UNASSIGNED");
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }   
            }else if( frmDashboard.showQuery =="OVERALL PENDING TIX"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_status=? and ti_type=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1,"PENDING");
                pstmt.setString(2,"ISSUE - TICKET");
                pstmt.setString(3,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            } 
            }else if( frmDashboard.showQuery =="OVERALL PENDING REQ"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_status=? and ti_type=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1,"PENDING");
                pstmt.setString(2,"CHANGE REQUEST");
                pstmt.setString(3,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            } 
            }else if( frmDashboard.showQuery =="OVERALL RESOLVED"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type"
                         + " from tblTicket where ti_status=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1,"RESOLVED");
                pstmt.setString(2,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            } 
            }else if( frmDashboard.showQuery =="OVERALL CLOSED"){
                 String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type"
                         + " from tblTicket where ti_status=? and ti_assigned=?";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1,"CLOSED");
                pstmt.setString(2,frmLogin.username);
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            } 
            }
    }
    private void tblTicketMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTicketMouseClicked
        int row = tblTicket.getSelectedRow();
        int ba = tblTicket.convertRowIndexToModel(row);
        String tblClick = tblTicket.getModel().getValueAt(ba, 0).toString();
        getAssigned = tblTicket.getModel().getValueAt(ba, 4).toString();
        String fetchData = "Select * from tblTicket where ti_id=?";
        disableTexts();
        btnAdd.setEnabled(true);
        try{
            pstmt = conn.prepareStatement(fetchData);
            pstmt.setString(1, tblClick);
            rs = pstmt.executeQuery();
            if(rs.next()){               
                txtAssignee.setText(rs.getString("ti_assignee"));
                txtDescription.setText(rs.getString("ti_description"));
                lblID.setText(String.valueOf(rs.getInt("ti_id")));
                cmbAssignedTo.setSelectedItem(rs.getString("ti_assigned"));
                cmbCategory.setSelectedItem(rs.getString("ti_category"));
                cmbClient.setSelectedItem(rs.getString("ti_client"));
                cmbStatus.setSelectedItem(rs.getString("ti_status"));
                cmbType.setSelectedItem(rs.getString("ti_type"));
                dateTicket.setDate(sdfDate.parse(rs.getString("ti_dateRaised")));
                getNameAssigned = rs.getString("ti_assigned");
                getAssignee = rs.getString("ti_assignee");
                cmbPriority.setSelectedItem(rs.getString("ti_priority"));
                getLink = rs.getString("ti_attachment");
                if (getLink == null){
                    lblAttachment.setIcon(null);
                    lblAttachment.setText("NO ATTACHED FILE FOR THIS ENTRY");
                    fillTableSequence();
                    pstmt.close();
                    return;
                }else{
                    File img = new File(rs.getString("ti_attachment"));
                    lblAttachment.setText("");
                    BufferedImage buffImg = ImageIO.read(img);
                    Image dimg = buffImg.getScaledInstance(lblAttachment.getWidth(), lblAttachment.getHeight(), Image.SCALE_SMOOTH);
                    ImageIcon imgIcon = new ImageIcon(dimg);
                    lblAttachment.setIcon(imgIcon);
                    fillTableSequence();
                    pstmt.close();
                }
//                cmbResolveStatus.setSelectedItem(rs.getString("ti_statusResolved"));
              
            }
        }catch(SQLException | ParseException | IOException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_tblTicketMouseClicked
    private void fillTableSequence(){
        String fetchData = "Select ts_id,ts_Date,ts_time,ts_addUser,ts_tixUpdate from tblTicketSequence "
                + "where ts_tiID=? order by ts_id DESC";
        try{
            pstmt = conn.prepareStatement(fetchData);
            pstmt.setInt(1, Integer.valueOf(lblID.getText()));
            rs = pstmt.executeQuery();
                tblSequence.setModel(DbUtils.resultSetToTableModel(rs));
            sortTableSequence();
            pstmt.close();
            txtUpdateTix.setText("");
            }catch(SQLException ex){
                ex.getMessage();
            }
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
       
        if (getNameAssigned.equals(frmLogin.username) || frmLogin.isAdmin.equals("Y")){
            btnEdit.setEnabled(false);
            btnAdd.setEnabled(false);
            enableTexts();
            add = false;
            edit = true;
        }else{
            JOptionPane.showMessageDialog(this, "CANNOT EDIT THIS TICKET", "TICKETING",JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void mnuChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChangePasswordActionPerformed
        frmChangePassword obj = new frmChangePassword();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuChangePasswordActionPerformed

    private void mnuTixPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTixPendingActionPerformed
     
        Map param = new HashMap();
            param.put("dateFrom",today.withDayOfMonth(1));
            param.put("dateTo", today.withDayOfMonth(today.lengthOfMonth()));
            param.put("showStatus", "PENDING");
            param.put("showAssigned", frmLogin.username);
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repPendingTix.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }  
    }//GEN-LAST:event_mnuTixPendingActionPerformed

    private void mnuTixResolveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTixResolveActionPerformed
        Map param = new HashMap();
        param.put("dateFrom",today.withDayOfMonth(1));
        param.put("dateTo", today.withDayOfMonth(today.lengthOfMonth()));
        param.put("showStatus", "RESOLVED");
        param.put("showType", "ISSUE - TICKET");
        param.put("showAssigned", frmLogin.username);
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repResolvedTix.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }  
    }//GEN-LAST:event_mnuTixResolveActionPerformed

    private void mnuTixCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTixCloseActionPerformed
     
        Map param = new HashMap();
            param.put("dateFrom",today.withDayOfMonth(1));
            param.put("dateTo", today.withDayOfMonth(today.lengthOfMonth()));
            param.put("showStatus", "CLOSED");
            param.put("showType", "ISSUE - TICKET");
            param.put("showAssigned", frmLogin.username);
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repClosedTix.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }  
    }//GEN-LAST:event_mnuTixCloseActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        try{
            frmDashboard obj = new frmDashboard();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            conn.close();
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();     
        }
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void mnuClientMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuClientMasterActionPerformed
//        try{
            frmClientDetails obj = new frmClientDetails();
            obj.setVisible(true);
//            rs.close();
//            conn.close();
//            pstmt.close();
//        }catch(SQLException e){
//            e.getMessage();     
//        }
    }//GEN-LAST:event_mnuClientMasterActionPerformed

    private void mnuDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDashboardActionPerformed
        try{
            frmDashboard obj = new frmDashboard();
            obj.setVisible(true);
            this.dispose();
            rs.close();
            conn.close();
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();     
        }
    }//GEN-LAST:event_mnuDashboardActionPerformed

    private void mnuUserMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUserMasterActionPerformed
        frmUserMaster obj = new frmUserMaster();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuUserMasterActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        disableTexts();
        clearTexts();
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void mnuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogoutActionPerformed
        try{
            frmLogin obj = new frmLogin();
            obj.setVisible(true);
            rs.close();
            conn.close();
            pstmt.close();
            this.dispose();
        }catch(SQLException e){
            e.getMessage();     
        }    
    }//GEN-LAST:event_mnuLogoutActionPerformed

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
       int itemSelected = JOptionPane.showConfirmDialog(this, "EXIT THE SYSTEM?","EXIT",JOptionPane.YES_NO_OPTION);
       if (itemSelected == JOptionPane.YES_OPTION){
            this.dispose();
            System.exit(0);
       }
    }//GEN-LAST:event_mnuExitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        insertTicketSequence();
        Date getDateToday = new Date();
        Date getTime = new Date();
        try{
            String insertTix= "INSERT INTO tblTicketSequence (ts_id, ts_tiID,"
                    + "ts_tixUpdate,ts_date,ts_time,ts_addUser,ts_attachment) values (?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(insertTix);
            pstmt.setInt(1,getMaxTixSequence);
            pstmt.setInt(2,Integer.valueOf(lblID.getText()));
            pstmt.setString(3, txtUpdateTix.getText());
            pstmt.setString(4, sdfDate.format(getDateToday));
            pstmt.setString(5, sdfTime.format(getTime));
            pstmt.setString(6,frmLogin.username);
            pstmt.setString(7,getImagePath);
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "TICKET UPDATED");
            clearTexts();
            disableTexts();
            txtUpdateTix.setText("");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void mnuPrintSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPrintSelectActionPerformed
        Map param = new HashMap();
            param.put("selectedID",lblID.getText());
            param.put("showAttachment",getLink);
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repSelected.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr,param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }      
    }//GEN-LAST:event_mnuPrintSelectActionPerformed

    private void mnuReportsReportMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReportsReportMenuActionPerformed
       
            frmReportGeneration obj = new frmReportGeneration();
            obj.setVisible(true);
        
    }//GEN-LAST:event_mnuReportsReportMenuActionPerformed

    private void tblSequenceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSequenceMouseClicked
        int row = tblSequence.getSelectedRow();
        int ba = tblSequence.convertRowIndexToModel(row);
        String tblClick = tblSequence.getModel().getValueAt(ba, 0).toString();
        String fetchData = "Select * from tblTicketSequence where ts_id=?";
        try{
            pstmt =conn.prepareStatement(fetchData);
            pstmt.setString(1,tblClick);
            rs = pstmt.executeQuery();
            if (rs.next()){
                txtUpdateTix.setText(rs.getString("ts_tixUpdate"));
                getLink = rs.getString("ts_attachment");
                if (getLink == null){
                    lblAttachment.setIcon(null);
                    lblAttachment.setText("NO ATTACHED FILE FOR THIS ENTRY");
                }else{
                    File img = new File(rs.getString("ts_attachment"));
                    lblAttachment.setText("");
                    BufferedImage buffImg = ImageIO.read(img);
                    Image dimg = buffImg.getScaledInstance(lblAttachment.getWidth(), lblAttachment.getHeight(), Image.SCALE_SMOOTH);
                    ImageIcon imgIcon = new ImageIcon(dimg);
                    lblAttachment.setIcon(imgIcon);
                }
            }
                
            pstmt.close();
        }catch(SQLException | IOException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_tblSequenceMouseClicked

    private void mnuProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProductsActionPerformed
        frmProduct obj = new frmProduct();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuProductsActionPerformed

    private void mnuClientQandIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuClientQandIActionPerformed
        frmClient obj = new frmClient();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuClientQandIActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        frmQuoteGeneration obj = new frmQuoteGeneration();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void mnuInvoiceGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuInvoiceGenActionPerformed
        frmInvoiceGeneration obj = new frmInvoiceGeneration();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuInvoiceGenActionPerformed

    private void mnuReportsQuotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReportsQuotationActionPerformed
        frmQuotationReport obj = new frmQuotationReport();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuReportsQuotationActionPerformed

    private void mnuReportsInvoiceReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReportsInvoiceReportsActionPerformed
        frmInvoiceReport obj = new frmInvoiceReport();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuReportsInvoiceReportsActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
                String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                    + "where ti_type=? and ti_assigned=? order by ti_resolveDate";
            try{
                pstmt = conn.prepareStatement(fetchTicketData);
                pstmt.setString(1,"ISSUE - TICKET");
                pstmt.setString(2,"UNASSIGNED");
                rs = pstmt.executeQuery();
                tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
                pstmt.close();
                sortTableTicket();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
                        
        Map param = new HashMap();
            param.put("dateFrom",today.withDayOfMonth(1));
            param.put("dateTo", today.withDayOfMonth(today.lengthOfMonth()));
            param.put("showStatus", "PENDING");
            param.put("showType", "ISSUE - TICKET");
            param.put("showAssigned", "UNASSIGNED");
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repUnassignedTix.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                + "where ti_type=? and ti_status=? order by ti_id";
        try{
            pstmt = conn.prepareStatement(fetchTicketData);
            pstmt.setString(1,"ISSUE - TICKET");
            pstmt.setString(2, "PENDING");
            rs = pstmt.executeQuery();
            tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTableTicket();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
                      
        Map param = new HashMap();
            param.put("dateFrom",today.withDayOfMonth(1));
            param.put("dateTo", today.withDayOfMonth(today.lengthOfMonth()));
            param.put("showStatus", "PENDING");
            param.put("showType", "ISSUE - TICKET");
            param.put("showAssigned", "UNASSIGNED");
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repPendingTix.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }  
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
         Map param = new HashMap();
            param.put("dateFrom",today.withDayOfMonth(1));
            param.put("dateTo", today.withDayOfMonth(today.lengthOfMonth()));
            param.put("showStatus", "PENDING");
            param.put("showType", "CHANGE REQUEST");
            param.put("showAssigned", "UNASSIGNED");
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repUnassignedReq.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        } 
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                + "where ti_type=? and ti_status=? order by ti_id";
        try{
            pstmt = conn.prepareStatement(fetchTicketData);
            pstmt.setString(1,"ISSUE - TICKET");
            pstmt.setString(2, "RESOLVED");
            rs = pstmt.executeQuery();
            tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTableTicket();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
                String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                + "where ti_type=? and ti_status=? order by ti_id";
        try{
            pstmt = conn.prepareStatement(fetchTicketData);
            pstmt.setString(1,"ISSUE - TICKET");
            pstmt.setString(2, "CLOSED");
            rs = pstmt.executeQuery();
            tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTableTicket();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                + "where ti_type=? and ti_status=? order by ti_id";
        try{
            pstmt = conn.prepareStatement(fetchTicketData);
            pstmt.setString(1,"CHANGE REQUEST");
            pstmt.setString(2, "UNASSIGNED");
            rs = pstmt.executeQuery();
            tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTableTicket();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                + "where ti_type=? and ti_status=? order by ti_id";
        try{
            pstmt = conn.prepareStatement(fetchTicketData);
            pstmt.setString(1,"CHANGE REQUEST");
            pstmt.setString(2, "PENDING");
            rs = pstmt.executeQuery();
            tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTableTicket();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                + "where ti_type=? and ti_status=? order by ti_id";
        try{
            pstmt = conn.prepareStatement(fetchTicketData);
            pstmt.setString(1,"CHANGE REQUEST");
            pstmt.setString(2, "RESOLVED");
            rs = pstmt.executeQuery();
            tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTableTicket();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        String fetchTicketData = "Select ti_id,ti_client,ti_dateRaised,ti_status,ti_description,ti_assigned,ti_type from tblTicket "
                + "where ti_type=? and ti_status=? order by ti_id";
        try{
            pstmt = conn.prepareStatement(fetchTicketData);
            pstmt.setString(1,"CHANGE REQUEST");
            pstmt.setString(2, "RESOLVED");
            rs = pstmt.executeQuery();
            tblTicket.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTableTicket();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void mnuStaffPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuStaffPositionActionPerformed
        frmPosition obj = new frmPosition();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuStaffPositionActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        Map param = new HashMap();
        param.put("showStatus", "RESOLVED");
        param.put("showType", "ISSUE - TICKET");
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repResolvedTixTasks.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);

        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }  
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        frmReceiptGeneration obj = new frmReceiptGeneration();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void mnuReportsReceiptReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReportsReceiptReportsActionPerformed
        frmReceiptReport obj = new frmReceiptReport();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuReportsReceiptReportsActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        frmMoneyTracker obj  = new frmMoneyTracker();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void mnuMoneyTrackerReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMoneyTrackerReportsActionPerformed
        frmMoneyTrackerReports obj = new frmMoneyTrackerReports();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuMoneyTrackerReportsActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        FileFilter ft = new FileNameExtensionFilter("images .pdf,.jpg,.png", "jpg","png","pdf");
        File outputFile  = new File("src\\attachments\\attachment" + "Ticketing" + lblID.getText());
        db.addChoosableFileFilter(ft);
        int returnVal = db.showOpenDialog(this);
        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION){
        try{
           BufferedImage input = ImageIO.read(db.getSelectedFile());
           ImageIO.write(input, "PNG", outputFile);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
            getImagePath = outputFile.toString();
            lblAttachment.setText(getImagePath);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void lblAttachmentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAttachmentMouseClicked
        Map param = new HashMap();
        param.put("showAttachment",String.valueOf(getLink));
        param.put("showID",lblID.getText());
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repTicketAttachment.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);
        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, "NO IMAGE ATTACHED TO THIS TICKET");
        }
    }//GEN-LAST:event_lblAttachmentMouseClicked

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        frmTicketCategory obj = new frmTicketCategory();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void lblAttachmentMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAttachmentMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_lblAttachmentMouseEntered

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        FileFilter ft = new FileNameExtensionFilter("images .pdf,.jpg,.png", "jpg","png","pdf");
        File outputFile  = new File("src\\attachments\\attachment" + "TicketingSequence" + lblID.getText());
        db.addChoosableFileFilter(ft);
        int returnVal = db.showOpenDialog(this);
        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION){
        try{
           BufferedImage input = ImageIO.read(db.getSelectedFile());
           ImageIO.write(input, "PNG", outputFile);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
            getImagePath = outputFile.toString();
            lblAttachment.setText(getImagePath);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        frmTimeKeeping obj = new frmTimeKeeping();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        frmTimeKeepingReport obj = new frmTimeKeepingReport();
                obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void mnuModifyLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuModifyLogsActionPerformed
        frmTKModifyLogs obj = new frmTKModifyLogs();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuModifyLogsActionPerformed

    private void mnuDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDepartmentActionPerformed
        frmDepartmentMaster obj = new frmDepartmentMaster();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuDepartmentActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        frmTimeKeepingReport obj = new frmTimeKeepingReport();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        frmClientReports obj = new frmClientReports();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void cmbClientSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClientSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbClientSearchActionPerformed

    private void mnuTKReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTKReportActionPerformed
        frmTKReport obj = new frmTKReport();
        obj.setVisible(true);
    }//GEN-LAST:event_mnuTKReportActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        frmLunch obj = new frmLunch();
        obj.setVisible(true);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

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
            java.util.logging.Logger.getLogger(fmrTicketing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fmrTicketing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fmrTicketing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fmrTicketing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fmrTicketing().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnShow;
    private javax.swing.JComboBox<String> cmbAssignedTo;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbClient;
    private javax.swing.JComboBox<String> cmbClientSearch;
    private javax.swing.JComboBox<String> cmbPriority;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JComboBox<String> cmbTypeCategory;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTicket;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JFileChooser db;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblAttachment;
    private javax.swing.JLabel lblID;
    private javax.swing.JMenu menu4;
    private javax.swing.JMenuItem mnuChangePassword;
    private javax.swing.JMenuItem mnuClientMaster;
    private javax.swing.JMenuItem mnuClientQandI;
    private javax.swing.JMenuItem mnuDashboard;
    private javax.swing.JMenuItem mnuDepartment;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenuItem mnuInvoiceGen;
    private javax.swing.JMenuItem mnuLogout;
    private javax.swing.JMenuItem mnuModifyLogs;
    private javax.swing.JMenu mnuMoneyTracker;
    private javax.swing.JMenuItem mnuMoneyTrackerReports;
    private javax.swing.JMenuItem mnuPrintSelect;
    private javax.swing.JMenuItem mnuProducts;
    private javax.swing.JMenu mnuQuoteGen;
    private javax.swing.JMenuItem mnuReportsInvoiceReports;
    private javax.swing.JMenuItem mnuReportsQuotation;
    private javax.swing.JMenuItem mnuReportsReceiptReports;
    private javax.swing.JMenuItem mnuReportsReportMenu;
    private javax.swing.JMenuItem mnuStaffPosition;
    private javax.swing.JMenuItem mnuTKReport;
    private javax.swing.JMenu mnuTasks;
    private javax.swing.JMenuItem mnuTixClose;
    private javax.swing.JMenuItem mnuTixPending;
    private javax.swing.JMenuItem mnuTixResolve;
    private javax.swing.JMenuItem mnuUserMaster;
    private javax.swing.JTable tblSequence;
    private javax.swing.JTable tblTicket;
    private javax.swing.JLabel txtAssignee;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextArea txtUpdateTix;
    // End of variables declaration//GEN-END:variables
}
