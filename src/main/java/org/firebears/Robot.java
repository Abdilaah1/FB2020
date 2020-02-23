package org.firebears;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.commands.*;
import org.firebears.subsystems.*;

public class Robot extends TimedRobot {

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    public static OI oi;
    public static Chassis chassis;
    public static Acquisition acquisition;
    public static Loader loader;
    public static Shooter shooter;
    public static Climber climberRight;
    public static Climber climberLeft;
    public static Vision vision;
    public static Lidar lidar;
    public static Storage storage;
    public static Lights lights;

    @Override
    public void robotInit() {

        chassis = new Chassis();
        loader = new Loader();
        acquisition = new Acquisition();
        shooter = new Shooter();
        climberLeft = new Climber("climber.left.canID", 22);
        climberRight = new Climber("climber.right.canID", 24);
        vision = new Vision();
        lidar = new Lidar();
        storage = new Storage();
        lights = new Lights();

        CommandScheduler.getInstance().registerSubsystem(vision);
        CommandScheduler.getInstance().registerSubsystem(lidar);

        // OI must be constructed after subsystems. If the OI creates Commands
        // (which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser
        chooser.setDefaultOption("FollowTarget", new FollowTargetCommand(chassis));

        SmartDashboard.putData("Auto mode", chooser);

        new ResetCommand(storage).schedule(); 
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called when the disabled button is hit. You can use it to
     * reset subsystems before shutting down.
     */
    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {

    }

    @Override
    public void autonomousInit() {
        autonomousCommand = chooser.getSelected();
        // schedule the autonomous command (example)
        if (autonomousCommand != null)
            autonomousCommand.schedule();
        setDefaultCommands();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null)
            autonomousCommand.cancel();     
        setDefaultCommands();   
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void testInit() {
        acquisition.setDefaultCommand(null);
        chassis.setDefaultCommand(null);
        storage.setDefaultCommand(null);
    }
    public void setDefaultCommands(){
        acquisition.setDefaultCommand(new AcquisitionRaiseCommand(acquisition, loader));
        chassis.setDefaultCommand(new DriveCommand(chassis));
        storage.setDefaultCommand(new TheOneCommandToRuleThemAll(storage));
    }
}