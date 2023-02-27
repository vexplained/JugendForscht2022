package de.vexplained.jufoSim;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.lang3.tuple.ImmutablePair;

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
	private DynamicCanvas canvasLevel;

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
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JPanel controllerPanel = new JPanel();
		GridBagConstraints gbc_controllerPanel = new GridBagConstraints();
		gbc_controllerPanel.insets = new Insets(0, 0, 5, 0);
		gbc_controllerPanel.fill = GridBagConstraints.BOTH;
		gbc_controllerPanel.gridx = 0;
		gbc_controllerPanel.gridy = 0;
		panel.add(controllerPanel, gbc_controllerPanel);
		GridBagLayout gbl_controllerPanel = new GridBagLayout();
		gbl_controllerPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_controllerPanel.rowHeights = new int[] { 0, 0 };
		gbl_controllerPanel.columnWeights = new double[] { 2.0, 2.0, Double.MIN_VALUE };
		gbl_controllerPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		controllerPanel.setLayout(gbl_controllerPanel);

		JSlider slider = new JSlider();
		slider.setValue(0);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.gridwidth = 2;
		gbc_slider.fill = GridBagConstraints.BOTH;
		gbc_slider.gridx = 0;
		gbc_slider.gridy = 0;
		controllerPanel.add(slider, gbc_slider);

		JTabbedPane displayPanel = new JTabbedPane();
		GridBagConstraints gbc_displayPanel = new GridBagConstraints();
		gbc_displayPanel.fill = GridBagConstraints.BOTH;
		gbc_displayPanel.gridx = 0;
		gbc_displayPanel.gridy = 1;
		panel.add(displayPanel, gbc_displayPanel);

		canvasDoppler = new DynamicCanvas();
		canvasDoppler.setBackground(Color.WHITE);
		displayPanel.addTab("Dopplereffekt", null, canvasDoppler, null);

		canvasLevel = new DynamicCanvas();
		canvasLevel.setBackground(Color.WHITE);
		displayPanel.addTab("Pegel\u00E4nderung", null, canvasLevel, null);

		MathFunction2D funcDoppler = new MathFunction2D()
		{
			private double hoch = 3d;
			private double tief = -3d;
			private double d = 5d;

			private double inter_y(double x)
			{
				return hoch * Math.sin(Math.PI / 2d * (x / d));
			}

			@Override
			public double y(double x)
			{
				if (x < -d)
				{
					return -hoch;
				} else if (x > d)
				{
					return -tief;
				} else
				{
					return inter_y(x);
				}
			}
		};

		DynamicFunctionPlot plotDoppler = new DynamicFunctionPlot(Color.BLUE, false,
				new ImmutablePair<Double, Double>(-10d, 10d),
				new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), 500, funcDoppler);

		canvasDoppler.addObject(plotDoppler);

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

		DynamicFunctionPlot plotLevel = new DynamicFunctionPlot(Color.RED, false,
				new ImmutablePair<Double, Double>(-10d, 10d),
				new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND), 500, funcLevel);

		canvasDoppler.addObject(plotLevel);
		canvasLevel.addObject(plotLevel);
	}

}
