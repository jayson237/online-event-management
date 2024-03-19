/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author jayso
 */
public class CreateEventException extends Exception {

    /**
     * Creates a new instance of <code>CreateEventException</code> without
     * detail message.
     */
    public CreateEventException() {
    }

    /**
     * Constructs an instance of <code>CreateEventException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateEventException(String msg) {
        super(msg);
    }
}
