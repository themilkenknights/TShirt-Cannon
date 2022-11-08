// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.AutoConstants;

public class PidController extends SubsystemBase {
  /** Creates a new PidController. */

  public static ProfiledPIDController PidController;

  public PidController() {
    PidController = new ProfiledPIDController(
        AutoConstants.kPPidController, 0, 0, AutoConstants.kPidControllerConstraints);

    PidController.enableContinuousInput(-Math.PI, Math.PI);

  }

  public ProfiledPIDController getPidController() {
    return PidController;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
