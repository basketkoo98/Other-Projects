package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Bon Hee Koo
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;

    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _set;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _set = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _set = _permutation.alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int val = 0;
        if (p >= 0) {
            return _permutation.wrap(
                    _permutation.permute(
                            _permutation.wrap(p + _set)) - _set);
        }
        if (p < 0) {
            return 0;
        }
        return 0;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int val = 0;
        if (e >= 0) {
            return _permutation.wrap(
                    _permutation.invert(
                            _permutation.wrap(e + _set)) - _set);
        }
        if (e < 0) {
            return 0;
        }
        return 0;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance one position, if possible. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** Name. */
    private final String _name;

    /** Permutation implemented by the rotor. */
    private Permutation _permutation;

    /** Current setting. */
    private int _set;
}
