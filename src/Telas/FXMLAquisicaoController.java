package Telas;

import Controladoras.CtrAquisicao;
import Controladoras.CtrCaixa;
import Controladoras.CtrFornecedor;
import Controladoras.CtrMaterial;
import Controladoras.CtrPagamento;
import JDBC.Banco;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
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

public class FXMLAquisicaoController implements Initializable
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
    private TableView<Object> table_aquisicao;
    @FXML
    private JFXComboBox<Object> cb_fornecedor;
    @FXML
    private JFXTextField tb_total;
    @FXML
    private Button btn_adicionar;
    @FXML
    private Button btn_remover;
    @FXML
    private ListView<Object> list_item;
    @FXML
    private TableColumn<Object, Integer> c_codigo;
    @FXML
    private TableColumn<Object, Object> c_fornecedor;
    @FXML
    private TableColumn<Object, Date> c_data;
    @FXML
    private TableColumn<Object, Integer> c_parcela;
    @FXML
    private TableColumn<Object, Double> c_total;
    @FXML
    private TableColumn<Object, Object> c_user;
    @FXML
    private TextField tb_item;
    @FXML
    private JFXTextField tb_valor;
    @FXML
    private ListView<Object> list_material;
    @FXML
    private JFXTextField tb_quant;
    @FXML
    private JFXTextField tb_nota;
    @FXML
    private JFXListView<Object> list_parcela;
    @FXML
    private Button btn_par_novo;
    @FXML
    private Button btn_par_remover;
    @FXML
    private JFXTextField tb_parcela;
    @FXML
    private JFXTextField tb_par_valor;
    @FXML
    private JFXDatePicker dp_parcela;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        c_codigo.setCellValueFactory(new PropertyValueFactory<Object, Integer>("codigo"));
        c_data.setCellValueFactory(new PropertyValueFactory<Object, Date>("data"));
        c_fornecedor.setCellValueFactory(new PropertyValueFactory<Object, Object>("forn"));
        c_parcela.setCellValueFactory(new PropertyValueFactory<Object, Integer>("parcela"));
        c_total.setCellValueFactory(new PropertyValueFactory<Object, Double>("total"));
        c_user.setCellValueFactory(new PropertyValueFactory<Object, Object>("user"));
        
        tb_total.setDisable(true);
        
        cb_fornecedor.setItems(CtrFornecedor.instancia().searchAll());
        cb_fornecedor.getSelectionModel().select(0);
        
        list_item.setItems(FXCollections.observableArrayList());
        
        dp_parcela.setValue(LocalDate.now());
        list_parcela.setItems(FXCollections.observableArrayList());
        
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
        cb_fornecedor.setDisable(flag);
        tb_nota.setDisable(flag);
        tb_item.setDisable(flag);
        tb_quant.setDisable(true);
        tb_valor.setDisable(true);
        list_material.setDisable(flag);
        btn_adicionar.setDisable(true);
        btn_remover.setDisable(flag);
        list_item.setDisable(flag);
        
        list_parcela.setDisable(flag);
        tb_par_valor.setDisable(flag);
        dp_parcela.setDisable(flag);
        tb_parcela.setDisable(flag);
        btn_par_novo.setDisable(true);
        btn_par_remover.setDisable(true);
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
        cb_fornecedor.getSelectionModel().select(0);
        tb_nota.clear();
        tb_item.clear();
        tb_quant.clear();
        tb_valor.clear();
        tb_total.clear();
        list_material.setItems(FXCollections.observableArrayList());    
        list_parcela.setItems(FXCollections.observableArrayList());
        list_item.setItems(FXCollections.observableArrayList());
        table_aquisicao.getItems().clear();
    }
    
    private boolean validaCampos()
    {
        boolean flag = true;
        String str = "";
        
        if(tb_nota.getText().isEmpty())
        {
            flag = false;
            str = "Nota não informada.";
        }
        else if(list_item.getItems().isEmpty())
        {
            flag = false;
            str = "Sem itens na lista.";
        }
        else if(list_parcela.getItems().isEmpty())
        {
            flag = false;
            str = "Aquisição sem parcelas.";
        }
        else if(!list_item.getItems().isEmpty() && !list_parcela.getItems().isEmpty())
        {
            double total = Double.valueOf(tb_total.getText());
            double ptotal = CtrAquisicao.instancia().totalParcelas(list_parcela.getItems());
            
            if((total - ptotal) != 0)
            {
                flag = false;
                str = "Valor de pagamento incompativeil ao valor da aquisição.";
            }
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
                String codigo = tb_nota.getText();
                Object forn = cb_fornecedor.getSelectionModel().getSelectedItem();
                int parc = list_parcela.getItems().size();
                double total = Double.valueOf(tb_total.getText());

                if(CtrAquisicao.instancia().insert(codigo, forn, parc, total, list_item.getItems(), list_parcela.getItems()))
                {
                    new Alert(Alert.AlertType.INFORMATION, "Aquisição Cadastrada!", ButtonType.OK).show();
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
        {
            Object cx = CtrCaixa.instancia().searchByToday();
            
            if(cx != null)
                setupNovo();
            else
                new Alert(Alert.AlertType.ERROR, "Caixa Fehcado.", ButtonType.OK).show();
        }
    }

    @FXML
    private void ClickEditar(ActionEvent event)
    {
        if(validaCampos())
        {
            String codigo = (String)CtrAquisicao.instancia().getField(_selected, "codigo");
            Object forn = cb_fornecedor.getSelectionModel().getSelectedItem();
            int parc = list_parcela.getItems().size();
            double total = Double.valueOf(tb_total.getText());

            if(CtrAquisicao.instancia().update(codigo, forn, parc, total, list_item.getItems(), list_parcela.getItems()))
            {
                new Alert(Alert.AlertType.INFORMATION, "Aquisição Alterada!", ButtonType.OK).show();
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
        if(new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir Aquisição selecionada ?", ButtonType.YES, ButtonType.NO).showAndWait().get() == ButtonType.YES)
        {
            String codigo = (String)CtrAquisicao.instancia().getField(_selected, "codigo");

            if(CtrAquisicao.instancia().delete(codigo))
                new Alert(Alert.AlertType.INFORMATION, "Aquisição Excluida!", ButtonType.OK).show();
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
        table_aquisicao.setItems(CtrAquisicao.instancia().searchAll());
    }

    @FXML
    private void ClickTable(MouseEvent event)
    {
        if(table_aquisicao.getSelectionModel().getSelectedIndex() >= 0)
        {
            _selected = table_aquisicao.getSelectionModel().getSelectedItem();
            
            cb_fornecedor.getSelectionModel().select(CtrAquisicao.instancia().getField(_selected, "obj_forn"));
            tb_total.setText(Double.toString((double)CtrAquisicao.instancia().getField(_selected, "total")));
            list_item.setItems(CtrAquisicao.instancia().getItens(_selected));
            tb_nota.setText((String)CtrAquisicao.instancia().getField(_selected, "codigo"));
            list_parcela.setItems(CtrPagamento.instancia().searchByAquisicao(_selected));
            
            setupEditar();
        }
    }

    @FXML
    private void ClickAdicionar(ActionEvent event)
    {
        int index = list_material.getSelectionModel().getSelectedIndex();
                
        if(index >= 0)
        {
            int quant = Integer.valueOf(tb_quant.getText());
            double valor = Double.valueOf(tb_valor.getText());
            Object obj = CtrAquisicao.instancia().makeItem(list_material.getItems().get(index), quant, valor);
            
            if(!CtrAquisicao.instancia().find(obj, list_item.getItems()))
            {
                CtrAquisicao.instancia().add(obj, list_item.getItems());
                tb_total.setText("" + CtrAquisicao.instancia().total(list_item.getItems()));

                list_item.refresh();

                tb_item.clear();
                tb_valor.clear();
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
        {
            list_item.getItems().remove(index);
            tb_total.setText("" + CtrAquisicao.instancia().total(list_item.getItems()));
        }
    }

    @FXML
    private void ClickListMaterial(MouseEvent event)
    {
        if(list_material.getSelectionModel().getSelectedIndex() >= 0)
        {
            tb_valor.setDisable(false);
            tb_quant.setDisable(false);
        }
        else
        {
            tb_valor.setDisable(true);
            tb_quant.setDisable(true);
        }
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
        boolean f_valor = tb_valor.getText().length() == 0;
        
        btn_adicionar.setDisable(f_quant || f_valor);
    }

    @FXML
    private void ChangeValor(KeyEvent event)
    {
        boolean f_quant = tb_quant.getText().length() == 0;
        boolean f_valor = tb_valor.getText().length() == 0;
        
        btn_adicionar.setDisable(f_quant || f_valor);
    }

    @FXML
    private void ClickListItem(MouseEvent event)
    {
        btn_remover.setDisable(list_item.getSelectionModel().getSelectedIndex() == -1);
    }

    @FXML
    private void ClickListParcela(MouseEvent event)
    {
        btn_par_remover.setDisable(false);
    }

    @FXML
    private void ClickParNovo(ActionEvent event)
    {
        double valor = Double.valueOf(tb_par_valor.getText());
        int parcela = Integer.valueOf(tb_parcela.getText());
        Date data = Date.valueOf(dp_parcela.getValue());
        
        Object obj = CtrPagamento.instancia().makeObject(valor, parcela, data);
        
        if(obj != null)
        {
            tb_parcela.clear();
            tb_par_valor.clear();
            dp_parcela.setValue(LocalDate.now());
            
            list_parcela.getItems().add(obj);
            list_parcela.refresh();
            
            btn_par_novo.setDisable(true);
            btn_par_remover.setDisable(true);
        }
        else
            new Alert(Alert.AlertType.ERROR, "Caixa Fehcado.", ButtonType.OK).show();
    }

    @FXML
    private void ClickParRemover(ActionEvent event)
    {
        if(list_parcela.getSelectionModel().getSelectedIndex() >= 0)
        {
            list_parcela.getItems().remove(list_parcela.getSelectionModel().getSelectedIndex());
            list_parcela.refresh();
        }
        
        btn_par_remover.setDisable(true);
    }

    @FXML
    private void ChangeValorParcela(KeyEvent event)
    {
        boolean f_par = tb_parcela.getText().length() == 0;
        boolean f_par_valor = tb_par_valor.getText().length() == 0;
        
        btn_par_novo.setDisable(f_par || f_par_valor);
    }

    @FXML
    private void ChangeNParcela(KeyEvent event)
    {
        boolean f_par = tb_parcela.getText().length() == 0;
        boolean f_par_valor = tb_par_valor.getText().length() == 0;
        
        btn_par_novo.setDisable(f_par || f_par_valor);
    }
    
}
