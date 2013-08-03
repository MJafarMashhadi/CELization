/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationserver.swing;

import celizationrequests.Coordinates;
import celization.mapgeneration.perlinnoise.PerlinNoiseParameters;
import celizationserver.core.ManagerStarter;
import javax.swing.JOptionPane;

/**
 *
 * @author mjafar
 */
public class NewGame extends javax.swing.JDialog {

    private boolean cancelled = true;
    private boolean autoPortSelection = true;
    private MapPreview previewDialogue;

    /**
     * Creates new form NewGame
     */
    public NewGame(final java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        javax.swing.UIManager.put("Slider.paintValue", false);
        initComponents();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                previewDialogue = new MapPreview(parent, false);
                mapRepaint();
                previewDialogue.setVisible(true);
            }
        });
        ManagerStarter.setCenter(this);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    private void mapRepaint() {
        previewDialogue.pnlPreview.setMapSize(Integer.valueOf(spnCols.getValue().toString()), Integer.valueOf(spnRows.getValue().toString()));
        previewDialogue.pnlPreview.setPerlinNoiseParameters(getPerlinNoiseParameters());
        previewDialogue.pnlPreview.repaint();
    }

    public PerlinNoiseParameters getPerlinNoiseParameters() {
        return new PerlinNoiseParameters(
                sldPersistence.getValue() / 100.1,
                sldFrequency.getValue() / 100.1,
                sldAmplitude.getValue(),
                sldOctaves.getValue(),
                Integer.valueOf(spnSeed.getValue().toString()));
    }

    public Coordinates getMapSize() {
        return new Coordinates(Integer.valueOf(spnCols.getValue().toString()), Integer.valueOf(spnRows.getValue().toString()));
    }

    public boolean getAutoPortSelection() {
        return autoPortSelection;
    }

    public Integer getPort() {
        return Integer.valueOf(txtPort.getText());
    }

    public String getGameName() {
        return txtGameName.getText();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblGameName = new javax.swing.JLabel();
        txtGameName = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        chkPortAutoSelect = new javax.swing.JCheckBox();
        pnlMap = new javax.swing.JPanel();
        lblMapSize = new javax.swing.JLabel();
        spnCols = new javax.swing.JSpinner();
        spnRows = new javax.swing.JSpinner();
        lblWidth = new javax.swing.JLabel();
        lblHeight = new javax.swing.JLabel();
        lblX = new javax.swing.JLabel();
        pnlPerlinNoise = new javax.swing.JPanel();
        sldAmplitude = new javax.swing.JSlider();
        lblAmplitude = new javax.swing.JLabel();
        lblPersistence = new javax.swing.JLabel();
        sldPersistence = new javax.swing.JSlider();
        sldFrequency = new javax.swing.JSlider();
        sldOctaves = new javax.swing.JSlider();
        lblFrequency = new javax.swing.JLabel();
        lblOctaves = new javax.swing.JLabel();
        lblSeed = new javax.swing.JLabel();
        spnSeed = new javax.swing.JSpinner();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New game");
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        lblGameName.setText("Game Name");

        lblPort.setText("Listening Port");

        txtPort.setEnabled(false);

        chkPortAutoSelect.setSelected(true);
        chkPortAutoSelect.setText("Choose Automatically");
        chkPortAutoSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPortAutoSelectActionPerformed(evt);
            }
        });

        pnlMap.setBorder(javax.swing.BorderFactory.createTitledBorder("Map properties"));

        lblMapSize.setText("Size");

        spnCols.setModel(new javax.swing.SpinnerNumberModel(160, 90, 500, 10));
        spnCols.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnColsStateChanged(evt);
            }
        });

        spnRows.setModel(new javax.swing.SpinnerNumberModel(120, 90, 500, 10));
        spnRows.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnRowsStateChanged(evt);
            }
        });

        lblWidth.setText("Width");

        lblHeight.setText("Height");

        lblX.setText("X");

        pnlPerlinNoise.setBorder(javax.swing.BorderFactory.createTitledBorder("Perlin Noise Parameters"));

        sldAmplitude.setMaximum(40);
        sldAmplitude.setValue(20);
        sldAmplitude.setEnabled(false);

        lblAmplitude.setText("Amplitude");
        lblAmplitude.setEnabled(false);

        lblPersistence.setText("Persistence");

        sldPersistence.setMaximum(80);
        sldPersistence.setToolTipText("");
        sldPersistence.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldPersistenceStateChanged(evt);
            }
        });

        sldFrequency.setMaximum(30);
        sldFrequency.setMinimum(10);
        sldFrequency.setSnapToTicks(true);
        sldFrequency.setToolTipText("");
        sldFrequency.setValue(18);
        sldFrequency.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldFrequencyStateChanged(evt);
            }
        });

        sldOctaves.setMaximum(8);
        sldOctaves.setMinimum(1);
        sldOctaves.setToolTipText("");
        sldOctaves.setValue(3);
        sldOctaves.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldOctavesStateChanged(evt);
            }
        });

        lblFrequency.setText("Frequency");

        lblOctaves.setText("Octaves");

        lblSeed.setText("Seed");

        spnSeed.setModel(new javax.swing.SpinnerNumberModel(0, 0, 1000, 10));
        spnSeed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnSeedStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlPerlinNoiseLayout = new javax.swing.GroupLayout(pnlPerlinNoise);
        pnlPerlinNoise.setLayout(pnlPerlinNoiseLayout);
        pnlPerlinNoiseLayout.setHorizontalGroup(
            pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPerlinNoiseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOctaves)
                    .addComponent(lblAmplitude)
                    .addComponent(lblPersistence)
                    .addComponent(lblFrequency)
                    .addComponent(lblSeed))
                .addGap(63, 63, 63)
                .addGroup(pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sldOctaves, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldFrequency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldPersistence, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sldAmplitude, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlPerlinNoiseLayout.createSequentialGroup()
                        .addComponent(spnSeed, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlPerlinNoiseLayout.setVerticalGroup(
            pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPerlinNoiseLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sldAmplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAmplitude))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPersistence)
                    .addComponent(sldPersistence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFrequency, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sldFrequency, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sldOctaves, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOctaves, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(7, 7, 7)
                .addGroup(pnlPerlinNoiseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnSeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSeed))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlMapLayout = new javax.swing.GroupLayout(pnlMap);
        pnlMap.setLayout(pnlMapLayout);
        pnlMapLayout.setHorizontalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlPerlinNoise, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlMapLayout.createSequentialGroup()
                        .addComponent(lblMapSize)
                        .addGap(18, 18, 18)
                        .addGroup(pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMapLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(lblWidth)
                                .addGap(58, 58, 58)
                                .addComponent(lblHeight))
                            .addGroup(pnlMapLayout.createSequentialGroup()
                                .addComponent(spnCols, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblX)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spnRows, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMapLayout.setVerticalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMapLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWidth)
                    .addComponent(lblHeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMapSize)
                    .addComponent(spnCols, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spnRows, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblX))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlPerlinNoise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnSave.setText("Create new game");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblGameName)
                                    .addComponent(lblPort))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chkPortAutoSelect))
                                    .addComponent(txtGameName, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(pnlMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGameName)
                    .addComponent(txtGameName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPort)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkPortAutoSelect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkPortAutoSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPortAutoSelectActionPerformed
        autoPortSelection = chkPortAutoSelect.isSelected();
        txtPort.setEnabled(!autoPortSelection);
    }//GEN-LAST:event_chkPortAutoSelectActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        cancelled = false;
        this.dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        boolean confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel making a new game? \n"
                + "All changes will be lost.",
                "Cancel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
        if (!confirmation) {
            return;
        }

        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void sldPersistenceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldPersistenceStateChanged
        mapRepaint();
    }//GEN-LAST:event_sldPersistenceStateChanged

    private void sldFrequencyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldFrequencyStateChanged
        mapRepaint();
    }//GEN-LAST:event_sldFrequencyStateChanged

    private void sldOctavesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldOctavesStateChanged
        mapRepaint();
    }//GEN-LAST:event_sldOctavesStateChanged

    private void spnColsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnColsStateChanged
        mapRepaint();
    }//GEN-LAST:event_spnColsStateChanged

    private void spnRowsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnRowsStateChanged
        mapRepaint();
    }//GEN-LAST:event_spnRowsStateChanged

    private void spnSeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnSeedStateChanged
        mapRepaint();
    }//GEN-LAST:event_spnSeedStateChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        previewDialogue.dispose();
    }//GEN-LAST:event_formWindowClosed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkPortAutoSelect;
    private javax.swing.JLabel lblAmplitude;
    private javax.swing.JLabel lblFrequency;
    private javax.swing.JLabel lblGameName;
    private javax.swing.JLabel lblHeight;
    private javax.swing.JLabel lblMapSize;
    private javax.swing.JLabel lblOctaves;
    private javax.swing.JLabel lblPersistence;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblSeed;
    private javax.swing.JLabel lblWidth;
    private javax.swing.JLabel lblX;
    private javax.swing.JPanel pnlMap;
    private javax.swing.JPanel pnlPerlinNoise;
    private javax.swing.JSlider sldAmplitude;
    private javax.swing.JSlider sldFrequency;
    private javax.swing.JSlider sldOctaves;
    private javax.swing.JSlider sldPersistence;
    private javax.swing.JSpinner spnCols;
    private javax.swing.JSpinner spnRows;
    private javax.swing.JSpinner spnSeed;
    private javax.swing.JTextField txtGameName;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables
}
