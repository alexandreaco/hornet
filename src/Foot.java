import javax.media.opengl.GL2;

/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	October 3, 2014
 *
 *	Foot.java: Contains display list to draw the lowest part
 *		of the leg and a joint object for the ankle.
 *
 *	- The foot doesn't call any other part of the leg
 *
 *	For CS480 at Boston University
 *
 */

public class Foot
{
	// Display list
	private int DL;
	
	// Foot length
	private float foot_length = .25f;
	
	// Building Block Objects
	private SolidCylinder tube;
	private Sphere sphere;
	
	// Store ankle object
	public Joint ankle;
	
	// Flag. Is foot active?
	public boolean active;
		
	/*
	 * Foot Constructor
	 * 
	 * - stores building block objects and creates a new 
	 * 		ankle joint.
	 * 
	 */
	public Foot( SolidCylinder tube , Sphere sphere )
	{
		this.tube = tube;
		this.sphere = sphere;
		this.ankle =  new Joint( -45f , -5f , -40f );	/* min, max, initial angles */
	}
		
	/*
	 * Initialize foot
	 * 
	 * - Create a new display list for calf
	 */
	public void init( GL2 gl )
	{
		this.DL = gl.glGenLists(1);
		
		gl.glNewList(this.DL, GL2.GL_COMPILE);
		gl.glPushAttrib( GL2.GL_CURRENT_BIT );
		
			// sets drawing color based on active state
			this.set_color( gl );
			
				gl.glPushMatrix();
		
					// set rotation angle with knee display list
					this.ankle.init( gl );
				
					// draw a tube with correct length for foot
					gl.glScalef( 0.05f, 0.05f, this.foot_length );
					this.tube.draw( gl );
		
				gl.glPopMatrix();
			gl.glPopAttrib();
		gl.glEndList();
	}
		
	/*
	 * Call Foot's display list
	 * 
	 */
	public void draw( GL2 gl )
	{
		gl.glCallList( this.DL );
	}
	

	/*
	 * Sets color of foot.
	 * 
	 * - will be grey if it's inactive
	 * - will be red if it's active
	 * 
	 */
	private void set_color( GL2 gl ) 
	{
		float red, green, blue;
		if ( this.active )
		{
			red = (float) 255/255;
			green = (float) 10/255;
			blue = (float) 10/255;
		}
		else
		{
			red = (float) 50/255;
			green = (float) 50/255;
			blue = (float) 50/255;
		}
		gl.glColor3f( red , green , blue );

	}
}