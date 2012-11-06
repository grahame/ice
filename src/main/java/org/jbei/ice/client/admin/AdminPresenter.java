package org.jbei.ice.client.admin;

import java.util.ArrayList;
import java.util.HashMap;

import org.jbei.ice.client.AbstractPresenter;
import org.jbei.ice.client.AppController;
import org.jbei.ice.client.IceAsyncCallback;
import org.jbei.ice.client.RegistryServiceAsync;
import org.jbei.ice.client.admin.group.GroupPresenter;
import org.jbei.ice.client.admin.setting.SystemSettingsWidget;
import org.jbei.ice.client.admin.transfer.TransferEntryPanel;
import org.jbei.ice.client.admin.user.UserPresenter;
import org.jbei.ice.client.exception.AuthenticationException;
import org.jbei.ice.shared.dto.AccountInfo;
import org.jbei.ice.shared.dto.GroupInfo;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * Presenter for the admin page
 *
 * @author Hector Plahar
 */
public class AdminPresenter extends AbstractPresenter {

    private final AdminView view;
    private final RegistryServiceAsync service;
    private final HandlerManager eventBus;
    private AdminOption currentOption;
    private GroupPresenter groupPresenter;
    private UserPresenter userPresenter;

    public AdminPresenter(RegistryServiceAsync service, HandlerManager eventBus, AdminView view, String optionStr) {
        this.service = service;
        this.view = view;
        this.eventBus = eventBus;

        AdminOption option = AdminOption.urlToOption(optionStr);
        if (option == null)
            option = AdminOption.SETTINGS;

        view.showMenuSelection(option);
        setViewForOption(option);
        setSelectionHandler();
    }

    protected void setSelectionHandler() {
        this.view.getUserSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                setViewForOption(view.getUserSelectionModel().getSelectedObject());
            }
        });
    }

    private void setViewForOption(AdminOption option) {
        if (option == null)
            return;

        currentOption = option;

        switch (option) {
            case SETTINGS:
                retrieveSystemSettings();
                break;

            case GROUPS:
                if (groupPresenter == null)
                    groupPresenter = new GroupPresenter(service, eventBus);
                retrieveGroups();
                break;

            case USERS:
                if (userPresenter == null)
                    userPresenter = new UserPresenter(service, eventBus);
                retrieveUsers();
                break;

            case TRANSFER:
                currentOption = AdminOption.TRANSFER;
                view.show(currentOption, new TransferEntryPanel());
                break;
        }
    }

    // GROUPS
    private void retrieveGroups() {
        new IceAsyncCallback<ArrayList<GroupInfo>>() {

            @Override
            protected void callService(AsyncCallback<ArrayList<GroupInfo>> callback) throws AuthenticationException {
                service.retrieveAllGroups(AppController.sessionId, callback);
            }

            @Override
            public void onSuccess(ArrayList<GroupInfo> result) {
                if (result == null || currentOption != AdminOption.GROUPS)
                    return;

                groupPresenter.setGroups(result);
                view.show(currentOption, groupPresenter.getView().asWidget());
            }
        }.go(eventBus);
    }

    // SYSTEMS
    private void retrieveSystemSettings() {
        new IceAsyncCallback<HashMap<String, String>>() {

            @Override
            protected void callService(AsyncCallback<HashMap<String, String>> callback) throws AuthenticationException {
                service.retrieveSystemSettings(AppController.sessionId, callback);
            }

            @Override
            public void onSuccess(HashMap<String, String> settings) {
                if (settings == null || currentOption != AdminOption.SETTINGS)
                    return;

                SystemSettingsWidget widget = new SystemSettingsWidget();
                widget.setData(settings);
                view.show(currentOption, widget);
            }
        }.go(eventBus);
    }

    private void retrieveUsers() {
        new IceAsyncCallback<ArrayList<AccountInfo>>() {

            @Override
            protected void callService(AsyncCallback<ArrayList<AccountInfo>> callback) throws AuthenticationException {
                service.retrieveAllUserAccounts(AppController.sessionId, callback);
            }

            @Override
            public void onSuccess(ArrayList<AccountInfo> result) {
                if (result == null || currentOption != AdminOption.USERS)
                    return;

                userPresenter.setData(result);
                view.show(currentOption, userPresenter.getView().asWidget());
            }
        }.go(eventBus);
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(this.view.asWidget());
    }
}
