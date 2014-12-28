import javax.media.opengl.GL2;

/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	October 3, 2014
 *
 *	Thigh.java: Contains display list to draw the upper part
 *		of the leg and a joint object for the hip.
 *
 *	- Can call Calf object to pass instructions to the rest of the leg
 *		
 *
 *	For CS480 at Boston University
 *
 */

public class Thigh
{
	// Thigh display list
	private int DL;
	
	// Thigh length
	private float hip_length = .5f;

	// Building Block Objects
	private SolidCylinder tube;
	private Sphere sphere;
	
	// Store calf and hip objects
	public Calf calf;
	public Joint hip;
	
	// Flag. Is hip active?
	public boolean active;

	/*
	 * Thigh constructor
	 * 
	 * - stores building block objects and creates a new 
	 * 		hip joint and calf.
	 */
	public Thigh( SolidCylinder tube , Sphere sphere )
	{
		this.tube = tube;
		this.sphere = sphere;
		this.calf = new Calf( tube, sphere );
		this.hip = new Joint( -10, 60, 25 );	/* min, max, initial angles */
	}
	
	/*
	 * Initialize thigh
	 * 
	 * - pass init() call down leg through calf.
	 * - Create a new display list for thigh
	 */
	public void init( GL2 gl )
	{
		this.calf.init( gl );
		this.DL = gl.glGenLists(1);
		
		gl.glNewList(this.DL, GL2.GL_COMPILE);
		gl.glPushAttrib( GL2.GL_CURRENT_BIT );
			
			// sets drawing color based on active state 
			this.set_color( gl );
			
				gl.glPushMatrix();
			
					// Set rotation angle with hip display list
					this.hip.init( gl );
			
					gl.glPushMatrix();
						// draw a tube with correct length for thigh
						gl.glScalef( 0.05f, 0.05f, this.hip_length );
						this.tube.draw( gl );
					gl.glPopMatrix();
    			
					// Move to the end of the hip and draw the rest of the leg
					gl.glTranslatef( 0f, 0f, this.hip_length );
					this.calf.draw(gl);
				
				gl.glPopMatrix();
			gl.glPopAttrib();
		gl.glEndList();
	}
	
	/*
	 * Calls thigh's display list
	 * 
	 */
	public void draw( GL2 gl  )
	{
		gl.glCallList( this.DL );
	}
	
	/*
	 * - Will rotate knee joint if it is active
	 * - Passes increment down to next leg part
	 * 
	 */
	public void increment() 
	{
		if (this.calf.knee.active)
		{
			this.calf.knee.rotate( true );
			this.calf.increment();
		}
	
	}
	
	/*
	 * - Will rotate knee joint if it is active
	 * - Passes decrement down to next leg part
	 * 
	 */
	public void decrement()
	{
		if (this.calf.knee.active)
		{
			this.calf.knee.rotate( false );
			this.calf.decrement();
		}
	}
	
	/*
	 * Sets color of thigh.
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