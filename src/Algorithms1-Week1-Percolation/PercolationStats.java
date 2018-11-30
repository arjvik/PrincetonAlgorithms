import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private final int n;
	private final double mean, stddev, confidenceHi, confidenceLo;

	public PercolationStats(int n, int trials) {
		this.n = n;
		if (n <= 0 || trials <= 0)
			throw new IllegalArgumentException();
		double[] results = new double[trials];
		for (int trial = 0; trial < trials; trial++) {
			results[trial] = runTrial();
		}
		mean = StdStats.mean(results);
		stddev = StdStats.stddev(results);
		confidenceLo = mean - (1.96 * stddev) / Math.sqrt(trials);
		confidenceHi = mean + (1.96 * stddev) / Math.sqrt(trials);
	}

	private double runTrial() {
		int[] indexes = range();
		Percolation p = new Percolation(n);
		StdRandom.shuffle(indexes);
		int i = 0;
		while (!p.percolates()) {
			int idx = indexes[i++];
			p.open(idx / n + 1, idx % n + 1);
		}
		return p.numberOfOpenSites() / (double) (n * n);
	}

	private int[] range() {
		int[] range = new int[n * n];
		for (int i = 0; i < n * n; i++) {
			range[i] = i;
		}
		return range;
	}

	public double mean() {
		return mean;
	}

	public double stddev() {
		return stddev;
	}

	public double confidenceLo() {
		return confidenceLo;
	}

	public double confidenceHi() {
		return confidenceHi;
	}

	public static void main(String[] args) {
		int n, t;
		if (args.length == 2) {
			n = Integer.parseInt(args[0]);
			t = Integer.parseInt(args[1]);
		} else {
			System.out.println("Usage: java PercolationStats <n> <trials>");
			System.out.print("n=");
			n = StdIn.readInt();
			System.out.print("t=");
			t = StdIn.readInt();
		}
		PercolationStats ps = new PercolationStats(n, t);
		System.out.printf("%-23s = %f%n%-23s = %f%n%-23s = [%f, %f]%n", "mean", ps.mean(), "stddev", ps.stddev(),
				"95% confidence interval", ps.confidenceLo(), ps.confidenceHi());
	}
}