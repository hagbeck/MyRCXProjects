package de.hagbeck.lego.mindstorms.rcx;
/*
 * $LOG$
 * Revision 0.4, ..., hagbeck
 * - use the sensors to control the remote control events
 * 
 * Revision 0.3, ..., hagbeck
 * - use the light sensors as a proximity sensor (josx.platform.rcx.ProximitySensor and waitTillNear(long aMilliseconds), see: http://www.lejos.org/rcx/tutorial/essential/hardware/sensors.html)
 * - use the rotation sensor for controlling the lift wagon
 * 
 * Revision 0.2, 2016-03-06, hagbeck
 * - add reading the sensors and displaying the values in the message methods: S1 and S2 are touch sensors, S3 is a light sensor
 * 
 * Revision 0.1, 2016-03-03, hagbeck
 * - reuse the framework of the lejos_rcx distribution example 'RemoteControlTest' by Matthias Paul Scholz (mp.scholz@t-online.de)
 * - make the code more readable for me ;-)
 *
 */
import josx.platform.rcx.*;
import josx.rcxcomm.remotecontrol.*;

/**
 * This program provides functions for IR controlling my container terminal. It also provides functions for material protection.
 *
 * @author hagbeck (https://github.com/hagbeck)
 * @version 0.1, 2016-03-06
 *
*/
public class ContainerTerminalControl implements RemoteControlListener {        

    // constants
    private static final char[] MESSAGE1 = {'M','S','G','1'};
    private static final char[] MESSAGE2 = {'M','S','G','2'};
    private static final char[] MESSAGE3 = {'M','S','G','3'};
    private static final char[] MOTOR_DOWN = {'M','O','T','-'};
    private static final char[] MOTOR_UP = {'M','O','T','+'};
    private static final char[] PROGRAM1 = {'P','R','O','G','1'};
    private static final char[] PROGRAM2 = {'P','R','O','G','2'};
    private static final char[] PROGRAM3 = {'P','R','O','G','3'};
    private static final char[] PROGRAM4 = {'P','R','O','G','4'};
    private static final char[] PROGRAM5 = {'P','R','O','G','5'};
    private static final char[] STOP = {'S','T','O','P'};
    private static final char[] SOUND = {'S','O','U','N','D'};
    private static final char[] READY = {'R','E','A','D','Y'};

    /**
     * current sensor states (activated = true)
     */
    private boolean[] fSensorState = { false,false,false }; 

    /** 
     * creates a new instance of ContainerTerminalControl 
     */
    public ContainerTerminalControl() {

        // reset to initial state
        reset();

        // display "ready"
        TextLCD.print(READY);
    }

    // RemoteControlListener interface methods
    
    /**
    * handler for the message 1 button
    */
    public void message1Pressed() {

        // display
        TextLCD.print(MESSAGE1);

        // current sensor state?
        boolean sensorState = fSensorState[0];

        // activate/passivate sensor
        if(sensorState) {

            Sensor.S1.passivate();
		}
        else {

            Sensor.S1.activate();
        }

        // change sensor state
        fSensorState[0] = !sensorState;
    } 

    /**
    * handler for the message 2 button
    */
    public void message2Pressed() {

        // display
        TextLCD.print(MESSAGE2);

        // current sensor state?
        boolean sensorState = fSensorState[1];

        // activate/passivate sensor
        if(sensorState) {

            Sensor.S2.passivate();
		}
        else {

            Sensor.S2.activate();
		}

        // change sensor state
        fSensorState[1] = !sensorState;
    } 
    
    /**
    * handler for the message 3 button
    */
    public void message3Pressed() {

        // display
        TextLCD.print(MESSAGE3);

        // current sensor state?
        boolean sensorState = fSensorState[0];

        // activate/passivate sensor
        if(sensorState) {

            Sensor.S3.passivate();
		}
        else {

            Sensor.S3.activate();
		}

        // change sensor state
        fSensorState[2] = !sensorState;
    } 

    /**
     * decrements motor power
     * @param aMotor the motor
     */
    public void motorDownPressed(Motor aMotor) {

        // display
        TextLCD.print(MOTOR_DOWN);

        // get current power
        int power = aMotor.getPower();

        // get current state
        if(power==0) {

            // move backward
            aMotor.setPower(++power);
            aMotor.backward();
        } 
		else {

            if(aMotor.isForward()) {

                // decrease forward movement
                aMotor.setPower(--power);
			}
            else {

                // move backward
                aMotor.setPower(Math.min(7,++power));
                aMotor.backward();
            } 
        } 
      
		displayMotorsPower();
    } 
	
    /**
     * increments motor power
     * @param aMotor the motor
     */
    public void motorUpPressed(Motor aMotor) {

        // display
        TextLCD.print(MOTOR_UP);

        // get current power
        int power = aMotor.getPower();

        // get current state
        if(power==0) {

            // move forward
            aMotor.setPower(++power);
            aMotor.forward();
        } 
		else {

            if(aMotor.isBackward()) {

                // decrease backward movement
                aMotor.setPower(--power);
			}
            else { 

                // move forward
                aMotor.setPower(Math.min(7,++power));
                aMotor.forward();
            } 
        } 

        // display power of motors
		displayMotorsPower();
    } 
	
    /**
    * handler for the program 1 button
    */
    public void program1Pressed() {

        // display
        TextLCD.print(PROGRAM1);
    } 

    /**
    * handler for the program 2 button
    */
    public void program2Pressed() {
        // display
        TextLCD.print(PROGRAM2);
    } 

    /**
    * handler for the program 3 button
    */
    public void program3Pressed() {

        // display
        TextLCD.print(PROGRAM3);
    }

    /**
    * handler for the program 4 button
    */
    public void program4Pressed() {

        // display
        TextLCD.print(PROGRAM4);
    } 

    /**
    * handler for the program 5 button
    */
    public void program5Pressed() {

        // display
        TextLCD.print(PROGRAM5);
    } 

    /**
    * handler for the sound button
    */
    public void soundPressed() {

        // display
        TextLCD.print(SOUND);
        // sound
        Sound.beep();
    } 

    /**
    * handler for the stop button
    */
    public void stopPressed() {

        // display
        TextLCD.print(STOP);
        // reset
        reset();
    }

    /**
     * resets the engine to its initial state
     */
    public void reset() {

        // stop motors
        Motor.A.stop();	
        Motor.A.setPower(0);	
        Motor.B.stop();
        Motor.B.setPower(0);	
        Motor.C.stop();
        Motor.C.setPower(0);
	
        // passivate sensors
        Sensor.S1.passivate();
        Sensor.S2.passivate();
        Sensor.S3.passivate();
        for(int i = 0; i < fSensorState.length; i++) {

            fSensorState[i] = false;
		}
    } 

    /**
    * displays the power of the motors.
    * <br>format: 0<Motor A><Motor B><Motor C>
    */	
    private void displayMotorsPower() {

        int powers = Motor.A.getPower() * 100 + Motor.B.getPower() * 10 + Motor.C.getPower(); 
        LCD.showNumber(powers);
    }

	public static void main(String[] args) throws InterruptedException {

        // init
        ContainerTerminalControl containerTerminalControl = new ContainerTerminalControl();

        // create remote control sensor
        RemoteControlSensor sensor = new RemoteControlSensor();
        sensor.addRemoteControlListener(containerTerminalControl);

        // reset engine
        containerTerminalControl.reset();

        // material protection
        Sensor.S1.setTypeAndMode(SensorConstants.SENSOR_TYPE_ROT, SensorConstants.SENSOR_MODE_ANGLE);
        Sensor.S2.setTypeAndMode(SensorConstants.SENSOR_TYPE_TOUCH, SensorConstants.SENSOR_MODE_BOOL);
        Sensor.S3.setTypeAndMode(SensorConstants.SENSOR_TYPE_TOUCH, SensorConstants.SENSOR_MODE_BOOL);

        // motor power
        Motor.A.setPower(5);
        Motor.B.setPower(5);

        // number of rotation
        int n = 12;

        // main loop
        while (!Button.RUN.isPressed()) {

            // driving control
            if ( (Motor.A.isForward() && Sensor.S2.readBooleanValue()) || (Motor.A.isBackward() && Sensor.S3.readBooleanValue())) {

                Motor.A.stop();
                Sound.beepSequence();
            }
            // lift wagon control
            else if (Motor.B.isBackward() && Sensor.S1.readValue() == n * 16) {

                Motor.B.stop();
                Sound.beepSequence();
            }
        }

        // start reset sequence
        Sound.twoBeeps();

        Thread.sleep(2000);

        Sound.twoBeeps();

        // at least reset the wagon to the init position
        Motor.B.backward();

        while (true) {

            if (Sensor.S1.readValue() == 0) {

                Motor.B.stop();
                break;
            }
            else if (Sensor.S1.readValue() == 2 * 16) {

                Motor.B.setPower(1);
            }
        }

        // exit program
        System.exit(0);
	}
}

