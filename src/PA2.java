import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl
import com.jogamp.opengl.util.gl2.GLUT;//for new version of gl

/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	September 25, 2014
 *
 *	Programming Assignment 2. This program renders a 3D model of a 
 *	hornet that can be manipulated via keyboard inputs. All 
 *	keyboard events and mouse events are handles within PA2.java. 
 *	The hornet object is self-maintaining and manages all state changes
 *	and updates. 
 *
 *	For CS480 at Boston University
 *
 */

public class PA2 extends JFrame implements GLEventListener, KeyListener,
MouseListener, MouseMotionListener 
{
	
	private final int DEFAULT_WINDOW_WIDTH  = 512;
	private final int DEFAULT_WINDOW_HEIGHT = 512;

	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private FPSAnimator animator;
	private GLU glu;
	private GLUT glut;
	
	// Building Block Objects
	private SolidCylinder tube;
	private Sphere sphere;
	private Hornet hornet;	
	
	// Object that handles test cases
	private TestCases test;

	// world rotation controlled by mouse actions
	private Quaternion viewing_quaternion; 
	

	// State variables for the mouse actions
	int last_x, last_y;
	boolean rotate_world;
	
	// Flag. Determines if scene needs to be redrawn
	private boolean stateChanged = true;
	
	
	public PA2() 
	{
		 
		capabilities = new GLCapabilities(null);
		capabilities.setDoubleBuffered(true);  // Enable Double buffering

		canvas  = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		canvas.setAutoSwapBufferMode(true); // true by default. Just to be explicit
		
		getContentPane().add(canvas);

		animator = new FPSAnimator(canvas, 60); // drive the display loop @ 60 FPS

		glu  = new GLU();
		glut = new GLUT();

		setTitle("CS480/CS680 : Programming Assignment 2");
		setSize( DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		   
		this.canvas.setFocusable(true);
		this.canvas.requestFocusInWindow();
		last_x = last_y = 0;
		rotate_world = false;

		// Set initialization code for user created classes that involves OpenGL
		// calls after here. After this line, the opengGl context will be
		// correctly initialized.
		   
		// Initialize test cases
		this.test = new TestCases();
		
		// Create building block objects
		tube = new SolidCylinder( .0, .0, .0, 1.0, 1.0, 20);
		sphere = new Sphere( .5 , glut);
		
		// Create hornet with building blocks
		hornet = new Hornet( tube , sphere );

		// Create viewing quaternion 
		viewing_quaternion = new Quaternion();
	}
	
	public void run()
	{
		animator.start();
	}

	public static void main( String[] args )
	{
		PA2 P = new PA2();
	    P.run();
	}
	
	
	//***************************************************************************
	  //GLEventListener Interfaces
	  //***************************************************************************
	  //
	  // Place all OpenGL related initialization here. Including display list
	  // initialization for user created classes
	  //
	  public void init( GLAutoDrawable drawable) 
	  {
		  GL2 gl = (GL2)drawable.getGL();


		  /* set up for shaded display of the hornet */
		  float light0_position[] = {1,1,1,0};
		  float light0_ambient_color[] = {0.25f,0.25f,0.25f,1};
		  float light0_diffuse_color[] = {1,1,1,1};

		  gl.glPolygonMode(GL2.GL_FRONT,GL2.GL_FILL);
		  gl.glEnable(GL2.GL_COLOR_MATERIAL);
		  gl.glColorMaterial(GL2.GL_FRONT,GL2.GL_AMBIENT_AND_DIFFUSE);

		  gl.glClearColor(0.0f,0.0f,0.0f,0.0f);
		  gl.glShadeModel(GL2.GL_SMOOTH);
	    
		  /* set up the light source */
		  gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
		  gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light0_ambient_color, 0);
		  gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse_color, 0);

		  /* turn lighting and depth buffering on */
		  gl.glEnable(GL2.GL_LIGHTING);
		  gl.glEnable(GL2.GL_LIGHT0);
		  gl.glEnable(GL2.GL_DEPTH_TEST);
		  gl.glEnable(GL2.GL_NORMALIZE);

		  // Initialize hornet and associated building block objects
		  tube.init( gl );
		  sphere.init( gl );
		  hornet.init( gl );
	  }
	
	  
	/* 
	 *	Redisplay graphics
	 * 
	 *	- if state change flag is triggered, 
	 * 	update all hornet display lists
	 * 
	 *	- redraw hornet to buffer
	 * 
	 */
	  public void display(GLAutoDrawable drawable)
	  {
	    GL2 gl = (GL2)drawable.getGL();

	    // clear the display 
	    gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	    
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    gl.glLoadIdentity();

	    // rotate the world and then call world display list object 
	    gl.glMultMatrixf( viewing_quaternion.to_matrix(), 0 );
	    
	    if (this.stateChanged)
	    {
	    	hornet.update( gl );
	    	this.stateChanged = false;
	    }
	    hornet.draw( gl );

	  }
	  
	/*
	 *	Changes window size
	 *	
	 */	
	  public void reshape(GLAutoDrawable drawable, int x, int y, 
	                            int width, int height)
	  {

	    // Change viewport dimensions
	    GL2 gl = (GL2)drawable.getGL();

	    // Prevent a divide by zero, when window is too short (you cant make a
	    // window of zero width).
	    if(height == 0) height = 1;

	    double ratio = 1.0f * width / height;

	    // Reset the coordinate system before modifying 
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    
	    // Set the viewport to be the entire window 
	    gl.glViewport(0, 0, width, height);
	    
	    // Set the clipping volume 
	    glu.gluPerspective(25,ratio,0.1,100);

	    // Camera positioned at (0,0,6), look at point (0,0,0), Up Vector (0,1,0)
	    glu.gluLookAt(0,0,12,0,0,0,0,1,0);

	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    
	  }
	  
	  
	  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
		      boolean deviceChanged)
	  {
	  }
	  
	  
	  /*********************************************** 
	   *          KeyListener Interfaces
	   ***********************************************
	   *
	   *	Q, q : quit program
	   *
	   *	R, r : reset viewing quaternion
	   *
	   *	1, 2, 3	: toggle respective joint in all 
	   *			active legs.
	   *	
	   *	F : toggle front left leg
	   *
	   *	f : toggle front right leg
	   *
	   *	M : toggle middle left leg
	   *
	   *	m : toggle middle right leg
	   *
	   *	B : toggle back left leg
	   *
	   *	b : toggle back right leg
	   *
	   *
	   *	T, t : cycle through demo test poses
	   *
	   */
	  public void keyTyped(KeyEvent key)
	  {
	      switch ( key.getKeyChar() ) {
	        case 'Q' :
	        case 'q' : new Thread() {
	                     public void run()
	                     { animator.stop(); }
	                   }.start();
	                   System.exit(0);
	                   break;

	        // set the viewing quaternion to 0 rotation 
	        case 'R' :
	        case 'r' : 
	                   viewing_quaternion.reset(); 
	                   break;

	        // Toggle which joint(s) are affected by the current rotation
	        case '1' : hornet.toggle_joint( 1 );
	        		   this.stateChanged = true;
	                   break;
	        case '2' : hornet.toggle_joint( 2 );
	        		   this.stateChanged = true;
	        		   break;
	        case '3' : hornet.toggle_joint( 3 );
	        		   this.stateChanged = true;
	                   break;

	        // select leg
	        case 'F' : hornet.toggle_limb( "front_left" );
	        		   this.stateChanged = true;
	        		   break;
	        	
	        case 'f' : hornet.toggle_limb( "front_right" );
	        		   this.stateChanged = true;
	        		   break;
	        
	        case 'M' : hornet.toggle_limb( "middle_left" );
	        		   this.stateChanged = true;		   
	        		   break;
	        
	        case 'm' : hornet.toggle_limb( "middle_right" );
	        		   this.stateChanged = true;
 		   			   break;
	        
	        case 'B' : hornet.toggle_limb( "back_left" );
	        		   this.stateChanged = true;
 		   			   break;
	        
	        case 'b' : hornet.toggle_limb( "back_right" );
	        		   this.stateChanged = true;
 		   			   break;

	        // Demo keys
	        case 'T' : 
	        case 't' : this.test.cycle_test( this.hornet );
	        		   this.stateChanged = true;
	                   break;
	        
	        default :
	          break;
	    	}
	 	}
	
	  
	  /*********************************************** 
	   *       ArrowKeyListener Interfaces
	   ***********************************************
	   *
	   *	up-arrow : increment active joints by 5 degrees
	   *
	   *	down-arrow : decrement active joints by 5 degrees
	   *
	   */
	  public void keyPressed(KeyEvent key)
	  {
	    switch (key.getKeyCode()) {
	      case KeyEvent.VK_ESCAPE:
	        new Thread()
	        {
	          public void run()
	          {
	            animator.stop();
	          }
	        }.start();
	        System.exit(0);
	        break;

	        // Up arrow key
	      case KeyEvent.VK_KP_UP :
	      case KeyEvent.VK_UP    :
	        this.hornet.increment_joints();
	        this.stateChanged = true;
	        break;

	        // down arrow key
	      case KeyEvent.VK_KP_DOWN :
	      case KeyEvent.VK_DOWN :
	        this.hornet.decrement_joints();
	        this.stateChanged = true;
	        break;

	      default:
	        break;
	    }
	  }

	  public void keyReleased(KeyEvent key)
	  {
	  }

	  //************************************************** 
	  // MouseListener and MouseMotionListener Interfaces
	  //************************************************** 
	  public void mouseClicked(MouseEvent mouse)
	  {
	  }

	  public void mousePressed(MouseEvent mouse)
	  {
	    int button = mouse.getButton();
	    if ( button == MouseEvent.BUTTON1 )
	    {
	      last_x = mouse.getX();
	      last_y = mouse.getY();
	      rotate_world = true;
	    }
	  }

	  public void mouseReleased(MouseEvent mouse)
	  {
	    int button = mouse.getButton();
	    if ( button == MouseEvent.BUTTON1 )
	    {
	      rotate_world = false;
	    }
	  }

	  public void mouseMoved( MouseEvent mouse)
	  {
	  }

	  public void mouseDragged(final MouseEvent mouse) {
		  if (this.rotate_world) {
		      // get the current position of the mouse
			  final int x = mouse.getX();
			  final int y = mouse.getY();
		 
		        // get the change in position from the previous one
			  final int dx = x - this.last_x;
			  final int dy = y - this.last_y;
		 
			  // create a unit vector in the direction of the vector (dy, dx, 0)
			  final double magnitude = Math.sqrt(dx * dx + dy * dy); // this can be 0 on MAC and Linux
			  final float[] axis = magnitude == 0 ? new float[]{1,0,0}: // avoid dividing by 0
				  new float[] { (float) (dy / magnitude),(float) (dx / magnitude), 0 };
		 
			  // calculate appropriate quaternion
			  final float viewing_delta = 3.1415927f / 180.0f;
			  final float s = (float) Math.sin(0.5f * viewing_delta);
			  final float c = (float) Math.cos(0.5f * viewing_delta);
			  final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
					  * axis[2]);
			  this.viewing_quaternion = Q.multiply(this.viewing_quaternion);
		 
			  // normalize to counteract acccumulating round-off error
			  this.viewing_quaternion.normalize();
		 
			  // save x, y as last x, y
			  this.last_x = x;
			  this.last_y = y;
		  }   
	  }

	  public void mouseEntered( MouseEvent mouse)
	  {
	  }

	  public void mouseExited( MouseEvent mouse)
	  {
	  }

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	} 	
}