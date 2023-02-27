package de.vexplained.jufoSim.graphicsExtension;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

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
	 * Draws a plot of the specified function.
	 * 
	 * @param xdomain
	 *            The x-domain to calculate and plot function values for.
	 */
	public DynamicFunctionPlot(Color color, ImmutablePair<Double, Double> xdomain, Stroke stroke,
			MathFunction2D function)
	{
		this(color, true, xdomain, stroke, function, 0);
	}

	/**
	 * @param xdomain
	 *            The x-domain to calculate and plot function values for.
	 */
	public DynamicFunctionPlot(Color color, boolean drawAxis, ImmutablePair<Double, Double> xdomain, Stroke stroke,
			MathFunction2D function, int numberOfPointsToCalculate)
	{
		this(color, drawAxis, xdomain, new ImmutablePair<Double, Double>(0d, 0d), stroke, function,
				numberOfPointsToCalculate);
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
		this(color, true, xdomain, ydomain, stroke, function, 0);
	}

	/**
	 * @param xdomain
	 *            The x-domain to calculate and plot function values for.
	 * @param ydomain
	 *            The y-domain used to limit which values should be displayed.
	 */
	public DynamicFunctionPlot(Color color, boolean drawAxis, ImmutablePair<Double, Double> xdomain,
			ImmutablePair<Double, Double> ydomain, Stroke stroke, MathFunction2D function,
			int numberOfPointsToCalculate)
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

		// draw stuff here
		for (double[] element : this.functionArrayBuffer)
		{

		}
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
			double xVal = i * interval;
			result[0][i] = xVal;
			result[1][i] = this.function.apply(xVal);
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
