/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author jayso
 */
public class EventNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>EventNotFoundException</code> without
     * detail message.
     */
    public EventNotFoundException() {
    }

    /**
     * Constructs an instance of <code>EventNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EventNotFoundException(String msg) {
        super(msg);
    }
}
