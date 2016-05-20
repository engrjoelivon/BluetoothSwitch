package aivco.com.bluetooth_exe;

/**
 * Created by joel on 5/19/16.
 * interface to be implemented by class that calls handle communication
 * it uses response method to send back the responce before closing.
 */
public interface ServerResponce {

    /**@param message the response message to be sent to the implementing class*/
    String response(String message);
}
