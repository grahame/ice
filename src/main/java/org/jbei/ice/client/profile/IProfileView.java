package org.jbei.ice.client.profile;

import org.jbei.ice.client.collection.table.CollectionDataTable;
import org.jbei.ice.client.login.RegistrationDetails;
import org.jbei.ice.client.profile.widget.UserOption;
import org.jbei.ice.shared.dto.AccountInfo;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface IProfileView {

    Widget asWidget();

    SingleSelectionModel<UserOption> getUserSelectionModel();

    void addEditProfileLinkHandler(ClickHandler editProfileHandler);

    void addChangePasswordLinkHandler(ClickHandler changePasswordHandler);

    void setContents(AccountInfo info);

    void changePasswordPanel(AccountInfo currentInfo, ClickHandler submitHandler, ClickHandler cancelHandler);

    RegistrationDetails getUpdatedDetails();

    String getUpdatedPassword();

    void editProfile(AccountInfo currentInfo, ClickHandler submitHandler, ClickHandler cancelHandler);

    void setEntryContent(CollectionDataTable collectionsDataTable);
}
