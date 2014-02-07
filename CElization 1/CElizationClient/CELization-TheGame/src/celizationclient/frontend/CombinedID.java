package celizationclient.frontend;

import celizationrequests.GameObjectID;

/**
 *
 * @author MohammmadJafar MashhadiEbrahim
 */
class CombinedID {

    private GameObjectID goid;
    private String username;

    public CombinedID(GameObjectID goid, String username) {
        this.goid = goid;
        this.username = username;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash += 49 * goid.hashCode();
        hash += 49 * username.hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return this.username.equals(((CombinedID) obj).getUsername())
                && this.goid.equals(((CombinedID) obj).getGoid());
    }

    public GameObjectID getGoid() {
        return goid;
    }

    public String getUsername() {
        return username;
    }
}
