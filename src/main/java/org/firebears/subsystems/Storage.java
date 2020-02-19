package org.firebears.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Storage extends SubsystemBase {
    private final Preferences config = Preferences.getInstance();

    private final CANSparkMax indexMotor;

    private final DigitalInput positionSensor;
    private final DigitalInput eye1;
    private final DigitalInput eye2;
    private final DigitalInput eye3;
    private final DigitalInput eye4;
    private final DigitalInput eye5;
    private final CANEncoder indexEncoder;

    public Storage() {
        CANError err;
        int stallLimit = config.getInt("storage.stallLimit", 20);
        int freeLimit = config.getInt("storage.freeLimit", 10);
        int limitRPM = config.getInt("storage.limitRPM", 500);
        int indexMotorCanID = config.getInt("storage.indexMotor.canID", 10);
        
        indexMotor = new CANSparkMax(indexMotorCanID, MotorType.kBrushless);
        indexMotor.setInverted(false);
        err = indexMotor.setSmartCurrentLimit(stallLimit, freeLimit, limitRPM);
        indexEncoder = indexMotor.getEncoder();
        if (err != CANError.kOk)
            System.err.println("ERROR: " + err + " setting limits on indexMotor");

        positionSensor = new DigitalInput(config.getInt("storage.position.dio", 4));
        eye1 = new DigitalInput(config.getInt("storage.eye1.dio", 6));
        eye2 = new DigitalInput(config.getInt("storage.eye2.dio", 7));
        eye3 = new DigitalInput(config.getInt("storage.eye3.dio", 8));
        eye4 = new DigitalInput(config.getInt("storage.eye4.dio", 9));
        eye5 = new DigitalInput(config.getInt("storage.eye5.dio", 10));
    }

    @Override
    public void periodic() {
        if (positionSensor.get()){
            indexEncoder.setPosition(0.0);
        }
    }

    public void move() {
        indexMotor.set(0.1);
    }
    public void reverse(){
        indexMotor.set(-0.1);
    }

    public void stop() {
        indexMotor.set(0.0);
    }

    public boolean getPositionSensor() {
        return positionSensor.get();
    }

    public int getPowerCellCount() {
        int count = 0;
        if (eye1.get()) 
            count ++; 
        if (eye2.get()) 
            count ++; 
        if (eye3.get()) 
            count ++; 
        if (eye4.get()) 
            count ++; 
        if (eye5.get()) 
            count ++;
        return count;
    }
    public double resetEncoder(){
        indexEncoder.setPosition(0.0);
        return indexEncoder.getPosition();
    }
    public boolean needsIndexing(){
        if (eye1.get() && !eye5.get()){
            return true;
        }else{
            return false;
        }
    }

}