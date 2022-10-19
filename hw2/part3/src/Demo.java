
public class Demo {

	public static void main(String[] args) {
		System.out.println("-->> Calculation with wait() and notify()");
		MatrixDFTWithWait withWait = new MatrixDFTWithWait();
		withWait.calculate();

		System.out.println("\n\n-->> Calculation with Monitors");
		MatrixDFTWithMonitor withMonitor = new MatrixDFTWithMonitor();
		withMonitor.calculate();
	}
}
