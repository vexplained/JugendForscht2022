package de.vexplained.jufoSim;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.tuple.ImmutablePair;

import de.vexplained.jufoSim.graphicsExtension.DecimalSlider;
import de.vexplained.jufoSim.graphicsExtension.DynamicFunctionPlot;
import de.vexplained.jufoSim.graphicsExtension.MathFunction2D;
import de.vexplained.stdGraphics.DynamicCanvas;

/**
 * @author vExplained
 *
 */
public class MainGUI
{

	private JFrame frame;
	private DynamicCanvas canvasDoppler;
	private DecimalSlider sliderDist;
	private DynamicFunctionPlot plotDoppler;
	private DynamicFunctionPlot plotLevel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1)
		{
			e1.printStackTrace();
		}

		frame = new JFrame();
		frame.setBounds(100, 100, 720, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JPanel controllerPanel = new JPanel();
		GridBagConstraints gbc_controllerPanel = new GridBagConstraints();
		gbc_controllerPanel.gridwidth = 2;
		gbc_controllerPanel.insets = new Insets(5, 5, 5, 5);
		gbc_controllerPanel.fill = GridBagConstraints.BOTH;
		gbc_controllerPanel.gridx = 0;
		gbc_controllerPanel.gridy = 0;
		panel.add(controllerPanel, gbc_controllerPanel);
		GridBagLayout gbl_controllerPanel = new GridBagLayout();
		gbl_controllerPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_controllerPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_controllerPanel.columnWeights = new double[] { 2.0, 2.0, Double.MIN_VALUE };
		gbl_controllerPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		controllerPanel.setLayout(gbl_controllerPanel);

		JSlider slider = new JSlider();
		slider.setVisible(false);
		slider.setToolTipText("Ort des Fahrzeugs auf der Stra\u00DFe");
		slider.setValue(0);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(2);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.insets = new Insets(0, 0, 5, 0);
		gbc_slider.gridwidth = 2;
		gbc_slider.fill = GridBagConstraints.BOTH;
		gbc_slider.gridx = 0;
		gbc_slider.gridy = 0;
		controllerPanel.add(slider, gbc_slider);

		JCheckBox chckbxShowDoppler = new JCheckBox("Dopplereffekt anzeigen");
		chckbxShowDoppler.setForeground(new Color(0x4484cd));
		chckbxShowDoppler.setFont(new Font("Tahoma", Font.BOLD, 18));
		chckbxShowDoppler.setSelected(true);
		chckbxShowDoppler.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				plotDoppler.setVisible(chckbxShowDoppler.isSelected());
			}
		});
		GridBagConstraints gbc_chckbxShowDoppler = new GridBagConstraints();
		gbc_chckbxShowDoppler.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxShowDoppler.gridx = 0;
		gbc_chckbxShowDoppler.gridy = 1;
		controllerPanel.add(chckbxShowDoppler, gbc_chckbxShowDoppler);

		JCheckBox chckbxShowLevel = new JCheckBox("Pegel\u00E4nderung anzeigen");
		chckbxShowLevel.setForeground(new Color(0xdc5959));
		chckbxShowLevel.setFont(new Font("Tahoma", Font.BOLD, 18));
		chckbxShowLevel.setSelected(true);
		chckbxShowLevel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				plotLevel.setVisible(chckbxShowLevel.isSelected());
			}
		});
		GridBagConstraints gbc_chckbxShowLevel = new GridBagConstraints();
		gbc_chckbxShowLevel.gridx = 1;
		gbc_chckbxShowLevel.gridy = 1;
		controllerPanel.add(chckbxShowLevel, gbc_chckbxShowLevel);

		sliderDist = new DecimalSlider(2);
		sliderDist.setFont(new Font("Tahoma", Font.PLAIN, 16));
		sliderDist.setDecimalSliderValue(1d);
		sliderDist.setDecimalSliderMinimum(0d);
		sliderDist.setDecimalSliderMaximum(5d);
		sliderDist.setToolTipText("Abstand Stra\u00DFe - Mikrofon");
		sliderDist.setDecimalSliderMajorTickSpacing(1d);
		sliderDist.setDecimalSliderMinorTickSpacing(0.1d);
		sliderDist.setPaintTicks(true);
		sliderDist.setPaintLabels(true);
		sliderDist.setPrecision(2);
		sliderDist.setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_slider_1 = new GridBagConstraints();
		gbc_slider_1.fill = GridBagConstraints.BOTH;
		gbc_slider_1.insets = new Insets(0, 0, 0, 5);
		gbc_slider_1.gridx = 0;
		gbc_slider_1.gridy = 1;
		panel.add(sliderDist, gbc_slider_1);

		JTabbedPane displayPanel = new JTabbedPane();
		displayPanel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_displayPanel = new GridBagConstraints();
		gbc_displayPanel.fill = GridBagConstraints.BOTH;
		gbc_displayPanel.gridx = 1;
		gbc_displayPanel.gridy = 1;
		panel.add(displayPanel, gbc_displayPanel);

		canvasDoppler = new DynamicCanvas();
		canvasDoppler.setBackground(Color.WHITE);
		displayPanel.addTab("Dopplereffekt / Pegel\u00E4nderung", null, canvasDoppler, null);

		MathFunction2D funcDoppler = new MathFunction2D()
		{
			private double hoch = 3d;
			private double tief = -3d;
			private double d = 5d;

			private double inter_y(double x)
			{
				return hoch * Math.sin(Math.PI / 2d * (x / (d * sliderDist.getDecimalSliderValue())));
			}

			@Override
			public double y(double x)
			{
				// if (x < -8)
				// {
				// return Double.NaN;
				// }
				if (x < -d * sliderDist.getDecimalSliderValue())
				{
					return hoch;
				} else if (x > d * sliderDist.getDecimalSliderValue())
				{
					return tief;
				} else
				{
					return -inter_y(x);
				}
			}

			@Override
			public int hashCode()
			{
				return super.hashCode() ^ sliderDist.getValue();
			}
		};

		plotDoppler = new DynamicFunctionPlot(new Color(0x4484cd), false, new ImmutablePair<Double, Double>(-15d, 15d),
				new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), 0, funcDoppler);

		canvasDoppler.addObject(plotDoppler);

		sliderDist.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				plotDoppler.invalidateFunction();
			}
		});

		MathFunction2D funcLevel = new MathFunction2D()
		{
			private double d = 1.6612089648975;

			private double links(double x)
			{
				return -20 * Math.log10(-x);
			}

			private double inter(double x)
			{
				return -1.5975 * x * x;
			}

			private double rechts(double x)
			{
				return links(-x);
			}

			@Override
			public double y(double x)
			{
				double result = 0;
				if (x < -d)
				{
					return links(x);
				} else if (x > d)
				{
					return rechts(x);
				} else
				{
					return inter(x);
				}
			}
		};

		plotLevel = new DynamicFunctionPlot(new Color(0xdc5959), false, new ImmutablePair<Double, Double>(-40d, 40d),
				new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), 0, funcLevel);

		canvasDoppler.addObject(plotLevel);

		// MathFunction2D tempFunc = new MathFunction2D()
		// {
		//
		// @Override
		// public double y(double x)
		// {
		// return 2 * x * x * x - 3 * x * x + 1;
		// }
		// };
		// DynamicFunctionPlot plotTemp = new DynamicFunctionPlot(Color.MAGENTA, false,
		// new ImmutablePair<Double, Double>(0d, 1d),
		// new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), 100, tempFunc);
		// // canvasDoppler.addObject(plotTemp);
		//
		// MathFunction2D tempFunc2 = new MathFunction2D()
		// {
		//
		// @Override
		// public double y(double x)
		// {
		// return Math.cos(200 * Math.PI * x);
		// }
		// };
		// DynamicFunctionPlot plotTemp2 = new DynamicFunctionPlot(Color.GREEN, false,
		// new ImmutablePair<Double, Double>(0d, 0.5d),
		// new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), 1000, tempFunc2);
		// canvasDoppler.addObject(plotTemp2);

	}

}
