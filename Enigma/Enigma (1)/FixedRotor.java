package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Bon Hee Koo
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        set(0);
    }
    @Override
    void advance() {
        throw new EnigmaException(" Advance is not"
                + " called in fixed rotor");
    }
}
