package org.jbei.ice.client.entry.add;

import org.jbei.ice.client.common.AbstractLayout;
import org.jbei.ice.client.common.FeedbackPanel;
import org.jbei.ice.client.entry.add.form.EntryCreateWidget;
import org.jbei.ice.client.entry.add.menu.EntryAddMenu;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EntryAddView extends AbstractLayout implements IEntryAddView {

    private Label contentHeader;
    private EntryAddMenu menu;
    private EntryCreateWidget currentForm;
    private VerticalPanel subContent;
    private FlexTable mainContent;

    public EntryAddView() {
        super();
    }

    @Override
    protected Widget createContents() {
        FlexTable contentTable = new FlexTable();
        contentTable.setWidth("100%");
        contentTable.setCellPadding(0);
        contentTable.setCellSpacing(0);
        contentTable.setWidget(0, 0, createMenu());
        contentTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasAlignment.ALIGN_TOP);

        // TODO : middle sliver goes here
        contentTable.setHTML(0, 1, "&nbsp;");

        contentTable.setWidget(0, 2, createMainContent());
        contentTable.getCellFormatter().setWidth(0, 2, "100%");
        contentTable.getFlexCellFormatter().setVerticalAlignment(0, 2, HasAlignment.ALIGN_TOP);
        return contentTable;
    }

    protected Widget createMenu() {
        menu = new EntryAddMenu();
        return menu;
    }

    protected Widget createMainContent() {
        subContent = new VerticalPanel();
        subContent.setWidth("100%");
        contentHeader = new Label("Add New Entry");

        mainContent = new FlexTable(); // wrapper
        mainContent.setCellPadding(3);
        mainContent.setWidth("100%");
        mainContent.setCellSpacing(0);
        mainContent.addStyleName("add_new_entry_main_content_wrapper");
        mainContent.setWidget(0, 0, contentHeader);
        mainContent.getCellFormatter().setStyleName(0, 0, "add_new_entry_main_content_header");

        // sub content
        subContent.add(new HTML("<p>Please select the type of entry you wish to add. "
                + "<p>Fields indicated by <span class=\"required\">*</span> are required. "
                + "Other instructions here. Lorem ipsum."));
        mainContent.setWidget(1, 0, subContent);
        mainContent.getFlexCellFormatter().setStyleName(1, 0, "add_new_entry_sub_content");
        mainContent.getFlexCellFormatter().setColSpan(1, 0, 2);

        return mainContent;
    }

    @Override
    public void setCurrentForm(EntryCreateWidget form, String title) {
        this.currentForm = form;
        subContent.clear();
        subContent.add(this.currentForm);
        contentHeader.setText(title);
    }

    @Override
    public void setFeedbackPanel(FeedbackPanel panel) {
        mainContent.setWidget(0, 1, panel);
        mainContent.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasAlignment.ALIGN_CENTER);
    }

    @Override
    public EntryCreateWidget getCurrentForm() {
        return this.currentForm;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public EntryAddMenu getMenu() {
        return menu;
    }
}
