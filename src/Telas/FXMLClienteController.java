package Telas;

import Controladoras.CtrCliente;
import JDBC.Banco;
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

public class FXMLClienteController implements Initializable
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
    private TextField tb_buscar;
    @FXML
    private Button btn_buscar;
    @FXML
    private TableView<Object> table_cliente;
    @FXML
    private TableColumn<Object, Integer> c_codigo;
    @FXML
    private TableColumn<Object, String> c_nome;
    @FXML
    private TableColumn<Object, String> c_cpf;
    @FXML
    private TableColumn<Object, String> c_fone;
    @FXML
    private TableColumn<Object, String> c_cidade;
    @FXML
    private TableColumn<Object, String> c_endereco;
    @FXML
    private JFXTextField tb_codigo;
    @FXML
    private JFXTextField tb_cpf;
    @FXML
    private JFXTextField tb_fone;
    @FXML
    private JFXTextField tb_nome;
    @FXML
    private JFXTextField tb_email;
    @FXML
    private JFXTextField tb_cep;
    @FXML
    private JFXTextField tb_cidade;
    @FXML
    private JFXTextField tb_endereco;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        c_codigo.setCellValueFactory(new PropertyValueFactory<Object, Integer>("codigo"));
        c_nome.setCellValueFactory(new PropertyValueFactory<Object, String>("nome"));
        c_cpf.setCellValueFactory(new PropertyValueFactory<Object, String>("cpf"));
        c_fone.setCellValueFactory(new PropertyValueFactory<Object, String>("fone"));
        c_cidade.setCellValueFactory(new PropertyValueFactory<Object, String>("cidade"));
        c_endereco.setCellValueFactory(new PropertyValueFactory<Object, String>("endereco"));
        
        cb_filtro.getItems().add("Nome");
        cb_filtro.getItems().add("CPF");
        cb_filtro.getItems().add("Cidade");
        
        cb_filtro.getSelectionModel().select(0);
        
        tb_codigo.setDisable(true);
        
        setupCancela();
    }    

    private void setupNovo()
    {
        btn_novo.setText("Salvar");
        btn_cancelar.setText("Cancelar");
        
        clearCampos();
        
        disableCampos(false);
        disableBusca(true);
    }
    
    private void setupEditar()
    {
        btn_novo.setDisable(true);
        btn_editar.setDisable(false);
        btn_excluir.setDisable(false);
        btn_cancelar.setText("Cancelar");
        
        disableCampos(false);
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
    
    private void disableCampos(boolean flag)
    {
        tb_nome.setDisable(flag);
        tb_cpf.setDisable(flag);
        tb_email.setDisable(flag);
        tb_fone.setDisable(flag);
        tb_cep.setDisable(flag);
        tb_cidade.setDisable(flag);
        tb_endereco.setDisable(flag);
    }
    
    private void disableBusca(boolean flag)
    {
        cb_filtro.setDisable(flag);
        tb_buscar.setDisable(flag);
        btn_buscar.setDisable(flag);
        table_cliente.setDisable(flag);
    }
    
    private void clearCampos()
    {
        tb_codigo.clear();
        tb_nome.clear();
        tb_cpf.clear();
        tb_email.clear();
        tb_fone.clear();
        tb_cep.clear();
        tb_cidade.clear();
        tb_endereco.clear();
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
        else if(tb_cpf.getText().isEmpty())
        {
            flag = false;
            str = "CPF Obrigatorio";
        }
        else if(tb_fone.getText().isEmpty())
        {
            flag = false;
            str = "Fone Obrigatorio";
        }
        else if(tb_cep.getText().isEmpty())
        {
            flag = false;
            str = "CEP Obrigatorio";
        }
        else if(tb_cidade.getText().isEmpty())
        {
            flag = false;
            str = "Cidade Obrigatorio";
        }
        else if(tb_endereco.getText().isEmpty())
        {
            flag = false;
            str = "Endereço Obrigatorio";
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
                String nome = tb_nome.getText();
                String cpf = tb_cpf.getText();
                String fone = tb_fone.getText();
                String email = tb_email.getText();
                String cep = tb_cep.getText();
                String cidade = tb_cidade.getText();
                String endereco = tb_endereco.getText();
                
                if(CtrCliente.instancia().insert(nome, cpf, fone, email, cep, cidade, endereco))
                    new Alert(Alert.AlertType.INFORMATION, "Cliente Cadastrado!", ButtonType.OK).show();
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
            int codigo = Integer.valueOf(tb_codigo.getText());
            String nome = tb_nome.getText();
            String cpf = tb_cpf.getText();
            String fone = tb_fone.getText();
            String email = tb_email.getText();
            String cep = tb_cep.getText();
            String cidade = tb_cidade.getText();
            String endereco = tb_endereco.getText();

            if(CtrCliente.instancia().update(codigo, nome, cpf, fone, email, cep, cidade, endereco))
                new Alert(Alert.AlertType.INFORMATION, "Cliente Alterado!", ButtonType.OK).show();
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
        if(new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir Cliente selecionado ?", ButtonType.YES, ButtonType.NO).showAndWait().get() == ButtonType.YES)
        {
            int codigo = Integer.valueOf(tb_codigo.getText());

            if(CtrCliente.instancia().delete(codigo))
                new Alert(Alert.AlertType.INFORMATION, "Cliente Excluido!", ButtonType.OK).show();
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
        
        if(!tb_buscar.getText().isEmpty())
            list = CtrCliente.instancia().searchByFilter(tb_buscar.getText(), cb_filtro.getSelectionModel().getSelectedIndex());
        else
            list = CtrCliente.instancia().searchAll();
        
        table_cliente.setItems(list);
    }

    @FXML
    private void ClickTable(MouseEvent event)
    {
        if(table_cliente.getSelectionModel().getSelectedIndex() >= 0)
        {
            Object obj = table_cliente.getSelectionModel().getSelectedItem();
            _selected = obj;
            
            String codigo = (String) CtrCliente.instancia().getField(obj, "codigo");
            String nome = (String) CtrCliente.instancia().getField(obj, "nome");
            String cpf = (String) CtrCliente.instancia().getField(obj, "cpf");
            String fone = (String) CtrCliente.instancia().getField(obj, "fone");
            String email = (String) CtrCliente.instancia().getField(obj, "email");
            String cep = (String) CtrCliente.instancia().getField(obj, "cep");
            String cidade = (String) CtrCliente.instancia().getField(obj, "cidade");
            String endereco = (String) CtrCliente.instancia().getField(obj, "endereco");
            
            tb_codigo.setText(codigo);
            tb_nome.setText(nome);
            tb_cpf.setText(cpf);
            tb_fone.setText(fone);
            tb_email.setText(email);
            tb_cep.setText(cep);
            tb_cidade.setText(cidade);
            tb_endereco.setText(endereco);
                        
            setupEditar();
        }
    }
    
}
