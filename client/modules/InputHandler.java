package laba6.client.modules;

import java.util.Scanner;

public class InputHandler {
    Scanner scanner;
    public InputHandler(Scanner scanner){
        this.scanner = scanner;
    }

    public String readLine(){
        if (scanner.hasNextLine()){
            return scanner.nextLine();
        } else {
            return "";
        }
    }

    public void close(){
        scanner.close();
    }
}
