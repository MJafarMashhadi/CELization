package celization.civilians;

import java.io.Serializable;

public class Experience implements Serializable {

    protected double amountOfExperience = 0;
    private int exercisesCount = 0;

    public Experience() {
        amountOfExperience = 0;
        exercisesCount = 0;
    }

    public void excercise() {
        exercisesCount++;
        if (exercisesCount == 5) {
            exercisesCount = 0;
            amountOfExperience += 0.05;
        }
    }
}