/**
 * Matrix of complex numbers.
 */
public class Matrix {

	/**
	 * Matrix.
	 */
	private Complex[][] data;
	/**
	 * Matrix sizes.
	 */
	private int rowS, colS;

	public Matrix (int rowS, int colS) {
		this.rowS = rowS;
		this.colS = colS;

		data = new Complex[rowS][colS];
		fillMatrix();
	}

	/**
	 * fills the matrix with random numbers.
	 */
	private void fillMatrix() {
		for(int i = 0; i < rowS; i++) {
			for(int j = 0; j < colS; j++) {
				data[i][j] = new Complex((int) (Math.random() * 9) + 1, (int) (Math.random() * 9) + 1);
			}
		}
	}

	/**
	 * Get the complex number in indexes.
	 * @param i Row index.
	 * @param j Column index.
	 * @return The complex number in indexes.
	 */
	public Complex get(int i, int j) {
		return data[i][j];
	}

	/**
	 * Set the complex number in indexes.
	 * @param i Row index.
	 * @param j Column index.
	 * @param n Value to be set.
	 */
	public void set(int i, int j, Complex n) {
		this.data[i][j].setReal(n.getReal());
		this.data[i][j].setImag(n.getImag());
	}

	public void print() {
		for(int i = 0; i < rowS; i++) {
			for(int j = 0; j < colS; j++) {
				System.out.print("(" + data[i][j].toString() + "), ");
			}
			System.out.println();
		}
	}
}