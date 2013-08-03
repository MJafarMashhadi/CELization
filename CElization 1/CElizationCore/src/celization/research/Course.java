/**
 *
 */
package celization.research;

import celization.NaturalResources;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public class Course implements Serializable {

    public String[] requiredCourses;
    public String[] enoughCourses;
    private boolean done;
    private int ETA;
    private int phase;
    public NaturalResources requiredResources;

    /**
     *
     */
    public Course(String[] required, String[] enough, int ETA,
            NaturalResources requiredResources) {
        done = false;
        requiredCourses = required.clone();
        enoughCourses = enough.clone();
        this.ETA = ETA;
        this.requiredResources = requiredResources;
        phase = 0;
    }

    public boolean hasBeenDone() {
        return done;
    }

    public void progress() {
        if (done) {
            return;
        }
        phase++;
        if (phase == ETA) {
            done = true;
        }
    }

    public void forceToDo() {
        phase = ETA;
        done = true;
    }

    public int getETA() {
        return ETA;
    }
}
