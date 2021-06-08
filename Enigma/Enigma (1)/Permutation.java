package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Bon Hee Koo
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _rotation = cycles;
        int i = 0;
        while (i < _rotation.length() - 1) {
            if (_rotation.charAt(i) == _rotation.charAt(i + 1)) {
                throw new EnigmaException("repeated");
            }
            i++;
        }
        char[] testing = _rotation.replaceAll("(\\s)", "").toCharArray();
        int j = 0;
        while (j < testing.length) {
            if (!_alphabet.contains(testing[j])) {
                throw new EnigmaException("Non existing alphabet");
            }
            j++;
        }
    }
    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _rotation += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int outInt = _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
        return outInt;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int outInt = _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));
        return outInt;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char val = p;
        if (_rotation.equals("")) {
            return val;
        }
        if (!_alphabet.contains(p)) {
            return val;
        }
        if (_rotation.indexOf(String.valueOf(p)) != -1) {
            if (_rotation.charAt(
                    _rotation.indexOf(String.valueOf(p)) + 1) != ')') {
                val = _rotation.charAt(
                        _rotation.indexOf(String.valueOf(p)) + 1);
            } else {
                int frontIdx = _rotation.indexOf(String.valueOf(p));
                while (_rotation.charAt(frontIdx) != '(') {
                    frontIdx -= 1;
                }
                val = _rotation.charAt(frontIdx + 1);
            }
        }
        return val;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char val = c;
        if (_rotation.equals("")) {
            return val;
        }
        if (!_alphabet.contains(c)) {
            return val;
        }
        if (_rotation.indexOf(
                String.valueOf(c)) != -1) {
            if (_rotation.charAt(
                    _rotation.indexOf(String.valueOf(c)) - 1) != '(') {
                val = _rotation.charAt(
                        _rotation.indexOf(String.valueOf(c)) - 1);
            } else {
                int backIdx = _rotation.indexOf(String.valueOf(c));
                while (_rotation.charAt(backIdx) != ')') {
                    backIdx += 1;
                }
                val = _rotation.charAt(backIdx - 1);
            }
        }
        return val;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int i = 0;
        while (i < _alphabet.size()) {
            if (_alphabet.toChar(i) == permute(_alphabet.toChar(i))) {
                return false;
            }
            i++;
        }
        return true;
    }
    /** string of cycles. */
    private String _rotation;

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
}
