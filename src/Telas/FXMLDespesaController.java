package Telas;

import Controladoras.CtrCaixa;
import Controladoras.CtrDespesa;
import Controladoras.CtrTipoDespesa;
import JDBC.Banco;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
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

public class FXMLDespesaController implements Initializable
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
    private Button btn_buscar;
    @FXML
    private TableView<Object> table_despesa;
    @FXML
    private TableColumn<Object, Integer> c_codigo;
    @FXML
    private TableColumn<Object, Object> c_tipo;
    @FXML
    private TableColumn<Object, Double> c_valor;
    @FXML
    private TableColumn<Object, Date> c_data;
    @FXML
    private TableColumn<Object, Object> c_usuario;
    @FXML
    private JFXComboBox<Object> cb_tipo;
    @FXML
    private JFXTextField tb_valor;
    @FXML
    private JFXDatePicker dp_data;
    @FXML
    private TextField tb_buscar;
    @FXML
    private JFXTextField tb_obs;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        c_codigo.setCellValueFactory(new PropertyValueFactory<Object, Integer>("codigo"));
        c_tipo.setCellValueFactory(new PropertyValueFactory<Object, Object>("tipo"));
        c_valor.setCellValueFactory(new PropertyValueFactory<Object, Double>("valor"));
        c_data.setCellValueFactory(new PropertyValueFactory<Object, Date>("data"));
        c_usuario.setCellValueFactory(new PropertyValueFactory<Object, Object>("user"));
        
        cb_filtro.getItems().add("Hoje");
        cb_filtro.getItems().add("Semana");
        cb_filtro.getItems().add("Mes");
        cb_filtro.getSelectionModel().select(0);
        
        btn_editar.setDisable(true);
        
        cb_tipo.setItems(CtrTipoDespesa.instancia().searchAll());
        cb_tipo.getSelectionModel().select(0);

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
        btn_excluir.setDisable(false);
        btn_cancelar.setText("Cancelar");
        
        disableCampos(false);
        disableBusca(true);
    }
    
    private void setupCancela()
    {
        btn_novo.setDisable(false);
        btn_novo.setText("Novo");
        btn_excluir.setDisable(true);
        btn_cancelar.setText("Fechar");
        
        clearCampos();
        
        disableCampos(true);
        disableBusca(false);
    }
    
    private void disableCampos(boolean flag)
    {
        cb_tipo.setDisable(flag);
        tb_valor.setDisable(flag);
        dp_data.setDisable(flag);
        tb_obs.setDisable(flag);
    }
    
    private void disableBusca(boolean flag)
    {
        cb_filtro.setDisable(flag);
        tb_buscar.setDisable(flag);
        btn_buscar.setDisable(flag);
        table_despesa.setDisable(flag);
    }
    
    private void clearCampos()
    {
        cb_tipo.getSelectionModel().selectFirst();
        tb_valor.clear();
        dp_data.setValue(LocalDate.now());
        tb_obs.clear();
    }
    
    private boolean validaCampos()
    {
        boolean flag = true;
        String str = "";
        
        if(tb_valor.getText().isEmpty())
        {
            flag = false;
            str = "Valor Obrigatorio";
        }
        else if(dp_data.getValue() == null)
        {
            flag = false;
            str = "Data Obrigatorio";
        }
        
        if(!flag)
            new Alert(Alert.AlertType.ERROR, str, ButtonType.OK).show();
            
        return flag;
    }
    
    @FXML
    private void ClickNovo(ActionEvent event)
    {
        Object cx = CtrCaixa.instancia().searchByToday();
        
        if(cx != null)
        {
            if(btn_novo.getText().compareTo("Novo") != 0)
            {
                if(validaCampos())
                {
                    Object tipo = cb_tipo.getValue();
                    double valor = Double.valueOf(tb_valor.getText());
                    String obs = tb_obs.getText();

                    if(CtrDespesa.instancia().insert(tipo, valor, obs))
                        new Alert(Alert.AlertType.INFORMATION, "Despesa Registrada!", ButtonType.OK).show();
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
        else
            new Alert(Alert.AlertType.ERROR, "Caixa Fehcado.", ButtonType.OK).show();
    }

    @FXML
    private void ClickEditar(ActionEvent event)
    {
        
    }

    @FXML
    private void ClickExcluir(ActionEvent event)
    {
        if(new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir Despesa selecionado ?", ButtonType.YES, ButtonType.NO).showAndWait().get() == ButtonType.YES)
        {
            int codigo = (int)(CtrDespesa.instancia().getField(_selected, "codigo"));
            Object caixa = CtrDespesa.instancia().getField(_selected, "caixa");
            
            if(CtrDespesa.instancia().delete(codigo, caixa))
                new Alert(Alert.AlertType.INFORMATION, "Despesa Excluida!", ButtonType.OK).show();
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
        Object cx = CtrCaixa.instancia().searchByToday();
        
        if(cx != null)
            table_despesa.setItems(CtrDespesa.instancia().searchByCaixa(cx));
        else
            new Alert(Alert.AlertType.ERROR, "Caixa Fehcado.", ButtonType.OK).show();
    }

    @FXML
    private void ClickTable(MouseEvent event)
    {
        if(table_despesa.getSelectionModel().getSelectedIndex() >= 0)
        {
            _selected = table_despesa.getSelectionModel().getSelectedItem();
            
            cb_tipo.setValue(CtrDespesa.instancia().getField(_selected, "tipo"));
            tb_valor.setText((String)CtrDespesa.instancia().getField(_selected, "valor"));
            dp_data.setValue(((Date)CtrDespesa.instancia().getField(_selected, "data")).toLocalDate());
            
            setupEditar();
        }
    }
    
}
