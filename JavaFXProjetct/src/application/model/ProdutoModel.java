package application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import application.Conexao;
import application.controller.CadastroProdutosController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ProdutoModel {
	public static boolean encontrou;
	private int id;
	private String nome;
	private String descricao;
	private String categoria;
	private int quantidade;
	private double preco;
	private String codigoBarras;

	public ProdutoModel(int id, String nome, String descricao, String categoria, int quantidade, double preco, String codigoBarras) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.categoria = categoria;
		this.quantidade = quantidade;
		this.preco = preco;
		this.codigoBarras = codigoBarras;
	}

	public int getId() {
		return id;
	}
	
	public String getIdFormatado() {
	    return String.format("%06d", this.id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPreco() {
		return preco;
	}
	
	public String getCodigoBarras() {
		return codigoBarras;
	}
	
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	public String getPrecoFormatado() {
	    // Configura o formato brasileiro com vírgula
	    DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
	    simbolos.setDecimalSeparator(',');
	    DecimalFormat formato = new DecimalFormat("#0.00", simbolos); // 2 casas decimais

	    return formato.format(this.preco);
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public boolean Salvar() {
		String sql = "INSERT INTO produto(nome, descricao, categoria, quantidade, preco, codigoBarras) VALUES(?,?,?,?,?,?)";

		try (Connection conn = Conexao.getConnection(); PreparedStatement consulta = conn.prepareStatement(sql)) {

			consulta.setString(1, nome);
			consulta.setString(2, descricao);
			consulta.setString(3, categoria);
			consulta.setInt(4, quantidade);
			consulta.setDouble(5, preco);
			consulta.setString(6, codigoBarras);

			int linhas = consulta.executeUpdate();

			return linhas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void Editar() {
		try (Connection conn = Conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement(
						"UPDATE produto SET nome =?, descricao=?, categoria=?, quantidade=?, preco=? , codigoBarras=? WHERE id = ?")) {

			consulta.setString(1, this.nome);
			consulta.setString(2, this.descricao);
			consulta.setString(3, this.categoria);
			consulta.setInt(4, this.quantidade);
			consulta.setDouble(5, this.preco);
			consulta.setString(6, this.codigoBarras);
			consulta.setInt(7, id);

			consulta.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
	
	public void Buscar(String valor) {
		String busca = "%" + valor + "%";
		try (Connection conn = Conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement(
						"select * from produto where id like ? or descricao like ? or categoria like ? or nome like ?");) {
			consulta.setString(1, busca);
			consulta.setString(2, busca);
			consulta.setString(3, busca);
			consulta.setString(4, busca);

			ResultSet resultado = consulta.executeQuery();
			if (resultado.next()) {
				this.id = resultado.getInt("id");
				this.nome = resultado.getString("nome");
				this.descricao = resultado.getString("descricao");
				this.categoria = resultado.getString("categoria");
				this.quantidade = resultado.getInt("quantidade");
				this.preco = resultado.getDouble("preco");
				this.codigoBarras = resultado.getString("codigoBarras");
				encontrou = true;
			} else {
				Alert mensagem = new Alert(Alert.AlertType.ERROR);
				mensagem.setContentText("Produto não encontrado!");
				encontrou = false;
				mensagem.showAndWait();
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

	public void Excluir() {
		try (Connection conn = Conexao.getConnection();
				PreparedStatement consulta = conn.prepareStatement("delete from produto where id=?");) {

			if (this.id > 0) {
				Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
				confirmacao.setTitle("Confirmação");
				confirmacao.setHeaderText("Excluir produto");
				confirmacao.setContentText("Deseja realmente excluir o produto: " + this.nome + "?");

				Optional<ButtonType> resultado = confirmacao.showAndWait();

				if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

					consulta.setInt(1, this.id);
					consulta.executeUpdate();

					Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
					sucesso.setContentText("Produto excluído com sucesso!");
					sucesso.showAndWait();

				} else {
					Alert cancelado = new Alert(Alert.AlertType.CONFIRMATION);
					cancelado.setContentText("Exclusão cancelada");
					cancelado.showAndWait();
				}

			} else {
				Alert mensagem = new Alert(Alert.AlertType.ERROR);
				mensagem.setContentText("Produto não encontrado!");
				mensagem.showAndWait();

			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public static List<ProdutoModel> listarTodos(String valor) {
		String sql = "";

		List<ProdutoModel> lista = new ArrayList<>();
		if(valor == null) {
			sql = "select * from produto";
		} else {
			sql = "select * from produto where id like ? or descricao like ? or categoria like ? or nome like ?";
		}
		
		try (Connection conn = Conexao.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			if(valor != null) {
				stmt.setString(1, "%"+valor+"%");
				stmt.setString(2, "%"+valor+"%");
				stmt.setString(3, "%"+valor+"%");
				stmt.setString(4, "%"+valor+"%");
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {

				ProdutoModel produto = new ProdutoModel(rs.getInt("id"), rs.getString("nome"),
						rs.getString("descricao"), rs.getString("categoria"), rs.getInt("quantidade"),
						rs.getDouble("preco"), rs.getString("codigoBarras"));

				lista.add(produto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lista;
	}
	
    public void ProcessarEstoque(String operacao) {
    	if(this.id>0) {
    		String sql = "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?";
    		if(operacao.equals("Saída")) {
    			sql = "UPDATE produto SET quantidade = quantidade - ? WHERE id = ?";    		
    			}
    		
    		try(Connection conn = Conexao.getConnection();
    				PreparedStatement consulta = conn.prepareStatement(sql);){
    			consulta.setInt(1, this.quantidade);
    			consulta.setInt(2, this.id);
    			consulta.execute();

    	        MovimentacaoEstoqueModel movimentacao = new MovimentacaoEstoqueModel(
    	                0, this.id, this.nome, null, this.quantidade, operacao);
    	        movimentacao.InsereMovimentacao();
    			
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
    
}