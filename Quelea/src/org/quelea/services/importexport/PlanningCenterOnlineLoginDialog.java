/* 
 * This file is part of Quelea, free projection software for churches.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.quelea.services.importexport;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.quelea.services.languages.LabelGrabber;

/**
 *
 * @author Bronson
 */


public class PlanningCenterOnlineLoginDialog extends Stage {

    private boolean isLoggedIn = false;
    private final PlanningCenterOnlineImportDialog importDialog;
    private final PlanningCenterOnlineParser parser;
        
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    public PlanningCenterOnlineLoginDialog() {
        importDialog = null;
        parser = null;
    }
    
    public PlanningCenterOnlineLoginDialog(PlanningCenterOnlineImportDialog importDlg, PlanningCenterOnlineParser parse) {
        importDialog = importDlg;
        parser = parse;
        
        initModality(Modality.APPLICATION_MODAL);
        setTitle(LabelGrabber.INSTANCE.getLabel("pco.login.import.heading"));

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            Parent root = loader.load(getClass().getResourceAsStream("PlanningCenterOnlineLoginDialog.fxml"));
            setScene(new Scene(root));
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
    
    @FXML private void onLoginAction(ActionEvent event) {
        if (parser.login(emailField.getText(), passwordField.getText())) {    
            isLoggedIn = true;
            hide();
            importDialog.onLogin();
        }
    }
    
    @FXML private void onCancelAction(ActionEvent event) {
        hide();
    }
        
    public void start() {
        if (!isLoggedIn)
        {
            show();
        }
        else
        {
            // already logged in from previously
            importDialog.onLogin();
        }
    }
}
