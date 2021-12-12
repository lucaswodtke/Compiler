import java.util.*;

public class Tool {
	String[][] tabelaSintaticaPreditiva = new String[29][37]; //Numero de Simbolos NT x Numero de Simbolos T
	Stack<String> pilha = new Stack<String>();	//Pilha para analise sintatica
	Hashtable<String, Integer> hash_NT = new Hashtable<String, Integer>();	//Hash para pegar id de um sismbolo NT na tabela de analise sintatica
	Hashtable<Integer, String> hash_T = new Hashtable<Integer, String>();	//Hash para pegar o simbolo do token
	String ultimo_erro;	//Erro que aconteceu na analise lexica

	//Metodo para retorno de string contendo um erro ou tokens + tabela de simbolos
	public String analise_lexica_string(String string, TabelaDeSimbolos tab) {
		Token[] tokens;
		String retorno = "";
		try {
			tokens = analise_lexica(string, tab);
		} catch (Erro e) {
			//Se aconteceu um erro lexico
			return ultimo_erro;
		}

		//Escreve tokens e tabela de simbolos em retorno
		retorno+="Tokens: \n";
		for (Token token: tokens) {
			retorno+=("[ " + hash_T.get(token.token_type) + " , "+ token.token_type + " , " + token.token_line + " , " + token.token_id + " ]\n");
		}
		retorno+=("Formato:  \n[Token, Num_Token, Linha, Id]\n");
		retorno+=("\nTabela de Simbolos: \n");
		retorno+=tab.stringHash();
		return retorno;
	}

	//Metodo para retorno de string contendo um erro ou mensagem de Sucesso
	public String analise_sintatica_string(String string, TabelaDeSimbolos tab) {
		try {
			Token[] tokens = analise_lexica(string, tab);
			return (analise_sintatica(transformar(tokens)));
		} catch (Erro e) {
			//Se aconteceu um erro lexico
			return ("Erro lexico");
		}
	}

	//Retorna array dos tokens encontrados ou 'joga' um Erro
	public Token[] analise_lexica(String string, TabelaDeSimbolos tab) throws Erro {
		string+="&";	//Coloca '&' no final da entrada do analisador
		int line_counter = 0;	//Conta em que linha esta
		int token_counter = 0;	//Conta em que lugar do array de retorno colocar o proximo token
		int collum_counter = 0;	//Conta em que coluna esta
		char[] entrada_char = string.toCharArray();
		Token[] retorno = new Token[entrada_char.length];	//Max de 1 token por char da string

		//Começo da Identificação dos tokens
		for (int i = 0; i < entrada_char.length; i++) {
			if (entrada_char[i] == ' ' || entrada_char[i] == '	') {	//Ignora espaço em branco/tab's mas conta colunas
				collum_counter++;
			} else if (entrada_char[i] == '{') {	//Encontrou simbolo '{', cria token
				retorno[token_counter] = new Token(token_counter, 17, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '}') { //Encontrou simbolo '}', cria token
				retorno[token_counter] = new Token(token_counter, 18, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '[') {	//Encontrou simbolo '[]', cria token
				retorno[token_counter] = new Token(token_counter, 20, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == ']') {	//Encontrou simbolo ']', cria token
				retorno[token_counter] = new Token(token_counter, 21, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '(') {	//Encontrou simbolo '(', cria token
				retorno[token_counter] = new Token(token_counter, 31, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == ')') {	//Encontrou simbolo ')', cria token
				retorno[token_counter] = new Token(token_counter, 13, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '+') {	//Encontrou simbolo '+', cria token
				retorno[token_counter] = new Token(token_counter, 28, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '*') {	//Encontrou simbolo '*', cria token
				retorno[token_counter] = new Token(token_counter, 32, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '/') {	//Encontrou simbolo '/', cria token
				retorno[token_counter] = new Token(token_counter, 33, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '%') {	//Encontrou simbolo '%', cria token
				retorno[token_counter] = new Token(token_counter, 34, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '-') {	//Encontrou simbolo '-', cria token
				retorno[token_counter] = new Token(token_counter, 29, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == ';') {	//Encontrou simbolo ';', cria token
				retorno[token_counter] = new Token(token_counter, 19, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if ( i + 1 < entrada_char.length && entrada_char[i+1] == '=' && entrada_char[i] == '=') {	//Se encontrou '=' verifica o proximo char
				i++;
				retorno[token_counter] = new Token(token_counter, 26, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
				collum_counter++;
			} else if ( i + 1 < entrada_char.length && entrada_char[i+1] == '=' && entrada_char[i] == '!') {		//Se encontrou '=' verifica o proximo char
				i++;
				retorno[token_counter] = new Token(token_counter, 27, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
				collum_counter++;
			} else if ( i + 1 < entrada_char.length && entrada_char[i+1] == '=' && entrada_char[i] == '<') {		//Se encontrou '=' verifica o proximo char
				i++;
				retorno[token_counter] = new Token(token_counter, 24, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
				collum_counter++;
			} else if ( i + 1 < entrada_char.length && entrada_char[i+1] == '=' && entrada_char[i] == '>') {		//Se encontrou '=' verifica o proximo char
				i++;
				retorno[token_counter] = new Token(token_counter, 25, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '<') {
				retorno[token_counter] = new Token(token_counter, 22, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '>') {
				retorno[token_counter] = new Token(token_counter, 23, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '=') {
				retorno[token_counter] = new Token(token_counter, 30, line_counter, collum_counter);
				token_counter++;
				collum_counter++;
			} else if (entrada_char[i] == '"') {	//Comeco de string, adiciona proximos simbolos no token até encontrar outro '"'
				String aux = "";
				collum_counter++;
				while ( i + 1 < entrada_char.length && entrada_char[i + 1] != '"') {	//Enquanto tiver simbolos na entrada e n encontrar '"'
					i++;
					collum_counter++;
					aux += entrada_char[i];
				}
				if (i + 1 == entrada_char.length) {	//Parou por que chegou ao final da entrada
					System.out.println("Erro Lexico Linha:" + line_counter + " Coluna: " + collum_counter);
					ultimo_erro = ("*****Erro Lexico*****\nLinha:" + line_counter + " Coluna: " + collum_counter);
					throw new Erro();
				} else {	//Parou por que encontrou '"'
					i++;

					retorno[token_counter] = new Token(token_counter, 16, line_counter, collum_counter,aux);
					token_counter++;
					collum_counter++;
				}
			} else if (entrada_char[i] == '\n') {	//Ignora \n mas conta linha
				line_counter++;
				collum_counter = 0;
			} else if (Character.isDigit(entrada_char[i])) {	//encontrou um numero é inteiro ou float
					String aux = "";
					while(i + 1 < entrada_char.length && Character.isDigit(entrada_char[i]) && Character.isDigit(entrada_char[i + 1])) {
						aux+=entrada_char[i];
						i++;
						collum_counter++;
					}
					collum_counter++;
					aux+=entrada_char[i];
					if (entrada_char[i + 1] == '.') {	//Se possui um '.', o proximo caracter tem que ser digito, e o token sera float
						collum_counter++;
						i++;
						aux+=entrada_char[i];
						i++;
						if (!Character.isDigit(entrada_char[i])) {	//Nao encontrou digito depois de '.'
							System.out.println("Erro Lexico Linha:" + line_counter + " Coluna: " + collum_counter);
							ultimo_erro = ("*****Erro Lexico*****\nLinha:" + line_counter + " Coluna: " + collum_counter);
							throw new Erro();

						}
						while(i + 1 < entrada_char.length && Character.isDigit(entrada_char[i]) && Character.isDigit(entrada_char[i + 1])) {	//Aceita digitos até proximo n seri digito
							aux+=entrada_char[i];
							collum_counter++;
							i++;
						}
						collum_counter++;
						aux+=entrada_char[i];
						retorno[token_counter] = new Token(token_counter, 7, line_counter, collum_counter,aux);
						token_counter++;
					} else {	//nao encontrou '.', e o token sera int
						retorno[token_counter] = new Token(token_counter, 3, line_counter, collum_counter,aux);
						token_counter++;
					}
			} else if (Character.isLetter(entrada_char[i])) {
				String aux = "";
				aux += entrada_char[i];
				collum_counter++;
				while (Character.isLetter(entrada_char[i + 1])) {	//Pega letras até encontrar algo diferente (espaco, tab, simbolos,...)
					collum_counter++;
					aux+=entrada_char[++i];
				}
				if (aux.equals("break")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 1, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("int")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 4, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("float")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 5, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("string")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 6, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("print")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 8, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("read")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 9, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("return")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 10, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("if")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 12, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("else")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 14, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("for")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 11, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("new")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 0, line_counter, collum_counter,aux);
					token_counter++;
				} else if (aux.equals("null")) {	//Compara para ver se nao é palavra reservada
					retorno[token_counter] = new Token(token_counter, 15, line_counter, collum_counter,aux);
					token_counter++;
				} else {	//É identificador
					//Coloca identificador na tabela de simbolos
					tab.adicionar(aux);
					retorno[token_counter] = new Token(token_counter, 2, line_counter, collum_counter,aux);
					token_counter++;
				}
			} else {
				if (entrada_char[i] != '&') {	//Se encontrou algum simbolo nao previsto
					System.out.println("erro Lexico Linha:" + line_counter + " Coluna: " + collum_counter);
					ultimo_erro = ("*****Erro Lexico*****\nLinha:" + line_counter + " Coluna: " + collum_counter);
					throw new Erro();
				}
			}
		}

		//Eliminação de espacos nullos no vetor
		Token[] retorno1 = new Token[token_counter];
		for (int i = 0; i < token_counter; i++) {
			retorno1[i] = retorno[i];
		}

		return retorno1;
	}

	public String analise_sintatica(ArrayList<Token> tokens) {
		pilha = new Stack<String>();	//Reseta a pilha
		String derivacao="Inicio";	//Armazena as produções utilizadas
		tokens.add(new Token(0, 36, 0, 0));	//Adiciona token $ para simbolizar final da lista de tokens
		pilha.push("$");	//Simbolo $
		pilha.push("Program");	//Começo programa
		Token inicial = new Token(-1,37,-1,-1);	//Para token ter valor inicial
		String prod = "";	//Producao atual
		String simboloPilha;
		String simboloEntrada;
		String esperado = "";
		String ultima_prod = "";
		String[] aux;

		while (pilha.size() > 1 && tokens.size() > 0) {	//Enquanto tiver valores na pilha e na lista
			inicial = tokens.get(0);	//Token no começo da lista
			simboloPilha = pilha.pop();	//Simbolo no topo da pilha
			simboloEntrada = hash_T.get(inicial.token_type);	//Simbolo do token inicial

			if (simboloEntrada.equals(simboloPilha)) {	//Se simbolo do token inicial = simbolo topo da pilha
				tokens.remove(0);
				continue;
			}

			try {
				prod = tabelaSintaticaPreditiva[hash_NT.get(simboloPilha)][inicial.token_type];	//Pega producao na tabela de analise sintatica
				if (!prod.equals("PRODUCAOFILLER")) {
					derivacao+="\n" + prod;
					ultima_prod = prod;
				} else {	//Miss na tabela
					return ("\nLinha_Erro: " + inicial.token_line + "\nToken_Erro: " + hash_T.get(inicial.token_type) + "\nNT_Erro: " + ultima_prod + "\nDerivacoes: " + derivacao);
				}
			} catch(Exception e) {
				//Exception se simbolo Terminal for colocado na pilha
			}

			esperado = prod.split("->")[0];


			if (!simboloPilha.equals(esperado)) {
				//Se o simbolo que a produção esta esperando é diferente do que tem na pilha
				return ("\nLinha_Erro: " + inicial.token_line + "\nToken_Erro: " + hash_T.get(inicial.token_type) + "\nNT_Erro: " + ultima_prod + "\nDerivacoes: " + derivacao);
			}

			try {	//Simbolos depois de '->'
				aux = prod.split("->")[1].split(" ");
			} catch(Exception e) {
				//Se producao filler for detectada
				return ("\nLinha_Erro: " + inicial.token_line + "\nToken_Erro: " + hash_T.get(inicial.token_type) + "\nNT_Erro: " + esperado + "\nDerivacoes: " + derivacao);
			}

			for (int i = aux.length - 1; i >= 0; i--) {
				if (!aux[i].equals("&"))  {
					pilha.push(aux[i]);	//P Cada simbolo coloca ele na pilha se nao for vazio
				} else {
					//transicaoo vazia
				}

			}
		}

		String prod_pilha;
		prod_pilha = pilha.pop();
		if (pilha.size() == 0 && tokens.size() == 1) {
			if (prod_pilha.equals(hash_T.get(tokens.get(0).token_type))) {
				return "Sucesso";	//Sucesso se terminou e unicos simbolos na pilha e lista sao '$'
			}
			return ("\nLinha_Erro: " + tokens.get(0).token_line + "\nToken_Erro: " + hash_T.get(tokens.get(0).token_type) + "\nNT_Erro: " + esperado + "\nDerivacoes: " + derivacao);
		}

		if (!tokens.get(0).equals(null))
			return ("\nLinha_Erro: " + tokens.get(0).token_line + "\nToken_Erro: " + hash_T.get(tokens.get(0).token_type) + "\nNT_Erro: " + esperado + "\nDerivacoes: " + derivacao);
		else
			return ("\nLinha_Erro: " + inicial.token_line + "\nToken_Erro: " + hash_T.get(inicial.token_type) + "\nNT_Erro: " + esperado + "\nDerivacoes: " + derivacao);

	}

	Tool () {
		//Inicio do hash para identificação dos NT
		hash_NT.put("Program", 0);
		hash_NT.put("STATEMENT", 1);
		hash_NT.put("VARDECL", 2);
		hash_NT.put("VARDECLLOOP", 3);
		hash_NT.put("ATRIBSTAT", 4);
		hash_NT.put("PRINTSTAT", 5);
		hash_NT.put("READSTAT", 6);
		hash_NT.put("RETURNSTAT", 7);
		hash_NT.put("IFSTAT", 8);
		hash_NT.put("FORSTAT", 9);
		hash_NT.put("STATELIST", 10);
		hash_NT.put("ALLOCEXPRESSION", 11);
		hash_NT.put("EXPRESSIONLOOP", 12);
		hash_NT.put("EXPRESSION", 13);
		hash_NT.put("NUMEXPRESSION", 14);
		hash_NT.put("TERMLOOP", 15);
		hash_NT.put("TERM", 16);
		hash_NT.put("UNARYEXPRLOOP", 17);
		hash_NT.put("UNARYEXPR", 18);
		hash_NT.put("FACTOR", 19);
		hash_NT.put("LVALUE", 20);
		hash_NT.put("LVALUELOOP", 21);
		hash_NT.put("VARDECL'", 22);
		hash_NT.put("ATRIBSTAT'", 23);
		hash_NT.put("IFSTAT'", 24);
		hash_NT.put("STATELIST'", 25);
		hash_NT.put("ALLOCEXPRESSION'", 26);
		hash_NT.put("EXPRESSIONLOOP'", 27);
		hash_NT.put("EXPRESSION'", 28);

		//Inicio do hash para identificaçãod os tokens
		hash_T.put(0, "new");
		hash_T.put(1, "break");
		hash_T.put(2, "ident");
		hash_T.put(3, "int_constant");
		hash_T.put(4,"int");
		hash_T.put(5,"float");
		hash_T.put(6,"string");
		hash_T.put(7,"float_constant");
		hash_T.put(8,"print");
		hash_T.put(9,"read");
		hash_T.put(10,"return");
		hash_T.put(11,"for");
		hash_T.put(12,"if");
		hash_T.put(13,")");
		hash_T.put(14,"else");
		hash_T.put(15,"null");
		hash_T.put(16,"string_constant");
		hash_T.put(17,"{");
		hash_T.put(18,"}");
		hash_T.put(19,";");
		hash_T.put(20,"[");
		hash_T.put(21,"]");
		hash_T.put(22,"<");
		hash_T.put(23,">");
		hash_T.put(24,"<=");
		hash_T.put(25,">=");
		hash_T.put(26,"==");
		hash_T.put(27,"!=");
		hash_T.put(28,"+");
		hash_T.put(29,"-");
		hash_T.put(30,"=");
		hash_T.put(31,"(");
		hash_T.put(32,"*");
		hash_T.put(33,"/");
		hash_T.put(34,"%");
		hash_T.put(35,"else");
		hash_T.put(36,"$");
		hash_T.put(37,"Filler");

		//Preenchimento da tabela sintatica preditiva com valores nao validos
		for (String[] a: tabelaSintaticaPreditiva) {
			Arrays.fill(a, "PRODUCAOFILLER");
		}

		//Preenchimento da tabela com os valores utilizados
		//Program=0
		tabelaSintaticaPreditiva[0][19] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][17] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][1] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][6] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][2] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][5] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][4] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][8] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][9] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][10] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][12] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][11] = "Program->STATEMENT";
		tabelaSintaticaPreditiva[0][36] = "Program->&";

		//STATEMENT=1
		tabelaSintaticaPreditiva[1][19] = "STATEMENT->;";
		tabelaSintaticaPreditiva[1][17] = "STATEMENT->{ STATELIST }";
		tabelaSintaticaPreditiva[1][6] = "STATEMENT->VARDECL ;";
		tabelaSintaticaPreditiva[1][2] = "STATEMENT->ATRIBSTAT ;";
		tabelaSintaticaPreditiva[1][1] = "STATEMENT->break ;";
		tabelaSintaticaPreditiva[1][4] = "STATEMENT->VARDECL ;";
		tabelaSintaticaPreditiva[1][5] = "STATEMENT->VARDECL ;";
		tabelaSintaticaPreditiva[1][8] = "STATEMENT->PRINTSTAT ;";
		tabelaSintaticaPreditiva[1][9] = "STATEMENT->READSTAT ;";
		tabelaSintaticaPreditiva[1][10] = "STATEMENT->RETURNSTAT ;";
		tabelaSintaticaPreditiva[1][11] = "STATEMENT->FORSTAT ;";

		//VARDECL=2
		tabelaSintaticaPreditiva[2][6] = "VARDECL->string ident VARDECL'";
		tabelaSintaticaPreditiva[2][5] = "VARDECL->float ident VARDECL'";
		tabelaSintaticaPreditiva[2][4] = "VARDECL->int ident VARDECL'";

		//VARDECLLOOP=3
		tabelaSintaticaPreditiva[3][19] = "VARDECLLOOP->&";
		tabelaSintaticaPreditiva[3][20] = "VARDECLLOOP->[ int_constant ] VARDECLLOOP";

		//ATRIBSTAT=4
		tabelaSintaticaPreditiva[4][2] = "ATRIBSTAT->LVALUE = ATRIBSTAT'";

		//PRINTSTAT=5
		tabelaSintaticaPreditiva[5][8] = "PRINTSTAT->print EXPRESSION";

		//READSTAT=6
		tabelaSintaticaPreditiva[6][9] = "READSTAT->read LVALUE";

		//RETURNSTAT=7
		tabelaSintaticaPreditiva[7][10] = "RETURNSTAT->return";

		//IFSTAT=8
		tabelaSintaticaPreditiva[8][12] = "IFSTAT->if ( EXPRESSION ) STATEMENT IFSTAT'";

		//FORSTAT=9
		tabelaSintaticaPreditiva[9][11] = "FORSTAT->for ( ATRIBSTAT ; EXPRESSION ; ATRIBSTAT ) STATEMENT";

		//STATELIST=10
		tabelaSintaticaPreditiva[10][6] = "STATELIST->string ident VARDECL' ; STATELIST'";
		tabelaSintaticaPreditiva[10][5] = "STATELIST->float ident VARDECL' ; STATELIST'";
		tabelaSintaticaPreditiva[10][4] = "STATELIST->int ident VARDECL' ; STATELIST'";
		tabelaSintaticaPreditiva[10][8] = "STATELIST->print EXPRESSION ; STATELIST'";
		tabelaSintaticaPreditiva[10][9] = "STATELIST->read LVALUE ; STATELIST'";
		tabelaSintaticaPreditiva[10][10] = "STATELIST-> return ; STATELIST'";
		tabelaSintaticaPreditiva[10][12] = "STATELIST->if ( EXPRESSION ) STATEMENT IFSTAT' ; STATELIST'";
		tabelaSintaticaPreditiva[10][11] = "STATELIST->for ( ATRIBSTAT ; EXPRESSION ; ATRIBSTAT ) STATEMENT ; STATELIST'";
		tabelaSintaticaPreditiva[10][17] = "STATELIST->{ STATELIST } STATELIST'";
		tabelaSintaticaPreditiva[10][1] = "STATELIST->break ; STATELIST' ";
		tabelaSintaticaPreditiva[10][19] = "STATELIST->; STATELIST'";
		tabelaSintaticaPreditiva[10][2] = "STATELIST->LVALUE = ATRIBSTAT' ; STATELIST'";

		//ALLOCEXPRESSION=11
		tabelaSintaticaPreditiva[11][0] = "ALLOCEXPRESSION->new ALLOCEXPRESSION'";

		//EXPRESSIONLOOP=12
		tabelaSintaticaPreditiva[12][20] = "EXPRESSIONLOOP->[ EXPRESSION ] EXPRESSIONLOOP'";

		//EXPRESSION=13
		tabelaSintaticaPreditiva[13][28] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";
		tabelaSintaticaPreditiva[13][29] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";
		tabelaSintaticaPreditiva[13][3] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";
		tabelaSintaticaPreditiva[13][7] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";
		tabelaSintaticaPreditiva[13][15] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";
		tabelaSintaticaPreditiva[13][16] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";
		tabelaSintaticaPreditiva[13][31] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";
		tabelaSintaticaPreditiva[13][2] = "EXPRESSION->NUMEXPRESSION EXPRESSION'";

		//NUMEXPRESSION=14
		tabelaSintaticaPreditiva[14][28] = "NUMEXPRESSION->TERM TERMLOOP";
		tabelaSintaticaPreditiva[14][29] = "NUMEXPRESSION->TERM TERMLOOP";
		tabelaSintaticaPreditiva[14][31] = "NUMEXPRESSION->TERM TERMLOOP";
		tabelaSintaticaPreditiva[14][15] = "NUMEXPRESSION->TERM TERMLOOP";
		tabelaSintaticaPreditiva[14][3] = "NUMEXPRESSION->TERM TERMLOOP";
		tabelaSintaticaPreditiva[14][7] = "NUMEXPRESSION->TERM TERMLOOP";
		tabelaSintaticaPreditiva[14][16] = "NUMEXPRESSION->TERM TERMLOOP";
		tabelaSintaticaPreditiva[14][2] = "NUMEXPRESSION->TERM TERMLOOP";

		//TERMLOOP=15
		tabelaSintaticaPreditiva[15][28] = "TERMLOOP->+ TERM TERMLOOP";
		tabelaSintaticaPreditiva[15][29] = "TERMLOOP->- TERM TERMLOOP";
		tabelaSintaticaPreditiva[15][19] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][21] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][13] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][22] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][23] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][24] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][25] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][27] = "TERMLOOP->&";
		tabelaSintaticaPreditiva[15][26] = "TERMLOOP->&";

		//teste
		tabelaSintaticaPreditiva[15][18] = "TERMLOOP->&";

		//TERM=16
		tabelaSintaticaPreditiva[16][28] = "TERM->UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[16][29] = "TERM->UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[16][3] = "TERM->UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[16][7] = "TERM->UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[16][15] = "TERM->UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[16][16] = "TERM->UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[16][2] = "TERM->UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[16][31] = "TERM->UNARYEXPR UNARYEXPRLOOP";

		//UNARYEXPRLOOP=17
		tabelaSintaticaPreditiva[17][32] = "UNARYEXPRLOOP->* UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[17][33] = "UNARYEXPRLOOP->/ UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[17][34] = "UNARYEXPRLOOP->% UNARYEXPR UNARYEXPRLOOP";
		tabelaSintaticaPreditiva[17][19] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][21] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][13] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][28] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][29] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][22] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][23] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][24] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][25] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][27] = "UNARYEXPRLOOP->&";
		tabelaSintaticaPreditiva[17][26] = "UNARYEXPRLOOP->&";


		//UNARYEXPR=18
		tabelaSintaticaPreditiva[18][28] = "UNARYEXPR->+ FACTOR";
		tabelaSintaticaPreditiva[18][29] = "UNARYEXPR->- FACTOR";
		tabelaSintaticaPreditiva[18][31] = "UNARYEXPR->FACTOR";
		tabelaSintaticaPreditiva[18][15] = "UNARYEXPR->FACTOR";
		tabelaSintaticaPreditiva[18][3] = "UNARYEXPR->FACTOR";
		tabelaSintaticaPreditiva[18][7] = "UNARYEXPR->FACTOR";
		tabelaSintaticaPreditiva[18][16] = "UNARYEXPR->FACTOR";
		tabelaSintaticaPreditiva[18][2] = "UNARYEXPR->FACTOR";

		//FACTOR=19
		tabelaSintaticaPreditiva[19][3] = "FACTOR->int_constant";
		tabelaSintaticaPreditiva[19][7] = "FACTOR->float_constant";
		tabelaSintaticaPreditiva[19][16] = "FACTOR->string_constant";
		tabelaSintaticaPreditiva[19][15] = "FACTOR->null";
		tabelaSintaticaPreditiva[19][2] = "FACTOR->LVALUE";
		tabelaSintaticaPreditiva[19][31] = "FACTOR->( EXPRESSION )";

		//LVALUE=20
		tabelaSintaticaPreditiva[20][2] = "LVALUE->ident LVALUELOOP";
		//LVALUELOOP=21
		tabelaSintaticaPreditiva[21][20] = "LVALUELOOP->[ EXPRESSION ] LVALUELOOP";
		tabelaSintaticaPreditiva[21][19] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][21] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][30] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][13] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][28] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][29] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][32] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][33] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][34] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][22] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][23] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][24] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][25] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][26] = "LVALUELOOP->&";
		tabelaSintaticaPreditiva[21][27] = "LVALUELOOP->&";

		///VARDECL'=22
		tabelaSintaticaPreditiva[22][20] = "VARDECL'->[ int_constant ] VARDECLLOOP";
		tabelaSintaticaPreditiva[22][19] = "VARDECL'->&";

		//ATRIBSTAT'=23
		tabelaSintaticaPreditiva[23][28] = "ATRIBSTAT'->+ FACTOR UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][29] = "ATRIBSTAT'->- FACTOR UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][3] = "ATRIBSTAT'->int_constant UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][7] = "ATRIBSTAT'->float_constant UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][16] = "ATRIBSTAT'->string_constant UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][15] = "ATRIBSTAT'->null UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][2] = "ATRIBSTAT'->ident LVALUELOOP UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][31] = "ATRIBSTAT'->( EXPRESSION ) UNARYEXPRLOOP TERMLOOP EXPRESSION'";
		tabelaSintaticaPreditiva[23][0] = "ATRIBSTAT'->new ALLOCEXPRESSION'";

		//IFSTAT'=24
		tabelaSintaticaPreditiva[24][35] = "IFSTAT'->else STATEMENT";
		tabelaSintaticaPreditiva[24][14] = "IFSTAT'->else STATEMENT";
		tabelaSintaticaPreditiva[24][19] = "IFSTAT'->&";

		///STATELIST'=25
		tabelaSintaticaPreditiva[25][6] = "STATELIST'->string ident VARDECL' ; STATELIST'";
		tabelaSintaticaPreditiva[25][5] = "STATELIST'->float ident VARDECL' ; STATELIST'";
		tabelaSintaticaPreditiva[25][4] = "STATELIST'->int ident VARDECL' ; STATELIST'";
		tabelaSintaticaPreditiva[25][2] = "STATELIST'->ident LVALUELOOP = ATRIBSTAT' ; STATELIST'";
		tabelaSintaticaPreditiva[25][8] = "STATELIST'->print EXPRESSION ; STATELIST'";
		tabelaSintaticaPreditiva[25][9] = "STATELIST'->read LVALUE ; STATELIST'";
		tabelaSintaticaPreditiva[25][10] = "STATELIST'->return ; STATELIST'";
		tabelaSintaticaPreditiva[25][12] = "STATELIST'->if ( EXPRESSION ) STATEMENT IFSTAT' ; STATELIST'";
		tabelaSintaticaPreditiva[25][11] = "STATELIST'->for ( ATRIBSTAT ; EXPRESSION ; ATRIBSTAT ) STATEMENT ; STATELIST'";
		tabelaSintaticaPreditiva[25][17] = "STATELIST'->{ STATELIST } STATELIST'";
		tabelaSintaticaPreditiva[25][1] = "STATELIST'->break ; STATELIST'";
		tabelaSintaticaPreditiva[25][19] = "STATELIST'->; STATELIST'";
		tabelaSintaticaPreditiva[25][18] = "STATELIST'->&";

		//ALLOCEXPRESSION'=26
		tabelaSintaticaPreditiva[26][4] = "ALLOCEXPRESSION'->int EXPRESSIONLOOP";
		tabelaSintaticaPreditiva[26][5] = "ALLOCEXPRESSION'->float EXPRESSIONLOOP";
		tabelaSintaticaPreditiva[26][6] = "ALLOCEXPRESSION'->string EXPRESSIONLOOP";

		//EXPRESSIONLOOP'=27
		tabelaSintaticaPreditiva[27][20] = "EXPRESSIONLOOP'->[ EXPRESSION ] EXPRESSIONLOOP'";
		tabelaSintaticaPreditiva[27][19] = "EXPRESSIONLOOP'->&";
		tabelaSintaticaPreditiva[27][13] = "EXPRESSIONLOOP'->&";

		//EXPRESSION'=28
		tabelaSintaticaPreditiva[28][19] = "EXPRESSION'->&";
		tabelaSintaticaPreditiva[28][13] = "EXPRESSION'->&";
		tabelaSintaticaPreditiva[28][21] = "EXPRESSION'->&";
		tabelaSintaticaPreditiva[28][22] = "EXPRESSION'->< NUMEXPRESSION";
		tabelaSintaticaPreditiva[28][23] = "EXPRESSION'->> NUMEXPRESSION";
		tabelaSintaticaPreditiva[28][24] = "EXPRESSION'-><= NUMEXPRESSION";
		tabelaSintaticaPreditiva[28][25] = "EXPRESSION'->>= NUMEXPRESSION";
		tabelaSintaticaPreditiva[28][26] = "EXPRESSION'->== NUMEXPRESSION";
		tabelaSintaticaPreditiva[28][27] = "EXPRESSION'->!= NUMEXPRESSION";

	}

	//Funcao auxiliar para transformar Token[] em ArrayList<Token>
	ArrayList<Token> transformar(Token[] tokens) {
		ArrayList<Token> retorno = new ArrayList<Token>();
		for (int i = tokens.length - 1; i >= 0; i--) {
			retorno.add(0, tokens[i]);
		}
		return retorno;
	}
}

//Excecao para erro lexico
class Erro extends Exception{
	private static final long serialVersionUID = 1L;

}
