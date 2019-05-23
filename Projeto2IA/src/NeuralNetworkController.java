import java.util.Random;

/**
 * Classe controladora da Rede Neural
 */
public class NeuralNetworkController {
	
	// Matrizes de pesos
	private double[][] matrixWeightsIn = new double[ApplicationParameters.qtInputLayer][ApplicationParameters.qtHiddenLayer];
	private double[][] matrixWeightsOut = new double[ApplicationParameters.qtHiddenLayer][ApplicationParameters.qtOutputLayer];
	// vetor com os valores das saidas dos neuronios da camada escondida
	private double[] outputHiddenLayer = new double[ApplicationParameters.qtHiddenLayer];
	// vetor com os valores das saidas da rede neural (saida da camada de saida)
	private double[] outputNeuralNetwork = new double[ApplicationParameters.qtOutputLayer];
	// Matriz de confusao
	private int[][] matrixConfusion = new int[ApplicationParameters.qtOutputLayer][ApplicationParameters.qtOutputLayer];
	
	private int count = 0;
	private int countLog= 0;
	
	/**
	 * Construtor
	 */
	public NeuralNetworkController() {
		start();
	}
	
	/**
	 * Inicia a rede neural
	 */
	private void start() {
		
		// Inicializacao dos pesos
		for (int i=0; i<ApplicationParameters.qtInputLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtHiddenLayer; j++) {
				matrixWeightsIn[i][j] = getRandomWeight();
			}
		}
		for (int i=0; i<ApplicationParameters.qtHiddenLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtOutputLayer; j++) {
				matrixWeightsOut[i][j] = getRandomWeight();
			}
		}
		
		// Executa o treinamento da rede pela quantidade de vezes definida no parametro 'qtIterations'
		int iter = 0;
		while (iter<ApplicationParameters.qtIterations) {
			count = 0;
			// Leitura do arquivo
			FileController fileController = new FileController();
			fileController.readFile(this, ApplicationParameters.fileToReadNameTrainning, false);
			iter++;
		}
		
		// 7494 linhas no arquivo de treinamento
		// 3498 linhas no arquivo de teste
		
		count = 0;
		// Executa o teste da rede uma vez
		// Leitura do arquivo
		FileController fileController = new FileController();
		fileController.readFile(this, ApplicationParameters.fileToReadNameTest, true);
		
		// Escreve a matriz de confusao no arquivo
		fileController.writeFile(matrixConfusion);
		
		System.out.println("\n");
		
		// Escreve a matriz de confusao no console
		for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtOutputLayer; j++) {
				System.out.println(matrixConfusion[i][j]);
			}
			System.out.println("\n");
		}
	}
	
	/**
	 * Processa as informações de entrada
	 * @param line - linha lida do arquivo
	 */
	public void processInput(String line, boolean test) {
		
		count++;
		
		// ========== ETAPA INICIAL:       1) Tratamento das entradas da rede         ==========
		// ========== 2) e calculo das saidas de cada neuronoo de cada camada da rede ==========
		
		// 1) Trata as informacoes lidas da linha do arquivo
		// 2) Normaliza as entradas
		// 3) Faz o somatorio das entradas multiplicadas pelos pesos,
		//    que vai ser a entrada da camada escondida
		// 4) Gera a saida do neuronio pela funcao sigmoidal (saida da camada escondida)
		// 5) A saida obtida da camada escondida vai ser a entrada da camada de saida
		//    repetir o processo para esta camada:
		//    - Faz o somatorio das entradas multiplicadas pelos pesos,
		//    que vai ser a entrada da camada de saida
		//    - Gera a saida do neuronio pela funcao sigmoidal 
		//    (saida da camada de saida, ou seja, saida final)
		
		// ========== 1 ==========
		
		//System.out.println(line);
		String[] coordinatesRead = line.split(","); // coordenadas
		String classificator = coordinatesRead[coordinatesRead.length-1]; // classificador (ultima posicao)
		//System.out.println("\n" + classificator + "\n");
		
		String strAux1 = classificator.replaceAll(" ", ""); // remove espaços para converter para inteiro
		int classificatorInt = Integer.parseInt(strAux1); // converte o classificador string para inteiro
		//System.out.println("classificador: " + classificatorInt);
		
		float[] coordinates = new float[coordinatesRead.length-1];
		for (int i=0; i<coordinatesRead.length-1; i++) {
			String strAux2 = coordinatesRead[i].replaceAll(" ", ""); // remove espaços para converter para inteiro
			coordinates[i] = Integer.parseInt(strAux2); // converte a coordenada string para inteiro
			//System.out.println(coordinates[i]);
			
			// ========== 2 ==========
			
			coordinates[i] = coordinates[i] / 100; // normaliza as entradas
			//System.out.println(coordinates[i]);
		}
		
		// ========== 3 ==========
		
		double sum = 0; // somatorio
		
		for (int i=0; i<ApplicationParameters.qtHiddenLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtInputLayer; j++) {
				sum += coordinates[j] * matrixWeightsIn[j][i];
			}
			
			// ========== 4 ==========
			
			outputHiddenLayer[i] = sigmoidal(sum);
			//System.out.println(outputHiddenLayer[i]);
		}
		
		// ========== 5 ==========
		
		sum = 0; // somatorio
		
		for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtHiddenLayer; j++) {
				sum += outputHiddenLayer[j] * matrixWeightsOut[j][i];
			}
			if (!test) {
				outputNeuralNetwork[i] = sigmoidal(sum);
			} else {
				//outputNeuralNetwork[i] = rigida(sum);
				outputNeuralNetwork[i] = sigmoidal(sum);
			}
			//System.out.println(outputNeuralNetwork[i]);
		}
		//System.out.println("\n");
		
		// ========== PROXIMA ETAPA: Calculo do erro ==========
		
		// 1) Calculo do fator de erro da saida (fatorErro = saidaEsperada - saidaAtual do neuronio)
		// 2) Calculo do erro da saida (Neuronio.Erro = Neuronio.Saida * (1 - Neuronio.Saida) * FatorErro)
		// 3) Calculo do fator de erro da camada escondida (Somatorio do erro*peso)
		// 4) Calculo do erro da camada escondida (Neuronio.Erro = Neuronio.Saida * (1 - Neuronio.Saida) * FatorErro)
		
		// ========== 1 ==========
		
		// Fator de erro de cada neuronio de saida
		//double[] outErrorFactor = new double[ApplicationParameters.qtOutputLayer];
		double[] outErrorFactor = {0d,0d,0d,0d,0d,0d,0d,0d,0d,0d};
		for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
			int outWanted = 0; // saida esperada
			if (i == classificatorInt) {
				outWanted = 1;
			}
			outErrorFactor[i] += outWanted - outputNeuralNetwork[i];
		}
		
		// ========== 2 ==========
		
		// Erro de cada neuronio de saida
		//double[] outError = new double[ApplicationParameters.qtOutputLayer];
		double[] outError = {0d,0d,0d,0d,0d,0d,0d,0d,0d,0d};
		for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
			outError[i] = outputNeuralNetwork[i] * (1 - outputNeuralNetwork[i]) * outErrorFactor[i];
		}
		
		// ========== 3 ==========
		
		// Fator de erro de cada neuronio da camada escondida
		//double[] hiddenErrorFactor = new double[ApplicationParameters.qtHiddenLayer];
		double[] hiddenErrorFactor = {0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d};
		for (int i=0; i<ApplicationParameters.qtHiddenLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtOutputLayer; j++) {
				hiddenErrorFactor[i] += (outError[j] * matrixWeightsOut[i][j]);
			}
		}
		
		// ========== 4 ==========
		
		// Erro de cada neuronio da camada escondida
		//double[] hiddenError = new double[ApplicationParameters.qtHiddenLayer];
		double[] hiddenError = {0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d};
		// Calculo do erro de cada neuronio da camada escondida
		for (int i=0; i<ApplicationParameters.qtHiddenLayer; i++) {
			hiddenError[i] = outputHiddenLayer[i] * (1- outputHiddenLayer[i]) * hiddenErrorFactor[i];
		}
		
		// ========== PROXIMA ETAPA: Ajuste dos pesos ==========
		
		// 1) Os pesos de todas as conexoes da rede neural devem ser ajustados
		// Novo_peso = Peso_anterior + Taxa de aprendizagem * Saida do neuronio anterior * Erro
		
		// ========== 1 ==========
		
		for (int i=0; i<ApplicationParameters.qtInputLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtHiddenLayer; j++) {
				matrixWeightsIn[i][j] = matrixWeightsIn[i][j] + ApplicationParameters.learningRate
						* coordinates[i] * hiddenError[j];
			}
		}
		for (int i=0; i<ApplicationParameters.qtHiddenLayer; i++) {
			for (int j=0; j<ApplicationParameters.qtOutputLayer; j++) {
				matrixWeightsOut[i][j] = matrixWeightsOut[i][j] + ApplicationParameters.learningRate
						* outputHiddenLayer[i] * outError[j];
			}
		}
		
		// Se for etapa de teste, montar matriz de confusao
		if (test) {
			double maxValue = getMaxValue(outputNeuralNetwork);
			if (outputNeuralNetwork[classificatorInt] == maxValue) { // if (outputNeuralNetwork[classificatorInt] == 1d) {
				matrixConfusion[classificatorInt][classificatorInt] += 1;
			} else {
				for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
					if (outputNeuralNetwork[i] == maxValue) { // if (outputNeuralNetwork[i] == 1d) {
						matrixConfusion[classificatorInt][i] += 1;
					}
				}
			}
			
			// Escreve no console as saidas da rede neural na etapa de teste
			if (count == 3498) {
				for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
					System.out.println(outputNeuralNetwork[i]);
				}
				System.out.println("\n");
			}	
		} else {
			// Escreve no console as saidas da rede neural na etapa de treinamento
			if (count == 7494 && countLog < 10) {
				for (int i=0; i<ApplicationParameters.qtOutputLayer; i++) {
					System.out.println(outputNeuralNetwork[i]);
				}
				System.out.println("\n");
				countLog ++;
			}
		}
	}
	
	private double getMaxValue(double[] out) {
		double returnValue = out[0];
		for (int i=1; i<out.length-1; i++) {
			if (returnValue < out[i]) {
				returnValue = out[i];
			}
		}
		return returnValue;
	}
	
	/**
	 * Funcao Sigmoidal
	 * saida = 1 / (1 + Exp(-somatorio))
	 * @param sum - somatorio
	 * @return valor da saida
	 */
	private double sigmoidal(double sum) {
		return (1 / (1 + Math.exp(-sum)));
	}
	
	/**
	 * Funcao Rigida
	 * usada apenas na fase de testes e na saída da rede neural
	 * @param sum - somatorio
	 * @return valor da saida
	 */
	/*private double rigida(double sum) {
		double saida = 0d;
		if (sum > 0.5) {
			saida = 1d;
		}
		return saida;
	}*/
	
	/**
	 * Obtem um peso aleatorio
	 * @return um valor aleatorio entre 0 e 1 (maior que 0 e menor que 1)
	 */
	private double getRandomWeight() {
		
		Random random = new Random();
		double value = random.nextDouble(); // returns the random float number between 0.0d (inclusive) and 1.0d(exclusive).
		// Como nao se deseja valor zero, se o valor for zero, atribui-se o valor 0.01
		if (value == 0.0d) {
			value = 0.01d;
		}
		return value;
	}
}
