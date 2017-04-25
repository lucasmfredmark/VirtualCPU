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
            cpu.setFlag(cpu.getA() != cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0000_1100) {
            // 0000 1100 ALWAYS
            // Function: F ← true; IP++
            cpu.setFlag(true);
            cpu.incIp();
        } else if (instr == 0b0000_1101) {
            // 0000 1101
            // Function: Undefined
        } else if (instr == 0b0000_1110) {
            // 0000 1110
            // Function: Undefined
        } else if (instr == 0b0000_1111) {
            // 0000 1111 HALT
            // Function: Halts execution
            cpu.setRunning(false);
        } else if ((instr & 0b1111_1110) == 0b0001_0000) {
            // 0001 000r PUSH r
            // Function: [--SP] ← r; IP++
            
            int r = instr & 0b0000_0001;
            cpu.decSp();
            
            if (r == CPU.A) {
                memory.set(cpu.getSp(), cpu.getA());
            } else {
                memory.set(cpu.getSp(), cpu.getB());
            }
            
            cpu.incIp();
        } else if ((instr & 0b1111_1110) == 0b0001_0010) {
            // 0001 001r POP r
            // Function: r ← [SP++]; IP++
            
            int r = instr & 0b0000_0001;
            int m = memory.get(cpu.getSp());
            
            if (r == CPU.A) {
                cpu.setA(m);
            } else {
                cpu.setB(m);
            }
            
            cpu.incSp();
            cpu.incIp();
        } else if (instr == 0b0001_0100) {
            // 0001 0100 MOV A B
            // Function: B ← A; IP++
            cpu.setB(cpu.getA());
            cpu.incIp();
        } else if (instr == 0b0001_0101) {
            // 0001 0101 MOV B A
            // Function: A ← B; IP++
            cpu.setA(cpu.getB());
            cpu.incIp();
        } else if (instr == 0b0001_0110) {
            // 0001 0110 INC
            // Function: A++; IP++
            cpu.setA(cpu.getA() + 1);
            cpu.incIp();
        } else if (instr == 0b0001_0111) {
            // 0001 0111 DEC
            // Function: A--; IP++
            cpu.setA(cpu.getA() - 1);
            cpu.incIp();
        } else if ((instr & 0b1111_1000) == 0b0001_1000) {
            // 0001 1ooo RTN +o
            // Function: IP ← [SP++]; SP += o; IP++
            
            int o = instr & 0b0000_0111;
            
            cpu.setIp(memory.get(cpu.getSp()));
            cpu.incSp();
            cpu.setSp(cpu.getSp() + o);
            cpu.incIp();
        } else if ((instr & 0b1111_0000) == 0b0010_0000) {
            // 0010 rooo MOV r o
            // Function: [SP + o] ← r; IP++
            
            int r = (instr & 0b0000_1000) >> 3;
            int o = instr & 0b0000_0111;
            
            if (r == CPU.A) {
                memory.set(cpu.getSp() + o, cpu.getA());
            } else {
                memory.set(cpu.getSp() + o, cpu.getB());
            }
            
            cpu.incIp();
        } else if ((instr & 0b1111_0000) == 0b0011_0000) {
            // 0011 ooor MOV o r
            // Function: r ← [SP + o]; IP++
            
            int r = instr & 0b0000_0001;
            int o = (instr & 0b0000_1110) >> 1;
            int m = memory.get(cpu.getSp() + o);
            
            if (r == CPU.A) {
                cpu.setA(m);
            } else {
                cpu.setB(m);
            }
            
            cpu.incIp();
        } else if ((instr & 0b1100_0000) == 0b0100_0000) {
            // 01vv vvvr MOV v r
            // Function: r ← v; IP++
            
            int r = instr & 0b0000_0001;
            int v = (instr & 0b0011_1110) >> 1;
            
            if ((instr & 0b0010_0000) == 0b0010_0000) {
                v = -((instr & 0b0001_1110) >> 1);
            }
            
            if (r == CPU.A) {
                cpu.setA(v);
            } else {
                cpu.setB(v);
            }
            
            cpu.incIp();
        } else if ((instr & 0b1100_0000) == 0b1000_0000) {
            // 10aa aaaa JMP #a
            // Function: if F then IP ← a else IP++
            
            int a = instr & 0b0011_1111;
            
            if (cpu.isFlag()) {
                cpu.setIp(a);
            } else {
                cpu.incIp();
            }
        } else if ((instr & 0b1100_0000) == 0b1100_0000) {
            // 11aa aaaa CALL #a
            // Function: if F then [--SP] ← IP; IP ← a else IP++
            
            int a = instr & 0b0011_1111;
            
            if (cpu.isFlag()) {
                cpu.decSp();
                memory.set(cpu.getSp(), cpu.getIp());
                cpu.setIp(a);
            } else {
                cpu.incIp();
            }
        }
    }

    public void print(PrintStream out) {
        out.println("------------------------------------------------------------------------------");
        memory.print(out);
        out.println("------------------------------------------------------------------------------");
        cpu.print(out);
        out.println("---------");
    }

    public CPU getCpu() {
        return cpu;
    }

}
