/**
 *
 */
package celization.research;

import java.util.HashMap;
import java.util.Map;

import celization.NaturalResources;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public final class CourseManager implements Serializable {

    public static Map<String, Course> courseList = new HashMap<String, Course>();
    public String inProgress = null;

    public CourseManager() {
        String[] required;
        String[] enough;
        NaturalResources requiredResources;

        /**
         * Resources
         */
        required = new String[0];
        enough = new String[0];
        requiredResources = new NaturalResources(0, 5, 0, 0, 0);
        add("Resources", required, enough, 1, requiredResources);

        /**
         * Resources/Mining
         */
        required = new String[1];
        required[0] = "Resources";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 10, 0, 0, 0);
        add("Mining", required, enough, 2, requiredResources);

        /**
         * Resources/Agriculture
         */
        required = new String[1];
        required[0] = "Resources";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 10, 0, 0, 0);
        add("Agriculture", required, enough, 2, requiredResources);

        /**
         * Resources/Lumber Mill
         */
        required = new String[1];
        required[0] = "Resources";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 10, 0, 0, 0);
        add("Lumber Mill", required, enough, 2, requiredResources);

        /**
         * Resources/Mining/Refinery
         */
        required = new String[1];
        required[0] = "Mining";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 70, 120, 30, 10);
        add("Refinery", required, enough, 16, requiredResources);

        /**
         * Resources/Agriculture/Irrigation
         */
        required = new String[1];
        required[0] = "Agriculture";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 70, 120, 10, 30);
        add("Irrigation", required, enough, 20, requiredResources);

        /**
         * Resources/Agriculture/Ranching
         */
        required = new String[1];
        required[0] = "Agriculture";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 35, 10, 0, 20);
        add("Ranching", required, enough, 8, requiredResources);

        /**
         * Resources/Agriculture/Fishing
         */
        required = new String[2];
        required[0] = "Ranching";
        required[1] = "Lumber Mill";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 10, 0, 0, 0);
        add("Fishing", required, enough, 16, requiredResources);

        /**
         * Resources/Lumber Mill/Carpentry
         */
        required = new String[1];
        required[0] = "Lumber Mill";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 70, 120, 0, 50);
        add("Carpentry", required, enough, 16, requiredResources);

        /**
         * Science
         */
        required = new String[0];
        enough = new String[0];
        requiredResources = new NaturalResources(0, 10, 0, 0, 0);
        add("Science", required, enough, 2, requiredResources);

        /**
         * Science/School
         */
        required = new String[1];
        required[0] = "Science";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 30, 20, 40, 30);
        add("School", required, enough, 4, requiredResources);

        /**
         * Science/Alphabet
         */
        required = new String[1];
        required[0] = "Science";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 20, 25, 0, 0);
        add("Alphabet", required, enough, 6, requiredResources);

        /**
         * Science/School/Alphabet/Mathematics
         */
        required = new String[1];
        required[0] = "Alphabet";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 20, 50, 0, 0);
        add("Mathematics", required, enough, 10, requiredResources);

        /**
         * Science/School/.../University
         */
        required = new String[0];
        enough = new String[2];
        enough[0] = "Carpentry";
        enough[1] = "School";
        requiredResources = new NaturalResources(0, 60, 100, 80, 60);
        add("University", required, enough, 12, requiredResources);

        /**
         * Science/School/Alphabet/Mathematics/Engineering
         */
        required = new String[1];
        required[0] = "Mathematics";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 50, 120, 50, 50);
        add("Engineering", required, enough, 24, requiredResources);

        /**
         * Science/School/Alphabet/Mathematics/Engineering/CERN
         */
        required = new String[2];
        required[0] = "Engineering";
        required[1] = "University";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 150, 250, 150, 150);
        add("CERN", required, enough, 28, requiredResources);

        /**
         * Economy
         */
        required = new String[0];
        enough = new String[0];
        requiredResources = new NaturalResources(0, 20, 20, 0, 10);
        add("Economy", required, enough, 4, requiredResources);

        /**
         * Economy/Micro Economics
         */
        required = new String[1];
        required[0] = "Economy";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 65, 80, 15, 15);
        add("Micro Economics", required, enough, 10, requiredResources);

        /**
         * Economy/Project Management
         */
        required = new String[1];
        required[0] = "Economy";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 80, 60, 0, 0);
        add("Project Managment", required, enough, 10, requiredResources);

        /**
         * Economy/Micro Economics/Macro Economics
         */
        required = new String[0];
        enough = new String[2];
        enough[0] = "Micro Economics";
        enough[1] = "Mathematics";
        requiredResources = new NaturalResources(0, 90, 140, 25, 25);
        add("Macro Economics", required, enough, 24, requiredResources);

        /**
         * Economy/Micro Economics/Market
         */
        required = new String[2];
        required[0] = "Micro Economics";
        required[1] = "Project Managment";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 50, 50, 50, 50);
        add("Market", required, enough, 12, requiredResources);

        /**
         * Economy/Project Managment/Strategical Project Managment
         */
        required = new String[1];
        required[0] = "Project Managment";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 100, 120, 0, 0);
        add("Strategical Project Managment", required, enough, 16,
                requiredResources);

        /**
         * Economy/Micro Economics/Macro Economics/Tax
         */
        required = new String[1];
        required[0] = "Macro Economics";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 100, 150, 0, 0);
        add("Tax", required, enough, 18, requiredResources);

        /**
         * Economy/Micro Economics/Macro Economics/Parsimony
         */
        required = new String[1];
        required[0] = "Macro Economics";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 80, 130, 10, 0);
        add("Parsimony", required, enough, 12, requiredResources);

        /**
         * Economy/Project Managment/Strategical Project Managment/Team Work
         */
        required = new String[1];
        required[0] = "Strategical Project Managment";
        enough = new String[0];
        requiredResources = new NaturalResources(0, 100, 120, 0, 0);
        add("Team Work", required, enough, 22, requiredResources);

        // TODO: add military
        // /** Military */
        // required = new String[0];
        // enough = new String[0];
        // requiredResources = new NaturalResources(0, gold, sc, stone,
        // wood);
        // add("Military", required, enough, , requiredResources);
    }

    private void add(String name, String[] required, String[] enough, int ETA,
            NaturalResources requiredResources) {
        courseList.put(name, new Course(required, enough, ETA,
                requiredResources));
    }

    public void startResearch(String name) {
        inProgress = name;
    }

    public boolean dependencyMet(String name) {
        /**
         * Enough courses. one of them is enough
         */
        for (String c : courseList.get(name).enoughCourses) {
            if (courseList.get(c).hasBeenDone()) {
                return true;
            }
        }
        if (courseList.get(name).requiredCourses.length == 0
                && courseList.get(name).enoughCourses.length != 0) {
            return false;
        }
        /**
         * Required courses. All of them are required
         */
        for (String c : courseList.get(name).requiredCourses) {
            if (!courseList.get(c).hasBeenDone()) {
                return false;
            }
        }

        return true;
    }

    public boolean hasBeenDone(String name) {
        return courseList.get(name).hasBeenDone();
    }

    public String progress() {
        String doneInThisTurn = null;

        if (inProgress != null) {
            courseList.get(inProgress).progress();
            if (courseList.get(inProgress).hasBeenDone()) {
                doneInThisTurn = inProgress;
                inProgress = null;
            }
        }

        return doneInThisTurn;
    }

    public Course get(String name) {
        return courseList.get(name);
    }

    public Course getCurrent() {
        return get(inProgress);
    }
}
