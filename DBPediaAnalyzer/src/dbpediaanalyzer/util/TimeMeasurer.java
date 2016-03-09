package dbpediaanalyzer.util;

import java.util.concurrent.TimeUnit;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class TimeMeasurer {
    private long beginTime;
    private long stopTime;

    public TimeMeasurer() {
        this.beginTime = 0;
        this.stopTime = 0;
    }

    public void begin() {
        this.beginTime = System.currentTimeMillis();
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        long delay = this.stopTime - this.beginTime;

        long days = TimeUnit.MILLISECONDS.toDays(delay);
        long hours = TimeUnit.MILLISECONDS.toHours(delay) - days * 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(delay) - TimeUnit.MILLISECONDS.toHours(delay) * 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(delay) - TimeUnit.MILLISECONDS.toMinutes(delay) * 60;
        long milliseconds = delay - TimeUnit.MILLISECONDS.toSeconds(delay) * 1000;

        return days + " days " + hours + ":" + minutes + ":" + seconds + ":" + milliseconds;
    }
}
