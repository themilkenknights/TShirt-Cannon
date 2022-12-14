// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class DriveSubsystem extends SubsystemBase {

  private ShuffleboardTab swerveTab = Shuffleboard.getTab("Swerve Diagnostics");
  
  private AHRS NAVX=new AHRS();

  //private PowerDistribution PDP = new PowerDistribution();

  private NetworkTableEntry xSpeedEntry = 
  swerveTab.add("xBox xSpeed", 0)
          .getEntry();

  private NetworkTableEntry ySpeedEntry = 
  swerveTab.add("xBox ySpeed", 0)
          .getEntry();

  private NetworkTableEntry rotEntry = 
  swerveTab.add("xBox rot", 0)
          .getEntry();

  private NetworkTableEntry frontLeftStateEntry =
  swerveTab.add("FL State v", 0)
          .getEntry();
  
  private NetworkTableEntry frontRightStateEntry =
  swerveTab.add("FR State v", 0)
          .getEntry();

  private NetworkTableEntry rearLeftStateEntry = 
    swerveTab.add("RL State v", 0)
          .getEntry();
  
  private NetworkTableEntry rearRightStateEntry =
    swerveTab.add("RR State v", 0)
          .getEntry();

  private NetworkTableEntry gyroEntry =
  swerveTab.add("Gyro Heading", 0)
          .getEntry();

  private final SwerveModule m_frontLeft = 
    new SwerveModule(
      DriveConstants.kFrontLeftDriveMotorPort,
      DriveConstants.kFrontLeftTurningMotorPort,
      DriveConstants.kFrontLeftTurningEncoderPorts,
      DriveConstants.kFrontLeftAngleZero);

  private final SwerveModule m_rearLeft =
    new SwerveModule(
      DriveConstants.kRearLeftDriveMotorPort, 
      DriveConstants.kRearLeftTurningMotorPort, 
      DriveConstants.kRearLeftTurningEncoderPorts,
      DriveConstants.kRearLeftAngleZero);

  private final SwerveModule m_frontRight = 
    new SwerveModule(
      DriveConstants.kFrontRightDriveMotorPort, 
      DriveConstants.kFrontRightTurningMotorPort, 
      DriveConstants.kFrontRightTurningEncoderPorts,
      DriveConstants.kFrontRightAngleZero);

  private final SwerveModule m_rearRight = 
    new SwerveModule(
      DriveConstants.kRearRightDriveMotorPort, 
      DriveConstants.kRearRightTurningMotorPort, 
      DriveConstants.kRearRightTurningEncoderPorts,
      DriveConstants.kRearRightAngleZero);

  // Odometry class for tracking robot pose
  SwerveDriveOdometry m_odometry =
    new SwerveDriveOdometry(DriveConstants.kDriveKinematics, Rotation2d.fromDegrees(NAVX.getYaw()));

    /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {}


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Update the odometry in the periodic block

    gyroEntry.setDouble(NAVX.getYaw());

    //SmartDashboard.putNumber("test", m_frontLeft.getModuleHeading());

    m_odometry.update(
      Rotation2d.fromDegrees(NAVX.getYaw()),
      m_frontLeft.getState(),
      m_rearRight.getState(),
      m_frontRight.getState(),
      m_rearRight.getState());

      
  }

  // Returns the currently-estimated pose of the robot

  public Pose2d getPose(){
    return m_odometry.getPoseMeters();
  }

  // Resets the odometry to the specified pose

  public void resetOdometry(Pose2d pose){
    m_odometry.resetPosition(pose, Rotation2d.fromDegrees(NAVX.getYaw()));
  }

  /**  Method to drive the robot using joystick info
   * @param xSpeed Speed of the robot in the x direction (forward).
   * @param ySpeed Speed of the robot in the y direction (sideways).
   * @param rot Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  @SuppressWarnings("ParameterName")
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative){
    
    var swerveModuleStates = 
      DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
          ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, Rotation2d.fromDegrees(NAVX.getYaw()))
          : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.desaturateWheelSpeeds(
      swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);

    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_rearLeft.setDesiredState(swerveModuleStates[2]);
    m_rearRight.setDesiredState(swerveModuleStates[3]); 

    // Telemetry
    xSpeedEntry.setDouble(xSpeed);
    ySpeedEntry.setDouble(ySpeed);
    rotEntry.setDouble(rot);
    frontLeftStateEntry.setDouble(swerveModuleStates[0].angle.getDegrees());
    frontRightStateEntry.setDouble(swerveModuleStates[1].angle.getDegrees());
    rearRightStateEntry.setDouble(swerveModuleStates[2].angle.getDegrees());
    rearLeftStateEntry.setDouble(swerveModuleStates[3].angle.getDegrees());
    

  }

    /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */

  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
      desiredStates, DriveConstants.kMaxSpeedMetersPerSecond);
      m_frontLeft.setDesiredState(desiredStates[0]);
      m_frontRight.setDesiredState(desiredStates[1]);
      m_rearLeft.setDesiredState(desiredStates[2]);
      m_rearRight.setDesiredState(desiredStates[3]);
  }

  // Resets the drive encoders to currently read a position of 0
  public void resetEncoders(){
    m_frontLeft.resetEncoders();
    m_rearLeft.resetEncoders();
    m_frontRight.resetEncoders();
    m_rearRight.resetEncoders();
  }

  // Zeroes the heading of the robot
  public void zeroHeading() {
    NAVX.reset();
  }

    /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public Rotation2d getHeading(){
    return Rotation2d.fromDegrees(NAVX.getYaw());
  }

    /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate(){
    return NAVX.getRate() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
  }
}
