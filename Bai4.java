import java.io.*;
import java.util.*;
import java.util.Vector;


public class Bai4 {

    public static Map<String, Integer> vocab = new HashMap();
    public static Map<String, Integer> corpus = new HashMap();
    public static Map<String, Integer> pairCorpus = new HashMap();
    public static Double[] probs;
    public static Double[][] conditionalProbs;

    public static void readFile() {
        try {
            Vector lines = new Vector();
            File file = new File("UIT-ViOCD/UIT-ViOCD.txt");
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                lines.addElement(line);
            }
            fileScanner.close();

            for (int idx = 0; idx < lines.size(); idx++) {
                String line = (String) lines.get(idx);
                line = line.replace("\n", "").replace("\r", "").replace("\t", "");
                line = line.replaceAll("^\\s+", "");
                line = line.replaceAll("\\s+$", "");
                line = line.toLowerCase();

                String[] words = line.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    if (!vocab.containsKey(word)) {
                        vocab.put(word, vocab.size());
                    }

                    corpus.put(word, corpus.getOrDefault(word, 0) + 1);
                }

                for (int i = 0; i < words.length - 1; i++) {
                    String words_ij = words[i] + "_" + words[i+1];
                    pairCorpus.put(words_ij, pairCorpus.getOrDefault(words_ij, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }

    public static void constructSingleProb() {
        int totalWords = 0;
        for (Iterator it = corpus.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry item = (Map.Entry) it.next();
            totalWords += (Integer) item.getValue();
        }

        probs = new Double[vocab.size()];
        for (Iterator it = corpus.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry item = (Map.Entry) it.next();
            String word = (String) item.getKey();
            Integer wordCount = (Integer) item.getValue();
            Integer wordId = (Integer) vocab.get(word);
            probs[wordId] = (double) wordCount / totalWords;
        }
    }

    public static void constructConditionalProb() {
        int totalPairsOfWords = 0;
        for (Iterator it = pairCorpus.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry item = (Map.Entry) it.next();
            totalPairsOfWords += (Integer) item.getValue();
        }

        Double[][] jointProbs = new Double[vocab.size()][vocab.size()];
        for (int i = 0; i < vocab.size(); i++) {
            for (int j = 0; j < vocab.size(); j++) {
                jointProbs[i][j] = 1e-20;
            }
        }

        for (Iterator it = pairCorpus.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry item = (Map.Entry) it.next();
            String key = (String) item.getKey();
            String[] pair = key.split("_");
            if (pair.length != 2) continue;

            String word_i = pair[0];
            String word_j = pair[1];
            int wordId_i = (Integer) vocab.get(word_i);
            int wordId_j = (Integer) vocab.get(word_j);

            jointProbs[wordId_i][wordId_j] = (double) ((Integer) item.getValue()) / totalPairsOfWords;
        }

        conditionalProbs = new Double[vocab.size()][vocab.size()];
        for (int i = 0; i < vocab.size(); i++) {
            for (int j = 0; j < vocab.size(); j++) {
                if (probs[i] > 0) {
                    conditionalProbs[i][j] = jointProbs[i][j] / probs[i];
                } else {
                    conditionalProbs[i][j] = 1e-20;
                }
            }
        }
    }

    public static void training() {
        constructSingleProb();
        constructConditionalProb();
    }

    public static Vector inferring(String w0) {
        Vector words = new Vector();
        words.add(w0);

        Integer w0Idx = (Integer) vocab.get(w0);
        Double logProbs = -Math.log(probs[w0Idx]);
        for (int t = 1; t <= 5; t++) {
            String maxWord = "";
            Double maxLogProb = Double.NEGATIVE_INFINITY;
            Integer w1Idx = 0;

            for (Iterator it = vocab.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry item = (Map.Entry) it.next();
                Integer candidateIdx = (Integer) item.getValue();
                Double prob = conditionalProbs[w0Idx][candidateIdx];
                Double candidateLogProb = -Math.log(prob);

                if (logProbs + candidateLogProb > maxLogProb) {
                    maxLogProb = logProbs + candidateLogProb;
                    maxWord = (String) item.getKey();
                    w1Idx = candidateIdx;
                }
            }

            logProbs += maxLogProb;
            words.add(maxWord);
            w0Idx = w1Idx;
        }

        return words;
    }

    public static void main(String[] args) throws Exception {
        readFile();
        training();
        Vector predicted_words = inferring("hàng");
        StringBuilder sentence = new StringBuilder();
        for (int i = 0; i < predicted_words.size(); i++) {
            sentence.append(predicted_words.get(i));
            if (i < predicted_words.size() - 1) sentence.append(" ");
        }
        System.out.println(sentence.toString());
    }
}
//     public static void main(String[] args) {
//         try (Scanner scanner = new Scanner(System.in)) {