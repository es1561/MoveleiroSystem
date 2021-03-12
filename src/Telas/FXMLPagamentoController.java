package Telas;

import Controladoras.CtrAquisicao;
import Controladoras.CtrPagamento;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import moveleirosystem.FXMLDocumentController;

public class FXMLPagamentoController implements Initializable
{
    private Object _selected;
    
    @FXML
    private Button btn_pagar;
    @FXML
    private Button btn_cancela;
    @FXML
    private ComboBox<String> cb_filtro;
    @FXML
    private TextField tb_filtro;
    @FXML
    private Button btn_buscar;
    @FXML
    private TableView<Object> table_pagamento;
    @FXML
    private TableColumn<Object, Integer> c_codigo;
    @FXML
    private TableColumn<Object, Integer> c_parcela;
    @FXML
    private TableColumn<Object, Double> c_valor;
    @FXML
    private TableColumn<Object, Date> c_vencimento;
    @FXML
    private TableColumn<Object, Object> c_aquisicao;
    @FXML
    private TableColumn<Object, Object> c_caixa;
    @FXML
    private JFXTextField tb_parcela;
    @FXML
    private JFXTextField tb_valor;
    @FXML
    private JFXTextField tb_vencimento;
    @FXML
    private JFXTextField tb_aquisicao;
    @FXML
    private JFXTextField tb_caixa;
    @FXML
    private TextArea ta_aquisicao;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        c_codigo.setCellValueFactory(new PropertyValueFactory<Object, Integer>("codigo"));
        c_aquisicao.setCellValueFactory(new PropertyValueFactory<Object, Object>("aquisicao"));
        c_parcela.setCellValueFactory(new PropertyValueFactory<Object, Integer>("parcela"));
        c_valor.setCellValueFactory(new PropertyValueFactory<Object, Double>("valor"));
        c_vencimento.setCellValueFactory(new PropertyValueFactory<Object, Date>("dt_venc"));
        c_caixa.setCellValueFactory(new PropertyValueFactory<Object, Object>("caixa"));
        
        tb_aquisicao.setDisable(true);
        tb_caixa.setDisable(true);
        tb_valor.setDisable(true);
        tb_parcela.setDisable(true);
        tb_vencimento.setDisable(true);
        ta_aquisicao.setDisable(true);
        
        setupCancela();
    }    
    
    private void setupCancela()
    {
        btn_pagar.setDisable(true);
        btn_cancela.setText("Fechar");
        clearCampos();
    }

    private void clearCampos()
    {
        tb_aquisicao.clear();
        tb_caixa.clear();
        tb_valor.clear();
        tb_parcela.clear();
        tb_vencimento.clear();
    }
    
    @FXML
    private void ClickPagar(ActionEvent event)
    {
        int codigo = (int)CtrPagamento.instancia().getField(_selected, "codigo");
        
        CtrPagamento.instancia().pagar(codigo);
        clearCampos();
        setupCancela();
    }

    @FXML
    private void ClickCancela(ActionEvent event)
    {
        if(btn_cancela.getText().compareTo("Cancelar") != 0)
            FXMLDocumentController.PANE.getChildren().clear();
        else
            setupCancela();
    }

    @FXML
    private void ClickBuscar(ActionEvent event)
    {
        table_pagamento.setItems(CtrPagamento.instancia().searchAllOpen());
    }

    @FXML
    private void ClickTable(MouseEvent event)
    {
        if(table_pagamento.getSelectionModel().getSelectedIndex() >= 0)
        {
            _selected = table_pagamento.getSelectionModel().getSelectedItem();
            Object aqui = CtrPagamento.instancia().getField(_selected, "aquisicao");
            
            
            tb_parcela.setText(String.valueOf((int)CtrPagamento.instancia().getField(_selected, "parcela")));
            tb_valor.setText(String.valueOf((double)CtrPagamento.instancia().getField(_selected, "valor")));
            tb_vencimento.setText(((Date)CtrPagamento.instancia().getField(_selected, "vencimento")).toString());
            ta_aquisicao.setText((String)CtrAquisicao.instancia().getField(aqui, "text"));
            
            btn_pagar.setDisable(false);
        }
    }
}
