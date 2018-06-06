
package forms;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import net.proteanit.sql.DbUtils;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class frmMoneyTracker extends javax.swing.JFrame {
String getType,getPanel;
ResultSet rs;
PreparedStatement pstmt;
Connection conn;
boolean add,edit;
int getMaxID;
String getImagePath;
DecimalFormat df = new DecimalFormat("0.000");
SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    public frmMoneyTracker() {
        initComponents();
        fillComboType();
        doConnect();
        fillTable();
        getSummary();
        disableTexts();
        setDefaultCloseOperation(frmMoneyTracker.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        sortTable();
    }
    private void sortTable(){
        tblShowAccounts.getColumnModel().getColumn(0).setHeaderValue("ID");
        tblShowAccounts.getColumnModel().getColumn(1).setHeaderValue("INCOME");
        tblShowAccounts.getColumnModel().getColumn(2).setHeaderValue("EXPENSE");
        tblShowAccounts.getColumnModel().getColumn(3).setHeaderValue("TYPE");
        tblShowAccounts.getColumnModel().getColumn(4).setHeaderValue("COMMENTS");
        tblShowAccounts.getColumnModel().getColumn(5).setHeaderValue("DATE");
        tblShowAccounts.getColumnModel().getColumn(6).setHeaderValue("ATTACHMENT");
    }
    private void disableTexts(){
        txtAmount.setEnabled(false);
        txtSource.setEnabled(false);
        cmbIncomeType.setEnabled(false);
        dateEntered.setEnabled(false);
    }
    private void enableTexts(){
        txtAmount.setEnabled(true);
        txtSource.setEnabled(true);
        cmbIncomeType.setEnabled(true);
        dateEntered.setEnabled(true);
    }
    private void doConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
        }catch(SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void getSummary(){
        try{
            pstmt = conn.prepareStatement("Select sum(ac_income) from tblAccount where ac_type=?");
            pstmt.setString(1, "CASH");
            rs = pstmt.executeQuery();
            if(rs.next()){
                lblCash.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        try{
            pstmt = conn.prepareStatement("Select sum(ac_expense) from tblAccount where ac_type=?");
            pstmt.setString(1, "CASH");
            rs = pstmt.executeQuery();
            if(rs.next()){
                lblCashExpense.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        checkForNull();
        double getTotalCash = Double.parseDouble(lblCash.getText()) - Double.parseDouble(lblCashExpense.getText());
        lblTotalCashAmount.setText(String.valueOf(df.format(getTotalCash)));
        //ACCOUNTS
        try{
            pstmt = conn.prepareStatement("Select sum(ac_income) from tblAccount where ac_type=?");
            pstmt.setString(1, "ACCOUNT");
            rs = pstmt.executeQuery();
            if (rs.next()){
                lblAccount.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        try{
            pstmt = conn.prepareStatement("Select sum(ac_expense) from tblAccount where ac_type=?");
            pstmt.setString(1, "ACCOUNT");
            rs = pstmt.executeQuery();
            if(rs.next()){
                lblAccountExpense.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        checkForNull();
        double getTotalAccount = Double.parseDouble(lblAccount.getText()) - Double.parseDouble(lblAccountExpense.getText());
        lblTotalAccountAmount.setText(String.valueOf(df.format(getTotalAccount)));
        
        //CHEQUE
        try{
            pstmt = conn.prepareStatement("Select sum(ac_income) from tblAccount where ac_type=?");
            pstmt.setString(1, "CHEQUE");
            rs = pstmt.executeQuery();
            if (rs.next()){
                lblCheque.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        try{
            pstmt = conn.prepareStatement("Select sum(ac_expense) from tblAccount where ac_type=?");
            pstmt.setString(1, "CHEQUE");
            rs = pstmt.executeQuery();
            if (rs.next()){
                lblChequeExpense.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        checkForNull();
        double getTotalCheck = Double.parseDouble(lblCheque.getText()) - Double.parseDouble(lblChequeExpense.getText());
        lblTotalChequeAmount.setText(String.valueOf(df.format(getTotalCheck)));
        
    }
    private void checkForNull(){
        if (lblAccountExpense.getText() == null){
            lblAccountExpense.setText("0.000");
        }
        if (lblAccount.getText() == null){
            lblAccount.setText("0.000");
        }
        if (lblCash.getText() == null){
            lblCash.setText("0.000");
        }
        if (lblCashExpense.getText() == null){
            lblCashExpense.setText("0.000");
        }
        if (lblCheque.getText() == null){
            lblCheque.setText("0.000");
        }
        if (lblChequeExpense.getText() == null){
            lblChequeExpense.setText("0.000");
        }
    }
    private void fillTable(){
        try{
            pstmt = conn.prepareStatement("Select * from tblAccount order by ac_date DESC");
            rs = pstmt.executeQuery();
            tblShowAccounts.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTable();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }    
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        db = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblShowAccounts = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dateEntered = new com.toedter.calendar.JDateChooser();
        txtSource = new javax.swing.JTextField();
        cmbIncomeType = new javax.swing.JComboBox<>();
        txtAmount = new javax.swing.JTextField();
        lblIdentifier = new javax.swing.JLabel();
        lblSource = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnIncome = new javax.swing.JButton();
        btnExpense = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTotalCashAmount = new javax.swing.JLabel();
        lblCash = new javax.swing.JLabel();
        lblCashExpense = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        lblTotalAccountAmount = new javax.swing.JLabel();
        lblAccount = new javax.swing.JLabel();
        lblAccountExpense = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblCheque = new javax.swing.JLabel();
        lblChequeExpense = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblTotalChequeAmount = new javax.swing.JLabel();
        lblAttachment = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblShowAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ));
        tblShowAccounts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblShowAccountsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblShowAccounts);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 670, 150));

        jPanel6.setBackground(new java.awt.Color(102, 102, 102));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("DATE:");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 50, -1));

        lblType.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblType.setText("INCOME TYPE:");
        jPanel6.add(lblType, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 140, 30));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("AMOUNT:");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 140, 30));

        dateEntered.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateEnteredMouseReleased(evt);
            }
        });
        jPanel6.add(dateEntered, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 170, 30));
        jPanel6.add(txtSource, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 240, 30));

        cmbIncomeType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel6.add(cmbIncomeType, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 240, 30));
        jPanel6.add(txtAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 240, 30));

        lblIdentifier.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblIdentifier.setForeground(new java.awt.Color(255, 204, 51));
        lblIdentifier.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(lblIdentifier, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 150, 40));

        lblSource.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblSource.setText("SOURCE:");
        jPanel6.add(lblSource, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 140, 30));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButton1.setText("ATTACHMENT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 270, -1));

        btnIncome.setText("ADD INCOME");
        btnIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncomeActionPerformed(evt);
            }
        });
        jPanel6.add(btnIncome, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 20));

        btnExpense.setText("ADD EXPENSE");
        btnExpense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpenseActionPerformed(evt);
            }
        });
        jPanel6.add(btnExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 110, 20));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 520, 320));

        jPanel8.setBackground(new java.awt.Color(102, 102, 102));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jPanel8.add(btnEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 140, 30));

        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel8.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 140, 30));

        btnCancel.setText("CANCEL");
        jPanel8.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 140, 30));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("TOTAL CASH AMOUNT:");
        jPanel8.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 140, -1));

        jLabel4.setText("TOTAL CASH:");
        jPanel8.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 160, -1));

        jLabel6.setText("TOTAL CASH EXPENSE:");
        jPanel8.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 160, -1));

        lblTotalCashAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalCashAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalCashAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalCashAmount.setText("0.00");
        jPanel8.add(lblTotalCashAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 80, -1));

        lblCash.setText("0.00");
        jPanel8.add(lblCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 80, -1));

        lblCashExpense.setText("0.00");
        jPanel8.add(lblCashExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 80, -1));
        jPanel8.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 420, 10));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("TOTAL ACCOUNT AMOUNT:");
        jPanel8.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 160, 140, -1));

        lblTotalAccountAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalAccountAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalAccountAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalAccountAmount.setText("0.00");
        jPanel8.add(lblTotalAccountAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 180, 80, -1));

        lblAccount.setText("0.00");
        jPanel8.add(lblAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 80, -1));

        lblAccountExpense.setText("0.00");
        jPanel8.add(lblAccountExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 80, -1));

        jLabel8.setText("TOTAL ACCOUNT:");
        jPanel8.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 160, -1));

        jLabel9.setText("TOTAL ACCOUNT EXPENSE:");
        jPanel8.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 160, -1));

        btnAdd.setText("ADD");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel8.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 140, 30));
        jPanel8.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 420, 10));

        jLabel10.setText("TOTAL CHEQUE:");
        jPanel8.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 160, -1));

        jLabel11.setText("TOTAL CHEQUE EXPENSE:");
        jPanel8.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 160, -1));

        lblCheque.setText("0.00");
        jPanel8.add(lblCheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 210, 80, -1));

        lblChequeExpense.setText("0.00");
        jPanel8.add(lblChequeExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 80, -1));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TOTAL CHEQUEAMOUNT:");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 210, 140, -1));

        lblTotalChequeAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalChequeAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalChequeAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalChequeAmount.setText("0.00");
        jPanel8.add(lblTotalChequeAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 80, -1));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 460, 320));

        lblAttachment.setBackground(new java.awt.Color(255, 255, 255));
        lblAttachment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel4.add(lblAttachment, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 310, 150));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1010, 500));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1030, 520));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1053, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateEnteredMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateEnteredMouseReleased

    }//GEN-LAST:event_dateEnteredMouseReleased
    private void fillComboType(){
        cmbIncomeType.removeAllItems();
        cmbIncomeType.addItem("CASH");
        cmbIncomeType.addItem("ACCOUNT");
        cmbIncomeType.addItem("CHEQUE");
    }
    private void btnIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncomeActionPerformed
        getPanel = "INCOME";
        lblType.setText("INCOME TYPE:");
        lblSource.setText("SOURCE:");
        lblIdentifier.setText(getPanel);
        enableTexts();
    }//GEN-LAST:event_btnIncomeActionPerformed

    private void btnExpenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpenseActionPerformed
        getPanel = "EXPENSE";
        lblType.setText("EXPENSE TYPE:");
        lblSource.setText("REASON:");
        lblIdentifier.setText(getPanel);
        enableTexts();
    }//GEN-LAST:event_btnExpenseActionPerformed
    private void getNextID(){
        String fetchMaxID = "SELECT ac_transactionID from tblAccount order by ac_transactionID DESC LIMIT 1";
        try{
            pstmt = conn.prepareStatement(fetchMaxID);
            rs = pstmt.executeQuery();
            if (rs.next()){
                getMaxID = rs.getInt("ac_transactionID");
                getMaxID++;
            }else{
                getMaxID = 1;
            }
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        getNextID();
        
        String addToIncome = "INSERT INTO tblAccount (ac_transactionID,ac_income,ac_type,ac_comments,ac_date,ac_attachment) "
                + "values (?,?,?,?,?,?)";
        String addToExpense = "INSERT INTO tblAccount (ac_transactionID,ac_expense,ac_type,ac_comments,ac_date,ac_attachment) "
                + "values (?,?,?,?,?,?)";
        if (getPanel.equals("INCOME")){
            try{
//                File image = new File(getImagePath);
//                FileInputStream fis = new FileInputStream(image);
//                
                pstmt = conn.prepareStatement(addToIncome);
                pstmt.setInt(1, getMaxID);
                pstmt.setDouble(2,Double.parseDouble(txtAmount.getText()));
                pstmt.setString(3, cmbIncomeType.getSelectedItem().toString());
                pstmt.setString(4,txtSource.getText());
                pstmt.setString(5, String.valueOf(sdfDate.format(dateEntered.getDate())));
                pstmt.setString(6, getImagePath);
                pstmt.execute();
                pstmt.close();
                JOptionPane.showMessageDialog(this, "INCOME ADDED TO " + cmbIncomeType.getSelectedItem().toString());
                txtAmount.setText("");
                txtSource.setText("");
                fillTable();
                getSummary();
                disableTexts();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }else if (getPanel.equals("EXPENSE")){
            try{
                pstmt = conn.prepareStatement(addToExpense);
                pstmt.setInt(1, getMaxID);
                pstmt.setDouble(2,Double.parseDouble(txtAmount.getText()));
                pstmt.setString(3, cmbIncomeType.getSelectedItem().toString());
                pstmt.setString(4,txtSource.getText());
                pstmt.setString(5, String.valueOf(sdfDate.format(dateEntered.getDate())));
                pstmt.setString(6, getImagePath);
                pstmt.execute();
                pstmt.close();
                JOptionPane.showMessageDialog(this, "EXPENSE ADDED TO " + cmbIncomeType.getSelectedItem().toString());
                txtAmount.setText("");
                txtSource.setText("");
                getSummary();
                fillTable();
                disableTexts();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        add = true;
        edit = false;
        enableTexts();
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        add = false;
        edit = true;
        enableTexts();
        btnAdd.setEnabled(false);
        btnEdit.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FileFilter ft = new FileNameExtensionFilter("images .pdf,.jpg,.png", "jpg","png","pdf");
        getNextID();
        File outputFile  = new File("src\\attachments\\attachment" + getMaxID);
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
        
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblShowAccountsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShowAccountsMouseClicked
        int row = tblShowAccounts.getSelectedRow();
        int ba = tblShowAccounts.convertRowIndexToModel(row);
        String tblClick = tblShowAccounts.getModel().getValueAt(ba, 0).toString();
        Object getLink = tblShowAccounts.getModel().getValueAt(ba, 6);
        String fetchData = "SELECT * from tblAccount where ac_transactionID=?";
        try{
            pstmt = conn.prepareStatement(fetchData);
            pstmt.setString(1, tblClick);
            rs = pstmt.executeQuery();
            if (rs.next()){
                if (getLink == null){
                    lblAttachment.setIcon(null);
                    lblAttachment.setText("NO ATTACHED FILE FOR THIS ENTRY");
                }else{
                    File img = new File(rs.getString("ac_attachment"));
                    lblAttachment.setText("");
                    BufferedImage buffImg = ImageIO.read(img);
                    Image dimg = buffImg.getScaledInstance(lblAttachment.getWidth(), lblAttachment.getHeight(), Image.SCALE_SMOOTH);
                    ImageIcon imgIcon = new ImageIcon(dimg);
                    lblAttachment.setIcon(imgIcon);
                }
            }   
        }catch(SQLException | IOException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_tblShowAccountsMouseClicked

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
            java.util.logging.Logger.getLogger(frmMoneyTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMoneyTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMoneyTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMoneyTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMoneyTracker().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExpense;
    private javax.swing.JButton btnIncome;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbIncomeType;
    private com.toedter.calendar.JDateChooser dateEntered;
    private javax.swing.JFileChooser db;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblAccount;
    private javax.swing.JLabel lblAccountExpense;
    private javax.swing.JLabel lblAttachment;
    private javax.swing.JLabel lblCash;
    private javax.swing.JLabel lblCashExpense;
    private javax.swing.JLabel lblCheque;
    private javax.swing.JLabel lblChequeExpense;
    private javax.swing.JLabel lblIdentifier;
    private javax.swing.JLabel lblSource;
    private javax.swing.JLabel lblTotalAccountAmount;
    private javax.swing.JLabel lblTotalCashAmount;
    private javax.swing.JLabel lblTotalChequeAmount;
    private javax.swing.JLabel lblType;
    private javax.swing.JTable tblShowAccounts;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtSource;
    // End of variables declaration//GEN-END:variables
}
