package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Bon Hee Koo
 */
class Machine {
    /** Plugboard. */
    private Permutation _plugboard;
    /** Used rotors. */
    private Collection<Rotor> _allrotors;
    /** Number of moving rotors. */
    private int _pawls;
    /** Number of rotors. */
    private int _numRotors;
    /** Rotor slots. */
    private Rotor[] _slots;
    /** array list of rotors. */
    private ArrayList<Rotor> _rotorArray;

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _allrotors = allRotors;
        _plugboard = new Permutation("", _alphabet);
        _pawls = pawls;
        _rotorArray = (ArrayList<Rotor>) allRotors;
        _numRotors = numRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }
    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _slots = new Rotor[_numRotors];
        int i = 0;
        while (i < _slots.length) {
            int j = 0;
            if (j >= 0) {
                while (j < _rotorArray.size()) {
                    if (_rotorArray.get(j).name().equals(rotors[i])) {
                        _slots[i] = _rotorArray.get(j);
                    }
                    j++;
                }
            }
            i++;
        }
    }
    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int i = 0;
        while (i < setting.length()) {
            _slots[i + 1].set(setting.charAt(i));
            i++;
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] doesrotate = new boolean[_slots.length];
        doesrotate[_slots.length - 1] = true;
        int i = doesrotate.length - 1;
        int in = _plugboard.permute(c);
        while (i > 1) {
            if (_slots[i].atNotch() && _slots[i - 1].rotates()) {
                doesrotate[i] = true;
                doesrotate[i - 1] = true;
            }
            i--;
        }
        int j = 0;
        int a = 0;
        while (j < _slots.length) {
            if (j >= 0) {
                a++;
                if (doesrotate[j]) {
                    _slots[j].advance();
                }
                if (!doesrotate[j]) {
                    j = (a + 1) * j - (a * j);
                }
                j++;
            }
        }
        int k = _numRotors - 1;
        while (k > 0) {
            if (k < _numRotors) {
                in = _slots[k].convertForward(in);
                k--;
            }
        }
        int out = _slots[0].convertForward(in);
        int l = 1;
        while (l < _numRotors) {
            if (k <= l) {
                out = _slots[l].convertBackward(out);
                l++;
            }
        }
        return _plugboard.permute(out);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "");
        msg = msg.toUpperCase();
        int[] string2 = new int[msg.length()];
        int[] int2 = new int[string2.length];
        char[] outputinarray = new char[msg.length()];
        String result = "";
        int x = 0;
        while (x < string2.length) {
            string2[x] = _alphabet.toInt(msg.charAt(x));
            int2[x] = convert(string2[x]);
            outputinarray[x] = _alphabet.toChar(int2[x]);
            result += outputinarray[x];
            x++;
        }
        return result;
    }
    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
}
