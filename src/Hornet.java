/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	September 25, 2014
 *
 *	Hornet.java : skeleton for hornet character. 
 *	Receives calls to manipulate the joints and passes
 *	them to each leg. Holds six leg objects in memory 
 *	that each maintains rotation angles and active states 
 *	for each joint.
 *
 *	For CS480 at Boston University
 *
 */

import javax.media.opengl.*;

import java.io.IOException;

public class Hornet 
{
	// Display lists
	public int DL;
	private int stinger_dl;
	private int head_dl;
	private int body_dl;	
	
	// Information to draw Hornet's static body
	private float red = (float) 255/255;
	private float green = (float) 201/255;
	private float blue = (float) 36/255;
	
	// Building Block Objects
	private SolidCylinder tube;
	private Sphere sphere;
	
	// Six hornet legs
	private Leg leg_FL;		// front left
	private Leg leg_FR;		// front right
	private Leg leg_ML;		// middle left
	private Leg leg_MR;		// middle right
	private Leg leg_BL;		// back left
	private Leg leg_BR;		// back right
	
	// Array of legs used for iterations
	private Leg[] legs;
	
	// Number of joints triggered in active legs
	private int num_active_joints;

	
	/*
	 * Hornet Class Constructor
	 * 
	 * - receives a tube and sphere building block object
	 * 		to create the body structure and legs.
	 * - creates each of the six legs and then stores them 
	 * 		in an array.
	 * - sets number of active joints to 1 (the hip)
	 * 
	 */
	public Hornet( SolidCylinder tube, Sphere sphere) {

		// Store building block objects
		this.tube = tube;
		this.sphere = sphere;
		
		// front legs
		this.leg_FL = new Leg( "Front Left", tube, sphere, new Point3D( .5f, 0f, -.125f ));
		this.leg_FR = new Leg( "Front Right", tube, sphere, new Point3D( .5f, 0f, .125f ));
		// middle legs
		this.leg_ML = new Leg( "Middle Left", tube, sphere, new Point3D( 0.25f, 0f, -.25f ));
		this.leg_MR = new Leg( "Middle Right", tube, sphere, new Point3D( 0.25f, 0f, .25f ));
		// back legs
		this.leg_BL = new Leg( "Back Left", tube, sphere, new Point3D( 0f, 0f, -.125f ));
		this.leg_BR = new Leg( "Back Right", tube, sphere, new Point3D( 0f, 0f, .125f ));
		
		// Store legs 
		this.legs = new  Leg[6];
		this.legs[0] = this.leg_FL;
		this.legs[1] = this.leg_FR;
		this.legs[2] = this.leg_ML;
		this.legs[3] = this.leg_MR;
		this.legs[4] = this.leg_BL;
		this.legs[5] = this.leg_BR;
		
		// set Hip as active joint
		this.num_active_joints = 1;
	}
	
	/* 
	 * Initialize hornet
	 * 
	 * - just pass it through to the update function. It
	 * 		will initialize the body parts with initial
	 * 		settings.
	 */
	public void init( GL2 gl )
	{
		this.update( gl );
	}
	
	
	/*
	 * Draw hornet.
	 * 
	 * - calls hornet displaylist
	 * 
	 */
	public void draw( GL2 gl )
	{
		// Rotate hornet into a more interesting angle
		gl.glRotatef( -45f, 0f, 1f, 0f);
		gl.glCallList(this.DL);
	}
	
	/*
	 * Create display list for stinger and store
	 * 		it in stinger_dl
	 * 
	 */
	private void create_stinger( GL2 gl ) {
		
		this.stinger_dl = gl.glGenLists(1);

	    gl.glNewList(this.stinger_dl, GL2.GL_COMPILE);

	    	gl.glPushMatrix(); // one
			
				gl.glPushMatrix(); // two
			
					gl.glScalef(.2f, .2f, .2f);
					gl.glRotatef(90f, 0f, 1f, 0f);
					this.tube.draw( gl );
			
				gl.glPopMatrix(); // end two
		
				gl.glScalef(.4f, .8f, .4f);
				gl.glTranslatef(0f, 0f, -.5f);
		
				this.sphere.draw( gl );
		
			gl.glPopMatrix(); // end one
		
			// Draw Stinger 
			for (int i = 0; i < 5 ; i++)
			{
		
				if (i == 0)
				{
					gl.glPushMatrix();
						gl.glScalef(.4f, .8f, .4f);
						gl.glTranslatef(.5f, 0f, -0.5f);
			
						this.sphere.draw( gl );
						gl.glPopMatrix();
				}
			
				gl.glTranslatef(-.2f, -.05f, 0);
				gl.glRotatef( 15f, 0f, 0f, 1f);
			
				gl.glPushMatrix(); // one
			
					gl.glPushMatrix(); // two
				
						gl.glScalef(.2f, .2f, .2f);
						gl.glRotatef(90f, 0f, 1f, 0f);
						this.tube.draw( gl );
			
					gl.glPopMatrix(); // end two
		
					gl.glScalef(.4f, .8f, .4f);
					gl.glTranslatef(0f, 0f, -.5f);
		
					this.sphere.draw( gl );
		
				gl.glPopMatrix(); // end one
			}
		
		gl.glEndList();
	}

	/*
	 * Create display list for head and store
	 * 		it in head_dl
	 * 
	 */
	private void create_head( GL2 gl )
	{
		this.head_dl = gl.glGenLists(1);

	    gl.glNewList(this.head_dl, GL2.GL_COMPILE);
	    	
	    	gl.glRotatef(-80f, 0f, 0f, 1f);
	    	gl.glRotatef(-90f, 0f, 1f, 0f);
	    	gl.glScalef(.8f, 1f, .8f);
		
	    	this.sphere.draw( gl );
	    
		gl.glEndList();
	}
	
	/*
	 * Create display list for body and store
	 * 		it in body_dl
	 * 
	 */
	private void create_body( GL2 gl )
	{
		this.body_dl = gl.glGenLists(1);

	    gl.glNewList(this.body_dl, GL2.GL_COMPILE);
	    	
	    	gl.glColor3f( this.red , this.green , this.blue );
	    	gl.glRotatef(-60f, 0f, 0f, 1f);
	    	gl.glRotatef(-90f, 1f, 0f, 0f);
	    	gl.glScalef(.75f, 1.5f, 1f);
		
	    	this.sphere.draw( gl ); // Draw body
	    
		gl.glEndList();
	}
	
	/*
	 * Toggle active/inactive for any of the six legs.
	 * 
	 * - accepts a string from the keyboard interface in 
	 * 		PA2.java. 
	 * - toggles appropriate joints in the specified leg
	 * 
	 */
	public void toggle_limb( String id )
	{
		switch ( id )
		{
			case "front_left" 	:	
				this.leg_FL.toggle_joints( this.num_active_joints );
				break;
				
			case "front_right" 	: 	
				this.leg_FR.toggle_joints( this.num_active_joints );
				break;
				
			case "middle_left" 	: 	
				this.leg_ML.toggle_joints( this.num_active_joints );
				break;
				
			case "middle_right" : 	
				this.leg_MR.toggle_joints( this.num_active_joints );
				break;
				
			case "back_left" 	: 	
				this.leg_BL.toggle_joints( this.num_active_joints );
				break;
				
			case "back_right" 	: 	
				this.leg_BR.toggle_joints( this.num_active_joints );
				break;	
			
				
			default				:
				break;
		}
	}
	
	/*
	 * Sets correct number of active joints 
	 * 
	 * - accepts an int from the keyboard interface in 
	 * 		PA2.java. 
	 * - updates number of active joints
	 * 
	 */
	public void toggle_joint( int num )
	{
		// Set correct number of active joints
		switch ( num )
		{
			case 1	:
				this.num_active_joints = 1;
				break;
				
			case 2	:
				this.num_active_joints = 2;
				break;
			
			case 3	:
				this.num_active_joints = 3;
				break;
			
			default	:
				break;
		}
		
		// Update all active legs with new number of 
		// active joints
		for ( int i = 0 ; i < this.legs.length ; i++ )
		{
			if ( this.legs[i].active )
			{
				this.legs[i].toggle_joints( this.num_active_joints );	// Turn all joint off
				this.legs[i].toggle_joints( this.num_active_joints );	// Toggle correct number of joints
			}
		}
	}

	/*
	 * Calls increment() function for each active leg
	 * 
	 */
	public void increment_joints()
	{	
		for (int i = 0; i < this.legs.length; i++ )
		{
			if ( this.legs[i].active )
			{
				this.legs[i].increment();
			}
		}
	}
	
	/*
	 * Calls decrement() function for each active leg
	 * 
	 */
	public void decrement_joints()
	{
		
		for (int i = 0; i < this.legs.length; i++ )
		{
			if ( this.legs[i].active )
			{
				this.legs[i].decrement();
			}
		}
	}
	
	/*
	 * Redraws display lists to create hornet.
	 * 
	 * 	- is called when the program is first started
	 * 	- called each time the hornet's state is changed 
	 */
	public void update( GL2 gl )
	{		
		this.create_stinger( gl );
		this.create_head( gl );
		this.create_body( gl );
		this.leg_FL.init_left( gl );
		this.leg_FR.init_right( gl );
		this.leg_ML.init_left( gl );
		this.leg_MR.init_right( gl );
		this.leg_BL.init_left( gl );
		this.leg_BR.init_right( gl );		
		
		this.DL = gl.glGenLists(1);

	    gl.glNewList(this.DL, GL2.GL_COMPILE);
	    
	    	gl.glPushMatrix();
	    		
	    		gl.glPushAttrib(GL2.GL_CURRENT_BIT);
	    			gl.glColor3f( this.red , this.green , this.blue );
	    			gl.glPushMatrix();	// draw head
	    		
	    				gl.glTranslatef(1f, -.2f, 0f);
	    				gl.glCallList( this.head_dl );
	    			
	    			gl.glPopMatrix();	// end draw head
	    	
	    			gl.glPushMatrix();	// draw body
	    		
	    				gl.glTranslatef( -0.125f, 0f, 0f);
	    				gl.glCallList( this.body_dl );
	    			
	    			gl.glPopMatrix();	// end draw body
	    		
	    			gl.glPushMatrix(); // draw stinger
	    			
	    				gl.glTranslatef(-.5f, 0f, 0f);
	    				gl.glCallList( this.stinger_dl );
	    		
	    			gl.glPopMatrix(); // end draw stinger
	    		gl.glPopAttrib();
    				
    			// Draw Legs
    			this.leg_BR.draw_right( gl );
    			this.leg_MR.draw_right( gl ); 
    			this.leg_FR.draw_right( gl );
    			this.leg_BL.draw_left( gl );
    			this.leg_ML.draw_left( gl );
    			this.leg_FL.draw_left( gl );
	
	    	gl.glPopMatrix();	// end draw finished hornet
	    gl.glEndList();
	}
	
	/*
	 * Sets specific angles for each joint on a leg.
	 * 
	 * - used by TestCases.java to set custom poses.
	 * - each joint object makes sure the angles are within 
	 * 		the bounds of rotation
	 * 
	 */
	public void set_leg_angles( int legNum, double hipAngle, double kneeAngle, double ankleAngle )
	{
		
		this.legs[ legNum ].thigh.hip.set_angle( hipAngle );
		this.legs[ legNum ].thigh.calf.knee.set_angle( kneeAngle );
		this.legs[ legNum ].thigh.calf.foot.ankle.set_angle( ankleAngle );
	}
}