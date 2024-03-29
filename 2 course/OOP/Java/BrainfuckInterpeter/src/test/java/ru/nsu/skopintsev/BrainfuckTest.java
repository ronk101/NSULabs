package ru.nsu.skopintsev;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class BrainfuckTest {
    private static final String testConfigPath = "src/test/resources/test_config.properties";

    @Test
    public void testCalculator() throws IOException {
        String program = """
                >>>>>>>>>>+<<<<<<<<<++++[->>>>>>>>>>>>++++++++<<<<<<<<<<<<]>>>>>>>>>>>>>++++++++
                ++<<<<<<<<<<<<<+++++++++++[>>>>>>>>>>>>>>++++++<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>.<<
                <<<<<<<<<<<<++++++++[>>>>>>>>>>>>>>++++++<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>.<<<<<<<<
                <<<<<<++++[>>>>>>>>>>>>>>----<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>-.++++++++.+++++.----
                ----.<<<<<<<<<<<<<<+++++[>>>>>>>>>>>>>>+++<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>.<<<<<<<
                <<<<<<<++++++[>>>>>>>>>>>>>>---<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>.++++++++.<<<<<<<<<
                <<<<<+++++++[>>>>>>>>>>>>>>-----------<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>++.<<<<<<<<<
                <<<<<+++++++[>>>>>>>>>>>>>>+++++<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>.<<<<<<<<<<<<<<+++
                +++[>>>>>>>>>>>>>>+++++<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>.+++++++++++.---------.<<<<
                <<<<<<<<<<++++++[>>>>>>>>>>>>>>+++<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>.---------.-----
                ------.<<<<<<<<<<<<<<++++[>>>>>>>>>>>>>>+++++<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>-.---
                --.+++.<<<<<<<<<<<<<<+++++++++[>>>>>>>>>>>>>>---------<<<<<<<<<<<<<<-]>>>>>>>>>>
                >>>>-.<<<<<<<<<<<<<<+++++++[>>>>>>>>>>>>>>---<<<<<<<<<<<<<<-]>>>>>>>>>>>>>>-.[-]
                <<<<<<+[>>>>>>>,[<<<<<<<<<<<<<<<+<+>>>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<[>>>>>>>>>>>
                >>>>+<<<<<<<<<<<<<<<-]++++++++[<---->-]<[>+<-]+>[<->[-]]<[[-]>-<]>+[->>>>>>>>>>>
                >>>>[<<<<<<<<<<<<<<<+<+>>>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<[>>>>>>>>>>>>>>>+<<<<<<<
                <<<<<<<<-]+++++++++++[<--------->-]<[>+<-]+>[<->[-]]<[[-]>>>>>>>>>>>>>>>++++++++
                ++.[-]++++++++++.[-]<..<<<<<->-<<<<<<<<<-<]>+[->>>>>>>>>>>>>>>[<<<<<<<<<<<<<<<+<
                +>>>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<[>>>>>>>>>>>>>>>+<<<<<<<<<<<<<<<-]++++++++++[<
                ------>-]-<[>+<-]+>[<->[-]]<[[-]>>>>>>>>>-<<<<<<<<-<]>+[->>>>>>>>>>>>>>>[<<<<<<<
                <<<<<<<<+<+>>>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<[>>>>>>>>>>>>>>>+<<<<<<<<<<<<<<<-]++
                +++++[<------>-]-<[>+<-]+>[<->[-]]<[[-]>>>>>>>>>>>>>>>>[<<<<<<<<<<<<+>>>>>>>>>>>
                >-]<<<<+<<<<<<<<<<<-<]>+[->>>>>>>>>>>>>>>[<<<<<<<<<<<<<<<+<+>>>>>>>>>>>>>>>>-]<<
                <<<<<<<<<<<<<[>>>>>>>>>>>>>>>+<<<<<<<<<<<<<<<-]+++++++++[<----->-]<[>+<-]+>[<->[
                -]]<[[-]>>>>>>>>>>>>>>>>[<<<<<<<<<<<<+>>>>>>>>>>>>-]<<<<+<<<<<<<<<<<-<]>+[->>>>>
                >>>>>>>>>>[<<<<<<<<<<<<<<<+<+>>>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<[>>>>>>>>>>>>>>>+<
                <<<<<<<<<<<<<<-]+++++++[<------>-]<[>+<-]+>[<->[-]]<[[-]>>>>>>>>>>>>>>>>[<<<<<<<
                <<<<<+>>>>>>>>>>>>-]<<<<+<<<<<<<<<<<-<]>+[->>>>>>>>>>>>>>>[<<<<<<<<<<<<<<<+<+>>>
                >>>>>>>>>>>>>-]<<<<<<<<<<<<<<<[>>>>>>>>>>>>>>>+<<<<<<<<<<<<<<<-]++++++[<--------
                >-]+<[>+<-]+>[<->[-]]<[[-]>>>>>>>>>>>>>>>>[<<<<<<<<<<<<+>>>>>>>>>>>>-]<<<<+<<<<<
                <<<<<<-<]>+[->>>>>>>>>>>>>>>[<<<<<<<<<<<<<<<+<+>>>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<
                [>>>>>>>>>>>>>>>+<<<<<<<<<<<<<<<-]++++++[<------>-]-<[>+<-]+>[<->[-]]<[[-]>>>>>>
                >>>>>>>>>>[<<<<<<<<<<<<+>>>>>>>>>>>>-]<<<<+<<<<<<<<<<<-<]>+[->>>>>>>>>>>>>>>[<<<
                <<<<<<<<<<<<+<+>>>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<[>>>>>>>>>>>>>>>+<<<<<<<<<<<<<<<
                -]++++++++[<------------>-]++<[>+<-]+>[<->[-]]<[[-]>>>>>>>>>>>>>>>>[<<<<<<<<<<<<
                +>>>>>>>>>>>>-]<<<<+<<<<<<<<<<<-<]>+[+++++++[->>>>>>>>>>>>>>>------<<<<<<<<<<<<<
                <<]>>>>>>>>>>>[<<<<<<<<<<<+<+>>>>>>>>>>>>-]<<<<<<<<<<<[>>>>>>>>>>>+<<<<<<<<<<<-]
                +[<->-]+<[>+<-]+>[<->[-]]<[[-]>>[<+>-]<[>++++++++++<-]>>>>>>>>>>>>>>>[<<<<<<<<<<
                <<<<+>>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<-<]>+[->>>>>>>>>>>[<<<<<<<<<<<+<+>>>>>>>>>>>
                >-]<<<<<<<<<<<[>>>>>>>>>>>+<<<<<<<<<<<-]+[<->-]<[>+<-]+>[<->[-]]<[[-]>>>[<<+>>-]
                <<[>>++++++++++<<-]>>>>>>>>>>>>>>>[<<<<<<<<<<<<<+>>>>>>>>>>>>>-]<<<<<<<<<<<<<<<-
                <]>+[->>>>>>>>>>>>>>++++++++++.[-]<<<<<+<<<<<<<<<]]]]]]]]]]]>>>>>>>>]<<<<<[<<<+<
                +>>>>-]<<<[>>>+<<<-]+++++++[<------>-]-<[>+<-]+>[<->[-]]<[[-]>>[>>>>>>>>>+<<<<<<
                <<<-]>[>>>>>>>>+<<<<<<<<-]<<<]>>>>[<<<+<+>>>>-]<<<[>>>+<<<-]+++++++[<------>-]<[
                >+<-]+>[<->[-]]<[[-]>>[->[<<+>>>>>>>>>>+<<<<<<<<-]<<[>>+<<-]>]<<]>>>>[<<<+<+>>>>
                -]<<<[>>>+<<<-]+++++++++[<----->-]<[>+<-]+>[<->[-]]<[[-]>>[>>>>>>>>>+<<<<<<<<<-]
                >[->>>>>>>>-<<<<<<<<]<<<]>>>>[<<<+<+>>>>-]<<<[>>>+<<<-]++++++[<-------->-]+<[>+<
                -]+>[<->[-]]<[[-]>>>[<<+>>>>>>>>+<<<<<<-]<<[>>+<<-]>[->>>>>>>-[<<<<<<<<+<+>>>>>>
                >>>-]<<<<<<<<[>>>>>>>>+<<<<<<<<-]<[[-]>-<]>+[-<+>]<[[-]>>>[<<+>>>>>>>>+<<<<<<-]<
                <[>>+<<-]>>>>>>>>>>+<<<<<<<<<<<]>>]<<]>>>>[<<<+<+>>>>-]<<<[>>>+<<<-]++++++[<----
                -->-]-<[>+<-]+>[<->[-]]<[[-]>>>[<<+>>>>>>>>+<<<<<<-]<<[>>+<<-]>[->>>>>>>-[<<<<<<
                <<+<+>>>>>>>>>-]<<<<<<<<[>>>>>>>>+<<<<<<<<-]<[[-]>-<]>+[-<+>]<[[-]>>>[<<+>>>>>>>
                >+<<<<<<-]<<[>>+<<-]<]>>]>[>>>>>>>>+<<<<<<<<-]>>>>>>[>>-<<-]<<<<<<<<<]>>>>[<<<+<
                +>>>>-]<<<[>>>+<<<-]++++++++[<------------>-]++<[>+<-]+>[<->[-]]<[[-]>>>>>>>>>>>
                +<<<<<<<<[>>>>>>>>[<<<<<<<<<[<+<+>>-]<[>+<-]>>>>>>>>>>-]<<<<<<<<<<<[>>>>>>>>>>>+
                <<<<<<<<<<<-]>>>-]<<<]>>>>>>>>>>[<<<<<<<<<+<+>>>>>>>>>>-]<<<<<<<<<[>>>>>>>>>+<<<
                <<<<<<-]<[[-]>>>>>>>>>>>>>.<<<<<<<++++++++++>>>>>[<<<<<<<<<<+>>>>>>>>>>>>>>>>+<<
                <<<<-]<<<<<<<<<<[>>>>>>>>>>+<<<<<<<<<<-]>>>>>>>>>>>>>>>>[-<<<<<<<<<<<-[<<<<<+<+>
                >>>>>-]<<<<<[>>>>>+<<<<<-]<[[-]>-<]>+[->>>>>>+<++++++++++<<<<<]>>>>>>>>>>>>>>>>]
                <<<<<<<<<<<<<<<<++++++++++>>>>>[-<<<<<->>>>>]<<<<<[->>>>>+<<<<<]>>>>>>[->>>>>>>>
                >>+<<<<<<<<<<]++++++++++>>>>>>>>>>[-<<<<<<<<<<-[<<<<<<+<+>>>>>>>-]<<<<<<[>>>>>>+
                <<<<<<-]<[[-]>-<]>+[->>>>>>>+<++++++++++<<<<<<]>>>>>>>>>>>>>>>>]<<<<<<<<<<<<<<<<
                ++++++++++>>>>>>[-<<<<<<->>>>>>]<<<<<<[->>>>>>+<<<<<<]>>>>>>[<<<<<<+>>>>+>>-]<<<
                <<<[>>>>>>+<<<<<<-]>>>>>>>[<<<<<<<+>>>>+>>>-]<<<<<<<[>>>>>>>+<<<<<<<-]>>>>>>>[<<
                <<<<<+<+>>>>>>>>-]<<<<<<<[>>>>>>>+<<<<<<<-]<[[-]>++++++++[->>>>>>>++++++<<<<<<<]
                >>>>>>>.<<<<<<<<]>>>>>[<<<<+<+>>>>>-]<<<<[>>>>+<<<<-]<[[-]>++++++++[->>>>>>+++++
                +<<<<<<]>>>>>>.<<<<<<<]>++++++++[->>>>>++++++<<<<<]>>>>>.[-]>[-]>[-]<<<<<<<<]""";

        String input = "5^3=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Brainfuck interpreter = new Brainfuck(testConfigPath, program);
        interpreter.interpret();


        String result = outputStream.toString();
        byte expected = 5 * 5 * 5;
        assertEquals("Brainfuck Calculator \n" + " " + expected, result);

    }
}
