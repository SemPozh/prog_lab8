package laba8.laba8.server.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class for generating responses to a client.
 */
public class ResponseOutputer {
    private static final StringBuilder stringBuilder = new StringBuilder();
    private static List<String> argList = new ArrayList<>();
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

    public static void appendargs(String... args) {
        argList.addAll(Arrays.asList(args));
    }

    public static String[] getArgsAndClear() {
        String[] argsAsArray = new String[argList.size()];
        argsAsArray = argList.toArray(argsAsArray);
        argList.clear();
        return argsAsArray;
    }

}