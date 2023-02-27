package de.vexplained.jufoSim.graphicsExtension;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.ImmutablePair;

import de.vexplained.stdGraphics.DynamicObject;

/**
 * @author vExplained
 *
 */
public class DynamicFunctionPlot extends DynamicObject
{

	private boolean drawAxis;
	private ImmutablePair<Double, Double> xdomain;
	private ImmutablePair<Double, Double> ydomain;
	private MathFunction2D function;
	private int numberOfPointsToCalculate;
	private int changeablesHashcode;

	private double[][] functionArrayBuffer;

	/**
	 * Margin at the edges of the viewport, in pixels.
	 */
	private int viewportMargin = 10;

	/**
	 * Draws a plot of the specified function.
	 * 
	 * @param xdomain
	 *            The x-domain to calculate and plot function values for.
	 */
	public DynamicFunctionPlot(Color color, ImmutablePair<Double, Double> xdomain, Stroke stroke,
			MathFunction2D function)
	{
		this(color, true, xdomain, stroke, 0, function);
	}

	/**
	 * @param xdomain
	 *            The x-domain to calculate and plot function values for.
	 */
	public DynamicFunctionPlot(Color color, boolean drawAxis, ImmutablePair<Double, Double> xdomain, Stroke stroke,
			int numberOfPointsToCalculate, MathFunction2D function)
	{
		this(color, drawAxis, xdomain, new ImmutablePair<Double, Double>(0d, 0d), stroke, numberOfPointsToCalculate,
				function);
	}

	/**
	 * @param xdomain
	 *            The x-domain to calculate and plot function values for.
	 * @param ydomain
	 *            The y-domain used to limit which values should be displayed.
	 */
	public DynamicFunctionPlot(Color color, ImmutablePair<Double, Double> xdomain,
			ImmutablePair<Double, Double> ydomain, Stroke stroke, MathFunction2D function)
	{
		this(color, true, xdomain, ydomain, stroke, 0, function);
	}

	/**
	 * @param xdomain
	 *            The x-domain to calculate and plot function values for.
	 * @param ydomain
	 *            The y-domain used to limit which values should be displayed.
	 */
	public DynamicFunctionPlot(Color color, boolean drawAxis, ImmutablePair<Double, Double> xdomain,
			ImmutablePair<Double, Double> ydomain, Stroke stroke, int numberOfPointsToCalculate,
			MathFunction2D function)
	{
		super(color, 0, 0, stroke);
		this.drawAxis = drawAxis;
		this.xdomain = xdomain;
		this.ydomain = ydomain;
		this.function = function;
		this.numberOfPointsToCalculate = numberOfPointsToCalculate > 0 ? numberOfPointsToCalculate : 100;
		this.changeablesHashcode = this.xdomain.hashCode() ^ this.ydomain.hashCode()
				^ System.identityHashCode(this.numberOfPointsToCalculate);

		this.functionArrayBuffer = new double[2][0];

		this.functionArrayBuffer = calculateFunctionValues();
	}

	@Override
	public boolean isInShape(double x, double y)
	{
		return false;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		// TODO implement ydomain

		int newHashcode = this.xdomain.hashCode() ^ this.ydomain.hashCode()
				^ System.identityHashCode(this.numberOfPointsToCalculate);
		if (this.changeablesHashcode != newHashcode)
		{
			this.functionArrayBuffer = calculateFunctionValues();
		}

		// calculate viewport
		DoubleSummaryStatistics statsX = Arrays.stream(this.functionArrayBuffer[0]).summaryStatistics();
		DoubleSummaryStatistics statsY = Arrays.stream(this.functionArrayBuffer[1]).summaryStatistics();

		double minValueX = statsX.getMin();
		double maxValueX = statsX.getMax();
		double minValueY = statsY.getMin();
		double maxValueY = statsY.getMax();
		// get canvas size; assumes that clip has not been set
		Rectangle canvasDim = g2d.getClipBounds();
		int canvasMinX = (int) canvasDim.getX() + viewportMargin;
		int canvasMaxX = (int) canvasDim.getMaxX() - viewportMargin;
		// invert y-axis!
		int canvasMinY = (int) canvasDim.getMaxY() - viewportMargin;
		int canvasMaxY = (int) canvasDim.getY() + viewportMargin;
		int canvasWidth = (int) canvasDim.getWidth() - 2 * viewportMargin;
		int canvasHeight = (int) canvasDim.getHeight() - 2 * viewportMargin;

		// draw coordinate system
		// FIXME implement coordinate system display
		// TODO draw in the center of the screen when possible

		// draw function plot
		g2d.setColor(color);

		Path2D.Double funcPath = new Path2D.Double(Path2D.WIND_NON_ZERO, this.functionArrayBuffer[0].length);

		double prevX = mapToRange(this.functionArrayBuffer[0][0], minValueX, maxValueX, canvasMinX, canvasMaxX - 1);
		double prevY = mapToRange(this.functionArrayBuffer[1][1], minValueY, maxValueY, canvasMinY, canvasMaxY - 1);
		funcPath.moveTo(prevX, prevY);
		for (int i = 1; i < this.functionArrayBuffer[0].length; i++)
		{
			double xPos = mapToRange(this.functionArrayBuffer[0][i], minValueX, maxValueX, canvasMinX, canvasMaxX - 1);
			double yPos = mapToRange(this.functionArrayBuffer[1][i], minValueY, maxValueY, canvasMinY, canvasMaxY - 1);

			funcPath.lineTo(xPos, yPos);
		}

		g2d.draw(funcPath);
	}

	private void saveBIToFile(BufferedImage img)
	{
		try
		{
			File outputfile = new File("image.png");
			if (!outputfile.exists())
			{
				outputfile.createNewFile();
			}
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * @param value
	 *            the value to map into the range
	 * @param valMin
	 *            the minimum of the range of <code>value</code>
	 * @param valMax
	 *            the maximum of the range of <code>value</code>
	 * @param outMin
	 *            the minimum of the range of the returned result
	 * @param outMax
	 *            the maximum of the range of the returned result
	 */
	private double mapToRange(double value, double valMin, double valMax, double outMin, double outMax)
	{
		return (outMax - outMin) * (value - valMin) / (valMax - valMin) + outMin;
	}

	private double clamp(double value, double min, double max)
	{
		return Math.min(Math.max(value, min), max);
	}

	/**
	 * Calculates the function values for the given function.
	 * 
	 * @return a 2-dimensional array consisting of two rows: <code>[double[], double[]]</code>. The first array contains
	 *         the x-values, the second array contains the y-values.
	 */
	private double[][] calculateFunctionValues()
	{
		double[][] result = new double[2][this.numberOfPointsToCalculate];

		// Calculate interval to calculate values in
		double interval = Math.abs(xdomain.getRight() - xdomain.getLeft()) / (this.numberOfPointsToCalculate - 1);
		double lower = Math.min(xdomain.getLeft(), xdomain.getRight());
		double upper = Math.max(xdomain.getLeft(), xdomain.getRight());
		for (int i = 0; i < this.numberOfPointsToCalculate; i++)
		{
			double xVal = i * interval + lower;
			result[0][i] = xVal;
			result[1][i] = this.function.y(xVal);
		}

		return result;
	}

	/* ======================================= */
	/* ========= Getters and Setters ========= */
	/* ======================================= */

	public boolean isDrawAxis()
	{
		return drawAxis;
	}

	public void setDrawAxis(boolean drawAxis)
	{
		this.drawAxis = drawAxis;
	}

	public ImmutablePair<Double, Double> getXdomain()
	{
		return xdomain;
	}

	public void setXdomain(ImmutablePair<Double, Double> xdomain)
	{
		this.xdomain = xdomain;
	}

	public ImmutablePair<Double, Double> getYdomain()
	{
		return ydomain;
	}

	public void setYdomain(ImmutablePair<Double, Double> ydomain)
	{
		this.ydomain = ydomain;
	}

	public int getNumberOfPointsToCalculate()
	{
		return numberOfPointsToCalculate;
	}

	public void setNumberOfPointsToCalculate(int numberOfPointsToCalculate)
	{
		this.numberOfPointsToCalculate = numberOfPointsToCalculate;
	}

}
