
package forms;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
public class frmLPOGenerator extends javax.swing.JFrame {
ResultSet rs;
Connection conn;
PreparedStatement pstmt;
DecimalFormat df = new DecimalFormat("#.000");
SimpleDateFormat sdfDate = new SimpleDateFormat("MMM dd,yyyy");
int getMaxQuoteID;
boolean add,edit;
String getItemCode, getImagePath;
    public frmLPOGenerator() {
        initComponents();
        doConnect();
        fillComboQty();
        fillTableProduct();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(frmClient.DISPOSE_ON_CLOSE);
        fillCompanyCombo();
        txtClientAddress.setLineWrap(true);
        txtItemName.setLineWrap(true);
        txtShowDescription.setLineWrap(true);
        txtClientAddress.setWrapStyleWord(true);
        txtItemName.setWrapStyleWord(true);
        txtShowDescription.setWrapStyleWord(true);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {listenSearch();}
            @Override
            public void removeUpdate(DocumentEvent e) {listenSearch();}
            @Override
            public void changedUpdate(DocumentEvent e) {listenSearch();}
        });
        cmbClientCompany.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {fillComboCompanyContactPerson();}
        });
    }
    private void fillCompanyCombo(){
        cmbClientCompany.removeAllItems();
        try{
            pstmt =conn.prepareStatement("SELECT DISTINCT cl_company from tblClientList order by cl_company");
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbClientCompany.addItem(rs.getString("cl_company"));
            }
            pstmt.close();
            cmbClientCompany.setSelectedIndex(-1);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void fillTextBoxes(){
        try{
            pstmt = conn.prepareStatement("Select * from tblClientList where cl_company=? and cl_contactperson=?");
            pstmt.setString(1,cmbClientCompany.getSelectedItem().toString());
            pstmt.setString(2,cmbClientName.getSelectedItem().toString());
            rs = pstmt.executeQuery();
            if (rs.next()){
                txtClientAddress.setText(rs.getString(4));
                txtClientContact.setText(rs.getString(5));
                txtClientEmail.setText(rs.getString(6));
            }
            pstmt.close();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        } 
    }
    private void fillComboCompanyContactPerson(){
        cmbClientName.removeAllItems();
        try{
            pstmt =conn.prepareStatement("SELECT * from tblClientList where cl_company=? order by cl_contactperson");
            pstmt.setString(1, cmbClientCompany.getSelectedItem().toString());
            rs = pstmt.executeQuery();
            while (rs.next()){
                cmbClientName.addItem(rs.getString(3));
            }
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();
        } 
    }
    private void fillTableProduct(){
        try{
            pstmt = conn.prepareStatement("SELECT * from tblProduct order by pr_id");
            rs = pstmt.executeQuery();
            tblProduct.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            tblProduct.getColumnModel().getColumn(0).setHeaderValue("ID");
            tblProduct.getColumnModel().getColumn(1).setHeaderValue("ITEM");
            tblProduct.getColumnModel().getColumn(2).setHeaderValue("PRICE");
        }catch(SQLException e){
            e.getMessage();
        }    
    }   
    private void fillComboQty(){
        cmbQuantity.removeAllItems();
        for(int i=1;i<51;i++){
            cmbQuantity.addItem(String.valueOf(i));
        }
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        db = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtClientAddress = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtClientContact = new javax.swing.JTextField();
        txtClientEmail = new javax.swing.JTextField();
        cmbClientName = new javax.swing.JComboBox<>();
        cmbClientCompany = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblQuote = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtItemName = new javax.swing.JTextArea();
        jSeparator3 = new javax.swing.JSeparator();
        cmbQuantity = new javax.swing.JComboBox<>();
        txtPrice = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblTotalPrice = new javax.swing.JLabel();
        lblTotalQty = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();
        jSeparator5 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtShowDescription = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        lblAttachment = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CLIENT INFORMATION");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 900, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Address:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 90, 30));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 900, 10));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Name:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, 30));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Company:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 90, 30));

        txtClientAddress.setColumns(20);
        txtClientAddress.setRows(5);
        jScrollPane1.setViewportView(txtClientAddress);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 450, 60));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Phone/Mobile:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 40, 90, 30));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Email:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 90, 30));

        txtClientContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClientContactActionPerformed(evt);
            }
        });
        jPanel3.add(txtClientContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 40, 230, 30));
        jPanel3.add(txtClientEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 80, 230, 30));

        jPanel3.add(cmbClientName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 410, 30));

        jPanel3.add(cmbClientCompany, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 450, 30));

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, 30, 30));

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

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 940, 210));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LOCAL PURCHASE ORDER");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 20, 940, -1));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 940, 20));

        jPanel5.setBackground(new java.awt.Color(102, 102, 102));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblQuote.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ITEM CODE", "DESCRIPTION", "QTY", "UNIT PRICE", "TOTAL PRICE"
            }
        ));
        tblQuote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQuoteMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblQuote);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 40, -1, 140));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("ITEM QUOTE LIST");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 10, 440, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("SEARCH:");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 30));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("QUANTITY:");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 90, 30));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("PRICE:");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 90, 30));
        jPanel5.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 320, 30));

        txtItemName.setColumns(20);
        txtItemName.setRows(5);
        jScrollPane3.setViewportView(txtItemName);

        jPanel5.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 320, -1));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel5.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 20, 340));

        cmbQuantity.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(cmbQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 160, 30));
        jPanel5.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 160, 30));

        jLabel12.setText("TOTAL QUANTITY:");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 190, 130, -1));

        jLabel13.setText("TOTAL PRICE:");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 210, 130, -1));

        lblTotalPrice.setText("0.000");
        jPanel5.add(lblTotalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 210, 70, -1));

        lblTotalQty.setText("0");
        jPanel5.add(lblTotalQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 190, 70, -1));
        jPanel5.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 300, 490, 10));

        jButton2.setText("PROCESS LPO");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 310, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("ITEM NAME:");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 90, 30));

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblProduct);

        jPanel5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 410, 70));
        jPanel5.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 430, -1));

        jButton1.setText("ADD TO QUOTE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 120, 70));

        txtShowDescription.setColumns(20);
        txtShowDescription.setRows(5);
        jScrollPane5.setViewportView(txtShowDescription);

        jPanel5.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 200, 210, 90));

        jButton4.setText("ATTACHMENT");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(633, 310, 130, -1));

        lblAttachment.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAttachment.setText("ATTACHMENT GOES HERE");
        jPanel5.add(lblAttachment, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 230, 210, 60));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 940, 360));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 960, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductMouseClicked
        int row = tblProduct.getSelectedRow();
        int ba = tblProduct.convertRowIndexToModel(row);
        String tblClick = tblProduct.getModel().getValueAt(ba, 0).toString();
        String fetchData = "Select * from tblProduct where pr_id=?";
        try{
            pstmt = conn.prepareStatement(fetchData);
            pstmt.setString(1,tblClick);
            rs=pstmt.executeQuery();
            if (rs.next()){
                txtItemName.setText(rs.getString("pr_name"));
                txtPrice.setText(rs.getString("pr_price"));
                getItemCode = rs.getString("pr_id");
            }
            pstmt.close();
            System.out.println(getItemCode);
        }catch(SQLException e){
            e.getMessage();
        }
    }//GEN-LAST:event_tblProductMouseClicked
    private void getTotalAmtAndQty(){
        double getSumAmount,showTotalAmountLPO=0;
        for(int i =0; i<tblQuote.getRowCount();i++){
            getSumAmount = Double.parseDouble(tblQuote.getValueAt(i,4).toString());
            showTotalAmountLPO+= getSumAmount;
        }
        lblTotalPrice.setText(String.valueOf(df.format(showTotalAmountLPO)));
        
        int getSumQty,showTotalQty=0;
        for(int i =0; i<tblQuote.getRowCount();i++){
            getSumQty = Integer.parseInt(tblQuote.getValueAt(i,2).toString());
            showTotalQty+= getSumQty;
        }
        lblTotalQty.setText(String.valueOf(showTotalQty));
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DefaultTableModel modelQuote = (DefaultTableModel)tblQuote.getModel();
        double showTotalUnit = Double.valueOf(cmbQuantity.getSelectedItem().toString()) * Double.valueOf(txtPrice.getText());
        Object addRow[] = {getItemCode,txtItemName.getText(),cmbQuantity.getSelectedItem().toString(),txtPrice.getText(),df.format(showTotalUnit)};
        modelQuote.addRow(addRow);
        getTotalAmtAndQty();
    }//GEN-LAST:event_jButton1ActionPerformed
    private void getNextQuoteID(){
        try{
            pstmt = conn.prepareStatement("SELECT * from tblLpoGenerator order by lg_id DESC LIMIT 1");
            rs = pstmt.executeQuery();
            if (rs.next()){
                getMaxQuoteID = rs.getInt("lg_id");
                getMaxQuoteID++;
            }else{
                getMaxQuoteID=1;
            }
            pstmt.close();
        }catch(SQLException e){
            e.getMessage();
        }
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        String insertQuote = "INSERT INTO tblLpoGenerator (lg_id,lg_supplierName,lg_supplierCompany,lg_supplierAddress,"
                + "lg_supplierContact,lg_supplierEmail,lg_itemCode,lg_itemName,lg_quantity,lg_price,"
                + "lg_totalPrice,lg_totalQuantity,lg_totalLpoAmount,lg_lpoDate)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            for(int i=0; i<tblQuote.getRowCount();i++){
                pstmt = conn.prepareStatement(insertQuote);
                pstmt.setInt(1,getMaxQuoteID);
                pstmt.setString(2,cmbClientName.getSelectedItem().toString());
                pstmt.setString(3,cmbClientCompany.getSelectedItem().toString());
                pstmt.setString(4,txtClientAddress.getText());
                pstmt.setString(5,txtClientContact.getText());
                pstmt.setString(6,txtClientEmail.getText());
                pstmt.setString(7,tblQuote.getValueAt(i, 0).toString());
                pstmt.setString(8,tblQuote.getValueAt(i, 1).toString());
                pstmt.setString(9,tblQuote.getValueAt(i, 2).toString());
                pstmt.setString(10,tblQuote.getValueAt(i, 3).toString());
                pstmt.setString(11,tblQuote.getValueAt(i, 4).toString());
                pstmt.setString(12, lblTotalQty.getText());
                pstmt.setString(13, lblTotalPrice.getText());
                Date dateToday = new Date();
                pstmt.setString(14,sdfDate.format(dateToday));
                pstmt.execute();
                pstmt.close();
            }
            JOptionPane.showMessageDialog(this, "QUOTATION CREATED AND SAVED");
            printQuote();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }            
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblQuoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQuoteMouseClicked
        int row =tblQuote.getSelectedRow();
        int ba = tblQuote.convertRowIndexToModel(row);
        String tblClick  = tblQuote.getModel().getValueAt(ba, 1).toString();
        txtShowDescription.setText(tblClick);

    }//GEN-LAST:event_tblQuoteMouseClicked

    private void txtClientContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClientContactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClientContactActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        getNextQuoteID();
        fillTextBoxes();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
      FileFilter ft = new FileNameExtensionFilter("images .pdf,.jpg,.png", "jpg","png","pdf");
        File outputFile  = new File("src\\attachments\\attachment" + "LPO" + getMaxQuoteID);
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
    }//GEN-LAST:event_jButton4ActionPerformed
    private void printQuote(){
            Date dateToday = new Date();
            Map param = new HashMap();
            param.put("showQuoteId", String.valueOf(getMaxQuoteID));
            param.put("showDate",sdfDate.format(dateToday));
            param.put("showAttachment",getImagePath);
            System.out.println("image ay " + getImagePath);
            try{
                conn.close();
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://192.168.100.2/dbticketing","iSoft","iSoft123");
                JasperDesign jd = JRXmlLoader.load(new File("src\\Reports\\repLPOGenerator.jrxml"));
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, param,conn);
                JasperViewer.viewReport(jp,false);
            }catch(ClassNotFoundException | SQLException | JRException e){
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
            this.dispose();
    }
    
    private void listenSearch(){
        try{
            String searchQuery = "Select * from tblProduct where pr_name like ?";
            pstmt = conn.prepareStatement(searchQuery);
            pstmt.setString(1, "%"+txtSearch.getText()+"%");
            rs = pstmt.executeQuery();
            tblProduct.setModel(DbUtils.resultSetToTableModel(rs));
            pstmt.close();
            tblProduct.getColumnModel().getColumn(0).setHeaderValue("ID");
            tblProduct.getColumnModel().getColumn(1).setHeaderValue("ITEM");
            tblProduct.getColumnModel().getColumn(2).setHeaderValue("PRICE");
            }catch(SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }          
    }
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
            java.util.logging.Logger.getLogger(frmLPOGenerator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmLPOGenerator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmLPOGenerator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmLPOGenerator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmLPOGenerator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbClientCompany;
    private javax.swing.JComboBox<String> cmbClientName;
    private javax.swing.JComboBox<String> cmbQuantity;
    private javax.swing.JFileChooser db;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblAttachment;
    private javax.swing.JLabel lblTotalPrice;
    private javax.swing.JLabel lblTotalQty;
    private javax.swing.JTable tblProduct;
    private javax.swing.JTable tblQuote;
    private javax.swing.JTextArea txtClientAddress;
    private javax.swing.JTextField txtClientContact;
    private javax.swing.JTextField txtClientEmail;
    private javax.swing.JTextArea txtItemName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextArea txtShowDescription;
    // End of variables declaration//GEN-END:variables
}
