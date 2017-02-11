package virtualcpu;

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
        Program program = new Program("00101001", "00001111", "10101010", "MOV B +3");
        Machine machine = new Machine();
        machine.load(program);
        machine.print(System.out);
        machine.tick();
        machine.print(System.out);

        for (int line : program) {
            System.out.println(">>> " + line);
        }
    }

}
