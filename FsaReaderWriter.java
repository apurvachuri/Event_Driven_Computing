import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class FsaReaderWriter implements FsaIo {

    //This class handles reading and writing FSA representations as
    //described in the practical specification
    //Read the description of a finite-state automaton from the
    //InputStream, is, and transfers it to Fsa, f.
    private enum dataPattern {

        lowerCase, upperCase
    }
    dataPattern inputFormat = dataPattern.lowerCase;

    public FsaReaderWriter() {
    }

    private boolean IsJavaIdentifier(String name) {

        if (name == null) {
            return false;
        }

        if (name.length() == 0) {
            return false;
        }

        //	Start with a letter?
        if (!Character.isLetter(name.charAt(0))) {
            return false;
        }

        //	Check the remaining characters in the string
        for (int i = 1; i < name.length(); i++) {
            if (!Character.isLetterOrDigit(name.charAt(i)) && !name.substring(i, i + 1).equalsIgnoreCase("_")) {
                return false;
            }
        }

        return true;
    }

    private boolean IsValidEventName(String eventName) {
        //Epsilon transition event
        if (null == eventName) {
            return false;
        } else if ((eventName.length() == 1) && eventName.equalsIgnoreCase("?")) {
            return true;
        }

        else if(!eventName.matches("[a-zA-Z]+")){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void read(Reader r, Fsa f) throws IOException, FsaFormatException {
        BufferedReader reader1 = new BufferedReader(r);
        String line;
        int lineno = 1;
        StringTokenizer tokens;
        ArrayList<String> recordItems = new ArrayList<>();
        String fromState;
        String toState;
        String event;
        String initialState;
        String finalState;

        //Retrieve all lines
        while ((line = reader1.readLine()) != null) {
            tokens = new StringTokenizer(line);
            lineno++;
            recordItems.clear(); //New record

            while (tokens.hasMoreTokens()) {
                recordItems.add(tokens.nextToken());
            }

            //Check if the number of items is valid
            int numItems = recordItems.size();
            if (numItems != 4 && numItems != 2) {
                continue;
            }

            //Store states into fsa
            if (recordItems.get(0).equalsIgnoreCase("STATE")) {
                if(recordItems.size() != 4){
                    throw new FsaFormatException(lineno,"Invalid input for states");
                }

                if (!IsJavaIdentifier(recordItems.get(1))) {
                    throw new FsaFormatException(lineno,"Invalid state");
                }

                if (null != f.findState(recordItems.get(1))) {
                    throw new FsaFormatException(lineno, "State Exists");
                }
                try {
                    f.newState(recordItems.get(1), Integer.valueOf(recordItems.get(2)), Integer.valueOf(recordItems.get(3)));
                } catch (Exception e) {
                    throw new FsaFormatException(lineno, "Could not create state");
                }
            }

            //Store transitions into fsa
            if (recordItems.get(0).equalsIgnoreCase("TRANSITION")) {

                if(recordItems.size()!=4){
                    throw new FsaFormatException(lineno, "Invalid input for Transition");
                }

                if (recordItems.get(1) == null || recordItems.get(3) == null) {
                    throw new FsaFormatException(lineno, "From/To state is null");
                }
                if(recordItems.get(2) == null){
                    throw new FsaFormatException(lineno, "Event Name is null");
                }

                fromState = recordItems.get(1);
                toState = recordItems.get(3);

                //Check the vadility of the event name
                if (!IsValidEventName(recordItems.get(2))) {
                    throw new FsaFormatException(lineno,"Event Name Not Valid <"+recordItems.get(2)+">");
                }
                else {
                    event = recordItems.get(2);
                    if(event.equals("?")){
                        event = null;
                    }
                }
                if (null == f.findState(fromState) || null == f.findState(toState)) {
                    throw new FsaFormatException(lineno, "From/To Sates Not Found");
                }

                f.newTransition(f.findState(fromState), f.findState(toState), event);
            }

            //Store initial states
            if (recordItems.get(0).equalsIgnoreCase("INITIAL")) {

                if (!IsJavaIdentifier(recordItems.get(1))) {
                    throw new FsaFormatException(lineno,"Invalid Initial state");
                }

                if (null == f.findState(recordItems.get(1))) {
                    throw new FsaFormatException(lineno, "Null initial state");
                }
                initialState = recordItems.get(1);
                f.findState(initialState).setInitial(true);
                // ((StateImpl) f.findState(initialState)).setCurrent(true);

                if (recordItems.get(0).equals("INITIAL")) {
                    inputFormat = dataPattern.upperCase;
                }
            }
            //Store final states
            if (recordItems.get(0).equalsIgnoreCase("FINAL")) {

                if (!IsJavaIdentifier(recordItems.get(1))) {
                    throw new FsaFormatException(lineno, "Invalid final state");
                }

                if (null == f.findState(recordItems.get(1))) {
                    throw new FsaFormatException(lineno, "Null final state");
                }
                finalState = recordItems.get(1);

                f.findState(finalState).setFinal(true);
                if (recordItems.get(0).equals("FINAL")) {
                    inputFormat = dataPattern.upperCase;
                }
            }
        }

    }

    //Write a representation of the Fsa, f, to the OutputStream, os.
    @Override
    public void write(Writer w, Fsa f)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(w);
        String stateKeyWord = "STATE";
        String transitionKeyWord = "TRANSITION";
        String initialKeyWord = "INITIAL";
        String finalKeyWord = "FINAL";
        String LinesOutput = "";

        if (inputFormat == dataPattern.lowerCase) {
            stateKeyWord = stateKeyWord.toLowerCase();
            transitionKeyWord = transitionKeyWord.toLowerCase();
            initialKeyWord = initialKeyWord.toLowerCase();
            finalKeyWord = finalKeyWord.toLowerCase();
        }

        //Output all states
        Set<State> allStates;
        allStates = f.getStates();
        State aState;
        Iterator itr = allStates.iterator();
        while (itr.hasNext()) {
            aState = (State) itr.next();
            LinesOutput = LinesOutput + stateKeyWord + " " + aState.getName() + " "
                    + aState.getXpos() + " " + aState.getYpos() + "\n";

//            Iterator itr_t = aState.transitionsFrom().iterator();
//            Transition aTransition;
//            while (itr_t.hasNext()) {
//                aTransition = (Transition) itr_t.next();
//                LinesOutput = LinesOutput + transitionKeyWord + " " + aTransition.fromState().getName() + " "
//                        + aTransition.eventName() + " " + aTransition.toState().getName() + "\n";
//            }
        }

        //Output transitions
        Iterator itrt = allStates.iterator();
        while (itrt.hasNext()) {
            aState = (State) itrt.next();
            Iterator itr_t = aState.transitionsFrom().iterator();
            Transition aTransition;
            while (itr_t.hasNext()) {
                aTransition = (Transition) itr_t.next();
                LinesOutput = LinesOutput + transitionKeyWord + " " + aTransition.fromState().getName() + " "
                        + aTransition.eventName() + " " + aTransition.toState().getName() + "\n";
                System.out.println("Txn: "+LinesOutput);
            }
        }

//        itr = ((FsaImpl) f).GetTransitions().iterator();
//        Transition aTransition;
//
//        while (itr.hasNext()) {
//            aTransition = (Transition) itr.next();
//            LinesOutput = LinesOutput + transitionKeyWord + " " + aTransition.fromState().getName() + " "
//                    + aTransition.eventName() + " " + aTransition.toState().getName() + "\n";
//        }
        itr = f.getInitialStates().iterator();
        State initialState;
        //Output initial states
        while (itr.hasNext()) {
            initialState = (State) itr.next();
            LinesOutput = LinesOutput + initialKeyWord + " " + initialState.getName() + "\n";
        }

        itr = f.getFinalStates().iterator();
        State finalState;
        //Output initial states
        while (itr.hasNext()) {
            finalState = (State) itr.next();
            LinesOutput = LinesOutput + finalKeyWord + " " + finalState.getName() + "\n";
        }
        //      System.out.println("Line: " + LinesOutput);
        writer.write(LinesOutput);
        writer.flush();
    }

}
