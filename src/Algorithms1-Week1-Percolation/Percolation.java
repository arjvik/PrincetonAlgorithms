import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private final int SOURCE, DEST;

	private final int n;
	private final WeightedQuickUnionUF uf, backwash;
	private final boolean[][] open;

	private int openSites = 0;

	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		this.n = n;
		uf = new WeightedQuickUnionUF(n * n + 2);
		backwash = new WeightedQuickUnionUF(n * n + 2);
		open = new boolean[n][n];
		SOURCE = n * n;
		DEST = n * n + 1;
		for (int i = 0; i < n; i++) {
			uf.union(SOURCE, i);
			backwash.union(SOURCE, i);
		} for (int i = n * (n - 1); i < n * n; i++)
			uf.union(DEST, i);
	}

	public void open(int row, int col) {
		offsetOpen(row - 1, col - 1);
	}

	private void offsetOpen(int row, int col) {
		if (!(0 <= row && row < n && 0 <= col && col < n))
			throw new IllegalArgumentException();
		if (open[row][col])
			return;
		open[row][col] = true;
		int idx = idx(row, col);
		if (isValidAndOpen(row + 1, col))
			union(idx, idx(row + 1, col));
		if (isValidAndOpen(row - 1, col))
			union(idx, idx(row - 1, col));
		if (isValidAndOpen(row, col + 1))
			union(idx, idx(row, col + 1));
		if (isValidAndOpen(row, col - 1))
			union(idx, idx(row, col - 1));
		openSites++;
	}

	private void union(int p, int q) {
		uf.union(p, q);
		backwash.union(p, q);
	}

	private int idx(int row, int col) {
		return n * row + col;
	}

	private boolean isValidAndOpen(int row, int col) {
		return inRange(row) && inRange(col) && isOpenOffset(row, col);
	}

	private boolean inRange(int i) {
		return 0 <= i && i < n;
	}

	public boolean isOpen(int row, int col) {
		return isOpenOffset(row - 1, col - 1);
	}

	private boolean isOpenOffset(int row, int col) {
		if (!inRange(row) || !inRange(col))
			throw new IllegalArgumentException();
		return open[row][col];
	}

	public boolean isFull(int row, int col) {
		return isFullOffset(row - 1, col - 1);
	}

	private boolean isFullOffset(int row, int col) {
		return isOpenOffset(row, col) && backwash.connected(SOURCE, idx(row, col));
	}

	public int numberOfOpenSites() {
		return openSites;
	}

	public boolean percolates() {
		if (n == 1)
			return openSites == 1;
		else
			return uf.connected(SOURCE, DEST);
	}

	public static void main(String[] args) {
		int n = StdIn.readInt();
		Percolation p = new Percolation(n);
		while (StdIn.hasNextLine())
			p.open(StdIn.readInt(), StdIn.readInt());
		System.out.println(p.percolates() ? "Percolates!" : "Does not percolate.");
	}

}
