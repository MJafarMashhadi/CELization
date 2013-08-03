/**
 *
 */
package celization.research;

import celizationrequests.GameObjectID;
import java.util.HashMap;

import celization.NaturalResources;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public final class CourseManager implements Serializable {

    private HashMap<String, GameObjectID> courseNameToID;
    private HashMap<GameObjectID, Course> courseList;
    private GameObjectID inProgress = null;

    public CourseManager() {
        courseNameToID = new HashMap<>();
        courseList = new HashMap<>();

        String[] required;
        String[] enough;
        String[] zeroArray = new String[0];
        NaturalResources requiredResources;

        /**
         * Resources
         */
        required = zeroArray;
        enough = zeroArray;
        requiredResources = new NaturalResources(5, 0, 0, 0, 0);
        add("Resources", required, enough, 1, requiredResources);

        /**
         * Resources/Mining
         */
        required = new String[]{"Resources"};
        requiredResources = new NaturalResources(10, 0, 0, 0, 0);
        add("Mining", required, enough, 2, requiredResources);

        /**
         * Resources/Agriculture
         */
//        required = new String[] { "Resources" };
        requiredResources = new NaturalResources(10, 0, 0, 0, 0);
        add("Agriculture", required, enough, 2, requiredResources);

        /**
         * Resources/Lumber Mill
         */
//        required = new String[] { "Resources" };
        requiredResources = new NaturalResources(10, 0, 0, 0, 0);
        add("Lumber Mill", required, enough, 2, requiredResources);

        /**
         * Resources/Mining/Refinery
         */
        required = new String[]{"Mining"};
        requiredResources = new NaturalResources(70, 30, 10, 0, 120);
        add("Refinery", required, enough, 16, requiredResources);

        /**
         * Resources/Agriculture/Irrigation
         */
        required = new String[]{"Agriculture"};
        requiredResources = new NaturalResources(70, 10, 30, 0, 120);
        add("Irrigation", required, enough, 20, requiredResources);

        /**
         * Resources/Agriculture/Ranching
         */
//        required = new String[] { "Agriculture" };
        requiredResources = new NaturalResources(35, 0, 20, 0, 10);
        add("Ranching", required, enough, 8, requiredResources);

        /**
         * Resources/Agriculture/Fishing
         */
        required = new String[]{"Ranching", "Lumber Mill"};
        requiredResources = new NaturalResources(10, 0, 0, 0, 0);
        add("Fishing", required, enough, 16, requiredResources);

        /**
         * Resources/Lumber Mill/Carpentry
         */
        required = new String[]{"Lumber Mill"};
        requiredResources = new NaturalResources(70, 0, 50, 0, 120);
        add("Carpentry", required, enough, 16, requiredResources);

        /**
         * Science
         */
        required = zeroArray;
        requiredResources = new NaturalResources(10, 0, 0, 0, 0);
        add("Science", required, enough, 2, requiredResources);

        /**
         * Science/School
         */
        required = new String[]{"Science"};
        requiredResources = new NaturalResources(30, 40, 30, 0, 20);
        add("School", required, enough, 4, requiredResources);

        /**
         * Science/Alphabet
         */
//        required = new String[] { "Science" };
        requiredResources = new NaturalResources(20, 0, 0, 0, 25);
        add("Alphabet", required, enough, 6, requiredResources);

        /**
         * Science/School/Alphabet/Mathematics
         */
        required = new String[]{"Alphabet"};
        requiredResources = new NaturalResources(20, 0, 0, 0, 50);
        add("Mathematics", required, enough, 10, requiredResources);

        /**
         * Science/School/.../University
         */
        required = zeroArray;
        enough = new String[]{"Carpentry", "School"};
        requiredResources = new NaturalResources(60, 80, 60, 0, 100);
        add("University", required, enough, 12, requiredResources);

        /**
         * Science/School/Alphabet/Mathematics/Engineering
         */
        required = new String[]{"Mathematics"};
        enough = zeroArray;
        requiredResources = new NaturalResources(50, 50, 50, 0, 120);
        add("Engineering", required, enough, 24, requiredResources);

        /**
         * Science/School/Alphabet/Mathematics/Engineering/CERN
         */
        required = new String[]{"Engineering", "University"};
        requiredResources = new NaturalResources(150, 150, 150, 0, 250);
        add("CERN", required, enough, 28, requiredResources);

        /**
         * Economy
         */
        required = zeroArray;
        requiredResources = new NaturalResources(20, 0, 10, 0, 20);
        add("Economy", required, enough, 4, requiredResources);

        /**
         * Economy/Micro Economics
         */
        required = new String[]{"Economy"};
        requiredResources = new NaturalResources(65, 15, 15, 0, 80);
        add("Micro Economics", required, enough, 10, requiredResources);

        /**
         * Economy/Project Management
         */
//        required = new String[] { "Economy" };
        requiredResources = new NaturalResources(80, 0, 0, 0, 60);
        add("Project Management", required, enough, 10, requiredResources);

        /**
         * Economy/Micro Economics/Macro Economics
         */
        required = zeroArray;
        enough = new String[]{"Micro Economics", "Mathematics"};
        requiredResources = new NaturalResources(90, 25, 25, 0, 140);
        add("Macro Economics", required, enough, 24, requiredResources);

        /**
         * Economy/Micro Economics/Market
         */
        required = new String[]{"Micro Economics", "Project Management"};
        enough = zeroArray;
        requiredResources = new NaturalResources(50, 50, 50, 0, 50);
        add("Market", required, enough, 12, requiredResources);

        /**
         * Economy/Project Management/Strategical Project Management
         */
        required = new String[]{"Project Management"};
        requiredResources = new NaturalResources(100, 0, 0, 0, 120);
        add("Strategical Project Management", required, enough, 16,
                requiredResources);

        /**
         * Economy/Micro Economics/Macro Economics/Tax
         */
        required = new String[]{"Macro Economics"};
        requiredResources = new NaturalResources(100, 0, 0, 0, 150);
        add("Tax", required, enough, 18, requiredResources);

        /**
         * Economy/Micro Economics/Macro Economics/Parsimony
         */
//        required = new String[] { "Macro Economics" };
        requiredResources = new NaturalResources(80, 10, 0, 0, 130);
        add("Parsimony", required, enough, 12, requiredResources);

        /**
         * Economy/Project Management/Strategical Project Management/Team Work
         */
        required = new String[]{"Strategical Project Management"};
        requiredResources = new NaturalResources(100, 0, 0, 0, 120);
        add("Team Work", required, enough, 22, requiredResources);

        /**
         * Military
         */
        required = zeroArray;
        requiredResources = new NaturalResources(20, 0, 20, 0, 0);
        add("Military", required, enough, 4, requiredResources);

        /**
         * Military/Horseback Riding
         */
        required = new String[]{"Military"};
        requiredResources = new NaturalResources(70, 0, 40, 0, 15);
        add("Horseback Riding", required, enough, 10, requiredResources);

        /**
         * Military/Basic Combat
         */
        required = new String[]{"Military"};
        requiredResources = new NaturalResources(50, 0, 50, 0, 15);
        add("Basic Combat", required, enough, 8, requiredResources);

        /**
         * Military/Horseback Riding/Animal Husbandry
         */
        required = new String[]{"Horseback Riding"};
        requiredResources = new NaturalResources(200, 0, 300, 0, 30);
        add("Animal Husbandry", required, enough, 20, requiredResources);

        /**
         * Military/Basic Combat/Advanced Armors
         */
        required = new String[]{"Basic Combat"};
        requiredResources = new NaturalResources(150, 0, 350, 0, 30);
        add("Advanced Armors", required, enough, 20, requiredResources);


    }

    private void add(final String name, final String[] required, final String[] enough, final int ETA, final NaturalResources requiredResources) {
        GameObjectID courseID = new GameObjectID(Course.class, courseNameToID.size());
        courseNameToID.put(name, courseID);
        courseList.put(courseID, new Course(required, enough, ETA, requiredResources));
    }

    public void startResearch(String name) {
        inProgress = courseNameToID.get(name);
    }

    public boolean dependencyMet(GameObjectID courseID) {
        /**
         * Enough courses. one of them is enough
         */
        for (String c : courseList.get(courseID).enoughCourses) {
            if (courseList.get(courseNameToID.get(c)).hasBeenDone()) {
                return true;
            }
        }
        if (courseList.get(courseID).requiredCourses.length == 0
                && courseList.get(courseID).enoughCourses.length != 0) {
            return false;
        }
        /**
         * Required courses. All of them are required
         */
        for (String c : courseList.get(courseID).requiredCourses) {
            if (!courseList.get(courseNameToID.get(c)).hasBeenDone()) {
                return false;
            }
        }

        return true;
    }

    public boolean dependencyMet(String name) {
        GameObjectID courseID = courseNameToID.get(name);
        return dependencyMet(courseID);
    }

    public boolean hasBeenDone(String name) {
        return courseList.get(courseNameToID.get(name)).hasBeenDone();
    }

    public GameObjectID progress() {
        GameObjectID doneInThisTurn = null;

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
        return courseList.get(courseNameToID.get(name));
    }

    public GameObjectID getCurrent() {
        return inProgress;
    }

    public String getName(GameObjectID id) {
        if (id.getType() != Course.class) {
            return null;
        }
        for (String name : courseNameToID.keySet()) {
            if (id.equals(courseNameToID.get(name))) {
                return name;
            }
        }
        return null;
    }

    public boolean isBusy() {
        return inProgress != null;
    }

    public HashMap<GameObjectID, Course> getCourses() {
        return courseList;
    }
}
