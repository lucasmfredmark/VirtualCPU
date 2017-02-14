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
        
        Program program = new Program("00010001", "00101001", "00001111", "10101010", "MOV B +3");
        Machine machine = new Machine();
        machine.load(program);
        machine.print(System.out);
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n-> Press [ENTER] to run next instruction");
        String input = scanner.nextLine();
        
        while (!input.equalsIgnoreCase("quit")) {
            machine.tick();
            machine.print(System.out);
            System.out.println("\n-> Press [ENTER] to run next instruction");
            input = scanner.nextLine();
        }
    }

}
