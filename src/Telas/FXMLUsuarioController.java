package Telas;

import Controladoras.CtrUsuario;
import JDBC.Banco;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import moveleirosystem.FXMLDocumentController;

public class FXMLUsuarioController implements Initializable
{   
    private Object _selected;
    
    @FXML
    private Button btn_novo;
    @FXML
    private Button btn_editar;
    @FXML
    private Button btn_excluir;
    @FXML
    private Button btn_cancelar;
    @FXML
    private ComboBox<String> cb_filtro;
    @FXML
    private TextField tb_filtro;
    @FXML
    private Button btn_buscar;
    @FXML
    private TableView<Object> table_usuario;
    @FXML
    private TableColumn<Object, String> c_nome;
    @FXML
    private TableColumn<Object, Integer> c_nivel;
    @FXML
    private TableColumn<Object, String> c_login;
    @FXML
    private TableColumn<Object, String> c_fone;
    @FXML
    private JFXTextField tb_login;
    @FXML
    private JFXComboBox<String> cb_nivel;
    @FXML
    private JFXTextField tb_senha;
    @FXML
    private JFXTextField tb_nome;
    @FXML
    private JFXTextField tb_fone;
    @FXML
    private JFXCheckBox check_substituir;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        c_nome.setCellValueFactory(new PropertyValueFactory<Object, String>("nome"));
        c_login.setCellValueFactory(new PropertyValueFactory<Object, String>("login"));
        c_nivel.setCellValueFactory(new PropertyValueFactory<Object, Integer>("nivel"));
        c_fone.setCellValueFactory(new PropertyValueFactory<Object, String>("fone"));        
        // TODO
        cb_nivel.getItems().add("Funcionario");
        cb_nivel.getItems().add("Gerente");
        cb_nivel.getItems().add("Administrador");
        
        cb_filtro.getItems().add("Nome");
        cb_filtro.getItems().add("Login");
        cb_filtro.getItems().add("Fone");
        
        cb_nivel.getSelectionModel().select(0);
        cb_filtro.getSelectionModel().select(0);
        
        setupCancela();
    }    

    private void setupNovo()
    {
        btn_novo.setText("Salvar");
        btn_cancelar.setText("Cancelar");
        
        clearCampos();
        
        disableCampos(false);
        check_substituir.setDisable(true);
        check_substituir.setSelected(true);
        disableBusca(true);
    }
    
    private void setupEditar()
    {
        btn_novo.setDisable(true);
        btn_editar.setDisable(false);
        btn_excluir.setDisable(false);
        btn_cancelar.setText("Cancelar");
        
        disableCampos(false);
        tb_login.setDisable(true);
        check_substituir.setDisable(false);
        check_substituir.setSelected(false);
        disableBusca(true);
    }
    
    private void setupCancela()
    {
        btn_novo.setDisable(false);
        btn_novo.setText("Novo");
        btn_editar.setDisable(true);
        btn_excluir.setDisable(true);
        btn_cancelar.setText("Fechar");
        
        clearCampos();
        
        disableCampos(true);
        disableBusca(false);
        
        ClickBuscar(null);
    }
    
    private void clearCampos()
    {
        tb_login.clear();
        tb_senha.clear();
        tb_nome.clear();
        tb_fone.clear();
        
        cb_nivel.getSelectionModel().select(0);
    }
    
    private void disableCampos(boolean flag)
    {
        tb_login.setDisable(flag);
        cb_nivel.setDisable(flag);
        tb_senha.setDisable(flag);
        check_substituir.setDisable(flag);
        tb_nome.setDisable(flag);
        tb_fone.setDisable(flag);
    }
    
    private void disableBusca(boolean flag)
    {
        cb_filtro.setDisable(flag);
        tb_filtro.setDisable(flag);
        btn_buscar.setDisable(flag);
        table_usuario.setDisable(flag);
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
        else if(tb_fone.getText().isEmpty())
        {
            flag = false;
            str = "Fone Obrigatorio";
        }
        
        if(!flag)
            new Alert(Alert.AlertType.ERROR, str, ButtonType.OK).show();
            
        return flag;
    }
    
    @FXML
    private void ClickNovo(ActionEvent event)
    {
        if(btn_novo.getText().compareTo("Novo") != 0)
        {
            if(validaCampos())
            {
                int nivel = cb_nivel.getSelectionModel().getSelectedIndex() + 1;
                String login = tb_login.getText();
                String senha = tb_senha.getText();
                String nome = tb_nome.getText();
                String fone = tb_fone.getText();
                
                if(CtrUsuario.instancia().insert(login, senha, nome, fone, nivel))
                    new Alert(Alert.AlertType.INFORMATION, "Usuario Cadastrado!", ButtonType.OK).show();
                else
                {
                    new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                    System.out.println(Banco.getConexao().getMensagemErro());
                }
            }
            
            setupCancela();
        }
        else
            setupNovo();
    }

    @FXML
    private void ClickEditar(ActionEvent event)
    {
        if(validaCampos())
        {
            int nivel = cb_nivel.getSelectionModel().getSelectedIndex() + 1;
            String login = tb_login.getText();
            String senha = check_substituir.isSelected() ? tb_senha.getText() : CtrUsuario.instancia().getField(_selected, "senha");;
            String nome = tb_nome.getText();
            String fone = tb_fone.getText();

            if(CtrUsuario.instancia().update(login, senha, nome, fone, nivel))
                new Alert(Alert.AlertType.INFORMATION, "Usuario Alterado!", ButtonType.OK).show();
            else
            {
                new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                System.out.println(Banco.getConexao().getMensagemErro());
            }

            setupCancela();
        }
    }

    @FXML
    private void ClickExcluir(ActionEvent event)
    {
        if(new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir usuario selecionado ?", ButtonType.YES, ButtonType.NO).showAndWait().get() == ButtonType.YES)
        {
            String login = tb_login.getText();

            if(CtrUsuario.instancia().delete(login))
                new Alert(Alert.AlertType.INFORMATION, "Usuario Excluido!", ButtonType.OK).show();
            else
            {
                new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                System.out.println(Banco.getConexao().getMensagemErro());
            }

            setupCancela();
        }
    }

    @FXML
    private void ClickCancelar(ActionEvent event)
    {
        if(btn_cancelar.getText().compareTo("Cancelar") != 0)
            FXMLDocumentController.PANE.getChildren().clear();
        else
            setupCancela();
    }

    @FXML
    private void ClickBuscar(ActionEvent event)
    {
        ObservableList<Object> list = null;
        
        if(!tb_filtro.getText().isEmpty())
            list = CtrUsuario.instancia().searchByFilter(tb_filtro.getText(), cb_filtro.getSelectionModel().getSelectedIndex());
        else
            list = CtrUsuario.instancia().searchAll();
        
        table_usuario.setItems(list);
    }

    @FXML
    private void ClickTable(MouseEvent event)
    {
        if(table_usuario.getSelectionModel().getSelectedIndex() >= 0)
        {
            Object obj = table_usuario.getSelectionModel().getSelectedItem();
            _selected = obj;
            
            int nivel = Integer.valueOf(CtrUsuario.instancia().getField(obj, "nivel")) - 1;
            String login = CtrUsuario.instancia().getField(obj, "login");
            String senha = CtrUsuario.instancia().getField(obj, "senha");
            String nome = CtrUsuario.instancia().getField(obj, "nome");
            String fone = CtrUsuario.instancia().getField(obj, "fone");
            
            cb_nivel.getSelectionModel().select(nivel);
            tb_login.setText(login);
            tb_senha.setText(senha);
            tb_nome.setText(nome);
            tb_fone.setText(fone);
            
            setupEditar();
        }
    }
    
}
