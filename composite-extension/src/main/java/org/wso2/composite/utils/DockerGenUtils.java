/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.composite.utils;

import org.wso2.composite.exceptions.DockerPluginException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * Util methods used for artifact generation.
 */
public class DockerGenUtils {

    private static final PrintStream error = System.err;
    private static final PrintStream out = System.out;

    /**
     * Extract the ballerina file name from a given file path.
     *
     * @param balxFilePath balx file path.
     * @return output file name of balx
     */
    public static String extractBalxName(String balxFilePath) {
        if (balxFilePath.contains(".balx")) {
            return balxFilePath.substring(balxFilePath.lastIndexOf(File.separator) + 1, balxFilePath.lastIndexOf("" +
                    ".balx"));
        }
        return null;
    }

    /**
     * Prints an Error message.
     *
     * @param msg message to be printed
     */
    public static void printError(String msg) {
        error.println("Info [composite plugin]: " + msg);
    }


    /**
     * Checks if a String is empty ("") or null.
     *
     * @param str the String to check, may be null
     * @return true if the String is empty or null
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * Write content to a File. Create the required directories if they don't not exists.
     *
     * @param context        context of the file
     * @param targetFilePath target file path
     * @throws IOException If an error occurs when writing to a file
     */
    public static void writeToFile(String context, String targetFilePath) throws IOException {
        File newFile = new File(targetFilePath);
        if (newFile.exists() && newFile.delete()) {
            Files.write(Paths.get(targetFilePath), context.getBytes(StandardCharsets.UTF_8));
            return;
        }
        if (newFile.getParentFile().mkdirs()) {
            Files.write(Paths.get(targetFilePath), context.getBytes(StandardCharsets.UTF_8));
            return;
        }
        Files.write(Paths.get(targetFilePath), context.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Recursively deletes a given directory or a file.
     *
     * @param path path to directory or file
     * @throws DockerPluginException if an error occurs while deleting
     */
    public static void deleteDirectory(String path) throws DockerPluginException {
        Path pathToBeDeleted = Paths.get(path);
        if (!Files.exists(pathToBeDeleted)) {
            return;
        }
        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new DockerPluginException("Unable to delete directory: " + path, e);
        }
    }
}
