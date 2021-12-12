import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Arquivos {
	String caminho_inicial;
	String os;
	Arquivos() {
		caminho_inicial = System.getProperty("user.dir");
		os = System.getProperty("os.name").toLowerCase();
	}

	//Metodo para ler os arquivos .ccc dentro de Compilador/Arquivos
	String lerArquivo(String cam) throws IOException{
		String caminho = caminho_inicial;
		if (cam.equals("README") || cam.equals("alunos")) {
			if (os.equals("win")) {
			caminho += "\\Compilador\\Arquivos\\" + cam;
			} else {
				System.out.println(caminho);
				caminho += "/Compilador/Arquivos/" + cam;
				System.out.println(caminho);
			}
		} else {
			if (os.equals("win")) {
				caminho += "\\Compilador\\Arquivos\\" + cam + ".ccc";
			} else {
				System.out.println(caminho);
				caminho += "/Compilador/Arquivos/" + cam + ".ccc";
				System.out.println(caminho);
			}
		}
		BufferedReader buffRead = new BufferedReader(new FileReader(caminho));
        String linha = "";
        String texto = "";
        while (true) {
        	linha = buffRead.readLine();
        	if (linha != null) {
                texto+=linha + "\n";
            } else
                break; 
        }
        buffRead.close();
        return texto;
	}
	
	//Metodo para salvar os arquivos .ccc dentro de Compilador/Arquivos
	void salvarArquivo(String texto, String cam) throws IOException{
		String caminho = caminho_inicial;
		if (os.equals("win")) {
			caminho += "\\Compilador\\Arquivos\\" + cam + ".ccc";
		} else {
			caminho += "/Compilador/Arquivos/" + cam + ".ccc";
		}
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(caminho));
		buffWrite.write(texto);
        buffWrite.close();
	}
}
