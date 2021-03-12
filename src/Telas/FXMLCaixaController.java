package Telas;

import Controladoras.CtrCaixa;
import Controladoras.CtrDespesa;
import JDBC.Banco;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import moveleirosystem.FXMLDocumentController;

public class FXMLCaixaController implements Initializable
{
    private Object _selected;
    
    @FXML
    private Button btn_caixa;
    @FXML
    private Button btn_cancelar;
    @FXML
    private JFXTextField tb_caixa;
    @FXML
    private JFXTextField tb_user;
    @FXML
    private JFXTextField tb_abertura;
    @FXML
    private Label lb_saldo;
    @FXML
    private ListView<Object> list_movimento;
    @FXML
    private Button btn_sangria;
    @FXML
    private TextField tb_valor;
    @FXML
    private Button btn_suprimento;
    @FXML
    private TextArea ta_movimento;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        tb_caixa.setDisable(true);
        tb_user.setDisable(true);
        tb_abertura.setDisable(true);
        ta_movimento.setDisable(true);
        
        _selected = CtrCaixa.instancia().searchByToday();
        setupCaixa();
    }    

    private void setupCaixa()
    {
        if(_selected != null)
        {
            btn_caixa.setText("Fechar Caixa");
            tb_caixa.setText(CtrCaixa.instancia().getField(_selected, "codigo"));
            tb_user.setText(CtrCaixa.instancia().getField(_selected, "usuario_a"));
            tb_abertura.setText(CtrCaixa.instancia().getField(_selected, "valor_a"));
            
            
            list_movimento.setDisable(false);
            list_movimento.setItems(CtrCaixa.instancia().getList(_selected));
            lb_saldo.setText(CtrCaixa.instancia().getField(_selected, "saldo"));
            
            btn_sangria.setDisable(false);
            tb_valor.setDisable(false);
            btn_suprimento.setDisable(false);
        }
        else
        {
            btn_caixa.setText("Abrir Caixa");
            
            list_movimento.setDisable(true);
            
            btn_sangria.setDisable(true);
            tb_valor.setDisable(true);
            btn_suprimento.setDisable(true);
        }
    }
    
    private void clearCampos()
    {
        tb_caixa.clear();
        tb_user.clear();
        tb_valor.clear();
        lb_saldo.setText("0.00");
        list_movimento.getItems().clear();
    }
    
    @FXML
    private void ClickCaixa(ActionEvent event)
    {
        if(btn_caixa.getText().compareTo("Abrir Caixa") == 0)
        {
            if(CtrCaixa.instancia().insert(100))
            {
                new Alert(Alert.AlertType.INFORMATION, "Caixa Aberto!", ButtonType.OK).show();
                _selected = CtrCaixa.instancia().searchByToday();
            }
            else
            {
                new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                System.out.println(Banco.getConexao().getMensagemErro());
            }
        }
        else
        {
            if(CtrCaixa.instancia().close(_selected))
            {
                new Alert(Alert.AlertType.INFORMATION, "Caixa Fechado!", ButtonType.OK).show();
                _selected = null;
            }
        }
        
        setupCaixa();
    }

    @FXML
    private void ClickCancelar(ActionEvent event)
    {
        FXMLDocumentController.PANE.getChildren().clear();
    }

    @FXML
    private void ClickLista(MouseEvent event)
    {
        if(list_movimento.getSelectionModel().getSelectedIndex() >= 0)
        {
            Object obj = list_movimento.getSelectionModel().getSelectedItem();
            
            ta_movimento.setText(CtrCaixa.instancia().getItemText(obj));
        }
    }

    @FXML
    private void ClickSangria(ActionEvent event)
    {
        if(!tb_valor.getText().isEmpty())
        {
            if(CtrCaixa.instancia().movimentar(2, Double.valueOf(tb_valor.getText())))
                new Alert(Alert.AlertType.INFORMATION, "Sangria Realizada!", ButtonType.OK).show();
            else
            {
                new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                System.out.println(Banco.getConexao().getMensagemErro());
            }
            
            setupCaixa();
        }
    }

    @FXML
    private void ClickSuprimento(ActionEvent event)
    {
        if(!tb_valor.getText().isEmpty())
        {
            if(CtrCaixa.instancia().movimentar(1, Double.valueOf(tb_valor.getText())))
                new Alert(Alert.AlertType.INFORMATION, "Suprimento Realizada!", ButtonType.OK).show();
            else
            {
                new Alert(Alert.AlertType.ERROR, Banco.getConexao().getMensagemErro(), ButtonType.OK).show();
                System.out.println(Banco.getConexao().getMensagemErro());
            }
            
            setupCaixa();
        }
    }

}
