// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Sokmontrey Sythat
// 2023

package frc.robot;

//import all the nessary library to control the robot using an XBox Controller 
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

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

  private double max_speed_factor = 0.65;
  private double intake_direction = 0;

  Timer timer;

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
      if(max_speed_factor == 0.65) max_speed_factor = 0.3;
      else max_speed_factor = 0.65;
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

  @Override 
  public void autonomousInit(){
    timer = new Timer();
  }

  @Override 
  public void autonomousPeriodic(){
    //initial object
    turn_right(2);
    move_forward(3);
    take_out();
  }

  private double max_auto_speed = 0.5;

  private void take_in(){
    set_intake(-1 * 0.7);
    timer.delay(2);
    set_intake(0);
  }
  private void take_out(){
    set_intake(1 * 0.7);
    timer.delay(2);
    set_intake(0);
  }
  private void stop_all_motor(){
    set_left(0);
    set_right(0);
    set_intake(0);
  }

  private void turn_left(double duration){
    set_left(-1 * max_auto_speed);
    set_right(1 * max_auto_speed);
    timer.delay(duration);
    stop_all_motor();
  }
  private void turn_right(double duration){
    set_left(1 * max_auto_speed);
    set_right(-1 * max_auto_speed);
    timer.delay(duration);
    stop_all_motor();
  }
  private void move_forward(double duration){
    set_left(1 * max_auto_speed);
    set_right(1 * max_auto_speed);
    timer.delay(duration);
    stop_all_motor();
  }
  private void move_backward(double duration){
    set_left(-1 * max_auto_speed);
    set_right(-1 * max_auto_speed);
    timer.delay(duration);
    stop_all_motor();
  }

  private void set_intake(double speed){
    _intake1.set(speed);
  }
  private void set_left(double speed){
    _left_drive1.set(speed);
    _left_drive2.set(speed);
  }
  private void set_right(double speed){
    _right_drive1.set(speed);
    _right_drive2.set(speed);
  }
}
