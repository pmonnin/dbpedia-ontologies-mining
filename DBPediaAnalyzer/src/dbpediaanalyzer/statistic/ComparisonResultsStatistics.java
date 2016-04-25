package dbpediaanalyzer.statistic;

import dbpediaanalyzer.comparison.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class ComparisonResultsStatistics {
    private ComparisonResultType type;
    private String uriPrefix;
    private int number;
    private Map<String, Double> averageValues;
    private Map<String, Double> minimumValues;
    private Map<String, Double> maximumValues;

    public ComparisonResultsStatistics(List<ComparisonResult> comparisonResults, ComparisonResultType type, String uriPrefix) {
        this.type = type;
        this.uriPrefix = uriPrefix;
        this.number = 0;
        this.averageValues = new HashMap<>();
        this.minimumValues = new HashMap<>();
        this.maximumValues = new HashMap<>();

        for(ComparisonResult result : comparisonResults) {
            if(result.getType() == type && result.getBottom().getUri().startsWith(uriPrefix)) {
                this.number++;

                for(Map.Entry<String, Double> value : result.getValues().entrySet()) {
                    if(!this.averageValues.containsKey(value.getKey())) {
                        this.averageValues.put(value.getKey(), value.getValue());
                        this.minimumValues.put(value.getKey(), value.getValue());
                        this.maximumValues.put(value.getKey(), value.getValue());
                    }

                    else {
                        if(value.getValue() < this.minimumValues.get(value.getKey())) {
                            this.minimumValues.put(value.getKey(), value.getValue());
                        }

                        if(value.getValue() > this.maximumValues.get(value.getKey())) {
                            this.maximumValues.put(value.getKey(), value.getValue());
                        }

                        this.averageValues.put(value.getKey(), this.averageValues.get(value.getKey()) + value.getValue());
                    }
                }
            }
        }

        if(this.number != 0) {
            HashMap<String, Double> temp = new HashMap<>();

            for(Map.Entry<String, Double> average : this.averageValues.entrySet()) {
                temp.put(average.getKey(), average.getValue() / (double) this.number);
            }

            this.averageValues = temp;
        }
    }

    public ComparisonResultType getType() {
        return this.type;
    }

    public String getUriPrefix() {
        return this.uriPrefix;
    }

    public int getNumber() {
        return this.number;
    }

    public Map<String, Double> getAverageValues() {
        return new HashMap<>(this.averageValues);
    }

    public Map<String, Double> getMinimumValues() {
        return new HashMap<>(this.minimumValues);
    }

    public Map<String, Double> getMaximumValues() {
        return new HashMap<>(this.maximumValues);
    }

}
