import javax.media.opengl.GL2;

/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	September 25, 2014
 *
 *	Leg.java: Skeleton for all legs on the hornet. Only has control
 *	over the number of active joints and the thigh.
 *
 *	- Contains displaylists for both left and right orientations of legs.
 *
 *	For CS480 at Boston University
 *
 */

public class Leg 
{
	// Display Lists
	private int right_leg_dl;
	private int left_leg_dl;
	
	// location of leg starting point
	public Point3D location;
	
	// Building Block Objects
	public Thigh thigh;
	private String name;	// For debugging purposes.
	
	// Flag. Is the leg active?
	public boolean active;
	
	// Number of active joints in leg. Can be 1, 2, or 3
	public int num_active_joints;
	
	/* 
	 * Leg Constructor
	 * 
	 * - Stores the leg name and  the starting position
	 * - Creates a thigh 
	 * - Sets active state to false;
	 * 
	 */
	public Leg( String name, SolidCylinder tube , Sphere sphere, Point3D location )
	{
		this.name = name;
		this.thigh = new Thigh( tube, sphere );
		this.location = new Point3D( location.x(), location.y(), location.z() );
		this.active = false;
	}
	
	/*
	 * Creates a display list for right-sided leg
	 * 
	 */
	public void init_right( GL2 gl )
	{
		this.thigh.init( gl );
		this.right_leg_dl = gl.glGenLists(1);

		
	    gl.glNewList(this.right_leg_dl, GL2.GL_COMPILE);
	    
	    	gl.glPushAttrib( GL2.GL_CURRENT_BIT );
	    	this.set_color( gl );
	    		gl.glPushMatrix();
	    	
	    		// Go to leg's location
	    		gl.glTranslated(this.location.x(), this.location.y(), this.location.z());

	    			this.thigh.draw( gl );
	
	    		gl.glPopMatrix();
	    	gl.glPopAttrib();
	    gl.glEndList();
		
	}
	
	/*
	 * Creates a display list for lef-sided leg
	 * 
	 */
	public void init_left( GL2 gl )
	{
		
		this.thigh.init( gl );
		this.left_leg_dl = gl.glGenLists(1);

		
	    gl.glNewList(this.left_leg_dl, GL2.GL_COMPILE);
	    	gl.glPushAttrib( GL2.GL_CURRENT_BIT );
    		this.set_color( gl );	
	    		
    			gl.glPushMatrix();
	    	
	    			// Go to leg's location
    				gl.glTranslated(this.location.x(), this.location.y(), this.location.z());

    				// Reflect leg onto other side of insect
    				gl.glScalef(1f, 1f, -1f);
    		
    				gl.glPushMatrix();
	    			
	    				this.thigh.draw( gl );
	    		
	    			gl.glPopMatrix();
	    		gl.glPopMatrix();
	    	gl.glPopAttrib();
	    gl.glEndList();
	}
	
	/*
	 * Calls left display list
	 * 
	 */
	public void draw_left( GL2 gl )
	{
		gl.glCallList(this.left_leg_dl);
	}
	
	/*
	 * Calls right display list
	 * 
	 */
	public void draw_right( GL2 gl )
	{
		gl.glCallList(this.right_leg_dl);
	}
	
	/*
	 * - Will rotate hip joint if it is active
	 * - Passes increment down to next leg part
	 * 
	 */
	public void increment() 
	{
		if (this.thigh.hip.active)
		{
			this.thigh.hip.rotate( true );
			this.thigh.increment();
		}
	
	}
	
	/*
	 * - Will rotate hip joint if it is active
	 * - Passes decrement down to next leg part
	 * 
	 */
	public void decrement()
	{
		if (this.thigh.hip.active)
		{
			this.thigh.hip.rotate( false );
			this.thigh.decrement();
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
		if (!this.active)
		{
			red = (float) 50/255;
			green = (float) 50/255;
			blue = (float) 50/255;
		}
		else
		{
			red = (float) 255/255;
			green = (float) 10/255;
			blue = (float) 10/255;
		}
		gl.glColor3f( red , green , blue );
	}
	
	/*
	 * Toggles joints on and off for this leg.
	 * 
	 */
	public void toggle_joints( int num_active_joints )
	{
		// If it's already active, turn off all joints
		if ( this.active )
		{
			this.active = false;
			this.thigh.active = false;
			this.thigh.hip.active = false;
			this.thigh.calf.active = false;
			this.thigh.calf.knee.active = false;
			this.thigh.calf.foot.active = false;
			this.thigh.calf.foot.ankle.active = false;
			this.num_active_joints = 0;
		}
		// If it's inactive, turn on correct number of joints
		else
		{
			// activate leg
			this.active = true;
			
			switch ( num_active_joints )
			{
				case 1 : // First joint active
					this.thigh.active = true;
					this.thigh.hip.active = true;	// hip
					break;
					
				case 2 : // First 2 joints active
					this.thigh.active = true;
					this.thigh.hip.active = true;	// hip
					this.thigh.calf.active = true;
					this.thigh.calf.knee.active = true;	// knee
					break;
					
				case 3 : // First 3 joints active
					this.thigh.active = true;
					this.thigh.hip.active = true;	// hip
					this.thigh.calf.active = true;
					this.thigh.calf.knee.active = true;	// knee
					this.thigh.calf.foot.active = true;
					this.thigh.calf.foot.ankle.active = true;	// ankle
					break;
					
				default:
					break;
			}
		}
	}
}