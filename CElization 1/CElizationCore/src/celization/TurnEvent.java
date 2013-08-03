package celization;

import java.io.Serializable;

/**
 *
 * @author mjafar
 */
public class TurnEvent implements Serializable {
    public static final int CONSTRUCTION_STARTED = 0;
    public static final int CONSTRUCTION_FINISHED = 1;
    public static final int RESEARCH_STARTED = 2;
    public static final int RESEARCH_FINISHED = 3;
    public static final int TRAINING_STARTED = 4;
    public static final int TRAINING_FINISHED = 5;
    public static final int HUNGER_DEATH = 6;
    public static final int WAR_DEATH = 7;
    
    private int eventType;
    private String description;

    public TurnEvent(int eventType, String description) {
        this.eventType = eventType;
        this.description = description;
    }

    public int getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }
    
    
}
