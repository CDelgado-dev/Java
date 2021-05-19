import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The FormatChecker class is used to check the data in the files are formatted
 * in the correct way.
 * 
 * @author cesardelgado
 */
public class FormatChecker {
	/**
	 * Prints weather the file is VALID or INVALID.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java FormatChecker file1 [file2 ...]");
			System.exit(1);
		}
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
			formatCheck(args[i]);
		}
	}

	/**
	 * Checks each file and processes it through each method to verify
	 * if the file is in a VALID or INVALID format.
	 * 
	 * @param file
	 */
	public static void formatCheck(String file) {
		try {
			Scanner scan = new Scanner(new File(file));
			checkCharacters(file);
			firstRowCheck(file);
			rowChecker(file);
			columnChecker(file);
			System.out.println("VALID");
			System.out.println();
			scan.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			System.out.println("INVALID");
			System.out.println();
		} catch (NumberFormatException e) {
			System.out.println(e.toString());
			System.out.println("INVALID");
			System.out.println();
		} catch (InputMismatchException e) {
			System.out.println(e.toString());
			System.out.println("INVALID");
			System.out.println();
		}
	}

	/**
	 * This method checks if there is values other that digits present in the file.
	 * 
	 * @param file
	 * @throws FileNotFoundException, NUmberFormatException
	 */
	public static void checkCharacters(String file) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(file));
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			Scanner lineScan = new Scanner(line);
			while (lineScan.hasNext()) {
				String token = lineScan.next();
				if (token.matches("\\D")) { // excluded values that resulted in non-digit
					System.out.println("It cannot contain anything other than numbers within the file.");
					throw new NumberFormatException();
				}
			}
			lineScan.close();
		}
		scan.close();
	}

	/**
	 * Scans the first row of the file and checks if the correct specifications are met to process the file.
	 * 
	 * @param file
	 * @throws FileNotFoundException, InputMismatchException
	 */
	public static void firstRowCheck(String file) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(file));
		String firstRow = scan.nextLine().replaceAll("\\s+", ""); // replaces all whitespace characters
		if (firstRow.length() != 2) {
			System.out.println("There should only be two integers allowed in the first row.");
			throw new InputMismatchException();
		}
	}

	/**
	 *Checks the number of rows scanned as well as the amount of rows wanted
	 * in file and compared the two values.
	 * 
	 * @param file
	 * @throws FileNotFoundException, InputMismatchException
	 */
	@SuppressWarnings("resource")
	public static void rowChecker(String file) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(file));
		int rowCount = 0;
		int rows = scan.nextInt();
		scan.nextLine();
		while (scan.hasNext()) {
			scan.nextLine();
			rowCount++;
		}
		if ((rowCount) != rows) {
			System.out.println("The amount of rows are smaller/bigger than what the file is specifiying");
			throw new InputMismatchException();
		}
		scan.close();
	}

	/**
	 * Checks the number of columns scanned as well as the amount of columns wanted
	 * in file and compares the two values.
	 * 
	 * @param file
	 * @throws FileNotFoundException, InputMismatchException
	 */
	public static void columnChecker(String file) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(file));
		int rows = scan.nextInt(); // needed to count first integer of that specifies rows and store it to read the column.
		int columns = scan.nextInt();
		scan.nextLine();
		while (scan.hasNext()) {
			String[] line = scan.nextLine().split(" ");
			if (columns != line.length) {
				System.out.println("The amount of columns are smaller/bigger than what the file is specifiying.");
				throw new InputMismatchException();
			}
		}
		scan.close();
	}
}
