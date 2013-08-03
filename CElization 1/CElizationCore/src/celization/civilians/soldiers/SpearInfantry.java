/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celization.civilians.soldiers;

import celization.GameParameters;

/**
 *
 * @author mjafar
 */
public class SpearInfantry extends Soldier {

    public SpearInfantry(String name) {
        super(name);
        scoreAgainstHorseman = GameParameters.spear_horsemanScore;
        scoreAgainstInfantry = GameParameters.spear_infantryScore;

        defensiveScore = GameParameters.infantryDefensive;
        offensiveScore = GameParameters.infantryOffensive;

        health = GameParameters.infantryHealth;

        creationTime = GameParameters.spearETA;
    }

}
