import java.util.*;
public class TabelaDeSimbolos {
	//Hash para identificação dos simbolos na tabela de simbolos
	Hashtable<String, Identificador> hash = new Hashtable<String, Identificador>();
	
	//Verifica se Id esta presente na tabela
	boolean contem(String nome) {
		if (hash.contains("nome"))
			return true;
		return false;
	}
	
	//Adiciona Identificador na tabela
	boolean adicionar(String nome) {
		if (!contem(nome)) {
			hash.put(nome, new Identificador(nome));
			return true;
		} else {
			return false;
		}
	}
	
	//Printa as chaves da tabela HASH
	void mostrarHash() {
		Enumeration<String> e = hash.keys();
		System.out.print("[ " );
		while (e.hasMoreElements()) {
			
			System.out.print(e.nextElement() + " " );
		}
		System.out.println("]" );
	}
	
	//Retorna as chaves da tabela HASH
	String stringHash() {
		String retorno = ("[ ");
		Enumeration<String> e = hash.keys();
		while (e.hasMoreElements()) {
			retorno+=(e.nextElement() + " " );
		}
		retorno+="]\n";
		return retorno;
	}
		
	

}

//Classe para organizar tokens na tabela de simbolos
class Identificador {
	Identificador(String nome) {
		identificador = nome;
		tipo = 0;
	}
	Identificador(String nome, int tipo) {
		identificador = nome;
		this.tipo = tipo;
	}
	String getNome() {
		return identificador;
	}
	String identificador;
	int tipo;
}
