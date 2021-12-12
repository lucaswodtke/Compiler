public class Token {
	//Iniciação de um token sem valor
	Token(int id,int tipo,int linha, int coluna) {
		valor = "";
		token_id = id;
		token_line = linha;
		token_collum = coluna;
		token_type = tipo;
	}

	//Iniciação de token com valor
	Token(int id,int tipo,int linha, int coluna, String valor) {
		this.valor = valor;
		token_id = id;
		token_line = linha;
		token_collum = coluna;
		token_type = tipo;
	}
	
	String valor;
	int token_id;
	int token_type;
	int token_line;
	int token_collum;
	
	
}


