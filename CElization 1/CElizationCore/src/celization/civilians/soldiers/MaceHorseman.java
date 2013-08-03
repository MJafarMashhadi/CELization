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
public class MaceHorseman extends Soldier {

    public MaceHorseman(String name) {
        super(name);
        scoreAgainstHorseman = GameParameters.mace_horsemanScore;
        scoreAgainstInfantry = GameParameters.mace_infantryScore;

        defensiveScore = GameParameters.maceDefensive;
        offensiveScore = GameParameters.maceOffensive;

        health = GameParameters.horsemanHealth;

        creationTime = GameParameters.maceETA;
    }
}
