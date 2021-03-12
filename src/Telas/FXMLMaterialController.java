package Telas;

import Controladoras.CtrCategoria;
import Controladoras.CtrMaterial;
import Controladoras.CtrTipoMaterial;
import JDBC.Banco;
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

public class FXMLMaterialController implements Initializable
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
    private TableView<Object> table_material;
    @FXML
    private TableColumn<Object, Integer> c_codigo;
    @FXML
    private TableColumn<Object, String> c_nome;
    @FXML
    private TableColumn<Object, Double> c_valor;
    @FXML
    private TableColumn<Object, Integer> c_estoque;
    @FXML
    private TableColumn<Object, Integer> c_estoque_min;
    @FXML
    private TableColumn<Object, Object> c_tipo;
    @FXML
    private TableColumn<Object, String> c_desc;
    @FXML
    private JFXTextField tb_codigo;
    @FXML
    private JFXComboBox<Object> cb_categoria;
    @FXML
    private JFXComboBox<Object> cb_tipo;
    @FXML
    private JFXTextField tb_nome;
    @FXML
    private JFXTextField tb_desc;
    @FXML
    private JFXTextField tb_valor;
    @FXML
    private JFXTextField tb_estoque_min;
    @FXML
    private JFXTextField tb_estoque;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        c_codigo.setCellValueFactory(new PropertyValueFactory<Object, Integer>("nome"));
        c_nome.setCellValueFactory(new PropertyValueFactory<Object, String>("nome"));
        c_valor.setCellValueFactory(new PropertyValueFactory<Object, Double>("valor"));
        c_estoque.setCellValueFactory(new PropertyValueFactory<Object, Integer>("estoque"));
        c_estoque_min.setCellValueFactory(new PropertyValueFactory<Object, Integer>("estoque_min"));
        c_tipo.setCellValueFactory(new PropertyValueFactory<Object, Object>("tipo"));
        c_desc.setCellValueFactory(new PropertyValueFactory<Object, String>("desc"));
        
        cb_categoria.setItems(CtrCategoria.instancia().searchAll());
        cb_categoria.getSelectionModel().select(0);
        ChangeCategoria(null);
        
        cb_filtro.getItems().add("Nome");
        cb_filtro.getItems().add("Categoria");
        cb_filtro.getItems().add("Tipo");
        
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
        tb_desc.setDisable(flag);
        tb_valor.setDisable(flag);
        tb_estoque.setDisable(flag);
        tb_estoque_min.setDisable(flag);
        
        cb_categoria.setDisable(flag);
        cb_tipo.setDisable(flag);
    }
    
    private void disableBusca(boolean flag)
    {
        cb_filtro.setDisable(flag);
        tb_buscar.setDisable(flag);
        btn_buscar.setDisable(flag);
        table_material.setDisable(flag);
    }
    
    private void clearCampos()
    {
        tb_codigo.clear();
        tb_nome.clear();
        tb_desc.clear();
        tb_valor.clear();
        tb_estoque.clear();
        tb_estoque_min.clear();
        
        cb_categoria.getSelectionModel().select(0);
        ChangeCategoria(null);
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
        else if(tb_valor.getText().isEmpty())
        {
            flag = false;
            str = "Valor Obrigatorio";
        }
        else if(tb_estoque.getText().isEmpty())
        {
            flag = false;
            str = "Estoque Obrigatorio";
        }
        else if(tb_estoque_min.getText().isEmpty())
        {
            flag = false;
            str = "Estoque Minimo Obrigatorio";
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
                String desc = tb_desc.getText();
                double valor = Double.valueOf(tb_valor.getText());
                int estoque = Integer.valueOf(tb_estoque.getText());
                int estoque_min = Integer.valueOf(tb_estoque_min.getText());
                Object tipo = cb_tipo.getSelectionModel().getSelectedItem();
                
                if(CtrMaterial.instancia().insert(nome, desc, valor, estoque, estoque_min, tipo))
                    new Alert(Alert.AlertType.INFORMATION, "Material Cadastrado!", ButtonType.OK).show();
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
            String desc = tb_desc.getText();
            double valor = Double.valueOf(tb_valor.getText());
            int estoque = Integer.valueOf(tb_estoque.getText());
            int estoque_min = Integer.valueOf(tb_estoque_min.getText());
            Object tipo = cb_tipo.getSelectionModel().getSelectedItem();

            if(CtrMaterial.instancia().update(codigo, nome, desc, valor, estoque, estoque_min, tipo))
                new Alert(Alert.AlertType.INFORMATION, "Material Alterado!", ButtonType.OK).show();
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
        if(new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir Material selecionado ?", ButtonType.YES, ButtonType.NO).showAndWait().get() == ButtonType.YES)
        {
            int codigo = Integer.valueOf(tb_codigo.getText());

            if(CtrMaterial.instancia().delete(codigo))
                new Alert(Alert.AlertType.INFORMATION, "Material Excluido!", ButtonType.OK).show();
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
            list = CtrMaterial.instancia().searchByFilter(tb_buscar.getText(), cb_filtro.getSelectionModel().getSelectedIndex());
        else
            list = CtrMaterial.instancia().searchAll();
        
        table_material.setItems(list);
    }

    @FXML
    private void ChangeCategoria(ActionEvent event)
    {
        Object obj = cb_categoria.getSelectionModel().getSelectedItem();
        cb_tipo.setItems(CtrTipoMaterial.instancia().searchByCategoria(Integer.valueOf(CtrCategoria.instancia().getField(obj, "codigo"))));
        cb_tipo.getSelectionModel().select(0);
    }

    @FXML
    private void ClickTable(MouseEvent event)
    {
        if(table_material.getSelectionModel().getSelectedIndex() >= 0)
        {
            Object obj = table_material.getSelectionModel().getSelectedItem();
            _selected = obj;
            
            String codigo = (String) CtrMaterial.instancia().getField(obj, "codigo");
            String nome = (String) CtrMaterial.instancia().getField(obj, "nome");
            String desc = (String) CtrMaterial.instancia().getField(obj, "desc");
            String valor = (String) CtrMaterial.instancia().getField(obj, "valor");
            String estoque = (String) CtrMaterial.instancia().getField(obj, "estoque");
            String estoque_min = (String) CtrMaterial.instancia().getField(obj, "estoque_min");
            Object tipo = cb_tipo.getSelectionModel().getSelectedItem();
            
            tb_codigo.setText(codigo);
            tb_nome.setText(nome);
            tb_desc.setText(desc);
            tb_valor.setText(valor);
            tb_estoque.setText(estoque);
            tb_estoque_min.setText(estoque_min);
            
            cb_categoria.setValue(CtrMaterial.instancia().getField(obj, "obj_categoria"));
            ChangeCategoria(null);
            cb_tipo.setValue(tipo);
            
            setupEditar();
        }
    }
    
}
