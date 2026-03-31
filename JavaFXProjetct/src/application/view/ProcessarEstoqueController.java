package application.view;

import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProcessarEstoqueController extends SistemaController {

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
                	//*txtId.setText(FormatarID(produto.getId()));
                    //txtNome.setText(produto.getNome());
                    //txtDescricao.setText(produto.getDescricao());
                    //txtCategoria.setText(produto.getCategoria());
                    //txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
                    //txtPreco.setText(String.valueOf(produto.getPreco()));
            		//txtCodigoBarras.setText(produto.getCodigoBarras());

                    produtoSelecionado = produto;
                }
            }
        );
    }
    
	
    public void carregarTabela(String valor) {
        ObservableList<ProdutoModel> lista =
                FXCollections.observableArrayList(ProdutoModel.listarTodos(valor));

        tvTabelaProdutos.setItems(lista);
    }
}
