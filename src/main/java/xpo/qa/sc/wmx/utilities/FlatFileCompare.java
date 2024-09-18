package xpo.qa.sc.wmx.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import xpo.qa.common.FileUtils;

/**
 * Class for comparing various types of flat files. The concept of this is to
 * have a file of instructions on how to mask things out of each file, apply the
 * instructions to both the files, then compare them.
 * 
 * INSTRUCTIONS
 * 
 * All lines - Replace characters on all lines between start and end position
 * <ALL>, start position, end position (ignores sequence for all lines)
 * 
 * Line Numbers - Replace characters between a start and end position on a
 * specific line. <LN>, line number, start position, end position
 * 
 * Segments - Replace characters on a segment in a file. Segment is between some
 * kind of delimiter. <SEG>, segment name, start position, end position
 * <SEGTAB>, segment name, segment location, string size <SEGCUST>, custom
 * separator, segment name, start position, number of mask characters <SEGCONT>,
 * custom separator, string segment contains, start position, number of mask
 * characters <SEGLEN>, characters, line length
 * 
 * Line Contents - If line contains characters, then do replacement <LINCONT>,
 * find characters, start position, end position <LINCONTPOS>, characters, start
 * position, end position, replace start position, replace end position
 * 
 * Line Length - Line contains certain characters gets put at certain length
 * <LINLEN>, characters, line length
 * 
 * Byte Order Mark (BOM) - Remove this if it exists in the file <BOM>
 * 
 * EXAMPLE
 * 
 * <LN>,1,5,10 <LINCONT>,TR201,8,22
 *
 * @author Geoff Raetz
 * @date 7/15/14
 */
public class FlatFileCompare {

	private static char maskChar = 'X';
	private static final String maskTypeLineNumber = "<LN>";
	private static final String maskTypeSegment = "<SEG>";
	private static final String maskTypeSegmentTab = "<SEGTAB>";
	private static final String maskTypeSegmentCust = "<SEGCUST>";
	private static final String maskTypeSegmentContains = "<SEGCONT>";
	private static final String maskTypeAll = "<ALL>";
	private static final String maskTypeContains = "<CONT>";
	private static final String maskTypeLineCont = "<LINCONT>";
	private static final String maskTypeLength = "<LINLEN>";
	private static final String maskTypeBOM = "<BOM>";
	private static final String maskTypeLineContPos = "<LINCONTPOS>";
	private static final String maskTypeSegLength = "<SEGLEN>";

	/**
	 * Compare contents of two files after applying some rules for masking out
	 * parts of the file. Keeping this for compatibility.
	 * 
	 * @author Geoff Raetz
	 * @date 8/10/2015
	 * 
	 * @param control Control file full path
	 * @param test Test file full path
	 * @param listFile Mask file full path
	 * @return List of differences in the control and test file
	 * 
	 * @throws MaskFileException
	 */
	public static String compare(String control, String test, String listFile) throws MaskFileException, IOException {
		try {
			return compare(control, test, listFile, "Cp1252");
		} catch (MaskFileException | IOException e) {
			throw e;
		}
	}

	/**
	 * Compare contents of two files after applying some rules for masking out
	 * parts of the file.
	 * 
	 * @author Geoff Raetz
	 * @date 8/10/2015
	 * 
	 * @param control Control file full path
	 * @param test Test file full path
	 * @param listFile Mask file full path
	 * @param format The format of the test/control file (i.e. UTF-8)
	 * @return List of differences in the control and test file
	 * 
	 * @throws MaskFileException
	 */
	public static String compare(String control, String test, String listFile, String format)
		throws MaskFileException, IOException {
		boolean maskFileExists = false;
		List<String> maskList = new ArrayList<String>();
		List<String> controlContents;
		List<String> testContents;
		File controlFile = new File(control);
		File testFile = new File(test);

		try {
			//Get list of masks, if it does not exist we still want to continue
			try {
				maskList = fileToList(listFile);
				maskFileExists = true;
			} catch (Exception e) {
				maskFileExists = false;
			}

			//Change EDI files formats
			formatEDIFile(control, "~", format);
			formatEDIFile(test, "~", format);

			//Apply masks to control and test file
			if (maskFileExists) {
				maskFile(controlFile, maskList, format);
				maskFile(testFile, maskList, format);
			}

			//Compare control and test files
			controlContents = fileToList(control);
			testContents = fileToList(test);
			Patch patch = DiffUtils.diff(controlContents, testContents);

			//Build list of differences.  Empty string if no differences.
			StringBuilder stringBuilder = new StringBuilder();
			for (Delta delta : patch.getDeltas()) {
				stringBuilder.append(delta.toString() + "\n");
			}

			return stringBuilder.toString();
		} catch (IOException | MaskFileException e) {
			throw e;
		}
	}

	/**
	 * Mask a file
	 * 
	 * @author Geoff Raetz
	 * @date 8/10/2015
	 * 
	 * @param filePath Source file path
	 * @param maskPath Mask file path
	 * @param compareType Type of comparison to mask for
	 * @return Status
	 */
	public static void mask(String filePath, String maskPath, String compareType)
		throws IOException, MaskFileException {

		try {
			if (!compareType.toUpperCase().equals("FLAT")) {
				throw new MaskFileException("Compare type must be 'FLAT'");
			}
			File file = new File(filePath);

			formatEDIFile(filePath, "~", "Cp1252");

			//Apply masks to file
			maskFile(file, fileToList(maskPath), "");

		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Mask a file
	 * 
	 * @author Geoff Raetz
	 * @date 8/10/2015
	 * 
	 * @param filePath Source file path
	 * @param maskPath Mask file path
	 * @param compareType Type of comparison to mask for
	 * @return Status
	 */
	public static void mask(String filePath, String maskPath, String compareType, boolean isEDI)
		throws IOException, MaskFileException {

		try {
			if (!compareType.toUpperCase().equals("FLAT")) {
				throw new MaskFileException("Compare type must be 'FLAT'");
			}
			File file = new File(filePath);

			if (isEDI) {
				formatEDIFile(filePath, "~", "Cp1252");
			}

			//Apply masks to file
			maskFile(file, fileToList(maskPath), "");

		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Mask a file with specific format
	 * 
	 * @author Geoff Raetz
	 * @date 8/10/2015
	 * 
	 * @param filePath Source file path
	 * @param maskPath Mask file path
	 * @param compareType Type of comparison to mask for
	 * @param format Format of the file
	 * @return Status
	 */
	public static void mask(String filePath, String maskPath, String compareType, String format)
		throws IOException, MaskFileException {

		try {
			if (!compareType.toUpperCase().equals("FLAT")) {
				throw new MaskFileException("Compare type must be 'FLAT'");
			}
			File file = new File(filePath);

			formatEDIFile(filePath, "~", format);

			//Apply masks to file
			maskFile(file,fileToList(maskPath), format);

		} catch (IOException e) {
			throw e;
		}
	}

	private static void formatEDIFile(String filePath, String lineDelimiter, String format) throws IOException {
		int lineNum = 0;
		String line;
		boolean isEDIFile = false;
		List<String> contents = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), format))) {
			while ((line = reader.readLine()) != null) {
				if ((lineNum == 0) && line.startsWith("ISA")) {
					//This is an EDI file, we need to parse it
					isEDIFile = true;
				}
				if (isEDIFile) {
					String[] segments = line.split(lineDelimiter);
					for (String segment : segments) {
						contents.add(segment);
					}
				} else {
					break;
				}
				lineNum++;
			}
			reader.close();
		} catch (Exception e) {
			throw e;
		}

		if (isEDIFile) {
			try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePath), format))) {
				for (String s : contents) {
					if (!StringUtils.chomp(s).equals("")) {
						writer.write(s);
						writer.newLine();
					}
				}
				writer.newLine();
				writer.flush();
				writer.close();
			} catch (Exception e) {
				throw e;
			}
		}
	}

	/**
	 * Apply a list of masks to a text file. It will apply all masks to each
	 * line of the file which will allow you to run multiple masks for each
	 * line.
	 * 
	 * @author Geoff Raetz
	 * @date 8/10/2015
	 * 
	 * @param file File to apply the masks
	 * @param maskList List of masks
	 * @return Status on if the operation was success or failure
	 */
	public static void maskFile(File file, List<String> maskList, String format) throws IOException, MaskFileException {
		int lineNum = 0;
		boolean addLine;
		String line, before, after, fill;
		List<String> contents = new ArrayList<String>();

		//Set a default format for compatibility which has been used for a while
		if (format.equals("")) {
			format = "Cp1252";
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), format))) {
			//Iterate over all lines in the file
			while ((line = reader.readLine()) != null) {
				lineNum++;
				addLine = true;
				//Iterate over all masks for each line
				if (!(maskList == null)) {
					for (String mask : maskList) {
						//Get mask information
						String[] maskInfo = mask.split(",");
						switch (maskInfo[0]) {
							//<LN> = Line number replacement
							case maskTypeLineNumber:
								if (lineNum == Integer.parseInt(maskInfo[1])) {
									before = line.substring(0, Integer.parseInt(maskInfo[2]));
									after = line.substring(Integer.parseInt(maskInfo[3]), line.length());

									char[] chars = new char[Integer.parseInt(maskInfo[3])
										- Integer.parseInt(maskInfo[2])];
									Arrays.fill(chars, maskChar);

									fill = new String(chars);
									line = before + fill + after;
								}
								break;
							//<SEG> = segment replacement
							case maskTypeSegment:
								if (line.startsWith(maskInfo[1])) {
									before = line.substring(0, Integer.parseInt(maskInfo[2]));
									after = line.substring(Integer.parseInt(maskInfo[3]), line.length());

									char[] chars = new char[Integer.parseInt(maskInfo[3])
										- Integer.parseInt(maskInfo[2])];
									Arrays.fill(chars, maskChar);

									fill = new String(chars);
									line = before + fill + after;
								}
								break;
							case maskTypeSegmentTab:
								if (line.startsWith(maskInfo[1])) {
									String[] sections = line.split("\t", -1);

									char[] chars = new char[Integer.parseInt(maskInfo[3])];
									Arrays.fill(chars, maskChar);

									fill = new String(chars);
									sections[Integer.parseInt(maskInfo[2])] = fill;
									line = join(sections, "\t");
								}
								break;
							case maskTypeSegmentCust:
								//<SEGCUST>,MST,10,5
								if (line.startsWith(maskInfo[2])) {
									String[] sections = line.split("\\" + maskInfo[1], -1);

									char[] chars = new char[Integer.parseInt(maskInfo[4])];
									Arrays.fill(chars, maskChar);

									fill = new String(chars);
									sections[Integer.parseInt(maskInfo[3])] = fill;
									line = join(sections, maskInfo[1]);
								}
								break;
							//needed another case where the line contains
							case maskTypeSegmentContains:
								if (line.contains(maskInfo[2])) {
									String[] sections = line.split("\\" + maskInfo[1], -1);

									char[] chars = new char[Integer.parseInt(maskInfo[4])];
									Arrays.fill(chars, maskChar);

									fill = new String(chars);
									sections[Integer.parseInt(maskInfo[3])] = fill;
									line = join(sections, maskInfo[1]);
								}
								break;

							case maskTypeAll:
								before = line.substring(0, Integer.parseInt(maskInfo[1]));
								after = line.substring(Integer.parseInt(maskInfo[2]), line.length());

								char[] chars = new char[Integer.parseInt(maskInfo[2]) - Integer.parseInt(maskInfo[1])];
								Arrays.fill(chars, maskChar);

								fill = new String(chars);
								line = before + fill + after;
								break;
							case maskTypeContains:
								if (line.contains(maskInfo[1])) {
									//Do not add this line if found
									addLine = false;
								}
								break;
							case maskTypeLineCont:
								if (line.contains(maskInfo[1])) {
									//Mask just part of the line
									before = line.substring(0, Integer.parseInt(maskInfo[2]));
									int startLast = Integer.parseInt(maskInfo[3]);
									if (startLast > line.length()) {
										startLast = line.length();
									}
									after = line.substring(startLast, line.length());

									char[] nchars = new char[Integer.parseInt(maskInfo[3])
										- Integer.parseInt(maskInfo[2])];
									Arrays.fill(nchars, maskChar);

									fill = new String(nchars);
									line = before + fill + after;
								}
								break;
							case maskTypeLength:
								if (line.contains(maskInfo[1])) {
									//If the line is not the length specified, add spaces
									int len = line.length();
									int targetLen = Integer.parseInt(maskInfo[2]);
									if (len < targetLen) {
										char[] nchars = new char[targetLen];
										Arrays.fill(nchars, ' ');
										for (int i = 0; i < line.length(); ++i) {
											nchars[i] = line.charAt(i);
										}
										line = new String(nchars);
									}
								}
								break;
							case maskTypeSegLength:
								if (line.contains(maskInfo[1])) {
									//If the line is not the length specified, add spaces
									int len = line.length();
									int targetLen = Integer.parseInt(maskInfo[2]);
									if (len < targetLen) {
										char[] nchars = new char[targetLen];
										Arrays.fill(nchars, '|');
										for (int i = 0; i < line.length(); ++i) {
											nchars[i] = line.charAt(i);
										}
										line = new String(nchars);
									}
								}
								break;
							case maskTypeBOM:
								if (lineNum == 1) {
									if (line.contains("﻿")) {
										//remove BOM characters
										line = line.substring(3, line.length());
									}
								}
								break;
							case maskTypeLineContPos:
								//<LINCONTPOS>, bar, 2, 4 , 6, 11
								if (line.length() >= Integer.parseInt(maskInfo[3])) {
									if (line.substring(Integer.parseInt(maskInfo[2]), Integer.parseInt(maskInfo[3]))
										.equals(maskInfo[1])) {
										before = line.substring(0, Integer.parseInt(maskInfo[4]));
										after = line.substring(Integer.parseInt(maskInfo[5]), line.length());

										char[] nchars = new char[Integer.parseInt(maskInfo[5])
											- Integer.parseInt(maskInfo[4])];
										Arrays.fill(nchars, maskChar);

										fill = new String(nchars);
										line = before + fill + after;
									}
								}
								break;
							default:
								throw new MaskFileException("Unknown file replacement instruction: " + maskInfo[0]);
						}
					}
				}
				if (addLine) {
					contents.add(line);
				}
			}
			reader.close();

			BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file.getPath()), format));
			for (String s : contents) {
				if (!StringUtils.chomp(s).equals("")) {
					writer.write(s);
					writer.newLine();
				}
			}
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Join strings together with a delimiter
	 * 
	 * @author Geoff Raetz
	 * @date 5/17/2014
	 * 
	 * @param input Array of String to join together
	 * @param delimiter Delimiter to join strings with
	 * @return One strings joined together with the delimiter
	 * 
	 */
	public static String join(String[] input, String delimiter) {
		try {
			StringBuilder sb = new StringBuilder();
			for (String value : input) {
				sb.append(value);
				sb.append(delimiter);
			}
			int length = sb.length();
			if (length > 0) {
				// Remove the extra delimiter
				sb.setLength(length - delimiter.length());
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Turns a file contents into a list, 1 list item per file line
	 * 
	 * @author Geoff Raetz
	 * @date 6/2/2015
	 * 
	 * @param filePath File name to turn contents into a list
	 * @return List of file contents
	 */
	public static List<String> fileToList(String filePath) throws IOException {
        List<String> lines = new LinkedList<String>();
        String line = "";
        
        try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
        	while ((line = in.readLine()) != null) {
        		lines.add(line);
        	}
        } catch (IOException e) {
        	throw e;
        }
        return lines;
	}

	public static void main(String[] args) {

		try {
			System.out.println(compare(
				System.getProperty("user.dir")
					+ "\\src\\main\\resources\\filecomparision\\qa_data.txt",
				System.getProperty("user.dir")
					+ "\\src\\main\\resources\\filecomparision\\prod_data.txt",
				"U:\\TTTT\\06-06-2019\\06-06-2019\\DATFILES\\GEN_GENxARv1_0\\GEN_GENxARv1_0.txt",
				"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
