/**
 * Classe dos Parametros da aplicacao
 */
public class ApplicationParameters {
	
	// ===  === === Parametros de Arquivos === === ===
	
	public static String fileDir = "C://Users//Anderson//eclipse-workspace//Projeto2IA//resources//"; // Diretorio para leitura ou gravacao de arquivos
	//public static String fileDir = "C:/Users/ACER/workspace/Projeto2IA/resources/"; // Diretorio para leitura ou gravacao de arquivos
	public static String fileToReadNameTrainning = "pendigits.tra"; // Nome do arquivo de leitura para treinamento (de preferencia com extencao)
	public static String fileToReadNameTest = "pendigits.tes"; // Nome do arquivo de leitura para teste (de preferencia com extencao)
	public static String fileToWriteName = "result.txt"; // Nome do arquivo de escrita (de preferencia com extencao)
	
	// ===  === === === === === === === === === ===
	
	// ===  === === Parametros da rede === === ===
	
	public static int qtInputLayer = 16; // Quantidade de neuronios da camada de entrada
	public static int qtOutputLayer = 10; // Quantidade de neuronios da camada de saida
	public static int qtHiddenLayer = (qtInputLayer+qtOutputLayer)/2; // Quantidade de neuronios da camada escondida (intermediaria) (16+10)/2 = 13
	
	public static double learningRate = 0.5; // Taxa de aprendizagem da rede neural
	public static int qtIterations = 3000; // Quantidade de iteracoes para executar o treinamento da rede
	
	// ===  === === === === === === === === === ===

}
