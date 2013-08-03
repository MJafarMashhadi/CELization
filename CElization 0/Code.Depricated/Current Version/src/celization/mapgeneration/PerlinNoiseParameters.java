/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celization.mapgeneration;

import java.io.Serializable;

/**
 *
 * @author mjafar
 */
public class PerlinNoiseParameters implements Serializable {

    public double persistence;
    public double frequency;
    public double amplitude;
    public int octaves;
    public int randomseed;

    public PerlinNoiseParameters(double persistence, double frequency, double amplitude, int octaves, int randomseed) {
        this.ChangeParameters(persistence, frequency, amplitude, octaves, randomseed);
    }

    public void ChangeParameters(double persistence, double frequency, double amplitude, int octaves, int randomseed) {
        this.persistence = persistence;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.randomseed = 2 + randomseed * randomseed;
    }
}
