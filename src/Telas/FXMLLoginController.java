package Telas;

import Controladoras.CtrUsuario;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import moveleirosystem.FXMLDocumentController;

public class FXMLLoginController implements Initializable
{

    @FXML
    private JFXTextField tb_login;
    @FXML
    private Button btn_entrar;
    @FXML
    private JFXPasswordField tb_senha;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void ClickEntrar(ActionEvent event)
    {
        String login = tb_login.getText();
        String senha = tb_senha.getText();
        Object user = CtrUsuario.instancia().login(login, senha);
        
        if(user != null)
        {
            FXMLDocumentController.USER = user;
            FXMLDocumentController.APP.disableLogin(Integer.valueOf(CtrUsuario.instancia().getField(user, "nivel")));
        }
        else
        {
            tb_senha.clear();
            new Alert(Alert.AlertType.ERROR, "Login e(ou) Senha Invalidos!", ButtonType.OK).show();
        }
    }
    
}
