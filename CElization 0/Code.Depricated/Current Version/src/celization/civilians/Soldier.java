/**
 * 
 */
package celization.civilians;

/**
 * @author mjafar
 * 
 */
// TODO: next phase
public final class Soldier extends Civilian {

	/**
	 * @param name
	 */
	public Soldier(String name) {
		super(name);
	}

	@Override
	public String getTypeString() {
		return "soldier";
	}

}
