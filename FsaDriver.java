import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FsaDriver {


    public static void main(String[] args) throws IOException, FsaFormatException {

        String fileName = "C:\\Apurva\\Event Driven Computing\\Assignment 2\\src\\example.fsa";
        String writerFileName = "C:\\Apurva\\Event Driven Computing\\Assignment 2\\src\\fsaWriter.txt";

        Fsa fsa = new FsaImpl();
        State s1 = fsa.newState("q1",10,10);
        State s2 = fsa.newState("q2",20,10);
        State s3 = fsa.newState("q3",20,10);
        s1.setInitial(true);
        s3.setFinal(true);
        Transition t1 = fsa.newTransition(s1,s2,"a");
        Transition t4 = fsa.newTransition(s1,s2,"?");
        Transition t2 = fsa.newTransition(s2,s3,"b");
        Transition t3 = fsa.newTransition(s1,s3,"a");

        fsa.removeState(s3);
        System.out.println(fsa);
        System.out.println(s1.getName()+" TF= "+s1.transitionsFrom()+" TT= "+s1.transitionsTo());
        System.out.println(s2.getName()+" TF= "+s2.transitionsFrom()+" TT= "+s2.transitionsTo());
        System.out.println(s3.getName()+" TF= "+s3.transitionsFrom()+" TT= "+s3.transitionsTo());

      /*  System.out.println(fsa);

        FsaReaderWriter frw = new FsaReaderWriter();
        FileReader r = null;
        FileWriter w = null;


        r = new FileReader(fileName);
        w = new FileWriter(writerFileName);

        frw.read(r, fsa);
        System.out.println(fsa);
        frw.write(w, fsa);
*/




        System.out.println("fsa driver");
    }

}
