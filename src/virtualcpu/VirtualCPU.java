package virtualcpu;

import java.util.Scanner;

/**
 *
 * @author lucasmfredmark
 */
public class VirtualCPU {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the awesome CPU program");
        
        Program factorial = new Program("01001010", "00010000", "00001100", "11000110", "00010010", "00001111", "00110010", "00000111", "10001100", "01000010", "00100001", "00011000", "00010000", "00010111", "00010000", "00001100", "11000110", "00010011", "00010010", "00000010", "00100001", "00011000");
        Program calculus = new Program("01001010", "00010000", "01010110", "00010000", "01111010", "00010000", "00001100", "11001010", "00010010", "00001111", "00110111", "00110100", "00000001", "00110011", "00000010", "00100011", "00011010");
        
        Machine machine = new Machine();
        machine.load(factorial);
        //machine.load(calculus);
        machine.print(System.out);
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n-> Press [ENTER] to run next instruction");
        String input = scanner.nextLine();
        
        while (!input.equalsIgnoreCase("quit")) {
            machine.tick();
            
            if (!machine.getCpu().isRunning()) {
                break;
            }
            
            machine.print(System.out);
            System.out.println("\n-> Press [ENTER] to run next instruction");
            input = scanner.nextLine();
        }
    }

}
