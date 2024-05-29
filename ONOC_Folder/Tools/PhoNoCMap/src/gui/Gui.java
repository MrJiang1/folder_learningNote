/*******************************************************************************
 * Copyright (c) 2015, Edoardo Fusella
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in
 *         the documentation and/or other materials provided with the
 *         distribution.
 *       * Neither the name of the University of Naples Federico II nor the names of its
 *         contributors may be used to endorse or promote products derived
 *         from this software without specific prior written permission.
 *   
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 *   IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 *   TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *   PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *   HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 *   TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import routers.Crux;
import routers.ODOR;
import routers.OXY;
import routing.XYMesh;
import routing.XYTorusFolded;
import routing.XYTorusUnfolded;
import routing.XYTorusUnfoldedOptimized;
import topologies.Mesh;
import topologies.TorusFolded;
import topologies.TorusUnfolded;
import topologies.TorusUnfoldedOptimized;
import main.Configuration;
import main.Globals;
import main.InputConfigurationReader;
import main.OptimizationMain;
import mapping_strategies.GeneticMapping;
import mapping_strategies.ListMapping;
import mapping_strategies.RandomMapping;

/**
 * The GUI class.
 * 
 * @author Edoardo Fusella, University of Naples Federico II
 */
public class Gui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		final InputConfigurationReader cr = new InputConfigurationReader("default_parameters.xml");
		cr.acquireData();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui(cr);
					frame.setVisible(true);
					// frame.contentPane.getComponent(0).setSize(frame.contentPane.getSize());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @param cr The file reader for the configuration file.
	 */
	public Gui(final InputConfigurationReader cr) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int) (screenSize.getWidth() * 0.35);
		int centerY = (int) (screenSize.getHeight() * 0.25);
		int dimX = (int) (screenSize.getWidth() * 0.2);
		int dimY = (int) (screenSize.getHeight() * 0.5);

		// For the GUI builder
		setBounds(100, 100, 325, 380);
		setBounds(centerX, centerY, dimX, dimY);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 5, 299, 331);
		contentPane.add(tabbedPane);

		final JPanel panelOpticalcoefficients = new JPanel();
		final JPanel panelPowerLoss = new JPanel();
		final JPanel panelMiscellaneous = new JPanel();
		final JPanel panelCrosstalk = new JPanel();
		final JPanel panelNoCArchitecture = new JPanel();
		final JPanel panelTopology = new JPanel();
		final JPanel panelRouting = new JPanel();
		final JPanel panelRouter = new JPanel();
		final JPanel panelChip = new JPanel();
		final JPanel panelApplicationMapping = new JPanel();
		final JPanel panelApplication = new JPanel();
		final JPanel panelMapping = new JPanel();
		final JPanel panelExecution = new JPanel();

		tabbedPane.addTab("Optical Coefficients", null, panelOpticalcoefficients, null);
		panelOpticalcoefficients.setLayout(null);

		panelPowerLoss.setBorder(new BevelBorder(BevelBorder.LOWERED));
		panelPowerLoss.setBounds(10, 10, 274, 169);
		panelOpticalcoefficients.add(panelPowerLoss);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[] { 210, 60, 0 };
		gbl_panel_5.rowHeights = new int[] { 20, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_5.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_5.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelPowerLoss.setLayout(gbl_panel_5);

		final JLabel lblPowerLoss = new JLabel("Power loss");
		lblPowerLoss.setHorizontalAlignment(SwingConstants.CENTER);
		lblPowerLoss.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblPowerLoss = new GridBagConstraints();
		gbc_lblPowerLoss.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPowerLoss.gridwidth = 2;
		gbc_lblPowerLoss.insets = new Insets(0, 0, 5, 0);
		gbc_lblPowerLoss.gridx = 0;
		gbc_lblPowerLoss.gridy = 0;
		panelPowerLoss.add(lblPowerLoss, gbc_lblPowerLoss);

		final JLabel lblPropagationLossdbcm = new JLabel("Propagation loss (dB/cm)  ");
		lblPropagationLossdbcm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPropagationLossdbcm.setVerticalAlignment(SwingConstants.CENTER);
		lblPropagationLossdbcm.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblPropagationLossdbcm = new GridBagConstraints();
		gbc_lblPropagationLossdbcm.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPropagationLossdbcm.insets = new Insets(0, 0, 5, 5);
		gbc_lblPropagationLossdbcm.gridx = 0;
		gbc_lblPropagationLossdbcm.gridy = 1;
		panelPowerLoss.add(lblPropagationLossdbcm, gbc_lblPropagationLossdbcm);

		final JSpinner spinner_propagationLoss = new JSpinner();
		spinner_propagationLoss.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_propagationLoss.setModel(new SpinnerNumberModel(cr.getL_p(), -10.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_propagationLoss = new GridBagConstraints();
		gbc_spinner_propagationLoss.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_propagationLoss.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_propagationLoss.gridx = 1;
		gbc_spinner_propagationLoss.gridy = 1;
		panelPowerLoss.add(spinner_propagationLoss, gbc_spinner_propagationLoss);

		final JLabel lblInsertionLossWaveguide = new JLabel("  Insertion loss waveguide crossing (dB)  ");
		lblInsertionLossWaveguide.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInsertionLossWaveguide.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblInsertionLossWaveguide = new GridBagConstraints();
		gbc_lblInsertionLossWaveguide.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblInsertionLossWaveguide.insets = new Insets(0, 0, 5, 5);
		gbc_lblInsertionLossWaveguide.gridx = 0;
		gbc_lblInsertionLossWaveguide.gridy = 2;
		panelPowerLoss.add(lblInsertionLossWaveguide, gbc_lblInsertionLossWaveguide);

		final JSpinner spinner_crossingLoss = new JSpinner();
		spinner_crossingLoss.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_crossingLoss.setModel(new SpinnerNumberModel(cr.getL_c(), -10.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_crossingLoss = new GridBagConstraints();
		gbc_spinner_crossingLoss.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_crossingLoss.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_crossingLoss.gridx = 1;
		gbc_spinner_crossingLoss.gridy = 2;
		panelPowerLoss.add(spinner_crossingLoss, gbc_spinner_crossingLoss);

		final JLabel lblInsertionLossPpseoff = new JLabel("Insertion loss PPSE-OFF (dB)  ");
		lblInsertionLossPpseoff.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInsertionLossPpseoff.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblInsertionLossPpseoff = new GridBagConstraints();
		gbc_lblInsertionLossPpseoff.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblInsertionLossPpseoff.insets = new Insets(0, 0, 5, 5);
		gbc_lblInsertionLossPpseoff.gridx = 0;
		gbc_lblInsertionLossPpseoff.gridy = 3;
		panelPowerLoss.add(lblInsertionLossPpseoff, gbc_lblInsertionLossPpseoff);

		final JSpinner spinner_ppseOffLoss = new JSpinner();
		spinner_ppseOffLoss.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_ppseOffLoss.setModel(new SpinnerNumberModel(cr.getL_poff(), -10.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_ppseOffLoss = new GridBagConstraints();
		gbc_spinner_ppseOffLoss.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_ppseOffLoss.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_ppseOffLoss.gridx = 1;
		gbc_spinner_ppseOffLoss.gridy = 3;
		panelPowerLoss.add(spinner_ppseOffLoss, gbc_spinner_ppseOffLoss);

		final JLabel lblInsertionLossPpseon = new JLabel("Insertion loss PPSE-ON (dB)  ");
		lblInsertionLossPpseon.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInsertionLossPpseon.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblInsertionLossPpseon = new GridBagConstraints();
		gbc_lblInsertionLossPpseon.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblInsertionLossPpseon.insets = new Insets(0, 0, 5, 5);
		gbc_lblInsertionLossPpseon.gridx = 0;
		gbc_lblInsertionLossPpseon.gridy = 4;
		panelPowerLoss.add(lblInsertionLossPpseon, gbc_lblInsertionLossPpseon);

		final JSpinner spinner_ppseOnLoss = new JSpinner();
		spinner_ppseOnLoss.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_ppseOnLoss.setModel(new SpinnerNumberModel(cr.getL_pon(), -10.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_ppseOnLoss = new GridBagConstraints();
		gbc_spinner_ppseOnLoss.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_ppseOnLoss.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_ppseOnLoss.gridx = 1;
		gbc_spinner_ppseOnLoss.gridy = 4;
		panelPowerLoss.add(spinner_ppseOnLoss, gbc_spinner_ppseOnLoss);

		final JLabel lblInsertionLossCpseoff = new JLabel("Insertion loss CPSE-OFF (dB)  ");
		lblInsertionLossCpseoff.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInsertionLossCpseoff.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblInsertionLossCpseoff = new GridBagConstraints();
		gbc_lblInsertionLossCpseoff.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblInsertionLossCpseoff.insets = new Insets(0, 0, 5, 5);
		gbc_lblInsertionLossCpseoff.gridx = 0;
		gbc_lblInsertionLossCpseoff.gridy = 5;
		panelPowerLoss.add(lblInsertionLossCpseoff, gbc_lblInsertionLossCpseoff);

		final JSpinner spinner_cpseOffLoss = new JSpinner();
		spinner_cpseOffLoss.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_cpseOffLoss.setModel(new SpinnerNumberModel(cr.getL_coff(), -10.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_cpseOffLoss = new GridBagConstraints();
		gbc_spinner_cpseOffLoss.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_cpseOffLoss.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_cpseOffLoss.gridx = 1;
		gbc_spinner_cpseOffLoss.gridy = 5;
		panelPowerLoss.add(spinner_cpseOffLoss, gbc_spinner_cpseOffLoss);

		final JLabel lblInsertionLossCpseon = new JLabel("Insertion loss CPSE-ON (dB)  ");
		lblInsertionLossCpseon.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInsertionLossCpseon.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblInsertionLossCpseon = new GridBagConstraints();
		gbc_lblInsertionLossCpseon.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblInsertionLossCpseon.insets = new Insets(0, 0, 0, 5);
		gbc_lblInsertionLossCpseon.gridx = 0;
		gbc_lblInsertionLossCpseon.gridy = 6;
		panelPowerLoss.add(lblInsertionLossCpseon, gbc_lblInsertionLossCpseon);

		final JSpinner spinner_cpseOnLoss = new JSpinner();
		spinner_cpseOnLoss.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_cpseOnLoss.setModel(new SpinnerNumberModel(cr.getL_con(), -10.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_cpseOnLoss = new GridBagConstraints();
		gbc_spinner_cpseOnLoss.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_cpseOnLoss.gridx = 1;
		gbc_spinner_cpseOnLoss.gridy = 6;
		panelPowerLoss.add(spinner_cpseOnLoss, gbc_spinner_cpseOnLoss);

		panelCrosstalk.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelCrosstalk.setBounds(10, 130, 274, 94);
		panelOpticalcoefficients.add(panelCrosstalk);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[] { 210, 60, 0 };
		gbl_panel_6.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_6.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_6.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelCrosstalk.setLayout(gbl_panel_6);

		final JLabel lblCrosstalk = new JLabel("Crosstalk");
		lblCrosstalk.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrosstalk.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblCrosstalk = new GridBagConstraints();
		gbc_lblCrosstalk.gridwidth = 2;
		gbc_lblCrosstalk.insets = new Insets(0, 0, 5, 5);
		gbc_lblCrosstalk.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCrosstalk.gridx = 0;
		gbc_lblCrosstalk.gridy = 0;
		panelCrosstalk.add(lblCrosstalk, gbc_lblCrosstalk);

		final JLabel lblCrosstalkWaveguideCrossing = new JLabel("  Crosstalk waveguide crossing (dB)  ");
		lblCrosstalkWaveguideCrossing.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCrosstalkWaveguideCrossing.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblCrosstalkWaveguideCrossing = new GridBagConstraints();
		gbc_lblCrosstalkWaveguideCrossing.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCrosstalkWaveguideCrossing.insets = new Insets(0, 0, 5, 5);
		gbc_lblCrosstalkWaveguideCrossing.gridx = 0;
		gbc_lblCrosstalkWaveguideCrossing.gridy = 1;
		panelCrosstalk.add(lblCrosstalkWaveguideCrossing, gbc_lblCrosstalkWaveguideCrossing);

		final JSpinner spinner_crossingCrosstalk = new JSpinner();
		spinner_crossingCrosstalk.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_crossingCrosstalk.setModel(new SpinnerNumberModel(cr.getK_c(), -80.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_crossingCrosstalk = new GridBagConstraints();
		gbc_spinner_crossingCrosstalk.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_crossingCrosstalk.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_crossingCrosstalk.gridx = 1;
		gbc_spinner_crossingCrosstalk.gridy = 1;
		panelCrosstalk.add(spinner_crossingCrosstalk, gbc_spinner_crossingCrosstalk);

		final JLabel lblCrosstalkPpseoffdb = new JLabel("Crosstalk PPSE-OFF (dB)  ");
		lblCrosstalkPpseoffdb.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCrosstalkPpseoffdb.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblCrosstalkPpseoffdb = new GridBagConstraints();
		gbc_lblCrosstalkPpseoffdb.insets = new Insets(0, 0, 5, 5);
		gbc_lblCrosstalkPpseoffdb.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCrosstalkPpseoffdb.gridx = 0;
		gbc_lblCrosstalkPpseoffdb.gridy = 2;
		panelCrosstalk.add(lblCrosstalkPpseoffdb, gbc_lblCrosstalkPpseoffdb);

		final JSpinner spinner_ppseOffCrosstalk = new JSpinner();
		spinner_ppseOffCrosstalk.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_ppseOffCrosstalk.setModel(new SpinnerNumberModel(cr.getK_poff(), -80.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_ppseOffCrosstalk = new GridBagConstraints();
		gbc_spinner_ppseOffCrosstalk.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_ppseOffCrosstalk.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_ppseOffCrosstalk.gridx = 1;
		gbc_spinner_ppseOffCrosstalk.gridy = 2;
		panelCrosstalk.add(spinner_ppseOffCrosstalk, gbc_spinner_ppseOffCrosstalk);

		final JLabel lblCrosstalkPpseondb = new JLabel("Crosstalk PPSE-ON (dB)  ");
		lblCrosstalkPpseondb.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCrosstalkPpseondb.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblCrosstalkPpseondb = new GridBagConstraints();
		gbc_lblCrosstalkPpseondb.insets = new Insets(0, 0, 0, 5);
		gbc_lblCrosstalkPpseondb.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCrosstalkPpseondb.gridx = 0;
		gbc_lblCrosstalkPpseondb.gridy = 3;
		panelCrosstalk.add(lblCrosstalkPpseondb, gbc_lblCrosstalkPpseondb);

		final JSpinner spinner_ppseOnCrosstalk = new JSpinner();
		spinner_ppseOnCrosstalk.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_ppseOnCrosstalk.setModel(new SpinnerNumberModel(cr.getK_pon(), -80.0, 0.0, 0.001));
		GridBagConstraints gbc_spinner_ppseOnCrosstalk = new GridBagConstraints();
		gbc_spinner_ppseOnCrosstalk.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_ppseOnCrosstalk.gridx = 1;
		gbc_spinner_ppseOnCrosstalk.gridy = 3;
		panelCrosstalk.add(spinner_ppseOnCrosstalk, gbc_spinner_ppseOnCrosstalk);

		panelMiscellaneous.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelMiscellaneous.setBounds(10, 130, 274, 94);
		panelOpticalcoefficients.add(panelMiscellaneous);
		GridBagLayout gbl_panel_Misc = new GridBagLayout();
		gbl_panel_Misc.columnWidths = new int[] { 210, 60, 0 };
		gbl_panel_Misc.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_Misc.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_Misc.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelMiscellaneous.setLayout(gbl_panel_Misc);

		final JLabel lblMiscellaneous = new JLabel("  Miscellaneous  ");
		lblMiscellaneous.setHorizontalAlignment(SwingConstants.CENTER);
		lblMiscellaneous.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblMiscellaneous = new GridBagConstraints();
		gbc_lblMiscellaneous.gridwidth = 2;
		gbc_lblMiscellaneous.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMiscellaneous.insets = new Insets(0, 0, 5, 5);
		gbc_lblMiscellaneous.gridx = 0;
		gbc_lblMiscellaneous.gridy = 0;
		panelMiscellaneous.add(lblMiscellaneous, gbc_lblMiscellaneous);

		final JLabel lblSensitivity = new JLabel("Photodetector sensitivity (dBm)  ");
		lblSensitivity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSensitivity.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSensitivity = new GridBagConstraints();
		gbc_lblSensitivity.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensitivity.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSensitivity.gridx = 0;
		gbc_lblSensitivity.gridy = 1;
		panelMiscellaneous.add(lblSensitivity, gbc_lblSensitivity);

		final JSpinner spinner_Sensitivity = new JSpinner();
		spinner_Sensitivity.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_Sensitivity.setModel(new SpinnerNumberModel(cr.getPhotodetectorSensitivity(), -100.0, 100.0, 0.1));
		GridBagConstraints gbc_spinner_Sensitivity = new GridBagConstraints();
		gbc_spinner_Sensitivity.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_Sensitivity.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_Sensitivity.gridx = 1;
		gbc_spinner_Sensitivity.gridy = 1;
		panelMiscellaneous.add(spinner_Sensitivity, gbc_spinner_Sensitivity);

		final JLabel lblLaseEfficiency = new JLabel("Laser efficiency (%)  ");
		lblLaseEfficiency.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLaseEfficiency.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblLaseEfficiency = new GridBagConstraints();
		gbc_lblLaseEfficiency.insets = new Insets(0, 0, 5, 5);
		gbc_lblLaseEfficiency.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblLaseEfficiency.gridx = 0;
		gbc_lblLaseEfficiency.gridy = 2;
		panelMiscellaneous.add(lblLaseEfficiency, gbc_lblLaseEfficiency);

		final JSpinner spinner_LaserEfficiency = new JSpinner();
		spinner_LaserEfficiency.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_LaserEfficiency.setModel(new SpinnerNumberModel(cr.getLaserEfficiency(), 1, 100, 1));
		GridBagConstraints gbc_spinner_LaserEfficiency = new GridBagConstraints();
		gbc_spinner_LaserEfficiency.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_LaserEfficiency.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_LaserEfficiency.gridx = 1;
		gbc_spinner_LaserEfficiency.gridy = 2;
		panelMiscellaneous.add(spinner_LaserEfficiency, gbc_spinner_LaserEfficiency);

		final JLabel lblModulationRate = new JLabel("Modulation rate (Gb/s)  ");
		lblModulationRate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblModulationRate.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblModulationRate = new GridBagConstraints();
		gbc_lblModulationRate.insets = new Insets(0, 0, 0, 5);
		gbc_lblModulationRate.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblModulationRate.gridx = 0;
		gbc_lblModulationRate.gridy = 3;
		panelMiscellaneous.add(lblModulationRate, gbc_lblModulationRate);

		final JSpinner spinner_ModulationRate = new JSpinner();
		spinner_ModulationRate.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_ModulationRate.setModel(new SpinnerNumberModel(cr.getModulationRate(), 1, 100, 1));
		GridBagConstraints gbc_spinner_ModulationRate = new GridBagConstraints();
		gbc_spinner_ModulationRate.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_ModulationRate.gridx = 1;
		gbc_spinner_ModulationRate.gridy = 3;
		panelMiscellaneous.add(spinner_ModulationRate, gbc_spinner_ModulationRate);

		tabbedPane.addTab("NoC Architecture", null, panelNoCArchitecture, null);
		panelNoCArchitecture.setLayout(null);

		panelTopology.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelTopology.setBounds(10, 11, 274, 99);
		panelNoCArchitecture.add(panelTopology);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[] { 180, 90, 0 };
		gbl_panel_7.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_7.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_7.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelTopology.setLayout(gbl_panel_7);

		final JLabel lblTopology = new JLabel("Topology");
		lblTopology.setHorizontalAlignment(SwingConstants.CENTER);
		lblTopology.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblTopology = new GridBagConstraints();
		gbc_lblTopology.gridwidth = 2;
		gbc_lblTopology.insets = new Insets(0, 0, 5, 5);
		gbc_lblTopology.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTopology.gridx = 0;
		gbc_lblTopology.gridy = 0;
		panelTopology.add(lblTopology, gbc_lblTopology);

		final JLabel lblType = new JLabel("Type  ");
		lblType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 1;
		panelTopology.add(lblType, gbc_lblType);

		final JComboBox<String> comboBox_topology = new JComboBox<String>();
		comboBox_topology.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox_topology = new GridBagConstraints();
		gbc_comboBox_topology.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_topology.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_topology.gridx = 1;
		gbc_comboBox_topology.gridy = 1;
		panelTopology.add(comboBox_topology, gbc_comboBox_topology);

		final JLabel lblRows = new JLabel("# rows  ");
		lblRows.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRows.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblRows = new GridBagConstraints();
		gbc_lblRows.insets = new Insets(0, 0, 5, 5);
		gbc_lblRows.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblRows.gridx = 0;
		gbc_lblRows.gridy = 2;
		panelTopology.add(lblRows, gbc_lblRows);

		final JSpinner spinner_rows = new JSpinner();
		spinner_rows.setModel(new SpinnerNumberModel(new Integer(cr.getM()), new Integer(2), null, new Integer(1)));
		spinner_rows.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_spinner_rows = new GridBagConstraints();
		gbc_spinner_rows.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_rows.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_rows.gridx = 1;
		gbc_spinner_rows.gridy = 2;
		panelTopology.add(spinner_rows, gbc_spinner_rows);

		final JLabel lblColumns = new JLabel("# columns  ");
		lblColumns.setHorizontalAlignment(SwingConstants.RIGHT);
		lblColumns.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblColumns = new GridBagConstraints();
		gbc_lblColumns.insets = new Insets(0, 0, 0, 5);
		gbc_lblColumns.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblColumns.gridx = 0;
		gbc_lblColumns.gridy = 3;
		panelTopology.add(lblColumns, gbc_lblColumns);

		final JSpinner spinner_columns = new JSpinner();
		spinner_columns.setModel(new SpinnerNumberModel(new Integer(cr.getN()), new Integer(2), null, new Integer(1)));
		spinner_columns.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_spinner_columns = new GridBagConstraints();
		gbc_spinner_columns.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_columns.gridx = 1;
		gbc_spinner_columns.gridy = 3;
		panelTopology.add(spinner_columns, gbc_spinner_columns);

		panelRouting.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelRouting.setBounds(10, 121, 274, 49);
		panelNoCArchitecture.add(panelRouting);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[] { 180, 90, 0 };
		gbl_panel_8.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel_8.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_8.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelRouting.setLayout(gbl_panel_8);

		final JLabel lblRouting = new JLabel("Routing");
		lblRouting.setHorizontalAlignment(SwingConstants.CENTER);
		lblRouting.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblRouting = new GridBagConstraints();
		gbc_lblRouting.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblRouting.gridwidth = 2;
		gbc_lblRouting.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouting.gridx = 0;
		gbc_lblRouting.gridy = 0;
		panelRouting.add(lblRouting, gbc_lblRouting);

		final JLabel lblType_1 = new JLabel("Type  ");
		lblType_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType_1.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblType_1 = new GridBagConstraints();
		gbc_lblType_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblType_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblType_1.anchor = GridBagConstraints.EAST;
		gbc_lblType_1.gridx = 0;
		gbc_lblType_1.gridy = 1;
		panelRouting.add(lblType_1, gbc_lblType_1);

		final JComboBox<String> comboBox_routing = new JComboBox<String>();
		comboBox_routing.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox_routing = new GridBagConstraints();
		gbc_comboBox_routing.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_routing.gridx = 1;
		gbc_comboBox_routing.gridy = 1;
		panelRouting.add(comboBox_routing, gbc_comboBox_routing);

		panelRouter.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelRouter.setBounds(10, 181, 274, 49);
		panelNoCArchitecture.add(panelRouter);
		GridBagLayout gbl_panel_9 = new GridBagLayout();
		gbl_panel_9.columnWidths = new int[] { 180, 90, 0 };
		gbl_panel_9.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel_9.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_9.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelRouter.setLayout(gbl_panel_9);

		final JLabel lblOpticalRouter = new JLabel("Optical router");
		lblOpticalRouter.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpticalRouter.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblOpticalRouter = new GridBagConstraints();
		gbc_lblOpticalRouter.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblOpticalRouter.gridwidth = 2;
		gbc_lblOpticalRouter.insets = new Insets(0, 0, 5, 0);
		gbc_lblOpticalRouter.gridx = 0;
		gbc_lblOpticalRouter.gridy = 0;
		panelRouter.add(lblOpticalRouter, gbc_lblOpticalRouter);

		final JLabel lblType_2 = new JLabel("Type  ");
		lblType_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType_2.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblType_2 = new GridBagConstraints();
		gbc_lblType_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblType_2.anchor = GridBagConstraints.EAST;
		gbc_lblType_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblType_2.gridx = 0;
		gbc_lblType_2.gridy = 1;
		panelRouter.add(lblType_2, gbc_lblType_2);

		final DefaultComboBoxModel<String> comboBox_router_model = new DefaultComboBoxModel<String>();
		for (int router_i = 0; router_i < cr.getRouter_types().size(); router_i++) {
			comboBox_router_model.addElement(cr.getRouter_types().get(router_i));
		}		
		final JComboBox<String> comboBox_router = new JComboBox<String>(comboBox_router_model);
		comboBox_router.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox_router = new GridBagConstraints();
		gbc_comboBox_router.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_router.gridx = 1;
		gbc_comboBox_router.gridy = 1;
		panelRouter.add(comboBox_router, gbc_comboBox_router);

		panelChip.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelChip.setBounds(10, 241, 274, 49);
		panelNoCArchitecture.add(panelChip);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 180, 90, 0 };
		gbl_panel_3.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel_3.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_3.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelChip.setLayout(gbl_panel_3);

		final JLabel lblChip = new JLabel("Chip");
		lblChip.setHorizontalAlignment(SwingConstants.CENTER);
		lblChip.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblChip = new GridBagConstraints();
		gbc_lblChip.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblChip.gridwidth = 2;
		gbc_lblChip.insets = new Insets(0, 0, 5, 5);
		gbc_lblChip.gridx = 0;
		gbc_lblChip.gridy = 0;
		panelChip.add(lblChip, gbc_lblChip);

		final JLabel lblSize = new JLabel("Size (mm^2)");
		lblSize.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSize.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblSize = new GridBagConstraints();
		gbc_lblSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSize.insets = new Insets(0, 0, 0, 5);
		gbc_lblSize.gridx = 0;
		gbc_lblSize.gridy = 1;
		panelChip.add(lblSize, gbc_lblSize);

		final JSpinner spinner_chipSize = new JSpinner();
		spinner_chipSize.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_chipSize
				.setModel(new SpinnerNumberModel(new Integer(cr.getChipSize()), new Integer(64), null, new Integer(1)));
		GridBagConstraints gbc_spinner_14 = new GridBagConstraints();
		gbc_spinner_14.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_14.gridx = 1;
		gbc_spinner_14.gridy = 1;
		panelChip.add(spinner_chipSize, gbc_spinner_14);

		tabbedPane.addTab("Application & Mapping", null, panelApplicationMapping, null);
		panelApplicationMapping.setLayout(null);

		panelApplication.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelApplication.setBounds(10, 11, 284, 49);
		panelApplicationMapping.add(panelApplication);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[] { 150, 120, 5 };
		gbl_panel_10.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel_10.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_10.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelApplication.setLayout(gbl_panel_10);

		final JLabel lblApplication = new JLabel("Application");
		lblApplication.setHorizontalAlignment(SwingConstants.CENTER);
		lblApplication.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblApplication = new GridBagConstraints();
		gbc_lblApplication.gridwidth = 2;
		gbc_lblApplication.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblApplication.insets = new Insets(0, 0, 5, 5);
		gbc_lblApplication.gridx = 0;
		gbc_lblApplication.gridy = 0;
		panelApplication.add(lblApplication, gbc_lblApplication);

		final JLabel lblFileName = new JLabel("File name  ");
		lblFileName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFileName.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblFileName = new GridBagConstraints();
		gbc_lblFileName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFileName.insets = new Insets(0, 0, 0, 5);
		gbc_lblFileName.anchor = GridBagConstraints.EAST;
		gbc_lblFileName.gridx = 0;
		gbc_lblFileName.gridy = 1;
		panelApplication.add(lblFileName, gbc_lblFileName);

		final JTextField TxtApp = new JTextField();
		TxtApp.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_TxtApp = new GridBagConstraints();
		gbc_TxtApp.fill = GridBagConstraints.HORIZONTAL;
		gbc_TxtApp.gridx = 1;
		gbc_TxtApp.gridy = 1;
		panelApplication.add(TxtApp, gbc_TxtApp);

		panelMapping.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelMapping.setBounds(10, 71, 284, 175);
		panelApplicationMapping.add(panelMapping);
		GridBagLayout gbl_panel_11 = new GridBagLayout();
		gbl_panel_11.columnWidths = new int[] { 150, 120, 5 };
		gbl_panel_11.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_11.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_11.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelMapping.setLayout(gbl_panel_11);

		final JLabel lblMappingStrategy = new JLabel("Mapping strategy");
		lblMappingStrategy.setHorizontalAlignment(SwingConstants.CENTER);
		lblMappingStrategy.setFont(new Font("Arial", Font.BOLD, 13));
		GridBagConstraints gbc_lblMappingStrategy = new GridBagConstraints();
		gbc_lblMappingStrategy.gridwidth = 2;
		gbc_lblMappingStrategy.insets = new Insets(0, 0, 5, 0);
		gbc_lblMappingStrategy.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMappingStrategy.gridx = 0;
		gbc_lblMappingStrategy.gridy = 0;
		panelMapping.add(lblMappingStrategy, gbc_lblMappingStrategy);

		final JLabel lblType_3 = new JLabel("Algorithm  ");
		lblType_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblType_3.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblType_3 = new GridBagConstraints();
		gbc_lblType_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblType_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblType_3.gridx = 0;
		gbc_lblType_3.gridy = 1;
		panelMapping.add(lblType_3, gbc_lblType_3);

		final JComboBox<String> comboBox_mappingType = new JComboBox<String>();
		for (int map_i = 0; map_i < cr.getMapping_strategies().size(); map_i++) {
			comboBox_mappingType.addItem(cr.getMapping_strategies().get(map_i));
		}
		comboBox_mappingType.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox_mappingType = new GridBagConstraints();
		gbc_comboBox_mappingType.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_mappingType.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_mappingType.gridx = 1;
		gbc_comboBox_mappingType.gridy = 1;
		panelMapping.add(comboBox_mappingType, gbc_comboBox_mappingType);

		final JLabel lblObjective = new JLabel("Objective  ");
		lblObjective.setHorizontalAlignment(SwingConstants.RIGHT);
		lblObjective.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblObjective = new GridBagConstraints();
		gbc_lblObjective.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblObjective.anchor = GridBagConstraints.EAST;
		gbc_lblObjective.insets = new Insets(0, 0, 5, 5);
		gbc_lblObjective.gridx = 0;
		gbc_lblObjective.gridy = 2;
		panelMapping.add(lblObjective, gbc_lblObjective);

		final JComboBox<String> comboBox_mappingObjective = new JComboBox<String>();
		comboBox_mappingObjective.addItem(Globals.mappingObjectiveCrosstalk);
		comboBox_mappingObjective.addItem(Globals.mappingObjectiveLoss);
		comboBox_mappingObjective.addItem(Globals.mappingObjectiveLaserPower);
		comboBox_mappingObjective.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_comboBox_mappingObjective = new GridBagConstraints();
		gbc_comboBox_mappingObjective.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_mappingObjective.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_mappingObjective.gridx = 1;
		gbc_comboBox_mappingObjective.gridy = 2;
		panelMapping.add(comboBox_mappingObjective, gbc_comboBox_mappingObjective);

		final JLabel lblIterations = new JLabel("# iterations  ");
		lblIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIterations.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblIterations = new GridBagConstraints();
		gbc_lblIterations.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblIterations.insets = new Insets(0, 0, 5, 5);
		gbc_lblIterations.gridx = 0;
		gbc_lblIterations.gridy = 3;
		panelMapping.add(lblIterations, gbc_lblIterations);

		final JSpinner spinner_iterations = new JSpinner();
		spinner_iterations
				.setModel(new SpinnerNumberModel(new Integer(cr.getIterations()), null, null, new Integer(1)));
		spinner_iterations.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_spinner_iterations = new GridBagConstraints();
		gbc_spinner_iterations.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_iterations.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_iterations.gridx = 1;
		gbc_spinner_iterations.gridy = 3;
		panelMapping.add(spinner_iterations, gbc_spinner_iterations);

		final JLabel lblIterationSame = new JLabel("Stop condition 1  ");
		lblIterationSame.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIterationSame.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblIterationSame = new GridBagConstraints();
		gbc_lblIterationSame.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblIterationSame.insets = new Insets(0, 0, 5, 5);
		gbc_lblIterationSame.gridx = 0;
		gbc_lblIterationSame.gridy = 4;
		panelMapping.add(lblIterationSame, gbc_lblIterationSame);

		final JRadioButton radioButton_StopCondition = new JRadioButton("");
		radioButton_StopCondition.setFont(new Font("Arial", Font.PLAIN, 11));
		radioButton_StopCondition.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_radioButton_NumSameValue = new GridBagConstraints();
		gbc_radioButton_NumSameValue.insets = new Insets(0, 0, 5, 0);
		gbc_radioButton_NumSameValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_radioButton_NumSameValue.gridx = 1;
		gbc_radioButton_NumSameValue.gridy = 4;
		panelMapping.add(radioButton_StopCondition, gbc_radioButton_NumSameValue);

		final JLabel lblPopulationSize = new JLabel("Default parameters  ");
		lblPopulationSize.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPopulationSize.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblPopulationSize = new GridBagConstraints();
		gbc_lblPopulationSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPopulationSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblPopulationSize.gridx = 0;
		gbc_lblPopulationSize.gridy = 5;
		panelMapping.add(lblPopulationSize, gbc_lblPopulationSize);

		final JLabel lblPopulationSize_1 = new JLabel("Population size  ");
		lblPopulationSize_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPopulationSize_1.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblPopulationSize_1 = new GridBagConstraints();
		gbc_lblPopulationSize_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPopulationSize_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblPopulationSize_1.gridx = 0;
		gbc_lblPopulationSize_1.gridy = 6;
		panelMapping.add(lblPopulationSize_1, gbc_lblPopulationSize_1);

		final JSpinner spinner_populationSize = new JSpinner();
		spinner_populationSize.setEnabled(false);
		spinner_populationSize.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_populationSize.setModel(
				new SpinnerNumberModel(new Integer(cr.getPopulation()), new Integer(10), null, new Integer(1)));
		GridBagConstraints gbc_spinner_populationSize = new GridBagConstraints();
		gbc_spinner_populationSize.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_populationSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_populationSize.gridx = 1;
		gbc_spinner_populationSize.gridy = 6;
		panelMapping.add(spinner_populationSize, gbc_spinner_populationSize);

		final JLabel lblOffspringSize = new JLabel("Offspring size  ");
		lblOffspringSize.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOffspringSize.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblOffspringSize = new GridBagConstraints();
		gbc_lblOffspringSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblOffspringSize.insets = new Insets(0, 0, 0, 5);
		gbc_lblOffspringSize.gridx = 0;
		gbc_lblOffspringSize.gridy = 7;
		panelMapping.add(lblOffspringSize, gbc_lblOffspringSize);

		final JSpinner spinner_offspringSize = new JSpinner();
		spinner_offspringSize.setEnabled(false);
		spinner_offspringSize.setFont(new Font("Arial", Font.PLAIN, 11));
		spinner_offspringSize
				.setModel(new SpinnerNumberModel(new Integer(cr.getOffspring()), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spinner_offspringSize = new GridBagConstraints();
		gbc_spinner_offspringSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_offspringSize.gridx = 1;
		gbc_spinner_offspringSize.gridy = 7;
		panelMapping.add(spinner_offspringSize, gbc_spinner_offspringSize);

		final JRadioButton radioButton_defaultGeneticParameters = new JRadioButton("");
		radioButton_defaultGeneticParameters.setFont(new Font("Arial", Font.PLAIN, 11));
		radioButton_defaultGeneticParameters.setSelected(true);
		radioButton_defaultGeneticParameters.setHorizontalAlignment(SwingConstants.LEFT);
		radioButton_defaultGeneticParameters.setEnabled(false);
		GridBagConstraints gbc_radioButton_defaultGeneticParameters = new GridBagConstraints();
		gbc_radioButton_defaultGeneticParameters.insets = new Insets(0, 0, 5, 0);
		gbc_radioButton_defaultGeneticParameters.fill = GridBagConstraints.HORIZONTAL;
		gbc_radioButton_defaultGeneticParameters.gridx = 1;
		gbc_radioButton_defaultGeneticParameters.gridy = 5;
		panelMapping.add(radioButton_defaultGeneticParameters, gbc_radioButton_defaultGeneticParameters);
		radioButton_defaultGeneticParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (radioButton_defaultGeneticParameters.isSelected()) {
					spinner_populationSize.setValue(cr.getPopulation());
					spinner_offspringSize.setValue(cr.getOffspring());
					spinner_populationSize.setEnabled(false);
					spinner_offspringSize.setEnabled(false);

				} else {
					spinner_populationSize.setEnabled(true);
					spinner_offspringSize.setEnabled(true);
				}
			}
		});

		tabbedPane.addTab("Execution", null, panelExecution, null);
		GridBagLayout gbl_panelExecution = new GridBagLayout();
		gbl_panelExecution.columnWidths = new int[] { 10, 147, 103, 10 };
		gbl_panelExecution.rowHeights = new int[] { 10, 23, 23, 17, 160, 10 };
		gbl_panelExecution.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelExecution.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelExecution.setLayout(gbl_panelExecution);

		final JButton btnNewButton = new JButton("Start Mapping Optimization");

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 1;
		panelExecution.add(btnNewButton, gbc_btnNewButton);

		final JRadioButton rdbtnWriting = new JRadioButton("Writing output on file");
		rdbtnWriting.setSelected(true);
		rdbtnWriting.setFont(new Font("Arial", Font.PLAIN, 11));
		rdbtnWriting.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_rdbtnWriting = new GridBagConstraints();
		gbc_rdbtnWriting.anchor = GridBagConstraints.NORTH;
		gbc_rdbtnWriting.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnWriting.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnWriting.gridx = 1;
		gbc_rdbtnWriting.gridy = 2;
		panelExecution.add(rdbtnWriting, gbc_rdbtnWriting);

		final JLabel lblMappingOptimization = new JLabel("Mapping optimization:");
		lblMappingOptimization.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMappingOptimization.setFont(new Font("Arial", Font.PLAIN, 11));
		GridBagConstraints gbc_lblMappingOptimization = new GridBagConstraints();
		gbc_lblMappingOptimization.anchor = GridBagConstraints.NORTH;
		gbc_lblMappingOptimization.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMappingOptimization.insets = new Insets(0, 0, 5, 5);
		gbc_lblMappingOptimization.gridx = 1;
		gbc_lblMappingOptimization.gridy = 3;
		panelExecution.add(lblMappingOptimization, gbc_lblMappingOptimization);

		final JProgressBar progressBarMapping = new JProgressBar();
		progressBarMapping.setFont(new Font("Arial", Font.PLAIN, 11));
		progressBarMapping.setStringPainted(true);
		GridBagConstraints gbc_progressBarMapping = new GridBagConstraints();
		gbc_progressBarMapping.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBarMapping.insets = new Insets(0, 0, 5, 0);
		gbc_progressBarMapping.gridx = 2;
		gbc_progressBarMapping.gridy = 3;
		panelExecution.add(progressBarMapping, gbc_progressBarMapping);

		final JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		final JScrollPane scroll = new JScrollPane(textArea);
		GridBagConstraints gbc_scroll = new GridBagConstraints();
		gbc_scroll.fill = GridBagConstraints.BOTH;
		gbc_scroll.gridwidth = 2;
		gbc_scroll.gridx = 1;
		gbc_scroll.gridy = 4;
		panelExecution.add(scroll, gbc_scroll);

		for (int top_i = 0; top_i < cr.getTopology_types().size(); top_i++) {
			comboBox_topology.addItem(cr.getTopology_types().get(top_i));
		}
		for (int routing_i = 0; routing_i < cr.getRouting_types().size(); routing_i++) {
			comboBox_routing.addItem(cr.getRouting_types().get(routing_i));
		}
		/*for (int router_i = 0; router_i < cr.getRouter_types().size(); router_i++) {
			comboBox_router.addItem(cr.getRouter_types().get(router_i));
		}*/

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				progressBarMapping.setValue(0);
				textArea.setText(null);
				btnNewButton.setEnabled(false);
				tabbedPane.setEnabled(false);
				rdbtnWriting.setEnabled(false);
				// set configuration
				new Configuration((int) spinner_chipSize.getValue(), (double) spinner_propagationLoss.getValue(),
						(double) spinner_crossingLoss.getValue(), (double) spinner_ppseOffLoss.getValue(),
						(double) spinner_ppseOnLoss.getValue(), (double) spinner_cpseOffLoss.getValue(),
						(double) spinner_cpseOnLoss.getValue(), (double) spinner_crossingCrosstalk.getValue(),
						(double) spinner_ppseOffCrosstalk.getValue(), (double) spinner_ppseOnCrosstalk.getValue(),
						(double) spinner_Sensitivity.getValue(), 
						(int) spinner_LaserEfficiency.getValue(), 
						(int) spinner_ModulationRate.getValue(),
						(int) spinner_rows.getValue(), (int) spinner_columns.getValue(), (String) TxtApp.getText(),
						(String) comboBox_mappingType.getSelectedItem(), (String) comboBox_router.getSelectedItem(),
						(String) comboBox_routing.getSelectedItem(), (String) comboBox_topology.getSelectedItem(),
						(int) spinner_iterations.getValue(), (int) spinner_populationSize.getValue(),
						(int) spinner_offspringSize.getValue(), (String) comboBox_mappingObjective.getSelectedItem(),
						(boolean) rdbtnWriting.isSelected(), (boolean) radioButton_StopCondition.isSelected());

				OptimizationMain opt = new OptimizationMain(textArea, progressBarMapping, tabbedPane, btnNewButton,
						rdbtnWriting);
				opt.execute();
			}
		});

		panelExecution.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			@Override
			public void ancestorResized(HierarchyEvent e) {

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				long baseRes = 2073600;// 1920x1080
				long prop = (long) (screenSize.getWidth() * screenSize.getHeight()) / baseRes;

				if (screenSize.getWidth() * screenSize.getHeight() < baseRes)
					prop = 1;

				int base = 300;
				double prop_y;
				tabbedPane.setSize(contentPane.getWidth() - 10, contentPane.getHeight() - 10);
				tabbedPane.setFont(new Font("Dialog", Font.BOLD, (int) (12 * prop)));
				int width = panelOpticalcoefficients.getWidth() - 20;
				int height = (int) ((panelOpticalcoefficients.getHeight() - 40) * 0.5);
				prop_y = (2.0 * height) / base;

				Font font8 = new Font("Arial", Font.PLAIN, (int) (8 * prop_y));
				Font font11 = new Font("Arial", Font.BOLD, (int) (11 * prop_y));
				Font fontTxtArea = new Font("Arial", Font.PLAIN, (int) (13 * (prop * 1.1)));

				// panelOpticalcoefficients
				int h2 = (int) ((panelOpticalcoefficients.getHeight() - 40) * 0.25);
				panelPowerLoss.setSize(width, height);
				panelCrosstalk.setSize(width, (int) ((panelOpticalcoefficients.getHeight() - 40) * 0.25));
				panelCrosstalk.setLocation(10, panelPowerLoss.getLocation().y + height + 10);
				panelMiscellaneous.setSize(width, (int) ((panelOpticalcoefficients.getHeight() - 40) * 0.25));
				panelMiscellaneous.setLocation(10, panelPowerLoss.getLocation().y + height + 10 + h2 + 10);

				int rowHeight = height / 8;
				((GridBagLayout) panelPowerLoss.getLayout()).columnWidths = new int[] { (int) (width * 0.78),
						(int) (width * 0.21), (int) (width * 0.01) };
				((GridBagLayout) panelPowerLoss.getLayout()).rowHeights = new int[] { rowHeight, rowHeight, rowHeight,
						rowHeight, rowHeight, rowHeight, rowHeight };
				lblPowerLoss.setFont(font11);
				lblPropagationLossdbcm.setFont(font8);
				((JSpinner.NumberEditor) spinner_propagationLoss.getEditor()).getTextField().setFont(font8);
				lblCrosstalk.setFont(font11);
				lblInsertionLossWaveguide.setFont(font8);
				((JSpinner.NumberEditor) spinner_crossingLoss.getEditor()).getTextField().setFont(font8);
				lblInsertionLossPpseoff.setFont(font8);
				((JSpinner.NumberEditor) spinner_ppseOffLoss.getEditor()).getTextField().setFont(font8);
				lblInsertionLossPpseon.setFont(font8);
				((JSpinner.NumberEditor) spinner_ppseOnLoss.getEditor()).getTextField().setFont(font8);
				lblInsertionLossCpseoff.setFont(font8);
				((JSpinner.NumberEditor) spinner_cpseOffLoss.getEditor()).getTextField().setFont(font8);
				lblInsertionLossCpseon.setFont(font8);
				((JSpinner.NumberEditor) spinner_cpseOnLoss.getEditor()).getTextField().setFont(font8);

				((GridBagLayout) panelCrosstalk.getLayout()).columnWidths = new int[] { (int) (width * 0.78),
						(int) (width * 0.21), (int) (width * 0.01) };
				((GridBagLayout) panelCrosstalk.getLayout()).rowHeights = new int[] { rowHeight, rowHeight, rowHeight,
						rowHeight };
				lblCrosstalkWaveguideCrossing.setFont(font8);
				((JSpinner.NumberEditor) spinner_crossingCrosstalk.getEditor()).getTextField().setFont(font8);
				lblCrosstalkPpseoffdb.setFont(font8);
				((JSpinner.NumberEditor) spinner_ppseOffCrosstalk.getEditor()).getTextField().setFont(font8);
				lblCrosstalkPpseondb.setFont(font8);
				((JSpinner.NumberEditor) spinner_ppseOnCrosstalk.getEditor()).getTextField().setFont(font8);

				((GridBagLayout) panelMiscellaneous.getLayout()).columnWidths = new int[] { (int) (width * 0.78),
						(int) (width * 0.21), (int) (width * 0.01) };
				((GridBagLayout) panelMiscellaneous.getLayout()).rowHeights = new int[] { rowHeight, rowHeight,
						rowHeight, rowHeight };
				lblMiscellaneous.setFont(font11);
				lblSensitivity.setFont(font8);
				((JSpinner.NumberEditor) spinner_Sensitivity.getEditor()).getTextField().setFont(font8);
				lblLaseEfficiency.setFont(font8);
				((JSpinner.NumberEditor) spinner_LaserEfficiency.getEditor()).getTextField().setFont(font8);
				lblModulationRate.setFont(font8);
				((JSpinner.NumberEditor) spinner_ModulationRate.getEditor()).getTextField().setFont(font8);

				// panelApplicationMapping
				height = (int) ((panelOpticalcoefficients.getHeight() - 30) * 0.2);
				rowHeight = height / 3;

				panelApplication.setSize(width, height);
				panelMapping.setSize(width, (panelOpticalcoefficients.getHeight() - 30) - height);
				panelMapping.setLocation(10, panelApplication.getLocation().y + height + 10);
				((GridBagLayout) panelApplication.getLayout()).columnWidths = new int[] { (int) (width * 0.40),
						(int) (width * 0.59), (int) (width * 0.01) };
				((GridBagLayout) panelApplication.getLayout()).rowHeights = new int[] { rowHeight, rowHeight };
				rowHeight = ((panelOpticalcoefficients.getHeight() - 30) - height) / 9;
				((GridBagLayout) panelMapping.getLayout()).columnWidths = new int[] { (int) (width * 0.40),
						(int) (width * 0.59), (int) (width * 0.01) };
				((GridBagLayout) panelMapping.getLayout()).rowHeights = new int[] { rowHeight, rowHeight, rowHeight,
						rowHeight, rowHeight, rowHeight, rowHeight, rowHeight };
				lblApplication.setFont(font11);
				lblFileName.setFont(font8);
				TxtApp.setFont(font8);
				lblMappingStrategy.setFont(font11);
				lblType_3.setFont(font8);
				comboBox_mappingType.setFont(font8);
				lblObjective.setFont(font8);
				comboBox_mappingObjective.setFont(font8);
				lblIterations.setFont(font8);
				lblIterationSame.setFont(font8);
				radioButton_StopCondition.setFont(font8);
				((JSpinner.NumberEditor) spinner_iterations.getEditor()).getTextField().setFont(font8);
				lblPopulationSize.setFont(font8);
				lblPopulationSize_1.setFont(font8);
				((JSpinner.NumberEditor) spinner_populationSize.getEditor()).getTextField().setFont(font8);
				lblOffspringSize.setFont(font8);
				((JSpinner.NumberEditor) spinner_offspringSize.getEditor()).getTextField().setFont(font8);
				radioButton_defaultGeneticParameters.setFont(font8);

				// panelNoCArchitecture
				height = (int) ((panelOpticalcoefficients.getHeight() - 50) * 0.4);
				panelTopology.setSize(width, height);
				rowHeight = height / 5;
				height = (int) ((panelOpticalcoefficients.getHeight() - 50) * 0.2);
				rowHeight = height / 3;

				((GridBagLayout) panelTopology.getLayout()).columnWidths = new int[] { (int) (width * 0.40),
						(int) (width * 0.59), (int) (width * 0.01) };
				((GridBagLayout) panelTopology.getLayout()).rowHeights = new int[] { rowHeight, rowHeight, rowHeight,
						rowHeight };
				lblTopology.setFont(font11);
				lblType.setFont(font8);
				lblRows.setFont(font8);
				lblColumns.setFont(font8);
				comboBox_topology.setFont(font8);
				((JSpinner.NumberEditor) spinner_rows.getEditor()).getTextField().setFont(font8);
				((JSpinner.NumberEditor) spinner_columns.getEditor()).getTextField().setFont(font8);

				height = (int) ((panelOpticalcoefficients.getHeight() - 50) * 0.4);
				panelRouting.setLocation(10, panelTopology.getLocation().y + height + 10);
				height = (int) ((panelOpticalcoefficients.getHeight() - 50) * 0.2);
				panelRouting.setSize(width, height);
				rowHeight = height / 3;
				((GridBagLayout) panelRouting.getLayout()).columnWidths = new int[] { (int) (width * 0.40),
						(int) (width * 0.59), (int) (width * 0.01) };
				((GridBagLayout) panelRouting.getLayout()).rowHeights = new int[] { rowHeight, rowHeight };
				lblRouting.setFont(font11);
				lblType_1.setFont(font8);
				comboBox_routing.setFont(font8);

				panelRouter.setLocation(10, panelRouting.getLocation().y + height + 10);
				panelRouter.setSize(width, height);
				rowHeight = height / 3;
				((GridBagLayout) panelRouter.getLayout()).columnWidths = new int[] { (int) (width * 0.40),
						(int) (width * 0.59), (int) (width * 0.01) };
				((GridBagLayout) panelRouter.getLayout()).rowHeights = new int[] { rowHeight, rowHeight };
				lblOpticalRouter.setFont(font11);
				lblType_2.setFont(font8);
				comboBox_router.setFont(font8);

				panelChip.setLocation(10, panelRouter.getLocation().y + height + 10);
				panelChip.setSize(width, height);
				rowHeight = height / 3;
				((GridBagLayout) panelChip.getLayout()).columnWidths = new int[] { (int) (width * 0.40),
						(int) (width * 0.59), (int) (width * 0.01) };
				((GridBagLayout) panelChip.getLayout()).rowHeights = new int[] { rowHeight, rowHeight };
				lblChip.setFont(font11);
				lblSize.setFont(font8);
				((JSpinner.NumberEditor) spinner_chipSize.getEditor()).getTextField().setFont(font8);

				// panelExecution
				btnNewButton.setFont(font8);
				rdbtnWriting.setFont(font8);
				lblMappingOptimization.setFont(font8);
				progressBarMapping.setFont(font8);
				textArea.setFont(fontTxtArea);

			}
		});
		
		comboBox_topology.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (((String) comboBox_topology.getSelectedItem()).compareTo(Mesh.class.getSimpleName()) == 0){
					comboBox_routing.setSelectedItem((String) XYMesh.class.getSimpleName());
					if(comboBox_router_model.getIndexOf((String) Crux.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) Crux.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) OXY.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) OXY.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) ODOR.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) ODOR.class.getSimpleName());
					}
				}
				if (((String) comboBox_topology.getSelectedItem()).compareTo(TorusUnfolded.class.getSimpleName()) == 0){
					comboBox_routing.setSelectedItem((String) XYTorusUnfolded.class.getSimpleName());
					if(comboBox_router_model.getIndexOf((String) Crux.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) Crux.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) OXY.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) OXY.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) ODOR.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) ODOR.class.getSimpleName());
					}
				}
				if (((String) comboBox_topology.getSelectedItem()).compareTo(TorusUnfoldedOptimized.class.getSimpleName()) == 0){
					comboBox_routing.setSelectedItem((String) XYTorusUnfoldedOptimized.class.getSimpleName());
					if(comboBox_router_model.getIndexOf((String) Crux.class.getSimpleName()) != -1 ){
						comboBox_router_model.removeElement((String) Crux.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) OXY.class.getSimpleName()) != -1 ){
						comboBox_router_model.removeElement((String) OXY.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) ODOR.class.getSimpleName()) != -1 ){
						comboBox_router_model.removeElement((String) ODOR.class.getSimpleName());
					}
				}
				if (((String) comboBox_topology.getSelectedItem()).compareTo(TorusFolded.class.getSimpleName()) == 0){
					comboBox_routing.setSelectedItem((String) XYTorusFolded.class.getSimpleName());
					if(comboBox_router_model.getIndexOf((String) Crux.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) Crux.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) OXY.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) OXY.class.getSimpleName());
					}
					if(comboBox_router_model.getIndexOf((String) ODOR.class.getSimpleName()) == -1 ){
						comboBox_router_model.addElement((String) ODOR.class.getSimpleName());
					}
				}
			}
		});

		comboBox_routing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(((String) comboBox_routing.getSelectedItem()).compareTo((String) XYMesh.class.getSimpleName())==0){
					comboBox_topology.setSelectedItem((String) Mesh.class.getSimpleName());
				}
				if(((String) comboBox_routing.getSelectedItem()).compareTo((String) XYTorusUnfolded.class.getSimpleName())==0){
					comboBox_topology.setSelectedItem((String) TorusUnfolded.class.getSimpleName());
				}
				if(((String) comboBox_routing.getSelectedItem()).compareTo((String) XYTorusUnfoldedOptimized.class.getSimpleName())==0){
					comboBox_topology.setSelectedItem((String) TorusUnfoldedOptimized.class.getSimpleName());
				}
				if(((String) comboBox_routing.getSelectedItem()).compareTo((String) XYTorusFolded.class.getSimpleName())==0){
					comboBox_topology.setSelectedItem((String) TorusFolded.class.getSimpleName());
				}
			}
		});
		
		comboBox_mappingType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(((String) comboBox_mappingType.getSelectedItem()).compareTo((String) GeneticMapping.class.getSimpleName())==0){
					radioButton_defaultGeneticParameters.setEnabled(true);
				}
				if(((String) comboBox_mappingType.getSelectedItem()).compareTo((String) ListMapping.class.getSimpleName())==0){
					radioButton_defaultGeneticParameters.setEnabled(false);
				}
				if(((String) comboBox_mappingType.getSelectedItem()).compareTo((String) RandomMapping.class.getSimpleName())==0){
					radioButton_defaultGeneticParameters.setEnabled(false);
				}
			}
		});
	}
}
