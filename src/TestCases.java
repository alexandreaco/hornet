/*
 *	Alexandrea Defreitas alexdef@bu.edu
 *
 *	October 14, 2014
 *
 *	Test cases for demo purposes. T and t keyboard 
 *	inputs triggered from PA2.java will cycle through the poses. 
 *
 *	For CS480 at Boston University
 *
 */

public class TestCases
{
	// Store current test case
	private int currentTest;
	
	/*
	 * TestCases constructor
	 * 
	 * - always start with first test pose
	 * 
	 */
	public TestCases()
	{
		this.currentTest = 0;
	}
	
	/*
	 * Runs current test case and then increments
	 * 
	 */
	public void cycle_test( Hornet hornet )
	{

		switch ( this.currentTest )
		{
			case 0 : 
				this.run_1( hornet );
				break;
				
			case 1 : 
				this.run_2( hornet );
				break;
					
			case 2 : 
				this.run_3( hornet );
				break;
					
			case 3 : 
				this.run_4( hornet );
				break;
					
			case 4 : 
				this.run_5( hornet );
				break;
			
			default:
				break;
		}
		
		this.currentTest = ( this.currentTest + 1 ) % 5;
	}
	
	/*
	 * Set angles for first test pose
	 */
	private void run_1( Hornet hornet )
	{
		hornet.set_leg_angles( 	0, 	 60, 	120, 	-45 );
		hornet.set_leg_angles( 	1, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	2, 	 60, 	120, 	-45 );
		hornet.set_leg_angles( 	3, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	4, 	 60, 	120, 	-45 );
		hornet.set_leg_angles( 	5, 	-10, 	 10, 	-20 );
		
		System.out.println("Test Case 1: To the left.... ");
	}
	
	/*
	 * Set angles for second test pose
	 */
	private void run_2( Hornet hornet )
	{
		hornet.set_leg_angles( 	0, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	1, 	 60, 	120, 	-45 );
		hornet.set_leg_angles( 	2, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	3, 	 60, 	120, 	-45 );
		hornet.set_leg_angles( 	4, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	5, 	 60, 	120, 	-45 );
		
		System.out.println("Test Case 2: To the right... ");
	}
	
	/*
	 * Set angles for third test pose
	 */
	private void run_3( Hornet hornet )
	{
		hornet.set_leg_angles( 	0, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	1, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	2, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	3, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	4, 	-10, 	 10, 	-20 );
		hornet.set_leg_angles( 	5, 	-10, 	 10, 	-20 );
		
		System.out.println("Test Case 3: Over the top... ");
	}
	
	/*
	 * Set angles for fourth test pose
	 */
	private void run_4( Hornet hornet )
	{
		hornet.set_leg_angles( 	0, 	 30, 	 15, 	-40 );
		hornet.set_leg_angles( 	1, 	 30, 	 15, 	-40 );
		hornet.set_leg_angles( 	2, 	 30, 	 15, 	-40 );
		hornet.set_leg_angles( 	3, 	 30, 	 15, 	-40 );
		hornet.set_leg_angles( 	4, 	 30, 	 15, 	-40 );
		hornet.set_leg_angles( 	5, 	 30, 	 15, 	-40 );
		
		System.out.println("Test Case 4: Push up time... ");
	}
	
	/*
	 * Set angles for fifth test pose
	 */
	private void run_5( Hornet hornet )
	{
		hornet.set_leg_angles( 	0, 	 40, 	 90, 	-40 );
		hornet.set_leg_angles( 	1, 	 40, 	 90, 	-40 );
		hornet.set_leg_angles( 	2, 	 40, 	 90, 	-40 );
		hornet.set_leg_angles( 	3, 	 40, 	 90, 	-40 );
		hornet.set_leg_angles( 	4, 	 40, 	 90, 	-40 );
		hornet.set_leg_angles( 	5, 	 40, 	 90, 	-40 );
		
		System.out.println("Test Case 5: And Center... ");
	}
}