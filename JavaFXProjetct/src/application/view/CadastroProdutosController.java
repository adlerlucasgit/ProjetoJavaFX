package application.view;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class CadastroProdutosController extends SistemaController {

    @FXML
    private Button btnCadastrar;

    @FXML
    private TextField txtCategoria;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtQuantidade;
    
    @FXML 
    private TextField txtCodigoBarras;

    @FXML
    private AnchorPane ap;
    
    @FXML
    private Button btnBuscar;
    
    @FXML
    private TextField txtBuscar;
    
    private ProdutoModel produtoSelecionado;
    
    @FXML
    private TableView<ProdutoModel> tvTabelaProdutos;

    @FXML private TableColumn<ProdutoModel, Integer> colId;
    @FXML private TableColumn<ProdutoModel, String> colNome;
    @FXML private TableColumn<ProdutoModel, String> colDescricao;
    @FXML private TableColumn<ProdutoModel, String> colCodigoBarras;
    @FXML private TableColumn<ProdutoModel, String> colCategoria;
    @FXML private TableColumn<ProdutoModel, Integer> colQuantidade;
    @FXML private TableColumn<ProdutoModel, Double> colPreco;

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
	ProdutoModel produto = new ProdutoModel(0,null, null, null, 0, 0, null);

	public String FormatarID(int id) {
	    return String.format("%06d", id);
	}
	
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
    public void Cadastrar() {
    	String erros = "";
    	if(txtNome.getText().isEmpty()) {
    		erros+="\nO campo Nome está em branco";
    	}
    	if(txtPreco.getText().isEmpty()) {
    		erros+="\nO campo Preço está em branco";
    	}
    	
        if (txtNome.getText().isEmpty() || txtPreco.getText().isEmpty()) {

            Alert aviso = new Alert(Alert.AlertType.ERROR);
            aviso.setTitle("Erro");
            aviso.setHeaderText("Preencha todos os campos obrigatórios!");
            aviso.setContentText(erros);
            aviso.showAndWait();
            return;
        }

        try {
            int quantidade = 0;
            double preco = Double.parseDouble(txtPreco.getText());

            ProdutoModel produto = new ProdutoModel(
                    0,
                    txtNome.getText(),
                    txtDescricao.getText(),
                    txtCategoria.getText(),
                    quantidade,
                    preco,
                    txtCodigoBarras.getText()
                );
            
            boolean salvo = produto.Salvar();

            if (salvo) {
                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Sucesso");
                sucesso.setHeaderText(null);
                sucesso.setContentText("Produto salvo com sucesso!");
                sucesso.showAndWait();

                limparCampos();
                carregarTabela(null);
            } else {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Erro");
                erro.setHeaderText(null);
                erro.setContentText("Erro ao salvar produto!");
                erro.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setTitle("Erro");
            erro.setHeaderText(null);
            erro.setContentText("Preço deve ser números válidos!");
            erro.showAndWait();
        }
    }
    
    @FXML
    public void Editar() {
        if (produtoSelecionado == null) {
            Alert aviso = new Alert(Alert.AlertType.ERROR);
            aviso.setContentText("Nenhum produto selecionado para edição!");
            aviso.showAndWait();
            return;
        } else {

            // Atualiza os atributos do objeto com os valores dos campos
            produtoSelecionado.setNome(txtNome.getText());
            produtoSelecionado.setDescricao(txtDescricao.getText());
            produtoSelecionado.setCategoria(txtCategoria.getText());
            produtoSelecionado.setQuantidade(Integer.parseInt(txtQuantidade.getText()));
            produtoSelecionado.setPreco(Double.parseDouble(txtPreco.getText()));
            produtoSelecionado.setCodigoBarras(txtCodigoBarras.getText());


            // Chama o método do modelo
            produtoSelecionado.Editar();

            // Recarrega a tabela para refletir a edição
            carregarTabela(null);
            limparCampos();

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setContentText("Produto editado com sucesso!");
            sucesso.showAndWait();
        }
    }
    
    @FXML
    public void Pesquisar() {
    	//usa o setOnAction para buscar usando a tecla enter
    	txtBuscar.setOnAction(event -> {
        	if(!txtBuscar.getText().isEmpty()) {
        		produto.Buscar(txtBuscar.getText());
           		carregarTabela(txtBuscar.getText());
        		
           		txtId.setText(FormatarID(produto.getId()));
        		txtNome.setText(produto.getNome());
        		txtDescricao.setText(produto.getDescricao());
        		txtCategoria.setText(produto.getCategoria());
        		txtQuantidade.setText(String.valueOf((produto.getQuantidade())));
        		txtPreco.setText(FormatarPreco(produto.getPreco()));
        		
        		produtoSelecionado = produto;
        	} else {
            	Alert mensagem = new Alert(Alert.AlertType.ERROR);
            	mensagem.setContentText("Produto não encontrado!");
            	mensagem.showAndWait();
            	ProdutoModel.listarTodos(null);
            	carregarTabela(null);
            	limparCampos();
        	}
    	});
    	
    	//usa o on action do botao pesquisar
    	if(!txtBuscar.getText().isEmpty()) {
    		produto.Buscar(txtBuscar.getText());
       		carregarTabela(txtBuscar.getText());
    		
       		txtId.setText(FormatarID(produto.getId()));
    		txtNome.setText(produto.getNome());
    		txtDescricao.setText(produto.getDescricao());
    		txtCategoria.setText(produto.getCategoria());
    		txtQuantidade.setText(String.valueOf((produto.getQuantidade())));
    		txtPreco.setText(FormatarPreco(produto.getPreco()));
    		
    		produtoSelecionado = produto;
    	} else {
        	Alert mensagem = new Alert(Alert.AlertType.ERROR);
        	mensagem.setContentText("Produto não encontrado!");
        	mensagem.showAndWait();
        	ProdutoModel.listarTodos(null);
        	carregarTabela(null);
        	limparCampos();
    	}
    	
    }
    
    @FXML
    public void Excluir() {
        if (produtoSelecionado == null) {
            Alert aviso = new Alert(Alert.AlertType.ERROR);
            aviso.setContentText("Nenhum produto selecionado para excluir!");
            aviso.showAndWait();
            return;
        } else {
            produtoSelecionado.Excluir();

            carregarTabela(null);
            limparCampos();
        }
    }

    public void limparCampos() {
    	txtId.clear();
        txtNome.clear();
        txtDescricao.clear();
        txtCategoria.clear();
        txtQuantidade.clear();
        txtPreco.clear();
        txtCodigoBarras.clear();
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
                	txtId.setText(FormatarID(produto.getId()));
                    txtNome.setText(produto.getNome());
                    txtDescricao.setText(produto.getDescricao());
                    txtCategoria.setText(produto.getCategoria());
                    txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
                    txtPreco.setText(String.valueOf(produto.getPreco()));
            		txtCodigoBarras.setText(produto.getCodigoBarras());

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