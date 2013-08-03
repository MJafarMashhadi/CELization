/**
 *
 */
package celization.civilians.soldiers;

import celization.civilians.Civilian;

/**
 * @author mjafar
 *
 */
public abstract class Soldier extends Civilian {

    protected int scoreAgainstInfantry;
    protected int scoreAgainstHorseman;
    protected float defensiveScore;
    protected float offensiveScore;
    protected float health;

    public float getHealth() {
        return health;
    }

    /**
     * @param name
     */
    public Soldier(String name) {
        super(name);
    }

    /**
     * True if it's an infantry
     * @return
     */
    public boolean isInfanrty() {
        return (this instanceof SapperInfantry || this instanceof SpearInfantry);
    }

    /**
     * True if it's a horseman
     * @return
     */
    public boolean isHorseman() {
        return !isInfanrty();
    }

    /**
     * Calculates S score for this soldier.
     * S score is calculated by this formula:
     * S = A + B
     * where A is score against infantry or horseman depending on what kind of Soldier is the foe
     * and B is offensive or defensive score depending on what "this" is doing, defensing himself or attacking another soldier
     * @param foe
     * @param isAttacking
     * @return
     */
    public float calculateScore(Soldier foe, boolean isAttacking) {
        return (foe.isHorseman() ? scoreAgainstHorseman : scoreAgainstInfantry) + (isAttacking ? offensiveScore : defensiveScore);
    }

    public void decreseHealth(float amount) {
        health -= amount;
        if (health < 0) {
            super.die();
        }
    }

}
