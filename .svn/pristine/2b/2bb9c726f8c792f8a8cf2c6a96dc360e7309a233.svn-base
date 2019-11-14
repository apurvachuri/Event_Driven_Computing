import java.util.HashSet;
import java.util.Set;

class TransitionImpl implements Transition {

    private State fs;
    private State ts;
    private String e;
    private Set<TransitionIcon> listeners;

    private boolean IsValidEventName(String eventName) {
        //Epsilon transition event

        if (null == eventName) {
            return true;
        } else if ((eventName.length() == 1) && eventName.equalsIgnoreCase("?")) {
            return true;
        }

        for (int i = 0; i < eventName.length(); i++) {
            if (!Character.isLetter(eventName.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public TransitionImpl(State from_state, State to_state, String event_name) throws IllegalArgumentException {
        listeners = new HashSet<TransitionIcon>();
        //Check the existance of from_state & to_state
        if (from_state == null || to_state == null) {
            throw new IllegalArgumentException("Form/To State is null");
        }

        //Check the vadility of the event name
        if (!IsValidEventName(event_name)) {
            throw new IllegalArgumentException("Event Name Not Valid");
        }

        this.fs = from_state;
        this.ts = to_state;
        this.e = event_name;

    }

    //Add a listener to this Transition
    @Override
    public void addListener(TransitionListener tl) {
        if (!listeners.contains((TransitionIcon) tl)) {
            TransitionIcon si = (TransitionIcon) tl;
            listeners.add(si);
            si.TransitionHasChanged();
        }
    }

    //Remove a listener tfrom this Transition
    @Override
    public void removeListener(TransitionListener tl) {
        listeners.remove((TransitionIcon) tl);
    }

    public Set<TransitionIcon> getListeners() {
        return listeners;
    }

    //Return the from-state of this transition
    @Override
    public State fromState() {
        return fs;
    }

    //Return the to-state of this transition
    @Override
    public State toState() {
        return ts;
    }

    //Return the name of the event that causes this transition
    @Override
    public String eventName() {
        return e;
    }

    //Return a string containing information about this transition
    //in the form (without quotes, of course!):
    //"from_stateName(eventName)to_stateName"
    @Override
    public String toString() {
        return fs.getName() + "(" + e + ")" + ts.getName();
    }
}
