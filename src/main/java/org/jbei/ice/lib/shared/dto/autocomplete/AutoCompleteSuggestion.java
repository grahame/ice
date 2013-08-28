package org.jbei.ice.lib.shared.dto.autocomplete;

import org.jbei.ice.lib.shared.dto.entry.AutoCompleteField;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * @author Hector Plahar
 */
public class AutoCompleteSuggestion implements IsSerializable, SuggestOracle.Suggestion {

    private AutoCompleteField type;
    private String display;

    // required no arg constructor
    public AutoCompleteSuggestion() {
    }

    public AutoCompleteSuggestion(String display) {
        this.display = display;
    }

    @Override
    public String getDisplayString() {
        return display.trim();
    }

    @Override
    public String getReplacementString() {
        return display;
    }
}
