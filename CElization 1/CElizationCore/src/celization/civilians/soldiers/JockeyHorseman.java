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
public class JockeyHorseman extends Soldier {

    public JockeyHorseman(String name) {
        super(name);
        scoreAgainstHorseman = GameParameters.jockey_horsemanScore;
        scoreAgainstInfantry = GameParameters.jockey_infantryScore;

        defensiveScore = GameParameters.jockeyDefensive;
        offensiveScore = GameParameters.jockeyOffensive;

        health = GameParameters.horsemanHealth;

        creationTime = GameParameters.jockeyETA;
    }
}
