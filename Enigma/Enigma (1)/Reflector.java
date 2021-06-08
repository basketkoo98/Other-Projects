package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Bon Hee Koo
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        set(0);
    }

    /** Returns true only when the rotor is a reflector. */
    boolean reflecting() {
        return true;
    }
}
