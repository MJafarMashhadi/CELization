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
public class SapperInfantry extends Soldier {

    public SapperInfantry(String name) {
        super(name);
        scoreAgainstHorseman = GameParameters.sapper_horsemanScore;
        scoreAgainstInfantry = GameParameters.sapper_infantryScore;

        defensiveScore = GameParameters.infantryDefensive;
        offensiveScore = GameParameters.infantryOffensive;

        health = GameParameters.infantryHealth;

        creationTime = GameParameters.sapperETA;
    }

}
