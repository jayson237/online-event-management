/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author jayso
 */
public class RegistrationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>RegistrationNotFoundException</code>
     * without detail message.
     */
    public RegistrationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>RegistrationNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RegistrationNotFoundException(String msg) {
        super(msg);
    }
}
