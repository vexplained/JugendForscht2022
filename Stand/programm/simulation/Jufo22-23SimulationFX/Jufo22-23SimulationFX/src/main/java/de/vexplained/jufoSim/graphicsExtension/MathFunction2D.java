package de.vexplained.jufoSim.graphicsExtension;

public abstract class MathFunction2D
{
	public abstract double y(double x);

	/**
	 * May be overriden to change the hashcode if the function is dependent on an external variable (which has changed).
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
}
