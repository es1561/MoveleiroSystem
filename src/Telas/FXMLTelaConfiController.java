package Telas;

import Controladoras.CtrUsuario;
import JDBC.Banco;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import moveleirosystem.FXMLDocumentController;

public class FXMLTelaConfiController implements Initializable
{

    @FXML
    private Button btn_confirma;
    @FXML
    private Button btn_limpa;
    @FXML
    private JFXTextField tb_nome;
    @FXML
    private JFXTextField tb_login;
    @FXML
    private JFXPasswordField tb_senha;
    @FXML
    private JFXPasswordField tb_csenha;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    private boolean validaCampos()
    {
        boolean flag = true;
        String str = "";
        
        if(tb_nome.getText().isEmpty())
        {
            flag = false;
            str = "Nome Obrigatorio";
        }
        else if(tb_login.getText().isEmpty())
        {
            flag = false;
            str = "Login Obrigatorio";
        }
        else if(tb_senha.getText().isEmpty())
        {
            flag = false;
            str = "Senha Obrigatorio";
        }
        else if(tb_csenha.getText().isEmpty() || tb_senha.getText().compareTo(tb_csenha.getText()) != 0)
        {
            flag = false;
            str = "Senhas Incompativeis";
        }
        
        if(!flag)
            new Alert(Alert.AlertType.ERROR, str, ButtonType.OK).show();
            
        return flag;
    }
    
    @FXML
    private void ClickConfirmar(ActionEvent event)
    {
        String login = tb_login.getText();
        String senha = tb_senha.getText();
        String nome = tb_nome.getText();
        
        if(validaCampos())
        {
            if(CtrUsuario.instancia().insert(login, senha, nome, "(00)00000-0000", 3))
            {
                new Alert(Alert.AlertType.INFORMATION, "Admnistrador Cadastrado!", ButtonType.OK).show();
                
                try
                {   
                    Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/Telas/FXMLLogin.fxml"));
                    FXMLDocumentController.PANE.getChildren().clear();
                    FXMLDocumentController.PANE.getChildren().add(newLoadedPane);
                }
                catch(Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
            else
                new Alert(Alert.AlertType.ERROR, "Falha ao cadastar o Admnistrador!\n\n" + Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
        }
    }

    @FXML
    private void ClickLimpar(ActionEvent event)
    {
        tb_nome.clear();
        tb_senha.clear();
        tb_nome.clear();
    }
    
}
