package application.view;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ProcessarEstoqueController extends SistemaController {
    @FXML
    private AnchorPane ap;

    @FXML
    private Button btnProcessar;
    
    @FXML
    private Button btnHistorico;
    
    @FXML
    private ToggleGroup rdOperacao;
    
    @FXML
    private TextField txtNome;
    
    @FXML
    private Button btnBuscar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextField txtCodigoBarras;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtQuantidade;
    
	private ProdutoModel produtoSelecionado;

	@FXML
	private TableView<ProdutoModel> tvTabelaProdutos;

	@FXML
	private TableColumn<ProdutoModel, Integer> colId;
	@FXML
	private TableColumn<ProdutoModel, String> colNome;
	@FXML
	private TableColumn<ProdutoModel, String> colDescricao;
	@FXML
	private TableColumn<ProdutoModel, String> colCodigoBarras;
	@FXML
	private TableColumn<ProdutoModel, String> colCategoria;
	@FXML
	private TableColumn<ProdutoModel, Integer> colQuantidade;
	@FXML
	private TableColumn<ProdutoModel, Double> colPreco;

	public String FormatarID(int id) {
	    return String.format("%06d", id);
	}
	ProdutoModel produto = new ProdutoModel(0,null, null, null, 0, 0, null);
	
	public String FormatarPreco(double preco) {
		// Configura o formato brasileiro com vírgula
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
		simbolos.setDecimalSeparator(',');
		DecimalFormat formato = new DecimalFormat("#0.00", simbolos); // 2 casas decimais

		// Converte para string com vírgula
		String precoFormatado = formato.format(preco);
		
		return precoFormatado;
	}
	
	@FXML
	public void Sair() {
		System.exit(0);
	}

	@FXML
	public void CadastroProduto() {
		carregarTela("CadastroProdutos.fxml");
	}

	@FXML
	public void CadastroCliente() {
		carregarTela("CadastroClientes.fxml");
	}

	@FXML
	public void Voltar() {
		carregarTela("Sistema.fxml");
	}

	@FXML
	public void ProcessarEstoque() {
		carregarTela("ProcessarEstoque.fxml");
	}
	
	@FXML 
	public void Historico() {
		if(produtoSelecionado == null) {
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setContentText("Nenhum produto selecionado");
			a.showAndWait();
			return;
		}
		LocalDate hoje = LocalDate.now();
		LocalDate primeiroDia = hoje.withDayOfMonth(1);
		LocalDate ultimoDia = hoje.withDayOfMonth(hoje.lengthOfMonth());
    	try {
    		FXMLLoader loader = new FXMLLoader(
    				getClass().getResource("HistoricoMovimentacao.fxml"));
    		Parent root = loader.load();
    		HistoricoController controller = loader.getController();
    		controller.BuscarHistorico(produtoSelecionado.getId(), primeiroDia, ultimoDia);
    		Stage stage= new Stage();
    		stage.setScene(new Scene (root));
    		stage.show();
        } catch(Exception e) {
    		e.printStackTrace();
    	}
		
	}
	
    @FXML
    public void Pesquisar() {
    	//usa o on action do botao pesquisar
    	if(!txtBuscar.getText().isEmpty()) {
    		produto.Buscar(txtBuscar.getText());
       		carregarTabela(txtBuscar.getText());
    		
       		txtId.setText(FormatarID(produto.getId()));
    		txtNome.setText(produto.getNome());
    		txtQuantidade.setText(String.valueOf((produto.getQuantidade())));
    		txtCodigoBarras.setText(produto.getCodigoBarras());
    		
    		produtoSelecionado = produto;
    	} else {
        	Alert mensagem = new Alert(Alert.AlertType.ERROR);
        	mensagem.setContentText("Produto não encontrado!");
        	mensagem.showAndWait();
        	carregarTabela(null);
        	limparCampos();
    	}
    	
    }
	
    @FXML
    public void initialize() {
    	
    	colId.setCellValueFactory(new PropertyValueFactory<>("idFormatado"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoFormatado"));
        colCodigoBarras.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));

        carregarTabela(null);

        tvTabelaProdutos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldValue, produto) -> {
                if (produto != null) {
                	txtId.setText(FormatarID(produto.getId()));
                    txtNome.setText(produto.getNome());
                    txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
                    txtCodigoBarras.setText(produto.getCodigoBarras());

                    produtoSelecionado = produto;
                }
            }
        );
    }
    
    public void Processar() {
    	if(!txtQuantidade.getText().isBlank()) {
    		produtoSelecionado.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
        	RadioButton operacao = (RadioButton) rdOperacao.getSelectedToggle();
        	produtoSelecionado.ProcessarEstoque(operacao.getText());
        	limparCampos();
        	carregarTabela(null);
    	}else {
    		Alert mensagem = new Alert(Alert.AlertType.ERROR);
    		mensagem.setContentText("Nenhum produto selecionado");
    		mensagem.showAndWait();
    	}
    	
    }
    
    public void carregarTabela(String valor) {
        ObservableList<ProdutoModel> lista =
                FXCollections.observableArrayList(ProdutoModel.listarTodos(valor));

        tvTabelaProdutos.setItems(lista);
    }
    
    public void limparCampos() {
    	txtId.clear();
        txtNome.clear();
        txtQuantidade.clear();
        txtCodigoBarras.clear();
        txtBuscar.clear();
    }
}
