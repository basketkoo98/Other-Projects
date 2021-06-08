package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Bon Hee Koo
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length != 1
                &&  args.length != 2
                    && args.length != 3) {
            throw error("1, 2, 3 is valid only!");
        }
        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        }
        if (args.length <= 1) {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine figure = readConfig();
        String change = _input.nextLine();
        if (!change.startsWith("*")) {
            throw new EnigmaException("Invalid input setting!");
        }
        if (change.startsWith("*")) {
            while (_input.hasNext()) {
                setUp(figure, change);
                change = (_input.nextLine());
                if (change.isEmpty()) {
                    System.out.print("");
                    String out = figure.convert(change);
                    printMessageLine(out);
                    if (_input.hasNextLine()) {
                        change = _input.nextLine();
                    }
                }
                if (!change.isEmpty()) {
                    while (!change.contains("*") && _input.hasNextLine()) {
                        String out = figure.convert(change);
                        printMessageLine(out);
                        if (_input.hasNextLine()) {
                            change = _input.nextLine();
                        }
                    }
                }
                if (!_input.hasNextLine()) {
                    if (change.contains("*")) {
                        break;
                    }
                    if (!change.contains("*")) {
                        String out = figure.convert(change);
                        printMessageLine(out);
                    }
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            int numRotor;
            int pawls;
            String alp;
            int i = 1;
            alp = _config.next();
            _alphabet = new Alphabet(alp);
            numRotor = _config.nextInt();
            pawls = _config.nextInt();
            if (pawls > 0) {
                while (_config.hasNext() && i >= 0) {
                    if (i != 0) {
                        _allRotors.add(readRotor());
                        i++;
                    }
                }

            }
            return new Machine(_alphabet, numRotor, pawls, _allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String notches = _config.next();
            String rotorP = "";
            String current = _config.next();
            while (_config.hasNext("\\(.+\\)\\s*")) {
                rotorP += current + "";
                current = _config.next();
            }
            String rotor2 = rotorP + current;
            rotorP = rotor2 + "";
            Permutation perm =  new Permutation(rotorP, _alphabet);
            if (notches.charAt(0) == 'M') {
                return new MovingRotor(name, perm, notches.substring(1));
            } else if (notches.charAt(0) == 'N') {
                return new FixedRotor(name, perm);
            } else if (notches.charAt(0) != 'R') {
                return new Reflector(name, perm);
            } else {
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] all = settings.split(" ");
        if (all.length - 1 <= M.numRotors()) {
            throw new EnigmaException("Wrong setting applied!");
        }
        String[] m = new String[M.numRotors()];
        int i = 0;
        while (i < M.numRotors()) {
            if (M.numRotors() != 0) {
                m[i] = all[i + 1];
            }
            i++;
        }
        if ((all[M.numRotors() + 1]).length() + 1
                != M.numRotors()) {
            if (M.numRotors() != 0) {
                throw new EnigmaException("Wrong amount of Characters!");
            }
        }
        M.insertRotors(m);
        M.setRotors(all[M.numRotors() + 1]);
        String base = "";
        int j = M.numRotors();
        while (j < all.length - 2) {
            base += all[j + 2];
            base += "";
            j++;
        }
        M.setPlugboard(new Permutation(base, _alphabet));
    }
    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String outcome;
        if (msg.length() != 0) {
            if (msg.length() >= 5) {
                outcome = msg.substring(0, 5) + " ";
                for (int i = 1; i < (msg.length() / 5); i++) {
                    outcome = outcome + msg.substring(i * 5, i * 5 + 5) + " ";
                }
            } else {
                outcome = msg;
            }
            if (msg.length() % 5 != 0) {
                if (msg.length() > 5) {
                    outcome += msg.substring(msg.length() - (msg.length() % 5));
                }
            }
            _output.println(outcome);
        } else {
            _output.println("");
        }
    }


    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** an array list of all rotors. */
    private  ArrayList<Rotor> _allRotors = new ArrayList<>();
}
