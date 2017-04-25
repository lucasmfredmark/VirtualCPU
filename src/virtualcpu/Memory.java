package virtualcpu;

import java.io.PrintStream;

/**
 *
 * @author lucasmfredmark
 */
public class Memory {
    public static final int SIZE = 64;
    private final byte[] data = new byte[SIZE];

    public int get(int index) {
        return data[index];
    }

    public void set(int index, int value) {
        data[index] = (byte) (value & 0b1111_1111);
    }

    public String binary(int value) {
        String result = "";
        for (int i = 7; i >= 0; i--) {
            result += (value & (1 << i)) == 0 ? "0" : "1";
        }
        return result;
    }

    public void print(PrintStream out, int index) {
        out.printf("%2d: %4d %s   ", index, get(index), binary(get(index)));
    }

    public void print(PrintStream out) {
        for (int index = 0; index < 16; index++) {
            print(out, index);
            print(out, 16 + index);
            print(out, 32 + index);
            print(out, 48 + index);
            out.println();
        }
    }

}
