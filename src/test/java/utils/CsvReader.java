package utils;

import dto.TestOrderData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CsvReader {

    public static List<TestOrderData> readOrders(String fileName) {

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        CsvReader.class
                                                .getClassLoader()
                                                .getResourceAsStream(fileName)
                                )
                        )
        ) {

            return reader.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(data -> new TestOrderData(
                            data[0],
                            data[1],
                            Double.parseDouble(data[2]),
                            data[3]
                    ))
                    .toList();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
