import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Student {
    private final String name;
    private final String id;
    private final long arrivalMillis; // epoch millis when enqueued

    private static final DateTimeFormatter SHORT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Normal constructor: arrival time set to now
    public Student(String name, String id) {
        this(name, id, System.currentTimeMillis());
    }

    // Constructor used when loading from storage (preserve arrival time)
    public Student(String name, String id, long arrivalMillis) {
        this.name = name == null ? "" : name.trim();
        this.id = id == null ? "" : id.trim();
        this.arrivalMillis = arrivalMillis;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public long getArrivalMillis() {
        return arrivalMillis;
    }

    public String getFormattedArrival() {
        LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(arrivalMillis), ZoneId.systemDefault());
        return dt.format(SHORT_FMT);
    }

    // CSV-friendly line (name,id,arrivalMillis)
    public String toDataLine() {
        // avoid commas in CSV fields for simplicity; use '|' as separator when saving a simple data file
        return name.replace("|", " ") + "|" + id.replace("|", " ") + "|" + arrivalMillis;
    }

    // Human-friendly
    @Override
    public String toString() {
        return name + ", " + id + " (arrived: " + getFormattedArrival() + ")";
    }
}

