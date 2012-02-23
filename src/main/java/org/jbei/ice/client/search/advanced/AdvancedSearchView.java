package org.jbei.ice.client.search.advanced;

import java.util.ArrayList;

import org.jbei.ice.client.common.table.EntryTablePager;
import org.jbei.ice.client.search.blast.BlastResultsTable;
import org.jbei.ice.shared.dto.SearchFilterInfo;

import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedSearchView extends Composite implements IAdvancedSearchView {

    private FlowPanel filterPanel;
    private AdvancedSearchResultsTable table;
    private BlastResultsTable blastTable;
    private EntryTablePager pager;
    private EntryTablePager blastPager;
    private final FlexTable layout;

    public AdvancedSearchView() {
        layout = new FlexTable();
        layout.setWidth("100%");
        layout.setHeight("100%");
        layout.setCellSpacing(0);
        layout.setCellPadding(0);
        initWidget(layout);

        initComponents();

        // add filters
        CaptionPanel captionPanel = new CaptionPanel("Search Filters");
        captionPanel.setWidth("97%");
        captionPanel.add(filterPanel);
        layout.setWidget(0, 0, captionPanel);

        // add a break between filters and results
        layout.setHTML(1, 0, "&nbsp;");

        // TODO : loading indicator?
    }

    protected void initComponents() {
        filterPanel = new FlowPanel();
        table = new AdvancedSearchResultsTable();
        blastTable = new BlastResultsTable();

        // search pager
        pager = new EntryTablePager();
        pager.setDisplay(table);

        blastPager = new EntryTablePager();
        blastPager.setDisplay(blastTable);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public AdvancedSearchResultsTable getResultsTable() {
        return this.table;
    }

    @Override
    public void setSearchFilters(ArrayList<SearchFilterInfo> filters) {
        filterPanel.clear();
        for (SearchFilterInfo filter : filters) {
            String filterString = filter.getType() + filter.getOperator() + filter.getOperand();
            Label label = new Label(filterString + " ");
            filterPanel.add(label);
        }
    }

    @Override
    public void setSearchVisibility(boolean visible) {
        pager.setVisible(visible);
        table.setVisible(visible);

        if (visible) {
            layout.setWidget(2, 0, table);
            layout.setWidget(3, 0, pager);
        }
    }

    @Override
    public void setBlastVisibility(boolean visible) {
        blastPager.setVisible(visible);
        blastTable.setVisible(visible);

        if (visible) {
            layout.setWidget(2, 0, blastTable);
            layout.setWidget(3, 0, blastPager);
        }
    }

    @Override
    public BlastResultsTable getBlastResultTable() {
        return blastTable;
    }
}
