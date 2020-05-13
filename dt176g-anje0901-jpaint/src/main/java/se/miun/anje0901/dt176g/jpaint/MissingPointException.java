package se.miun.anje0901.dt176g.jpaint;

/**
 * Custom exception class for Point-related exceptions.
 *
 * @author  Anders Jensen-Urstad (anje0901)
 * @version 1.0
 * @since   2018-11-07
 */
class MissingPointException extends Exception {
    MissingPointException(String message) {
        super(message);
    }
}
