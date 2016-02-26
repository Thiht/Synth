package org.istic.synthlab.ui.plugins.history;

import net.minidev.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Manages a state from ab origin object, memento class from memento pattern
 *
 * @author Stephane Mangin <stephane[dot]mangin[at]freesbee[dot]fr>
 */
public class State {

    private JSONObject content;
    private Origin origin;
    private StateType type;
    private long time;

    public State(Origin origin) {
        this.content = origin.getJson();
        this.origin = origin;
        this.time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public JSONObject getContent() {
        return content;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setType(StateType type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public StateType getType() {
        return type;
    }
}
