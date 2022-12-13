// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

  public static final class DriveConstants {
    
    public static final int kFrontLeftDriveMotorPort = 3;
    public static final int kRearLeftDriveMotorPort = 2;
    public static final int kFrontRightDriveMotorPort = 5;
    public static final int kRearRightDriveMotorPort = 7;

    public static final int kFrontLeftTurningMotorPort = 4;
    public static final int kRearLeftTurningMotorPort = 1;
    public static final int kFrontRightTurningMotorPort = 6;
    public static final int kRearRightTurningMotorPort = 8;

    public static final int kFrontLeftTurningEncoderPorts = 16;
    public static final int kRearLeftTurningEncoderPorts = 15;
    public static final int kFrontRightTurningEncoderPorts = 18;
    public static final int kRearRightTurningEncoderPorts = 17;

    public static final double kFrontLeftAngleZero = -3.7; 
    public static final double kRearLeftAngleZero = -3.7; 
    public static final double kFrontRightAngleZero = -3.7;
    public static final double kRearRightAngleZero = -3.7; 

    public static final boolean kFrontLeftTurningEncoderReversed = false;
    public static final boolean kRearLeftTurningEncoderReversed = false;
    public static final boolean kFrontRightTurningEncoderReversed = false;
    public static final boolean kRearRightTurningEncoderReversed = false;

    public static final boolean kFrontLeftDriveEncoderReversed = true;
    public static final boolean kRearLeftDriveEncoderReversed = true;
    public static final boolean kFrontRightDriveEncoderReversed = true;
    public static final boolean kRearRightDriveEncoderReversed = true;

    public static final double kTrackWidth = 0.577; // meters
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = 0.577; // meters
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    public static final boolean kGyroReversed = false;

    // NED CHANGE
    public static final double ksVolts = 0.509;
    public static final double kvVoltSecondsPerMeter = 2.73;
    public static final double kaVoltSecondsSquaredPerMeter = 0.124;
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxRotationalSpeedMetersPerSecond = 4; 

    public static final double ksTurning = 0.7;
    public static final double kvTurning = 0.216;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI * 2;

  }

  public static final class ModuleConstants {
    // Drive motor -> FX Encoder (2048 units)
    // Turning motor -> CTRE CANcoder (4096 units)
    public static final double kMaxModuleAngularSpeedRadiansPerSecond = 20 * Math.PI;
    public static final double kMaxModuleAngularAccelerationRadiansPerSecondSquared = 35 * Math.PI;
    public static final double kDriveGearRatio = 6.75; // Todo:
    public static final double kTurningGearRatio = 50/7;

    public static final int kDriveFXEncoderCPR = 2048;
    public static final int kTurningCANcoderCPR = 4096;
    public static final double kWheelDiameterMeters = 0.1016; // 4 inches
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI; // C = D * pi
    public static final double kDrivetoMetersPerSecond = (10 * kWheelCircumferenceMeters) / (kDriveGearRatio * 2048);

    // PID turn motor values

    public static final double kPModuleTurningController = .1;
    public static final double kDModuleTurningController = .1;

    public static final double kPModuleDriveController = 3;
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 1.35;// changed from 1.5, 10% change
    public static final double kMaxAccelerationMetersPerSecondSquared = 1.5;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularAccelerationRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 2;
    public static final double kPYController = 2;
    public static final double kPPidController = 2;

    // Constraint for the motion profilied robot angle controller
    public static final TrapezoidProfile.Constraints kPidControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularAccelerationRadiansPerSecondSquared);

    public static final TrajectoryConfig config = new TrajectoryConfig(
        AutoConstants.kMaxSpeedMetersPerSecond,
        AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(DriveConstants.kDriveKinematics);

    public static final Trajectory movingOutTrajectory = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)),
        List.of(new Translation2d(-1, 0)),
        new Pose2d(-1.5, 0, new Rotation2d(0)),
        config.setReversed(true));

    public static final Trajectory grabSecondBallTrajectory = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)),
        List.of(new Translation2d(0, 1)),
        new Pose2d(0, 1.7, new Rotation2d(0)),
        config);

    public static final Trajectory rotate90Trajectory = TrajectoryGenerator.generateTrajectory(
        new Pose2d(0, 1, new Rotation2d(0)),
        List.of(new Translation2d(-.5, -.5)),
        new Pose2d(-1, -1, new Rotation2d(Math.toRadians(-50))),
        config);

    public static final ProfiledPIDController PidController = new ProfiledPIDController(
        AutoConstants.kPPidController, 0, 0, AutoConstants.kPidControllerConstraints);

  }

  

}