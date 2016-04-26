#!/usr/bin/python

import sys
import json
import matplotlib
# Use this only if your are on SSH session and don't have a graphic environment
# matplotlib.use('Agg')
import matplotlib.pyplot

__author__ = "Pierre Monnin"

possible_strategies = ["NumberOfSubmissions", "AverageExtensionsRatio", "DistanceFromLCA"]


def print_usage():
    print("Usage:\n python dbpediaresultsgraphs.py comparison-results hist-confirmed strategy-confirmed "
          "hist-proposed-inferred-to-direct strategy-proposed-inferred-to-direct "
          "hist-proposed-new strategy-proposed-new")
    print("\t comparison-results\n\t\t JSON file produced by LatticeAnalysis program to be analyzed")
    print("\t hist-confirmed\n\t\t histogram of values of confirmed relationships")
    print("\t strategy-confirmed\n\t\t strategy used during analysis to evaluate confirmed relationships")
    print("\t hist-proposed-inferred-to-direct\n\t\t histogram of values of relationships proposed to be changed from "
          "inferred to direct")
    print("\t strategy-proposed-inferred-to-direct\n\t\t strategy used during analysis to evaluate relationships "
          "proposed to be changed from inferred to direct")
    print("\t hist-proposed-new\n\t\t histogram of values of proposed new relationships")
    print("\t strategy-proposed-new\n\t\t strategy used during analysis to evaluate proposed new relationships")
    print("Possible values for strategies: NumberOfSubmissions, AverageExtensionsRatio, DistanceFromLCA")
    print("If there are 0 values for a relationship type, related histogram is not produced")


def check_command_arguments():
    correct_arguments = len(sys.argv) == 8 and \
                        sys.argv[3] in possible_strategies and \
                        sys.argv[5] in possible_strategies and \
                        sys.argv[7] in possible_strategies

    if not correct_arguments:
        print_usage()

    return correct_arguments


def read_values_from_json(json_file):
    fp = open(json_file, 'r')
    json_values = json.load(fp)

    confirmed = []
    inferred = []
    new = []
    for o in json_values:
        if o['type'] == "CONFIRMED_DIRECT":
            confirmed.append(float(o['value']))
        elif o['type'] == "PROPOSED_INFERRED_TO_DIRECT":
            inferred.append(float(o['value']))
        elif o['type'] == "PROPOSED_NEW":
            new.append(float(o['value']))

    fp.close()
    return confirmed, inferred, new


def plot_histogram_to_file(values, bins, title, histogram_file):
    matplotlib.pyplot.figure()
    matplotlib.pyplot.hist(values, bins)
    matplotlib.pyplot.title(title)
    matplotlib.pyplot.xlabel("Values")
    matplotlib.pyplot.ylabel("Number")
    matplotlib.pyplot.savefig(histogram_file)
    matplotlib.pyplot.clf()


def strategy_values_to_hist_bins(strategy, values):
    if strategy == possible_strategies[0]:
        return range(1, int(max(values)) + 2)

    return 100


if check_command_arguments():
    values_confirmed, values_inferred, values_new = read_values_from_json(sys.argv[1])

    # Plotting confirmed relationships histogram
    if len(values_confirmed) != 0:
        plot_histogram_to_file(values_confirmed, strategy_values_to_hist_bins(sys.argv[3], values_confirmed),
                               "Values of confirmed relationships", sys.argv[2])

    # Plotting relationships changed from inferred to direct histogram
    if len(values_inferred) != 0:
        plot_histogram_to_file(values_inferred, strategy_values_to_hist_bins(sys.argv[5], values_inferred),
                               "Values of relationships to be changed from inferred to direct", sys.argv[4])

    # Plotting new relationships histogram
    if len(values_new) != 0:
        plot_histogram_to_file(values_new, strategy_values_to_hist_bins(sys.argv[7], values_new),
                               "Values of new relationships", sys.argv[6])
