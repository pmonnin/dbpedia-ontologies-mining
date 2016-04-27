#!/usr/bin/python

import sys
import json
import matplotlib
# Use this only if your are on SSH session and don't have a graphic environment
# matplotlib.use('Agg')
import matplotlib.pyplot

__author__ = "Pierre Monnin"

classes_prefixes = {"DBCategories": "http://dbpedia.org/resource/Category",
                    "DBOntologyClasses": "http://dbpedia.org/ontology",
                    "DBYagoClasses": "http://dbpedia.org/class/yago"}

comparison_results_types = ["CONFIRMED_DIRECT", "PROPOSED_INFERRED_TO_DIRECT", "PROPOSED_NEW"]
strategies = ["NumberOfSubmissions", "AverageExtensionsRatio", "DistanceViaLCA"]

histograms_targets = {"NumberOfSubmissions": ["range"], "AverageExtensionsRatio": ["default"],
                      "DistanceViaLCA": ["default", "exclude-0-values"]}


def print_usage():
    print("Usage:\n python dbpediaresultsgraphs.py comparison-results output-prefix")
    print("\t comparison-results\n\t\t JSON file with knowledge comparison results produced by LatticeAnalysis program")
    print("\t output-prefix\n\t\t prefix to be used for output files")
    print("\t\t Each output file will be named according to the following pattern:")
    print("\t\t\t output-prefix-class-type-strategy.png")
    print("If there are 0 values for a class, a type and a strategy, related histogram is not produced")


def check_command_arguments():
    if len(sys.argv) != 3:
        print_usage()
        return False

    return True


def read_comparison_results_from_json(json_file):
    fp = open(json_file, 'r')
    json_objects = json.load(fp)

    fp.close()
    return json_objects


def get_values_from_comparison_results(json_values, class_prefix, comparison_result_type, strategy):
    values = []

    for v in json_values:
        if v["type"] == comparison_result_type and v["top"].startswith(class_prefix):
            values.append(float(v["values"][strategy]))

    return values


def histogram_target_to_bins(histogram_target, values):
    if histogram_target == "range":
        return range(1, int(max(values)) + 2)

    return 100


def histogram_target_to_values(histogram_target, values):
    if histogram_target == "exclude-0-values":
        return [x for x in values if x != 0.0]

    return values


def plot_histogram_to_file(values, histogram_target, title, histogram_file):
    matplotlib.pyplot.figure()
    matplotlib.pyplot.hist(histogram_target_to_values(histogram_target, values),
                           histogram_target_to_bins(histogram_target, values))
    matplotlib.pyplot.title(title)
    matplotlib.pyplot.xlabel("Values")
    matplotlib.pyplot.ylabel("Number")
    matplotlib.pyplot.savefig(histogram_file)
    matplotlib.pyplot.clf()


def main():
    if check_command_arguments():
        comparison_results = read_comparison_results_from_json(sys.argv[1])

        for class_name in classes_prefixes:
            for type in comparison_results_types:
                for strategy in strategies:
                    for histogram_target in histograms_targets[strategy]:
                        filtered_values = get_values_from_comparison_results(comparison_results,
                                                                             classes_prefixes[class_name], type, strategy)

                        if len(filtered_values) != 0:
                            title = "Values for " + type + " relationships on " + class_name + "\nStrategy " + strategy
                            file_name = sys.argv[2] + "-" + class_name + "-" + type + "-" + strategy

                            if histogram_target == "exclude-0-values":
                                title += " (0 values excluded)"
                                file_name += "-0-excluded"

                            plot_histogram_to_file(filtered_values, histogram_target, title, file_name)


main()
