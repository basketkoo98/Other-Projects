package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Bon Hee Koo
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        if (_notches != null) {
            return _notches.indexOf(alphabet().toChar(setting())) != -1;
        } else {
            return false;
        }
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }


    /** Notches of the rotator.*/
    private String _notches;

}
