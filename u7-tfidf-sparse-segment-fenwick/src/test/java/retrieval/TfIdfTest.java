package retrieval;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TfIdfTest {

    @Test
    void queryRanksMostRelevantDocumentFirst() {
        List<String> docs = List.of(
            "gato negro corre rapido",
            "perro ladra fuerte",
            "gato come pescado fresco"
        );
        TfIdf tfidf = new TfIdf(docs);

        List<Integer> ranked = tfidf.query("gato pescado", 2);
        assertEquals(2, ranked.get(0));
    }

    @Test
    void cosineSimilaritySpecialCases() {
        Map<String, Double> v = Map.of("a", 1.0, "b", 2.0);
        Map<String, Double> orth = Map.of("c", 1.0);

        assertEquals(1.0, TfIdf.cosineSimilarity(v, v), 1e-9);
        assertEquals(0.0, TfIdf.cosineSimilarity(v, orth), 1e-9);
    }
}
