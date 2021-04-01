/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Telas;

import Controladoras.CtrAcerto;
import Controladoras.CtrMaterial;
import JDBC.Banco;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import moveleirosystem.FXMLDocumentController;

public class FXMLAcertoController implements Initializable
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
    private JFXTextField tb_item;
    @FXML
    private ListView<Object> list_material;
    @FXML
    private Button btn_adicionar;
    @FXML
    private JFXTextField tb_quant;
    @FXML
    private Button btn_mat_remover;
    @FXML
    private Button btn_remover;
    @FXML
    private ListView<Object> list_item;
    @FXML
    private ComboBox<String> cb_filtro;
    @FXML
    private TextField tb_filtro;
    @FXML
    private Button btn_buscar;
    @FXML
    private TableView<Object> table_aquisicao;
    @FXML
    private TableColumn<Object, Integer> c_codigo;
    @FXML
    private TableColumn<Object, Object> c_user;
    @FXML
    private TableColumn<Object, Date> c_data;
    @FXML
    private TableColumn<Object, String> c_fornecedor;
    @FXML
    private JFXTextField tb_obs;
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        c_codigo.setCellValueFactory(new PropertyValueFactory<Object, Integer>("codigo"));
        c_data.setCellValueFactory(new PropertyValueFactory<Object, Date>("data"));
        c_fornecedor.setCellValueFactory(new PropertyValueFactory<Object, String>("obs"));
        c_user.setCellValueFactory(new PropertyValueFactory<Object, Object>("user"));

        list_item.setItems(FXCollections.observableArrayList());

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
    }
    
    private void disableCampos(boolean flag)
    {
        tb_item.setDisable(flag);
        tb_quant.setDisable(true);
        list_material.setDisable(flag);
        btn_adicionar.setDisable(true);
        btn_remover.setDisable(flag);
        list_item.setDisable(flag);
    }
    
    private void disableBusca(boolean flag)
    {
        cb_filtro.setDisable(flag);
        tb_filtro.setDisable(flag);
        btn_buscar.setDisable(flag);
        table_aquisicao.setDisable(flag);
    }
    
    private void clearCampos()
    {
        tb_item.clear();
        tb_quant.clear();
        list_material.setItems(FXCollections.observableArrayList());    
        list_item.setItems(FXCollections.observableArrayList());
        table_aquisicao.getItems().clear();
    }
    
    private boolean validaCampos()
    {
        boolean flag = true;
        String str = "";
        
        if(list_item.getItems().isEmpty())
        {
            flag = false;
            str = "Sem itens na lista.";
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
                String obs = tb_obs.getText();
                
                if(CtrAcerto.instancia().insert(obs, list_item.getItems()))
                {
                    new Alert(Alert.AlertType.INFORMATION, "Acerto Registrado!", ButtonType.OK).show();
                    setupCancela();
                }
                else
                {
                    new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                    System.out.println(Banco.getConexao().getMensagemErro());
                }
            }
        }
        else
            setupNovo();
    }

    @FXML
    private void ClickEditar(ActionEvent event)
    {
        if(validaCampos())
        {
            int codigo = (int)CtrAcerto.instancia().getField(_selected, "codigo");
            String obs = (String)CtrAcerto.instancia().getField(_selected, "obs");

            if(CtrAcerto.instancia().update(codigo, obs, list_item.getItems()))
            {
                new Alert(Alert.AlertType.INFORMATION, "Acerto Alterado!", ButtonType.OK).show();
                setupCancela();
            }
            else
            {
                new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                System.out.println(Banco.getConexao().getMensagemErro());
            }
        }

        
    }

    @FXML
    private void ClickExcluir(ActionEvent event)
    {
        if(new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir Acerto selecionado ?", ButtonType.YES, ButtonType.NO).showAndWait().get() == ButtonType.YES)
        {
            int codigo = (int)CtrAcerto.instancia().getField(_selected, "codigo");

            if(CtrAcerto.instancia().delete(codigo))
                new Alert(Alert.AlertType.INFORMATION, "Acerto Excluido!", ButtonType.OK).show();
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
        table_aquisicao.setItems(CtrAcerto.instancia().searchAll());
    }

    @FXML
    private void ClickTable(MouseEvent event)
    {
        if(table_aquisicao.getSelectionModel().getSelectedIndex() >= 0)
        {
            _selected = table_aquisicao.getSelectionModel().getSelectedItem();

            list_item.setItems(CtrAcerto.instancia().getItens(_selected));
            tb_obs.setText((String)CtrAcerto.instancia().getField(_selected, "obs"));
            
            setupEditar();
        }
    }

    @FXML
    private void ClickAdicionar(ActionEvent event)
    {
        int index = list_material.getSelectionModel().getSelectedIndex();
                
        if(index >= 0)
        {
            int quant = -Integer.valueOf(tb_quant.getText());
            Object obj = CtrAcerto.instancia().makeItem(list_material.getItems().get(index), quant);
            
            if(!CtrAcerto.instancia().find(obj, list_item.getItems()))
            {
                CtrAcerto.instancia().add(obj, list_item.getItems());

                list_item.refresh();

                tb_item.clear();
                tb_quant.clear();
                list_material.setItems(null);
                btn_adicionar.setDisable(false);
            }
            else
                new Alert(Alert.AlertType.ERROR, "Este material já esta na lista.", ButtonType.OK).show();
        }
    }

    @FXML
    private void ClickRemover(ActionEvent event)
    {
        int index = list_item.getSelectionModel().getSelectedIndex();
        
        if(index >= 0)
            list_item.getItems().remove(index);
    }

    @FXML
    private void ClickListMaterial(MouseEvent event)
    {
        if(list_material.getSelectionModel().getSelectedIndex() >= 0)
            tb_quant.setDisable(false);
        else
            tb_quant.setDisable(true);
    }

    @FXML
    private void ChangeItem(KeyEvent event)
    {
        if(!tb_item.getText().isEmpty())
            list_material.setItems(CtrMaterial.instancia().searchByFilter(tb_item.getText(), 0));
    }

    @FXML
    private void ChangeQuantidade(KeyEvent event)
    {
        boolean f_quant = tb_quant.getText().length() == 0;
        
        btn_adicionar.setDisable(f_quant);
    }

    @FXML
    private void ClickListItem(MouseEvent event)
    {
        btn_remover.setDisable(list_item.getSelectionModel().getSelectedIndex() == -1);
    }

    @FXML
    private void ClickMatRemover(ActionEvent event)
    {
        int index = list_material.getSelectionModel().getSelectedIndex();
                
        if(index >= 0)
        {
            int quant = Integer.valueOf(tb_quant.getText());
            Object obj = CtrAcerto.instancia().makeItem(list_material.getItems().get(index), quant);
            
            if(!CtrAcerto.instancia().find(obj, list_item.getItems()))
            {
                CtrAcerto.instancia().add(obj, list_item.getItems());

                list_item.refresh();

                tb_item.clear();
                tb_quant.clear();
                list_material.setItems(null);
                btn_adicionar.setDisable(false);
            }
            else
                new Alert(Alert.AlertType.ERROR, "Este material já esta na lista.", ButtonType.OK).show();
        }
    }
}
