package com.poojan.kothari;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.trim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author poojank
 *
 *         Utility class to replicate input CSV file and generate variety of
 *         data using keywords <code>${alpha}</code> for replacing it with alpha
 *         numeric string and <code>${num}</code> for replacing with current
 *         line number
 */
public class CSVGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(CSVGenerator.class);

	private static int numberOfRecords;
	private static String inputCSVFilePath;
	private static String outputCSVFilePath;

	private static final boolean HEADER_FLAG = true;
	private static final String ALPHA_NUMERIC_VARIABLE = "${alpha}";
	private static final String NUMERIC_VARIABLE = "${num}";

	// Instance variable representing allowed upper-case characters
	public static final String UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	// Instance variable representing allowed lower-case characters
	public static final String LOWER_CHAR = UPPER_CHAR.toLowerCase(Locale.ROOT);

	// Instance variable representing allowed special characters
	public static final String SPECIAL_CHAR = "~=+%^*/()[]{}/!@#$?|";

	// Instance variable representing allowed DIGITS
	public static final String DIGITS = "0123456789";

	// Instance variable representing string of combination of all allowed
	// characters
	public static final String ALPHA_NUMERIC_STRING = UPPER_CHAR + LOWER_CHAR + SPECIAL_CHAR + DIGITS;

	private static Random random = new SecureRandom();

	private static char[] validSymbols = ALPHA_NUMERIC_STRING.toCharArray();

	private static char[] bufferArray = new char[12];

	public static void main(String[] args) {
		processArgs(args);
		generateDataFiles();
	}

	private static void generateDataFiles() {
		File csvFile = new File(outputCSVFilePath);
		long startTime = System.nanoTime();
		try (FileWriter filewriter = new FileWriter(csvFile)) {
			final List<String> inputLines = parseInputFile();
			IntStream.range(0, inputLines.size()).forEach(index -> {
				try {
					if (index == 0 && HEADER_FLAG) {
						filewriter.write(inputLines.get(index) + "\n");
					} else {
						filewriter.write(generateLines(inputLines.get(index)));
						LOGGER.info("{} persisted in {}", ((index) * numberOfRecords), outputCSVFilePath);
					}
				} catch (Exception e) {
					LOGGER.error("Exception while generating CSV", e);
				}
			});
			long endTime = System.nanoTime();

			LOGGER.info("JOB Completed in time : {} Seconds.", ((endTime - startTime) / (1000000000)));
		} catch (Exception e) {
			LOGGER.error("Exception while generating CSV", e);
		}
	}

	private static String generateLines(String line) {
		StringBuilder data = new StringBuilder();
		if (isNotBlank(line)) {
			IntStream.range(0, numberOfRecords).forEach(
					index -> data.append(replace(replace(line, ALPHA_NUMERIC_VARIABLE, getRandomAlphaNumString()),
							NUMERIC_VARIABLE, Integer.toString(index))).append("\n"));
		}
		return data.toString();
	}

	private static List<String> parseInputFile() throws IOException {
		File csvFile = new File(inputCSVFilePath);
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			return br.lines().collect(Collectors.toList());
		}

	}

	private static void processArgs(String[] args) {
		if (isNotBlank(args[0])) {
			numberOfRecords = Integer.parseInt(trim(args[0]));
		}
		if (isNotBlank(args[1])) {
			inputCSVFilePath = trim(args[1]);
		}
		if (isNotBlank(args[2])) {
			outputCSVFilePath = trim(args[2]);
		}
	}

	private static String getRandomAlphaNumString() {
		IntStream.range(0, bufferArray.length)
				.forEach(index -> bufferArray[index] = validSymbols[random.nextInt(validSymbols.length)]);
		return new String(bufferArray);
	}

}
