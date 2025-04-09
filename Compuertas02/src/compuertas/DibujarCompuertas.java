package compuertas;

import java.awt.Color;
import javax.swing.BorderFactory;

public class DibujarCompuertas extends javax.swing.JFrame {
    
    private final MiPanel p;
    private int yPosicion = 50; // Posición inicial de las compuertas

    public DibujarCompuertas() {
        initComponents();
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ajustarTamañoPanel();
            }

            private void ajustarTamañoPanel() {
                p.setSize(pAreaCliente.getWidth(), pAreaCliente.getHeight());
                p.revalidate();
                p.repaint();
            }
        });

        p = new MiPanel();
        p.setSize(pAreaCliente.getSize());
        p.setBorder(BorderFactory.createEtchedBorder());
        p.setBackground(Color.white);
        pAreaCliente.add(p);
        pAreaCliente.revalidate();
        pAreaCliente.repaint();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        pAreaCliente = new javax.swing.JPanel();
        JbAND = new javax.swing.JButton();
        JbNOT = new javax.swing.JButton();
        JbOR = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pAreaCliente.setBackground(new java.awt.Color(255, 255, 255));
        pAreaCliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout pAreaClienteLayout = new javax.swing.GroupLayout(pAreaCliente);
        pAreaCliente.setLayout(pAreaClienteLayout);
        pAreaClienteLayout.setHorizontalGroup(
            pAreaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pAreaClienteLayout.setVerticalGroup(
            pAreaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );

        JbAND.setBackground(new java.awt.Color(204, 255, 255));
        JbAND.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compuertas/AND.png"))); // NOI18N
        JbAND.setText("AND");
        JbAND.setAutoscrolls(true);
        JbAND.setBorder(null);
        JbAND.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbANDActionPerformed(evt);
            }
        });

        JbNOT.setBackground(new java.awt.Color(255, 51, 51));
        JbNOT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compuertas/NOT.png"))); // NOI18N
        JbNOT.setText("NOT");
        JbNOT.setBorder(null);
        JbNOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbNOTActionPerformed(evt);
            }
        });

        JbOR.setBackground(new java.awt.Color(204, 255, 204));
        JbOR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compuertas/OR.png"))); // NOI18N
        JbOR.setText(" OR");
        JbOR.setBorder(null);
        JbOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbORActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 0, 204));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compuertas/rgb_10069326.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 133, 73));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compuertas/on-off_11798155 (1).png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 153));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/compuertas/XOR.png"))); // NOI18N
        jButton3.setText("XOR");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");
        jMenuBar2.add(jMenu1);

        jMenu3.setText("Edit");
        jMenuBar2.add(jMenu3);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(JbAND, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(JbOR, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(JbNOT, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
            .addComponent(pAreaCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JbAND, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JbOR, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JbNOT, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pAreaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JbANDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbANDActionPerformed
        // TODO add your handling code here:
       p.seleccionarCompuerta(new AND (0,0));
    }//GEN-LAST:event_JbANDActionPerformed

    private void JbORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbORActionPerformed
        // TODO add your handling code here:
        p.seleccionarCompuerta(new OR (0,0) );
    }//GEN-LAST:event_JbORActionPerformed

    private void JbNOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbNOTActionPerformed
        // TODO add your handling code here:
        p.seleccionarCompuerta(new NOT (0,0));
    }//GEN-LAST:event_JbNOTActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       p.seleccionarCompuerta(new LED (0,0));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        p.seleccionarCompuerta(new Switch (0,0));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        p.seleccionarCompuerta(new Xor (0,0));
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(DibujarCompuertas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DibujarCompuertas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DibujarCompuertas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DibujarCompuertas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DibujarCompuertas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JbAND;
    private javax.swing.JButton JbNOT;
    private javax.swing.JButton JbOR;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPanel pAreaCliente;
    // End of variables declaration//GEN-END:variables
}
