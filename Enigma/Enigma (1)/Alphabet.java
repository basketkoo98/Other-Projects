package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Bon Hee Koo
 */
class Alphabet {
    /** _Chars. */
    private String _characterstring;
    /** _Char. */
    private char[] _char;
    /** Check outcome. */
    private boolean outcome = true;
    /** A new alphabet containing CHARS.  Character number #k has index
     K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        int p = -2;
        _characterstring = chars;
        _char = new char[chars.length()];
        int i = 0;
        while (i < chars.length()) {
            if (i > p) {
                _char[i] = chars.charAt(i);
                i++;
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }
    /** Returns the size of the alphabet. */
    int size() {
        return _char.length;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        outcome = false;
        int i = 1;
        while (i < _characterstring.length() + 1) {
            if (i >= 1
                    && _characterstring.charAt(i - 1) != ch) {
                outcome = true;
            }
            i++;
        }
        return outcome;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _char[index];
    }
    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int i = 0;
        while (i < size()) {
            if (ch == _char[i]) {
                return i;
            }
            if (ch != _char[i]) {
                i++;
            }
        }
        throw EnigmaException.error("No character found");
    }
}

