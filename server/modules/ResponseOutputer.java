package laba6.server.modules;

/**
 * A class for generating responses to a client.
 */
public class ResponseOutputer {
    private static final StringBuilder stringBuilder = new StringBuilder();

    /**
     * Append object to out to the end of string.
     *
     * @param toOut Object to Out.
     */
    public static void append(Object toOut) {
        stringBuilder.append(toOut);
    }

    /**
     * Append line break to the end of string.
     */
    public static void appendln() {
        stringBuilder.append("\n");
    }

    /**
     * Append object to out and line break to the end of string.
     *
     * @param toOut Object to Out.
     */
    public static void appendln(Object toOut) {
        stringBuilder.append(toOut).append("\n");
    }

    /**
     * Append error description and line break to the end of string.
     *
     * @param toOut Error description.
     */
    public static void appenderror(Object toOut) {
        stringBuilder.append("error: ").append(toOut).append("\n");
    }

    /**
     * Takes a constructed string.
     *
     * @return Сonstructed string.
     */
    public static String getString() {
        return stringBuilder.toString();
    }

    /**
     * Takes a constructed string and clears the buffer.
     *
     * @return Сonstructed string.
     */
    public static String getAndClear() {
        String toReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return toReturn;
    }

}