import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class StateImpl implements State {

    private int x;
    private int y;
    private int size = 100;
    private String state_name;
    private boolean initial_state;
    private boolean final_state;
    private boolean current_state;
    private HashSet<Transition> transitionsFromSet;
    private HashSet<Transition> transitionsToSet;
    private Set<StateIcon> listeners;

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

    public StateImpl(String state_name, int x, int y) throws IllegalArgumentException {
        if (!IsJavaIdentifier(state_name)) {
            throw new IllegalArgumentException("State Name Not Valid");
        }
        this.state_name = state_name;
        this.x = x;
        this.y = y;
        transitionsFromSet = new HashSet<>();
        transitionsToSet = new HashSet<>();
        listeners = new HashSet<StateIcon>();
    }

    // Add a new transition which is From this state
    public void AddTransitionFrom(Transition from) {
        transitionsFromSet.add(from);
        for (Iterator<StateIcon> it = listeners.iterator(); it.hasNext();) {
            StateIcon sl = it.next();
            sl.StateHasChanged();
        }
    }

    // Delete a existing transition in the fromSet
    public void DeleteTransitionFrom(Transition from) {
        transitionsFromSet.remove(from);
    }

    //Delete a existing transition in the toSet
    public void DeleteTransitionTo(Transition to) {
        transitionsToSet.remove(to);
    }

    // Add a new transition which is To this state
    public void AddTransitionTo(Transition to) {
        transitionsToSet.add(to);
        for (Iterator<StateIcon> it = listeners.iterator(); it.hasNext();) {
            StateIcon sl = it.next();
            sl.StateHasChanged();
        }
    }

    //Add a listener to this state
    @Override
    public void addListener(StateListener sl) {
        if (!listeners.contains((StateIcon) sl)) {
            StateIcon si = (StateIcon) sl;
            si.setPosition(x, y);
            si.setState(this);
            listeners.add(si);
            size = si.getIconSize();
        }
    }

    //Add a listener to this state
    @Override
    public void removeListener(StateListener sl) {
        listeners.remove((StateIcon) sl);
    }

    public Set<StateIcon> getListeners() {
        return listeners;
    }

    //Return a set containing all transitions FROM this state
    @Override
    public Set<Transition> transitionsFrom() {
        return transitionsFromSet;
    }

    //Return a set containing all transitions TO this state
    @Override
    public Set<Transition> transitionsTo() {
        return transitionsToSet;
    }

    //Move the position of this state
    //by (dx,dy) from its current position
    @Override
    public void moveBy(int dx, int dy) {
        this.x += dx;
        this.y += dy;

        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }

        for (Iterator<StateIcon> it = listeners.iterator(); it.hasNext();) {
            StateIcon sl = it.next();
            if (x + sl.getIconSize() > sl.getMaxX()) {
                x = sl.getMaxX() - sl.getIconSize();
            }

            if (y + sl.getIconSize() > sl.getMaxY()) {
                y = sl.getMaxY() - sl.getIconSize() + 5;
            }
        }

        for (Iterator<StateIcon> it = listeners.iterator(); it.hasNext();) {
            StateIcon sl = it.next();
            sl.StateHasChanged();
        }
    }

    //Return a string containing information about this state
    //in the form (without the quotes, of course!) :
    //"stateName(xPos,yPos)jk"
    //where j is 1/0 if this state is/is-not an initial state
    //where k is 1/0 if this state is/is-not a final state
    @Override
    public String toString() {
        return this.state_name + "(" + this.x + "," + this.y + ")" + (this.initial_state ? 1 : 0) + (this.final_state ? 1 : 0);
    }

    //Returnthe name of this state
    @Override
    public String getName() {
        return this.state_name;
    }

    //Return the X position of this state
    @Override
    public int getXpos() {
        return this.x;
    }

    //Return the Y position of this state
    @Override
    public int getYpos() {
        return this.y;
    }

    //Set/clear this state as an initial state
    @Override
    public void setInitial(boolean b) {
        this.initial_state = b;
        for (Iterator<StateIcon> it = listeners.iterator(); it.hasNext();) {
            StateIcon sl = it.next();
            sl.StateHasChanged();
        }
    }

    //Indicate if this is an initial state
    @Override
    public boolean isInitial() {
        return this.initial_state;
    }

    //Set/clear this state as a final state
    @Override
    public void setFinal(boolean b) {
        this.final_state = b;
        for (Iterator<StateIcon> it = listeners.iterator(); it.hasNext();) {
            StateIcon sl = it.next();
            sl.StateHasChanged();
        }

    }

    //Indicate if this is a final state
    @Override
    public boolean isFinal() {
        return this.final_state;
    }

    //Set/clear this state as a current state
    public void setCurrent(boolean b) {
        this.current_state = b;
        for (Iterator<StateIcon> it = listeners.iterator(); it.hasNext();) {
            StateIcon sl = it.next();
            sl.StateHasChanged();
        }
    }

    @Override
    public boolean isCurrent() {
        return this.current_state;
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
