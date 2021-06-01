package os.pagereplacement.algorithm;

/**
 * Optimal: O algoritmo ótimo é o único que precisa ter acesso à cadeia de referencias de páginas para “prever” as páginas que serão
 * solicitadas. Sua implementação consiste em substituir a página que será referenciada o mais adiante possível a partir da página corrente
 * na cadeia.
 * 
 * 
 */
public class Optimal extends ReplacementAlgorithm {

	private final int[] pageReferenceString;

	public Optimal(int pageFrameSize, int[] pageReferenceString) {
		super(pageFrameSize, "Optimal");
		this.pageReferenceString = pageReferenceString;
	}

	@Override
	public int insert(int referencedPageIndex) {
		int pageNumber = pageReferenceString[referencedPageIndex];
		int frameIndex = tryBasicInsert(pageNumber);
		if (frameIndex != -1) {
			return frameIndex;
		}

		int[] frameCopy = frames.clone();
		int indexLastFrameFound = -1; // Elege o 1o frame para substituição caso não nenhum frame será referenciado adiante
		int framesFoundCount = 0;

		// Procura pela página que será referenciada o mais adiante possível:
		for (int i = referencedPageIndex + 1; i < pageReferenceString.length; i++) {
			// Verifica se a página está em algum frame:
			for (int j = 0; j < frameCopy.length; j++) {
				if (pageReferenceString[i] == frameCopy[j]) {
					indexLastFrameFound = j;
					framesFoundCount++;
					frameCopy[j] = -1;
					break;
				}
			}
			/*
			 * Pára a análise do vetor de referencias caso todos os frames da memória já tiverem 
			 * sido encontrados na cadeia de referencias, isto é, se framesCopy só contém -1:
			 */
			if (framesFoundCount == pageFrameSize) {
				break;
			}
		}
		
		//Se algum frame não for referenciado adiante, o seleciona para substituição:
		for (int i = 0; i < frameCopy.length; i++) {
			if (frameCopy[i] != -1) {
				indexLastFrameFound = i;
				break;
			}
		}
		int replacedFrameIndex = indexLastFrameFound;
		frames[indexLastFrameFound] = pageNumber;
		return replacedFrameIndex;
	}
}
