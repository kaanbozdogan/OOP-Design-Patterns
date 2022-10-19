/**
 * Complex number class.
 */
public class Complex {

	/**
	 * Real poart of the complex number.
	 */
	private double real;
	/**
	 * Imaginary part of the complex part.
	 */
	private double imag;

	public Complex() {
		real = 0;
		imag = 0;
	}

	public Complex(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}

	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public double getImag() {
		return imag;
	}

	public void setImag(double imag) {
		this.imag = imag;
	}

	/**
	 * Sums up two complex numbers.
	 * @param e1 First number.
	 * @param e2 Second number.
	 * @return Summation.
	 */
	static public Complex sum(Complex e1, Complex e2) {
		return new Complex(e1.real + e2.real, e1.imag + e2.imag);
	}

	@Override
	public String toString() {
		if (imag < 0)
			return real + " - " + Math.abs(imag) + "i";
		else
			return real + " + " + imag + "i";
	}
}
