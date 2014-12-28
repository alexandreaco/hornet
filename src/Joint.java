
import javax.media.opengl.*;

import java.io.IOException;

/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	October 10, 2014
 *
 *	Joint.java: Skeleton class for all joints in the insect.
 *		Contains functions to change the angle of rotation.
 *		Defines rotation boundaries with angleMin and angleMax 
 *
 *	For CS480 at Boston University
 *
 */

public class Joint
{
	// Number of degrees to increment/decrement by
	private final double ANGLE_INCREMENT = 5.0;

	// Define rotation bounds
	private final double ANGLE_MIN;
	private final double ANGLE_MAX;
	
	// Store current angle
	private double angle_current;
	
	// Flag. Is joint active?
	public boolean active;

	/* 
	 * Joint Constructor
	 * 
	 * - sets min, max, and initial angle values
	 * 
	 */
	public Joint( double min, double max, double current )
	{
		this.ANGLE_MIN = min;
		this.ANGLE_MAX = max;
		this.angle_current = current;
		this.active = false;
	}
	
	/*
	 * Initialize joint by calling update
	 */
	public void init( GL2 gl )
	{
		this.update( gl );
		
	}
	
	/*
	 * Update joint display list with current angle
	 * 
	 */
	public void update( GL2 gl )
	{
		// Rotate joint about x axis
		gl.glRotated( this.angle_current, 1.0, 0.0, 0.0 );	
	}
	
	/*
	 * Increase or decrease rotation of joint
	 * 
	 * - ANGLE_MIN < current_angle < ANGLE_MAX
	 * 
	 * - if true is passed in, increment angle
	 * 		otherwise, decrement angle
	 */
	public void rotate( boolean increase )
	{
		if (!increase)	// decrement angle
		{
			double new_angle = this.angle_current - this.ANGLE_INCREMENT;
			
			if ( new_angle < this.ANGLE_MIN )
			{
				this.angle_current = this.ANGLE_MIN;	// ANGLE_MIN is the lowest it can go
			}
			else 
			{
				this.angle_current = new_angle;
			}
		}
		else	// increment angle
		{
			double new_angle = this.angle_current + this.ANGLE_INCREMENT;
			
			if ( new_angle > this.ANGLE_MAX )
			{
				this.angle_current = this.ANGLE_MAX;	// ANGLE_MAX is the highest it can go
			}
			else 
			{
				this.angle_current = new_angle;
			}
		}
	}
	
	/*
	 * Set custom angle value
	 * 
	 * - ANGLE_MIN < angle < ANGLE_MAX
	 * - used for test poses
	 * 
	 */
	public void set_angle( double angle )
	{
		if ( angle < this.ANGLE_MIN )
		{
			this.angle_current = ANGLE_MIN;	// ANGLE_MIN is the lowest it can go
		}
		else if ( angle > this.ANGLE_MAX )
		{
			this.angle_current = this.ANGLE_MAX;	// ANGLE_MAX is the highest it can go
		}
		else 
		{
			this.angle_current = angle;
		}
	}
}