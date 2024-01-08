// Enum for AlarmSignal
enum AlarmSignal {
    OFF,
    ON
}

// Enum for CoolantSignal
enum CoolantSignal {
    OFF,
    ON
}

// Enum for RodSignal
enum RodSignal {
    MORE,
    LESS,
    NOCHANGE
}

// Temperature class
class Temperature {
    private Double value;

    public Temperature(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}

// Class representing the state of FissionTemperatureMonitor
class FissionTemperatureMonitor {
    private Temperature receivedTemp;
    private AlarmSignal alarmStatus;
    private CoolantSignal coolantStatus;
    private RodSignal rodStatus;

    // Constructor
    public FissionTemperatureMonitor() {
        this.receivedTemp = null;
        this.alarmStatus = AlarmSignal.OFF;
        this.coolantStatus = CoolantSignal.OFF;
        this.rodStatus = RodSignal.NOCHANGE;
    }

    // Invariant
    public boolean validTemp(Temperature temp) {
        return temp != null && temp.getValue() != null;
    }

    // Function to record the initial temperature of the system
    public void getTemperature(Temperature recordedTemp) {
        assert validTemp(recordedTemp) : "Recorded temperature should not be null";
        this.receivedTemp = recordedTemp;
    }

    // Function to check the temperature of the system
    public void checkTemperature() {
        assert validTemp(receivedTemp) : "Received temperature should not be null";
        
        if (receivedTemp.getValue() < 50.0){
            withdrawRod();
        }
        else if (receivedTemp.getValue() >= 50.0&& receivedTemp.getValue() <=  70.0) {
            optimalRods();
        } 

        else if (150.0 > receivedTemp.getValue() && receivedTemp.getValue() > 70.0) {
            insertRod();
        } 
        
        else if (receivedTemp.getValue() >= 150.0) {
            optimalRods();
        } 
        
    }

    // Function to insert rods
    private void insertRod() {
        if (rodStatus == RodSignal.LESS || rodStatus == RodSignal.NOCHANGE) {
            rodStatus = RodSignal.MORE;
        }
    }

    // Function to withdraw rods
    private void withdrawRod() {
        if (rodStatus == RodSignal.MORE || rodStatus == RodSignal.NOCHANGE) {
            rodStatus = RodSignal.LESS;
        }
    }

    // Function for optimal rod status
    private void optimalRods() {
        if (rodStatus == RodSignal.NOCHANGE) {
            // Your logic for optimal rod status here
            // For now, it remains NOCHANGE
        }
    }

    // Function for emergency control
    public void emergencyControl() {
        assert validTemp(receivedTemp) : "Received temperature should not be null";

        if (receivedTemp.getValue() >= 150.0) {
            ringAlarm();
            activateCoolant();
        } else {
            stopAlarm();
            deactivateCoolant();
        }
    }

    // Function to ring the alarm
    private void ringAlarm() {
        if (alarmStatus == AlarmSignal.OFF) {
            alarmStatus = AlarmSignal.ON;
        }
    }

    // Function to activate the coolant
    private void activateCoolant() {
        if (coolantStatus == CoolantSignal.OFF) {
            coolantStatus = CoolantSignal.ON;
        }
    }

    // Function to stop the alarm
    private void stopAlarm() {
        if (alarmStatus == AlarmSignal.ON) {
            alarmStatus = AlarmSignal.OFF;
        }
    }

    // Function to deactivate the coolant
    private void deactivateCoolant() {
        if (coolantStatus == CoolantSignal.ON) {
            coolantStatus = CoolantSignal.OFF;
        }
    }

    // Getter for alarm status
    public AlarmSignal getAlarmStatus() {
        return alarmStatus;
    }

    // Getter for coolant status
    public CoolantSignal getCoolantStatus() {
        return coolantStatus;
    }

    // Getter for rod status
    public RodSignal getRodStatus() {
        return rodStatus;
    }
}
public class Main {
    public static void main(String[] args) {
        FissionTemperatureMonitor monitor = new FissionTemperatureMonitor();

        // Example: Valid recorded temperature, should print signals
        Temperature recordedTemp = new Temperature(90.0);
        monitor.getTemperature(recordedTemp);
        monitor.checkTemperature();
        monitor.emergencyControl();
        printStatus(monitor);
    }

    private static void printStatus(FissionTemperatureMonitor monitor) {
        System.out.println("==== Current System Status ====");
        System.out.println("Alarm Status: " + monitor.getAlarmStatus());
        System.out.println("Coolant Status: " + monitor.getCoolantStatus());
        System.out.println("Rod Status: " + monitor.getRodStatus());
        System.out.println("===============================");
    }
};