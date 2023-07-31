package proto.serialization;

import proto.Grades.Student;
import proto.Grades.Subject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProtoSerialization {

    public static void main(String[] args)
    {
        try {
            new ProtoSerialization().testProto();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testProto() throws IOException {
        Student student = Student.newBuilder()
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

        byte[] serStudent = student.toByteArray();

        //write to file
        String filename = "student.ser";
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        fileOutputStream.write(serStudent);
        fileOutputStream.close();

        //read from file
        FileInputStream fileInputStream = new FileInputStream(filename);
        byte[] deserializedStudent = fileInputStream.readAllBytes();
        fileInputStream.close();

        //received and restored student
        Student restoredStudent = Student.parseFrom(deserializedStudent);
        System.out.println("Deserialized and restored student:");
        System.out.println("Student name: " + restoredStudent.getName());
        System.out.println("Subjects with grades: ");
        for (Subject subject : restoredStudent.getSubjectList()) {
            System.out.println("\tSubject name: " + subject.getName());
            System.out.println("\t\tGrades: " + subject.getGradesList());
        }
    }
}

