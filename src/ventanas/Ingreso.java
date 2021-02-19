/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import java.awt.event.*;
import javax.swing.*;
import SecuGen.FDxSDKPro.jni.*;
import clases.Conexion;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import static jssc.SerialPort.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author alex_
 */
public class Ingreso extends javax.swing.JFrame {

    String user;
    private long deviceName;
    private long devicePort;
    private JSGFPLib fplib = null;  
    private long ret;
    private boolean bLEDOn;
    private byte[] regMin1 = new byte[400];
    private byte[] regMin2 = new byte[400];
    private byte[] vrfMin  = new byte[400];
    private byte[] m  = new byte[400];
    private SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
    private BufferedImage imgRegistration1;
    private BufferedImage imgRegistration2;
    private BufferedImage imgVerification;
    private boolean r1Captured = false;
    private boolean r2Captured = false;
    private boolean v1Captured = false;
    private static int MINIMUM_QUALITY = 60;       //User defined
    private static int MINIMUM_NUM_MINUTIAE = 20;  //User defined
    private static int MAXIMUM_NFIQ = 2;           //User defined
    private boolean aux = true;
    private boolean cliente_reconocido = false;
    PanamaHitek_Arduino ino;
    Timer timer;
    int short_delay = 3000;

    /**
     * Creates new form Ingreso
     */
    public Ingreso() {
        setIconImage(getIconImage());
        initComponents();
        
        user = Login.user;
        ino = new PanamaHitek_Arduino();
        try {
            ino.arduinoTX("COM5", 9600);
        } catch (Exception e) {
            System.out.println("error arduino");
        }
        
        
        setTitle("Registrar nuevo cliente - Sesión de " + user);

        setResizable(false);
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        URL url = Login.class.getResource("/wallpaperPrincipal.jpg");
        ImageIcon wallpaper = new ImageIcon(url);
        Icon icono = new ImageIcon(wallpaper.getImage().getScaledInstance(jLabel_Wallpaper.getWidth(),
                jLabel_Wallpaper.getHeight(), Image.SCALE_DEFAULT));
        jLabel_Wallpaper.setIcon(icono);
        this.repaint();
        
        
        fplib = new JSGFPLib();       
        ret = fplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
        if ((fplib != null) && (ret  == SGFDxErrorCode.SGFDX_ERROR_NONE))
        {
            this.jLabelStatus.setText("JSGFPLib Initialization Success");
            ret = fplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
            if (ret != SGFDxErrorCode.SGFDX_ERROR_NONE)
            {
                this.jLabelStatus.setText("OpenDevice() Success [" + ret + "]");       
                ret = fplib.GetDeviceInfo(deviceInfo);
                if (ret == SGFDxErrorCode.SGFDX_ERROR_NONE)
                {
                    
                    imgRegistration1 = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
                    imgRegistration2 = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
                    imgVerification = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
                    
                }
                else
                    this.jLabelStatus.setText("GetDeviceInfo() Error [" + ret + "]");                                
            }
            else
                this.jLabelStatus.setText("OpenDevice() Error [" + ret + "]");                                
        }
        else{
            this.jLabelStatus.setText("JSGFPLib Initialization Failed");  
        }   
        
        
    }

    @Override
    public Image getIconImage() {
        URL url = Login.class.getResource("/icon.png");
        Image retValue = Toolkit.getDefaultToolkit().getImage(url);
        return retValue;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_NombreUsuario = new javax.swing.JLabel();
        d = new javax.swing.JLabel();
        mensaje = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jLabelVerifyImage = new javax.swing.JLabel();
        jProgressBarV1 = new javax.swing.JProgressBar();
        jLabel_footer = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        n = new javax.swing.JLabel();
        jLabel_Wallpaper = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_NombreUsuario.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel_NombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_NombreUsuario.setText("Sistema de ingreso");
        jLabel_NombreUsuario.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jLabel_NombreUsuarioComponentShown(evt);
            }
        });
        getContentPane().add(jLabel_NombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(211, 12, -1, -1));

        d.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        d.setForeground(new java.awt.Color(204, 204, 204));
        d.setText("jLabel1");
        getContentPane().add(d, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        mensaje.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        mensaje.setForeground(new java.awt.Color(255, 255, 255));
        mensaje.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mensaje.setText("jLabel1");
        getContentPane().add(mensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, -1, -1));

        date.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        date.setForeground(new java.awt.Color(255, 255, 255));
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        date.setText("jLabel1");
        getContentPane().add(date, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, -1, -1));

        jLabelStatus.setText("Click Initialize Button ...");
        jLabelStatus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        getContentPane().add(jLabelStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 301, 610, 30));

        jLabelVerifyImage.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jLabelVerifyImage.setMinimumSize(new java.awt.Dimension(130, 150));
        jLabelVerifyImage.setPreferredSize(new java.awt.Dimension(130, 150));
        getContentPane().add(jLabelVerifyImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 94, -1, -1));

        jProgressBarV1.setForeground(new java.awt.Color(0, 51, 153));
        getContentPane().add(jProgressBarV1, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 244, 130, -1));

        jLabel_footer.setText("                       Xtreme Gym ®");
        getContentPane().add(jLabel_footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, -1, -1));

        name.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        name.setForeground(new java.awt.Color(255, 255, 255));
        name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        name.setText("jLabel1");
        getContentPane().add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, -1, -1));

        n.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        n.setForeground(new java.awt.Color(204, 204, 204));
        n.setText("jLabel1");
        getContentPane().add(n, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        getContentPane().add(jLabel_Wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_NombreUsuarioComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabel_NombreUsuarioComponentShown
        
    }//GEN-LAST:event_jLabel_NombreUsuarioComponentShown

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                huella();
                if (cliente_reconocido){
                    cliente_reconocido = false;
                    try {
                        ino.sendData("1");
                    } catch (ArduinoException | SerialPortException ex) {
                        Logger.getLogger(Ingreso.class.getName()).log(Level.SEVERE, null, ex);
                    }
 
                }
            }
        };
        
        timer = new Timer(short_delay ,taskPerformer);
        timer.start();

        
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        fplib.CloseDevice();
        fplib.Close();
        
        try {
            ino.killArduinoConnection();
        } catch (Exception e) {
            
        }
        
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public void dormir () {
        long delay = 5000;
        try {
            timer.setDelay(short_delay);
        }catch(Exception e){
            System.out.println("Error dormir");
        };
    }
    public void huella(){
                   
            int[] quality = new int[1];
            int[] numOfMinutiae = new int[1];
            byte[] imageBuffer1 = ((java.awt.image.DataBufferByte) imgVerification.getRaster().getDataBuffer()).getData();
            long iError = SGFDxErrorCode.SGFDX_ERROR_NONE;
            
            
            iError = fplib.GetImageEx(imageBuffer1,short_delay, 0, 50);
            fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
            this.jProgressBarV1.setValue(quality[0]);
            SGFingerInfo fingerInfo = new SGFingerInfo();
            fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
            fingerInfo.ImageQuality = quality[0];
            fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
            fingerInfo.ViewNumber = 1;

            if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE)
            {
                this.jLabelVerifyImage.setIcon(new ImageIcon(imgVerification.getScaledInstance(130,150,Image.SCALE_DEFAULT)));
                if (quality[0] < MINIMUM_QUALITY)
                this.jLabelStatus.setText("GetImageEx() Realizado [" + ret + "] pero la calidad es: [" + quality[0] + "]. Por favor, intente nuevamente");
                else
                {
                    this.jLabelStatus.setText("GetImageEx() Realizado [" + ret + "]");

                    iError = fplib.CreateTemplate(fingerInfo, imageBuffer1, vrfMin);
                    if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE)
                    {
                        long nfiqvalue;
                        long ret2 = fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, imageBuffer1, quality);
                        nfiqvalue = fplib.ComputeNFIQ(imageBuffer1, deviceInfo.imageWidth, deviceInfo.imageHeight);
                        ret2 = fplib.GetNumOfMinutiae(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400, vrfMin, numOfMinutiae);

                        if ((quality[0] >= MINIMUM_QUALITY) && (nfiqvalue <= MAXIMUM_NFIQ) && (numOfMinutiae[0] >= MINIMUM_NUM_MINUTIAE))
                        {
                            this.jLabelStatus.setText("Captura cerificada PASS QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue + "] Minutiae[" + numOfMinutiae[0] + "]");
                            v1Captured = true;

                            long secuLevel = 5;
                            boolean[] matched = new boolean[1];
                            int id = 0;
                            String nombre;
                            matched[0] = false;

                            try {
                                Connection cn = Conexion.conectar();
                                PreparedStatement pst = cn.prepareStatement("select nombre_cliente, id_cliente, huella_id from clientes ");
                                ResultSet rs = pst.executeQuery();

                                while (rs.next()) {
                                    m = rs.getBytes("huella_id");
                                    iError = fplib.MatchTemplate(m, vrfMin, secuLevel, matched);

                                    if (iError == SGFDxErrorCode.SGFDX_ERROR_NONE){
                                        if (matched[0]){

                                            this.jLabelStatus.setText( "Cliente reconocido (1er intento)");
                                            id = rs.getInt("id_cliente");
                                            nombre = rs.getString("nombre_cliente");
                                            
                                            pst = cn.prepareStatement("select * from equipos where id_cliente = ? and fecha_fin >= CURRENT_DATE");
                                            pst.setInt(1, id);
                                            
                                            rs = pst.executeQuery();
                                            if (rs.next()){
                                                System.out.println("cliente reconocido: " + nombre);
                                                matched[0] = false;
                                                
                                                cliente_reconocido = true;
                                                
                                                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                                                String fecha = f.format(rs.getDate("fecha_fin"));
                                                
                                                imprimir_cliente(nombre, fecha);

                                                Timer t2 = new javax.swing.Timer(5000, new ActionListener(){
                                                        public void actionPerformed(ActionEvent e){
                                                        limpiar("Identifíquese");
                                                    }
                                                });
                                                t2.start();
                                                t2.setRepeats(false);
                                                
                                            }
                                            
                                            if (!cliente_reconocido){
                                                this.jLabelStatus.setText( "Mensualidad vencida! :c ");
                                                limpiar("Mensualidad vencida");
                                            }
                                            
                                            break;
                                        } else {
                                            this.jLabelStatus.setText( "Cliente no registrado. Por favor, solicite ayuda.");
                                            limpiar("No Registrado");
                                        }
                                    } else {
                                        System.out.println("Error en la lectura de la huella dactilar. Por favor, intente nuevamente.");
                                        limpiar("Intente nuevamente");
                                    }

                                }

                            } catch (SQLException e) {
                                System.err.println("Error en el llenado de la tabla.");
                                
                            } 

                        }
                        else
                        {
                            this.jLabelStatus.setText("Error en la lectura QC. Quality[" + quality[0] + "] NFIQ[" + nfiqvalue + "] Minutiae[" + numOfMinutiae[0] + "]");
                            limpiar("Intente nuevamente");
                        }
                    }
                    else
                    this.jLabelStatus.setText("CreateTemplate() Error : " + iError);
                }
            }
            else{
            this.jLabelStatus.setText("Bien venido. Coloque su dedo en el sensor antes de ingresar");
            limpiar("Identifíquese");
            }

        
    }
    
    public void imprimir_cliente (String nombre, String fecha){
        n.setText("Cliente: ");
        d.setText("Vigencia: ");
        name.setText(nombre);
        date.setText(fecha);
        mensaje.setText("Bienvenido!!");
    }
    
    public void limpiar (String msg){
        n.setText("");
        d.setText("");
        name.setText("");
        date.setText("");
        mensaje.setText(msg);
    }
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
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        SwingUtilities.invokeLater(() -> new Ingreso().setVisible(true));
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ingreso().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel d;
    private javax.swing.JLabel date;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelVerifyImage;
    private javax.swing.JLabel jLabel_NombreUsuario;
    private javax.swing.JLabel jLabel_Wallpaper;
    private javax.swing.JLabel jLabel_footer;
    private javax.swing.JProgressBar jProgressBarV1;
    private javax.swing.JLabel mensaje;
    private javax.swing.JLabel n;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables
}
