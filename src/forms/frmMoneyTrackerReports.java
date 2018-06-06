
package forms;

import java.io.File;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class frmMoneyTrackerReports extends javax.swing.JFrame {
String getType,getPanel,getReportString,getReportID;
ResultSet rs;
PreparedStatement pstmt;
Connection conn;
SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
Date getFromDate,getToDate;
DecimalFormat df = new DecimalFormat("#.000");
    public frmMoneyTrackerReports() {
        initComponents();
        doConnect();
        sortTable();
        setDefaultCloseOperation(frmMoneyTrackerReports.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void getSummary(){
        try{
            pstmt = conn.prepareStatement("Select sum(ac_income) from tblAccount where ac_type=? and ac_date between ? and ?");
            pstmt.setString(1, "CASH");
            pstmt.setString(2, sdfDate.format(getFromDate));
            pstmt.setString(3, sdfDate.format(getToDate));
            rs = pstmt.executeQuery();
            if (rs.next()){
                lblCash.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        try{
            pstmt = conn.prepareStatement("Select sum(ac_expense) from tblAccount where ac_type=?  and ac_date between ? and ?");
            pstmt.setString(1, "CASH");
            pstmt.setString(2, sdfDate.format(getFromDate));
            pstmt.setString(3, sdfDate.format(getToDate));
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
            pstmt = conn.prepareStatement("Select sum(ac_income) from tblAccount where ac_type=?  and ac_date between ? and ?");
            pstmt.setString(1, "ACCOUNT");
            pstmt.setString(2, sdfDate.format(getFromDate));
            pstmt.setString(3, sdfDate.format(getToDate));
            rs = pstmt.executeQuery();
            if (rs.next()){
                lblAccount.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        try{
            pstmt = conn.prepareStatement("Select sum(ac_expense) from tblAccount where ac_type=?  and ac_date between ? and ?");
            pstmt.setString(1, "ACCOUNT");
            pstmt.setString(2, sdfDate.format(getFromDate));
            pstmt.setString(3, sdfDate.format(getToDate));
            rs = pstmt.executeQuery();
            if (rs.next()){
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
            pstmt = conn.prepareStatement("Select sum(ac_income) from tblAccount where ac_type=?  and ac_date between ? and ?");
            pstmt.setString(1, "CHEQUE");
            pstmt.setString(2, sdfDate.format(getFromDate));
            pstmt.setString(3, sdfDate.format(getToDate));
            rs = pstmt.executeQuery();
            if(rs.next()){
                lblCheck.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        
        try{
            pstmt = conn.prepareStatement("Select sum(ac_expense) from tblAccount where ac_type=?  and ac_date between ? and ?");
            pstmt.setString(1, "CHEQUE");
            pstmt.setString(2, sdfDate.format(getFromDate));
            pstmt.setString(3, sdfDate.format(getToDate));
            rs = pstmt.executeQuery();
            if(rs.next()){
                lblCheckExpense.setText(rs.getString(1));
                pstmt.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        checkForNull();
        double getTotalCheck = Double.parseDouble(lblCheck.getText()) - Double.parseDouble(lblCheckExpense.getText());
        lblTotalCheckAmount.setText(String.valueOf(df.format(getTotalCheck)));
        //GET TOTAL
        double getTotalExpenseAmount = Double.parseDouble(lblCashExpense.getText()) + Double.parseDouble(lblAccountExpense.getText()) + Double.parseDouble(lblCheckExpense.getText());    
        lblTotalExpenseAmount.setText(String.valueOf(df.format(getTotalExpenseAmount)));
        
        double getTotalMoneyAmount = Double.parseDouble(lblTotalCashAmount.getText())+
                Double.parseDouble(lblTotalAccountAmount.getText()) + Double.parseDouble(lblTotalCheckAmount.getText());
        lblTotalMoneyAmount.setText(String.valueOf(df.format(getTotalMoneyAmount)));
        
        
        
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
        if (lblCheck.getText() == null){
            lblCheck.setText("0.000");
        }
        if (lblCheckExpense.getText() == null){
            lblCheckExpense.setText("0.000");
        }
    }
    private void getDate(){
       getFromDate = dateFrom.getDate();
       getToDate = dateTo.getDate();
    }
    private void doConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
        }catch(SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void sortTable(){
        tblReports.getColumnModel().getColumn(0).setHeaderValue("ID");
        tblReports.getColumnModel().getColumn(1).setHeaderValue("INCOME");
        tblReports.getColumnModel().getColumn(2).setHeaderValue("EXPENSE");
        tblReports.getColumnModel().getColumn(3).setHeaderValue("TYPE");
        tblReports.getColumnModel().getColumn(4).setHeaderValue("COMMENTS");
        tblReports.getColumnModel().getColumn(5).setHeaderValue("DATE");
        tblReports.getColumnModel().getColumn(6).setHeaderValue("ATTACHMENT");
    }
    private void fillTable(){
        getDate();
        try{
            pstmt = conn.prepareStatement("Select * from tblAccount where ac_date "
                    + "between ? and ?");
            pstmt.setString(1, sdfDate.format(getFromDate));
            pstmt.setString(2, sdfDate.format(getToDate));
            rs = pstmt.executeQuery();
            tblReports.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTable();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }    
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dateTo = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblReports = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTotalCashAmount = new javax.swing.JLabel();
        lblTotalAccountAmount = new javax.swing.JLabel();
        lblAccount = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblAccountExpense = new javax.swing.JLabel();
        lblCash = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblCashExpense = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        lblTotalExpenseAmount = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTotalMoneyAmount = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblCheck = new javax.swing.JLabel();
        lblCheckExpense = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblTotalCheckAmount = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateFromMouseReleased(evt);
            }
        });
        jPanel1.add(dateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 160, 40));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("DATE:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 50, 20));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("DATE:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 50, 20));

        dateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateToMouseReleased(evt);
            }
        });
        jPanel1.add(dateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 160, 40));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 640, -1));

        tblReports.setModel(new javax.swing.table.DefaultTableModel(
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
        tblReports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReportsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblReports);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 620, 170));

        jButton1.setText("VIEW REPORT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 120, 40));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalCashAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalCashAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalCashAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalCashAmount.setText("0.00");
        jPanel3.add(lblTotalCashAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 40, 80, -1));

        lblTotalAccountAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalAccountAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalAccountAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalAccountAmount.setText("0.00");
        jPanel3.add(lblTotalAccountAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 90, 80, -1));

        lblAccount.setText("0.00");
        jPanel3.add(lblAccount, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 50, -1));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("TOTAL ACCOUNT AMOUNT:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 70, 140, -1));

        lblAccountExpense.setText("0.00");
        jPanel3.add(lblAccountExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 80, -1));

        lblCash.setText("0.00");
        jPanel3.add(lblCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 50, -1));

        jLabel5.setText("TOTAL CASH:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 160, -1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("TOTAL CASH AMOUNT:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 140, -1));

        lblCashExpense.setText("0.00");
        jPanel3.add(lblCashExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 80, -1));

        jLabel6.setText("TOTAL CASH EXPENSE:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 160, -1));

        jLabel8.setText("TOTAL ACCOUNT:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 160, -1));

        jLabel9.setText("TOTAL ACCOUNT EXPENSE:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 160, -1));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 0, 10, 190));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("TOTAL EXPENSE:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 140, -1));

        lblTotalExpenseAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalExpenseAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalExpenseAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalExpenseAmount.setText("0.00");
        jPanel3.add(lblTotalExpenseAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 70, 80, -1));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("TOTAL MONEY:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, 140, -1));

        lblTotalMoneyAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalMoneyAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalMoneyAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalMoneyAmount.setText("0.00");
        jPanel3.add(lblTotalMoneyAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 80, -1));

        jLabel12.setText("TOTAL CHEQUE:");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 160, -1));

        jLabel13.setText("TOTAL CHEQUE EXPENSE:");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 160, -1));

        lblCheck.setText("0.00");
        jPanel3.add(lblCheck, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 50, -1));

        lblCheckExpense.setText("0.00");
        jPanel3.add(lblCheckExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 80, -1));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("TOTAL CHEQUE AMOUNT:");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, 140, -1));

        lblTotalCheckAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblTotalCheckAmount.setForeground(new java.awt.Color(204, 0, 0));
        lblTotalCheckAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalCheckAmount.setText("0.00");
        jPanel3.add(lblTotalCheckAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 140, 80, -1));

        jButton2.setText("PRINT");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 190, -1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 620, 210));

        jButton3.setText("PRINT");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 283, 150, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateFromMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateFromMouseReleased

    }//GEN-LAST:event_dateFromMouseReleased

    private void dateToMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateToMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_dateToMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        fillTable();
        getSummary();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Map param = new HashMap();
        param.put("dateFrom", String.valueOf(sdfDate.format(dateFrom.getDate())));
        param.put("dateTo", String.valueOf(sdfDate.format(dateTo.getDate())));
        param.put("getTotalCash",lblTotalCashAmount.getText());
        param.put("getTotalCheque", lblTotalCheckAmount.getText());
        param.put("getTotalAccount",lblTotalAccountAmount.getText());
        param.put("getTotalExpense",lblTotalExpenseAmount.getText());
        param.put("getTotalMoney",lblTotalMoneyAmount.getText());
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\Reports\\repAccounting.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);
        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Map param = new HashMap();
        param.put("showAttachment",getReportString);
        param.put("showID",getReportID);
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repAccountIndi.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);
        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tblReportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReportsMouseClicked
        int row = tblReports.getSelectedRow();
        int ba = tblReports.convertRowIndexToModel(row);
        getReportString = tblReports.getModel().getValueAt(ba, 6).toString();
        getReportID = tblReports.getModel().getValueAt(ba, 0).toString();
    }//GEN-LAST:event_tblReportsMouseClicked

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
            java.util.logging.Logger.getLogger(frmMoneyTrackerReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMoneyTrackerReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMoneyTrackerReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMoneyTrackerReports.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMoneyTrackerReports().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblAccount;
    private javax.swing.JLabel lblAccountExpense;
    private javax.swing.JLabel lblCash;
    private javax.swing.JLabel lblCashExpense;
    private javax.swing.JLabel lblCheck;
    private javax.swing.JLabel lblCheckExpense;
    private javax.swing.JLabel lblTotalAccountAmount;
    private javax.swing.JLabel lblTotalCashAmount;
    private javax.swing.JLabel lblTotalCheckAmount;
    private javax.swing.JLabel lblTotalExpenseAmount;
    private javax.swing.JLabel lblTotalMoneyAmount;
    private javax.swing.JTable tblReports;
    // End of variables declaration//GEN-END:variables
}
