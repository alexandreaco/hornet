import javax.media.opengl.GL2;

/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	October 3, 2014
 *
 *	Calf.java: Contains display list to draw the middle part
 *		of the leg and a joint object for the knee.
 *
 *	- Can call Foot object to pass instructions to the rest of the leg
 *
 *	For CS480 at Boston University
 *
 */

public class Calf
{
	// Display list
	private int DL;
	
	// Calf length
	private float calf_length = .75f;
	
	// Building Block Objects
	private SolidCylinder tube;
	private Sphere sphere;
	
	// Store foot and knee objects
	public Foot foot;
	public Joint knee;
	
	// Flag. Is calf active?
	public boolean active;
		
	/*
	 * Calf constructor
	 * 
	 * - stores building block objects and creates a new 
	 * 		knee joint and foot.
	 */
	public Calf( SolidCylinder tube , Sphere sphere )
	{
		this.tube = tube;
		this.sphere = sphere;
		
		this.knee = new Joint( 10f, 90f, 90f ); /* min, max, initial angles */
		this.foot = new Foot( tube, sphere );
	}
		
	/*
	 * Initialize calf
	 * 
	 * - pass init() call down leg through foot.
	 * - Create a new display list for calf
	 */
	public void init( GL2 gl )
	{
		this.foot.init( gl );
		this.DL = gl.glGenLists(1);
		
		gl.glNewList(this.DL, GL2.GL_COMPILE);
		gl.glPushAttrib( GL2.GL_CURRENT_BIT );
		
			// sets drawing color based on active state
			this.set_color( gl );
			
				gl.glPushMatrix();
	
					// set rotation angle with knee display list
					this.knee.init( gl );
				
					gl.glPushMatrix();
						// draw a tube with correct length for calf
						gl.glScalef( 0.05f, 0.05f, this.calf_length );
						this.tube.draw( gl );
					gl.glPopMatrix();
				
					// move to the end of the calf and draw the rest of the leg
					gl.glTranslatef( 0, 0, this.calf_length );
					this.foot.draw( gl );
				
				gl.glPopMatrix();
			gl.glPopAttrib();
		gl.glEndList();
	}
		
	/*
	 * Calls calf's display list
	 * 
	 */
	public void draw( GL2 gl )
	{
		gl.glCallList( this.DL );
	}
	
	/*
	 * - Will rotate ankle joint if it is active
	 * - Passes increment down to next leg part
	 * 
	 */
	public void increment()
	{
		if (this.foot.ankle.active)
		{
			this.foot.ankle.rotate( true );
		}
	}
	
	/*
	 * - Will rotate ankle joint if it is active
	 * - Passes decrement down to next leg part
	 * 
	 */
	public void decrement()
	{
		if (this.foot.ankle.active)
		{
			this.foot.ankle.rotate( false );
		}
	}
	
	/*
	 * Sets color of calf.
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