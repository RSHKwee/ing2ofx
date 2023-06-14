package zandbak;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClassContentReader {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Read the class file bytes from storage
        String filePath = "path/to/your/file.class";
        byte[] classBytes = Files.readAllBytes(Path.of(filePath));

        // Define a custom class loader
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals("com.example.YourClass")) {
                    return defineClass(name, classBytes, 0, classBytes.length);
                }
                return super.findClass(name);
            }
        };

        // Load the class using the custom class loader
        Class<?> loadedClass = classLoader.loadClass("com.example.YourClass");

        // You can now work with the loaded class as needed
        System.out.println("Loaded class: " + loadedClass.getName());
    }
}
