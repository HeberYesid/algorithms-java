package retrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TfIdf {

    private final List<Map<String, Double>> tfidfVectors = new ArrayList<>();
    private final Map<String, Double> idfMap = new HashMap<>();
    private final List<String> docs;

    public TfIdf(List<String> documents) {
        this.docs = new ArrayList<>(documents);
        buildIndex();
    }

    /** Tokeniza: minusculas, solo letras y digitos. */
    static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        for (String w : text.toLowerCase().split("\\W+")) {
            if (!w.isBlank()) {
                tokens.add(w);
            }
        }
        return tokens;
    }

    private void buildIndex() {
        int n = docs.size();
        List<Map<String, Integer>> rawTf = new ArrayList<>();
        Map<String, Integer> df = new HashMap<>();

        for (String doc : docs) {
            Map<String, Integer> freq = new HashMap<>();
            for (String t : tokenize(doc)) {
                freq.merge(t, 1, Integer::sum);
            }
            rawTf.add(freq);
            for (String t : freq.keySet()) {
                df.merge(t, 1, Integer::sum);
            }
        }

        for (Map.Entry<String, Integer> entry : df.entrySet()) {
            idfMap.put(entry.getKey(), Math.log((double) n / entry.getValue()));
        }

        for (Map<String, Integer> freq : rawTf) {
            tfidfVectors.add(buildVector(freq, idfMap));
        }
    }

    private static Map<String, Double> buildVector(Map<String, Integer> freq, Map<String, Double> idfMap) {
        int total = freq.values().stream().mapToInt(Integer::intValue).sum();
        Map<String, Double> vec = new HashMap<>();
        if (total == 0) {
            return vec;
        }

        for (Map.Entry<String, Integer> e : freq.entrySet()) {
            double tf = (double) e.getValue() / total;
            double idf = idfMap.getOrDefault(e.getKey(), 0.0);
            vec.put(e.getKey(), tf * idf);
        }
        return vec;
    }

    /** Similitud coseno entre dos vectores TF-IDF. */
    public static double cosineSimilarity(Map<String, Double> v1, Map<String, Double> v2) {
        double dot = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (Map.Entry<String, Double> e : v1.entrySet()) {
            double val2 = v2.getOrDefault(e.getKey(), 0.0);
            dot += e.getValue() * val2;
            norm1 += e.getValue() * e.getValue();
        }
        for (double v : v2.values()) {
            norm2 += v * v;
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0;
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /** Retorna indices de los k documentos mas similares. */
    public List<Integer> query(String q, int k) {
        Map<String, Integer> qFreq = new HashMap<>();
        for (String token : tokenize(q)) {
            qFreq.merge(token, 1, Integer::sum);
        }
        Map<String, Double> qVec = buildVector(qFreq, idfMap);

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < tfidfVectors.size(); i++) {
            indices.add(i);
        }

        indices.sort((a, b) -> Double.compare(
            cosineSimilarity(tfidfVectors.get(b), qVec),
            cosineSimilarity(tfidfVectors.get(a), qVec)
        ));

        return indices.subList(0, Math.min(k, indices.size()));
    }
}
