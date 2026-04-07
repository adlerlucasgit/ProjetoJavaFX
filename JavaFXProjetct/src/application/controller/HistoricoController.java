package application.controller;

import java.time.LocalDate;
import java.util.List;

import application.model.MovimentacaoEstoqueModel;
import application.model.ProdutoModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoricoController extends SistemaController {
	@FXML
	private Button btnBuscar;
	
	@FXML
	private Label lblProduto;
	
	@FXML
	private TableColumn <MovimentacaoEstoqueModel, String> colData;
	
	@FXML
	private TableColumn <MovimentacaoEstoqueModel, String> colID;
	
	@FXML
	private TableColumn <MovimentacaoEstoqueModel, String> colIdProd;
	
	@FXML
	private TableColumn <MovimentacaoEstoqueModel, String> colNome;
	
	@FXML
	private TableColumn <MovimentacaoEstoqueModel, String> colQuantidade;
	
	@FXML
	private TableColumn <MovimentacaoEstoqueModel, String> colTipo;
	
	@FXML
	private DatePicker dtFinal;
	
	@FXML
	private DatePicker dtInicio;
	
	@FXML
	private TableView <MovimentacaoEstoqueModel> tvHistorico;
	
	private ObservableList <MovimentacaoEstoqueModel> listaMovimentacao;
	
	private LocalDate hoje, primeiroDia, ultimoDia;
	MovimentacaoEstoqueModel movimentacao = new MovimentacaoEstoqueModel(0, 0, null, null, 0, null);
	
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
	public void initialize() {
		
    	colID.setCellValueFactory(new PropertyValueFactory<>("idFormatado"));
    	colNome.setCellValueFactory(new PropertyValueFactory<>("nomeProd"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		
		hoje = LocalDate.now();
		primeiroDia = hoje.withDayOfMonth(1);
		ultimoDia = hoje.withDayOfMonth(hoje.lengthOfMonth());
		
		dtInicio.setValue(primeiroDia);
		dtFinal.setValue(ultimoDia);
	}
	
	public void BuscarHistorico(int idProd, LocalDate dataInicio, LocalDate dataFinal) {
		List <MovimentacaoEstoqueModel> listaHistorico = movimentacao.HistoricoMovimentacao(idProd, dataInicio, dataFinal);
		listaMovimentacao = FXCollections.observableArrayList(listaHistorico);
		tvHistorico.setItems(listaMovimentacao);
		lblProduto.setText(movimentacao.getNomeProd());
		dtInicio.setValue(dataFinal);
		dtFinal.setValue(dataFinal);
	}

}
