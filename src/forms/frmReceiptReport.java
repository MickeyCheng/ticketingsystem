package forms;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
public class frmReceiptReport extends javax.swing.JFrame {
ResultSet rs;
PreparedStatement pstmt;
Connection conn;
SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
Date getDateFrom, getToDate;
String tblClick;
    public frmReceiptReport() {
        initComponents();
        doConnect();
        fillTable();
        setDefaultCloseOperation(frmReceiptReport.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void doConnect(){
    try{
        Class.forName("com.mysql.jdbc.Driver");
//        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
//        conn = DriverManager.getConnection("jdbc:mysql://192.168.1.13/dbticketing","iSoft","iSoft123");
        
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
    }catch(SQLException | ClassNotFoundException e){
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }
    private void getDate(){
        getDateFrom = dateFrom.getDate();
        getToDate = dateTo.getDate();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        dateTo = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvoice = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnPrint = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateFromMouseReleased(evt);
            }
        });
        jPanel2.add(dateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 160, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("INVOICE REPORTS");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 500, 40));

        dateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateToMouseReleased(evt);
            }
        });
        jPanel2.add(dateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 170, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("FROM:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 60, 30));

        btnSearch.setText("SEARCH");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        jPanel2.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 150, 30));

        tblInvoice.setModel(new javax.swing.table.DefaultTableModel(
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
        tblInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvoiceMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblInvoice);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 480, 170));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("TO:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 50, 30));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 500, 10));

        btnPrint.setText("PRINT");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jPanel2.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 320, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateFromMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateFromMouseReleased

    }//GEN-LAST:event_dateFromMouseReleased

    private void dateToMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateToMouseReleased

    }//GEN-LAST:event_dateToMouseReleased
    private void sortTable(){
        tblInvoice.getColumnModel().getColumn(0).setHeaderValue("ID");
        tblInvoice.getColumnModel().getColumn(1).setHeaderValue("DATE");
        tblInvoice.getColumnModel().getColumn(2).setHeaderValue("CONTACT PERSON");
        tblInvoice.getColumnModel().getColumn(3).setHeaderValue("COMPANY");
        tblInvoice.getColumnModel().getColumn(4).setHeaderValue("INVOICE AMOUNT");
    }
    private void fillTable(){
        try{
            pstmt = conn.prepareStatement("Select rg_id,rg_quotedate,rg_clientName,rg_clientCompany,"
                    + "rg_totalinvoiceamount from tblReceiptGenerator order by rg_id DESC");
            rs = pstmt.executeQuery();
            tblInvoice.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTable();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        getDate();
        try{
            pstmt = conn.prepareStatement("Select rg_id,rg_quotedate,rg_clientName,rg_clientCompany,"
                    + "rg_totalinvoiceamount, from tblReceiptGenerator where rg_quotedate between ? and ?");
            rs = pstmt.executeQuery();
            tblInvoice.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            sortTable();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        Map param = new HashMap();
        param.put("showQuoteID",tblClick);
        try{
            conn.close();
            Class.forName("com.mysql.jdbc.Driver");
            //            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
            JasperDesign jd = JRXmlLoader.load(new File("src\\reports\\repReceiptSelect.jrxml"));
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
            JasperViewer.viewReport(jp,false);
        }catch(ClassNotFoundException | SQLException | JRException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void tblInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvoiceMouseClicked
        int row = tblInvoice.getSelectedRow();
        int ba = tblInvoice.convertRowIndexToModel(row);
        tblClick = tblInvoice.getModel().getValueAt(ba, 0).toString();
        System.out.println("add " + tblClick);
//        String fetchData ="Select * from tblQuoteGenerator where rg_id=?";
//        try{
//            pstmt = conn.prepareStatement(fetchData);
//            pstmt.setString(1,tblClick);
//            rs = pstmt.executeQuery();
//            pstmt.close();
//        }catch(SQLException e){
//            JOptionPane.showMessageDialog(this, e.getMessage());
//        }
    }//GEN-LAST:event_tblInvoiceMouseClicked

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
            java.util.logging.Logger.getLogger(frmReceiptReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmReceiptReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmReceiptReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmReceiptReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmReceiptReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearch;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblInvoice;
    // End of variables declaration//GEN-END:variables
}
