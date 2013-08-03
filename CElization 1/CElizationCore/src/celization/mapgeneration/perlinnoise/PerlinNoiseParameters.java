package celization.mapgeneration.perlinnoise;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        this.changeParameters(persistence, frequency, amplitude, octaves, randomseed);
    }

    public void changeParameters(double persistence, double frequency, double amplitude, int octaves, int randomseed) {
        this.persistence = persistence > 0.999999D ? 0.9 : persistence;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.octaves = octaves;
        this.randomseed = Math.abs(randomseed);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Object value = new Object();
        for(Field field: this.getClass().getFields()) {
            ret.append(field.getName());
            ret.append(" = ");
            try {
                value = field.get(this);
            } catch (    IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(PerlinNoiseParameters.class.getName()).log(Level.SEVERE, null, ex);
            }
            ret.append(value.toString());
            ret.append("\t");
        }

        return ret.toString();
    }
}
