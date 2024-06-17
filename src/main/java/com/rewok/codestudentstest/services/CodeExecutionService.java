package com.rewok.codestudentstest.services;

import com.rewok.codestudentstest.models.Tasks;


import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CodeExecutionService {

    private static final String SAVE_DIRECTORY = "D:/UsersProject/";

    private static String createUserDirectory(String username) throws IOException {
        String directoryPath = SAVE_DIRECTORY + username + "/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
            copyFilesToDirectory(directoryPath);
        }

        return directoryPath;
    }


    private static void copyFilesToDirectory(String directoryPath) {
        // Определяем пути к файлам, которые нужно скопировать
        String[] filesToCopy = {"D:/UsersProject/1.RESOURCES/SumTest.class"};

        for (String filePath : filesToCopy) {
            try {
                File sourceFile = new File(filePath);
                File destinationFile = new File(directoryPath + sourceFile.getName());
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String executeCode(String code, String testClass, String username) {

        try {
            String userDirectory = createUserDirectory(username);
            saveCodeToFile(code, username);
            String executionResult = executeCodeFromFile(userDirectory, "CodES.java", testClass);
            return executionResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка выполнения! \n" + e.getMessage();
        }
    }


    private static String executeCodeFromFile(String directoryPath, String fileName, String testClass) throws Exception {

        String filePath = directoryPath + fileName;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(filePath));
        JavaCompiler.CompilationTask task = compiler.getTask(new PrintWriter(outputStream), fileManager, diagnostics, null, null, compilationUnits);
        boolean success = task.call();

        fileManager.close();

        if (!success) {
            throw new Exception("Ошибка компиляции!\n" + outputStream.toString());
        }

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(directoryPath).toURI().toURL()});

        Class<?> cls = Class.forName(testClass, true, classLoader);/////////

        Method method = cls.getDeclaredMethod("main", String[].class);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(resultStream));

        method.invoke(null, (Object) new String[]{});

        System.out.flush();
        System.setOut(originalOut);

        return resultStream.toString();
    }





    private static String extractCodeFromRequest(String requestBody) {
        try {
            String[] parts = requestBody.split("=", 2);
            String encodedCode = parts[1].trim();
            String decodedCode = URLDecoder.decode(encodedCode, StandardCharsets.UTF_8.toString());
            return decodedCode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void saveCodeToFile(String requestBody, String username) throws IOException {
        String code = extractCodeFromRequest(requestBody);
        String userDirectory = createUserDirectory(username);
        File file = new File(userDirectory + "CodES.java");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(code.getBytes());
        }
    }



}
