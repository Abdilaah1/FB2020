package org.firebears.commands;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.firebears.Robot;
import org.firebears.subsystems.Chassis;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FollowTargetCommand extends CommandBase {
  final Preferences config;

  public FollowTargetCommand(Chassis chassis) {
    addRequirements(chassis);
    config = Preferences.getInstance();

  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    if (Robot.vision.getVisionTargetConfidenceBoolean()) {
      double rotation = 1.0 / 30.0 * Robot.vision.getVisionTargetAngleX();
      Robot.chassis.drive(0, rotation);
      SmartDashboard.putNumber("rotation", rotation);
    } else {
      Robot.chassis.drive(0, 0);
      SmartDashboard.putNumber("rotation", 0);
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
  }

}