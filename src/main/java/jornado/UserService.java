package jornado;

/**
 * A user service that can load a user object given a string-based ID.
 * @param <U>
 */
public interface UserService<U extends WebUser> {
    /**
     * Loads the user.
     * @param id the user key.
     * @return the user, if found. null otherwise.
     */
    U load(String id);
}
