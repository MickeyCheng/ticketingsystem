
package forms;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class frmTKModifyLogs extends javax.swing.JFrame {
ResultSet rs;
Connection conn;
PreparedStatement pstmt;
int getMaxID;
SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
DecimalFormat df = new DecimalFormat ("#.00");
    public frmTKModifyLogs() {
        initComponents();
        doConnect();
        fillComboStaff();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(frmTimeKeepingReport.DISPOSE_ON_CLOSE);
        tblTime.setAutoCreateRowSorter(true);
    }
    private void fillComboStaff(){
        cmbStaff.removeAllItems();
        try{
            pstmt = conn.prepareStatement("SELECT * from tblUserControl where uc_department=? order by uc_username");
            pstmt.setString(1,frmLogin.department);
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbStaff.addItem(rs.getString("uc_username"));
            }
            pstmt.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    public void doConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbticketing","root","root");
    //        conn = DriverManager.getConnection("jdbc:mysql://192.168.1.13/dbticketing","iSoft","iSoft123");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
        }catch(SQLException | ClassNotFoundException e){
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
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        dateTo = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        cmbStaff = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTime = new javax.swing.JTable();
        lblHours = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtLogout = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MODIFY LOGS");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 480, 20));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 480, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("FROM:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 60, 20));

        dateFrom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateFromMouseReleased(evt);
            }
        });
        jPanel1.add(dateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 160, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("TO:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 50, 20));

        dateTo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dateToMouseReleased(evt);
            }
        });
        jPanel1.add(dateTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 160, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("FROM:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 60, 20));

        cmbStaff.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cmbStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 150, 50));

        jButton1.setText("VIEW");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 220, 30));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 480, 10));

        tblTime.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTimeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTime);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, 220));
        jPanel1.add(lblHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 80, 20));
        jPanel1.add(txtLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 140, -1));

        jLabel5.setText("LOGOUT:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 210, 80, 20));
        jPanel1.add(txtLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 210, 140, -1));

        jLabel7.setText("LOGIN:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 180, 80, 20));

        jLabel8.setText("HOURS:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 80, 20));

        lblId.setText("HOURS:");
        jPanel1.add(lblId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 80, 20));
        jPanel1.add(lblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 80, 20));

        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, 140, 40));

        jLabel10.setText("DATE:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 80, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateFromMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateFromMouseReleased

    }//GEN-LAST:event_dateFromMouseReleased

    private void dateToMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateToMouseReleased

    }//GEN-LAST:event_dateToMouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        viewLogs();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void viewLogs(){
    Date getFrom = dateFrom.getDate();
        Date getTo = dateTo.getDate();
        try{
            String queryTime = "Select tk_id,tk_date,tk_login,tk_logout,tk_hours from tbltimekeeping where "
                    + "tk_name=? and tk_date between ? and ? order by tk_date DESC";
            pstmt = conn.prepareStatement(queryTime);
            pstmt.setString(1,cmbStaff.getSelectedItem().toString());
            pstmt.setString(2,sdfDate.format(getFrom));
            pstmt.setString(3, sdfDate.format(getTo));
            rs = pstmt.executeQuery();
            tblTime.setModel(DbUtils.resultSetToTableModel(rs));
            tblTime.getColumnModel().getColumn(0).setHeaderValue("ID");
            tblTime.getColumnModel().getColumn(1).setHeaderValue("DATE");
            tblTime.getColumnModel().getColumn(2).setHeaderValue("LOGIN");
            tblTime.getColumnModel().getColumn(3).setHeaderValue("LOGOUT");
            tblTime.getColumnModel().getColumn(4).setHeaderValue("HOURS");
            pstmt.close();
            txtLogin.setText("");
            txtLogout.setText("");
            lblDate.setText("");
            lblHours.setText("");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void tblTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTimeMouseClicked
        int row = tblTime.getSelectedRow();
        int ba = tblTime.convertRowIndexToModel(row);
       
        lblDate.setText(tblTime.getModel().getValueAt(ba, 1).toString());
        lblId.setText(tblTime.getModel().getValueAt(ba, 0).toString());
        txtLogin.setText(tblTime.getModel().getValueAt(ba, 2).toString());
        lblHours.setText(tblTime.getModel().getValueAt(ba, 4).toString());
        txtLogout.setText(tblTime.getModel().getValueAt(ba, 3).toString());
    }//GEN-LAST:event_tblTimeMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        double getHours =0.0;
        try{
            pstmt = conn.prepareStatement("UPDATE tblTimeKeeping set tk_login=?,tk_logout=?,tk_hours=? where tk_id=?");
            pstmt.setString(1,txtLogin.getText());
            pstmt.setString(2,txtLogout.getText());
            //calculate hours
            Date timeIn = sdfTime.parse(txtLogin.getText());
            Date timeOut = sdfTime.parse(txtLogout.getText());
            getHours = Math.abs(timeIn.getTime() - timeOut.getTime()) / 1000 / 60;
            Double conHours = getHours /60;
            pstmt.setString(3,df.format(conHours));
            pstmt.setString(4,lblId.getText());
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "LOG UPDATED");
            viewLogs();
        }catch(SQLException | ParseException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btnSaveActionPerformed

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
            java.util.logging.Logger.getLogger(frmTKModifyLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmTKModifyLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmTKModifyLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmTKModifyLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmTKModifyLogs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbStaff;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateTo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblHours;
    private javax.swing.JLabel lblId;
    private javax.swing.JTable tblTime;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtLogout;
    // End of variables declaration//GEN-END:variables
}