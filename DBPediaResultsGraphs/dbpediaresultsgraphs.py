#!/usr/bin/python

import sys
import csv
import matplotlib.pyplot

possible_strategies = ["NumberOfSubmissions", "AverageExtensionsRatio", "DistanceFromLCA"]


def print_usage():
    print("Usage:\n python dbpediaresultsgraphs.py comparison-results hist-confirmed strategy-confirmed "
          "hist-proposed-inferred-to-direct strategy-proposed-inferred-to-direct "
          "hist-proposed-new strategy-proposed-new")
    print("\t comparison-results\n\t\t CSV file produced by LatticeAnalysis program to be analyzed")
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


def read_values_from_csv(csv_file):
    fp = open(csv_file, 'r')
    csvreader = csv.reader(fp)

    confirmed = []
    inferred = []
    new = []
    for line in csvreader:
        if line[0] == "CONFIRMED_DIRECT":
            confirmed.append(float(line[3]))
        elif line[0] == "PROPOSED_INFERRED_TO_DIRECT":
            inferred.append(float(line[3]))
        elif line[0] == "PROPOSED_NEW":
            new.append(float(line[3]))

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
    values_confirmed, values_inferred, values_new = read_values_from_csv(sys.argv[1])

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
