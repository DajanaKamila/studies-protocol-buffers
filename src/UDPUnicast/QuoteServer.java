package UDPUnicast;

import proto.Grades.Student;
import proto.Grades.Subject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class QuoteServer {
    public static void main(String[] args) throws IOException {
        new QuoteServerThread().start();
    }
}

class QuoteServerThread extends Thread {
    protected DatagramSocket socket = null;
    protected boolean moreQuotes = true;

    public QuoteServerThread() throws IOException {
        this("QuoteServerThread");
        socket = new DatagramSocket(4448);
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);
    }

    public void run() {
        while (moreQuotes) {
            try {
                byte[] buf = new byte[256];

                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                // create random answer
                Student responseStudent = Student.newBuilder()
                        .setName("James Bond")
                        .addSubject(Subject.newBuilder()
                                .setName("Math")
                                .addGrades(3)
                                .addGrades(3)
                                .addGrades(4)
                                .build())
                        .addSubject(Subject.newBuilder()
                                .setName("History")
                                .addGrades(4)
                                .addGrades(4)
                                .addGrades(5)
                                .build())
                        .build();

                // serialize and send response
                byte[] serializedResponse = responseStudent.toByteArray();
                packet = new DatagramPacket(serializedResponse, serializedResponse.length, packet.getAddress(), packet.getPort());
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}