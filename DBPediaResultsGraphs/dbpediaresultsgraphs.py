#!/usr/bin/python

import sys
import csv
import matplotlib.pyplot


def print_usage():
    print("Usage:\n python dbpediaresultsgraphs.py comparison-results hist-confirmed hist-proposed-inferred-to-direct "
          "hist-proposed-new")
    print("\t comparison-results\n\t\t CSV file produced by LatticeAnalysis program to be analyzed")
    print("\t hist-confirmed\n\t\t histogram of values of confirmed relationships")
    print("\t hist-proposed-inferred-to-direct\n\t\t histogram of values of values proposed to be changed from inferred"
          " to direct")
    print("\t hist-proposed-new\n\t\t histogram of values of proposed new relationships")


def check_command_arguments():
    if len(sys.argv) != 5:
        print_usage()
        return False

    else:
        return True


def read_values_from_csv(csv_file):
    fp = open(csv_file, 'r')
    csvreader = csv.reader(fp)

    confirmed = []
    inferred = []
    new = []
    for line in csvreader:
        if line[0] == "CONFIRMED_DIRECT":
            confirmed.append(int(line[3]))
        elif line[0] == "PROPOSED_INFERRED_TO_DIRECT":
            inferred.append(int(line[3]))
        elif line[0] == "PROPOSED_NEW":
            new.append(int(line[3]))

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


if check_command_arguments():
    values_confirmed, values_inferred, values_new = read_values_from_csv(sys.argv[1])

    if max(values_confirmed) == 0:
        plot_histogram_to_file(values_confirmed, 1, "Values of confirmed relationships", sys.argv[2])
    else:
        plot_histogram_to_file(values_confirmed, max(values_confirmed), "Values of confirmed relationships", sys.argv[2])

    plot_histogram_to_file(values_inferred, 100, "Values of relationships to be changed from inferred to direct", sys.argv[3])
    plot_histogram_to_file(values_new, 100, "Values of new relationships", sys.argv[4])
