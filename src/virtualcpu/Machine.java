package virtualcpu;

import java.io.PrintStream;

/**
 *
 * @author lucasmfredmark
 */
public class Machine {
    private final CPU cpu = new CPU();
    private final Memory memory = new Memory();

    public void load(Program program) {
        int index = 0;
        for (int instr : program) {
            memory.set(index++, instr);
        }
    }

    public void tick() {
        int instr = memory.get(cpu.getIp());
        if (instr == 0b0000_0000) {
            // 0000 0000 NOP
            // Function: IP++
            cpu.incIp();
        } else if (instr == 0b0000_0001) {
            // 0000 0001 ADD
            // Function: A ← A + B; IP++
            cpu.setA(cpu.getA() + cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_0010) {
            // 0000 0010 MUL
            // Function: A ← A*B; IP++
            cpu.setA(cpu.getA() * cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_0011) {
            // 0000 0011 DIV
            // Function: A ← A/B; IP++
            cpu.setA(cpu.getA() / cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_0100) {
            // 0000 0100 ZERO
            // Function: F ← A = 0; IP++
            cpu.setFlag(cpu.getA() == 0);
            cpu.incIp();
        } else if (instr == 0b0000_0101) {
            // 0000 0101 NEG
            // Function: F ← A < 0; IP++
            cpu.setFlag(cpu.getA() < 0);
            cpu.incIp();
        } else if (instr == 0b0000_0110) {
            // 0000 0110 POS
            // Function: F ← A > 0; IP++
            cpu.setFlag(cpu.getA() > 0);
            cpu.incIp();
        } else if (instr == 0b0000_0111) {
            // 0000 0111 NZERO
            // Function: F ← A ≠ 0; IP++
            cpu.setFlag(cpu.getA() != 0);
            cpu.incIp();
        } else if (instr == 0b0000_1000) {
            // 0000 1000 EQ
            // Function: F ← A = B; IP++
            cpu.setFlag(cpu.getA() == cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_1001) {
            // 0000 1001 LT
            // Function: F ← A < B; IP++
            cpu.setFlag(cpu.getA() < cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_1010) {
            // 0000 1010 GT
            // Function: F ← A > B; IP++
            cpu.setFlag(cpu.getA() > cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_1011) {
            // 0000 1011 NEQ
            // Function: F ← A ≠ B; IP++
            cpu.setFlag(cpu.getA() > cpu.getB());
            cpu.incIp();
        }else if(instr == 0b0000_1100){
            //0000_1100 ALWAYS
            //F ← true; IP++
            cpu.setFlag(true);
            cpu.incIp();
        }else if(instr == 0b0001_0100) {
            //0001 0100 MOV A B 
            //B ← A; IP++
            cpu.setB(cpu.getA());
            cpu.incIp();
        } else if(instr == 0b0001_0101){
            //0001 0101	MOV B A
            //A ← B; IP++
            cpu.setA(cpu.getB());
            cpu.incIp();
        } else if(instr == 0b0001_0110){
        //0001 0110 INC
        //A++; IP++
        cpu.setA(cpu.getA()+1);
        cpu.incIp();
        }else if(instr == 0b0001_0111) {
        //0001 0111 DEC
        //A--; IP++
        cpu.setA(cpu.getA()-1);
        cpu.incIp();
        }else if((instr & 0b1111_1000) == 0b0001_1000) {
         //0001 1ooo RTN +o
         //IP ← [SP++]; SP += o; IP++
         cpu.setIp(memory.get(cpu.getSp()));
        }else if ((instr & 0b1111_0000) == 0b0010_0000) {
            // 0010 r ooo	MOV r o	   [SP + o] ← r; IP++

            // 0010 1 011 MOV B (=1) +3  [SP +3] // Move register B to memory position of SP with offset 3
            // 00101011 finding instruction
            //    and
            // 11110000
            // --------
            // 00100000
            // 00101011 finding offset
            //    and
            // 00000111
            // --------
            // 00000011 = 3
            // 00101011 finding register
            //    and
            // 00001000
            // --------
            // 00001000 = 8
            //    >> 3
            // 00000001 = 1
            int o = instr & 0b0000_0111;
            int r = (instr & 0b0000_1000) >> 3;
            if (r == cpu.A) {
                memory.set(cpu.getSp() + o, cpu.getA());
            } else {
                memory.set(cpu.getSp() + o, cpu.getB());
            }
            cpu.incIp();
        }
    }

    public void print(PrintStream out) {
        memory.print(out);
        out.println("-------------");
        cpu.print(out);
    }

}
