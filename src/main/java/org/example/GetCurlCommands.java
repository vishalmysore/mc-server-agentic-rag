package org.example;

import com.t4a.processor.AIProcessingException;
import io.github.vishalmysore.debug.DebugCurlCommands;

import lombok.extern.java.Log;


import java.util.Map;

/**
 * This class is used to get the curl commands for debugging purposes and testing.
 * It uses the DebugCurlCommands utility to fetch the commands based on the provided arguments.
 * The main method prints out the curl commands in a key-value format.
 */
@Log
public class GetCurlCommands {
    public static void main(String[] args) throws AIProcessingException {
        Map<String, String> curlCommands = DebugCurlCommands.getCurlCommands(args);

        // Iterate and print the keys and values
        for (Map.Entry<String, String> entry : curlCommands.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }
}
