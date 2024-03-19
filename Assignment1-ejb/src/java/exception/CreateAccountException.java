/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author jayso
 */
public class CreateAccountException extends Exception {

    /**
     * Creates a new instance of <code>CreateAccountException</code> without
     * detail message.
     */
    public CreateAccountException() {
    }

    /**
     * Constructs an instance of <code>CreateAccountException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateAccountException(String msg) {
        super(msg);
    }
}
