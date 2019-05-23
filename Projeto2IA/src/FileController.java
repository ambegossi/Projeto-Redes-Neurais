import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Classe controladora de Arquivos
 */
public class FileController {

	/**
	 * Efetua a leitura do arquivo e o tratamento dos dados lidos
	 * @param neuralNetwork - Instancia da classe controladora da rede neural
	 */
	public void readFile(NeuralNetworkController neuralNetwork, String fileToReadName, boolean test) {

		try {
			String file = ApplicationParameters.fileDir + fileToReadName;
			
			// Abre o arquivo para leitura
			BufferedReader br = new BufferedReader(new FileReader(file));

			// Efetua leitura linha a linha
			while (br.ready()) {

				// le a linha do arquivo
				String line = br.readLine();
				
				// Processa os dados lidos da linha do arquivo
				neuralNetwork.processInput(line, test);
			}

			// Fecha o arquivo
			br.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();

			// Exibe mensagem de informando que ocorreu problema caso ocorra falha
			JOptionPane.showMessageDialog(null, "Problemas na leitura do arquivo.");
		}
	}

	/**
	 * Grava um arquivo de texto
	 */
	public void writeFile(int[][] matrixConfusion) {

		System.out.println("Começou a escrita do arquivo.");
		try {
			String file = ApplicationParameters.fileDir + ApplicationParameters.fileToWriteName;
			
			// Abre o arquivo para escrita (cria caso nao exista, se existe sobreescreve)
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			// Pega a data e hora atual e formata
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date dateAndHour = new Date();
			String strDateAndHour = sdf.format(dateAndHour);

			bw.newLine();
			bw.write(" ----- Matriz de Confusão ----- ");
			bw.newLine();
			bw.newLine();
			
			for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
				for (int j=0; j<ApplicationParameters.qtOutputLayer; j++) {
					bw.write("  " + matrixConfusion[i][j]);
				}
				bw.newLine();
			}
			
			bw.newLine();
			bw.write(" Data/Hora: " + strDateAndHour);
			bw.newLine();

			// Fecha o arquivo
			bw.close();

			// Exibe mensagem de informação que o Arquivo foi gravado
			//JOptionPane.showMessageDialog(null, "Arquivo gravado com sucesso.");

		} catch (IOException ioe) {
			ioe.printStackTrace();

			// Exibe mensagem de informando que ocorreu problema caso ocorra falha
			JOptionPane.showMessageDialog(null, "Problemas na gravação do arquivo.");
		}
		System.out.println("Terminou a escrita do arquivo.");
	}

}
