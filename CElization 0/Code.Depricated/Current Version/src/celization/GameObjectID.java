/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celization;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author mjafar
 */
public class GameObjectID implements Serializable {
    private Class objectType;
    private int objectNumber;

    public GameObjectID(Class gameObjectClass, int number) {
        objectType = gameObjectClass;
        objectNumber = number;
    }

    @Override
    public boolean equals(Object e) {
        if (e == null) {
            return false;
        }
        if (e.getClass() != GameObjectID.class) {
            return false;
        }
        return ((GameObjectID)e).getNumber() == objectNumber && ((GameObjectID)e).getType() == objectType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.objectType);
        hash = 29 * hash + this.objectNumber;
        return hash;
    }

    public int getNumber() {
        return objectNumber;
    }

    public Class getType() {
        return objectType;
    }
}
