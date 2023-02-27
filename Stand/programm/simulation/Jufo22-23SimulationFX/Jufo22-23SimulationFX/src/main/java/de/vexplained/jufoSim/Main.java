package de.vexplained.jufoSim;

/**
 * @author vExplained
 *
 */
public class Main
{

	public static void main(String[] args)
	{
		System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
		System.setProperty("sun.java2d.opengl", "true");

		MainGUI mainGui = new MainGUI();
		mainGui.main(args);
	}

}
