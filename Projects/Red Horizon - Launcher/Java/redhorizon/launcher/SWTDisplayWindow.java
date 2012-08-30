
package redhorizon.launcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * SWT implementation of Red Horizon's display window.
 * 
 * @author Emanuel Rabina
 */
public class SWTDisplayWindow extends DisplayWindow {

	private final Display display;
	private final Shell shell;
	private final GLCanvas canvas;

	/**
	 * Constructor, sets the attached display renderer and defaults to a
	 * width/height of 800/600.
	 * 
	 * @param title	   Title of this window.
	 * @param renderer Listener for the rendering event.
	 */
	public SWTDisplayWindow(String title, DisplayRenderer renderer) {

		this(title, 800, 600, renderer);
	}

	/**
	 * Constructor, sets the attached display renderer.
	 * 
	 * @param title	   Title of this window.
	 * @param width	   Horizontal resolution of the OpenGL canvas.
	 * @param height   Vertical resolution of the OpenGL canvas.
	 * @param renderer Listener for the rendering event.
	 */
	public SWTDisplayWindow(String title, int width, int height, final DisplayRenderer renderer) {

		super(renderer);

		display = Display.getDefault();

		// Window
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.DOUBLE_BUFFERED);
		shell.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setText(title);
		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				renderer.displayShutdown();
			}
		});
		shell.setLayout(SWTUtility.createLayout());

		// OpenGL pixel descriptor
		GLData gldata = new GLData();
		gldata.doubleBuffer = true;
		gldata.sampleBuffers = 2;
		gldata.samples = 4;

		// OpenGL canvas
		canvas = new GLCanvas(shell, SWT.NONE, gldata);
		canvas.setLayoutData(new GridData(width, height));
	}

	/**
	 * Closes the window, being sure not to interfere with the rendering thread.
	 */
	@Override
	protected void closeImpl() {

		display.syncExec(new Runnable() {
			@Override
			public void run() {
				shell.dispose();
			}
		});
	}

	/**
	 * Centers and displays the window, starting the rendering thread on the
	 * display.
	 */
	@Override
	protected void openImpl() {

		shell.pack();
		SWTUtility.centerShell(shell);
		shell.open();

		// Begin render loop
		canvas.setCurrent();
		renderer.displayStartup();

		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!shell.isDisposed()) {
					renderer.displayRendering();
					canvas.swapBuffers();
					display.timerExec(5, this);
				}
			}
		});

		// Wait until closed
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}