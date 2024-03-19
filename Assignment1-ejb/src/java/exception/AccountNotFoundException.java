/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package exception;

/**
 *
 * @author jayso
 */
public class AccountNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AccountNotFoundException</code> without
     * detail message.
     */
    public AccountNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AccountNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
