package UDPUnicast;

import proto.Grades.Student;
import proto.Grades.Subject;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class QuoteClient {
    public static void main(String[] args) throws IOException {
        InetAddress address = InetAddress.getByName("localhost");
        int port = 4448;

        DatagramSocket socket = new DatagramSocket();

        Student student = Student.newBuilder()
                .setName("Jan Kowalski")
                .build();

        byte[] serializedStudent = student.toByteArray();

        // send request
        DatagramPacket packet = new DatagramPacket(serializedStudent, serializedStudent.length, address, port);
        System.out.println("Sending packet...");
        socket.send(packet);
        System.out.println("Sent.");

        // get response
        byte[] buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        System.out.println("Waiting for a response...");
        socket.receive(packet);

        int receivedDataLength = packet.getLength();
        byte[] deserializedStudent = Arrays.copyOf(packet.getData(), receivedDataLength);
        Student responseStudent = Student.parseFrom(deserializedStudent);

        // print response
        System.out.println("Answer from server:");
        System.out.println("Student name: " + responseStudent.getName());
        System.out.println("Subjects with grades:");
        for (Subject subject : responseStudent.getSubjectList()) {
            System.out.println("\tSubject name: " + subject.getName());
            System.out.println("\t\tGrades: " + subject.getGradesList());
        }
        socket.close();
    }
}