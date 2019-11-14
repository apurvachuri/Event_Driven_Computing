import java.util.HashSet;
import java.util.Set;
import java.util.Hashtable;
import java.util.Iterator;

class FsaImpl implements Fsa, FsaSim {

    public Hashtable<String, State> statesSet;
    public HashSet<Transition> transitionsSet;
    public Set<FsaListener> fsaListeners;
    public Set<State> currentStates;

    public FsaImpl() {
        statesSet = new Hashtable<>();
        transitionsSet = new HashSet<>();
        fsaListeners = new HashSet<>();
        currentStates = getInitialStates();
    }

    //Create a new State and add it to this FSA
    //Returns the new state
    //Throws IllegalArgumentException if:
    //the name is not valid or is the same as that
    //of an existing state
    @Override
    public State newState(String name, int x, int y) throws IllegalArgumentException {

        if (!IsJavaIdentifier(name)) {
            throw new IllegalArgumentException();
        }

        if (statesSet.containsKey(name)) {
            throw new IllegalArgumentException();
        }

        State newState = new StateImpl(name, x, y);
        statesSet.put(name, newState);

        newState.addListener(new StateIcon(newState, FsaEditor.width, FsaEditor.height));

        for (Iterator<FsaListener> it = fsaListeners.iterator(); it.hasNext();) {
            FsaPanel fl = (FsaPanel) it.next();
            fl.statesChanged();
        }
        return newState;
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

    public HashSet<Transition> GetTransitions() {
        return transitionsSet;
    }

    //Remove a state from the FSA
    //If the state does not exist, returns without error
    @Override
    public void removeState(State s) {
        if (s == null) {
            return;
        }

        String state_name = s.getName();
        if (!statesSet.containsKey(state_name)) {
            return;
        }
        Set<Transition> fromTrans = s.transitionsFrom();
        Set<Transition> toTrans = s.transitionsTo();
        System.out.println("State Found");
        //Remove the state
        statesSet.remove(state_name);
        System.out.println("State removed");
//        //Remove the corresponding transition(s)
//        Iterator itr = transitionsSet.iterator();
//        Transition aTransition = null;
//        System.out.println("Removing Txn");
//        while (itr.hasNext()) {
//            System.out.println("While Removing Txn");
//            aTransition = (Transition) itr.next();
//            if ((((StateImpl)aTransition.fromState()).getName())==state_name || state_name==(((StateImpl)aTransition.toState()).getName())) {
//                System.out.println("Removing Transaction: fromState "+aTransition.fromState().getName()+" to state: "+aTransition.toState().getName());
//                removeTransition(aTransition);
//            }
//        }

        for (Iterator<Transition> it = toTrans.iterator(); it.hasNext();) {
            Transition t = it.next();
            System.out.println("Removing To Transition: " + t.toString());
            removeTransition(t);
        }

        for (Iterator<Transition> it = fromTrans.iterator(); it.hasNext();) {
            Transition t = it.next();
            System.out.println("Removing To Transition: " + t.toString());
            removeTransition(t);
        }

        System.out.println("Removed Txn");
        for (Iterator<FsaListener> it = fsaListeners.iterator(); it.hasNext();) {
            FsaListener fl = it.next();
            fl.statesChanged();
        }
    }

    //Find and return the State with the given name
    //If no state exists with given name, return NULL
    @Override
    public State findState(String stateName) {
        if (!IsJavaIdentifier(stateName)) {
            return null;
        }
        if (statesSet.containsKey(stateName)) {
            return statesSet.get(stateName);
        }
        return null;
    }

    //Return a set containing all the states in this Fsa
    @Override
    public Set<State> getStates() {
        Set<State> allStates = new HashSet<>();
        Set<String> allStatesName = statesSet.keySet();
        Iterator<String> itr = allStatesName.iterator();

        //Add all existing states into a set
        while (itr.hasNext()) {
            allStates.add(statesSet.get(itr.next()));
        }

        return allStates;
    }

    //Create a new Transition and add it to this FSA
    //Returns the new transition.
    //eventName==null specifies an epsilon-transition
    //Throws IllegalArgumentException if:
    //  The fromState or toState does not exist or
    //  The eventName is invalid or
    //  An identical transition already exists
    @Override
    public Transition newTransition(State fromState, State toState, String eventName)
            throws IllegalArgumentException {

        //Check the existance of fromState & toState
        if (fromState == null || toState == null) {
            throw new IllegalArgumentException("Form/To State is null");
        }

        String fromStateName = fromState.getName();
        String toStateName = toState.getName();

        if (!statesSet.containsKey(fromStateName) || !statesSet.containsKey(toStateName)) {
            throw new IllegalArgumentException("From/To Sates Not Found");
        }

        Set<Transition> st = findTransition(fromState, toState);
        System.out.println(st);
        if (!st.isEmpty()) {
            for (Transition t : st) {
                if (t.eventName() == null && eventName == null) {
                    throw new IllegalArgumentException("Transition exisits");
                }
                if (t.eventName().equals(eventName)) {
                    throw new IllegalArgumentException("Transition exisits");
                }
            }
        }
        //Check the vadility of the event name
        if (!IsValidEventName(eventName)) {
            throw new IllegalArgumentException("Event Name Not Valid");
        }
        int count = 0;
        for (Iterator<Transition> it = transitionsSet.iterator(); it.hasNext();) {
            Transition tt = it.next();
            if (tt.fromState().getName().equals(fromState.getName()) && tt.toState().getName().equals(toState.getName())) {
                count++;
            }
        }
        Transition newTransition = new TransitionImpl(fromState, toState, eventName);
        newTransition.addListener(new TransitionIcon(newTransition, count));
        ((StateImpl) fromState).AddTransitionFrom(newTransition);
        ((StateImpl) toState).AddTransitionTo(newTransition);
        transitionsSet.add(newTransition);

        for (Iterator<FsaListener> it = fsaListeners.iterator(); it.hasNext();) {
            FsaListener fl = it.next();
            fl.transitionsChanged();
        }

        return newTransition;

    }

    private boolean IsValidEventName(String eventName) {
        //Epsilon transition event
        if (null == eventName) {
            return true;
        } else if ((eventName.length() == 1) && eventName.equalsIgnoreCase("?")) {
            return true;
        } else if (!eventName.matches("[a-zA-Z]+")) {
            return false;
        } else {
            return true;
        }

        /*
         for (int i = 0; i < eventName.length(); i++) {
         if (!Character.isLetter(eventName.charAt(i))) {
         return false;
         }
         }*/
    }

    //Remove a transition from the FSA
    //If the transition does not exist, returns without error
    @Override
    public void removeTransition(Transition t) {
        if (t == null) {
            return;
        }
        for (Iterator<Transition> it = transitionsSet.iterator(); it.hasNext();) {
            Transition checkedTransition = it.next();
            if (t.equals(checkedTransition)) {
                System.out.println(" Transitions beinf removed " + t.toString());
                it.remove();
            }
        }

        Set<String> keys = statesSet.keySet();

        Iterator itr = keys.iterator();
        String aState = null;
        while (itr.hasNext()) {
            aState = (String) itr.next();

            ((StateImpl) statesSet.get(aState)).DeleteTransitionFrom(t);
            ((StateImpl) statesSet.get(aState)).DeleteTransitionTo(t);

            System.out.println("Txn From:" + statesSet.get(aState).transitionsFrom());
            System.out.println("Txn TO:" + statesSet.get(aState).transitionsTo());
        }

        for (Iterator<FsaListener> it = fsaListeners.iterator(); it.hasNext();) {
            FsaListener fl = it.next();
            fl.transitionsChanged();
        }

    }

    //Find all the transitions between two states
    //Throws IllegalArgumentException if:
    //  The fromState or toState does not exist
    @Override
    public Set<Transition> findTransition(State fromState, State toState) {
        if (fromState == null || toState == null) {
            throw new IllegalArgumentException("New Transition");
        }

        String fromStateName = fromState.getName();
        String toStateName = toState.getName();

        if (!statesSet.containsKey(fromStateName) || !statesSet.containsKey(toStateName)) {
            throw new IllegalArgumentException("New Transition");
        }

        HashSet<Transition> transitionsFound = new HashSet<>();
        for (Transition checkedTransition : transitionsSet) {
            if ((checkedTransition.fromState().getName() == null ? fromStateName == null : checkedTransition.fromState().getName().equals(fromStateName)) && (checkedTransition.toState().getName() == null ? toStateName == null : checkedTransition.toState().getName().equals(toStateName))) {
                transitionsFound.add(checkedTransition);
            }
        }
        return transitionsFound;

    }

    //Return the set of initial states of this Fsa
    @Override
    public Set<State> getInitialStates() {
        HashSet<State> stateFound = new HashSet<>();
        Set<String> keys = statesSet.keySet();

        for (String key : keys) {
            if (statesSet.get(key).isInitial()) {
                stateFound.add(statesSet.get(key));
            }
        }
        return stateFound;
    }

    //Return the set of final states of this Fsa
    @Override
    public Set<State> getFinalStates() {
        HashSet<State> stateFound = new HashSet<>();
        Set<String> keys = statesSet.keySet();

        for (String key : keys) {
            if (statesSet.get(key).isFinal()) {
                stateFound.add(statesSet.get(key));
            }
        }
        return stateFound;
    }

    //Returns a set containing all the current states of this FSA
    @Override
    public Set<State> getCurrentStates() {
        HashSet<State> stateFound = new HashSet<>();
        Set<String> keys = statesSet.keySet();

        for (String key : keys) {
            if (statesSet.get(key).isCurrent()) {
                stateFound.add(statesSet.get(key));
            }
        }
        return stateFound;

    }

    //Return a string describing this Fsa
    //Returns a string that contains (in this order):
    //for each state in the FSA, a line (terminated by \n) containing
    //  STATE followed the toString result for that state
    //for each transition in the FSA, a line (terminated by \n) containing
    //  TRANSITION followed the toString result for that transition
    //for each initial state in the FSA, a line (terminated by \n) containing
    //  INITIAL followed the name of the state
    //for each final state in the FSA, a line (terminated by \n) containing
    //  FINAL followed the name of the state
    //NOTE This output is different to the file format for a .fsa file.
    @Override
    public String toString() {
        String s = "";
        String t = "";
        String i = "";
        String f = "";

        Set<String> keys = statesSet.keySet();
        for (String key : keys) {
            if (statesSet.get(key).isInitial()) {
                i += "INITIAL " + key + "\n";
            }
            if (statesSet.get(key).isFinal()) {
                f += "FINAL " + key + "\n";
            }
            s += "STATE " + statesSet.get(key).toString() + "\n";

        }
        Iterator itr = transitionsSet.iterator();
        Transition aTransition = null;
        while (itr.hasNext()) {
            aTransition = (Transition) itr.next();
            t += "TRANSITION " + aTransition.toString() + "\n";
        }

        return s + t + i + f;
    }

    //Add a listener to this FSA
    @Override
    public void addListener(FsaListener fl) {
        if (!fsaListeners.contains(fl)) {
            fsaListeners.add(fl);
        }
    }

    //Remove a listener from this FSA
    @Override
    public void removeListener(FsaListener fl) {
        fsaListeners.remove(fl);
    }

    //Reset the simulation to its initial state(s)
    @Override
    public void reset() {
        Set<String> keys = statesSet.keySet();
        for (String key : keys) {
            if (statesSet.get(key).isInitial()) {
                ((StateImpl) statesSet.get(key)).setCurrent(true);
            } else {
                ((StateImpl) statesSet.get(key)).setCurrent(false);
            }
        }
    }

    //Take one step in the simulation
    public void step2(String event) {
        Set<Transition> allTransitions = new HashSet<>();
        StateImpl validState;
        TransitionImpl tmpTransition;

        Set<String> keys = statesSet.keySet();
        for (String key : keys) {
            validState = (StateImpl) statesSet.get(key);
            if (validState.isCurrent()) {
                allTransitions = validState.transitionsFrom();
                Iterator itr = allTransitions.iterator();
                while (itr.hasNext()) {
                    tmpTransition = (TransitionImpl) itr.next();
                    System.out.println(tmpTransition);
                    if (tmpTransition.eventName().equals(event) || tmpTransition.eventName().equals("?")) {
                        ((StateImpl) tmpTransition.fromState()).setCurrent(false);
                        //                        System.out.println("From state: " + tmpTransition.fromState());
                        ((StateImpl) (tmpTransition.toState())).setCurrent(true);
                        //                        System.out.println("To state: " + tmpTransition.toState());
                    }
                }
            }
        }
    }

    @Override
    public void step(String event) {

        //compute eclose
        currentStates = ecl();
        //System.out.println(event);
        //iterate through post-eclose current states and execute transition
        Set<State> newCurrent = new HashSet<State>();
        for (Iterator<Transition> it = transitionsSet.iterator(); it.hasNext();) {
            Transition t = it.next();
            if (t.eventName() != null) {
                if (t.eventName().equals(event)) {
                    for (Iterator<State> itt = currentStates.iterator(); itt.hasNext();) {
                        StateImpl s = (StateImpl) itt.next();
                        if (s.getName().equals(t.fromState().getName())) {
                            //System.out.println("  STEPPING");
                            newCurrent.add(t.toState());
                            StateImpl to = (StateImpl) t.toState();
                            s.setCurrent(false);
                            to.setCurrent(true);
                        }
                    }
                }
            } else {
                if (event.equals("?")) {
                    for (Iterator<State> itt = currentStates.iterator(); itt.hasNext();) {
                        StateImpl s = (StateImpl) itt.next();
                        if (s.getName().equals(t.fromState().getName())) {
                            //System.out.println("  STEPPING");
                            newCurrent.add(t.toState());
                            StateImpl to = (StateImpl) t.toState();
                            s.setCurrent(false);
                            to.setCurrent(true);
                        }
                    }
                }
            }
        }

        if (newCurrent.isEmpty()) {
            return;
        } else {
            for (Iterator<State> it = currentStates.iterator(); it.hasNext();) {
                State si = it.next();
                if (!newCurrent.contains(si)) {
                    ((StateImpl) si).setCurrent(false);
                }
            }
        }

        currentStates = newCurrent;

        for (Iterator<FsaListener> it = fsaListeners.iterator(); it.hasNext();) {
            FsaListener fl = it.next();
            fl.otherChanged();
        }
    }
    //Returns true if the simulation has recognised
    //the sequence of events it has been given

    @Override
    public boolean isRecognised() {
        Set<String> keys = statesSet.keySet();
        for (String key : keys) {
            if (statesSet.get(key).isCurrent() && statesSet.get(key).isFinal()) {
                return true;
            }
        }
        return false;
    }

    public Set<State> ecl() {
        Set<State> ecl = new HashSet<State>();
        Set<String> keys = statesSet.keySet();
        for (String key : keys) {
            if (statesSet.get(key).isCurrent()) {
                State s = statesSet.get(key);
                ecl.add(s);
                Set<Transition> fromTrans = s.transitionsFrom();
                for (Iterator<Transition> itt = fromTrans.iterator(); itt.hasNext();) {
                    Transition t = itt.next();

                    if (t.eventName() == null) {
                        ecl.add(t.toState());
                    }
                }
            }
        }

        return ecl;
    }
}
