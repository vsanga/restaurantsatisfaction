package com.assignment.restaurantsatisfaction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class BootApp {
	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Please provide data file path & maxTimeLimit:");
			System.exit(0);
		}
		final String filePath = args[0];
		final int maxTime = Integer.valueOf(args[1]);

		ConfigurableApplicationContext context = SpringApplication.run(
				BootApp.class, args);
		BootApp app = context.getBean(BootApp.class);

		// Lists to hold satisfaction and time values
		List<Integer> satisfactionValues = new ArrayList<Integer>();
		List<Integer> timeValues = new ArrayList<Integer>();

		// Load data file
		app.loadDataFile(filePath, satisfactionValues, timeValues);

		// maxSatisfactionValue
		System.out.println(app.getMaxSatisfactionValue(satisfactionValues,
				timeValues, maxTime));
	}

	/**
	 * @param filePath
	 * @param satisfactionValues
	 * @param timeValues
	 */
	private void loadDataFile(String filePath,
			List<Integer> satisfactionValues, List<Integer> timeValues) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				satisfactionValues.add(Integer.valueOf(tokens[0]));
				timeValues.add(Integer.valueOf(tokens[1]));
			}
			System.out.println(satisfactionValues.size());
			System.out.println(timeValues.size());
		} catch (FileNotFoundException fne) {
			System.err.println("File Not exception:" + fne);
		} catch (IOException ioe) {
			System.err.println("IO exception:" + ioe);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.err
							.println("Exception while closing resources:" + e);
				}
			}
		}

	}

	/**
	 * @param satisfactionValues
	 * @param timeValues
	 * @param maxTime
	 * @return
	 */
	public Integer getMaxSatisfactionValue(List<Integer> satisfactionValues,
			List<Integer> timeValues, int maxTime) {

		return dpMax(maxTime, satisfactionValues, timeValues,
				satisfactionValues.size());
	}

	/**
	 * @param num1
	 * @param num2
	 * @return
	 */
	private int max(int num1, int num2) {
		return (num1 > num2) ? num1 : num2;
	}

	// Maximum satisfaction value for given timelimit maxTime
	/**
	 * @param maxTime
	 * @param satisfactionValues
	 * @param timeValues
	 * @param size
	 * @return
	 */
	private int dpMax(int maxTime, List<Integer> satisfactionValues,
			List<Integer> timeValues, int size) {

		if (size == 0 || maxTime == 0)
			return 0;

		// Exclude items with timelimit > maxLimit
		if (timeValues.get(size - 1) > maxTime)
			return dpMax(maxTime, satisfactionValues, timeValues, size - 1);

		// Return the max in the current subset
		else {
			return max(
					satisfactionValues.get(size - 1)
							+ dpMax(maxTime - timeValues.get(size - 1),
									satisfactionValues, timeValues, size - 1),
					dpMax(maxTime, satisfactionValues, timeValues, size - 1));
		}
	}

}
