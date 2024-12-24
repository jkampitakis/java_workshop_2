import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        System.out.println("Enter your message:");
        /*
            A socket is an endpoint of communication between two computers.
            An endpoint is a combination of an IP address and a port number.
            Sockets are used to send and receive data.
         */

        /*
            TCP stands for Transmission Control Protocol.
            It is a connection-oriented protocol, meaning two computers form a connection before sending data. Every TCP connection can be identified by its two endpoints.

            check image tcp_vs_udp.png
         */

        /*
            A port is not hardware.
            A port contains information describing the service the two hosts want to communicate with.
             For example, port 80 is HTTP, 25 is SMTP, and 21 is FTP.
         */

        /*
            A stream is a sequence of data.
            There are two types: a character stream (usually used with text files) and a byte stream (usually used with images or binary files).
         */

        /*
            In Java, a character stream class will end with Reader or Writer (e.g., InputStreamReader).
            A byte stream will end with Stream (e.g., InputStream).
         */

        /*
            OutputStreamWriter and InputStreamReader are both bridges from byte streams to character streams.
            The underlying byte streams are the OutputStream and InputStream from the socket respectively.
         */

        /*
            Improve the efficiency of the program by using Buffers
            Speed the input / output operations by rather than writing one character at a time to the network or the disk it writes a large block at a time
            So, instead of reading one character at a time from the underlying reader the BufferedReader reads a larger block or an array at a time
         */

        /*
            Flushing a stream forces any buffered bytes to be written out (in the case of an output stream) or read in (in the case of an input stream).
            A buffer will only flush if full unless forced to through code.
         */

        /*
            We wrap the InputStreamReader with a BufferedReader just to improve efficiency.
            The contents of the buffer, when full, get flushed to the InputStreamReader, which then get flushed to the underlying InputStream.
         */

        /*
            CHECK img1.png for detailed explanation about BufferedReader
         */

        /*
             All bytes written to it will get put inside the buffer and the internal byte array of the BufferedWriter,
             when this buffer is full it will flush it to the OutputStreamWriter
         */

        try (
                Socket socket = new Socket("localhost", 1234); // Create a socket connection to localhost on port 1234
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Wrap InputStreamReader with BufferedReader for efficiency
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Wrap OutputStreamWriter with BufferedWriter for efficiency
                Scanner scanner = new Scanner(System.in) // System.in -> an InputStream connected to the keyboard to get input from console
        ) {
            while (true) {
                String msgToSend = scanner.nextLine(); // It is important to note that scanner.nextLine() leaves the line separator (\n) out of the string it returns.

                bufferedWriter.write(msgToSend); // The write() method of BufferedWriter writes the given argument, a String in this case, to the underlying writer, which writes it to the underlying output stream. Here, that is the OutputStreamWriter and OutputStream, respectively.
                bufferedWriter.newLine(); // The newLine() method of BufferedWriter writes a line separator to the underlying streams. This is important because the scanner’s nextLine() method does not include a line separator in the string it returns.
                // We need the newLine() because later we will use the BufferedReader and readLine() function that needs to know if the line has ended
                // The readLine() method of BufferedReader reads a line of text. A line is considered to be terminated by a line feed (enter key or \n), a carriage return (\r or tab key), or a carriage return followed immediately by a line feed.
                bufferedWriter.flush(); // A buffer flushes its stream in each of these cases: the buffer is full, the flush method is called, or the buffer stream is closed (the buffer is flushed before closing).
                // Buffers are normally used with large text files because the buffer flushes when full. The messages we are sending to the server will most likely not fill the buffer. But we want the efficiency of the buffer anyway.
                // We want the message to be sent when we press the ENTER key, not when the buffer is full!

                // Now we wait for the server to send a response
                String response = bufferedReader.readLine();
                System.out.println("Server says: " + response);

                // When the break keyword is encountered inside a loop in Java, the loop is immediately terminated, and the program control resumes at the next statement following the loop.
                // The loop here is the while (true).
                if (msgToSend.equals("bye")) {
                    break;
                }
            }

            System.out.println("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}