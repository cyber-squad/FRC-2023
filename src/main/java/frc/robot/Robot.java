// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/*
 * Author: Sythat, Sokmontrey & Mallari, Eirich Rain
 * Mr. Odebiyi
 * ICS4U
 * Date: January 23, 2023
 */

//import all the nessary library to control the robot using an XBox Controller 
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  //declare PWMVictorSPX drivers object for the left motors(front and back)
  private PWMVictorSPX _left_drive1;
  private PWMVictorSPX _left_drive2;

  //declare PWMVictorSPX drivers object for the right motors(front and back)
  private PWMVictorSPX _right_drive1;
  private PWMVictorSPX _right_drive2;

  private PWMVictorSPX _intake1;

  //declare XboxController with the name "_stick"
  private XboxController _stick;
  private Joystick _stick2;

  private double max_speed_factor = 0.8;
  private double intake_direction = 0;

  @Override
  public void robotInit() {

    /*
      define each motor driver object variable to new PWMVictorSPX with the assigned channel (RoboRIO PWM pin)
    */

    _left_drive1 = new PWMVictorSPX(0);
    _left_drive2 = new PWMVictorSPX(1);

    _right_drive1 = new PWMVictorSPX(3);
    _right_drive2 = new PWMVictorSPX(2);

    _intake1 = new PWMVictorSPX(4);

    //define _stick object to a new XboxController object listen on the port 0
    _stick = new XboxController(0);
    _stick2 = new Joystick(1);

    //Invert the axis of the left motor
    _left_drive1.setInverted(true);
    _left_drive2.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    if(_stick.getRightBumperPressed()){
      if(max_speed_factor == 0.6) max_speed_factor = 0.3;
      else max_speed_factor = 0.6;
    }

    if(_stick.getYButtonPressed()){
      intake_direction = 1;
    }
    if(_stick.getBButtonPressed()){
      intake_direction = -1;
    }
    if(_stick.getYButtonReleased() || _stick.getBButtonReleased()){
      intake_direction = 0;
    }

    if(_stick2.getRawButtonPressed(2)){
      intake_direction = 1;
    }
    if(_stick2.getRawButtonPressed(1)){
      intake_direction = -1;
    }
    if(_stick2.getRawButtonReleased(1) || _stick2.getRawButtonReleased(2)){
      intake_direction = 0;
    }

    _intake1.set(intake_direction * 0.6);

    //get value of Joysticks on the Xbox controller and multiply it with 0.4 
    //  to slow down the speed, prevent from motor overload
    double left_speed = Math.min(1, Math.max(-1, _stick.getLeftY() - _stick.getLeftX()));
    double right_speed = Math.min(1, Math.max(-1, _stick.getLeftY() + _stick.getLeftX()));

    left_speed *= max_speed_factor;
    right_speed *= max_speed_factor;

    //set the speed value to thier corresponding motor using the motor driver object
    _right_drive1.set(right_speed);
    _left_drive1.set(left_speed);

    _right_drive2.set(right_speed);
    _left_drive2.set(left_speed);
  }
}
